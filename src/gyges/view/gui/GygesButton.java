package gyges.view.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import gyges.model.Point;
import gyges.model.players.HumanPlayer;

/**
 * Representation of a square of the game board.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class GygesButton extends JButton implements MouseListener
{
	/** The human player who's currently playing. */
	public static HumanPlayer player;

	/** Hold the x position of the button. */
	int x;
	/** Hold the y position of the button. */
	int y;
	/** Hold the graphical model associated with the button. */
	GraphicalBoard gBoard;

	/** The value of the pawn placed here. */
	int val;

	/**
	 * Create a graphical cell.
	 *
	 * @param gBoard The graphical board.
	 * @param x The x coordinate of the button.
	 * @param y The y coordinate of the button.
	 * @param val The value of the pawn..
	 */
	public GygesButton(GraphicalBoard gBoard, int x, int y, int val)
	{
		super(GraphicalBoard.iconList[val]);

		setOpaque(false);
		setContentAreaFilled(false);

		this.gBoard = gBoard;
		this.x = x;
		this.y = y;
		this.val = val;

		setVisible(true);
		addMouseListener(this);
	}

	/**
	 * Update the button with a new value.
	 *
	 * @param val The new value of the button.
	 */
	public void update(int val)
	{
		this.val = val;
		setIcon(GraphicalBoard.iconList[val]);
	}


	/**
	 * Invoked when a mouse button has been pressed on the button..
	 *
	 * @param e The event recieved.
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		switch (e.getButton())
		{
			//On left click.
			case MouseEvent.BUTTON1:
			{
				if (GygesButton.player != null)
					// The player want to place a pawn here.
					GygesButton.player.recieveInputMove(new Point(x,y));
				break;
			}

			//On wheel click.
			case MouseEvent.BUTTON2:
			{
				if (GygesButton.player != null)
					// The player want to do a replacement with the pawn here.
					GygesButton.player.recieveInputJump(new Point(x,y));
				break;
			}

			// On right click.
			case MouseEvent.BUTTON3:
			{
				if (GygesButton.player != null)
					// The player want to move this pawn.
					GygesButton.player.recieveInputSelect(new Point(x,y));
				break;
			}
		}
	}

	// Do nothing, but must be redefined.
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}
