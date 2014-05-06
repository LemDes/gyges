package gyges.model.evals;

import gyges.model.Board;

/**
 * A basic evaluation function.
 *
 * @see IEval
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class BasicEval extends IEval
{
	/*
	 * Rank a board from the north (player 0) point of view.
	 *
	 * @param board Board to rank.
	 *
	 * @return The rank.
	 */
	public int getValue (Board board)
	{
		nbEval += 1;

		// The player doesn't want pawns of value x on his xth line.
		int score = 0;

		// Start at the first line of the board (the fisrt of the player),
		// up to the 3rd line of the bord (the 3rd of the player).
		for (int i = 0; i < 3; i++)
		{
			// which line is it (player pov) ?
			int line = i + 1 ;
			
			// Increment the score for each case of the line i if case value != line nb (player pov).
			for (int j = 0; j < 6; j++)
			{
				score += ( board.getValue(i,j) == line ) ? 0 : 1;
				
			}
		}

		// Max the score if there is a pawn in the adv base (ie. you have won).
		score += board.bases[1] != 0 ? 10000 : 0;

		return score;
	}
}
