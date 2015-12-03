package ConceptGraph.DataStructures;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Dan on 26/11/2015.
 */
public class WikiPage {
    public String title;
    public String rawText;
    private String strippedText;
    public String getStrippedText(){
        if(rawText.isEmpty()){
            return (strippedText = "");
        }
        if(strippedText != null){
            return strippedText;
        }

        Document doc = Jsoup.parse(rawText);
        return (strippedText = doc.text());
    }

    public WikiPage(String title, String rawText){
        if(rawText == null){
            rawText = "";
        }
        this.title = title;
        this.rawText = rawText;
    }
}
