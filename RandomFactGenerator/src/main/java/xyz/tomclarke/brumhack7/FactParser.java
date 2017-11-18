package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * A class to parse a single String, generating the annotations for it
 * @author Dave Marlow
 */
public class FactParser{
//    private boolean readyToParse = false;
    private String toParse = "";
    private Tree tree;
    private CoreMap sentence;
    private SemanticGraph dependencies;
    private StanfordCoreNLP pipeline;

    /**
     * Constructor
     * @param str The string to parse
     * @param pipeline The pipeline to set up the parser with
     */
    public FactParser(String str, StanfordCoreNLP pipeline){
        toParse = str;
        this.pipeline = pipeline;
//        readyToParse = true;
    }

    /**
     * Constructor
     * @param pipeline The pipeline to set up the parser with
     */
    public FactParser(StanfordCoreNLP pipeline){
        this.pipeline = pipeline;
        toParse = "";
    }

    /**
     * Setter for string to parse
     * @param toParse String to parse
     */
    public void setToParse(String toParse) {
        this.toParse = toParse;
//        readyToParse = true;
    }

    /**
     * Parse the previously input string
     */
    public void parse(){
        parse(this.toParse);
    }

    /**
     * Parse the specified string
     * @param str the string to parse
     */
    public void parse(String str){
//        if(!readyToParse || str.length() == 0){
//            // parsing an empty string, none set
//            return;
//        }
//
        String toParse = str;

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(toParse);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        if (sentences.isEmpty()) {
            this.sentence = null;
            return;
        }

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(TextAnnotation.class);
//                // this is the POS tag of the token
//                String pos = token.get(PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String ne = token.get(NamedEntityTagAnnotation.class);
//
//                System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
//
//            }

        // this is the parse tree of the current sentence

        this.tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//        System.out.println("parse tree:\n" + this.tree);

        // this is the Stanford dependency graph of the current sentence
        this.dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
//        System.out.println("dependency graph:\n" + this.dependencies);
    }
    }

    /**
     * Getter to return the tree
     * @return the tree that has been created from the last parse
     */
    public Tree getTree(){
        return this.tree;
    }

    /**
     * Getter to return the tree for a specified sentence
     * @param sentence the specified sentence
     * @return the tree for that sentence
     */
    public Tree getTree(CoreMap sentence){
        return sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
    }

    /**
     * Getter for the coremap
     * @return coremap sentence
     */
    public CoreMap getSentence(){
        return this.sentence;
    }

    /**
     * getter for dependency graph
     * @return SemanticGraph of dependencies
     */
    public SemanticGraph getDependencies(){
        return this.dependencies;
    }

    /**
     * getter for dependency graph for a specific sentence
     * @param sentence  The specific sentence to return this for
     * @return SemanticGraph of dependencies
     */
    public SemanticGraph getDependencies(CoreMap sentence){
        return sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
    }
}