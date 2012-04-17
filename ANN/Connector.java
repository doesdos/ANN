
public class Connector {
	
	private double weight=0.0;
	private Neuron n= null;
	
	public double getValue()
	{
		return n != null ?  n.getOutput() * this.weight : 0 ;
	}
	
	public void setWeight(double w)
	{
		this.weight = w;
	}
	
	public void setConnection(Neuron n)
	{
		this.n = n;
	}

}
