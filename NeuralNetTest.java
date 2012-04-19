import static org.junit.Assert.*;

import org.junit.Test;


public class NeuralNetTest {

	@Test
	public void test_INeuron() {
		INeuron i = new INeuron();
		i.setInput(10.0);
		assertEquals(10.0, i.getOutput(),.001);
	}

    @Test
    public void test_Layer(){
        Layer hidden = new Layer("hidden",5);
        hidden.setActivation("sigmoid");
        assertEquals(1,hidden.function);
        Layer out = new Layer("output", 2);
        out.connectToParent(hidden);
        assertEquals("hidden", out.pid);

    }

    @Test
    public void test_ONeuron(){
        ONeuron o = new ONeuron();
		assertEquals(0.0,o.getOutput(),.001 );
    }

}
