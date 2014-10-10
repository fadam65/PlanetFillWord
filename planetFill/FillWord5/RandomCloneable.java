/* RandomCloneable.java
   An extension of java.util.Random that implements Cloneable.
   Dr. Dale Parson, CSC 243, Spring 2013
*/

package FillWord5 ;
import java.util.Random;
import java.io.* ;

/**
    Class RandomCloneable adds a clone() method to clone a
    Random object's state.

    @author Professor Dale Parson
**/
public class RandomCloneable extends Random implements Cloneable {
    // serialVersionUID encodes the version of serializable state.
    private static final long serialVersionUID = 042013L ;
    /** Invoke the random constructor without a seed. **/
    public RandomCloneable() {
        super();
    }
    /** Invoke the random constructor with a seed. **/
    public RandomCloneable(long seed) {
        super(seed);
    }
    /**
     *  clone() implements the Cloneable interface by serializing and
     *  then deserializing a copy of this object.
    **/
    public RandomCloneable clone() {
        RandomCloneable result = null ;
        try {
            // 1. Create a byte array into which to write Serializable object.
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            // 2. Create library object that can write Serializable objects.
            ObjectOutputStream pickle = new ObjectOutputStream(buffer);
            // 3. Write Serializable object to the byte array.
            pickle.writeObject(this);
            // 4. Flush to ensure it's all written.
            pickle.flush();
            // 5. Attach a reader to the actual, underlying byte [].
            ByteArrayInputStream unbuffer = new ByteArrayInputStream(
                buffer.toByteArray());
            // 6. Done with output, so close it.
            pickle.close();
            // 7. Create library object that can read Serializable objects.
            ObjectInputStream unpickle = new ObjectInputStream(unbuffer);
            // 8. Read (deserialize) a copy of the original object.
            result = (RandomCloneable) unpickle.readObject();
            // 9. Close it. Done.
            unpickle.close();
        } catch (Exception shouldNotHappen) {
            System.err.println("DEBUG INTERNAL ERROR CLONING RANDOM: "
                + shouldNotHappen.getMessage());
        }
        return result ;
    }
}
