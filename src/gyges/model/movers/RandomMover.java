package gyges.model.movers;

import gyges.model.Board;

import java.util.Random;
import java.util.LinkedList;

/**
 * A basic implementation of IMover.
 *
 * @see IMover
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class RandomMover extends IMover
{
	/** A random number generator. */
	private Random rng;
	
	/**
	 * Create a random number generator.
	 * 
	 * @param depth The seed of the rng.
	 */
	public RandomMover (int depth) 
	{
		rng = new Random(depth);
	}

	/**
	 * Return the next move of the player.
	 *
	 * @param board The initial board.
	 *
	 * @return The next move.
	 */
	public Board nextMove (Board board)
	{
		Board newBoard = bestMove(listMoves(board));
		addMove(newBoard);

		return newBoard;
	}

	/**
	 * Find the best move from a list of moves.
	 *
	 * @param boards The possible moves.
	 *
	 * @return The best move.
	 */
	protected Board bestMove (LinkedList<Board> boards)
	{
		// Board implements comparable (call the eval function from IEval)
		return boards.get(rng.nextInt(boards.size()));
	}
}
