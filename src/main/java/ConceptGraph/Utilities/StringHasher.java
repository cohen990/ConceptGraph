package ConceptGraph.Utilities;

/**
 * Created by Dan on 18/11/2015.
 */
public class StringHasher {
    public static int simpleHash(String str)
    {
        //http://stackoverflow.com/questions/2624192/good-hash-function-for-strings
        int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash*31 + Character.toLowerCase(str.charAt(i));
        }

        return hash;
    }

}
