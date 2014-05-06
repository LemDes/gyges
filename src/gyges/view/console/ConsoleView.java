package gyges.view.console;

import gyges.model.Gyges;
import gyges.presenter.Presenter;
import gyges.view.IView;

/**
 * Console view implements IView.
 * 
 * @see IView
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class ConsoleView extends IView
{
	/** The game. */
	protected Gyges game;
	/** The step. */
	protected int i;
	/** The presenter. */
	protected Presenter presenter;

	/**
	 * Construct a ConsoleView.
	 *
	 * @param presenter The presenter of the application.
	 */
	public ConsoleView (Presenter presenter)
	{
		this.presenter = presenter;
	}

	/**
	 * Set the current game;
	 * 
	 * @param game The current game.
	 */
	public void setGame(Gyges game)
	{
		this.game = game;
		System.out.println(game.toString());
		System.out.println("\nPlayer " + game.getPlayerName(i) + " turn:");
	}


	/**
	 * Display the end mesage: draw or who won.
	 */
	public void displayEndMsg ()
	{
		int winner = game.winner();

		if (winner == 2)
		{
			System.out.println("\nDraw.");
		}
		else
		{
			System.out.println("\n" + game.getPlayerName(winner) + " won in " + Integer.toString(i) + " moves!");
		}
	}

	/**
	 * Setup the view.
	 */
	public void init ()
	{
		i = 1;
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
	 * Get the next board.
	 */
	public void next ()
	{
		game.next();
		i += 1;
	}

	/**
	 * Update the view with the new board.
	 */
	public void update ()
	{
		System.out.println(game.toString());
		System.out.println("\nPlayer " + game.getPlayerName(i%2) + " turn:");
	}
}
