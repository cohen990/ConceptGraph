package ConceptGraph;

import java.util.HashMap;

public class Node{
    public HashMap<Node, Integer> connectedConcepts;
    public Boolean hasWikiPage;
    public String name;

    public Node(String _name){
        hasWikiPage = false;
        name = _name;
        connectedConcepts = new HashMap<>();
    }

    @Override
    public String toString() {
        String representation = "!" + toStringWithoutChildren();
        for(Node node : connectedConcepts.keySet()){
            // should be string builders
            representation += System.getProperty("line.separator") + "\t" + connectedConcepts.get(node) + ": " + node.toStringWithoutChildren();
        }

        return representation;
    }

    public String toStringWithoutChildren(){
        return "\"" + name + "\"";
    }
}
