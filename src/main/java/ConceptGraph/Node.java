package ConceptGraph;

import com.sun.jndi.toolkit.url.Uri;

import java.util.HashMap;

public class Node{
    public HashMap<Node, Integer> connectedConcepts;
    public Boolean hasWikiPage;
    public String name;
    public Boolean written;
    private Uri uri;

    public Node(String name){
        hasWikiPage = false;
        this.name = name.toLowerCase();
        connectedConcepts = new HashMap<>();
        written = false;
    }

    public Node(String name, Uri uri){
        this.uri = uri;
        hasWikiPage = true;
        this.name = name;
        connectedConcepts = new HashMap<>();
        written = false;
    }

    public void setUri(Uri uri){
        hasWikiPage = true;
        this.uri = uri;
    }

    @Override
    public String toString() {
        String representation = "!" + toStringWithoutChildren();
        if(hasWikiPage){
            representation += " - " + uri;
        }
        for(Node node : connectedConcepts.keySet()){
            // should be string builders
            representation += System.getProperty("line.separator") + "\t" + connectedConcepts.get(node) + ": " + node.toStringWithoutChildren();
        }

        return representation;
    }

    public String toStringWithoutChildren(){
        return "\"" + name + "\"";
    }

    public void dropConnections() {
        connectedConcepts = new HashMap<>();
    }

    public void addConnection(Node node, int weight){
        connectedConcepts.putIfAbsent(node, weight);
    }
}
