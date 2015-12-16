public class Levenshtein {

    public static void main(String [] args) {
        String [] data = { "befoer", "before", "befoer", "beer", "beseige", "besiege","beseige","beige","babys","baby","babys","babies" };
        for (int i = 0; i < data.length; i += 2){
            System.out.println("1stAlg(" + data[i] + ", " + data[i+1] + ") = " + distance(data[i], data[i+1]));
            System.out.println("2ndAlg(" + data[i] + ", " + data[i+1] + ") = " + computeLevenshteinDistance(data[i], data[i+1]));
            System.out.println("3rdAlg(" + data[i] + ", " + data[i+1] + ") = " + levenshteinDistance(data[i], data[i+1]));
        }
    }

    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    //https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= rhs.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

        return distance[lhs.length()][rhs.length()];
    }
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    //supposedly "non-recursive and faster"
    public static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
    int len0 = lhs.length() + 1;
    int len1 = rhs.length() + 1;

    // the array of distances
    int[] cost = new int[len0];
    int[] newcost = new int[len0];

    // initial cost of skipping prefix in String s0
    for (int i = 0; i < len0; i++) cost[i] = i;

    // dynamically computing the array of distances

    // transformation cost for each letter in s1
    for (int j = 1; j < len1; j++) {
        // initial cost of skipping prefix in String s1
        newcost[0] = j;

        // transformation cost for each letter in s0
        for(int i = 1; i < len0; i++) {
            // matching current letters in both strings
            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

            // computing cost for each transformation
            int cost_replace = cost[i - 1] + match;
            int cost_insert  = cost[i] + 1;
            int cost_delete  = newcost[i - 1] + 1;

            // keep minimum cost
            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
        }

        // swap cost/newcost arrays
        int[] swap = cost; cost = newcost; newcost = swap;
    }

    // the distance is the cost for transforming all letters in both strings
    return cost[len0 - 1];
  }
}
