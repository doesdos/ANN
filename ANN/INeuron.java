
public class INeuron implements Neuron {

	private double input;
	@Override
	public double getOutput() {
		return input;
	}
	
	public void setInput(double value)
	{
	this.input = value;	
	}

}
