///Users/william_burford/GitHub/SpellCheck/wordsEn.txt

//add your local reference to wordsEn.txt above so you can easily copy and paste below
import java.util.*;
import java.io.*;
public class SpellCheck{
  public static void main(String[]args){
    try{
      Trie dictionary = readFile("REPLACE");//please set this to "REPLACE" before pushing
      //continue writing code here
    }catch(IOException e){};
  }
  
  public static Trie readFile(String fileName) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
        String line = br.readLine();
        Trie dictionary = new Trie();

        while (line != null) {
            dictionary.insert(line);
            line = br.readLine();
        }
        return dictionary;
    } finally {
        br.close();
    }
    
    private int wordDistance(String word1,String word2){
      int dist=0;
      if(word1.equalsIgnoreCase(word2)){return 0}
      dist= Math.abs(word1.length-word2.length)+dist;
      for(int i=0; i<word1.length-1;i++)
      {
        if(||word1.charAt(i)==word2.charAt(i)){dist++;}
      }
    }
    
    
  }
}
