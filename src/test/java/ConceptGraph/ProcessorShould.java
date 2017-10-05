package ConceptGraph;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.Output.FileOutput;
import com.sun.jndi.toolkit.url.Uri;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ProcessorShould {

    private FileOutput fileOutput;
    private Processor processor;
    private Node rootNode;
    private TestProcessor testProcessor;

    @Before
    public void before(){
        fileOutput = mock(FileOutput.class);
        processor = new Processor(fileOutput);
        testProcessor = new TestProcessor(fileOutput);
        rootNode = new Node("root-node");
    }

    @Test
    public void return_the_same_root_node_provided(){
        String[] words = new String[0];

        Node result = getNodesFromWords(words);
        assertEquals(rootNode, result);
    }

    private Node getNodesFromWords(String[] words) {
        return processor.getNodesFromWordsSideEffectFree(words, rootNode);
    }

    @Test
    public void add_a_new_word_to_the_root_node(){
        String expectedName = "expected-name";
        String[] words = {expectedName};

        Node result = getNodesFromWords(words);
        Node childNode = getOnlyChild(result);

        assertEquals(result.connectedConcepts.size(), 1);
        assertEquals(expectedName, childNode.name);
        assertEquals(1, (int) result.connectedConcepts.get(childNode));
    }

    private Node getOnlyChild(Node parent) {
        return (Node) parent.connectedConcepts.keySet().toArray()[0];
    }

    @Test
    public void increase_strength_of_existing_child_concept(){
        String expectedName = "added-twice";
        String[] words = {expectedName, expectedName};

        Node result = getNodesFromWords(words);

        Node childNode = getOnlyChild(result);

        assertEquals(result.connectedConcepts.size(), 1);
        assertEquals(expectedName, childNode.name);
        assertEquals(2, (int) result.connectedConcepts.get(childNode));
    }

    @Test
    public void add_all_words_as_nodes(){
        String[] words = {"one", "two", "three", "four", "five", "six"};

        Node result = getNodesFromWords(words);

        assertEquals(6, result.connectedConcepts.size());

        for (int strength : result.connectedConcepts.values()) {
            assertEquals(1, strength);
        }
    }

    @Test
    public void only_increase_strength_of_nodes_that_are_added_multiple_times(){
        String[] words = {
                "one",
                "two-added-twice",
                "two-added-twice",
                "three",
                "four-added-twice",
                "four-added-twice",
                "five",
                "six"};

        Node result = getNodesFromWords(words);

        assertEquals(6, result.connectedConcepts.size());

        for (Node key : result.connectedConcepts.keySet()) {
            if(key.name.endsWith("added-twice")){
                assertEquals(2, (int) result.connectedConcepts.get(key));
            }
            else{
                assertEquals(1, (int) result.connectedConcepts.get(key));
            }
        }
    }

    private class TestProcessor extends Processor{
        public TestProcessor(FileOutput fileOutputAssistant) {
            super(fileOutputAssistant);
        }

        @Override
        protected List<Uri> getQueriedUris() {
            return super.getQueriedUris();
        }

        @Override
        protected Node getNodeFromStorage(String word) {
            return super.getNodeFromStorage(word);
        }

        @Override
        protected HashMap<String, Integer> getFrequency() {
            return super.getFrequency();
        }
    }
}
