package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

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
                if (Links.size() > 2000) {
                    return;
                }
                Uri curr = Links.remove();
                try {
                    Document doc = Jsoup.connect(curr.toString()).get();

                    String title = doc.getElementById("firstHeading").text();
                    Node node;
                    if(!Nodes.containsKey(title)){
                        node = new Node(title);
                    }
                    else{
                        node = Nodes.get(title);
                    }
                    node.hasWikiPage = true;
                    Nodes.put(title, node);

                    QueriedUris.add(curr);
                    String mainBody = doc.getElementById("mw-content-text").text();
                    String[] words = mainBody.split("\\W");
                    addWordsToHashSet(words);
                    getUrisFromDocument(doc);
                    getNodesFromWords(words, node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally{
            WriteOutput();
        }
    }

    private static void getNodesFromWords(String[] words, Node root) {
        for(String word: words) {
            Node node;
            if (!Nodes.containsKey(word)) {
                node = new Node(word);
            }
            else{
                node = Nodes.get(word);
            }

            int strength = 1;
            if(root.connectedConcepts.containsKey(node)){
                strength = root.connectedConcepts.get(node) + 1;
            }

            root.connectedConcepts.put(node, strength);
        }
    }

    private static void getUrisFromDocument(Document doc) {
        Elements links = doc.select("#mw-content-text a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("/")) {
                href = "https://www.wikipedia.org" + href;
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

    private static void addWordsToHashSet(String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!Frequency.containsKey(word)) {
                    Frequency.put(word, 0);
                }
                Frequency.merge(word, 1, (oldvalue, one) -> oldvalue + one);
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

        for(Uri uri : QueriedUris){
            queried.write(uri.toString() + System.getProperty("line.separator"));
        }

        queried.close();

        QueriedUris.clear();

        FileWriter nodes = new FileWriter("graph.txt");
        for(Node node : Nodes.values()){
            nodes.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
        }

        nodes.close();

        Nodes.clear();
    }
}

