package gyges.model;

import gyges.model.players.HumanPlayer;
import gyges.model.players.IPlayer;

/**
 * A game of Gygès.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class Gyges
{
	/** The players. */
	protected IPlayer[] players = { null, null };
	/** The game board. */
	protected Board board;
	/** The current player. */
	protected int player;

	/**
	 * Construct a Gygès game.
	 *
	 * @param north The north player.
	 * @param south The south player.
	 * @param posJ1 The opening position of player north.
	 * @param posJ2 The opening position of player south.
	 */
	public Gyges (IPlayer north, IPlayer south, int[] posJ1, int[] posJ2)
	{
		players[0] = north;
		players[1] = south;

		player = 1;
		board = new Board(player, posJ1, posJ2);
	}

	/**
	 * Return a String version of the board.
	 *
	 * @return The board.
	 */
	public String toString ()
	{
		return board.toString();
	}

	/**
	 * Get the next human player.
	 *
	 * @return The next human player.
	 */
	public HumanPlayer nextPlayer ()
	{
		if (!players[player].isAI())
		{
			return (HumanPlayer)players[player];
		}

		// Next player is AI, return null.
		return null;
	}

	/**
	 * Launch next move.
	 */
	public void next ()
	{
		IPlayer p = players[player];

		board = p.play(board);

		player = board.player;
	}

	/**
	 * Check if the game is finished.
	 *
	 * @return true if the game is finished, false otherwise.
	 */
	public boolean isFinished ()
	{
		return board.isFinished();
	}

	/**
	 * Return the winner of the game.
	 *
	 * @return The winner.
	 */
	public int winner ()
	{
		return board.winner();
	}

	/**
	 * Return a reseted game.
	 *
	 * @return A reseted game.
	 */
	public Gyges reset ()
	{
		return new Gyges(players[0], players[1], null, null);
	}

	/**
	 * Return a copy of the board.
	 *
	 * @return A board copy.
	 */
	public Board getReadableBoard ()
	{
		return board.clone();
	}

	/**
	 * Get a player's name.
	 *
	 * @param i The player id.
	 *
	 * @return The player's name.
	 */
	public String getPlayerName (int i)
	{
		return players[i].getName();
	}
}
