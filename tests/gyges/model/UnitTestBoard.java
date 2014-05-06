package gyges.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import gyges.model.Board;
import gyges.model.Point;
import gyges.model.evals.IEval;

/**
 * Unit test for Board.java.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class UnitTestBoard
{
	/**
	 * Launch the tests.
	 */
	public static void test ()
	{
		Board.eval = new MockEval();

		int[][] empty = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};

		// Basic tests: try them randomly.
		for (int i=0; i<100; i++)
		{
			Board board = new Board(0, null, null);

			checkFromBoard(board);
			validSize(board);
			allPawns(board);
			checkCopy(board);
			checkIsFree(board);
			checkRotation(board);

			// Check hashkey
			assert board.getHashKey().equals(board.getHashKey()) : "UnitTestBoard : Ask twice get two hash keys";
			Board b = new Board(0, board.getCopyBoard());
			assert board.getHashKey().equals(b.getHashKey()) : "UnitTestBoard : Same board (2 objects) two keys";
			b.setValue(1, 2, 3); // add a 3 pawn in case (1, 2)
			assert !board.getHashKey().equals(b.getHashKey()) : "UnitTestBoard : Different objects have the same key";

			// Change the board value, must be last.
			startPosition(board);

			checkSetGetValue(new Board(0, empty));
		}

		checkPlayer();
		checkEndGame();
		checkPlayerLine();
		checkEval();
		checkCompareTo();

		System.out.println("UnitTestBoard: Ok.");
	}

	/**
	 *	Test compareTo.
	 */
	protected static void checkCompareTo()
	{
		int[][] b = {{42,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{24,0,0,0,0,0}};

		// Board is equal to itself (value 42)
		Board board42 = new Board(0, b);
		assert board42.compareTo(board42)==0 : "UnitTestBoard.checkCompareTo : board value isnt equal to itself";

		// Board of value 24
		Board board24 = new Board(1, b);

		assert board42.compareTo(board24)==1 : "UnitTestBoard.checkCompareTo : board 42 isnt bigger than board 24";
		assert board24.compareTo(board42)==-1 : "UnitTestBoard.checkCompareTo : board 24 isnt lesser than board 42";

		// Should work too with Collection.min and Collection.max
		LinkedList<Board> boards = new LinkedList<Board>();

		for (int i=1; i<=100; i++)
		{
			int[][] c = {{i,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
			boards.add(new Board(0, c));
			boards.add(new Board(1, c));
		}

		// max
		Board maxB = Collections.max(boards);
		assert maxB.eval() == 100 : "UnitTestBoard.checkCompareTo : max value board should have been 100 ("+Integer.toString(maxB.eval())+")";

		// min
		Board minB = Collections.min(boards);
		assert minB.eval() == -100 : "UnitTestBoard.checkCompareTo : min value board should have been -100 ("+Integer.toString(maxB.eval())+")";

		// sort
		Collections.sort(boards);
		for (int i=0; i<100; i++)
		{
			assert boards.get(i).eval() == -1 * (100-i) : "UnitTestBoard.checkCompareTo : bad sort of negatives values. i="+
															Integer.toString(i)+" eval="+Integer.toString(boards.get(i).eval());
		}
		for (int i=100; i<200; i++)
		{
			assert boards.get(i).eval() == i-99 : "UnitTestBoard.checkCompareTo : bad sort of positives values. i="+
															Integer.toString(i)+" eval="+Integer.toString(boards.get(i).eval());
		}
	}

	/**
	 *	Test eval.
	 */
	protected static void checkEval()
	{
		int[][] b = {{42,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{24,0,0,0,0,0}};

		// Board has value of 18 for player 0
		Board board = new Board(0, b);
		assert board.eval() == 18 : "UnitTestBoard.checkEval : player 0 should have value 18 ("+Integer.toString(board.eval())+")";

		// Board has value of -18 for player 1
		board = new Board(1, b);
		assert board.eval() == -18 : "UnitTestBoard.checkEval : player 1 should have value -18 ("+Integer.toString(board.eval())+")";
	}

	/**
	 * Check playerLine.
	 */
	protected static void checkPlayerLine ()
	{
		// Start line
		Board board = new Board(0, null, null);
		assert board.playerLine()==0 : "UnitTestBoard.checkPlayerLine : player 0 line number is wrong ("+Integer.toString(board.playerLine())+")";
		assert board.advPlayerLine()==5 : "UnitTestBoard.checkPlayerLine : player 1 line number is wrong ("+Integer.toString(board.advPlayerLine())+")";

		// 1 and 4
		{
		int[][] b = {{0,0,0,0,0,0},{1,1,2,2,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0},{1,1,2,2,3,3},{0,0,0,0,0,0}};
		board = new Board(0, b);
		assert board.playerLine()==1 : "UnitTestBoard.checkPlayerLine : player 0 line number is wrong ("+Integer.toString(board.playerLine())+")";
		assert board.advPlayerLine()==4 : "UnitTestBoard.checkPlayerLine : player 1 line number is wrong ("+Integer.toString(board.advPlayerLine())+")";
		}

		// 2 and 3
		{
		int[][] b = {{0,0,0,0,0,0},{0,0,0,0,0,0},{1,1,2,2,3,3},{1,1,2,2,3,3},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		board = new Board(0, b);
		assert board.playerLine()==2 : "UnitTestBoard.checkPlayerLine : player 0 line number is wrong ("+Integer.toString(board.playerLine())+")";
		assert board.advPlayerLine()==3 : "UnitTestBoard.checkPlayerLine : player 1 line number is wrong ("+Integer.toString(board.advPlayerLine())+")";
		}

		// 1 and 5
		{
		int[][] b = {{0,0,0,0,0,0},{0,0,2,0,0,0},{1,1,0,2,3,3},{1,1,2,2,0,3},{0,0,0,0,0,0},{0,0,0,0,3,0}};
		board = new Board(0, b);
		assert board.playerLine()==1 : "UnitTestBoard.checkPlayerLine : player 0 line number is wrong ("+Integer.toString(board.playerLine())+")";
		assert board.advPlayerLine()==5 : "UnitTestBoard.checkPlayerLine : player 1 line number is wrong ("+Integer.toString(board.advPlayerLine())+")";
		}
	}

	/**
	 * Check if player is set correctly.
	 */
	protected static void checkPlayer ()
	{
		Board board = new Board(1, null, null);
		assert board.player==1 : "UnitTestBoard.checkPlayer : should be 1 ("+Integer.toString(board.player)+")";
		board = new Board(0, null, null);
		assert board.player==0 : "UnitTestBoard.checkPlayer : should be 0 ("+Integer.toString(board.player)+")";
	}

	/**
	 * Check isFinished + winner
	 */
	protected static void checkEndGame ()
	{
		Board board = new Board(1, null, null);

		assert !board.isFinished() : "UnitTestBoard.checkEndGame : bases are empty but end of game detected";
		assert board.winner() == 2 : "UnitTestBoard.checkEndGame : no-one should have won yet winner="+Integer.toString(board.winner());

		board.bases[0] = 0; board.bases[1] = 3;
		assert board.isFinished() : "UnitTestBoard.checkEndGame : base have pawns but end of game not detected";
		assert board.winner() == 0 : "UnitTestBoard.checkEndGame : north (0) should have won winner="+Integer.toString(board.winner());

		board.bases[1] = 0; board.bases[0] = 3;
		assert board.isFinished() : "UnitTestBoard.checkEndGame : base have pawns but end of game not detected";
		assert board.winner() == 1 : "UnitTestBoard.checkEndGame : south (1) should have won winner="+Integer.toString(board.winner());
	}

	/**
	 * Check if a board init from an int[][] is created ok.
	 */
	protected static void checkFromBoard (Board board)
	{
		int[][] b = board.getBoard();
		int[][] b2 = board.getCopyBoard();

		// both Board object should be different object
		assert b!=b2 : "UnitTestBoard.checkFromBoard : same board";

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				// Value should be the same
				assert b[i][j]==b2[i][j] : "UnitTestBoard.checkFromBoard : different value at case ("+Integer.toString(i)+","+
					Integer.toString(j)+") b="+Integer.toString(b[i][j])+" b2="+Integer.toString(b2[i][j]);
			}
		}

	}

	/**
	 * Check to see if the board has valid dimensions.
	 */
	protected static void validSize (Board board)
	{
		int[][] b = board.getBoard();

		// y must be equal to 6.
		int x = b.length;
		assert x==6 : "UnitTestBoard.validSize : x isnt equal to 6 ("+Integer.toString(x)+")";

		// y must be equal to 6 too.
		int y = b[0].length;
		assert y==6 : "UnitTestBoard.validSize : y isnt equal to 6 ("+Integer.toString(y)+")";
	}

	/**
	 * Check that the board has a valid starting position.
	 */
	protected static void startPosition (Board board)
	{
		int[][] b = board.getBoard();
		int[] correct = { 1, 1, 2, 2, 3, 3 };
		int[] empty = { 0, 0, 0, 0, 0, 0 };

		// The first line should have two 1, two 2 and two 3.
		Arrays.sort(b[0]);
		assert Arrays.equals(b[0],correct) : "UnitTestBoard.startPosition : first line incorrect ("+Arrays.toString(b[0])+")";

		// Same for the last line.
		Arrays.sort(b[5]);
		assert Arrays.equals(b[5],correct) : "UnitTestBoard.startPosition : last line incorrect ("+Arrays.toString(b[5])+")";

		// All other should be empty.
		for (int i=1; i<5; i++)
		{
			assert Arrays.equals(b[i],empty) : "UnitTestBoard.startPosition : line "+Integer.toString(i)+" incorrect ("+Arrays.toString(b[i])+")";
		}

		// So there should be 12 pawns.
		assert board.nbPawns()==12 : "UnitTestBoard.startPosition : wrong number of pawns ("+Integer.toString(board.nbPawns())+")";

		// Bases must be empty.
		assert board.bases[0]==0 : "UnitTestBoard.startPosition : base 0 not empty ("+Integer.toString(board.bases[0])+")";
		assert board.bases[1]==0 : "UnitTestBoard.startPosition : base 1 not empty ("+Integer.toString(board.bases[0])+")";

		// Game isnt finished
		assert !board.isFinished() : "UnitTestBoard.startPosition : bases are empty but end of game detected";

		// Players line
		assert board.playerLine()==0 : "UnitTestBoard.startPosition : player 0 line number is wrong ("+Integer.toString(board.playerLine())+")";
		assert board.advPlayerLine()==5 : "UnitTestBoard.startPosition : player 1 line number is wrong ("+Integer.toString(board.advPlayerLine())+")";
	}

	/**
	 * Check that the board has all pawns (and nothing else).
	 */
	protected static void allPawns (Board board)
	{
		int[][] b = board.getBoard();
		int[] nb = { 0, 0, 0, 0 };

		for (int[] l : b)
		{
			for (int p : l)
			{
				// A case can have no pawn (0), or a pawn from value 1 to 3.
				assert (p>=0 && p<=3) : "UnitTestBoard.allPawns : incorrect value for a pawn ("+Integer.toString(p)+")";

				nb[p]++;
			}
		}

		// There must be 24 empty cases.
		assert nb[0]==24 : "UnitTestBoard.allPawns : incorrect nb of empty cases ("+Integer.toString(nb[0])+")";
		// There must be four 1, four 2 and four 3 pawns.
		assert nb[1]==4 : "UnitTestBoard.allPawns : incorrect nb of pawn 1 ("+Integer.toString(nb[1])+")";
		assert nb[2]==4 : "UnitTestBoard.allPawns : incorrect nb of pawn 2 ("+Integer.toString(nb[2])+")";
		assert nb[3]==4 : "UnitTestBoard.allPawns : incorrect nb of pawn 3 ("+Integer.toString(nb[3])+")";
	}

	/**
	 * Check that the return value of the deep copy is equal to the source.
	 */
	protected static void checkCopy (Board board)
	{
		int[][] b = board.getBoard();
		int[][] b2 = board.getCopyBoard();

		// both array should not be the same object
		assert b!=b2 : "UnitTestBoard.checkCopy : copy is the same object";

		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				// Value should be the same
				assert b[i][j]==b2[i][j] : "UnitTestBoard.checkCopy : different value at case ("+Integer.toString(i)+","+
					Integer.toString(j)+") b="+Integer.toString(b[i][j])+" b2="+Integer.toString(b2[i][j]);
			}
		}
	}

	/**
	 * Check isFree() on a newly created board (easier test).
	 * Test both the (int, int) and (Point) versions.
	 */
	protected static void checkIsFree (Board board)
	{
		// First line and last arent free.
		for (int y=0; y<6; y++)
		{
			assert !board.isFree(0, y) : "UnitTestBoard.checkIsFree : a case of the first line is free (0,"+Integer.toString(y)+")";
			// Same for the Point version
			Point p = new Point(0, y);
			assert !board.isFree(p) : "UnitTestBoard.checkIsFree : a case of the first line is free (point version) "+p;

			assert !board.isFree(5, y) : "UnitTestBoard.checkIsFree : a case of the first line is free (5,"+Integer.toString(y)+")";
			// Same for the Point version
			p = new Point(5, y);
			assert !board.isFree(p) : "UnitTestBoard.checkIsFree : a case of the first line is free (point version) "+p;
		}

		// other lines are
		for (int x=1; x<5; x++)
		{
			for (int y=0; y<6; y++)
			{
				assert board.isFree(x, y) : "UnitTestBoard.checkIsFree : a case of another line isnt free ("+Integer.toString(x)+","+Integer.toString(y)+")";
				// Same for the Point version
				Point p = new Point(x, y);
				assert board.isFree(p) : "UnitTestBoard.checkIsFree : a case of another line isnt free (point version) "+p;
			}
		}
	}

	/**
	 * Check that the (int, int) and (Point) versions are the same.
	 * Test on an empty Board (fill with 0).
	 */
	protected static void checkSetGetValue (Board board)
	{
		int x = (int)(Math.random() * 6);
		int y = (int)(Math.random() * 6);
		Point p = new Point(x, y);
		Board board2 = new Board(board.player, board.getCopyBoard());

		board.setValue(x, y, x+y);
		board2.setValue(p, x+y);

		// Value must have changed
		assert board.getBoard()[x][y]==x+y : "UnitTestBoard.checkSetValue : value hasnt changed";
		assert board2.getBoard()[x][y]==x+y : "UnitTestBoard.checkSetValue : value hasnt changed";

		// Get should work the same with (int, int) or (Point)
		assert board.getValue(x, y)==x+y : "UnitTestBoard.checkSetValue : get (int, int) value is incorrect";
		assert board.getValue(p)==x+y : "UnitTestBoard.checkSetValue : get (Point) value is incorrect";
		assert board2.getValue(x, y)==x+y : "UnitTestBoard.checkSetValue : get (int, int) value is incorrect";
		assert board2.getValue(p)==x+y : "UnitTestBoard.checkSetValue : get (Point) value is incorrect";
	}

	/**
	 * Check the rotation of a board.
	 */
	protected static void checkRotation (Board board)
	{
		// Check rotation
		int[] basesBefore = board.bases;
		String firstLineBefore = Arrays.toString(board.getBoard()[0]);
		String lastLineBefore = Arrays.toString(board.getBoard()[5]);

		Board rboard = board.rotate();
		String firstLine = Arrays.toString(rboard.getBoard()[0]);
		String lastLine = Arrays.toString(rboard.getBoard()[5]);

		// Bases
		assert (basesBefore[0] == rboard.bases[1] && basesBefore[1] == rboard.bases[0]) : "UnitTestBoard.checkRotation : bases didn't rotate";
		// First line
		assert firstLineBefore.equals(lastLine) : "UnitTestBoard.checkRotation : first and last lines didn't switch";
		// Last line
		assert lastLineBefore.equals(firstLine) : "UnitTestBoard.checkRotation : last and first lines didn't switch";

		// Rotate 2 times = start board
		assert rboard.rotate().getHashKey().equals(board.getHashKey()) : "UnitTestBoard.checkRotation : Double rotation doesn't equal start board. rBoard="+
																	rboard.rotate().getHashKey() + " startBoard=" + board.getHashKey();
	}
}

/**
 * Mock object version of an eval function.
 * Return the value of [0][0]
 */
class MockEval extends IEval
{
	public int getValue (Board board)
	{
		return board.getBoard()[0][0];
	}
}
