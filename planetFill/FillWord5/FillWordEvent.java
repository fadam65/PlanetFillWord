/* FillWordEvent.java
   An Event class that an IFillWord-derived class propagates to
   subscribers after each change of game state.
   Dr. Dale Parson, CSC 243, Spring 2013
*/

package FillWord5 ;

/**
    Class FillWordEvent propagates to subscribers after each change of
    game state.
    @see FillWordEventListener
    @author Professor Dale Parson
**/
public class FillWordEvent extends java.util.EventObject {
    // serialVersionUID encodes the version of serializable state.
    private static final long serialVersionUID = 042013L ;
    private final IFillWord changedGame ;
    /**
     *  Construct a game object and forward it to subscribers after each
     *  change of game state.
     *  @param game is the game making the state change, which also serves as
     *  the event source. This constructor actually stores a clone() of game.
     *  @see FillWordEventListener
    */
    public FillWordEvent(IFillWord game) {
        super(game);
        changedGame = game.clone() ;
    }
    /**
     *  Retrieve a copy of the game that triggered the event.
     *  @return a clone of the changed game.
     *  @see FillWordEventListener
    **/
    public IFillWord getGame() {
        return changedGame.clone();
    }
}
