package ConceptGraph.Output.Storage;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.Output.FileOutput;
import ConceptGraph.Output.Logging.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleFileGraphStoreShould {

    private FileOutput fileOutput;
    private Logger logger;
    private SingleFileGraphStore graphStore;
    private FileOutputStream graphFileOutputStream;
    private FileWriter indexFile;

    @Before
    public void before() throws IOException {
        fileOutput = mock(FileOutput.class);
        logger = mock(Logger.class);

        graphFileOutputStream = mock(FileOutputStream.class);
        when(fileOutput.getFileOutputStream(anyString())).thenReturn(graphFileOutputStream);

        indexFile = mock(FileWriter.class);
        when(fileOutput.getWriter(anyString())).thenReturn(indexFile);

        graphStore = new SingleFileGraphStore(fileOutput, logger);
    }

    @Test
    public void get_graph_output_stream() throws IOException {
        verify(fileOutput).getFileOutputStream("graph.grp");
    }

    @Test
    public void get_index_output_writer() throws IOException {
        verify(fileOutput).getWriter("index.idx");
    }

    @Test
    public void drops_node_connections_after_writing_to_file() {
        Node node = new Node("node-name");
        node.addConnection(new Node("child-node"), 1);

        graphStore.write(node);

        assertEquals(0, node.connectedConcepts.size());
    }

    @Test
    public void writes_node_index_to_file() throws IOException {
        Node node = new Node("node-name");
        node.addConnection(new Node("child-node"), 1);

        graphStore.write(node);

        verify(indexFile).write(anyString());
    }
}