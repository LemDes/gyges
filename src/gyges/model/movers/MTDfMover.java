package gyges.model.movers;

import gyges.model.Board;

import java.util.HashMap;

/**
 * An improve version of Negamax, with Transposition table.
 * Implements IMover.
 *
 * @see IMover
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class MTDfMover extends IMover
{
	/** Transposition table holding value for each board. */
	HashMap<String, TTEntry> tt;
	/** Hold the depth of the search. */
	final int DEPTH;

	/**
	 * Initialize the mover with the depth of the search and
	 * create the transposition table.
	 *
	 * @param depth The depth of the search.
	 */
	public MTDfMover (int depth)
	{
		DEPTH = depth;
		tt = new HashMap<String, TTEntry>();
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
		Board newBoard = MTDf(board, 0, DEPTH).board;
		addMove(newBoard);

		return newBoard;
	}

	/**
	 * Perform a negamax search with alpha-beta prunning and transposition table.
	 *
	 * @param board The board.
	 * @param depth The actual depth of the tree.
	 * @param alpha The value of alpha.
	 * @param beta The value of beta.
	 *
	 * @return The highest alpha and the best board of the search.
	 */
	protected SearchRes alphaBetaTT (Board board, int depth, int alpha, int beta)
	{
		int value;

		// Get the entry.
		String key = board.getHashKey();
		TTEntry tte = GetTTEntry(key);

		if (tte != null && tte.depth >= depth)
		{
			// Value stored is exact.
			if (tte.type == TTtype.EXACT_VALUE)
			{
				return new SearchRes(board, tte.value);
			}

			// Update lowerbound alpha if needed.
			if (tte.type == TTtype.LOWERBOUND && tte.value > alpha)
			{
				alpha = tte.value;
			}
			// Update upperbound beta if needed.
			else if (tte.type == TTtype.UPPERBOUND && tte.value < beta)
			{
				beta = tte.value;
			}

			// If lowerbound surpasses upperbound.
			if (alpha >= beta)
			{
				return new SearchRes(board, tte.value);
			}
		}

		if (depth == 0 || board.isFinished())
		{
			value = board.eval();

			// A lowerbound value.
			if (value <= alpha)
			{
				StoreTTEntry(key, value, TTtype.LOWERBOUND, depth);
			}
			// An upperbound value.
			else if (value >= beta)
			{
				StoreTTEntry(key, value, TTtype.UPPERBOUND, depth);
			}
			// A true minimax value.
			else
			{
				StoreTTEntry(key, value, TTtype.EXACT_VALUE, depth);
			}

			return new SearchRes(board, value);
		}

		// Best score and board.
		int best = Integer.MIN_VALUE;
		Board bestBoard = null;

		// Board as at least another move or the function would have exit in the previous if.
		for (Board nextBoard : listMoves(board))
		{
			if (!canPlay(nextBoard))
			{
				continue;
			}

			value = -1 * alphaBetaTT(nextBoard, depth-1, -1*beta, -1*alpha).value;

			if (value > best)
			{
				best = value;
				bestBoard = nextBoard;
			}

			if (best > alpha)
			{
				alpha = best;
			}

			if (best >= beta)
			{
				break;
			}
		}

		// A lowerbound value.
		if (best <= alpha)
		{
			StoreTTEntry(key, best, TTtype.LOWERBOUND, depth);
		}
		// An upperbound value.
		else if (best >= beta)
		{
			StoreTTEntry(key, best, TTtype.LOWERBOUND, depth);
		}
		// A true minimax value.
		else
		{
			StoreTTEntry(key, best, TTtype.EXACT_VALUE, depth);
		}

		return new SearchRes(bestBoard, best);
	}

	/**
	 * Perform a MTDf search.
	 *
	 * @param board The board.
	 * @param first An estimate value of the result.
	 * @param depth The depth of the search.
	 *
	 * @return The result of the MTDf search.
	 */
	protected SearchRes MTDf (Board board, int first, int depth)
	{
		SearchRes result = new SearchRes(null, first);
		SearchRes best = new SearchRes(null, Integer.MIN_VALUE);

		int window = 1;
		int beta;
		int upperbound = 99999;
		int lowerbound = -99999;

		do
		{
			if (result.value == lowerbound)
			{
				beta = result.value + window;
			}
			else
			{
				beta = result.value;
			}

			result = alphaBetaTT(board, depth, beta - window, beta);
			if (best.value < result.value)
			{
				if (result.value < beta)
				{
					upperbound = result.value;
				}
				else
				{
					lowerbound = result.value;
				}

				best = result;
			}
			else
			{
				window += 1 ;
			}

		}
		while(lowerbound < upperbound && window < 50);

		return best;
	}

	/**
	 * Get the information on a board ranking from the transposition table.
	 *
	 * @param key The key of the board.
	 *
	 * @return The information of the board.
	 */
	protected TTEntry GetTTEntry (String key)
	{
		return tt.get(key);
	}

	/**
	 * Store the information of a board ranking in the transposition table.
	 *
	 * @param key The key of the board.
	 * @param value The value of the board.
	 * @param type  The type of the value.
	 * @param depth The depth where the value has been calculated.
	 */
	protected void StoreTTEntry (String key, int value, TTtype type, int depth)
	{
		tt.put(key, new TTEntry(value, type, depth));
	}
}

/** Enumeration of the different type of value. */
enum TTtype
{
	EXACT_VALUE,
	LOWERBOUND,
	UPPERBOUND,
}

/**
 * A n-uple of int, TTtype & int used to store the value,
 * depth and type of a board.
 */
class TTEntry
{
	/** The value of the board. */
	public int value;
	/** The type of the value. */
	public TTtype type;
	/** The depth where the value has been calculated. */
	public int depth;

	/**
	 * Result of a mdt-f search.
	 *
	 * @param value The value of the board.
	 * @param tpye  The type of the value.
	 * @param depth The depth where the value has been calculated.
	 */
	public TTEntry (int value, TTtype type, int depth)
	{
		this.value = value;
		this.type = type;
		this.depth = depth;
	}
}

/**
 * A tuple of int & Board used to store the result of a mtd-f search.
 */
class SearchRes
{
	/** The board. */
	Board board;
	/** The value of the board. */
	int value;

	/**
	 * Result of a mdt-f search.
	 *
	 * @param board The board.
	 * @param value The value of the board.
	 */
	public SearchRes (Board board, int value)
	{
		this.board = board;
		this.value = value;
	}
}
