///Users/william_burford/GitHub/SpellCheck/wordsEn.txt
///Users/william_burford/GitHub/SpellCheck/badwords.txt

//add your local reference to wordsEn.txt above so you can easily copy and paste below
import java.util.*;
import java.io.*;
public class SpellCheck{
  public static void main(String[]args){
    try{
      PrintWriter writer = new PrintWriter("correctedWords.txt","UTF-8");
      Trie dictionaryTrie = readFileToTrie("/Users/william_burford/GitHub/SpellCheck/wordsEn.txt");//please set this to "REPLACE" before pushing
      Object[] temp = readFileToArrayList("/Users/william_burford/GitHub/SpellCheck/wordsEn.txt").toArray();
      String[] dictionaryArr = Arrays.copyOf(temp,temp.length,String[].class);
      String[] dictionaryLength = Arrays.copyOf(dictionaryArr,dictionaryArr.length);
      Arrays.sort(dictionaryLength, new CompStrLen());
      int[] lengthLocations = new int[dictionaryLength[dictionaryLength.length-1].length()+1];
      //lengthLocations stores location of first word of each length
      int len = 0;
      for(int x=0;x<dictionaryLength.length;x++){
        if (dictionaryLength[x].length()>len) {
          len++;
          lengthLocations[len] = x;
        }
      }

      ArrayList<String> misspelt = readFileToArrayList("/Users/william_burford/GitHub/SpellCheck/badwords.txt");
      System.out.println("misspelt.size = "+misspelt.size());

      //this is where time should begin
      long startTime = System.nanoTime();
      for(int x=0;x<misspelt.size();x++){
        String word = misspelt.get(x);
        System.out.println("Testing word: "+word);
        if (dictionaryTrie.search(word)) {//if in dictionary, put same word in correct list
          writer.println(word);
        }
        else{
          boolean correctionFound = false;
          if (dictionaryTrie.search(word.concat("e"))) {
            writer.println(word.concat("e"));
            correctionFound = true;
          }
          else{
            int shortestLoc = lengthLocations[word.length()-2];
            int longestLoc = lengthLocations[word.length()+3]-1;//limit the words we check against to just +/- 2 letters

            int minDist = Integer.MAX_VALUE;
            String correction = "";
            for (int counter=shortestLoc; counter<longestLoc; counter++) {
              int dist = Levenshtein.distance(word,dictionaryLength[counter]);
              if (dist<minDist) {
                minDist=dist;
                correction = dictionaryLength[counter];
              }
            }
            writer.println(correction);
            correctionFound=true;
            //do stuff to correct
          }

          if (!correctionFound) {
            writer.println("!!! "+word);
          }
        }
      }
      long endTime = System.nanoTime();
      System.out.println("Time elapsed = "+(endTime-startTime)/1000000+"ms");
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

class CompStrLen implements Comparator<String> {
  public int compare(String o1, String o2) {
    if (o1.length() > o2.length()) {
      return 1;
    } else if (o1.length() < o2.length()) {
      return -1;
    } else {
      return 0;
    }
  }
}
