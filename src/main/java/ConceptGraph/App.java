package ConceptGraph;

import ConceptGraph.Input.Scraper;
import ConceptGraph.Input.WikiDumpReader;
import ConceptGraph.Input.WikiPageXmlParser;
import ConceptGraph.Input.XmlScraper;
import ConceptGraph.Output.FileOutputAssistant;
import ConceptGraph.Output.Logging.FileLogger;
import ConceptGraph.Output.Logging.Logger;
import ConceptGraph.Output.Storage.GraphStore;
import ConceptGraph.Output.Storage.IndividualFilesGraphStore;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class App {
    public static void main(String[] args) throws IOException {
        boolean analyse = false;
//        analyse = true;
        FileOutputAssistant fileOutput = new FileOutputAssistant();
        Logger logger = FileLogger.Create(fileOutput);
        if(analyse){
            analyse(logger);
        }
        else{
            scrape(fileOutput, logger);
        }
    }

    private static void analyse(Logger logger) throws FileNotFoundException {
        Analyser analyser = new Analyser(logger);
        analyser.getTopConnectedConcepts();
    }

    private static void scrape(FileOutputAssistant fileOutput, Logger logger) throws IOException {
        Scraper scraper = getScraper(fileOutput, logger);
        scraper.scrape();
    }

    private static Scraper getScraper(FileOutputAssistant fileOutput, Logger logger) throws FileNotFoundException {
        Processor processor = new Processor(fileOutput);
        GraphStore graphStore = new IndividualFilesGraphStore(fileOutput, logger);
        Reader baseReader = new FileReader("C:\\wikidump\\enwiki-20150205-pages-articles-multistream.xml");
        WikiDumpReader wikiReader = new WikiDumpReader(baseReader);
        WikiPageXmlParser xmlParser = new WikiPageXmlParser(logger);
        return new XmlScraper(processor, logger, graphStore, wikiReader, xmlParser);
    }
}