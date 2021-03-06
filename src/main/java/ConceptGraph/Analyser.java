package ConceptGraph;

import ConceptGraph.DataStructures.Node;
import ConceptGraph.DataStructures.SizedMaxMap;
import ConceptGraph.Output.Logging.Logger;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyser {
    private Logger logger;

    public Analyser(Logger logger) {
        this.logger = logger;
    }

    public void getTopConnectedConcepts() throws FileNotFoundException {
        Node root = get("biathlon");

        logger.log("Querying " + root.toStringWithoutChildren());

        HashMap<String, Integer> frequencies = getFrequencies();

        String strMax = "";
        for(String str : frequencies.keySet()){
            if(str.length() > strMax.length()){
                strMax = str;
            }
        }

        try {
            int max = frequencies.get("a");
            // this is fucking ridiculous - use a treemap<K, V>?
            SizedMaxMap arr = new SizedMaxMap(10);

            Object[] keys = root.connectedConcepts.keySet().toArray();

            Object[] vals = root.connectedConcepts.values().toArray();

            for(int i = 0; i < root.connectedConcepts.size(); i++) {
                double val = (double) ((int) vals[i]);
                String name = ((Node) keys[i]).name;

                Integer frequency = frequencies.get(name);
                if(frequency == null){
                    continue;
                }
                double relativeStrength = Math.pow(1- (frequency / (double) max), 2);
                double weightedStrength = val * relativeStrength;

                arr.insert(weightedStrength, keys[i]);
            }

            for(int i = 0; i < arr.maxSize; i++){
                System.out.println(((Node)arr.getValues()[i]).name + " - " + arr.getKeys()[i]);
            }
        }
        catch (InvalidArgumentException e){
            logger.logException(e);
            logger.log("You have full control of this. Just don't set it to 0");
        }
    }

    private Node get(String nodeName) throws FileNotFoundException {
        String path = "output/graph/" + nodeName.replace(" ", "_") + ".grp";
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);

        String root = scanner.nextLine();
        Pattern regex = Pattern.compile("!\"(.*)\" - https?.*");
        Matcher matcher = regex.matcher(root);
        if(matcher.find()) {
            root = matcher.group(1);
        }

        Node rootNode = new Node(root);
        while(scanner.hasNextLine()){
            String curr = scanner.nextLine();
            if(curr.startsWith("!")){
                break;
            }
            regex = Pattern.compile("\\s(\\d*): \"(.*)\"");
            matcher.usePattern(regex);
            matcher.reset(curr);

            if(matcher.find()) {
                int weight = Integer.parseInt(matcher.group(1));
                curr = matcher.group(2);
                Node currNode = new Node(curr);
                rootNode.addConnection(currNode, weight);
            }
        }

        return rootNode;
    }

    public HashMap<String,Integer> getFrequencies() throws FileNotFoundException {
        HashMap<String, Integer> freqs = new HashMap<>();
        FileReader reader = new FileReader("output/freq_output.txt");
        Scanner in = new Scanner(reader);

        while(in.hasNextLine()){
            String line = in.nextLine();

            String[] content = line.split("=");

            freqs.put(content[0], Integer.parseInt(content[1]));
        }

        return freqs;
    }
}
