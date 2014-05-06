package gyges.model.movers;

import gyges.model.Board;
import gyges.model.Point;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Abstract class used for choosing the next move.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public abstract class IMover
{
	/** Hold the moves already played (to break cycle) */
	static LinkedList<String> alreadyPlayed = new LinkedList<String>();
	/** Hold the coordinates modificators for each pawn type. */
	protected static LinkedList<LinkedList<Point>> moves = new LinkedList<LinkedList<Point>>();

	/**
	 * Init the list of moves.
	 */
	public static void init()
	{
		moves.add(new LinkedList<Point>());
		moves.add(new LinkedList<Point>());
		moves.add(new LinkedList<Point>());

		// Accessible position for pawn 1.
		moves.get(0).add(new Point(1,0));
		moves.get(0).add(new Point(-1,0));
		moves.get(0).add(new Point(0,1));
		moves.get(0).add(new Point(0,-1));

		// Accessible position for pawn 2.
		moves.get(1).add(new Point(0,2));
		moves.get(1).add(new Point(0,-2));
		moves.get(1).add(new Point(1,1));
		moves.get(1).add(new Point(1,-1));
		moves.get(1).add(new Point(-1,1));
		moves.get(1).add(new Point(-1,-1));
		moves.get(1).add(new Point(2,0));
		moves.get(1).add(new Point(-2,0));

		// Accessible position for pawn 3.
		moves.get(2).add(new Point(0,1));
		moves.get(2).add(new Point(1,0));
		moves.get(2).add(new Point(0,-1));
		moves.get(2).add(new Point(-1,0));
		moves.get(2).add(new Point(3,0));
		moves.get(2).add(new Point(-3,0));
		moves.get(2).add(new Point(0,3));
		moves.get(2).add(new Point(0,-3));
		moves.get(2).add(new Point(2,1));
		moves.get(2).add(new Point(2,-1));
		moves.get(2).add(new Point(-2,1));
		moves.get(2).add(new Point(-2,-1));
		moves.get(2).add(new Point(1,2));
		moves.get(2).add(new Point(-1,2));
		moves.get(2).add(new Point(1,-2));
		moves.get(2).add(new Point(-1,-2));
	}

	/**
	 * Return the next move of the player.
	 *
	 * @param board The initial board
	 *
	 * @return The next move
	 */
	public abstract Board nextMove (Board board);

	/**
	 * List all movables pawns for a player.
	 *
	 * @param board The board
	 *
	 * @return All movables pawns for the player
	 */
	public static LinkedList<Point> movablesPawns (Board board)
	{
		LinkedList<Point> movables = new LinkedList<Point>();

		// Start on the first (=0 for north) ; or last (=5 for south) line
		// Loop until a line with pawn is found
		// if nothing on the line, continue to the next: i+1 for north and i-1 for south.

		int i = board.playerLine();
		for (int j = 0; j < 6; j++)
		{
			if (board.getValue(i, j) != 0)
			{
				movables.add(new Point(i,j));
			}
		}

		return movables;
	}

	/**
	 * List all possible boards accessible from a pawn.
	 *
	 * @param board The initial board
	 * @param pawn The pawn to move
	 *
	 * @return All possible boards accessible from the player's pawn
	 */
	protected static LinkedList<Board> accessible (Board board, Point pawn)
	{
		int player = board.player;
		int newPlayer = (player + 1) % 2;

		// Array: true = accessible position for the pawn, else false.
		boolean[][] mask = new boolean[6][6];
		// Stock the coord already visited.
		boolean[] done = new boolean[36];
		// Stock the coord where we landed.
		Stack<Point> border = new Stack<Point>();
		border.push(pawn);
		// The board without the moving pawn.
		Board newBoard = new Board(player, board.getCopyBoard());
		newBoard.setValue(pawn, 0);
		// The value of the moved pawn.
		int pawnValue = board.getValue(pawn);
		// Stock the result(s).
		LinkedList<Board> boards = new LinkedList<Board>();

		// Loop while we haven't find all terminal positions or until we find a terminal board.
		while (!border.empty())
		{
			Point p = border.pop();
			int value = board.getValue(p);

			if (value == 0)
			{
				// Accessible position.
				done[p.hashCode()] = true;
				continue;
			}

			// Add each accessible position to the border and on the mask.
			for (Point coord : moves.get(value-1))
			{
				Point temp = Point.add(p,coord);

				// If pawn end on base and can go there: winning move.
				if (((temp.x == -1 && player == 1)  || (temp.x == 6 && player == 0)) && isAccessibleFrom(p, temp, newBoard, value, 42))
				{
					Board tempBoard = new Board(newPlayer, newBoard.getCopyBoard());
					tempBoard.bases[newPlayer] = pawnValue;

					boards = new LinkedList<Board>();
					boards.add(tempBoard);

					return boards;
				}

				// Not on the border nor out of the board nor the path is not free.
				else if (temp.isInBound() && !done[temp.hashCode()] && isAccessibleFrom(p, temp, newBoard, value, 42))
				{
					mask[temp.x][temp.y] = true;
					border.push(temp);
				}
			}

			done[p.hashCode()] = true;
		}

		// Create and store each accessible board.
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				// if the position is accessible and free add the board to the res.
				if (mask[i][j])
				{
					if (board.getValue(i, j) == 0)
					{
						Board tempBoard = new Board(newPlayer, newBoard.getCopyBoard());
						tempBoard.setValue(i, j, pawnValue);
						boards.add(tempBoard);
					}
					else
					{
						boards.addAll(replacement(newBoard, new Point(i, j), pawnValue));
					}
				}
			}
		}

		return boards;
	}

	/**
	 * List all moves from a board.
	 *
	 * @param board The initial board
	 *
	 * @return All moves from the board
	 */
	protected static LinkedList<Board> listMoves (Board board)
	{
		LinkedList<Point> movables = movablesPawns(board);
		LinkedList<Board> moves = new LinkedList<Board>();

		for (Point pawn : movables)
		{
			moves.addAll(accessible(board, pawn));
		}

		return moves;
	}

	/**
	 * Test recursively if there is a path for a pawn between two points.
	 *
	 * @param from The origin point
	 * @param to The destination point
	 * @param board The board
	 * @param value The capacity of movement of the pawn
	 * @param sens The direction where the pawn is comming from
	 *
	 * @return True is a pawn can go between the two points, else false
	 */
	protected static boolean isAccessibleFrom (Point from, Point to, Board board, int value, int sens)
	{
		int player = board.player;

		// You can't go on your base.
		if ((player == 1 && to.x == 6) || (player == 0 && to.x == -1))
			return false;

		// You can go on the adv base.
		if (value == 1 && ((player == 1 && from.x == 0 && to.x == -1) || (player == 0 && from.x == 5 && to.x == 6)) )
			return true;

		// Store the result.
		boolean isA = false;
		Point p;

		for (int i = 0; i < 4; i++)
		{
			if (sens + i == 5 || sens + i == 1)
			{
				continue;
			}

			p = moves.get(0).get(i);
			Point np = Point.add(from, p);

			if (value == 1 && np.equals(to))
			{
				// You have direct access to the terminal position.
				return true;
			}
			else if (value != 1)
			{
				// Test if you can access the terminal position with more displacement.
				isA |= (np.isInBound() && board.isFree(np) && isAccessibleFrom(np, to, board, value-1, i));
			}
		}

		return isA;
	}

	/**
	 * List all correct replacements for a pawn and its target.
	 *
	 * @param board The board
	 * @param target The position of the replacable pawn
	 * @param value The value of the inital pawn
	 *
	 * @return All correct replacements
	 */
	protected static LinkedList<Board> replacement (Board board, Point target, int value)
	{
		int player = board.player;
		int newPlayer = (board.player + 1) % 2;

		LinkedList<Board> boards = new LinkedList<Board>();

		int maxLine = board.advPlayerLine();
		int sens = (player == 0) ? 1 : -1;

		for (int i = 5*player; i != maxLine+sens; i+=sens)
		{
			for (int j = 0; j < 6; j++)
			{
				if (board.isFree(i, j))
				{
					Board tempB = new Board(newPlayer, board.getCopyBoard());
					tempB.setValue(i, j, board.getValue(target));
					tempB.setValue(target, value);

					boards.add(tempB);
				}
			}
		}

		return boards;
	}

	/**
	 * Get the mask of movements accessible from a player's pawn.
	 *
	 * @param board The board
	 * @param pawn The pawn to move
	 *
	 * @return Mask of movements accessible from the player's pawn
	 */
	public static Board getMask(Board board, Point pawn)
	{
		// Array: true = accessible position for the pawn, else false.
		Board mask = new Board(0, new int[6][6]);
		// Stock the coord already visited.
		boolean[] done = new boolean[36];
		// Stock the coord where we landed.
		Stack<Point> border = new Stack<Point>();
		border.push(pawn);
		// The board without the moving pawn.
		Board newBoard = board.clone();
		newBoard.setValue(pawn, 0);

		// Loop while we haven't find all terminal positions.
		while (!border.empty())
		{
			Point p = border.pop();
			int value = board.getValue(p);

			if (value == 0)
			{
				done[p.hashCode()] = true;
				continue;
			}

			// Add each accessible position to the border and on the mask.
			for (Point coord : moves.get(value-1))
			{
				Point temp = Point.add(p,coord);
				if (((temp.x == -1 && board.player == 1)  || (temp.x == 6 && board.player == 0)) && isAccessibleFrom(p, temp, newBoard, value, 42))
				{
					mask.bases[(board.player+1)%2] += 1;
				}
				// Not on the border nor out of the board nor the path is not free.
				else if (temp.isInBound() && !done[temp.hashCode()] && isAccessibleFrom(p, temp, newBoard, value, 42))
				{
					mask.setValue(temp, mask.getValue(temp)+1);
					border.push(temp);
				}
			}

			done[p.hashCode()] = true;
		}

		return mask;
	}

	/**
	 * Test if a board is playable,
	 * i.e. if it's hasn't already by played, to break cycles.
	 *
	 * @param board the board
	 *
	 * @return True if the board is playable, else false
	 */
	public boolean canPlay (Board board)
	{
		return !IMover.alreadyPlayed.contains(board.getHashKey());
	}

	/**
	 * Add the board to the list of played board.
	 *
	 * @param board The played board.
	 */
	public void addMove (Board board)
	{
		IMover.alreadyPlayed.add(board.getHashKey());
	}
}
