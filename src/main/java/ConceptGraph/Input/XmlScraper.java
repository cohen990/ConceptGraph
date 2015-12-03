package ConceptGraph.Input;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.DataStructures.Storage;
import ConceptGraph.DataStructures.WikiPage;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.Output.Storage.GraphStore;
import ConceptGraph.Processor;
import ConceptGraph.Utilities.Timer;

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
    private int DEC_THIRD_2015_AT_6PM_GMT = 1449165600;
    private int END_TIME = DEC_THIRD_2015_AT_6PM_GMT;

    public XmlScraper(Processor processor, Logger logger, GraphStore graphStore, WikiDumpReader wikiReader, WikiPageXmlParser xmlParser) throws FileNotFoundException {
        this.processor = processor;
        this.logger = logger;
        this.graphStore = graphStore;
        this.reader = wikiReader;
        this.parser = xmlParser;
    }

    public void scrape() throws IOException {
        boolean shouldBreak= false;
        String pageXml = reader.getPage();
        count = 0;

        while (pageXml != null) {
            if(shouldBreak || getUnixEpoch() > END_TIME){
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

    private int getUnixEpoch() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    private void process(WikiPage page) throws IOException {

        logger.log("Processing " + page.title);

        Timer timer = Timer.startNew();

        Node node = Storage.getNodeIfExists(page.title);

        if(node == null){
            logger.logWarning("Unable to create the node: " + page.title);
            return;
        }

        logger.log("node retrieved");

        Storage.Nodes.put(page.title, node);

        String[] words = getWordsFrom(page.getStrippedText());

        logger.log("Found " + (words == null ? 0  : words.length) + " words.");

        processor.getNodesFromWords(words, node);

        logger.log(node.connectedConcepts.size() + " connected concepts found.");

        graphStore.writeNodeToFile(node);

        timer.stop();

        logger.log((++ count) + " processed so far...");
        logger.logMemUsage();
        logger.logTimeElapsed("Document processed" , timer, true);
    }

    private String[] getWordsFrom(String mainBody) {
        String[] words = mainBody.split("[\\W|_]");

        Object[] filtered = Stream.of(words)
                .map((word) -> word.toLowerCase().trim())
                .filter((word) -> !word.isEmpty())
                .toArray();

        words = Arrays.copyOf(filtered, filtered.length, String[].class);
        return words;
    }
}

