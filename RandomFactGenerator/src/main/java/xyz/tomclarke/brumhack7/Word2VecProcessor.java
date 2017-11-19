package xyz.tomclarke.brumhack7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

/**
 * Uses word2vec
 * 
 * @author tbc452
 *
 */
public class Word2VecProcessor {

    /**
     * Loads the Word2Vec object
     * 
     * @return Word2Vec loaded object
     */
    public static Word2Vec getWord2Vec() {
        return WordVectorSerializer.readWord2VecModel(new File("/home/tom/Downloads/W2V_GoogleNews.bin.gz"));
    }

    /**
     * Gets words similar to the words given
     * 
     * @param words
     *            Words to process
     * @param vec
     *            The Word2Vec object
     * @return similar words (max 20)
     */
    public static List<String> process(List<String> words, Word2Vec vec) throws Exception {
        return new ArrayList<String>(vec.wordsNearest(words, new ArrayList<String>(), 20));
    }
}
