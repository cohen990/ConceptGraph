package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Scraper {
    private final Processor processor;
    private final StringHasher hasher;
    private final Logger logger;

    public Scraper(){
        this.processor = new Processor();
        this.hasher = new StringHasher();
        this.logger = new Logger();
    }

    public void scrape() throws IOException {
        Uri firstUrl = new Uri("https://en.wikipedia.org/wiki/Tree");

        Storage.Links.add(firstUrl);
        boolean shouldBreak= false;

        while (!Storage.Links.isEmpty()) {
            if(shouldBreak){
                break;
            }
            Uri curr = Storage.Links.remove();
            logger.log("querying " + curr.toString());
            FileWriter writer = null;
            try {
                logger.logDate();
                Document doc = getDocument(curr);
                writer = parseDocument(curr, writer, doc);
            } catch (IOException e) {
                logger.logException(e);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
        processor.writeOutput();
    }

    private FileWriter parseDocument(Uri currUri, FileWriter writer, Document doc) throws IOException {
        Timer timer = Timer.startNew();

        String title = getTitle(doc);

        writer = addToSeenHashes(writer, title);

        Node node = Storage.addNewNodeToNodes(currUri, title);

        Storage.Nodes.put(title, node);

        Storage.QueriedUris.add(currUri);
        removeRefs(doc);

        String mainBody = doc.getElementById("mw-content-text").text();

        String[] words = getWords(mainBody);

        processor.getUrisFromDocument(doc);
        processor.getNodesFromWords(words, node);


        processor.writeNode(node, writer);
        timer.stop();

        logger.log(Storage.QueriedUris.size() + " queried so far...");
        logger.logMemUsage();
        logger.logTimeElapsed("Document processed" , timer, true);
        return writer;
    }

    private String getTitle(Document doc) {
        String title = doc.getElementById("firstHeading").text();

        Elements redirectedFrom = doc.select(".mw-redirectedfrom a");

        if(!redirectedFrom.isEmpty()){
            title = redirectedFrom.first().text();
        }
        return title;
    }

    private Document getDocument(Uri curr) throws IOException {
        Timer timer = Timer.startNew();
        Document doc = Jsoup.connect(curr.toString()).get();
        timer.stop();
        logger.log("Document fetched in " + timer.elapsedSeconds() + " seconds");
        return doc;
    }

    private FileWriter addToSeenHashes(FileWriter writer, String title) throws IOException {
        int hash = hasher.simpleHash(title);

        if (!Storage.SeenHashCodes.add(hash)) {
            writer = new FileWriter("output/graph/" + hasher.simpleHash(title) + ".grp", true);
        } else {
            writer = new FileWriter("output/graph/" + hasher.simpleHash(title) + ".grp", false);
        }

        logger.log("Writing to output/graph/" + hasher.simpleHash(title) + ".grp");
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

    private void removeRefs(Document doc) {
        doc.getElementsByClass("reflist").remove();
        doc.getElementsByClass("refbegin").remove();
    }
}

