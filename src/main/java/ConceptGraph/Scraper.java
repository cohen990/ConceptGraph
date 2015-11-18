package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class Scraper {
    private Queue<Uri> Links = new LinkedList<>();
    private HashSet<Uri> SeenLinks = new HashSet<>();
    private HashMap<String, Integer> Frequency = new HashMap<>();
    private List<Uri> QueriedUris = new ArrayList<>();
    private HashMap<String, Node> Nodes = new HashMap<>();
    private HashSet<Integer> SeenHashCodes = new HashSet<>();
    private StringHasher Hasher = new StringHasher();

    public void scrape(String[] args) throws IOException {

        Uri firstUrl = new Uri("https://en.wikipedia.org/wiki/Tree");

        Links.add(firstUrl);

        while (!Links.isEmpty()) {
//                if (QueriedUris.size() > 2000) {
//                    return;
//                }
            Uri curr = Links.remove();
            System.out.println("querying " + curr.toString());
            FileWriter writer = null;
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48

                long startTime = System.currentTimeMillis();

                Document doc = Jsoup.connect(curr.toString()).get();
                long fetchTime = System.currentTimeMillis();
                System.out.println("Document fetched in " + (fetchTime - startTime) / 1000.0 + " seconds");

                String title = doc.getElementById("firstHeading").text();
                int hash = Hasher.simpleHash(title);

                if (SeenHashCodes.contains(hash)) {
                    writer = new FileWriter("graph/" + Hasher.simpleHash(title) + ".grp", true);
                } else {
                    SeenHashCodes.add(hash);
                    writer = new FileWriter("graph/" + Hasher.simpleHash(title) + ".grp", false);
                }

                System.out.println("Writing to graph/" + hash + ".grp");

                Node node;
                if (!Nodes.containsKey(title)) {
                    node = new Node(title, curr);
                } else {
                    node = Nodes.get(title);
                    node.setUri(curr);
                }

                Nodes.put(title, node);

                QueriedUris.add(curr);
                System.out.println(QueriedUris.size() + " queried so far...");
                System.out.println("Mem usage: " + NumberFormat.getNumberInstance(Locale.US).format((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "KB");
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
                writeNode(node, writer);
                long endTime = System.currentTimeMillis();

                System.out.println("Document processed in " + (endTime - fetchTime) / 1000.0 + " seconds" + System.getProperty("line.separator"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    private void writeNode(Node node, FileWriter nodes) throws IOException {
        nodes.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
        node.written = true;
        node.dropConnections();
    }

    private void getNodesFromWords(String[] words, Node root) {
        for (String word : words) {
            Node node = Nodes.get(word);

            if (node == null) {
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

    private void getUrisFromDocument(Document doc) {
        Elements links = doc.select("#mw-content-text a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            if (href.startsWith("/") && !href.contains("File:")) {
                if (href.startsWith("//")) {
                    href = "http:" + href;
                } else {
                    href = "https://www.wikipedia.org" + href;
                }
                if (href.contains("wikipedia.org")) {
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

    private void addWordsToHashSet(String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                if (!Frequency.containsKey(word)) {
                    Frequency.put(word, 0);
                }
                Frequency.merge(word, 1, (oldValue, one) -> oldValue + one);
            }
        }
    }

    private void WriteOutput(FileWriter nodes) throws IOException {
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
        for (Node node : Nodes.values()) {
            if (node.written == false) {
                nodes.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
            }
        }

        nodes.close();

        Nodes.clear();
    }
}

