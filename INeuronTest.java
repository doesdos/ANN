import static org.junit.Assert.*;

import org.junit.Test;


public class INeuronTest {

	@Test
	public void test() {
		INeuron i = new INeuron();
		i.setInput(10.0);
		assertEquals(10.0, i.getOutput(),.001);
		
	}

}
