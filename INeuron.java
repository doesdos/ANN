public class INeuron implements Neuron {

	private double input;
    private double weight;


	@Override
	public double getOutput() {
		return input;
	}

    @Override
    public void setWeight(double w){
        this.weight = w;
    }
	
	public void setInput(double value)
	{
	this.input = value;	
	}

}
