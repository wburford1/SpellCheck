///Users/william_burford/GitHub/SpellCheck/wordsEn.txt
///Users/william_burford/GitHub/SpellCheck/wordsFreqFinal.txt
///Users/william_burford/GitHub/SpellCheck/badwords.txt

//add your local reference to wordsEn.txt above so you can easily copy and paste below
import java.util.*;
import java.io.*;
public class SpellCheck{
  public static void main(String[]args){
    try{
      PrintWriter writer = new PrintWriter("correctedWords.txt","UTF-8");
      Trie dictionaryTrie = readFileToTrie("REPLACE");//please set this to "REPLACE" before pushing
      Object[] temp = readFileToArrayList("REPLACE").toArray();
      String[] dictionaryArr = Arrays.copyOf(temp,temp.length,String[].class);
      String[] dictionaryLength = Arrays.copyOf(dictionaryArr,dictionaryArr.length);
      Arrays.sort(dictionaryLength, new CompStrLen());
      int[] lengthLocations = new int[dictionaryLength[dictionaryLength.length-1].length()+1];
      //lengthLocations stores location of first word of each length
      Map<String,Integer> wordFreqMap = readFileToMap("REPLACE");
      System.out.println("wordFreqMap(leave) = "+wordFreqMap.get("leave"));

      int len = 0;
      for(int x=0;x<dictionaryLength.length;x++){
        if (dictionaryLength[x].length()>len) {
          len++;
          lengthLocations[len] = x;
        }
      }

      ArrayList<String> misspelt = readFileToArrayList("REPLACE");
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
  public static Map<String,Integer> readFileToMap(String fileName) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
        String line = br.readLine();
        Map<String,Integer> map = new HashMap<String,Integer>();
        int rank = 1;

        while (line != null) {
            map.put(line,new Integer(rank));
            rank++;
            line = br.readLine();
        }
        return map;
    } finally {
        br.close();
    }
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
