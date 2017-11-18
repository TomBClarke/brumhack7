package xyz.tomclarke.brumhack7;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FactParser{
    private String toParse;
    private Tree tree;
    private Map<Integer, CorefChain> corefGraph;
    private CoreMap sentence;
    private SemanticGraph dependencies;

    public FactParser(String str){
        toParse = str;
    }

    public void setToParse(String toParse) {
        this.toParse = toParse;
    }


    public void parse(){
        parse(this.toParse);
    }

    public void parse(String str){
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String toParse = str;

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(toParse);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        this.sentence =  sentences.get(0);


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

//        }
//
//        // This is the coreference link graph
//        // Each chain stores a set of mentions that link to each other,
//        // along with a method for getting the most representative mention
//        // Both sentence and token offsets start at 1!
        this.corefGraph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

    }

    public Tree getTree(){
        return this.tree;
    }

    public Map<Integer, CorefChain> getCorefGraph(){
        return this.corefGraph;
    }

    public CoreMap getSentence(){
        return this.sentence;
    }

    public SemanticGraph getDependencies(){
        return this.dependencies;
    }
}