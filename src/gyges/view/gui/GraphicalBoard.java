package gyges.view.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import gyges.model.Board;

/**
 * Representation of the board extends JPanel.
 *  
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class GraphicalBoard extends JPanel
{
	/** The view of the application. */
	UiView view;
	/** The size of the button. */
	final int buttonSize = 50;
	
	/** List of button for normal square. */
	GygesButton[][] gButton = new GygesButton[6][6];
	/** List of buttons for bases. */
	GygesButton[] gBases = new GygesButton[2];
	
	/** List of pawn images. */
	static ImageIcon iconList[] = {
		new ImageIcon(GraphicalBoard.class.getClassLoader().getResource("props/zero.png")),
		new ImageIcon(GraphicalBoard.class.getClassLoader().getResource("props/one.png")),
		new ImageIcon(GraphicalBoard.class.getClassLoader().getResource("props/two.png")),
		new ImageIcon(GraphicalBoard.class.getClassLoader().getResource("props/three.png"))
	};

	/**
	 * Create a graphical board.
	 * 
	 * @param view The view of the application.
	 */
	public GraphicalBoard(UiView view)
	{
		super(null);
		this.view = view;
		
		// Set the size of the graphical board.
		setPreferredSize(new Dimension(400,400));
		setMaximumSize(new Dimension(400,400));
		
		int middlePos = (int)(2.5*buttonSize);

		// Add north bases.
		gBases[0] = new GygesButton(this,-1,0,0);
		gBases[0].setBounds(middlePos, 0, buttonSize-5, buttonSize-5);
		add(gBases[0]);

		// Get an empty board for initialization.
		Board board = Board.getEmptyBoard(1); 

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				gButton[i][j] = new GygesButton(this,i,j,board.getValue(i,j));
				gButton[i][j].setBounds(j*buttonSize, (i+1)*buttonSize, buttonSize-5, buttonSize-5);
				add(gButton[i][j]);
			}
		}
		
		// Add south bases.
		gBases[1] = new GygesButton(this,6, 0,0);
		gBases[1].setBounds(middlePos, 7*buttonSize, buttonSize-5, buttonSize-5);
		add(gBases[1]);
	}

	/**
	 * Update the graphical board.
	 * 
	 * @param board The board that will be shown to screen.
	 */
	public void update(Board board)
	{
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 6; j++)
				gButton[i][j].update(board.getValue(i,j));

		gBases[0].update(board.bases[0]);
		gBases[1].update(board.bases[1]);
	}

}
