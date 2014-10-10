/* FillWordTest.java -- Assignment 2 test driver client.
   Dr. Dale Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.util.Scanner ;
import java.util.Set ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.lang.reflect.InvocationTargetException ;

/**
    This class is a test driver for classes implementing interface
    IFillWord. There is no need to use the default constructor, because
    "main" is a static method.
    @author Dr. Dale Parson
**/
public class FillWordTest {
    public static final String usage =
    "Usage: java FillWord5.FillWordTest FillWord5.FillWordCLASSNAME BOARD_SIZE DICT_FILENAME RANDOMSEED";
    /**
     *  Static method main tests the game.
     *  Usage:
     *  java FillWord5.FillWordTest FillWord5.FillWordBasic BOARD_SIZE DICTIONARY_FILENAME RANDOMSEED,
     *  where FillWord5.FillWordBasic can be any concrete subclass of IFillWord,
     *  where BOARD_SIZE is an integer > 1 and < 11 giving the maximum board
     *  width & height as well as the number of letters allocated per move, and
     *  DICTIONARY_FILENAME gives the file path to the text file containing
     *  the dictionary of words, one word per line. RANDOMSEED if >= 0
     *  is a seed for the pseudo-random number generator to ensure repeat
     *  values on test runs; if < 0 then the RANDOMSEED is not used.
     *  @param args is an array of the command line arguments, passed in from
     *  the operating system, where args[0] is the first actual argument,
     *  and *not* the name of the executable program as in C++.
    **/
    public static void main(String [] args) {
        if (args.length != 4) {
            System.err.println(usage);
            System.exit(1);     // non-0 exit status indicates an error
        }
        String filename = args[2];
        int boardsize ;
        try {
            boardsize = Integer.parseInt(args[1]);
            // see java.lang.Integer.parseInt
        } catch (NumberFormatException nogood) {
            boardsize = -1 ;    // use an invalid value to trigger error msg.
        }
        int randomSeed ;
        try {
            randomSeed = Integer.parseInt(args[3]);
        } catch (NumberFormatException nogood) {
            randomSeed = -1 ;    // < 0 means "don't use the seed"
        }
        if (boardsize < 2 || boardsize > 10) {
            System.err.println("BOARD_SIZE must be an integer > 0, < 11: "
                + args[1]);
            System.err.println(usage);
            System.exit(1);     // non-0 exit status indicates an error
        }
        try {
            IFillWord gameObject = loadPuzzleAndStart(args[0],boardsize,
                filename,randomSeed);
            // From here we read user commands until EOF is read.
            // (End-of-file is ctrl-D on Unix, ctrl-Z on Windows.)
            // Later we will let a GUI accept and process commands.
            Scanner lineScanner = new Scanner(System.in);
            try {
                gameObject.makeFirstMove();
                gameObject.printBoard();
            } catch (FillWordException fwx) {
                System.err.print("Exception while making first move: ");
                System.err.println(fwx.getMessage());
            }
            while (lineScanner.hasNextLine()) {
                String cmd = lineScanner.nextLine().trim();
                try {
                    System.out.println("Interpreting command string: "
                        + cmd);
                    gameObject.interpret(cmd);
                } catch (FillWordException fwx) {
                    System.err.println(
                        "Exception while interpreting command string: "
                        + cmd + ":");
                    System.err.println(fwx.getMessage());
                }
                gameObject.printBoard();
            }
        } catch (java.io.FileNotFoundException fnx) {
            System.err.println("File not found: " + filename
                + ": " + fnx.getMessage());
            // fnx.printStackTrace(); 
            System.exit(1);
        } catch (java.lang.ClassNotFoundException cnx) {
            System.err.println("Class not found: " + args[0]
                + ": " + cnx.getMessage());
            // cnx.printStackTrace(); 
            System.exit(3);
        } catch (java.lang.ClassCastException ccx) {
            System.err.println("Class does not inherit from IFillWord: "
                + args[0]
                + ": " + ccx.getMessage());
            System.exit(3);
        } catch (java.lang.NoSuchMethodException nnx) {
            System.err.println("Class constructor not found: " + args[0]
                + ": " + nnx.getMessage());
            System.exit(3);
        } catch (java.lang.InstantiationException inx) {
            System.err.println("Class constructor error: " + args[0]
                + ": " + inx.getMessage());
            System.exit(3);
        } catch (java.lang.IllegalAccessException anx) {
            System.err.println("Class constructor access error: " + args[0]
                + ": " + anx.getMessage());
            System.exit(3);
        } catch (InvocationTargetException vnx) {
            System.err.println("Class constructor call error: " + args[0]
                + ": " + vnx.getMessage());
            System.exit(3);
        }
        System.out.println();
        System.exit(0);     // exit status 0 signifies success
    }
    /**
     *  Factory method loadPuzzleAndStart loads a class that implements
     *  interface IFillWord, invokes a constructor that takes an int
     *  boardSize, String dictionaryFilename, and int randomNumberSeed
     *  and returns that object.
     *  @param classFilePath is the PACKAGE.CLASS name of the clas that
     *  subclasses IFillWord.
     *  @param boardSize is the initial boardSize X boardSize size of
     *  the game board.
     *  @param dictionaryFilename is the name of the file containing the
     *  game dictionary, with one word per line.
     *  @param randomNumberSeed is the seed for the random move generator,
     *  use -1 for a non-repeatedable game (no explicit seed).
     *  @throws FileNotFoundException on an invalid file name.
     *  @throws ClassNotFoundException on an invalid class file name.
     *  @throws IllegalAccessException if not allowed to load a class.
     *  @throws NoSuchMethodException if required constructor missing.
     *  @throws InstantiationException on problem running constructor.
     *  @throws InvocationTargetException on problem running constructor.
    **/
    public static IFillWord loadPuzzleAndStart(
        String classFilePath, int boardSize, String dictionaryFilename,
        int randomNumberSeed)
            throws InstantiationException, FileNotFoundException,
                ClassNotFoundException, IllegalAccessException,
                NoSuchMethodException,
                InvocationTargetException {
        // Load class classFilePath from the file
        // system and run its 3-param cons.
        Scanner lineScanner = new Scanner(new File(dictionaryFilename));
        Class <? extends
            IFillWord> puzzleclass
            = Class.forName(classFilePath).
                asSubclass(IFillWord.class);
        Class<?> [] consparams = new Class<?> [3];
        consparams[0] = Integer.class ; // constructor's param type
        consparams[1] = Scanner.class ; // constructor's param type
        consparams[2] = Integer.class ; // constructor's param type
        Object [] consargs = new Object [3];
        consargs[0] = new Integer(boardSize);
        consargs[1] = lineScanner ;
        consargs[2] = new Integer(randomNumberSeed);
        java.lang.reflect.Constructor<?
        		extends IFillWord> newcons =
          puzzleclass.getConstructor(consparams);
        IFillWord gameObject = (IFillWord)
            newcons.newInstance(consargs);
        lineScanner.close() ;   // the dictionary has been read
        return gameObject ;
    }
}
