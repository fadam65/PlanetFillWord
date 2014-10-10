/*
    FillWordGrows.java, D. Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.io.File ;
import java.util.* ;

/**
 *  FillWordGrows extends FillWordBasic by allowing a player to
 *  grow the board by entering two negative numbers larger in
 *  magnitude than the board size.
**/
public class FillWordGrows extends FillWordBasic {
    // serialVersionUID encodes the version of serializable state.
    private static final long serialVersionUID = 042013L ;
    /**
     *  Construct a new game to play.
     *  @see FillWordBasic#FillWordBasic
    **/
    public FillWordGrows(Integer boardSizeI, Scanner dictScanner,
            Integer rndseedI) {
        super(boardSizeI, dictScanner, rndseedI);
    }
    public boolean canGrow() {
        return (swaps < swapLimit) ;
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
    protected FillWordGrows(List<String> existingDictionary) {
        super(existingDictionary);
    }
    /**
     *  Return a cloned copy of this object.
     *  @see IFillWord#clone
    **/
    public FillWordGrows clone() {
        FillWordGrows result = new FillWordGrows(dictionary);
        result.helpClone(this);
        return result ;
    }
    /**
     *  Redefine placeWord to allow the user to enter a negative row
     *  such that -row (i.e., the row's magnitude) is > the board size.
     *  In that case the game deducts row^2 number and the length of
     *  the current word to play from the score. If -row == -col
     *  then the new word size limit is set to -row.
     *  @throws FillWordException on any kind of illegal word placement
     *  @see FillWordBasic#placeWord
    **/
    @Override
    protected void placeWord(int row, int col) throws FillWordException {
        if (row < 0 && col < 0 && swaps < swapLimit
                && (-row > board.length)) {
            // Swap the word and deduct the points.
            System.out.println("Grow board, discard word " + wordToPlay
                + " at a cost of " + ((-row)+wordToPlay.length())
                + " points.");
            int newsize = -row ;
            int lognew = (int)(Math.ceil(Math.log10((double)newsize)
                / Math.log10(2.0)));
            int logold = (int)(Math.ceil(Math.log10((double)board.length)
                / Math.log10(2.0)));
            int penalty = newsize ; //(newsize*logold) - (board.length);
            score -= wordToPlay.length() + penalty ;
            char [][] newboard = new char[newsize][newsize];
            for (int r = 0 ; r < newsize ; r++) {
                for (int c = 0 ; c < newsize ; c++) {
                    if (r < board.length && c < board.length) {
                        newboard[r][c] = board[r][c] ;
                    } else {
                        newboard[r][c] = EMPTY ;
                    }
                }
            }
            board = newboard ;
            if (row == col) {
                wordSizeLimit = newsize ;
                System.out.println("Word length limit set to "
                    + newsize + ".");
            }
            wordToPlay = pickWord();
            swaps++ ;
            isacross = ! isacross ;
            return ;
        }
        placeWord(row, col, true);
    }
}
