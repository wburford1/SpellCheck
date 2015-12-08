///Users/william_burford/GitHub/SpellCheck/wordsEn.txt
///Users/william_burford/GitHub/SpellCheck/badwords.txt

//add your local reference to wordsEn.txt above so you can easily copy and paste below
import java.util.*;
import java.io.*;
public class SpellCheck{
  public static void main(String[]args){
    try{
      PrintWriter writer = new PrintWriter("correctedWords.txt","UTF-8");
      Trie dictionaryTrie = readFileToTrie("REPLACE");//please set this to "REPLACE" before pushing
      ArrayList<String> misspelt = readFileToArrayList("REPLACE");
      for(int x=0;x<misspelt.size();x++){
        if (dictionaryTrie.search(misspelt.get(x))) {//if in dictionary, put same word in correct list
          writer.println(misspelt.get(x));
        }
        else{
          //do stuff to correct
        }
      }
      writer.close();
    }catch(IOException e){};
  }

  public static Trie readFileToTrie(String fileName) throws IOException {
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
  public static ArrayList<String> readFileToArrayList(String fileName) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
        String line = br.readLine();
        ArrayList<String> arrList = new ArrayList<String>();

        while (line != null) {
            arrList.add(line);
            line = br.readLine();
        }
        return arrList;
    } finally {
        br.close();
    }
  }
    private int wordDistance(String word1,String word2){
      int dist=0;
      if(word1.equalsIgnoreCase(word2)){return 0;}
      dist= Math.abs(word1.length()-word2.length())+dist;
      for(int i=0; i<word1.length()-1;i++)
      {
        if(word1.charAt(i)==word2.charAt(i)){dist++;}
      }
      return dist;
  }
}
