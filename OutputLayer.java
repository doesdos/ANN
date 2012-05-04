/* Layer class that contains neurons dependent on its input, 
 * and each may have  different types of output functions.
 */
public class OutputLayer implements Layer{

    private double[] weights;
    private double[] initWeights;
    private boolean calComplete;
    private Layer parent;
    private String layer_id;

    public String p_id;
    public int size;

    public OutputLayer(String id, int size){
        this.layer_id = id;
        this.size = size;
        this.calComplete = false;
        this.weights = new double[size];
        this.initWeights = new double[size];
        for(double w: this.initWeights){
            // Taken from http://bit.ly/JoWZRw 
            // Random double (0,1) rather than [0,1) 
            w = 0.01 + (Math.random() * (1 - 0.01));
        }
    }

    /* this identifies this layer from others */  
    public String getId(){
        return this.layer_id;
    }

    /* Used to connect this layers input to parent layers calc functions 
     * Params: Layer p, the layer you want the data to come from */
    public void connectToParent(Layer p){
        this.p_id = p.getId();
        this.parent = p;
    }

    /* Is this layer done completing its maniplation on its input data? */
    public boolean isComplete(){
        return this.calComplete;
    }

    /* Can this layer get the data from the parent data feeding it? */
    public boolean isReady(){
        return this.parent.isComplete();
    }

    public double[] calc(){
        double output[] = new double[this.size]; 
        double parent_data[] =null;
        // if not ready tell that stupied parent to calc his first
        while(!isReady()){
                // recursive overhead ??
                parent_data = this.parent.calc();
        }
        // parent has done a good job and passed his data along
        for(int w=0; w<this.weights.length; w++){
            output[w] = this.initWeights[w];
            for(int d=0; d<parent_data.length; d++ ){
                output[w] += parent_data[d] *this.weights[w];
            }
        }

        /* final transformation */
        output = softmax(output);
        this.calComplete= true;
        return output;
    }
    /* Softmax function */
     public double[] softmax(double[] inputs) {
    //        checkInputLength(inputs);
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
     /* Backpropagation */
     public void backProp(){
        double output[] = new double[this.size]; 
     }
}
