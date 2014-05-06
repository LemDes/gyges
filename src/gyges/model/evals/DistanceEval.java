package gyges.model.evals;

import gyges.model.Board;

/**
 * Evaluation function using maximum distance to rank boards.
 *
 * @see IEval
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class DistanceEval extends IEval
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

		// The score.
		int score = 0;

		// We want the pawn to be the far away from our base.
		for (int i = 0; i != 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board.getValue(i,j) != 0)
				{
					score += i;
				}
			}
		}

		// Max the score if there is a pawn in the adv base (ie. you have won).
		score += board.bases[1] != 0 ? 10000 : 0;

		return score;
	}
}
