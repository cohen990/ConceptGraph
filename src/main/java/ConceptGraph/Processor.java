package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;

public class Processor {
    public void writeNode(Node node, Writer nodes) throws IOException {
        nodes.write(node.toString() + System.getProperty("line.separator") + System.getProperty("line.separator"));
        node.written = true;
        node.dropConnections();
    }

    public void getNodesFromWords(String[] words, Node root) {
        for (String word : words) {
            Node node = Storage.Nodes.get(word);

            if (node == null) {
                node = new Node(word);
                Storage.Nodes.put(word, node);
            }

            int strength = 1;
            if (root.connectedConcepts.containsKey(node)) {
                strength = root.connectedConcepts.get(node) + 1;
            } else {
                addWordToHashSet(word);
            }

            root.connectedConcepts.put(node, strength);

            strength = 1;
            if (node.connectedConcepts.containsKey(root)) {
                strength = node.connectedConcepts.get(root) + 1;
            }

            node.connectedConcepts.putIfAbsent(root, strength);
        }
    }

    public void getUrisFromDocument(Document doc) {
        Elements links = doc.select("#mw-content-text a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            addUriToLinks(href);
        }
    }

    public void addUriToLinks(String href) {
        if (href.startsWith("/") && !href.contains("File:")) {
            if (href.startsWith("//")) {
                href = "http:" + href;
            } else {
                href = "https://en.wikipedia.org" + href;
            }
            if (href.contains("wikipedia.org/wiki")) {
                try {
                    if(href.startsWith("http://www.")){
                        href = href.replace("http://www.", "https://en.");
                    }
                    if(href.startsWith("https://www.")){
                        href = href.replace("https://www.", "https://en.");
                    }
                    Uri uri = new Uri(href);
                    if (Storage.SeenLinks.add(uri.toString())) {
                        Storage.Links.add(uri);
                    }
                } catch (MalformedURLException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }

    public void addWordToHashSet(String word) {
        if (!word.isEmpty()) {
            if (!Storage.Frequency.containsKey(word)) {
                Storage.Frequency.put(word, 0);
            }
            Storage.Frequency.merge(word, 1, (oldValue, one) -> oldValue + one);
        }
    }

    public void writeOutput() throws IOException {
        FileWriter freq = new FileWriter("output/freq_output.txt");

        for (String key : Storage.Frequency.keySet()) {
            freq.write(key + "=" + Storage.Frequency.get(key) + System.getProperty("line.separator"));
        }

        freq.close();

        Storage.Frequency.clear();

        FileWriter queried = new FileWriter("output/queried.txt");

        for (Uri uri : Storage.QueriedUris) {
            queried.write(uri.toString() + System.getProperty("line.separator"));
        }

        queried.close();

        Storage.QueriedUris.clear();
    }
}

