package ConceptGraph;

import ConceptGraph.Input.Scraper;
import ConceptGraph.Output.FileOutputAssistant;
import ConceptGraph.Output.Logging.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AppShould{
    private static boolean scrapeWasCalled;
    private static boolean analyseWasCalled;
    private static Analyser mockAnalyser;
    private static Scraper mockScraper;
    private App app;

    @Before
    public void setup(){
        scrapeWasCalled = false;
        analyseWasCalled = false;
        mockAnalyser = mock(Analyser.class);
        mockScraper = mock(Scraper.class);
        app = new App(mockScraper, mockAnalyser);
    }

    @Test
    public void delegate_to_analyser() throws IOException {
        analyse();
        verify(mockAnalyser).getTopConnectedConcepts();
    }

    @Test
    public void delegate_to_scraper() throws IOException {
        scrape();
        verify(mockScraper).scrape();
    }

    @Test
    public void handle_IOexceptions_thrown_by_scraper() throws IOException {
        doThrow(new IOException()).when(mockScraper).scrape();

        scrape();
    }

    private void scrape() throws IOException {
        app.run(false);
    }

    private void analyse() throws IOException {
        app.run(true);
    }
}
