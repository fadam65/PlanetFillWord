/*
    FillWordBasic.java, D. Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.io.File ;
import java.util.* ;

/**
 *  FillWordBasic is a basic word game that lets players place simple
 *  crosswords on a fixed-size game board. It does not support
 *  "emergent words," which are crosswords not explicitly placed;
 *  placed words may or may not cross existing words, but they may not abut
 *  letters from other words along their sides. CSC 243 will extend this game
 *  during Spring 2013.
**/
public class FillWordBasic extends FillWordHelper {
    // serialVersionUID encodes the version of serializable state.
    private static final long serialVersionUID = 042013L ;
    /**
     *  Construct a new game to play. It calls its base class constructor
     *  but nothing else.
     *
     *  @see FillWordHelper#FillWordHelper
    **/
    public FillWordBasic(Integer boardSizeI, Scanner dictScanner,
            Integer rndseedI) {
        super(boardSizeI, dictScanner, rndseedI);
    }
    public boolean canGrow() {
        return false ;
    }
    /**
     *  Construct an incompletely-constructed game to play using the
     *  FillWordHelper constructor of the same parameter type.
     *  This constructor is useful as a prelude to cloning via helpClone().
     *  @param existingDictionary is a dictionary already populated
     *  by another FillWordHelper-derived object. This object shares
     *  that dictionary as its own.
     *  @see FillWordHelper#FillWordHelper
     *  @see FillWordHelper#helpClone
    **/
    protected FillWordBasic(List<String> existingDictionary) {
        super(existingDictionary);
    }
    /**
     *  Return a cloned copy of this object.
     *  @see IFillWord#clone
    **/
    public FillWordBasic clone() {
        FillWordBasic result = new FillWordBasic(dictionary);
        result.helpClone(this);
        return result ;
    }
    /**
     *  Make the first move in the game, after the constructor has
     *  finished its work. Call this method only once, immediately
     *  after construction.
     *  @throws FillWordException if move made after first move
    **/
    public void makeFirstMove() throws FillWordException {
        wordToPlay = pickWord();
        placeWord(0, randomNumGenerator.nextInt(board.length), true);
        // 1st word down from top, middle column
    }
    /**
     *  Place the current word at the ROW COLUMN location given by
     *  command OR "save" or "restore" the state of the game in
     *  a file named by the token following "save" or "restore".
     *  Placing at ROW COLUMN saves the state of the game prior to any
     *  changes onto an undo stack and clears the redo stack.
     *  Invoking "save FILENAME" has no effect on these stacks.
     *  Invoking "restore FILENAME" restores the stacks in effect when
     *  the game was saved to FILENAME. Invoking "undo" as a command
     *  pushes a copy of the current game state to the redo stack and
     *  then sets the state of the game to the state popped off of
     *  the undo stack; invoking "redo" as a command pushes a copy of
     *  the current game state to the undo stack and then sets the state
     *  of the game to the state popped off of the redo stack; a check
     *  is made on the stack-to-be-popped prior to changing any state
     *  for undo or redo and, if the required stack is empty, interpret
     *  throws FillWordException without changing any state.
     *  This method invokes processGameEvent on every FillWordEventListener
     *  on every FillWordEventListener subscribed via addFillWordEventListener.
     *  @param command holds two integers separated by a space,
     *  OR "save FILENAME" or "restore FILENAME" where FILENAME is
     *  the file that saves state of a game.
     *  @throws FillWordException on any invalid command
     *  @see IFillWord#addFillWordEventListener
     *  @see FillWordEventListener
     *  @see FillWordEvent
    **/
    public void interpret(String command) throws FillWordException {
        int row = -1, col = -1;
        FillWordBasic myclone = clone();
        Scanner linescan = new Scanner(command);    // parse the command string
        try {
            if (linescan.hasNextInt()) {
                row = linescan.nextInt();
                col = linescan.nextInt();
                argsdone(command, linescan);
                FillWordRecord record = new FillWordRecord(wordToPlay,
                    row, col, isacross);
                placeWord(row, col);
                if (row > -1 && col > -1) {
                    thismove = record ;
                }
                undoStack.push(myclone);
                redoStack.clear();
            } else {
                String cmd = linescan.next();
                if (cmd.equals("save")) {
                    String fname = linescan.next();
                    argsdone(command, linescan);
                    helpSave(fname);
                    System.out.println("Saved game in file "
                        + fname + ".");
                } else if (cmd.equals("restore")) {
                    String fname = linescan.next();
                    argsdone(command, linescan);
                    helpRestore(fname);
                    System.out.println("Restored game from file "
                        + fname + ".");
                } else if (cmd.equals("undo")) {
                    argsdone(command, linescan);
                    if (undoStack.empty()) {
                        throw new FillWordException(
                            "Undo stack is empty on 'undo' command.");
                    }
                    helpClone(undoStack.pop());
                    redoStack.push(myclone);
                } else if (cmd.equals("redo")) {
                    argsdone(command, linescan);
                    if (redoStack.empty()) {
                        throw new FillWordException(
                            "Redo stack is empty on 'redo' command.");
                    }
                    helpClone(redoStack.pop());
                    undoStack.push(myclone);
                } else {
                    throw new FillWordException(
                        "Unknown command: " + cmd);
                }
            }
        } catch (FillWordException fwx) {
            throw fwx ;
        } catch (Exception anyproblem) {
            throw new FillWordException(
                "ERROR placing word at " + command
                + ": " + anyproblem.getClass().toString() + ": "
                + anyproblem.getMessage(), anyproblem);
        } finally {
            FillWordEvent evt = new FillWordEvent(this);
            for (FillWordEventListener l : subscribers) {
                l.processGameEvent(evt);
            }
        }
    }
    // Helper method for interpret. Ensure no more command line arguments.
    private void argsdone(String command, Scanner linescan)
            throws FillWordException {
        if (linescan.hasNext()) {
            throw new FillWordException(
                "Invalid unused arguments for command '"
                    + command +"' starting at '" + linescan.next() + "'");
        }
    }
    /** Place a word onto the board.
     *  This version of the game allows but does not require crossing
     *  an existing word after the first move. Re-implementing this
     *  method to pass isfirst as false would change to require crossing
     *  after the first word.
     *  This implementation calls placeWord(row, col, true);
     *  @param row is row from top for start of word.
     *  @param col is column from left for start of word.
     *  @throws FillWordException on any kind of illegal word placement
    **/
    protected void placeWord(int row, int col) throws FillWordException {
        placeWord(row, col, true);
    }
    /** Place a word onto the board.
     *  @param row is row from top for start of word.
     *  @param col is column from left for start of word.
     *  @param isfirst if this word is NOT required to cross another word.
     *  @throws FillWordException on any kind of illegal word placement
    **/
    protected void placeWord(int row, int col, boolean isfirst)
            throws FillWordException {
        int rowdelta, coldelta ;
        boolean crossLetter = false ;
        if (row < 0 && col < 0 && swaps < swapLimit) {
            // Swap the word and deduct the points.
            System.out.println("Discard word " + wordToPlay
                + " at a cost of " + wordToPlay.length() + " points.");
            score -= wordToPlay.length() ;
            if (row <= -3 && row == col) {
                int newlimit = - row ;
                if (newlimit <= board.length) {
                    wordSizeLimit = newlimit ;
                    System.out.println("Word length limit set to "
                        + newlimit + ".");
                }
            }
            wordToPlay = pickWord();
            swaps++ ;
            isacross = ! isacross ;
            return ;
        }
        if (isacross) {
            rowdelta = 0 ;
            coldelta = 1 ;
        } else {
            rowdelta = 1 ;
            coldelta = 0 ;
        }
        // 1. The first loop checks for errors before placing the letters.
        for (int charpos = 0 ; charpos < wordToPlay.length() ; charpos++) {
            int rowindex = row + charpos * rowdelta ;
            int colindex = col + charpos * coldelta ;
            char toplace = wordToPlay.charAt(charpos);
            if (rowindex < 0 || rowindex >= board.length || colindex < 0
                    || colindex >= board.length) {
                throw new FillWordException("ERROR, position " + rowindex
                    + " " + colindex + " for letter " + toplace
                    + " is off the board.");
            }
            char current = board[rowindex][colindex];
            if (current != EMPTY && current != toplace) {
                // In the future we will make this an exception.
                throw new FillWordException("ERROR, position " + rowindex
                    + " " + colindex + " for letter " + toplace
                    + " already holds letter " + current + ".");
            }
            // Make sure there is no letter immediately before the
            // first in the direction of play.
            if (charpos == 0) {
                int crossrow = rowindex - rowdelta ;
                int crosscol = colindex - coldelta ;
                if (crossrow >= 0 && crosscol >= 0
                        && board[crossrow][crosscol] != EMPTY) {
                    throw new FillWordException(
                        "ERROR, position " + rowindex
                            + " " + colindex + " for letter " + toplace
                            + " is immediately after letter "
                            + board[crossrow][crosscol] + ".");
                }
            }
            // Make sure there is no letter immediately after the
            // last in the direction of play.
            if (charpos == (wordToPlay.length()-1)) {
                int crossrow = rowindex + rowdelta ;
                int crosscol = colindex + coldelta ;
                if (crossrow < board.length && crosscol < board.length
                        && board[crossrow][crosscol] != EMPTY) {
                    throw new FillWordException(
                        "ERROR, position " + rowindex
                            + " " + colindex + " for letter " + toplace
                            + " is immediately before letter "
                            + board[crossrow][crosscol] + ".");
                }
            }
            if (current == toplace) {
                crossLetter = true ;
            } else {
                // We are over-writing a blank. Make sure we don't abut
                // a letter to the side (perpendicular).
                int crossrow = rowindex - coldelta ;    // left or up
                int crosscol = colindex - rowdelta ;
                if (crossrow >= 0 && crosscol >= 0
                        && board[crossrow][crosscol] != EMPTY) {
                    throw new FillWordException(
                        "ERROR, position " + rowindex
                            + " " + colindex + " for letter " + toplace
                            + " is immediately adjacent to letter "
                            + board[crossrow][crosscol] + ".");
                }
                crossrow = rowindex + coldelta ;    // right or down
                crosscol = colindex + rowdelta ;
                if (crossrow < board.length && crosscol < board.length
                        && board[crossrow][crosscol] != EMPTY) {
                    throw new FillWordException(
                        "ERROR, position " + rowindex
                            + " " + colindex + " for letter " + toplace
                            + " is immediately adjacent to letter "
                            + board[crossrow][crosscol] + ".");
                }
            }
        }
        if (! (crossLetter || isfirst)) {
            // In the current implementation we will never enter this block.
            // We may add a rule requiring a word to cross a word in rev. 2.
            throw new FillWordException(
                "ERROR, word must cross another word.");
        }
        // 2. The second loop places the letters.
        for (int charpos = 0 ; charpos < wordToPlay.length() ; charpos++) {
            int rowindex = row + charpos * rowdelta ;
            int colindex = col + charpos * coldelta ;
            char toplace = wordToPlay.charAt(charpos);
            board[rowindex][colindex] = toplace ;
        }
        // 3. Switch direction and pick a new word for the next turn.
        score += wordToPlay.length();
        isacross = ! isacross ;
        wordToPlay = pickWord();
        swaps = 0 ;
    }
}
