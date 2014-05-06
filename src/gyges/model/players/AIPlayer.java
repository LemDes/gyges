package gyges.model.players;

import gyges.model.Board;
import gyges.model.evals.IEval;
import gyges.model.movers.IMover;

/**
 * Represent a bot (AI).
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class AIPlayer extends IPlayer
{
	/** The eval function of the AI. */
	public IEval evalF;
	/** The search algorithm of the AI. */
	public IMover mover;

	/**
	 * Create an AI player.
	 *
	 * @param evalF The evaluation function
	 * @param mover The search algorithm
	 * @param name The name of the AI
	 * @param player The player's id
	 */
	public AIPlayer (IEval evalF, IMover mover, String name, int player)
	{
		super(name, player, true);

		this.evalF = evalF;
		this.mover = mover;
	}

	/**
	 * Get the player next move.
	 *
	 * @param board The board to play from
	 *
	 * @return The player move
	 */
	public Board play (Board board)
	{
		board.player = player;
		Board.eval = evalF;

		return mover.nextMove(board);
	}
}
