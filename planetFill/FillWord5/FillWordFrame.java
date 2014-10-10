/* FillWordFrame.java -- Assignment 4 & 5 GUI.
   Dr. Dale Parson, CSC 243, Spring 2013.
*/

package FillWord5 ;
import java.awt.* ;
import javax.swing.* ;
import java.awt.event.* ;

/**
    This class is the main Jframe-derived GUI class for the IFillWord
    family of games.
    @see IFillWord
**/
public class FillWordFrame extends JFrame implements FillWordEventListener{
    private static final int COUNTDOWN_LIMIT = 32 ; // seconds
    private static final int COUNTDOWN_SHOW = 10 ; // seconds
    private int countdown = COUNTDOWN_LIMIT ;
    private int startdown = COUNTDOWN_LIMIT ;
    private int newStartdown = startdown / 2;
    private Timer timer;
    private IFillWord game ;
    private JPanel gamegrid ;
    private JPanel cmdpaneltop ;
    private JPanel cmdpanelbottom ;
    private JPanel cmdpanelOuter ;
    private JPanel extra;
	private JPanel extra2;
	private JPanel extra3;
    private JLabel wordLbl ;
    private JButton [][] myboard ;
    private JButton undoBut ;
    private JButton redoBut ;
    private JButton growBoardBut ;
    private JSpinner growSpin ;
    private SpinnerNumberModel growSpinModel ;
    private JButton growBoardWordBut ;
    private JLabel scoreLbl ;
    private JButton swapBut ;
    private JButton saveBut ;
    private JTextField filenameText ;
    private JButton restoreBut ;
    private JLabel countdownLabel ;
    private Font newfont ;
    private Color front;
    private Color back;
	private ImageIcon star;
	private JLabel starLbl;
	private ImageIcon falcon;
	private JLabel falconLbl;
	private ImageIcon yoda;
	private JLabel yodaLbl;
	private ImageIcon vader;
	private JLabel vaderLbl;
	private JButton yodaBut;
	private Icon  yodaIC;
	private ImageIcon anakin;
	private ImageIcon deathStar;
	private JLabel deathStarLbl;
    /**
     *  Construct a GUI JFrame and play a game.
     *  @param gameObject the freshly constructed game to be played.
    **/
    public FillWordFrame(IFillWord gameObject) {
        JButton dummy = new JButton();
        Font oldfont = dummy.getFont();
        newfont = new Font(oldfont.getName(), oldfont.getStyle(),
            18);
        setLayout(new BorderLayout(0, 0));
        game = gameObject ;
        char [] [] board = game.getBoard();
        myboard = new JButton[board.length][board.length];
        gamegrid = new JPanel(new GridLayout(board.length,
            board.length, 10, 10));
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board.length ; j++) {
                myboard[i][j] = new JButton(
                    board[i][j] == IFillWord.EMPTY ? " "
                        : Character.toString(board[i][j]));
                myboard[i][j].setFont(newfont);
		myboard[i][j].setBackground(back.BLACK);
		myboard[i][j].setForeground(front.MAGENTA);
		myboard[i][j].addActionListener(new Matrix(i,j));
		myboard[i][j].addMouseListener(new highlight(i,j));
                gamegrid.add(myboard[i][j]);
            }
        }
		gamegrid.setBackground(back.BLACK);
        add(gamegrid, BorderLayout.CENTER);
        cmdpaneltop = new JPanel(new FlowLayout(
            FlowLayout.CENTER, 10, 10));
        undoBut = new JButton("undo") ;
        undoBut.setFont(newfont);
		undoBut.setForeground(front.BLACK);
		undoBut.setBackground(back.RED);
		
		setBackground(back.BLACK);
		
        if (! game.canUndo()) {
            undoBut.setEnabled(false);
			undoBut.setForeground(front.WHITE);
			undoBut.setBackground(back.WHITE);
        }
		cmdpaneltop.setBackground(back.BLACK);
		yoda = new ImageIcon("yoda.jpg");
		yodaLbl = new JLabel("", yoda, JLabel.CENTER);
		cmdpaneltop.add(yodaLbl);
        cmdpaneltop.add(undoBut);
        redoBut = new JButton("redo") ;
        redoBut.setFont(newfont);
		redoBut.setForeground(front.BLACK);
		redoBut.setBackground(back.RED);
        if (! game.canRedo()) {
            redoBut.setEnabled(false);
			redoBut.setForeground(front.WHITE);
			redoBut.setBackground(back.WHITE);
        }
        cmdpaneltop.add(redoBut);
       
        if(game.canGrow())
	    { growBoardBut = new JButton("grow board");
	      growBoardBut.setFont(newfont);
	      growBoardBut.setEnabled(true);
		  growBoardBut.setForeground(front.BLACK);
		  growBoardBut.setBackground(back.RED);
     	      cmdpaneltop.add(growBoardBut);
	    }
	if (game.canGrow()) {
            growSpin = new JSpinner(new SpinnerNumberModel(
                board.length+1, board.length+1, 18, 1));
            growSpin.setFont(newfont);
			growSpin.setForeground(front.BLACK);
			growSpin.setBackground(back.RED);
            cmdpaneltop.add(growSpin);
	    growSpin.setEnabled(true);
        }
	
        if(game.canGrow())
            { growBoardWordBut = new JButton("grow board & word");
		growBoardWordBut.setFont(newfont);
		growBoardWordBut.setEnabled(true);
		growBoardWordBut.setForeground(front.BLACK);
		growBoardWordBut.setBackground(back.RED);
		cmdpaneltop.add(growBoardWordBut);
	    }

	scoreLbl = new JLabel("Score: " + game.getScore());
        scoreLbl.setFont(newfont);
		scoreLbl.setForeground(front.WHITE);
		scoreLbl.setBackground(back.BLACK);
        cmdpaneltop.add(scoreLbl);
        JPanel wordpanel = new JPanel(new FlowLayout(
            FlowLayout.CENTER, 10, 10));
        String dir = game.isAcross() ? "across" : "down";
        wordLbl = new JLabel("Word to play " + dir
            + ": " + game.getWordToPlay());
        wordLbl.setFont(newfont);
		wordLbl.setForeground(front.WHITE);
		wordLbl.setBackground(back.BLACK);
		wordpanel.setBackground(back.BLACK);
		star = new ImageIcon("tieFighter.jpg");
		starLbl = new JLabel("", star, JLabel.CENTER);
		falcon = new ImageIcon("falcon.jpg");
		falconLbl = new JLabel("",falcon, JLabel.CENTER);
 	    wordpanel.add(falconLbl);
		wordpanel.add(wordLbl);
		wordpanel.add(starLbl);

		extra = new JPanel(new BorderLayout());
	extra.setBackground(back.BLACK);
	add(extra, BorderLayout.SOUTH);
        extra.add(cmdpaneltop, BorderLayout.NORTH);
	add(wordpanel, BorderLayout.NORTH);
	extra2 = new JPanel(new BorderLayout());
	extra2.setBackground(back.BLACK);
	extra3 = new JPanel(new BorderLayout());
	extra3.setBackground(back.BLACK);
        add(extra2, BorderLayout.EAST);
        add(extra3, BorderLayout.WEST);
   
	cmdpanelbottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
	cmdpanelbottom.setBackground(back.BLACK);
	swapBut = new JButton("Swap Word");
	swapBut.setFont(newfont);
	swapBut.setForeground(front.BLACK);
	swapBut.setBackground(back.RED);
	
	if(game.getSwaps() < game.getSwapLimit())
	    { swapBut.setEnabled(true);
		}
	else
	    {swapBut.setEnabled(false);
		swapBut.setForeground(front.WHITE);
		swapBut.setBackground(back.WHITE);}

	cmdpanelbottom.add(swapBut);
	saveBut = new JButton("Save");
	saveBut.setFont(newfont);
	saveBut.setForeground(front.BLACK);
	saveBut.setBackground(back.RED);
	cmdpanelbottom.add(saveBut);
	filenameText = new JTextField("Enter Filename");
	filenameText.setFont(newfont);
	cmdpanelbottom.add(filenameText);
	restoreBut = new JButton("Restore");
	restoreBut.setFont(newfont);
	restoreBut.setForeground(front.BLACK);
	restoreBut.setBackground(back.RED);
	cmdpanelbottom.add(restoreBut);
	countdownLabel = new JLabel("Seconds to penalty: ?");
	countdownLabel.setFont(newfont);
	countdownLabel.setForeground(front.WHITE);
	countdownLabel.setBackground(back.BLACK);
	cmdpanelbottom.add(countdownLabel);
	vader = new ImageIcon("vader.jpg");
	vaderLbl = new JLabel("", vader, JLabel.CENTER);
	cmdpanelbottom.add(vaderLbl);
	extra.add(cmdpanelbottom, BorderLayout.SOUTH);

	cmdpanelbottom.setBackground(back.BLACK);
	setTitle("Fill Word Game");
        setSize(1000, 700); // Set the frame size
        setLocationRelativeTo(null); // New since JDK 1.4
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // Display the frame
        requestFocusInWindow();
	timer = new Timer(1000, new TimeListener());
	timer.start();
	JOptionPane.showMessageDialog(null, "Begun the Clone War has...", "YODA", JOptionPane.PLAIN_MESSAGE, yoda);
	
	
	undoBut.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent s)
                { 
                    interpret("undo");
					JOptionPane.showMessageDialog(null, "You are part of the Rebel Alliance and a traitor! Take her away! ", "VADER", JOptionPane.PLAIN_MESSAGE, vader);
                }
            });

	redoBut.addActionListener(new ActionListener()
            { public void actionPerformed(ActionEvent s)
                {
                    interpret("redo");
					JOptionPane.showMessageDialog(null, "It is the future you see. ", "YODA", JOptionPane.PLAIN_MESSAGE, yoda);
                }
            });
	if(game.canGrow())
	    {
		growBoardBut.addActionListener(new ActionListener()
		    { public void actionPerformed(ActionEvent s)
			{
			    Integer value = (Integer)growSpin.getValue();
			    interpret("-"+value + " -9");
			}
		    });

		growBoardWordBut.addActionListener(new ActionListener()
		    { public void actionPerformed(ActionEvent s)
			{
			    Integer value = (Integer)growSpin.getValue();
			    interpret("-"+ value + " " + "-" + value);
			}
		    });
	    }

	saveBut.addActionListener(new ActionListener()
	    { public void actionPerformed(ActionEvent s)
  		{
		    String file = filenameText.getText();
		    interpret("save " + file);	
			anakin = new ImageIcon("anakin.jpg");
			JOptionPane.showMessageDialog(null, "I listen to all the traders and star pilots who come through here. I'm a pilot, you know, and someday I'm going to fly away from this place.  ", "ANAKIN", JOptionPane.PLAIN_MESSAGE, anakin);
		    JOptionPane.showMessageDialog(null, "Game saved to " + file);
	
			
		}
	    });

	restoreBut.addActionListener(new ActionListener()
            {public void actionPerformed(ActionEvent r)
                {       
		    String file = filenameText.getText(); 
                    interpret("restore " + file);
					
			JOptionPane.showMessageDialog(null, "Decide you must, how to serve them best. If you leave now, help them you could; but you would destroy all for which they have fought, and suffered.", "YODA", JOptionPane.PLAIN_MESSAGE, yoda);
		    JOptionPane.showMessageDialog(null, "Game restored from " + file);
			
			  
                    }
            });
            swapBut.addActionListener(new ActionListener()
                {public void actionPerformed(ActionEvent sl)
                    {
			interpret("-1 -1");
                    }
                });

	    game.addFillWordEventListener(this);
	
    }
    public static final String usage =
    "Usage: java FillWord4.FillWordFrame FillWord4.FillWordCLASSNAME BOARD_SIZE DICT_FILENAME RANDOMSEED";
    /**
     *  Static method main starts the game & the GUI.
     *  Usage:
     *  java FillWord4.FillWordFrame FillWord4.FillWordBasic BOARD_SIZE DICTIONARY_FILENAME RANDOMSEED,
     *  where FillWord4.FillWordBasic can be any concrete subclass of IFillWord,
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
        BuildFillWord.logSessionData(null, false);
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
            IFillWord gameObject = FillWordTest.loadPuzzleAndStart(
                args[0],boardsize,filename,randomSeed);
            gameObject.makeFirstMove();
            FillWordFrame frame = new FillWordFrame(gameObject);
        } catch (Exception whateverx) {
            System.err.println("ERROR on FillWordFrame startup: "
                + whateverx.getClass().getName() + ": "
                + whateverx.getMessage());
            whateverx.printStackTrace();
            System.exit(1);
        }
    
    } 

    private class Matrix implements ActionListener
    { 
      private int bRow = 0;
      private int bCol = 0;
       
       public Matrix(int i, int j)
       {   bRow = i;
	   bCol = j;
       }
	public void actionPerformed(ActionEvent e)
	{  
	    interpret(bRow + " " + bCol);
       	}

    }
	
	private class highlight implements MouseListener
	{
		private int r = 0;
		private int c = 0;
		
		public highlight(int i, int j)
		{ r = i;
			c = j;
		}
		public void mouseClicked(MouseEvent mou)
		{}
		public void mousePressed(MouseEvent u)
		{}
		public void mouseReleased(MouseEvent t)
		{}
		
		public void mouseEntered(MouseEvent m)
		{ String thisWord = game.getWordToPlay();
			char[][] test = game.getBoard();
		    if(game.isAcross())
			{ for(int k = c; (k < (thisWord.length() + c) && (k < test.length)); k++)
				{ myboard[r][k].setBackground(back.GREEN);}
			}
		    else
			{ for(int k = r; (k < (thisWord.length()+ r) && (k < test.length)); k++)
                                { myboard[k][c].setBackground(back.GREEN);}
                        }
		}
		public void mouseExited(MouseEvent mo)
		{
		String thisWord = game.getWordToPlay();
		char[][] test = game.getBoard();
		    if(game.isAcross())
			{ for(int k = c; (k < (thisWord.length() + c)) && (k < test.length); k++)
				{ myboard[r][k].setBackground(back.BLACK);}
			}
		    else
			{ for(int k = r; (k < (thisWord.length() + r)) && (k < test.length); k++)
                                { myboard[k][c].setBackground(back.BLACK);}
                        }
		}
	}		
		
    
    private class TimeListener implements ActionListener 
    { public void actionPerformed(ActionEvent t)
      	{ 	
	    
	    countdown = countdown - 1;
	    
	    if(countdown <= COUNTDOWN_SHOW)
		{
		    countdownLabel.setText("Seconds to penalty: " + countdown);
		    countdownLabel.setForeground(front.RED);
			countdownLabel.setBackground(back.BLACK);
		}
	    if(countdown <= 0)
		{
		    game.addOrDeductPoints(-5);
		    scoreLbl.setText("Score: " + game.getScore());
		    if (startdown >= 2)
			{startdown = newStartdown;
			    newStartdown = newStartdown / 2;
      			    countdown = startdown;
			    countdownLabel.setText("Seconds to penalty: " + countdown);
			    if(startdown == 0)
				{	if(game.getScore() < -100)
					{countdownLabel.setText("This is some rescue. You came in here and you didn’t have a plan for getting out?");}
					else
					{countdownLabel.setText("LOSING POINTS!");}
				}
			}
		     countdownLabel.setForeground(front.RED);
			 
		}
	}
    }

	       
    public void processGameEvent(FillWordEvent event)
    { 
	Color cool = new Color(255,255,255);
	Integer val;
	if(growBoardBut != null)
		{ val = (Integer)growSpin.getValue();}
		else
		{val = 11;}
		
	char [][] current = game.getBoard();	
	    if(game.canUndo())
		{ undoBut.setEnabled(true);
			undoBut.setForeground(front.BLACK);
			undoBut.setBackground(back.RED);}
	    else
		{undoBut.setEnabled(false);
			undoBut.setForeground(front.WHITE);
			undoBut.setBackground(back.WHITE);}

	    if(game.canRedo())
		{ redoBut.setEnabled(true);
		  redoBut.setForeground(front.BLACK);
	      redoBut.setBackground(back.RED);}
	    else
		{redoBut.setEnabled(false);
		 redoBut.setForeground(front.WHITE);
		 redoBut.setBackground(back.WHITE);}
	    String dir = game.isAcross() ? "across" : "down";
	    wordLbl.setText("Word to play " + dir + ": " + game.getWordToPlay());
	    
	    scoreLbl.setText("Score: " + game.getScore());
		
	    if(game.getSwaps() >= game.getSwapLimit())
		{
		    swapBut.setEnabled(false);
			swapBut.setForeground(front.WHITE);
			swapBut.setBackground(back.WHITE);
		    
		    if(growBoardBut != null)
			{   growBoardBut.setEnabled(false);
			    growBoardWordBut.setEnabled(false);
				if(val >= 18)
				{growSpin.setValue(11);}
			    growSpin.setEnabled(false);
				growBoardBut.setForeground(front.WHITE);
				growBoardBut.setBackground(back.WHITE);
				growBoardWordBut.setForeground(front.WHITE);
				growBoardWordBut.setBackground(back.WHITE);
				growSpin.setForeground(front.WHITE);
				growSpin.setBackground(back.WHITE);
			}
		    
		}
	    else
		{
		    swapBut.setEnabled(true);
			swapBut.setForeground(front.BLACK);
			swapBut.setBackground(back.RED);
			
		    if(growBoardBut != null)
                        {  if(current.length < 18) 
							{growBoardBut.setEnabled(true);
                             growBoardWordBut.setEnabled(true);
							 growSpin.setValue(current.length + 1);
                             growSpin.setEnabled(true);
							 growBoardBut.setForeground(front.BLACK);
							 growBoardBut.setBackground(back.RED);
							 growBoardWordBut.setForeground(front.BLACK);
						 	 growBoardWordBut.setBackground(back.RED);
							 growSpin.setForeground(front.BLACK);
							 growSpin.setBackground(back.RED);
							}
							else
							{	growBoardBut.setEnabled(false);
								growBoardWordBut.setEnabled(false);
								growSpin.setValue(12);
								growSpin.setEnabled(false);
								growBoardBut.setForeground(front.WHITE);
								growBoardBut.setBackground(back.WHITE);
								growBoardWordBut.setForeground(front.WHITE);
								growBoardWordBut.setBackground(back.WHITE);
								growSpin.setForeground(front.WHITE);
								growSpin.setBackground(back.WHITE);}
                        }

                }
			
	 	    remove(gamegrid);
		    myboard = new JButton[current.length][current.length];
		    gamegrid = new JPanel(new GridLayout(current.length, current.length, 10, 10));
		    for (int i = 0 ; i < current.length ; i++) 
			{
			    for (int j = 0 ; j < current.length ; j++) 
			      {
				myboard[i][j] = new JButton(current[i][j] == IFillWord.EMPTY ? " "
																					: Character.toString(current[i][j]));
				myboard[i][j].setFont(newfont); 
				myboard[i][j].setBackground(back.BLACK);
				myboard[i][j].setForeground(front.MAGENTA);
				myboard[i][j].addActionListener(new Matrix(i,j));
				myboard[i][j].addMouseListener(new highlight(i,j));
				gamegrid.setBackground(cool.BLACK);
				gamegrid.add(myboard[i][j]);
			      }
			}
	      
		    IFillWord.FillWordRecord move = game.getThisMove();
		  if(move != null)
		  {
		    if(move.isacross)
			{ for(int k = move.column; k < (move.word.length() + move.column); k++)
				{ myboard[move.row][k].setForeground(cool.YELLOW);}
			}
		    else
			{ for(int k = move.row; k < (move.word.length() + move.row); k++)
                                { myboard[k][move.column].setForeground(cool.YELLOW);}
                        }
		  }	
			if(game.getScore() < -100)
			{deathStar = new ImageIcon("explosion.jpg");
			 deathStarLbl = new JLabel(deathStar);
			remove(gamegrid);
			gamegrid = new JPanel(new FlowLayout());
			gamegrid.setBackground(back.BLACK);
			gamegrid.add(deathStarLbl); 
			undoBut.setEnabled(false);
			undoBut.setBackground(back.BLACK);
			undoBut.setForeground(front.BLACK);
			redoBut.setEnabled(false);
			redoBut.setBackground(back.BLACK);
			redoBut.setForeground(front.BLACK);
			saveBut.setEnabled(false);
			saveBut.setBackground(back.BLACK);
			saveBut.setForeground(front.BLACK);
			restoreBut.setEnabled(false);
			restoreBut.setBackground(back.BLACK);
			restoreBut.setForeground(front.BLACK);
			swapBut.setEnabled(false);
			swapBut.setBackground(back.BLACK);
			swapBut.setForeground(front.BLACK);
			countdownLabel.setText("This is some rescue. You came in here and you didn’t have a plan for getting out?");
			countdownLabel.setForeground(front.RED);
			countdownLabel.setBackground(back.BLACK);
			if(growBoardBut != null)
				{
				growBoardBut.setEnabled(false);
				growBoardWordBut.setEnabled(false);
				growSpin.setValue(12);
				growSpin.setEnabled(false);
				growBoardBut.setForeground(front.BLACK);
				growBoardBut.setBackground(back.BLACK);
				growBoardWordBut.setForeground(front.BLACK);
				growBoardWordBut.setBackground(back.BLACK);
				growSpin.setForeground(front.BLACK);
				growSpin.setBackground(back.BLACK);
				}
			}
			 add(gamegrid, BorderLayout.CENTER);
		    
		    countdownLabel.setText("Seconds to penalty: ?");
		    countdownLabel.setForeground(front.WHITE);
			countdownLabel.setBackground(back.BLACK);
		    countdown = COUNTDOWN_LIMIT;
		    startdown = COUNTDOWN_LIMIT;
		   			
		validate();
		repaint();
		
		if(game.getScore() < -101 && game.getScore() > -110)
			JOptionPane.showMessageDialog(null,"GAME OVER! I am wondering, why are you here?", "YODA", JOptionPane.PLAIN_MESSAGE, yoda); 
    }

    public void interpret(String instruction)
    {
	try{
	    game.interpret(instruction);
	   }
	catch (FillWordException sad)
	    { JOptionPane.showMessageDialog(null, sad.getMessage());}
    }

    
}
