import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class NetForTwitter {

    public static void main(String[] args) {
        FileReader fr;

        // read in the files
        File dir = new File("../twittergame/just_tweets");
        File[] files = dir.listFiles();
        for(int i=0;i<files.length; i++){
            System.out.println(files[i].getName());
        
            // get the files line by line
            try {
              fr = new FileReader(files[i].getAbsoluteFile());
              BufferedReader br = new BufferedReader (fr);
              String line = "";
              while ((line =br.readLine()) != null) {
                  System.out.println(line);
              }
              br.close();
              
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }
    }
    
}
