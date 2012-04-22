import static org.junit.Assert.*;

import org.junit.Test;


public class NeuralNetTest {

	@Test
	public void test_Neuron() {
        // Intial sigmoid test
		Neuron i = new Neuron(.02);
		assertEquals(0.5, i.getOutput(3),.01);
	}

    @Test
    public void test_Layer(){
        HiddenLayer hidden = new HiddenLayer("hidden",5);
        HiddenLayer out = new HiddenLayer("output", 2);
        out.connectToParent(hidden);
        assertEquals("hidden", hidden.getId());
        assertEquals("output", out.getId());
    }

}
