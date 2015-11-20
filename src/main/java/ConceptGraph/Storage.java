package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Storage {
    public static Queue<Uri> Links = new LinkedList<>();
    public static HashSet<String> SeenLinks = new HashSet<>();
    public static HashMap<String, Integer> Frequency = new HashMap<>();
    public static List<Uri> QueriedUris = new ArrayList<>();
    public static HashMap<String, Node> Nodes = new HashMap<>();
    public static HashSet<Integer> SeenHashCodes = new HashSet<>();

    public static Node addNewNodeToNodes(Uri curr, String title) {
        Node node;
        if (!Storage.Nodes.containsKey(title)) {
            node = new Node(title, curr);
        } else {
            node = Storage.Nodes.get(title);
            node.setUri(curr);
        }
        return node;
    }
}


