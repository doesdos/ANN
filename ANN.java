/* Author: Matthew Clemens
 * Project: Neural Net CS13 
 *
 * Class for the far less complicataed Neural Network
 * Uses only a main  loop and its combined methods to;
 * 1. Feed the data in , sum weights, and determine threshold activations,
 * 2. Compute the deltas of the layers,
 * 4. Then back propagate, adjusting the weights,  using graident descent.
 */
public class ANN {

    /* settings */
    int inputDimension, numHidden, numOutputClasses, epochMax;
    double learningRate, momentum, errorConverganceThreshold;

    /* weights to be fitted  */
    public double [][]  hiddenWeights, outputWeights;
    /*  output (number of k classes of output  */
    public double [] hiddenOut, output;

    /* deltas to be calculated using  gradient descent on the weight functions */
    public double[][] deltaOut, deltaHidden;

    /* deltas cached for use with momentum adjustments */
    //public double[][] deltaOutMem, deltaHiddenMem;

    /* The learning rate should be something around .1
     * the momentum is again around the .1 mark,
     * and errorConverganceThreshold is the error you will except if you 
     * want to terminate early */
    public ANN(int input_dimension, int num_hidden, int numOutputClasses, int epochMax, double learningRate){
                // double momentum, double errorConverganceThreshold

        // account for bias input at x_0
        this.inputDimension = input_dimension+1;

        this.numHidden = num_hidden;
        // row major
        this.hiddenWeights = new double[numHidden][inputDimension];
        this.deltaHidden = new double[numHidden][inputDimension];
        //this.deltaHiddenMem = new double[numHidden][inputDimension];
        this.hiddenOut =  new double[numHidden];
        //bias input is always 1
        this.hiddenOut[0]=1;

        this.numOutputClasses = numOutputClasses;
        // row major
        this.outputWeights = new double[numOutputClasses][numHidden];
        this.deltaOut = new double[numOutputClasses][numHidden];
        //this.deltaOutMem = new double[numOutputClasses][numHidden];
        this.output = new double[numOutputClasses];

        /* options*/
        this.learningRate = learningRate;
        this.epochMax = epochMax;
        //this.errorConverganceThreshold = errorConverganceThreshold;
        //this.momentum = momentum;
        // initlize the randomness
        this.setWeights();
    }

    /* runs through the neural net for all of the inputs, for a most epochMax 
     * Returns the summed error over all all class and all inputs error, and
     * incremental non zero values count the number of iterations run */
    public double [] offlineLearning(double [][] input, double [][] expected){
        double [] error = new double[epochMax]; 
        
        // Fisher yates inplace shuffle of input and expected
        int m = input.length;
        System.out.println("The Number of vectors is "+ m);
        int i;
        double[] temp1, temp2;
       
        while(m>0){
            //pick random remaining element
            i = (int)Math.floor((Math.random() *m-- ));
            temp1 = input[m];
            temp2 = expected[m];

            input[m]= input[i];
            expected[m] = expected[i];

            input[i] = temp1;
            expected[i] = temp2;
        }

        // start the training 
        // TODO terminate early
        int time = 0 ;
        double singleError;
        while(time<epochMax){
            double tempError = 0;
            for(int x=0; x<input.length; x++){
               double [] e =  this.feedForward(input[x]);
                // check the error of the single input
                singleError = this.calcError(e, expected[x]);
                System.out.println("Single Error is "+ singleError);
                tempError += singleError;
            
                this.compute(input[x], expected[x]);
                this.adjust();
            } 
            error[time] = tempError;
            time++;
        }
            return error;
    }

    public double [] feedForward(double [] input ){
        this.calcHiddenFromInput(input);
        return this.calcOutputFromHidden();
    }

    public void compute(double [] input, double [] expected ){
        this.calcDeltaOutput(expected);
        this.calcDeltaHidden(input, expected);
    }

    public void adjust(){
        this.updateOutputWeight();
        this.updateHiddenWeight();
    }

    public void setWeights(){
        /*  Random double (0,1) rather than [0,1), 
        * and really close to 1 source: http://bit.ly/JoWZRw */
        for(int h=0; h<this.numHidden; h++){
            for(int j=0; j<this.inputDimension; j++){
                // [hidden][input]
                //hiddenWeights[h][j] = (0.01 + (Math.random() * (1 - 0.01)));
                hiddenWeights[h][j] =  (Math.random() * 2)-1;
            }
            for(int x=0; x<this.numOutputClasses; x++){
                // [output][hidden]
                //outputWeights[x][h] = (0.01 + (Math.random() * (1 - 0.01)));
                outputWeights[x][h] = (Math.random() * 2)-1;
            }
        }
    }

