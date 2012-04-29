public class InputLayer implements Layer {

    private boolean calComplete;
    private double []data;
    private String layer_id;

    public String p_id;
    public int size;

    public InputLayer(String id, int size){
        this.layer_id = id;
        this.size = size;
        this.calComplete = false;
    }

    public String getId(){
        return this.layer_id;
    }

    public void connectToParent(Layer p){
    }

    public boolean isComplete(){
        return this.calComplete;
    }
  /* Can this layer get the data from the parent data feeding it? */
    public boolean isReady(){
        return false;
    }

    public double[] calc(){
        double output[] = new double[this.size];
        /* TODO: Create a feature extractor */
        this.calComplete = true;
        return output;
    }

}


