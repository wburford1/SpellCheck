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
  }
}
