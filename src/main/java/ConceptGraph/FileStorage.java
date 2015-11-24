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
    private Logger logger = new Logger();
    private StringHasher hasher = new StringHasher();

    public Writer getWriter(Writer writer, String title) throws IOException {
        int hash = hasher.simpleHash(title);

        if (!Storage.SeenHashCodes.add(hash)) {
            writer = new FileWriter("output/graph/" + hasher.simpleHash(title) + ".grp", true);
        } else {
            writer = new FileWriter("output/graph/" + hasher.simpleHash(title) + ".grp", false);
        }

        logger.log("Writing to output/graph/" + hasher.simpleHash(title) + ".grp");
        return writer;
    }
}

