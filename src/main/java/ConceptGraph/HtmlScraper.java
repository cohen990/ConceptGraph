package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Stream;

public class HtmlScraper {
    private final Processor processor;
    private Logger logger;
    private FileStorage fileStorage;

    public HtmlScraper(){
        this.processor = new Processor();
        try {
            this.logger = new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileStorage = new FileStorage();
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
            Writer writer = null;
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

    private Writer parseDocument(Uri currUri, Writer writer, Document doc) throws IOException {
        Timer timer = Timer.startNew();

        String title = getTitle(doc);

        writer = fileStorage.getWriter(writer, title);

        Node node = Storage.getNodeIfExists(currUri, title);

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

