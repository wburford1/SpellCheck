///Users/william_burford/GitHub/SpellCheck/wordsEn.txt
///Users/william_burford/GitHub/SpellCheck/wordsFreqFinal.txt
///Users/william_burford/GitHub/SpellCheck/badwords.txt

//add your local reference to wordsEn.txt above so you can easily copy and paste below
import java.util.*;
import java.io.*;
public class SpellCheck{
  public static void main(String[]args){
    try{
      String computer = "will";//please set this to "REPLACE" before pushing
      String pathDictionary = "";
      String pathWordFreq = "";
      String pathBadwords = "";
      String pathMatrix = "";
      if (computer.equalsIgnoreCase("will")) {
        pathDictionary = "/Users/william_burford/GitHub/SpellCheck/wordsEn.txt";
        pathWordFreq = "/Users/william_burford/GitHub/SpellCheck/wordsFreqFinal.txt";
        pathBadwords = "/Users/william_burford/GitHub/SpellCheck/badwords.txt";
        pathMatrix = "/Users/william_burford/GitHub/SpellCheck/AlphabetMatrix.txt";
      }else if(computer.equalsIgnoreCase("test")){
        pathDictionary = "";
        pathWordFreq = "";
        pathBadwords = "";
        pathMatrix = "";
      }

       /*(computer.equalsIgnoreCase("sid")){

          pathDictionary = "/Users/SiddharthKucheria/Desktop/SpellCheck/wordsEn.txt";
          pathWordFreq = "/Users/SiddharthKucheria/Desktop/SpellCheck/wordsFreqFinal.txt";
          pathBadwords = "/Users/SiddharthKucheria/Desktop/SpellCheck/badwords.txt";
          pathMatrix = "/Users/SiddharthKucheria/Desktop/SpellCheck/AlphabetMatrix.txt";

      }
        */
      else{
        System.out.println("Error: Not a recognized computer");
        System.exit(1);
      }

      PrintWriter writer = new PrintWriter("correctedWords.txt","UTF-8");
      Trie dictionaryTrie = readFileToTrie(pathDictionary);
      Object[] temp = readFileToArrayList(pathDictionary).toArray();
      String[] dictionaryArr = Arrays.copyOf(temp,temp.length,String[].class);
      String[] dictionaryLength = Arrays.copyOf(dictionaryArr,dictionaryArr.length);
      Arrays.sort(dictionaryLength, new CompStrLen());
      int[] lengthLocations = new int[dictionaryLength[dictionaryLength.length-1].length()+1];
      //lengthLocations stores location of first word of each length
      Map<String,Integer> wordFreqMap = readFileToMap(pathWordFreq);

      int len = 0;
      for(int x=0;x<dictionaryLength.length;x++){
        if (dictionaryLength[x].length()>len) {
          len++;
          lengthLocations[len] = x;
        }
      }

      /*int[][] costsMatrix = readFileTo2DArray(pathMatrix);
      for (int[] arr: costsMatrix) {
        System.out.println(Arrays.toString(arr));
      }*/

      ArrayList<String> misspelt = readFileToArrayList(pathBadwords);
      System.out.println("misspelt.size = "+misspelt.size());

      //this is where time should begin
      long startTime = System.nanoTime();
      for(int x=0;x<misspelt.size();x++){
        String word = misspelt.get(x).toLowerCase();
        System.out.println("Testing word: "+word);
        if (dictionaryTrie.search(word)) {//if in dictionary, put same word in correct list
          writer.println(word);
        }
        else{
          boolean correctionFound = false;
          if (dictionaryTrie.search(word.concat("e"))) {
            writer.println(word.concat("e"));
            System.out.println("Corrected word: "+word.concat("e"));
            correctionFound = true;
          }
          if (!correctionFound && word.charAt(word.length()-1)=='s') {
            String check = word.substring(0,word.length()-1);
            if (dictionaryTrie.search(check.concat("es"))) {
              writer.println(check.concat("es"));
              System.out.println("Corrected word: "+check.concat("es"));
              correctionFound = true;
            }
            else if (check.charAt(check.length()-1)=='y'&&dictionaryTrie.search(check.substring(0,check.length()-1).concat("ies"))) {
              writer.println(check.substring(0,check.length()-1).concat("ies"));
              System.out.println("Corrected word: "+check.substring(0,check.length()-1).concat("ies"));
              correctionFound = true;
            }
          }
          if(!correctionFound){
            int shortestLoc = lengthLocations[word.length()-2];
            int longestLoc = lengthLocations[word.length()+3]-1;//limit the words we check against to just +/- 2 letters

            int minDist = Integer.MAX_VALUE;
            ArrayList<String> corrections = new ArrayList<String>();
            for (int counter=shortestLoc; counter<longestLoc; counter++) {
              //int dist = Levenshtein.distance(word,dictionaryLength[counter]);
              //int dist = Levenshtein.computeLevenshteinDistance(word,dictionaryLength[counter]);
              int dist = Levenshtein.levenshteinDistance(word,dictionaryLength[counter]);
              if (dist<minDist) {
                minDist=dist;
                corrections.clear();
                corrections.add(dictionaryLength[counter]);
              }
              else if (dist==minDist) {
                corrections.add(dictionaryLength[counter]);
              }
            }
            String correction = "";
            if (corrections.size()>1) {
              ArrayList<String> round2 = new ArrayList<String>();
              minDist = Integer.MAX_VALUE;
              WordData currentData = new WordData(word);
              for (int var = 0; var<corrections.size();var++) {
                int dist = currentData.compareTo(corrections.get(var));
                if (dist<minDist) {
                  minDist=dist;
                  round2.clear();
                  round2.add(corrections.get(var));
                }
                else if (dist==minDist) {
                  round2.add(corrections.get(var));
                }
              }
              if (round2.size()>1) {
                correction = round2.toString();
                ArrayList<String> round3 = new ArrayList<String>();
                for (int wow = 0; wow<round2.size(); wow++) {
                  if (wordFreqMap.containsKey(round2.get(wow))) {
                    round3.add(round2.get(wow));
                  }
                }
                if (round3.size()==1) {
                  correction = round3.get(0);
                }
                else if (round3.size()>1) {
                  System.out.println("UNTESTED REGION");
                  int minRank = Integer.MAX_VALUE;
                  String minWord = round3.get(0);
                  for (int boom = 0; boom<round3.size(); boom++) {
                    int rank = wordFreqMap.get(round3.get(boom)).intValue();
                    if (rank<minRank) {
                      minRank = rank;
                      minWord = round3.get(boom);
                    }
                  }
                  correction = minWord;
                }
                else{//this part is the sketchy part that is basically complete guess
                  System.out.println("sketchy part");
                  int wordLen = word.length();
                  int closest = 3;
                  String closestWord = round2.get(0);
                  for (int blah=0; blah<round2.size(); blah++) {
                    String shrek = round2.get(blah);
                    if (shrek.length()==wordLen) {
                      correction = shrek;
                      break;
                    }
                    else if(Math.abs(shrek.length()-wordLen)<closest){
                      closest = Math.abs(shrek.length()-wordLen);
                      closestWord = shrek;
                    }
                  }
                  correction = closestWord;
                }
              }
              else{
                correction = round2.get(0);
              }
            }
            else{
              correction = corrections.get(0);
            }
            writer.println(correction);
            System.out.println("Corrected word: "+correction);
            correctionFound=true;
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

  public static int[][] readFileTo2DArray(String fileName) throws IOException{
      BufferedReader buffer = new BufferedReader(new FileReader(fileName));
      int[][] alph2D = new int[26][26];
      String line;
      int row = 26;
      int size=26;
      String currentWord;
//      while ((line = buffer.readLine()) != null) {

          /*
           currentWord = line;
          for(int x=0; x<line.length(); x++){
              if(line.charAt(x)!=","){
                  currentWord = currentWord.concat(testAgainst.substring(x,x+1));
              }
          }



          String wholeLine  =
          String[] vals = line.trim().split("\\s+");
          size = vals.length;

          /*
           if (alph2D == null) {
           size = vals.length;
           int log10 = (int) Math.floor(Math.log10(size * size)) + 1;
           alph2D = String.format("%%%dd", log10);
           }


          for (int col = 0; col < size; col++) {
              alph2D[row][col] = Integer.parseInt(vals[col]);
          }

          row++;
      }
      */
      return alph2D;
  }

    /*
      while ((line = buffer.readLine()) != null) {
          String[] vals = line.trim().split("\\s+");

//          if (matrix == null) {
              size = vals.length;
              matrix = new int[26][26];
//          }

          for (int col = 0; col < size; col++) {
              matrix[row][col] = Integer.parseInt(vals[col]);
          }

          row++;
      }

      return matrix;
  }
     */

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
