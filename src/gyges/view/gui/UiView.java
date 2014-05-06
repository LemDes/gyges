package gyges.view.gui;

import java.awt.Dialog.ModalExclusionType;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import gyges.model.Board;
import gyges.model.Gyges;
import gyges.presenter.Presenter;
import gyges.view.IView;

/**
 * The view of the application.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class UiView extends IView
{
	/** The game. */
	protected Gyges game;
	/** The frame. */
	protected JFrame frame;
	/** The step. */
	protected int i;
	/** The presenter. */
	protected Presenter presenter;

	/** The representation of the board. */
	GraphicalBoard gBoard;

	/** The main panel of the frame. */
	JPanel newContentPane;

	/**
	 * Create an UiView.
	 *
	 * @param presenter The presenter of the application.
	 */
	public UiView(Presenter presenter)
	{
		frame = new JFrame();

		this.presenter = presenter;
	}

	/**
	 * Set the current game;
	 *
	 * @param newGame The game
	 */
	public void setGame(Gyges newGame)
	{
		game = newGame;
		update();
	}

	/**
	 * Get the next board.
	 */
	public synchronized void next ()
	{
		GygesButton.player = game.nextPlayer();
		game.next();
		i += 1;
	}

	/**
	 * Test if the game is finished.
	 *
	 * @return True if the game is finished, false else.
	 */
	public boolean isFinished ()
	{
		return game.isFinished();
	}

	/**
	 * Display the end mesage: draw or who won.
	 */
	public void displayEndMsg ()
	{
		int winner = game.winner();
		String msg;

		// Draw.
		if (winner == 2)
		{
			msg = "Draw.";
		}
		else
		{
			msg = game.getPlayerName(winner) + " won in " + Integer.toString(i) + " moves!";
		}

		new OnWin(frame, msg, "Game Over");
	}


	/**
	 * Return a copy of the board.
	 *
	 * @return A copy of the board.
	 */
	public Board getBoard ()
	{
		return game.getReadableBoard();
	}

	/**
	 * Setup the view.
	 */
	public void init()
	{
		i = 1;

		// Base frame
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Gyges");
		frame.setLocationRelativeTo(null);

		newContentPane = new JPanel(new BorderLayout());

		// Add the graphical model.
		gBoard = new GraphicalBoard(this);
		newContentPane.add(gBoard, BorderLayout.CENTER);

		// Add the reset button.
		newContentPane.add(new ResetButton(this,"Nouvelle partie"), BorderLayout.EAST);

        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        frame.setVisible(true);
        frame.pack();
	}

	/**
	 * Update the view with the new board.
	 */
	public void update()
	{
		gBoard.update(getBoard());
		try { Thread.sleep(500); } catch (InterruptedException e) {}
	}

	/**
	 * Start a new game.
	 */
	public synchronized void cbNewGame()
	{
		i = 1;
		new NewGame(presenter);
	}
}

/**
 * Class used to start a new game.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
class NewGame extends Thread {

	/** The presenter of the application. */
	Presenter pres;

	/**
	 * Create a newGame launcher
	 *
	 * @param pres The presenter of the application.
	 */
	public NewGame (Presenter pres)
	{
		super();
		this.pres=pres;
		start();
	}

	/**
	 * Increment the view id and ask the presenter to start a new game.
	 */
	public void run()
	{
		pres.newGame(++IView.id);
	}
}
