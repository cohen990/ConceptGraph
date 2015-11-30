package ConceptGraph.Input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by Dan on 26/11/2015.
 */
public class WikiDumpReader extends BufferedReader {

    public WikiDumpReader(Reader in, int sz) {
        super(in, sz);
    }

    public WikiDumpReader(Reader in) {
        super(in);
    }

    public String getPage() throws IOException {
        StringBuilder page = scanUntilPageStart();
        if(page == null){
            return null;
        }

        String line;
        while((line = this.readLine()) != null){
            page.append(line);
            if(line.contains("</page>")){
                break;
            }
        }

        return page.toString();
    }

    private StringBuilder scanUntilPageStart() throws IOException {
        String line = "";
        while((line = this.readLine()) != null){
            if(line.contains("<page>")){
                return new StringBuilder(line);
            }
        }

        return null;
    }
}
