package ConceptGraph;

import java.io.File;
import java.io.IOException;

/**
 * Created by danco on 29/11/2015.
 */
public abstract class GraphStore {
    private final Logger logger;

    abstract void writeNodeToFile(Node node);

    protected GraphStore(){
        this.logger = FileLogger.Create();
    }

    protected void createDirectoryIfNotExists(String path){
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdir()) {
                logger.log("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }
}
