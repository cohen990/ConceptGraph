package ConceptGraph;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class XmlScraper implements Scraper {
    private final Processor processor;
    private Logger logger;
    private final WikiPageXmlParser parser;
    private GraphStore graphStore;
    private WikiDumpReader reader;
    private int count;

    public XmlScraper() throws FileNotFoundException {
        this.processor = new Processor();
        this.logger = FileLogger.Create();
        this.graphStore = new SingleFileGraphStore();
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

            logger.logDate();
            process(page);
            pageXml = reader.getPage();
        }
        processor.writeOutput();
    }

    private void process(WikiPage page) throws IOException {
        Timer timer = Timer.startNew();

        Node node = Storage.getNodeIfExists(page.title);

        if(node == null){
            logger.logWarning("Unable to create the node: " + page.title);
        }

        Storage.Nodes.put(page.title, node);

        String[] words = getWords(page.getStrippedText());

        processor.getNodesFromWords(words, node);
        graphStore.writeNodeToFile(node);
        timer.stop();

        logger.log((++ count) + " processed so far...");
        logger.logMemUsage();
        logger.logTimeElapsed("Document processed" , timer, true);
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

