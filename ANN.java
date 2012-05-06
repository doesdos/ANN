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
    public double[][] deltaOutMem, deltaHiddenMem;

    /* The learning rate should be something around .1
     * the momentum is again around the .1 mark,
     * and errorConverganceThreshold is the error you will except if you 
     * want to terminate early */
    public ANN(int inputDimension, int numHidden, int numOutputClasses,
                // double momentum, double errorConverganceThreshold
               int epochMax, double learningRate){

        this.inputDimension = inputDimension;

        this.numHidden = numHidden;
        // row major
        this.hiddenWeights = new double[numHidden][inputDimension];
        this.deltaHidden = new double[numHidden][inputDimension];
        this.deltaHiddenMem = new double[numHidden][inputDimension];
        double [] hiddenOut =  new double[numHidden];

        this.numOutputClasses = numOutputClasses;
        // row major
        this.outputWeights = new double[numOutputClasses][numHidden];
        this.deltaOut = new double[numOutputClasses][numHidden];
        this.deltaOutMem = new double[numOutputClasses][numHidden];
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
        int m = input[0].length;
        int i;
        double[] temp1, temp2;
        while(m>0){
            //pick random remaining element
            i = (int)Math.floor((Math.random() *m-- ));
        System.out.println("THE ARRAY INDEX IS "+m);
            temp1 = input[m];
            temp2 = expected[m];

            input[m]= input[i];
            expected[m] = expected[i];

            input[i] = temp1;
            expected[i] = temp2;
        }

        // start the training 
        // TODO terminate early
        i = 0;
        m = input[0].length;
        while(i<epochMax){
            double tempError = 0;
            while(m>=0){
                this.feedForward(input[m]);
                // check the error of the single input
                tempError += this.calcError(this.output, expected[m]);
                this.compute(input[m], expected[m]);
                this.adjust();
                m--;
            }
            m = input[0].length;
            error[i] = tempError;
        }
            return error;
    }

    public void feedForward(double [] input ){
        this.calcHiddenFromInput(input);
        this.calcOutputFromHidden();
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
        for(int h=1; h<this.numHidden; h++){
            for(int j=0; j<this.inputDimension; j++){
                // [hidden][input]
                hiddenWeights[h][j] = (0.01 + (Math.random() * (1 - 0.01)));
            }
            for(int x=1; h<this.numOutputClasses; h++){
                // [output][hidden]
                outputWeights[x][h] = (0.01 + (Math.random() * (1 - 0.01)));
            }
        }
    }

    public void  calcHiddenFromInput(double [] input){
        /* this is some sort of default weight as suggested in paper */
        hiddenWeights[0][0] = 1;
        double sumWeight = 0;

        for(int h=1; h<this.numHidden; h++){
            for(int j=0; j<this.inputDimension; j++){
                sumWeight += input[j] * hiddenWeights[h][j];
            }

            this.hiddenOut[h] = this.sigmoid(sumWeight);
        }
    }

    public void calcOutputFromHidden(){
        // used for soft max
        double total = 0;
        double [] preSoftmaxOutput = new double[numOutputClasses];
        double sumWeight = 0;
        /* default weight again */
        outputWeights[0][0] = 1;

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
    }

     public void calcDeltaOutput(double [] expected){
         for(int i = 1;i<this.numOutputClasses; i++){
             for(int h =0;h< this.numHidden; h++){
                 deltaOut[i][h] =  this.learningRate *(expected[i] - output[i])* hiddenOut[h];
             }
         }
     }

     public void calcDeltaHidden(double [] input, double [] expected){
         // damn this has a cubic runtime not fast at all.
         // speedups, use previous calcDeltaOutput or something??
         for(int h=1;h<this.numHidden; h++){
             for(int j=0; j<this.inputDimension;j++){
                 double temp = 0;
                 for(int i=1; i<this.numOutputClasses; i++){
                     temp += (expected[i] - output[i]) * outputWeights[i][h];
                 }
                 deltaHidden[h][j] = this.learningRate * temp * hiddenOut[h]* (1-hiddenOut[h]) * input[j];
             }
         }
     }

     public void updateOutputWeight(){
         for(int i=1; i<this.numOutputClasses; i++){
             for(int h=0; h<this.numHidden; h++){
                 outputWeights[i][h] =
                     outputWeights[i][h] + deltaOut[i][h];//+ (this.momentum*deltaOut[i][h]);
                 //TODO momentum
             }
         }
     }
     public void updateHiddenWeight(){
         for(int h=1; h<this.numHidden; h++){
             for(int j=0; j<this.inputDimension; j++){
                 hiddenWeights[h][j] = 
                     hiddenWeights[h][j] + deltaHidden[h][j];// +(deltaHidden[h][j]* this.momentum); 
                 //TODO momentum
             }
         }
     }
    /*  node threshold function that detrimens the nodes output */
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
