import java.util.*;
import java.io.*;
public class WordStuff{
  public static void main(String[]args){
    try{
    PrintWriter writer = new PrintWriter("correctedWordsFreq.txt","UTF-8");
    Object[] temp = readFileToArrayList("/Users/william_burford/GitHub/SpellCheck/wordFreq2.txt").toArray();
    String[] dictionaryArr = Arrays.copyOf(temp,temp.length,String[].class);
    String[] copyArr = new String[dictionaryArr.length];
    int numDups = 0;
    for(int x=0;x<dictionaryArr.length;x++){
      boolean duplicate = false;
      for (int y=0; y<copyArr.length; y++) {
        if (dictionaryArr[x].equalsIgnoreCase(copyArr[y])) {
          System.out.println("duplicate word: "+dictionaryArr[x]);
          duplicate = true;
          numDups++;
          break;
        }
      }
      if (!duplicate) {
        copyArr[x]=dictionaryArr[x];
        writer.println(dictionaryArr[x]);
      }
    }
    System.out.println("Number = "+numDups);
    writer.close();
  }catch(IOException e){}
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
}
