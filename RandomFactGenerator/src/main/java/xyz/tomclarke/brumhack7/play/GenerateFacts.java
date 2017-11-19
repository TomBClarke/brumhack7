package xyz.tomclarke.brumhack7.play;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.deeplearning4j.models.word2vec.Word2Vec;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import javafx.util.Pair;
import xyz.tomclarke.brumhack7.BuildFactData;
import xyz.tomclarke.brumhack7.FactParser;
import xyz.tomclarke.brumhack7.Word2VecProcessor;
import xyz.tomclarke.brumhack7.fact.Fact;
import xyz.tomclarke.brumhack7.fact.Facts;

/**
 * Generates facts
 * 
 * @author Tom Clarke
 *
 */
public class GenerateFacts {

    public static void main(String[] args) throws Exception {
        Facts facts = BuildFactData.buildFactData(false);

        // Write generated facts out to file
        BufferedWriter bw = null;
        try {
            File fout = new File("alternate_facts.txt");
            FileOutputStream fos = new FileOutputStream(fout);

            bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < 10; i++) {
            }

            GenerateFacts gen = new GenerateFacts();
            for (Fact fact : facts) {
                try {
                    gen.processFact(fact);

                    // Only write fact if it exists...
                    if (fact.getAlternative() != null) {
                        bw.write(fact.getAlternative());
                        bw.newLine();
                    }
                    facts.save();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } finally {
            bw.close();
        }
    }

    private StanfordCoreNLP pipeline;
    private Word2Vec vec;
    private List<String> words;

    public GenerateFacts() throws Exception {
        // Read a tonne of linux words
        BufferedReader br = new BufferedReader(new FileReader("/usr/share/dict/words"));
        words = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null) {
            words.add(line);
        }
        br.close();
    }

    /**
     * Processes a fact and generates an alternate fact
     * 
     * @param fact
     *            The fact to process
     * @throws Exception
     */
    public void processFact(Fact fact) throws Exception {
        System.out.println("Next: " + fact.getOriginal());
        // Ensure parsed
        if (fact.getAnnotations() == null) {
            System.out.println("Annotating...");
            FactParser parser = new FactParser(fact.getOriginal(), pipeline());
            parser.parse();
            fact.setAnnotations(parser.getSentence());
        }

        // Travel through tree to find a NP
        Tree tree = fact.getAnnotations().get(TreeCoreAnnotations.TreeAnnotation.class);
        List<Tree> nps = getLowestNPStrings(tree);
        // Largest string first
        nps.sort(new Comparator<Tree>() {
            @Override
            public int compare(Tree t1, Tree t2) {
                int wordCount1 = treeToString(t1).split(" ").length;
                int wordCount2 = treeToString(t2).split(" ").length;
                return wordCount2 - wordCount1;
            }
        });

        // Find end word
        String noun = getDeepestNoun(nps.get(0), 0).getKey();
        boolean swapped = false;
        // Ensure we don't get stuck on one forever
        int attempts = 0;
        while (!swapped) {
            attempts++;
            if (attempts > 10) {
                System.out.println("Giving up...");
                return;
            }
            // Add extra salt
            List<String> query = new ArrayList<String>();
            query.add(noun);

            // Choose number of extras to use
            int numOfExtras = 0;
            double randDecider = Math.random();
            if (randDecider < 0.5) {
                numOfExtras = 1;
            } else if (randDecider < 0.75) {
                numOfExtras = 2;
            } else {
                numOfExtras = 3;
            }

            for (int i = 0; i < numOfExtras; i++) {
                int index = (int) Math.round((words.size() - 1) * Math.random());
                query.add(words.get(index));
            }

            // Find alternates
            List<String> options = Word2VecProcessor.process(query, vec());

            if (options.isEmpty()) {
                // Try again
                continue;
            }

            // Parse and filter
            List<String> goodOptions = new ArrayList<String>();
            for (String option : options) {
                FactParser parser = new FactParser(option.replaceAll("_", " "), pipeline());
                parser.parse();
                if (parser.getTree().getChild(0).label().value().equals("NP")) {
                    goodOptions.add(option);
                }
            }

            if (goodOptions.isEmpty()) {
                // Try again
                continue;
            }

            // Choose and swap out
            String chosenOption = goodOptions.get((int) Math.round(goodOptions.size() * Math.random()));

            fact.setAlternative(fact.getOriginal().replaceAll(treeToString(nps.get(0)), chosenOption));
            swapped = true;
        }

        // When done
        if (fact.getAlternative() != null) {
            System.out.println("Made: " + fact.getAlternative());
        }
    }

    /**
     * Makes the pipeline is null
     * 
     * @return A loaded pipeline
     */
    private StanfordCoreNLP pipeline() {
        if (pipeline == null) {
            // creates a parsing pipeline
            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
            pipeline = new StanfordCoreNLP(props);
        }
        return pipeline;
    }

    /**
     * Makes a Word2Vec
     * 
     * @return
     */
    private Word2Vec vec() {
        if (vec == null) {
            vec = Word2VecProcessor.getWord2Vec();
        }
        return vec;
    }

    /**
     * Gets all base phrases for a sentence
     * 
     * @param tree
     *            The tree
     * @return A list of base phrases
     */
    private List<Tree> getLowestNPStrings(Tree tree) {
        List<Tree> nps = new ArrayList<Tree>();

        for (Tree child : tree.getChildrenAsList()) {
            if (child.label().value().equals("NP") && !hasNPChild(child, true)) {
                // Child is NP and doesn't have child NP at any point
                nps.add(child);
            } else {
                nps.addAll(getLowestNPStrings(child));
            }
        }

        return nps;
    }

    /**
     * Goes through tree and finds if there is an NP somewhere in there
     * 
     * @param tree
     *            The tree to interigate
     * @param isRoot
     *            Is it the root of the tested tree
     * @return If there is a noun phrase further down the tree
     */
    private boolean hasNPChild(Tree tree, boolean isRoot) {
        // Check self
        if (tree.label().value().equals("NP") && !isRoot) {
            return true;
        }

        // Check children
        for (Tree child : tree.getChildrenAsList()) {
            if (hasNPChild(child, false)) {
                return true;
            }
        }

        // No more NPs
        return false;

    }

    /**
     * Gets the string of a tree
     * 
     * @return The string inside the tree
     */
    private String treeToString(Tree tree) {
        String string = "";
        List<Tree> leaves = tree.getLeaves();
        for (Tree leaf : leaves) {
            String leafString = leaf.label().value();
            if (leafString.startsWith("'") || leafString.startsWith(",")) {
                // Shouldn't be a space
                string += leafString;
            } else {
                string += " " + leafString;
            }
        }

        return string.trim();
    }

    /**
     * Gets the deepest noun (or last if same depth)
     * 
     * @param tree
     * @return
     */
    private Pair<String, Integer> getDeepestNoun(Tree tree, int depth) {
        boolean isN = tree.label().value().startsWith("NN");
        if (isN) {
            return new Pair<String, Integer>(tree.getChild(0).label().value(), depth);
        }

        Pair<String, Integer> deepestSoFar = new Pair<String, Integer>(null, 0);
        for (Tree child : tree.getChildrenAsList()) {
            Pair<String, Integer> pair = getDeepestNoun(child, depth + 1);
            if (pair.getValue() >= deepestSoFar.getValue()) {
                deepestSoFar = pair;
            }
        }

        return deepestSoFar;
    }
}
