/** Layer class that contains a specifc type of neuron dependent 
 * on its input, and each may have  different types of output functions.
 */
public class Layer{

    private Neuron[] neurons;
    private String id;
    private boolean calComplete;
    private double []internaldata;

    public Layer parent;
    public String pid;
    public int function, size;

    public Layer(String id, int size){
        this.id = id;
        this.size = size;
        this.neurons = new Neuron[size];
        this.calComplete = false;
    }

    public void connectToParent(Layer p){
        this.pid = p.id;
        this.parent = p;
    }

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
    public void calcInput(){
        double input[] = new double[this.size]; 
        for(int i=0;i<this.size;i++){
            input[i] = this.neurons[i].getOutput();
        }
        this.internaldata = input;
    }
    public double[] calcOutput(){
        double output[] = new double[this.size]; 
        for(int i=0;i<this.size;i++){
            /**TODO Create sigmoid, softmax, etc functions **/
        }

        this.calComplete = true;
        return output;
    }

    public boolean isComplete(){
        return this.calComplete;
    }

    public boolean isReady(){
        return this.parent.isComplete();
    }
}
