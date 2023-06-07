package board.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import board.controller.Controller;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.BevelBorder;

public class BoardPanel extends JPanel
{
	private Controller app;
	private Dimension screenSize;
	private int screenHeight;
	private int screenWidth;
	private JPanel redInfo;
	private JPanel blueInfo;
	private JTextField redScore;
	private JTextPane redScoreBreakdown;
	private int redNumScore;
	private int redNumCubes;
	private int redNumBottles;
	private int redNumTower;
	private int redNumPenalties;
	private JTextField blueScore;
	private JTextPane blueScoreBreakdown;
	private int blueNumScore;
	private int blueNumCubes;
	private int blueNumBottles;
	private int blueNumTower;
	private int blueNumPenalties;
	private SpringLayout overallLayout;

	private long startTime;
	private Set<Integer> pressedKeys;

	private JTextField timer;

	boolean reset;
	boolean checkBonus;

	public BoardPanel(Controller app)
	{
		this.app = app;
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.overallLayout = new SpringLayout();
		this.screenHeight = (int) screenSize.getHeight();
		this.screenWidth = (int) screenSize.getWidth();
		this.redInfo = new JPanel();
		this.redScore = new JTextField("0");
		this.redScoreBreakdown = new JTextPane();
		this.blueScoreBreakdown = new JTextPane();
		this.blueInfo = new JPanel();
		this.blueScore = new JTextField("0");
		this.pressedKeys = new TreeSet<Integer>();

		this.timer = new JTextField("5:00");

		reset = false;
		checkBonus = false;

		setupPanel();
		setupListeners();
		setupLayout();

	}

