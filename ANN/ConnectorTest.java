import static org.junit.Assert.*;

import org.junit.Test;


public class ConnectorTest {

	@Test
	public void test() {
		Connector c = new Connector();
		assertEquals(0.0, c.getValue(), .001);
		c.setWeight(.5);
		INeuron i = new INeuron();
		i.setInput(.7);
		c.setConnection(i);
		assertEquals(.7*.5, c.getValue(), .001);
		i.setInput(1.0);
		assertEquals(.5, c.getValue(), .001);
		
	}

}
