package gyges.model.players;

import gyges.model.Board;

/**
 * Interface representing a player.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public abstract class IPlayer
{
	/** Player's id. */
	int player;
	/** Player's name. */
	String name;
	/** True if player is an AI. */
	public final boolean isAI;
	/* Other player's id. */
	int otherPlayer;
	/** Adv base position. */
	int goalLocation;

	/**
	 * Construct a player.
	 *
	 * @param name The player's name
	 * @param player The player's id
	 */
	public IPlayer (String name, int player, boolean isAI)
	{
		this.name = name;
		this.player = player;
		this.isAI = isAI;

		otherPlayer = (player + 1) % 2;
		goalLocation = (player == 0) ? 6 : -1;
	}

	/**
	 * Make the player play.
	 *
	 * @param board The board to play from
	 *
	 * @return The player's move
	 */
	public abstract Board play (Board board);

	/**
	 * Check if the player is an AI.
	 *
	 * @return True if player is an AI, else false
	 */
	public boolean isAI ()
	{
		return this.isAI;
	}

	/**
	 * Get the player's name.
	 *
	 * @return The player's name
	 */
	public String getName ()
	{
		return name;
	}

	/**
	 * Transform a opening position string into an array of pawns.
	 *
	 * @param pos The opening position
	 *
	 * @return The position array
	 */
	public static int[] pos2array (String pos)
	{
		int[] arr = new int[6];

		try
		{
			for (int i=0; i < 6; i++)
			{
				arr[i] = Character.getNumericValue(pos.charAt(i));
			}
		}
		catch (NumberFormatException e)
		{
			// One element of the string wasn't a number, discard the opening string.
			return null;
		}

		return arr;
	}

	/**
	 * Take an opening position string and return the position array.
	 *
	 * @param pos The opening position
	 *
	 * @return Return the position array if the string is correct, null otherwise
	 */
	public static int[] checkOpeningPosition (String pos)
	{
		// Needs to be 6 pawns.
		if (pos.length() != 6) { return null; }

		int[] line = pos2array(pos);

		// One of the char wasn't a digit.
		if (line == null) { return null; };

		int[] nb = { 0, 0, 0 };

		for (int i : line)
		{
			if (i < 1 || i > 3) { return null; }

			nb[i-1]++;
		}

		// There should be 2 of each pawns: 2 ones, 2 twos and 2 threes.
		if (nb[0] != 2 || nb[1] != 2 || nb[2] != 2) { return null; }

		return line;
	}
}