	// SETUP AND STYLIZE ELEMENTS
	private void setupPanel()
	{
		this.setLayout(overallLayout);
		
		// SETUP AND STYLIZE RED SCORE
		redScore.setFont(new Font("Kohinoor Gujarati", Font.BOLD, (int) (screenWidth / 3) - 130));
		redScore.setPreferredSize(new Dimension((int) (screenWidth / 2.75), (int) (screenWidth / 2.75)));
		redScore.setHorizontalAlignment(JTextField.CENTER);
		redScore.setEditable(false);
		redScore.setHighlighter(null);
		redScore.setBackground(Color.RED);
		redScore.setForeground(Color.WHITE);
		redScore.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(110, 7, 9), new Color(110, 7, 9), null, null));

		// SETUP AND STYLIZE RED SCORE BREAKWON
		redScoreBreakdown.setText("\nBottle Points:\n" + 0 + "\n\nCube Points:\n" + 0 + "\n\nTower Destruction:\n" + 0 + "\n\nPenalties:\n" + 0 + "\n\n");
		redScoreBreakdown.setFont(new Font("Kohinoor Gujarati", Font.BOLD, 28));
		redScoreBreakdown.setPreferredSize(new Dimension((screenWidth - (int) (screenWidth / 2.75) * 2) / 2, (int) (screenWidth / 2.75)));
		redScoreBreakdown.setEditable(false);
		redScoreBreakdown.setHighlighter(null);
		redScoreBreakdown.setBackground(Color.LIGHT_GRAY);
		redScoreBreakdown.setForeground(Color.BLACK);
		redScoreBreakdown.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), new Color(0, 0, 0), null, null));
		
		// CENTERS TEXT FOR RED SCORE BREAKDOWN
		StyledDocument doc = redScoreBreakdown.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		// SETUP AND STYLIZE BLUE SCORE
		blueScore.setFont(new Font("Kohinoor Gujarati", Font.BOLD, (int) (screenWidth / 3) - 130));
		blueScore.setPreferredSize(new Dimension((int) (screenWidth / 2.75), (int) (screenWidth / 2.75)));
		blueScore.setHorizontalAlignment(JTextField.CENTER);
		blueScore.setEditable(false);
		blueScore.setHighlighter(null);
		blueScore.setBackground(Color.BLUE);
		blueScore.setForeground(Color.WHITE);
		blueScore.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(4, 2, 84), new Color(4, 2, 84), null, null));
		
		// SETUP AND STYLIZE BLUE SCORE BREAKDOWN
		blueScoreBreakdown.setText("\nBottle Points:\n" + 0 + "\n\nCube Points:\n" + 0 + "\n\nTower Destruction:\n" + 0 + "\n\nPenalties:\n" + 0 + "\n\n" );
		blueScoreBreakdown.setFont(new Font("Kohinoor Gujarati", Font.BOLD, 28));
		blueScoreBreakdown.setPreferredSize(new Dimension((screenWidth - (int) (screenWidth / 2.75) * 2) / 2, (int) (screenWidth / 2.75)));
		blueScoreBreakdown.setEditable(false);
		blueScoreBreakdown.setHighlighter(null);
		blueScoreBreakdown.setBackground(Color.LIGHT_GRAY);
		blueScoreBreakdown.setForeground(Color.BLACK);
		blueScoreBreakdown.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), new Color(0, 0, 0), null, null));
		
		// CENTERS TEXT FOR BLUE SCORE BREAKDOWN
		StyledDocument doc2 = blueScoreBreakdown.getStyledDocument();
		SimpleAttributeSet center2 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center2, StyleConstants.ALIGN_CENTER);
		doc2.setParagraphAttributes(0, doc2.getLength(), center2, false);

		// SETUP AND STYLIZE TIMER
		timer.setFont(new Font("Kohinoor Gujarati", Font.BOLD, (int) (screenWidth / 5) - 73));
		timer.setPreferredSize(new Dimension((screenWidth - (int) (screenWidth / 1.49)), screenHeight - (int) (screenWidth / 2.5) - 22));
		timer.setSize(new Dimension((screenWidth - (int) (screenWidth / 1.49)), screenHeight - (int) (screenWidth / 2.5) - 22));
		timer.setEditable(false);
		timer.setHighlighter(null);
		timer.setBackground(Color.WHITE);
		timer.setForeground(Color.BLACK);
		timer.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 0, 0), new Color(0, 0, 0), null, null));

		// ADD ELEMENTS TO PANEL
		this.add(redScore);
		this.add(redScoreBreakdown);
		this.add(blueScore);
		this.add(blueScoreBreakdown);
		this.add(timer);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	// SETUP ACTIONS
	private void setupListeners()
	{
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent keyboard)
			{
			}

			@Override
			public void keyReleased(KeyEvent keyboard)
			{
				pressedKeys.remove(keyboard.getKeyCode());
			}
			
			// UPDATE VARIABLES BASED OFF KEY PRESSES
			@Override
			public void keyPressed(KeyEvent keyboard)
			{
				int code = keyboard.getKeyCode();
				Integer val = Integer.valueOf(code);
				
				if (pressedKeys.contains(val))
				{
				} else
				{
					switch (keyboard.getKeyChar())
					{
					case 'q': //Green Cubes
						redNumCubes += 1;
						break;
					case 'Q': //Undo Green Cubes
						redNumCubes -= 1;
						break;
					case 'w': //Orange Cubes
						redNumCubes += 2;
						break;
					case 'W': //Undo Orange Cubes
						redNumCubes -= 2;
						break;
					case 'e': //Purple Cubes
						redNumCubes += 3;
						break;
					case 'E': //Undo Purple Cubes
						redNumCubes -= 3;
						break;
					case 'r': //Cube stack bonus
						redNumCubes += 3;
						break;
					case 'R': //Undo Cube stack bonus
						redNumCubes -= 3;
						break;
					case 'a': //Green Bottle
						redNumBottles += 4;
						break;
					case 'A': //Undo Green Bottle
						redNumBottles -= 4;
						break;
					case 's': //Orange Bottle
						redNumBottles += 5;
						break;
					case 'S': //Undo Orange Bottle
						redNumBottles -= 5;
						break;
					case 'd': //Purple Bottle
						redNumBottles += 6;
						break;
					case 'D': //Undo Purple Bottle
						redNumBottles -= 6;
						break;
					case 'z': //Green Tower Fallen
						redNumTower -= 4;
						break;
					case 'Z': //Undo Green Tower Fallen
						redNumTower += 4;
						break;
					case 'x': //Orange Tower Fallen
						redNumTower -= 5;
						break;
					case 'X': //Undo Orange Tower Fallen
						redNumTower += 5;
						break;
					case 'c': //Purple Tower Fallen
						redNumTower -= 6;
						break;
					case 'C': //Undo Purple Tower Fallen
						redNumTower += 6;
						break;
					case '1': //Penalty -1
						redNumPenalties -= 1;
						break;
					case '!': //Undo Penalty -1
						redNumPenalties += 1;
						break;
					case 'i': //Green Cube
						blueNumCubes += 1;
						break;
					case 'I': //Undo Green Cube
						blueNumCubes -= 1;
						break;
					case 'o': //Orange Cube
						blueNumCubes += 2;
						break;
					case 'O': //Undo Orange Cube
						blueNumCubes -= 2;
						break;
					case 'p': //Purple Cube
						blueNumCubes += 3;
						break;
					case 'P': //Undo Purple Cube
						blueNumCubes -= 3;
						break;
					case 'u': //Cube stack bonus
						blueNumCubes += 3;
						break;
					case 'U': //Undo Cube stack bonus
						blueNumCubes -= 3;
						break;
					case 'j': //Green Bottle
						blueNumBottles += 4;
						break;
					case 'J': //Undo Green Bottle
						blueNumBottles -= 4;
						break;
					case 'k': //Orange Bottle
						blueNumBottles += 5;
						break;
					case 'K': //Undo Orange Bottle
						blueNumBottles -= 5;
						break;
					case 'l': //Purple Bottle
						blueNumBottles += 6;
						break;
					case 'L': //Undo Purple Bottle
						blueNumBottles -= 6;
						break;
					case 'n': //Green Tower Fallen
						blueNumTower -= 4;
						break;
					case 'N': //Undo Green Tower Fallen
						blueNumTower += 4;
						break;
					case 'm': //Orange Tower Fallen
						blueNumTower -= 5;
						break;
					case 'M': //Undo Orange Tower Fallen
						blueNumTower += 5;
						break;
					case ',': //Purple Tower Fallen
						blueNumTower -= 6;
						break;
					case '<': //Undo Purple Tower Fallen
						blueNumTower += 6;
						break;
					case '-': //Penalty -1
						blueNumPenalties -= 1;
						break;
					case '_': //Undo Penalty -1
						blueNumPenalties += 1;
						break;
					case 'g':

						checkBonus = true;
						break;
					case 't':
						reset = true;
						break;
					}
					
					// UPDATE TEXT VALUES EVERY KEY PRESS
					redScoreBreakdown.setText("\nBottle Points:\n" + redNumBottles + "\n\nCube Points:\n" + redNumCubes + "\n\nTower Destruction:\n" + redNumTower + "\n\nPenalties:\n" + redNumPenalties + "\n\n");
					redNumScore = redNumBottles + redNumCubes + redNumTower + redNumPenalties;
					redScore.setText(redNumScore + "");
					blueScoreBreakdown.setText("\nBottle Points:\n" + blueNumBottles + "\n\nCube Points:\n" + blueNumCubes + "\n\nTower Destruction:\n" + blueNumTower + "\n\nPenalties:\n" + blueNumPenalties + "\n\n");
					blueNumScore = blueNumBottles + blueNumCubes + blueNumTower + blueNumPenalties;
					blueScore.setText(blueNumScore + "");
					
					pressedKeys.add(keyboard.getKeyCode());
				}
			}
		});
	}

	// LAYOUT ELEMENTS ON BOARD
	private void setupLayout()
	{
		// LAYOUT RED AND BLUE SCORE ELEMENTS
		overallLayout.putConstraint(SpringLayout.EAST, redScore, (int) (screenWidth / 2), SpringLayout.WEST, this);
		overallLayout.putConstraint(SpringLayout.SOUTH, redScore, 0, SpringLayout.SOUTH, this);
		overallLayout.putConstraint(SpringLayout.WEST, blueScore, (int) (screenWidth / 2), SpringLayout.WEST, this);
		overallLayout.putConstraint(SpringLayout.SOUTH, blueScore, 0, SpringLayout.SOUTH, this);
		
		// LAYOUT RED AND BLUE BREAKDOWN SCORE ELEMENTS
		overallLayout.putConstraint(SpringLayout.SOUTH, redScoreBreakdown, 0, SpringLayout.SOUTH, this);
		overallLayout.putConstraint(SpringLayout.WEST, redScoreBreakdown, 0, SpringLayout.WEST, this);
		overallLayout.putConstraint(SpringLayout.SOUTH, blueScoreBreakdown, 0, SpringLayout.SOUTH, this);
		overallLayout.putConstraint(SpringLayout.EAST, blueScoreBreakdown, 0, SpringLayout.EAST, this);

		// LAYOUT TIMER ELEMENT
		overallLayout.putConstraint(SpringLayout.WEST, timer, (int) (screenWidth / 2) - ((screenWidth - (int) (screenWidth / 1.49)) / 2), SpringLayout.WEST, this);
	}

	// UPDATES THE TIMER
	public boolean updateTimer(long minutes, long seconds)
	{
		// CHECK AND UPDATE IF TIMER IS IN SINGLE DIGITS
		if (seconds < 10)
		{
			timer.setText(minutes + ":0" + seconds);
		} else
		{
			timer.setText(minutes + ":" + seconds);
		}

		// PLAY SOUNDFILES BASED OFF TIMER VALUE
		if (minutes == 4 && seconds == 0)
		{
			try
			{
				URL url = getClass().getResource("FourMin.wav");
				Clip clip = AudioSystem.getClip();
				File file = new File(url.getPath());
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
			}
		} else if (minutes == 3 && seconds == 0)
		{
			try
			{
				URL url = getClass().getResource("ThreeMin.wav");
				Clip clip = AudioSystem.getClip();
				File file = new File(url.getPath());
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
			}
		} else if (minutes == 2 && seconds == 0)
		{
			try
			{
				URL url = getClass().getResource("TwoMin.wav");
				Clip clip = AudioSystem.getClip();
				File file = new File(url.getPath());
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
			}
		} else if (minutes == 1 && seconds == 0)
		{
			try
			{
				URL url = getClass().getResource("OneMin.wav");
				Clip clip = AudioSystem.getClip();
				File file = new File(url.getPath());
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
			}
		} else if (minutes == 0 && seconds == 0)
		{
			try
			{
				URL url = getClass().getResource("EndGame.wav");
				Clip clip = AudioSystem.getClip();
				File file = new File(url.getPath());
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception exc)
			{
				exc.printStackTrace(System.out);
			}
		}
		
		// TURN TIMER YELLOW FOR ENDGAME
		if (minutes == 0 && seconds <= 30)
		{
			timer.setBackground(Color.YELLOW);

		}

		// CHECK IF BONUS TIME KEY WAS PRESSED AND UPDATE VALUE
		if (checkBonus == true)
		{
			checkBonus = false;
			return true;
		} else
		{
			return false;
		}

	}

	// PASSES RESET CONTROL TO CONTROLLER
	public boolean checkReset()
	{
		return reset;
	}
}
