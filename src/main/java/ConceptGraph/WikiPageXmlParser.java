package ConceptGraph;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Dan on 26/11/2015.
 */
public class WikiPageXmlParser {
    private Logger logger;

    public WikiPageXmlParser() {
        try {
            this.logger = new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WikiPage parse(String input) {
        if(input.isEmpty()){
            return null;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(input));

            Document doc = builder.parse(is);
            Element el = doc.getDocumentElement();

            String title = getValue("title", el);
            String rawText = getValue("text", el);

            return new WikiPage(title.toLowerCase(), rawText);
        }
        catch(Exception e){
            logger.logException(e);
            return null;
        }
    }

    protected String getValue(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }
}
