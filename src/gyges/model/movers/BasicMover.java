package gyges.model.movers;

import gyges.model.Board;

import java.util.Collections;
import java.util.LinkedList;

/**
 * A basic implementation of IMover.
 *
 * @see IMover
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class BasicMover extends IMover
{
	/** Do nothing, need to exist for reflection access to constructor. */
	public BasicMover (int depth) { }

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
		return Collections.max(boards);
	}
}
