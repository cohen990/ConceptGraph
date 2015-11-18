package ConceptGraph;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyser {
    private StringHasher Hasher = new StringHasher();

    public void getTopConnectedConcepts() throws FileNotFoundException {
        Node root = get("Botany");

        try {
            // this is fucking ridiculous - use a treemap<K, V>?
            SizedMaxMap arr = new SizedMaxMap(10);

            Object[] keys = root.connectedConcepts.keySet().toArray();

            Object[] vals = root.connectedConcepts.values().toArray();
            for(int i = 0; i < root.connectedConcepts.size(); i++) {
                arr.insert((Integer)vals[i], keys[i]);
            }

            for(int i = 0; i < arr.maxSize; i++){
                System.out.format("%s - %d\n", ((Node)arr.getValues()[i]).name,arr.getKeys()[i]);
            }
        }
        catch (InvalidArgumentException e){
            e.printStackTrace();
            System.out.println("You have full control of this. Just don't set it to 0");
        }
    }

    private Node get(String nodeName) throws FileNotFoundException {
        String path = "graph/" + Hasher.simpleHash(nodeName) + ".grp";
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
}

//import java.io.Console;
//import java.util.regex.Pattern;
//import java.util.regex.Matcher;
//
//public class Analyser {
//
//    public static void getTopConnectedConcepts(String[] args){
//        Console console = System.console();
//            Pattern pattern =
//                    Pattern.compile(".*\\((.*)\\).*");
//
//            Matcher matcher =
//                    pattern.matcher("any string (in brackets) but not out");
//
//            boolean found = false;
//            while (matcher.find()) {
//                System.out.format("I found the text" +
//                                " \"%s\" starting at " +
//                                "index %d and ending at index %d.%n",
//                        matcher.group(),
//                        matcher.start(),
//                        matcher.end());
//                found = true;
//            }
//            if(!found){
//                System.out.format("No match found.%n");
//            }
//    }
//}

