/* Author: Matthew Clemens
 * Tite: Artifical Neural Network
 * Description: A machine learning algorithm build for my cs13 class,
 * which focused on understanding object orientated principles.
 *
 * The AnnNet class coordinates the all layers and provides the methods 
 * that teach the network through backpropagation (gradient descent).
 *
 * Layer provides the interface that each layer must abide by.
 *
 * Neuron is a class used in the HiddenLayer and it holds and
 * can manipulate its weights.
 *
 * InputLayer is designed for extending its methods to adapt to different
 * data problems.
 *
 * HiddenLayer contains the Neuron class and utilizes it's methods
 * for it's data processing sigmoid function
 *
 * OutputLayer is the final layer that does not use neurons, instead 
 * it manipulates just raw arrays, and does preprocessing with the 
 * softmax function for probabilties in the output.
 *
 */
public class AnnNet {


    public static void main(String [] args){

    }


    public double error(double[] result, double[] target) {
        double error = 0.0;
        for (int i=0; i<result.length; i++) {
            double diff = result[i]-target[i];
            error += diff*diff;
        }
        return error*0.5; 
    }

    public void gradientUpdate(){
    }

    
}
