package ConceptGraph;

import ConceptGraph.Input.*;
import ConceptGraph.Output.*;
import ConceptGraph.Output.Logging.*;
import ConceptGraph.Output.Storage.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class App {
    private final FileOutputAssistant fileOutput;
    private final Logger logger;
    private Analyser analyser;
    private Processor processor;
    private Scraper scraper;

    public App() {
        fileOutput = getFileOutput();
        logger = FileLogger.Create(fileOutput);
    }

    public App(Scraper scraper, Analyser analyser) {
        this();
        this.scraper = scraper;
        this.analyser = analyser;
    }

    public static void main(String[] args) throws IOException {
        boolean shouldAnalyse = false;
        App app = new App();
        app.run(shouldAnalyse);
    }

    public void run(boolean analyse) throws IOException {
        if(analyse){
            analyse();
        }
        else{
            scrape();
        }
    }

    protected void analyse() throws FileNotFoundException {
        analyser = getAnalyser(logger);
        analyser.getTopConnectedConcepts();
    }

    protected void scrape() throws IOException {
        processor = getProcessor(fileOutput);
        scraper = getScraper(fileOutput, logger, processor);
        try {
            scraper.scrape();
        }
        catch(Exception e){
            handleFailedScrape(logger, processor, e);
        }
    }

    private FileOutputAssistant getFileOutput() {
        return new FileOutputAssistant();
    }

    protected Analyser getAnalyser(Logger logger) {
        return analyser == null
                ? new Analyser(logger)
                : analyser;
    }

    private Processor getProcessor(FileOutputAssistant fileOutput) {
        return new Processor(fileOutput);
    }

    private void handleFailedScrape(Logger logger, Processor processor, Exception e) throws IOException {
        logger.logException(e);
        processor.writeOutput();
    }

    protected Scraper getScraper(FileOutputAssistant fileOutput, Logger logger, Processor processor) throws IOException {
        if(scraper != null){
            return scraper;
        }
        GraphStore graphStore = new SingleFileGraphStore(fileOutput, logger);
        Reader baseReader = new FileReader("/Users/dancohen/Documents/enwiki-20170820-pages-articles.xml");
        WikiDumpReader wikiReader = new WikiDumpReader(baseReader);
        WikiPageXmlParser xmlParser = new WikiPageXmlParser(logger, fileOutput);
        return new XmlScraper(processor, logger, graphStore, wikiReader, xmlParser);
    }
}