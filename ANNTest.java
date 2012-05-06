import static org.junit.Assert.*;
import org.junit.Test;

public class ANNTest {


    @Test
    public void test_ANN_construct(){
        double xorInput[][] ={ 
           {0.0,0.0},
           {1.0,0.0},
           {0.0,1.0},
           {1.0,1.0}};

        double xorIdeal[][] =
          { {0.0},{1.0},{1.0},{0.0}};

       ANN net = new ANN(2,3,1,50,.6);
        double [] errors = net.offlineLearning(xorInput, xorIdeal);
        }

}
