import java.io.*;
import java.util.*;

public class NetForTwitter {


    public  static void main(String[] args) {
       LinkedHashMap<String, Integer> tokenIndex;
       LinkedHashMap<String, Integer> map =  readAllFiles();

       // map array index to keys
       Object [] keys = map.keySet().toArray(); // returns an array of keys
       //Object [] values = map.values().toArray(); // returns an array of values
       tokenIndex = new LinkedHashMap<String, Integer>();
       System.out.println(""+keys.length);
       for(int i=0; i<keys.length; i++){
           //System.out.println(keys[i]);
           tokenIndex.put((String)keys[i],i);
       }

        // read in the files
        File dir = new File("../twittergame/just_tweets");
        File[] files = dir.listFiles();
        System.out.print("" +readSingleFile(files[0]).size());

       double [][] data = new double[files.length][keys.length];
       double [][] expected = new double[files.length][files.length];
       for(int i=0; i<files.length; i++){
           double [] columns = new double[keys.length];
           LinkedHashMap<String, Integer> tokens = readSingleFile(files[0]);
           Object [] tkeys = tokens.keySet().toArray(); // returns an array of keys
           Object [] tvalues = tokens.values().toArray(); // returns an array of values
           for(int y=0; y<keys.length;y++){
               // it has this word add its value to correct index
               if(tokens.containsKey(keys[y])){
                   columns[tokenIndex.get(keys[y])] = (double)tokens.get(keys[y]);
               }
           }
           expected[i][i] = 1.0;
           data[i] = columns;
       }

       // Create the neaural net stuff 
        ANN net = new ANN(keys.length,5,files.length,500,.3);
        net.offlineLearning(data, expected);
    }

    public static LinkedHashMap readSingleFile(File file){
        FileReader fr;
        LinkedHashMap<String, Integer> Lhm = new LinkedHashMap<String, Integer>();
            try {
              fr = new FileReader(file.getAbsoluteFile());
              BufferedReader br = new BufferedReader (fr);
              String line = "";
              while ((line =br.readLine()) != null) {
                  String updated =  line.replaceAll("(http://[^ ]*|www.[^ ]*)", "").toLowerCase().trim();
                  
                  //System.out.println(updated);
                  //System.out.println(line);
            
                  char[] w = new char[501];
                  Stemmer s = new Stemmer();
                  ByteArrayInputStream in = new ByteArrayInputStream(updated.getBytes());
                  while(true)

                       {  int ch = in.read();
                          if (Character.isLetter((char) ch))
                          {
                             int j = 0;
                             while(true)
                             {  ch = Character.toLowerCase((char) ch);
                                w[j] = (char) ch;
                                if (j < 500) j++;
                                ch = in.read();
                                if (!Character.isLetter((char) ch))
                                {
                                   // to test add(char ch) 
                                   for (int c = 0; c < j; c++) s.add(w[c]);

                                   // or, to test add(char[] w, int j) 
                                   // s.add(w, j); 

                                   s.stem();
                                   {  String u;

                                      u = s.toString();
                                      if(u.length()<=2){
                                          //System.out.println("passing "+u);

                                      }
                                      else if(Lhm.containsKey(u)){
                                      int num = Lhm.get(u);
                                      //System.out.println("adding  "+u);
                                      num++;
                                      Lhm.put(u,num);
                                      }
                                      else{
                                 //     System.out.println("creating  "+u);
                                          Lhm.put(u,1);
                                      }

                                      //System.out.println(u);
                                   }
                                   break;
                                }
                             }
                          }
                          if (ch < 0) break;
                          //System.out.println((char)ch);
                       }
                }
              br.close();
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            return Lhm;
    }

    public static LinkedHashMap readAllFiles() {
        FileReader fr;
        LinkedHashMap<String, Integer> Lhm = new LinkedHashMap<String, Integer>();

        // read in the files
        File dir = new File("../twittergame/just_tweets");
        File[] files = dir.listFiles();

        for(int i=0;i<files.length; i++){
            //System.out.println(files[i].getName());
        
            // get the files line by line
            try {
              fr = new FileReader(files[i].getAbsoluteFile());
              BufferedReader br = new BufferedReader (fr);
              String line = "";
              while ((line =br.readLine()) != null) {
                  String updated =  line.replaceAll("(http://[^ ]*|www.[^ ]*)", "").toLowerCase().trim();
                  
                  //System.out.println(updated);
                  //System.out.println(line);
            
                  char[] w = new char[501];
                  Stemmer s = new Stemmer();
                  ByteArrayInputStream in = new ByteArrayInputStream(updated.getBytes());
                  while(true)

                       {  int ch = in.read();
                          if (Character.isLetter((char) ch))
                          {
                             int j = 0;
                             while(true)
                             {  ch = Character.toLowerCase((char) ch);
                                w[j] = (char) ch;
                                if (j < 500) j++;
                                ch = in.read();
                                if (!Character.isLetter((char) ch))
                                {
                                   // to test add(char ch) 
                                   for (int c = 0; c < j; c++) s.add(w[c]);

                                   // or, to test add(char[] w, int j) 
                                   // s.add(w, j); 

                                   s.stem();
                                   {  String u;

                                      u = s.toString();
                                      if(u.length()<=2){
                                          //System.out.println("passing "+u);

                                      }
                                      else if(Lhm.containsKey(u)){
                                      int num = Lhm.get(u);
                                      //System.out.println("adding  "+u);
                                      num++;
                                      Lhm.put(u,num);
                                      }
                                      else{
                                 //     System.out.println("creating  "+u);
                                          Lhm.put(u,1);
                                      }

                                      //System.out.println(u);
                                   }
                                   break;
                                }
                             }
                          }
                          if (ch < 0) break;
                          //System.out.println((char)ch);
                       }
                }
              br.close();
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
        }


      return Lhm;
         
    }
    
}
