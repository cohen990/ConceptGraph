package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileStorage {
    private Logger logger;
    private StringHasher hasher = new StringHasher();

    public FileStorage() {
        try {
            logger = new Logger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Writer getWriter(Writer writer, String title) throws IOException {
        String fileName = title.replace(" ", "_");
        if (!Storage.SeenLinks.add(title)) {
            writer = new FileWriter("output/graph/" + fileName + ".grp", true);
        } else {
            writer = new FileWriter("output/graph/" + fileName + ".grp", false);
        }

        logger.log("Writing to output/graph/" + fileName + ".grp");
        return writer;
    }
}

