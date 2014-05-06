package gyges.model.players;

import gyges.model.Board;
import gyges.model.Point;
import gyges.model.movers.IMover;

/**
 * Represent a human player.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class HumanPlayer extends IPlayer
{
	Point selected;
	Point jump;

	Board currentBoard;

	boolean hasMoved;
	boolean hasSelectedJumpPawn;

	public HumanPlayer(String name, int player)
	{
		super(name, player, false);
	}

	public Board play(Board board) //throws InterruptedException
	{
		hasMoved = false;
		currentBoard = board.clone();
		while (!hasMoved)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
		}
		currentBoard.player = otherPlayer;
		return currentBoard;
	}

	public void recieveInputJump(Point pawn)
	{
		if (currentBoard != null && currentBoard.getValue(pawn) != 0)
		{
			jump = pawn;
			hasSelectedJumpPawn = true;
		}
		else
		{
			jump = null;
			hasSelectedJumpPawn = false;
		}

	}


	public void recieveInputSelect(Point pawn)
	{
		if (currentBoard != null && currentBoard.playerLine() == pawn.x)
			selected = pawn;
		else
			selected = null;
	}

	public void recieveInputMove(Point pawn)
	{
		if (selected != null && currentBoard != null)
		{
			if (hasSelectedJumpPawn)
			{
				hasSelectedJumpPawn = false;
				int startValue = currentBoard.getValue(selected);
				int jumpValue = currentBoard.getValue(jump);
				int sens = (player == 0) ? 1 : -1;

				if ((sens * pawn.x <= sens * currentBoard.advPlayerLine()) ||  selected==pawn)
				{

					currentBoard.setValue(selected,0);
					currentBoard.setValue(jump,startValue);
					currentBoard.setValue(pawn,jumpValue);
					selected = null;
					jump = null;
					hasMoved = true;
				}
			}
			else
			{
				int startValue = currentBoard.getValue(selected);

				Board mask = IMover.getMask(currentBoard,selected);

				if (pawn.x == goalLocation && mask.bases[otherPlayer] > 0)
				{
					currentBoard.setValue(selected,0);
					selected = null;
					currentBoard.bases[otherPlayer] = startValue;
					hasMoved = true;
				}
				else if (pawn.x != goalLocation && mask.getValue(pawn)!=0 && currentBoard.getValue(pawn)==0)
				{
					currentBoard.setValue(selected,0);
					selected = null;
					currentBoard.setValue(pawn, startValue);
					hasMoved = true;
				}
			}
		}
	}
}
