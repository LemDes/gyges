package gyges.presenter;

import gyges.model.evals.IEval;
import gyges.model.Gyges;
import gyges.model.players.IPlayer;
import gyges.view.IView;

/**
 * Presenter of the application.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class Presenter
{
	/** Hold the player north. */
	IPlayer north;
	/** Hold the player south. */
	IPlayer south;
	/** The view of the game */
	IView view;
	/** The game. */
	Gyges game;
	/** Opening position for player north. */
	int[] posJ1;
	/** Opening position for player south. */
	int[] posJ2;

	/**
	 * Initialise a game of Gygès.
	 *
	 * @param north The player (IA/Human) with his characteristics for north.
	 * @param south The player (IA/Human) with his characteristics for south.
	 * @param posJ1 The initial position of the player1's pawn.
	 * @param posJ2 The initial position of the player2's pawn.
	 */
	public Presenter(IPlayer north, IPlayer south, int[] posJ1, int[] posJ2)
	{
		this.north = north;
		this.south = south;

		this.posJ1 = posJ1;
		this.posJ2 = posJ2;
	}

	/**
	 * Set the view of the application.
	 *
	 * @param view The view of the application.
	 */
	public void setView(IView view)
	{
		this.view = view;
	}

	/**
	 * Start a new game.
	 *
	 * @param id The id of the view.
	 */
	public void newGame(int id)
	{
		game = new Gyges(north, south, posJ1, posJ2);
		view.setGame(game);

		// While the game isn't finished, limited at 100 moves, play.
		for (int i = 1; i < 100 && !view.isFinished() && id == IView.id ; i++)
		{
			// Stats
			IEval.nbEval = 0;
			double time = System.currentTimeMillis();

			// Next move.
			view.next();
			// Update the view.
			view.update();

			// Stats
			// System.out.println("Evals\t"+Integer.toString(IEval.nbEval));
			// System.out.println("Time\t"+Double.toString(System.currentTimeMillis()-time));
		}

		// Check that we are running the current game.
		if (id == IView.id)
		{
			// Game is finished.
			view.displayEndMsg();
		}
	}
}