    public void setWeightsToOne(){
        for(int h=0; h<this.numHidden; h++){
            for(int j=0; j<this.inputDimension; j++){
                // [hidden][input]
                hiddenWeights[h][j] = 1;
            }
            for(int x=0; x<this.numOutputClasses; x++){
                // [output][hidden]
                outputWeights[x][h] = 1;
            }
        }
    }

    public void calcHiddenFromInput(double [] input){
        double sumWeight = 0;
        for(int h=1; h<this.numHidden; h++){
            for(int j=0; j<this.inputDimension-1; j++){
                sumWeight += input[j] * hiddenWeights[h][j];
            }
            /* this is some sort of default weight as suggested in paper */
            sumWeight += 1 * hiddenWeights[h][inputDimension-1];
            this.hiddenOut[h] = this.sigmoid(sumWeight);
        }
    }

    public double [] calcOutputFromHidden(){
        // used for soft max
        double total = 0;
        double [] preSoftmaxOutput = new double[numOutputClasses];
        double sumWeight = 0;
        /* default weight again */
        this.hiddenOut[0] = 1;

        for(int i=1; i< this.numOutputClasses; i++){
            for(int h=0; h< this.numHidden; h++){
                sumWeight += hiddenOut[h] * outputWeights[i][h];
            }
            preSoftmaxOutput[i] = sumWeight;
            // denominator of softmax
            total +=  Math.exp(sumWeight);
        }
        for(int i=1; i< this.numOutputClasses; i++){
            /* softmax to output */
            this.output[i] = Math.exp(preSoftmaxOutput[i]) / total;
         }
        return output;
    }

    public void calcDeltaOutput(double [] expected){
        for(int i = 1;i<this.numOutputClasses; i++){
            for(int h =0;h< this.numHidden; h++){
                 deltaOut[i][h] = this.learningRate *(expected[i] - output[i])* hiddenOut[h];
             }
         }
     }


    public void calcDeltaHidden(double [] input, double [] expected){
         // damn this has a cubic runtime not fast at all.
         // speedups, use previous calcDeltaOutput or something??
         for(int h=1;h<this.numHidden; h++){
             for(int j=0; j<this.inputDimension-1; j++){
                 double temp = 0;
                 for(int i=1; i<this.numOutputClasses; i++){
                     temp += (expected[i] - output[i]) * outputWeights[i][h];
                 }
                 temp += (expected[i] - 1) * outputWeights[i][h];
                 deltaHidden[h][j] = this.learningRate * temp * hiddenOut[h]* (1-hiddenOut[h]) * input[j];
             }
         }
     }

    public void updateOutputWeight(){
         for(int i=1; i<this.numOutputClasses; i++){
             for(int h=0; h<this.numHidden; h++){
                 outputWeights[i][h] = outputWeights[i][h] + deltaOut[i][h];
                 //+ (this.momentum*deltaOut[i][h]);
                 //TODO momentum
             }
         }
     }

    public void updateHiddenWeight(){
         for(int h=1; h<this.numHidden; h++){
             for(int j=0; j<this.inputDimension-1; j++){
                 hiddenWeights[h][j] = hiddenWeights[h][j] + deltaHidden[h][j];
                 // +(deltaHidden[h][j]* this.momentum); 
                 //TODO momentum
             }
         }
     }

    /*   threshold function for hidden output */
    public double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    /* Standalone Softmax function  for  transformation
     * to a probabilty between 0 and 1.
     * NOTE: not used because it repeats a loop that can be
     * done in the calcOutputFromHidden */
    public double[] softmax(double[] inputs) {
        //checkInputLength(inputs);
        double[] outputs = new double[inputs.length];
        double expSum = 0.0;
        for (int i=0; i<inputs.length; i++) {
            expSum += Math.exp(inputs[i]);
        }
        for (int i=0; i<outputs.length; i++) {
            outputs[i] = Math.exp(inputs[i])/expSum;
        }
        return outputs;
     }

    public double calcError(double result, double target) {
        double diff;
        diff = result-target;
        return diff*diff*.5;
    }
    
     /*Sum of squares error for one epoch of data */
    public double calcError(double[] result, double[] target) {
        double error = 0.0;
        for (int i=0; i<result.length; i++) {
            double diff = result[i]-target[i];
            error += diff*diff;
        }
        return error*0.5; 
    }
}
