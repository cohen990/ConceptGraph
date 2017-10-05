package ConceptGraph.Output.Storage;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.Output.Logging.Logger;

import java.io.File;

/**
 * Created by danco on 29/11/2015.
 */
public abstract class GraphStore {
    protected final Logger logger;

    public abstract void write(Node node);

    public abstract void finalize();

    protected GraphStore(Logger logger){
        this.logger = logger;
    }
}
