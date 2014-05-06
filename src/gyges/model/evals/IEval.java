package gyges.model.evals;

import gyges.model.Board;

/**
 * Abstract class used for ranking a board.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public abstract class IEval
{
	/** Number of board evaluated. */
	public static int nbEval = 0;

	/**
	 * Rank a board.
	 *
	 * @param board Board to rank.
	 *
	 * @return The rank.
	 */
	public abstract int getValue (Board board);
}
