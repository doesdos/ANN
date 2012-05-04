/* The Artifical Neural Network that coordinates the input layer , 
 * hidden layer(s), output layer 
 */
public class AnnNet {

    public double error(double[] result, double[] target) {
        double error = 0.0;
        for (int i=0; i<result.length; i++) {
            double diff = result[i]-target[i];
            error += diff*diff;
        }
        return error*0.5; 
    }
    
}
