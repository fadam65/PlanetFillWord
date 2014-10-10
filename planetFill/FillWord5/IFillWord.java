/*
    FillWordBasic.java, D. Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.io.File ;
import java.io.Serializable ;
import java.util.* ;

/**
 *  IFillWord is an interface that specifies operations for a
 *  class of word puzzle games that pull words from a dictionary
 *  and ask a player to fit the words onto a two-dimensional
 *  grid of letters. Detailed rules vary by game.
 *  By extending Cloneable this interface sets a requirement that
 *  any subclass must implement the clone() capabilities required
 *  by java.lang.Cloneable.
 *  By extending Serializable this interface sets a requirement that
 *  any subclass must implement the clone() capabilities required
 *  by java.io.Serializable.
**/
public interface IFillWord extends Cloneable, Serializable {
    /** Printable representation for an empty slot on the board. **/
    public final char EMPTY = '|' ;
    /**
     *  Make the first move in the game, after the constructor has
     *  finished its work. Call this method only once, immediately
     *  after construction.
     *  @throws FillWordException if call made after first move
    **/
    public void makeFirstMove() throws FillWordException ;
    /**
     *  Print the game state and a prompt for the next move to
     *  System.out.
    **/
    public void printBoard() ;
    /**
     *  Get a copy of the N x N board.
     *  @return a deep (cloned) copy of the game board
     *  The first dimension is the row, starting at the top row, and
     *  the second dimension is the column, starting at the left.
    **/
    public char [][] getBoard();
    /**
     *  Test whether an "undo" command would work at the present time.
     *  A return value of false could indicate that the undo stack is empty,
     *  or that undo is not supported by this game.
     *  @return true if an "undo" command is valid at this time, else false.
    **/
    public boolean canUndo();
    /**
     *  Test whether a "redo" command would work at the present time.
     *  A return value of false could indicate that the redo stack is empty,
     *  or that redo is not supported by this game.
     *  @return true if a "redo" command is valid at this time, else false.
    **/
    public boolean canRedo();
    /**
     *  Test whether this game board can grow via a negative number as the
     *  first command token to interpret, with a second, matching negative
     *  integer indicating a growth in board size.
     *  @return true if the board can grow, false otherwise.
    **/
    public boolean canGrow();
    /**
     *  Retrieve the current direction of play.
     *
     *  @return true for across or false for down.
    **/
    public boolean isAcross() ;
    /**
     *  Interpret a user command in the game.
     *  @param command holds a user command for the game.
     *  It may consist of > 1 space-separated tokens, according
     *  to the command set of the concrete game class.
     *  @throws FillWordException on an invalid user command, always
     *  with a getMessage(), optionally with a getCause().
    **/
    public void interpret(String command) throws FillWordException ;
    /**
     *  Retrieve the current word undergoing play.
     *
     *  @return the word being played.
    **/
    public String getWordToPlay() ;
    /**
     *  Retrieve the current score for this game.
     *
     *  @return the score.
    **/
    public int getSwaps();

    public int getSwapLimit();
 
   public int getScore() ;
    /**
     *  Return a cloned copy of this IFillWord-derived object, such that
     *  modiying the clone's state will not modify the original's state
     *  and vice versa. Consult java.lang.Cloneable and java.Object.clone
     *  for general documentation.
    **/
    public IFillWord clone();
    /**
     *  Add or deduct points from outside the game. Use this method for
     *  mechanisms such as move timers that are outside the game object.
     *  @param points value to add or negative for deduction.
    **/
    public void addOrDeductPoints(int points);
    /**
     *  FillWordRecord provides a means for a game to record & report
     *  the valid move that just completed. An object of FillWordRecord is
     *  immutable.
     *  @see getThisMove
    **/
    public static class FillWordRecord implements Cloneable,
            java.io.Serializable {
        // serialVersionUID encodes the version of serializable state.
        private static final long serialVersionUID = 042013L ;
        /** word placed in the move **/
        public final String word ;
        /** row of the move **/
        public final int row ;
        /** column of a move **/
        public final int column ;
        /** direction of move, true for across, false for down **/
        public final boolean isacross ;
        /**
         *  Construct a move record from its word, row, column and
         *  direction, where isacross == true for a horizontal word.
        **/
        public FillWordRecord(String word, int row, int column,
                boolean isacross) {
            this.word = word ;
            this.row = row ;
            this.column = column ;
            this.isacross = isacross ;
        }
        /**
         *  Return the reference to this immutable object as its own clone.
        **/
        public FillWordRecord clone() {
            return this ;
        }
    }
    /**
     *  Retrieve the move that just completed or null if there was none.
     *  @return the move that just completed or null if there was none.
    **/
    public FillWordRecord getThisMove() ;
    /**
     *  Add a FillWordEventListener to this game. After every update to
     *  the state of the game, the game will construct and send a
     *  FillWordEvent to each subscriber.
     *  @see IFillWord#removeFillWordEventListener
    **/
    public void addFillWordEventListener(FillWordEventListener subscriber);
    /**
     *  Remove a FillWordEventListener added by addFillWordEventListener.
     *  @see IFillWord#addFillWordEventListener
    **/
    public void removeFillWordEventListener(FillWordEventListener subscriber);
}
