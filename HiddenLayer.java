/** Layer class that contains neurons dependent 
 * on its input, and each may have  different types of output functions.
 */
public class HiddenLayer implements Layer{

    private Neuron[] neurons;
    private boolean calComplete;
    private double []internaldata;
    private Layer parent;
    private String layer_id;

    public String p_id;
    public int size;

    public HiddenLayer(String id, int size){
        this.layer_id = id;
        this.size = size;
        this.calComplete = false;
        this.neurons = new Neuron[size];
        for(Neuron n: this.neurons){
            // Taken from http://bit.ly/JoWZRw 
            // Random double (0,1) rather than [0,1)
            n = new Neuron(0.01 + (Math.random() * (1 - 0.01)));
        }
    }

    public String getId(){
        return this.layer_id;
    }

    /* Used to connect this layers input to this layers calc functions 
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
        for(double data: parent_data){
            for(int i=0; i<neurons.length;i++){
                output[i] +=neurons[i].getOutput(data);
            }
        /* TODO: implement softmax function */
        }
        this.calComplete= true;
        return output;
    }
                    
/*
    public void setActivation(String type){
        if(type.equals("sigmoid")){
            this.function = 1;
        }
        else if (type.equals("softmax")){
            this.function = 2;
        }
        else{
            this.function = 0;
        }
    }
*/
}
