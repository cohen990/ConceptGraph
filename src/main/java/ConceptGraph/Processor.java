package ConceptGraph;

import ConceptGraph.DataStructures.*;
import ConceptGraph.Output.*;
import com.sun.jndi.toolkit.url.Uri;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class Processor {

    private final FileOutput fileOutputAssistant;

    public Processor(FileOutput fileOutputAssistant){
        this.fileOutputAssistant = fileOutputAssistant;
    }

    public Node getNodesFromWordsSideEffectFree(String[] words, Node root){
       getNodesFromWords(words, root);
       return root;
    }

    public void getNodesFromWords(String[] words, Node root) {
        for (String word : words) {
            Node node = getOrAddFromStorage(word);

            int strength = 1;
            if (root.connectedConcepts.containsKey(node)) {
                strength = root.connectedConcepts.get(node) + 1;
            } else {
                addOrUpdateFrequency(word);
            }

            root.addConnection(node, strength, true);
        }
    }

    public void addOrUpdateFrequency(String word) {
        if (!word.isEmpty()) {
            if (!getFrequency().containsKey(word)) {
                getFrequency().put(word, 0);
            }
            getFrequency().merge(word, 1, (oldValue, one) -> oldValue + one);
        }
    }

    // This function should be in an entirely different class
    public void getUrisFromDocument(Document doc) {
        Elements links = doc.select("#mw-content-text a[href]");

        for (Element link : links) {
            String href = link.attr("href");
            addUriToLinks(href);
        }
    }

    // This function should be in an entirely different class
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

    // This function should be in an entirely different class
    public void writeOutput() throws IOException {
        FileWriter freq = fileOutputAssistant.getWriter("freq_output.txt");

        for (String key : getFrequency().keySet()) {
            freq.write(key + "=" + getFrequency().get(key) + System.getProperty("line.separator"));
        }

        freq.close();

        getFrequency().clear();

        FileWriter queried = fileOutputAssistant.getWriter("queried.txt");

        for (Uri uri : getQueriedUris()) {
            queried.write(uri.toString() + System.getProperty("line.separator"));
        }

        queried.close();

        getQueriedUris().clear();
    }

    private Node getOrAddFromStorage(String word) {
        Node node = getNodeFromStorage(word);

        if (node == null) {
            node = addNodeToStorage(word);
        }
        return node;
    }

    private Node addNodeToStorage(String word) {
        Node node = new Node(word);
        Storage.Nodes.put(word, node);
        return node;
    }

    protected List<Uri> getQueriedUris() {
        return Storage.QueriedUris;
    }

    protected Node getNodeFromStorage(String word) {
        return Storage.Nodes.get(word);
    }

    protected HashMap<String, Integer> getFrequency() {
        return Storage.Frequency;
    }
}

