public class Neuron{

    private double intial_weight;
    private double weight;
	private double input;

    public Neuron(double w){
        this.intial_weight = w;
        this.weight = 0;
    }

	public double getOutput(double input) {

		return sigmoid(intial_weight + (weight* input));
	}

    public void setWeight(double w){
        this.weight = w;
    }

    private double sigmoid(double x) {
        return (1 / (1 + Math.exp(-x)));
    }
    
}
