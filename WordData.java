import java.util.*;
public class WordData implements Comparable<String>{
  private String word;
  private int[] selfFreqs;
  public WordData(String wordArg){
    word = wordArg;
    selfFreqs = WordData.calcLetterFreq(word);
  }

  public static void main(String[] args){
    WordData tester = new WordData("befoer");
    String testAgainst = "beer boer befog defer refer beaker bearer beater beaver bedder beeper befogs befool before befoul belier bender berber better bezoar buffer defier defter hefter lefter vetoer beefier";
    String currentWord = "";
    ArrayList<String> testWords = new ArrayList<String>();
    for (int x = 0; x<testAgainst.length(); x++) {
      if (testAgainst.charAt(x)!=' ') {
        currentWord = currentWord.concat(testAgainst.substring(x,x+1));
      }
      else{
        testWords.add(currentWord);
        currentWord = "";
      }
    }
    testWords.add(currentWord);
    long startTime = System.nanoTime();
    for (String testWord : testWords) {
      System.out.println("letterFreqDiff("+testWord+") = "+tester.compareTo(testWord));
    }
    long endTime = System.nanoTime();
    System.out.println((endTime-startTime)/1000000+"ms");
  }

  public int[] getSelfFreqs(){
    return selfFreqs;
  }

  public static int[] calcLetterFreq(String word){
    int[] freqs = new int[26];
    for (int x=0; x<word.length(); x++) {
      freqs[word.charAt(x)-'a']++;
    }
    return freqs;
  }

  public int compareTo(String other){
    int[] otherFreqs = WordData.calcLetterFreq(other);
    int[] costs = new int[26];
    int cost = 0;
    for (int counter = 0; counter<26; counter++) {
      costs[counter] = Math.abs(this.selfFreqs[counter]-otherFreqs[counter]);
      cost+=costs[counter];
    }
    return cost;
  }
}
