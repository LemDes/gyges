package gyges.model;

import gyges.model.evals.IEval;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represent a board of Gygès.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class Board implements Comparable<Board>
{
	/** Hold the state of the board. */
	protected int board[][] = new int[6][6];
	/** The other player id from the player id. */
	public static final int otherPlayers[] = { 1, 0 };
	/** Evaluation function. */
	public static IEval eval;
	/** The value of the bases. */
	public int[] bases = { 0, 0 };
	/** The player. */
	public int player;

	/**
	 * Create a board.
	 *
	 * @param player The player.
	 * @param posJ1 The opening position for the player north, use random position if null.
	 * @param posJ2 The opening position for the player south, use random position if null.
	 */
	public Board (int player, int[] posJ1, int[] posJ2)
	{
		this.player = player;

		// Create random opening positions.
		List<Integer> valuesN = Arrays.asList(1, 1, 2, 2, 3, 3);
		List<Integer> valuesS = Arrays.asList(2, 1, 3, 1, 3, 2);
		Collections.shuffle(valuesN);
		Collections.shuffle(valuesS);

		// North.
		for (int x = 0; x < 6; x++)
		{
			if (posJ1 == null)
			{
				board[0][x] = valuesN.get(x);
			}
			else
			{
				board[0][x] = posJ1[x];
			}
		}

		// Middle of the board.
		for (int y = 1; y < 5; y++)
		{
			for (int x = 0; x < 6; x++)
			{
				board[y][x] = 0;
			}
		}

		// South.
		for (int x = 0; x < 6; x++)
		{
			if (posJ2 == null)
			{
				board[5][x] = valuesS.get(x);
			}
			else
			{
				board[5][x] = posJ2[x];
			}
		}
	}

	/**
	 * Create a board from a given board state.
	 *
	 * @param player The player.
	 * @param board A board to init from, must be 6x6.
	 */
	public Board (int player, int[][] board)
	{
		this.player = player;
		this.board = board;
	}

	/**
	 * Create an empty board.
	 *
	 * @param player The player.
	 *
	 * @return The board created.
	 */
	public static Board getEmptyBoard (int player)
	{
		int[][] empty = { {0,0,0,0,0,0}, {0,0,0,0,0,0}, {0,0,0,0,0,0}, {0,0,0,0,0,0}, {0,0,0,0,0,0}, {0,0,0,0,0,0} };
		return new Board(player, empty);
	}

	/**
	 * Getter for the board.
	 *
	 * @return The board.
	 */
	public int[][] getBoard ()
	{
		return this.board;
	}

	/**
	 * Return a string representation of the board.
	 *
	 * @return The representation of the board.
	 */
	public String toString ()
	{
		StringBuffer s = new StringBuffer("  N");

		s.append(bases[0]);
		s.append("  \n");

		for (int y = 0; y < 6; y++)
		{
			for (int x = 0; x < 6; x++)
			{
				s.append(board[y][x]);
			}

			s.append("\n");
		}

		s.append("  S");
		s.append(bases[1]);
		s.append("  ");

		return s.toString();
	}

	/**
	 * Getter for a position in the board.
	 *
	 * @see Board#getValue(Point p)
	 *
	 * @param i The line value.
	 * @param j The column value.
	 *
	 * @return The value for the position.
	 */
	public int getValue (int i, int j)
	{
		return board[i][j];
	}

	/**
	 * Setter for a position in the board.
	 *
	 * @see Board#setValue(Point p, int newValue)
	 *
	 * @param i The line value.
	 * @param j The column value.
	 * @param newValue The new value. Must be between 0 and 3.
	 */
	public void setValue (int i, int j, int newValue)
	{
		board[i][j] = newValue;
	}

	/**
	 * Getter for a position in the board.
	 *
	 * @see Board#getValue(int i, int j)
	 *
	 * @param p The position.
	 *
	 * @return The value for the position.
	 */
	public int getValue (Point p)
	{
		return board[p.x][p.y];
	}

	/**
	 * Setter for a position in the board.
	 *
	 * @see Board#setValue(int i, int j, int newValue)
	 *
	 * @param p The position.
	 * @param newValue The new value. Must be between 0 and 3.
	 */
	public void setValue (Point p, int newValue)
	{
		board[p.x][p.y] = newValue;
	}

    /**
     * Make a deep copy of the state of the board.
     *
     * @return The copy.
     */
    public int[][] getCopyBoard ()
    {
		int[][] newBoard = new int[6][6];

		for (int i = 0; i < 6; i++)
		{
			System.arraycopy(board[i], 0, newBoard[i], 0, 6);
		}

		return newBoard;
	}

	/**
     * Make a deep copy of the Board.
     *
     * @return The clone.
     */
	public Board clone()
	{
		Board clone = new Board(player, getCopyBoard());
		System.arraycopy(bases, 0, clone.bases, 0, 2);

		return clone;
	}

	/**
	 * Compare this Board with the other.
	 *
	 * @param other The board to compare to.
	 *
	 * @return 1 if bigger than other, -1 if smaller and 0 if equal.
	 */
	public int compareTo (Board other)
	{
		int thisValue = this.eval();
		int otherValue = other.eval();

		if (thisValue > otherValue) { return 1; }
		if (thisValue < otherValue) { return -1; }

		return 0;
	}

	/**
	 * Test if the position of the board is free.
	 *
	 * @see Board#isFree(Point p)
	 *
	 * @param x The line value of the position.
	 * @param y The column value of the position.
	 *
	 * @return True if the value of the position is free, else false.
	 */
	public boolean isFree (int x, int y)
	{
		return board[x][y] == 0;
	}

	/**
	 * Test if the position of the board is free.
	 *
	 * @see Board#isFree(int x, int y)
	 *
	 * @param p The position.
	 *
	 * @return True if the value of the position is free, else false.
	 */
	public boolean isFree (Point p)
	{
		return board[p.x][p.y] == 0;
	}

	/**
	 * Test if the game is finished.
	 *
	 * @return True if a base has been reached, else false.
	 */
	public boolean isFinished ()
	{
		return (bases[0] != 0) || (bases[1] != 0);
	}

	/**
	 * Return the line number where the player can play from.
	 *
	 * @return The line number.
	 */
	public int playerLine ()
	{
		return line(player);
	}

	/**
	 * Return the line number where the adv player can play from.
	 *
	 * @return The line number.
	 */
	public int advPlayerLine()
	{
		return line(Board.otherPlayers[player]);
	}

	/**
	 * Return the line number for a player.
	 *
	 * @param player The player.
	 *
	 * @return The line number.
	 */
	protected int line (int player)
	{
		// Start on the first (=0 for north) ; or last (=5 for south) line
		// Loop until a line with pawn is found
		// if nothing on the line, continue to the next: i+1 for north and i-1 for south
		int sens = (player == 0) ? 1 : -1;
		int stop = -5*(player-1);

		for (int i = 5*player; i != stop+sens; i += sens)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board[i][j] != 0)
				{
					return i;
				}
			}
		}

		// Should never happen.
		return -1;
	}

	/**
	 * Return the number of pawns on the board.
	 *
	 * @return The number of pawns.
	 */
	public int nbPawns ()
	{
		int nb = 0;

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board[i][j] != 0)
				{
					nb++;
				}
			}
		}

		return nb;
	}

	/**
	 * Evaluate the board.
	 *
	 * @return The value of the board.
	 */
	public int eval ()
	{
		int value = eval.getValue(this) - eval.getValue(rotate());

		// Since board are evaluated from the player 0 pov.
		if (this.player == 1)
		{
			return -1*value;
		}

		return value;
	}

	/**
	 * Rotate the board (and the bases).
	 *
	 * @return The rotated board.
	 */
	public Board rotate ()
    {
		int[][] newBoard = new int[6][6];

		int j=0;
		for (int i = 5; i >= 0; i--)
		{
			// Put line i of old board to line j of new board.
			System.arraycopy(board[i], 0, newBoard[j], 0, 6);
			j++;
		}

		Board nb = new Board(Board.otherPlayers[player], newBoard);
		nb.bases[0] = this.bases[1];
		nb.bases[1] = this.bases[0];

		return nb;
	}

	/**
	 * Get the winner id.
	 *
	 * @return 0 for north, 1 for south and 2 for no-one (draw).
	 */
	public int winner()
	{
		if (bases[0] != 0) { return 1; }
		if (bases[1] != 0) { return 0; }

		return 2;
	}

	/**
	 * Uniq key representing the board.
	 *
	 * @return The key.
	 */
	public String getHashKey ()
	{
		StringBuffer res = new StringBuffer();

		res.append(player);
		res.append(Arrays.toString(bases));

		for (int[] line : board)
		{
			res.append(Arrays.toString(line));
		}

		return res.toString();
	}

	/**
	 * Check if the board is equal to other.
	 *
	 * @param other Object to compare to, if not an instance of Board return false.
	 *
	 * @return True if the board equal other, else false.
	 */
	public boolean equals (Object other)
	{
		if (!(other instanceof Board))
		{
			// Other is not a Board, therefor other isnt equal to this.
			return false;
		}

		String key = getHashKey();
		String otherKey = Board.class.cast(other).getHashKey();

		// Compare the string not the pointer.
		return key.equals(otherKey);
	}
}
