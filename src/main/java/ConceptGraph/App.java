package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Hello world!
 */
public class App {
    private static Queue<Uri> Links = new LinkedList<>();
    private static HashSet<Uri> SeenLinks = new HashSet<>();
    private static HashMap<String, Integer> Frequency = new HashMap<>();
    private static List<Uri> QueriedUris = new ArrayList<>();
    private static HashMap<String, Node> Nodes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Uri firstUrl = new Uri("https://en.wikipedia.org/wiki/Tree");

        Links.add(firstUrl);

        try {
            while (!Links.isEmpty()) {
                if(QueriedUris.size() > 2000){
                    return;
                }
                Uri curr = Links.remove();
                System.out.println("querying " + curr.toString() + System.getProperty("line.separator"));
                try {
                    Document doc = Jsoup.connect(curr.toString()).get();

                    String title = doc.getElementById("firstHeading").text();
                    Node node;
                    if (!Nodes.containsKey(title)) {
                        node = new Node(title);
                    } else {
                        node = Nodes.get(title);
                    }
                    node.hasWikiPage = true;
                    Nodes.put(title, node);

                    QueriedUris.add(curr);
                    System.out.println(QueriedUris.size() + " queried so far...");
                    String mainBody = doc.getElementById("mw-content-text").text();
                    // currently case sensitive
                    String[] words = mainBody.split("\\W");

                    Object[] filtered = Stream.of(words)
                            .map((word) -> word.toLowerCase().trim())
                            .filter((word) -> !word.isEmpty())
                            .toArray();

                    words = Arrays.copyOf(filtered, filtered.length, String[].class);

                    addWordsToHashSet(words);
                    getUrisFromDocument(doc);
                    getNodesFromWords(words, node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            WriteOutput();
        }
    }

    private static void getNodesFromWords(String[] words, Node root) {
        for (String word : words) {
            Node node = Nodes.get(word);

            if(node == null){
                node = new Node(word);
                Nodes.put(word, node);
            }

            int strength = 1;
            if (root.connectedConcepts.containsKey(node)) {
                strength = root.connectedConcepts.get(node) + 1;
            }

            root.connectedConcepts.put(node, strength);

            strength = 1;
            if (node.connectedConcepts.containsKey(root)) {
                strength = node.connectedConcepts.get(root) + 1;
            }

            node.connectedConcepts.putIfAbsent(root, strength);
        }
    }

    private static void getUrisFromDocument(Document doc) {
        Elements links = doc.select("#mw-content-text a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("/")) {
                if (href.startsWith("//")) {
                    href = "http:" + href;
                } else {
                    href = "https://www.wikipedia.org" + href;
                }
                if(href.contains("wikipedia.org")) {
                    try {
                        Uri uri = new Uri(href);
                        if (SeenLinks.add(uri)) {
                            Links.add(uri);
                        }
                    } catch (MalformedURLException e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private static void addWordsToHashSet(String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!Frequency.containsKey(word)) {
                    Frequency.put(word, 0);
                }
                Frequency.merge(word, 1, (oldValue, one) -> oldValue + one);
            }
        }
    }

    private static void WriteOutput() throws IOException {
        FileWriter freq = new FileWriter("freq_output.txt");

        for (String key : Frequency.keySet()) {
            freq.write(key + "=" + Frequency.get(key) + System.getProperty("line.separator"));
        }

        freq.close();

        Frequency.clear();

        FileWriter queried = new FileWriter("queried.txt");

        for (Uri uri : QueriedUris) {
            queried.write(uri.toString() + System.getProperty("line.separator"));
        }

        queried.close();

        QueriedUris.clear();

        FileWriter nodes = new FileWriter("graph.txt");
        for (Node node : Nodes.values()) {
            nodes.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
        }

        nodes.close();

        Nodes.clear();
    }
}

