import static org.junit.Assert.*;
import org.junit.Test;

public class ANNTest {

    @Test
    public void test_setWeights(){
       ANN net = new ANN(2,2,1,100,.6);
       for(int x=0; x<net.numHidden;x++){
           for(int y=0; y<net.inputDimension;y++){
                //System.out.println(
                //"Weight for ("+x+","+y+"): +" + net.hiddenWeights[x][y]);
                assertFalse(net.hiddenWeights[x][y] ==0);
           }
           for(int z=0;z<net.numOutputClasses; z++){
                //System.out.println(
                //"Weight for ("+x+","+y+"): +" + net.outputWeights[z][x]);
               assertFalse(net.outputWeights[z][x]  == 0);
           }
       }
       net.setWeightsToOne();
        for(int x=0; x<net.numHidden;x++){
           for(int y=0; y<net.inputDimension;y++){
                //System.out.println(
                //"Weight for ("+x+","+y+"): +" + net.hiddenWeights[x][y]);
                assertEquals(1,net.hiddenWeights[x][y],.00001);
           }
           for(int z=0;z<net.numOutputClasses; z++){
                //System.out.println(
                //"Weight for ("+x+","+y+"): +" + net.outputWeights[z][x]);
               assertEquals(1, net.outputWeights[z][x],.00001);
           }
       }
    }

    @Test
    public void test_inputToHidden(){
        double  input [] = {2.0,2.0,2.0};
        ANN net = new ANN(3,2,3,10,.5);
        net.setWeightsToOne();
        net.calcHiddenFromInput(input);
        assertEquals(1, net.hiddenOut[0],0);
        for(int x=1;x<net.hiddenOut.length; x++){
            // it's 7 rather than 6 because of that annoying bias weight of 1 
            assertEquals(net.sigmoid(7),net.hiddenOut[x],0.0001);
        }
    }
  
    @Test
    public void test_hiddentToOutput(){
        double sum =0;
        double  input [] = {2.0,2.0,2.0};
        ANN net = new ANN(3,3,3,10,.5);
        net.setWeightsToOne();
        net.calcHiddenFromInput(input);
        net.calcOutputFromHidden();
        for(int x=1;x<net.numOutputClasses; x++){
            sum+= net.output[x];
        }
            assertEquals(1 ,sum, .0001);
    }

    @Test
    public void test_feedForward(){
        double xorIn[][] ={ 
           {0.4,0.0},
           {1.0,0.3},
           {0.0,1.4},
           {1.0,1.0}};

        double  input [] = {2.0,2.0,2.0};
        ANN net = new ANN(2,3,3,10,.5);
        double [] e = net.feedForward(xorIn[0]);
        assertArrayEquals(e, net.output, .001);
        e = net.feedForward(xorIn[3]);
        assertArrayEquals(e, net.output, .001);
    }

    @Test
    public void test_calcDeltas(){

    }

    @Test
    public void test_compute(){
    }

    @Test
    public void ANN_construct(){
        double xorIn[][] ={ 
           {1.0,0.0},
           {1.0,0.0},
           {0.0,1.0},
           {1.0,1.0}};

        double xorOut[][] = { {0.0},{1.0},{1.0},{0.0}};

        double [] t = {1.0, 0.0};

       ANN net2 = new ANN(2,3,1,10,.1);
        double [] errors = net2.offlineLearning(xorIn, xorOut);
        for(int i=0; i<errors.length;i++){
            System.out.println("errors  "+errors[i]);
        }
         double [] ans =  net2.feedForward(t);
        for(int i=0; i<ans.length;i++){
            System.out.println("THE  answer is  "+ans[i]);
        }
    }
}
