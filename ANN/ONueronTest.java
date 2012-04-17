import static org.junit.Assert.*;

import org.junit.Test;


public class ONueronTest {

	@Test
	public void test() {
		ONeuron n  = new ONeuron();
		assertEquals(0.0,n.getOutput(),.001 );
	}

}
