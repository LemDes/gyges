package gyges.view;

import gyges.model.Gyges;

/**
 * Interface representing a view.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public abstract class IView
{
	/** Id of the view */
	public static int id = 0;
	
	/**
	 * Set the current game;
	 * 
	 * @param game The current game.
	 */
	public abstract void setGame(Gyges game);

	/**
	 * Display the end mesage: draw or who won.
	 */
	public abstract void displayEndMsg ();

	/**
	 * Setup the view.
	 */
	public abstract void init ();

	/**
	 * Test if the game is finished.
	 *
	 * @return True if the game is finished, false else.
	 */
	public abstract boolean isFinished ();

	/**
	 * Get the next board.
	 */
	public abstract void next ();

	/**
	 * Update the view with the new board.
	 */
	public abstract void update ();
}
