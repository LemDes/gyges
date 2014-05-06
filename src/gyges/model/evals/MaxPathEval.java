package gyges.model.evals;

import gyges.model.Board;
import gyges.model.Point;
import gyges.model.movers.IMover;

/**
 * Evaluation function using the size of path to rank boards.
 *
 * @see IEval
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class MaxPathEval extends IEval
{
	/**
	 * Rank a board from the north (player 0) point of view.
	 *
	 * @param board Board to rank.
	 *
	 * @return The rank.
	 */
	public int getValue (Board board)
	{
		nbEval += 1;

		// Create a board owned by player 0.
		Board b = new Board(0, board.getBoard());
		int score = 0;

		// For each pawn you can move add its score to yours.
		for (Point pawn : IMover.movablesPawns(b))
		{
			score += scoreMask(IMover.getMask(b, pawn));
		}

		// Max the score if there is a pawn in the adv base (ie. you have won).
		score += board.bases[1] != 0 ? 10000 : 0;

		return score;
	}

	/**
	 * Score a mask.
	 *
	 * @param mask The mask to evaluate
	 *
	 * @return The score of the mask
	 */
	protected int scoreMask(Board mask)
	{
		int[][] board = mask.getBoard();

		int score = 0;
		for (int i = 0; i < 6;  i++)
		{
			for (int j = 0; j < 6; j++)
			{
				score += (board[i][j] > 0) ? Math.pow(i, board[i][j]) : 0;
			}
		}

		return score;
	}
}
