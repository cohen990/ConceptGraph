package ConceptGraph;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        boolean analyse = false;
        analyse = true;
        if(analyse){
            Analyser analyser = new Analyser();
            analyser.getTopConnectedConcepts();
        }
        else{
            Scraper scraper = new Scraper();
            scraper.scrape(args);
        }
    }
}

