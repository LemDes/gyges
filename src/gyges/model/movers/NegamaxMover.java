package gyges.model.movers;

import gyges.model.Board;

/**
 * An implementation of IMover using negamax + alphaBeta.
 *
 * @see IMover
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class NegamaxMover extends IMover
{
	/** Hold the depth of the search. */
	final int DEPTH;

	/**
	 * Initialize the mover with the depth of the search.
	 *
	 * @param depth The depth of the search.
	 */
	public NegamaxMover (int depth)
	{
		DEPTH = depth;
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
		Board newBoard = negamax(DEPTH, board, Integer.MIN_VALUE, Integer.MAX_VALUE).board;
		addMove(newBoard);

		return newBoard;
	}

	/**
	 * Perform a negamax search with alpha-beta prunning.
	 *
	 * @param depth The actual depth of the tree.
	 * @param board The board.
	 * @param alpha The value of alpha.
	 * @param beta The value of beta.
	 *
	 * @return The highest alpha and the best board of the search.
	 */
	protected NegamaxRes negamax (int depth, Board board, int alpha, int beta)
	{
		if (depth == 0)
		{
			return new NegamaxRes(board.eval(), board);
		}

		if (board.isFinished())
		{
			return new NegamaxRes(alpha, board);
		}

		Board resBoard = null;

		for (Board move : listMoves(board))
		{
			if (!canPlay(move))	{ continue; }

			NegamaxRes nr = negamax(depth-1, move, -1*beta, -1*alpha);

			if (-1*nr.alpha > alpha)
			{
				alpha = -1*nr.alpha;
				resBoard = move;
			}

			if (alpha >= beta)
			{
				return new NegamaxRes(alpha, resBoard);
			}
		}

		return new NegamaxRes(alpha, resBoard);
	}
}

/**
 * A tuple of int & Board used to store the result of a Negamax search.
 */
class NegamaxRes
{
	/** The value of alpha */
	public int alpha;
	/** The board */
	public Board board;

	/**
	 * Result of the negamax search.
	 *
	 * @param alpha The value of alpha.
	 * @param board The board.
	 */
	public NegamaxRes (int alpha, Board board)
	{
		this.alpha = alpha;
		this.board = board;
	}
}
