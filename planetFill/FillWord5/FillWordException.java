/* FillWordException.java
   An Exception thrown by a FillWord* on an error.
   Demonstration of Java interfaces and packages.
   Dr. Dale Parson, CSC 243, Spring 2013
*/

package FillWord5 ;

/**
    Class FillWordException is thrown by the FillWord* game
    on any game error, typically on an error caused by an invalid
    command from the user..

    @author Professor Dale Parson
**/
public class FillWordException extends Exception {
    // serialVersionUID encodes the version of serializable state.
    private static final long serialVersionUID = 042013L ;
    /**
     *  Construct an exception for a FillWord game with message text.
     *  @param text error message for getMessage().
    */
    public FillWordException(String text) {
        super(text);
    }
    /**
     *  Construct an exception for a FillWord game with message text
     *  and a cause.
     *  @param text error message for getMessage().
     *  @param cause the underlying, lower-level cause of the problem.
    */
    public FillWordException(String text, Throwable cause) {
        super(text, cause);
    }
}
