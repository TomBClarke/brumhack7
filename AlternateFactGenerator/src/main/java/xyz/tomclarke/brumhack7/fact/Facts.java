package xyz.tomclarke.brumhack7.fact;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A list of fact objects, with save and load methods.
 * 
 * @author Tom Clarke
 *
 */
public class Facts extends ArrayList<Fact> implements Serializable {

    private static final long serialVersionUID = 4752288137917613796L;

    // Tools for saving / loading
    private static final String serFileName = "facts.ser";

    public void save() throws FileNotFoundException, IOException {
        File oldSer = new File(serFileName);
        if (oldSer.exists()) {
            oldSer.delete();
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFileName));
        oos.writeObject(this);
        oos.close();
    }

    public static Facts load() throws FileNotFoundException, IOException, ClassNotFoundException {
        if (new File(serFileName).exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFileName));
            Facts facts = (Facts) ois.readObject();
            ois.close();
            return facts;
        } else {
            // No previous data
            return null;
        }
    }

}
