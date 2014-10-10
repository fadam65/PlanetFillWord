/*
    FillWordHelper.java, D. Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.util.* ;
import java.io.* ;

/**
 *  FillWordHelper is an abstract helper class used by concrete
 *  classes that implement operations specified in IFillWord.
**/
public abstract class FillWordHelper implements IFillWord {
    /** Limit on number of swaps in a move. **/
    protected static final int swapLimit = 10 ;
    /**
     *  The dictionary of words in the order in which they were scanned.
     *  The dictionary is transient in order to avoid storing or
     *  transmitting a big dictionary into a "save" file or across
     *  a network.
    **/
    protected transient List<String> dictionary = new ArrayList<String>();
    /**
     * subscribers is a list of FillWordEventListeners added by
     * addFillWordEventListener and removed by removeFillWordEventListener.
     * It is transient and therefore not saved by serialization.
     * helpClone() just copies the reference to this object, so that a
     * single session of a game shares its subscribers across clones.
    **/
    protected transient List<FillWordEventListener> subscribers = new LinkedList<FillWordEventListener>();
    /** The actual game board with subscripts [row][column] **/
    protected char [][] board = null ;
    /** Limit on the length of the longest word. **/
    protected int wordSizeLimit ;
    /**
     *  Random number generator used to select words from the dictionary.
     *  RandomCloneable is a subclass of Random with the ability to clone().
    **/
    protected RandomCloneable randomNumGenerator ;
    /** True to play across, false for down. **/
    protected boolean isacross = false ; // alternate words are across & down
    /** Next word to play in the game. **/
    protected String wordToPlay = null ;
    /** Player's current score. **/
    protected int score = 0 ;
    /** How many swaps made in current move. **/
    protected int swaps = 0 ;
    /** The undoStack supports unlimited undo of previous game moves. **/
    protected Stack<FillWordHelper> undoStack = new Stack<FillWordHelper>();
    /** The redoStack supports unlimited redo of previous game moves. **/
    protected Stack<FillWordHelper> redoStack = new Stack<FillWordHelper>();
    /** A record of the most recent move or null if there was none. **/
    protected FillWordRecord thismove = null ;
    /**
     *  Construct a new game to play. This constructor initializes
     *  the dictionary and the initially empty game board, along with
     *  other helper data fields, but it does not make the first (or any)
     *  move in the game.
     *
     *  @param boardSizeI gives width & height of the initial game board.
     *
     *  @param dictScanner is a scanner holding a dictionary of legal
     *  words to be scanned, with one legal word per line.
     *  FillWordHelper converts its internal copies of words to lower case.
     *
     *  @param rndseedI is a seed for a pseudo-random number generator that
     *  selects words. Using a value < 0 results in a different game
     *  each time; using a value >= 0 allows a game to be repeated
     *  for testing purposes.
    **/
    public FillWordHelper(Integer boardSizeI, Scanner dictScanner,
            Integer rndseedI) {
        int boardSize = boardSizeI.intValue();
        int rndseed = rndseedI.intValue();
        board = new char [ boardSize ][ boardSize ];
        wordSizeLimit = boardSize ;
        if (rndseed < 0) {
            randomNumGenerator = new RandomCloneable();
        } else {
            randomNumGenerator = new RandomCloneable(rndseed);
        }
        // Populate board with blank characters at the start.
        for (int row = 0 ; row < boardSize ; row++) {
            for (int col = 0 ; col < boardSize ; col++) {
                board[row][col] = EMPTY ;
            }
        }
        // Now copy all dictScanner lines into the dictionary.
        while (dictScanner.hasNextLine()) {
            String line = dictScanner.nextLine();
            line = line.trim();     // discard any leading / trailing blanks
            if (line.length() > 0) {    // do not store empty lines
                dictionary.add(line.toLowerCase());
            }
        }
    }
    /**
     *  Construct an incompletely-constructed game to play.
     *  This constructor is a helper that constructs an object with
     *  all fields in their default values except the dictionary. It
     *  is intended as a partial constructor to be completed by
     *  helpClone. Its goal is to support clone()ing while sharing
     *  the transient dictionary field across objects.
     *  @param existingDictionary is a dictionary already populated
     *  by another FillWordHelper-derived object. This object shares
     *  that dictionary as its own.
     *  @see FillWordHelper#helpClone
    **/
    protected FillWordHelper(List<String> existingDictionary) {
        dictionary = existingDictionary ;
    }
    /**
     *  Return a cloned copy of this object.
     *  @see IFillWord#clone
    **/
    public abstract IFillWord clone();
    /**
     *  Copy all state-bearing fields except the dictionary from
     *  the source object into this object. helpClone builds a new board
     *  matrix and copies the individual elements; it also invokes 
     *  randomNumGenerator.clone() to get a cloned copy of the
     *  RandomCloneable randomNumGenerator field, and it clones the
     *  undo and redo stacks. It just copies the primitive
     *  type fields as well as the wordToPlay String reference, which is
     *  immutable; String objects are immutable. Its goal is to support
     *  the clone() method in subclasses.
     *  @see FillWordHelper#FillWordHelper
    **/
    protected void helpClone(FillWordHelper source) {
        // I decided to keep the deep copy of undoStack/redoStack in
        // case we ever get into AIs playing multiple virtual games
        // with intersecting timelines.
        undoStack = (Stack<FillWordHelper>) source.undoStack.clone();
        redoStack = (Stack<FillWordHelper>) source.redoStack.clone();
        if (source.subscribers != null) {
        	// Be careful not to over-write a valid List with a restored null.
        	subscribers = source.subscribers ;
        }
        board = source.getBoard();
        // board = new char [source.board.length][source.board[0].length];
        for (int i = 0 ; i < source.board.length ; i++) {
            for (int j = 0 ; j < source.board[i].length ; j++) {
                board[i][j] = source.board[i][j];
            }
        }
        wordSizeLimit = source.wordSizeLimit ;
        isacross = source.isacross ;
        wordToPlay = source.wordToPlay ;
        score = source.score ;
        swaps = source.swaps ;
        randomNumGenerator = source.randomNumGenerator.clone();
        thismove = source.thismove ;
    }
    public void printBoard() {
        for (int row = 0 ; row < board.length ; row++) {
            for (int col = 0 ; col < board.length ; col++) {
                System.out.print(board[row][col]);  // NOT println
                System.out.print(' ');
                if (col > 9) {
                    System.out.print(' ');
                }
            }
            System.out.println("    (row = " + row + ")");
        }
        // print columns across the bottom
        for (int col = 0 ; col < board.length ; col++) {
            System.out.print(col);
            System.out.print(' ');
        }
        System.out.println("    (columns)");
        System.out.println("Score = " + score + " points, "
            + "word length limit = " + wordSizeLimit + ".\n");
        System.out.println("Word to play "
            + (isacross ? "ACROSS" : "DOWN") + ": " + wordToPlay);
        if (swaps < swapLimit) {
            System.out.print(
            "Enter ROW COLUMN pair, -N -N to swap, or CONTROL-D to quit: ");
        } else {
            System.out.print(
            "Enter ROW COLUMN pair, or CONTROL-D to quit: ");
        }
    }
    public char [][] getBoard() {
        char [][] result = new char[board.length][board.length];
        for (int i = 0 ; i < result.length ; i++) {
            for (int j = 0 ; j < result.length ; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result ;
    }
    public boolean canUndo() {
        return (! undoStack.empty());
    }
    public boolean canRedo() {
        return (! redoStack.empty());
    }
    /**
     *  Retrieve the current word undergoing play.
     *
     *  @return the word being played.
    **/
    public String getWordToPlay() {
        return wordToPlay ;
    }
    public boolean isAcross() {
        return isacross ;
    }

    public int getSwaps()
    {
	return swaps;
    }

    public int getSwapLimit()
    {
	return swapLimit;
    }
    /**
     *  Retrieve the current score for this game.
     *
     *  @return the score.
    **/
    public int getScore() {
        return score ;
    }
    public FillWordRecord getThisMove() {
        return thismove ;
    }
    // Pick a random word from the dictionary within the wordSizeLimit.
    // The word must be at least 2 characters long.
    protected String pickWord() {
        String result ;
        do {
            int wordIndex = randomNumGenerator.nextInt(dictionary.size());
            result = dictionary.get(wordIndex);
        } while (result.length() > wordSizeLimit || result.length() < 2);
        return result ;
    }
    public void addFillWordEventListener(FillWordEventListener subscriber) {
        subscribers.add(subscriber);
    }
    public void removeFillWordEventListener(FillWordEventListener subscriber) {
        subscribers.remove(subscriber);
    }
    public void addOrDeductPoints(int points) {
        score += points ;
        FillWordEvent evt = new FillWordEvent(this);
        for (FillWordEventListener l : subscribers) {
            l.processGameEvent(evt);
        }
    }
    /**
     *  Serialize and save this object in the named file.
     *  @param filename is the name of the file to create.
     *  @throws FillWordException as a chained exception
     *  on any output serialization failure.
     *  @see FillWordHelper#helpRestore
    **/
    protected void helpSave(String filename) throws FillWordException {
        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            ObjectOutputStream oostream = new ObjectOutputStream(fileout);
            oostream.writeObject(this);
            oostream.close();
        } catch (Exception ex) {
            throw new FillWordException("save fails: " + ex.getMessage(), ex);
        }
    }

    /**
     *  Retrieve and deserialize the state of the current object
     *  from a file previously created by helpSave.
     *  The retrieved object's state is cloned into this object's
     *  state via a call to helpClone.
     *  @param savefile is the name of the file to deserialize.
     *  @throws FillWordException as a chained exception
     *  on any output serialization failure.
     *  @see FillWordHelper#helpSave
     *  @see FillWordHelper#helpClone
    **/
    protected void helpRestore(String savefile) throws FillWordException {
        try {
            FileInputStream filein = new FileInputStream(savefile);
            ObjectInputStream oostream = new ObjectInputStream(filein);
            FillWordHelper oldgame = (FillWordHelper) oostream.readObject();
            oostream.close();
            helpClone(oldgame);
        } catch (Exception ex) {
            throw new FillWordException("restore fails: " + ex.getMessage());
        }
    }
}
