package gyges.model.movers;

import java.util.LinkedList;
import java.util.Random;

import gyges.model.Board;
import gyges.model.Point;
import gyges.model.movers.IMover;

/**
 * Unit test for IMover.java.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class UnitTestIMover
{
	protected static IMover mover = new MockIMover();

	/**
	 * Launch the tests.
	 */
	public static void test ()
	{
		checkMovablesPawns();
		checkAccessible();
		checkIsAccessibleFrom();
		checkReplacement();
		checkListMoves();

		System.out.println("UnitTestIMover: Ok.");
	}

	/*
	 * Check if the right pawn are detected movables.
	 */
	protected static void checkMovablesPawns ()
	{
		int[][] b = {{1,0,3,0,0,2},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,2,0,7,0,0},{0,0,0,0,0,0},{24,0,0,0,0,0}};
		Board boardN = new Board(0, b);
		Board boardS = new Board(1, b);

		// player north
		LinkedList<Point> points = IMover.movablesPawns(boardN);
		assert points.size() == 3 : "UnitTestIMover.checkMovablesPawns : Bad number of movable pawns ("+Integer.toString(points.size())+")";
		int[] pos = {0,2,5};
		for (int i=0; i<3; i++)
		{
			Point point = points.get(i);
			assert point.x == 0 : "UnitTestIMover.checkMovablesPawns : x should equal 0 ("+Integer.toString(point.x)+")";
			assert point.y == pos[i] : "UnitTestIMover.checkMovablesPawns : y should equal "+Integer.toString(pos[i])+" ("+Integer.toString(point.y)+")";
		}

		// player south
		points = IMover.movablesPawns(boardS);
		assert points.size() == 1 : "UnitTestIMover.checkMovablesPawns : Bad number of movable pawns ("+Integer.toString(points.size())+")";
		Point point = points.get(0);
		assert point.x == 5 : "UnitTestIMover.checkMovablesPawns : x should equal 5 ("+Integer.toString(point.x)+")";
		assert point.y == 0 : "UnitTestIMover.checkMovablesPawns : y should equal 0 ("+Integer.toString(point.y)+")";

		boardS.setValue(5, 0, 0);
		// player south p2
		points = IMover.movablesPawns(boardS);
		assert points.size() == 2 : "UnitTestIMover.checkMovablesPawns : Bad number of movable pawns ("+Integer.toString(points.size())+")";
		int[] posS = {1,3};
		for (int i=0; i<2; i++)
		{
			point = points.get(i);
			assert point.x == 3 : "UnitTestIMover.checkMovablesPawns : x should equal 3 ("+Integer.toString(point.x)+")";
			assert point.y == posS[i] : "UnitTestIMover.checkMovablesPawns : y should equal "+Integer.toString(posS[i])+" ("+Integer.toString(point.y)+")";
		}

	}

	/**
	 * Check the accessible function.
	 */
	protected static void checkAccessible ()
	{
		// Check basic point movements.
		int[][] one = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,1,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board boardOne = new Board(0, one);
		LinkedList<Board> boardsOne = IMover.accessible(boardOne, new Point(2, 3));
		assert boardsOne.size() == 4 : "UnitTestIMover.checkAccessible : Pawn 1 doesnt have 4 moves ("+Integer.toString(boardsOne.size())+")";

		int[][] two = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board boardTwo = new Board(0, two);
		LinkedList<Board> boardsTwo = IMover.accessible(boardTwo, new Point(2, 3));
		assert boardsTwo.size() == 8 : "UnitTestIMover.checkAccessible : Pawn 2 doesnt have 8 moves ("+Integer.toString(boardsTwo.size())+")";

		int[][] threep1 = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,3,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board boardThreep1 = new Board(0, threep1);
		LinkedList<Board> boardsThreep1 = IMover.accessible(boardThreep1, new Point(2, 3));
		assert boardsThreep1.size() == 14 : "UnitTestIMover.checkAccessible : Pawn 3 (p1) doesnt have 14 moves ("+Integer.toString(boardsThreep1.size())+")";

		int[][] threep2 = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,3,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board boardThreep2 = new Board(1, threep2); // Must be 0 to avoid access to base
		LinkedList<Board> boardsThreep2 = IMover.accessible(boardThreep2, new Point(3, 2));
		assert boardsThreep2.size() == 14 : "UnitTestIMover.checkAccessible : Pawn 3 (p2) doesnt have 14 moves ("+Integer.toString(boardsThreep2.size())+")";

		// Basic base access.
		for (int y=0; y<6; y++)
		{
			for (int p=1; p<4; p++)
			{
				{
				int[][] empty = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};

				// player north
				Board board = new Board(0, empty);
				Point point = new Point(6-p,y);
				board.setValue(point, p);
				assert board.nbPawns() == 1 : "UnitTestIMover.checkAccessible : weird "+point+" "+Integer.toString(p);

				LinkedList<Board> boards = IMover.accessible(board, point);

				assert boards.size() == 1 : "UnitTestIMover.checkAccessible : Winning move, more than one board ("+Integer.toString(boards.size())+")";
				assert boards.get(0).bases[1] == p : "UnitTestIMover.checkAccessible : Winning move, base 1 doesnt have the pawn "
														+Integer.toString(p)+" ("+Integer.toString(boards.get(0).bases[1])+")";
				}

				{
				int[][] empty = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};

				// player south
				Board board = new Board(1, empty);
				Point point = new Point(p-1,y);
				board.setValue(point, p);
				assert board.nbPawns() == 1 : "UnitTestIMover.checkAccessible : weird "+point+" "+Integer.toString(p);

				LinkedList<Board> boards = IMover.accessible(board, point);
				assert boards.size() == 1 : "UnitTestIMover.checkAccessible : Winning move, more than one board ("+Integer.toString(boards.size())+")";
				assert boards.get(0).bases[0] == p : "UnitTestIMover.checkAccessible : Winning move, base 0 doesnt have the pawn "
														+Integer.toString(p)+" ("+Integer.toString(boards.get(0).bases[0])+")";
				}
			}
		}

		// Test jumping
		int[][] jump = {{0,0,2,0,1,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board jumpBoard = new Board(0, jump);
		LinkedList<Board> boards = IMover.accessible(jumpBoard, new Point(0,2));
		assert boards.size() == 12 : "UnitTestIMover.checkAccessible : Bad jump, instead of 12 board we have "+Integer.toString(boards.size());

		// Check result
		int[][] jump2 = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,3,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		Board jumpBoard2 = new Board(1, jump2);

		LinkedList<Board> boards2 = IMover.accessible(jumpBoard2, new Point(3,1));
		assert boards2.size() == 14+23 : "UnitTestIMover.checkAccessible : Bad jump, instead of 37 board we have "+Integer.toString(boards2.size());

		LinkedList<Board> result = new LinkedList<Board>();

		{ int[][] b = {{0,3,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,3,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{3,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,3,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,3,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,3,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{3,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,3,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,3,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,3,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,3,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{3,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,2,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,3,0,0,0}}; result.add(new Board(0, b)); }

		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{2,0,0,3,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,2,0,3,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,2,3,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,3,2,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		{ int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,3,0,2},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}; result.add(new Board(0, b)); }
		for (int i=3;i<6;i++)
		{
			for (int j=0; j<6; j++)
			{
				int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,3,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
				b[i][j] = 2;
				result.add(new Board(0, b));
			}
		}

		for (Board board : result)
		{
			assert boards2.contains(board) : "UnitTestIMover.checkAccessible : Missing board.\n"+board.toString();
		}
	}

	/**
	 * Check the isAccessibleFrom function.
	 */
	protected static void checkIsAccessibleFrom ()
	{
		Board boardAccessibilityP0;
		Board boardAccessibilityP1;

		boolean north;
		boolean correct;
		int correctPawnValue;
		int base;


		//test base accessibility
		for (int i = 0; i<6; i++)
		{
			north = i<3;

			base = (north) ? -1 : 6;

			correctPawnValue = (north) ? i+1 : 6-i;

			for (int j=0; j<6; j++)
			{
				for (int pawnValue=1; pawnValue < 4; pawnValue++)
				{
					correct = (pawnValue == correctPawnValue) || (pawnValue > correctPawnValue);
					int[][] b0 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
					int[][] b1 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};

					boardAccessibilityP0 = new Board(0, b0);
					boardAccessibilityP1 = new Board(1, b1);

					boardAccessibilityP0.setValue(i,j,pawnValue);
					boardAccessibilityP0.setValue(i,j,pawnValue);

					assert (IMover.isAccessibleFrom(new Point(i,j), new Point(base,0),boardAccessibilityP0,pawnValue,42) == (correct && !north)) : "Player 0 Pawn " + Integer.toString(pawnValue)+" access base " + Integer.toString(base)+" from " + Integer.toString(i) + ", "+Integer.toString(j) + "(" + Boolean.toString(!north) + ")";
					assert (IMover.isAccessibleFrom(new Point(i,j), new Point(base,0),boardAccessibilityP1,pawnValue,42) == (correct &&  north)) : "Player 1 Pawn " + Integer.toString(pawnValue)+" access base " + Integer.toString(base)+" from " + Integer.toString(i) + ", "+Integer.toString(j) + "(" + Boolean.toString(north) + ")";
				}
			}
		}

		//Random displacement test
		long seed = 424242;
		int pawn;
		Random randomGenerator = new Random(seed);
		for (int i=0; i<100; i++)
		{
			int[][] b0 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
			boardAccessibilityP0 = new Board(0,b0);
			int[][] b1 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
			boardAccessibilityP1 = new Board(1,b1);

			Point pawnPos = new Point(randomGenerator.nextInt(6),randomGenerator.nextInt(6));

			pawn = randomGenerator.nextInt(3);
			boardAccessibilityP0.setValue(pawnPos, pawn+1);
			boardAccessibilityP1.setValue(pawnPos, pawn+1);

			int size = IMover.moves.get(pawn).size();

			Point to = Point.add(pawnPos, IMover.moves.get(pawn).get(randomGenerator.nextInt(size)));

			if (!to.isInBound())
				continue;


			assert (IMover.isAccessibleFrom(pawnPos, to, boardAccessibilityP0, pawn+1, 42) == (to.x != -1)): "(Player 0) Pawn " +Integer.toString(pawn+1) +" in " +Integer.toString(pawnPos.x) +", " +Integer.toString(pawnPos.y)+ " can't access " +Integer.toString(to.x) +", "+ Integer.toString(to.y);
			assert (IMover.isAccessibleFrom(pawnPos, to, boardAccessibilityP1, pawn+1, 42) == (to.x !=  6)): "(Player 1) Pawn " +Integer.toString(pawn+1) +" in " +Integer.toString(pawnPos.x) +", " +Integer.toString(pawnPos.y)+ " can't access " +Integer.toString(to.x) +", "+ Integer.toString(to.y);


		}

		//Test no rollback
		int[][] b0 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,0,2,0,0},{0,0,2,3,2,0,0},{0,0,2,2,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP0 = new Board(0,b0);
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(2,3),boardAccessibilityP0, 3, 42)) : "RollBack north is authorized";

		int[][] b1 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,2,2,0,0},{0,0,2,3,2,0,0},{0,0,2,0,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP0 = new Board(0,b1);
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(4,3), boardAccessibilityP0, 3, 42)): "RollBack north is authorized";

		int[][] b2 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,2,2,0,0},{0,0,0,3,2,0,0},{0,0,2,2,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP0 = new Board(0,b2);
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(3,2), boardAccessibilityP0, 3, 42)): "RollBack west is authorized";

		int[][] b3 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,2,2,0,0},{0,0,2,3,0,0,0},{0,0,2,2,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP0 = new Board(0,b3);
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(3,4), boardAccessibilityP0, 3, 42)): "RollBack east is authorized";


		//Test passing through
		int[][] p0 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,0,2,0,0},{0,0,2,3,2,0,0},{0,0,2,2,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP0 = new Board(0,p0);

		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(2,1),boardAccessibilityP0, 3, 42)) : "Pawn 3 pass through other pawn (north west)";
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(2,5),boardAccessibilityP0, 3, 42)) : "Pawn 3 pass through other pawn (north east)";

		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(3,2),boardAccessibilityP0, 3, 42)) : "Pawn 3 pass through other pawn (west)";
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(3,4),boardAccessibilityP0, 3, 42)) : "Pawn 3 pass through other pawn (east)";

		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(4,3),boardAccessibilityP0, 3, 42)) : "Pawn 3 pass through other pawn (south)";

		int[][] p1 = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,2,2,2,0,0},{0,0,2,3,2,0,0},{0,0,2,0,2,0,0},{0,0,0,0,0,0,0}};
		boardAccessibilityP1 = new Board(0,p1);

		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(1,3),boardAccessibilityP1, 3, 42)) : "Pawn 3 pass through other pawn (south west)";
		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(5,3),boardAccessibilityP1, 3, 42)) : "Pawn 3 pass through other pawn (south east)";

		assert (!IMover.isAccessibleFrom(new Point(3,3), new Point(2,3),boardAccessibilityP1, 3, 42)) : "Pawn 3 pass through other pawn (north)";

	}

	/**
	 * Check the replacement function.
	 */
	protected static void checkReplacement()
	{
		int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,2,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
		Board board0 = new Board(0,b);
		Board board1 = new Board(1,b);

		assert ( IMover.replacement(board0, new Point(2,4),0).size() == 17 ) : "Not 17 new board for replacement (2,4) for P0";
		assert ( IMover.replacement(board1, new Point(2,4),0).size() == 23 ) : "Not 23 new board for replacement (2,4) for P1";

		// Random test
		long seed = 424242;
		Random randomGenerator = new Random(seed);

		for (int i=0; i < 100 ; i++)
		{
			int[][] empty = {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
			Point pawnPos = new Point(randomGenerator.nextInt(6),randomGenerator.nextInt(6));
			String pawnText = Integer.toString(pawnPos.x) + ","+Integer.toString(pawnPos.y);

			board0 = new Board(0,empty);
			board1 = new Board(1,empty);

			//Any value to show that there is a pawn.
			board0.setValue(pawnPos,1);

			assert ( IMover.replacement(board0, pawnPos,0).size() == (pawnPos.x+1)*6-1 ) : "Not "+Integer.toString((pawnPos.x+1)*6-1) +" new board for replacement "+pawnText+" for P0";
			assert ( IMover.replacement(board1, pawnPos,0).size() == (6-pawnPos.x)*6-1 ) : "Not "+Integer.toString((6-pawnPos.x)*6-1) +" new board for replacement "+pawnText+" for P1";
		}

		//Test result board.

		int[][] startBoardP0 = {{0,0,0,0,0,0,0},{3,3,3,2,3,3,3},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
		board0 = new Board(0,startBoardP0);
		board1 = board0.rotate();
		board1.player = 1;

		int[][][] resP0 = {
						{{2,0,0,0,0,0},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}},
						{{0,2,0,0,0,0},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}},
						{{0,0,2,0,0,0},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}},
						{{0,0,0,2,0,0},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}},
						{{0,0,0,0,2,0},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}},
						{{0,0,0,0,0,2},{3,3,3,1,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}}};

		LinkedList<Board> resultsP0 = IMover.replacement(board0, new Point(1,3), 1);
		LinkedList<Board> resultsP1= IMover.replacement(board1, new Point(4,3), 1);

		for (int i=0; i < 6 ; i++)
		{
			Board temp = new Board(1,resP0[i]);
			Board temp1 = temp.rotate();

			assert (temp.getHashKey().compareTo(resultsP0.get(i).getHashKey()) == 0) : Integer.toString(i) + " res is  incorrect (Player0)";
			assert (temp1.getHashKey().compareTo(resultsP1.get(i).getHashKey()) == 0) : Integer.toString(i) + " res is  incorrect (Player1)";
		}


	}

	/**
	 *Check listMoves function.
	 */
	public static void checkListMoves()
	{
		int[][] boardP0 = {{0,0,0,1,0,0,0},{3,3,3,2,3,3,3},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
		Board board0 = new Board(0,boardP0);

		Board board1 = board0.rotate();
		board1.player = 1;

		//assert (mover.listMoves(board0).size() == 11) : "Not 11 moves for player 0(found "+Integer.toString(mover.listMoves(board0).size())+")";
		//assert (mover.listMoves(board1).size() == 11) : "Not 11 moves for player 1(found "+Integer.toString(mover.listMoves(board1).size())+")";


		int[][] board = {{1,1,2,2,3,3},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{2,1,3,1,3,2}};
		board0 = new Board(0,board);
		board1 = new Board(1,board);


		assert (IMover.listMoves(board0).size() == 231) : "Not  moves for player 0(found "+Integer.toString(IMover.listMoves(board0).size())+")";
		assert (IMover.listMoves(board1).size() == 548) : "Not  moves for player 1(found "+Integer.toString(IMover.listMoves(board1).size())+")";

	}


}

class MockIMover extends IMover
{
	public MockIMover()
	{
		IMover.init();
	}

	public Board nextMove(Board board)
	{
		System.err.println("Call to undefined mock function (MockIMover.nextMove)");
		System.exit(1);

		return null;
	}
}
