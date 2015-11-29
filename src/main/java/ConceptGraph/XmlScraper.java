package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class XmlScraper implements Scraper {
    private final Processor processor;
    private Logger logger;
    private final WikiPageXmlParser parser;
    private FileStorage fileStorage;
    private WikiDumpReader reader;
    private int count;

    public XmlScraper() throws FileNotFoundException {
        this.processor = new Processor();
        try {
            this.logger = new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileStorage = new FileStorage();
        Reader baseReader = new FileReader("C:\\Users\\Dan\\Desktop\\wikidump\\wikidump.20151002.xml");
        this.reader = new WikiDumpReader(baseReader);
        this.parser = new WikiPageXmlParser();
    }

    public void scrape() throws IOException {
        boolean shouldBreak= false;
        String pageXml = reader.getPage();
        count = 0;

        while (pageXml != null) {
            if(shouldBreak){
                break;
            }

            WikiPage page = parser.parse(pageXml);
            if(page == null){
                return;
            }

            Writer writer = null;
            try {
                logger.logDate();
                writer = process(page, writer);
            } catch (IOException e) {
                logger.logException(e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
                pageXml = reader.getPage();
            }
        }
        processor.writeOutput();
    }

    private Writer process(WikiPage page, Writer writer) throws IOException {
        Timer timer = Timer.startNew();

        writer = fileStorage.getWriter(writer, page.title);

        Node node = Storage.getNodeIfExists(page.title);

        if(node == null){
            return null;
        }

        Storage.Nodes.put(page.title, node);

        String[] words = getWords(page.getStrippedText());

        processor.getNodesFromWords(words, node);
        processor.writeNode(node, writer);
        timer.stop();

        logger.log((++ count) + " processed so far...");
        logger.logMemUsage();
        logger.logTimeElapsed("Document processed" , timer, true);
        return writer;
    }

    private String[] getWords(String mainBody) {
        String[] words = mainBody.split("[\\W|_]");

        Object[] filtered = Stream.of(words)
                .map((word) -> word.toLowerCase().trim())
                .filter((word) -> !word.isEmpty())
                .toArray();

        words = Arrays.copyOf(filtered, filtered.length, String[].class);
        return words;
    }
}

