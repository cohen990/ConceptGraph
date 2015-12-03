package ConceptGraph.Input;

import ConceptGraph.DataStructures.WikiPage;
import ConceptGraph.Output.FileOutputAssistant;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.Utilities.StringHasher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

/**
 * Created by Dan on 26/11/2015.
 */
public class WikiPageXmlParser {
    private final StringHasher hasher;
    private final FileOutputAssistant output;
    private Logger logger;

    public WikiPageXmlParser(Logger logger, FileOutputAssistant output) {
        this.logger = logger;
        this.hasher = new StringHasher();
        this.output = output;
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

            if(title == null || title.isEmpty()){
                String fileName = writeFailedXmlParse(input);
                logger.logWarning("Title was null or empty. Input written to " + fileName);
                return null;
            }
            if(rawText == null){
                String fileName = writeFailedXmlParse(input);
                logger.logWarning(String.format("Raw Text for %s is empty.", title));
                return null;
            }

            return new WikiPage(title.toLowerCase(), rawText);
        }
        catch(Exception e){
            logger.logException(e);
            return null;
        }
    }

    private String writeFailedXmlParse(String input) throws IOException {
        String fileName = Integer.toString(hasher.simpleHash(input)) + ".txt";
        Writer writer = output.getWriter(fileName);
        writer.write(input);
        return fileName;
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
