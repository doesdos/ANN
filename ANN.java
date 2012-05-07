import java.util.Arrays;
import static org.junit.Assert.*;
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

        // account for bias input  always 1
        this.inputDimension = input_dimension+1;

        this.numHidden = num_hidden+1;
        // row major
        this.hiddenWeights = new double[numHidden][inputDimension];
        this.deltaHidden = new double[numHidden][inputDimension];
        //this.deltaHiddenMem = new double[numHidden][inputDimension];
        this.hiddenOut =  new double[numHidden];
        //bias input is always 1
        this.hiddenOut[0]= -1;

        this.output = null;

        this.numOutputClasses = numOutputClasses;
        // row major
        this.outputWeights = new double[numOutputClasses][numHidden];
        this.deltaOut = new double[numOutputClasses][numHidden];
        //this.deltaOutMem = new double[numOutputClasses][numHidden];

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
        double [] epochError = new double[epochMax]; 
        double tempError = 0;

        while(time<epochMax){
            for(int x=0; x<input.length; x++){
               double [] e =  this.feedForward(input[x]);
                // check the error of the single input
                singleError = this.calcError(e, expected[x]);
                tempError += singleError;
            
            System.out.println("Vector Error is "+ singleError);
                this.compute(input[x], expected[x]);
                this.adjust();
            } 
            System.out.println("Epoch Error is "+ tempError);
            epochError[time] = tempError;
            tempError= 0;
            time++;
        }
        this.printResults();
        return epochError;
    }

    public void printResults(){
        // dont want to mess ordering of current output;
        double [] temp = Arrays.copyOf(this.output, this.output.length);
        for(int i=0;i<temp.length; i++){
            System.out.println("Class: "+i+" prob: "+temp[i]);
        }
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
                hiddenWeights[h][j] = (Math.random()*2)-1;//(Math.random() * (0.03 - .01))-0.01;
            }
            for(int x=0; x<this.numOutputClasses; x++){
                // [output][hidden]
                outputWeights[x][h] = (Math.random()*2)-1;//(Math.random() * (0.03 - 0.01))-0.01;
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
            sumWeight += -1 * hiddenWeights[h][inputDimension-1];
            this.hiddenOut[h] = this.sigmoid(sumWeight);
        }
    }

    public double [] calcOutputFromHidden(){
        this.output = new double[numOutputClasses];
        // used for soft max
        //double total = 0;
        double [] preSoftmaxOutput = new double[numOutputClasses];
        double sumWeight = 0;
        /* default weight again */

        
        for(int i=0; i< this.numOutputClasses; i++){
            sumWeight = 0;
            for(int h=0; h< this.numHidden; h++){
                sumWeight += hiddenOut[h] * outputWeights[i][h];
            }
            preSoftmaxOutput[i] = sumWeight;
            System.out.println("sumWeight is"+sumWeight);
        }
            this.output = this.softmax(preSoftmaxOutput);
        /*
            // denominator of softmax
            total +=  Math.exp(sumWeight);
        }

        if ( Double.isInfinite(total))
            throw new ArithmeticException("denominator == infinite!");
        for(int i=0; i< this.numOutputClasses; i++){
            double var;
            // softmax to output 
            var = Math.exp(preSoftmaxOutput[i]) / total;
            //System.out.println("softmax total is " +total);
            //System.out.println("softmax is " +var);
            this.output[i] = var;
         }*/
        return output;
    }

    public void calcDeltaOutput(double [] expected){
        for(int i = 0;i<this.numOutputClasses; i++){
            for(int h =0;h< this.numHidden; h++){
                 this.deltaOut[i][h] = this.learningRate *(expected[i] - this.output[i]) * hiddenOut[h];
             }
         }
     }


    public void calcDeltaHidden(double [] input, double [] expected){
         // speedups, use previous calcDeltaOutput loop??
         for(int h=1;h<this.numHidden; h++){
             double sum = 0;
             for(int i=0; i<this.numOutputClasses; i++){
                 sum = sum + ((expected[i] - this.output[i]) * outputWeights[i][h]);
             }
             for(int j=0; j<this.inputDimension; j++){
                 // account for bias input
                 if(j==inputDimension-1){
                     this.deltaHidden[h][j] = (this.learningRate * sum * this.hiddenOut[h]* (1-this.hiddenOut[h]) * -1);
                 } else{
                         this.deltaHidden[h][j] = (this.learningRate * sum * this.hiddenOut[h]* (1-this.hiddenOut[h]) * input[j]);
                 }
             }
         }
     }

    public void updateOutputWeight(){
         for(int i=0; i<this.numOutputClasses; i++){
             for(int h=0; h<this.numHidden; h++){
                 this.outputWeights[i][h] +=  this.deltaOut[i][h];
                 //+ (this.momentum*deltaOut[i][h]);
                 //TODO momentum
             }
         }
     }

    public void updateHiddenWeight(){
         for(int h=1; h<this.numHidden; h++){
             for(int j=0; j<this.inputDimension; j++){
                 this.hiddenWeights[h][j] +=  deltaHidden[h][j];
                 // +(deltaHidden[h][j]* this.momentum); 
                 //TODO momentum
             }
         }
     }

    /*   threshold function for hidden output */
    public double sigmoid(double x) {
        assertTrue(x !=0.0);
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
        if ( Double.isNaN(expSum)) throw new ArithmeticException("denominator == 0!");
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
    /**
     * Implementation of a Kahan summation reduction in plain Java
     */
    public float kahanSum(float data[])
    {
        float sum = data[0];
        float c = 0.0f; 
        for (int i = 1; i < data.length; i++)
        {
            float y = data[i] - c;  
            float t = sum + y;      
            c = (t - sum) - y;  
            sum = t;           
        }
        return sum;
    }


    public double [] checkArray(double[] input){
        if(input[input.length-1]!=0.0){
            double[] temp = Arrays.copyOf(input,input.length+1);
            temp[temp.length-1] = 1;
            return temp;
        }
        else{
            return input;
        }
    }

}
