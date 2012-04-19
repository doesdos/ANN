public class ONeuron implements Neuron{

    private double weight;

	@Override
	public double getOutput(){
		return 0;
	}

    public void setWeight(double w){
        this.weight = w;
    }
}
