package xyz.tomclarke.brumhack7.fact;

import java.io.Serializable;

import edu.stanford.nlp.util.CoreMap;

/**
 * A fact representation - the original, the annotations of the original and the
 * alternative when made
 * 
 * @author Tom Clarke
 *
 */
public class Fact implements Serializable {

    private static final long serialVersionUID = 1434839700214501552L;
    private final String original;
    private CoreMap annotations;
    private String alternative;

    public Fact(String original) {
        this.original = original;
        this.annotations = null;
        this.alternative = null;
    }

    public String getAlternative() {
        return alternative;
    }

    public void setAnnotations(CoreMap annotations) {
        this.annotations = annotations;
    }

    public CoreMap getAnnotations() {
        return annotations;
    }

    public void setAlternative(String alternative) {
        this.alternative = alternative;
    }

    public String getOriginal() {
        return original;
    }

    @Override
    public String toString() {
        String returnString = original;
        if (alternative != null) {
            returnString += " -> " + alternative;
        }
        return returnString;
    }

}
