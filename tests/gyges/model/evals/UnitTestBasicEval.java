package gyges.model.evals;

import java.util.Random;

import gyges.model.Board;
import gyges.model.evals.BasicEval;

/**
 * Unit test for BasicEval.java.
 *
 * Suppose that Board.java works normally.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class UnitTestBasicEval
{
	/**
	 * Launch the tests.
	 */
	public static void test ()
	{
		long seed = 424242;
		BasicEval eval = new BasicEval();
		Board.eval = eval;

		// Test empty board evaluation.

		int[][] empty = {{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};

		Board emptyBoardPlayer0 = new Board(0,empty);
		assert (emptyBoardPlayer0.eval() == 0) : "UnitTestBasicEval : Empty board have a value different of 0 ("+Integer.toString(emptyBoardPlayer0.eval())+")";
		assert (eval.getValue(emptyBoardPlayer0) == 18) : "UnitTestBasicEval : Empty board have a value different of 18 ("+Integer.toString(eval.getValue(emptyBoardPlayer0))+")";

		Board emptyBoardPlayer1 = new Board(1,empty);
		assert (emptyBoardPlayer1.eval() == 0) : "UnitTestBasicEval : Empty board have a value different of 0 "+Integer.toString(emptyBoardPlayer1.eval())+")";
		assert (eval.getValue(emptyBoardPlayer1.rotate()) == 18) : "UnitTestBasicEval : Empty board have a value different of 18 "+Integer.toString(eval.getValue(emptyBoardPlayer1.rotate()))+")";

		// Basic random test
		Random randomGen = new Random(seed);
		int pawn;
		int base;

		for (int test = 0; test <100; test++)
		{
			int[][] b = new int[6][6];

			int valueP0 = 0;
			int valueP1 = 0;

			for (int i=0; i < 6; i++)
			{
				for (int j=0; j < 6; j++)
				{
					pawn = randomGen.nextInt(3);

					if (i < 3) // i = 0,1,2
					{
						if (pawn != i)
							valueP0++;
					}

					else if (i>2) // i = 3,4,5
					{
						if (pawn != 5-i)
							valueP1++;
					}

					b[i][j] = pawn+1;
				}
			}

			Board b0 = new Board(0,b);
			Board b1 = new Board(1,b);

			base = randomGen.nextInt(3);
			if (base != 2)
			{
				int baseVal = randomGen.nextInt(3)+1;

				b0.bases[base] = baseVal;
				b1.bases[base] = baseVal;

				valueP0 += (base == 1) ? 10000 : 0;
				valueP1 += (base == 0) ? 10000 : 0;
			}

			assert (b0.eval() == valueP0-valueP1) : "UnitTestBasicEval : Incorrect evaluation of board (player0) eval="+
												Integer.toString(b0.eval())+" valueP0="+Integer.toString(valueP0);
			assert (eval.getValue(b0) == valueP0) : "UnitTestBasicEval : Eval function is incorrect (player0) eval="+
												Integer.toString(eval.getValue(b0))+" valueP0="+Integer.toString(valueP0);

			assert (b1.eval() == valueP1-valueP0) : "UnitTestBasicEval : Incorrect evaluation of board (player1) eval="+
												Integer.toString(b1.eval())+" valueP1="+Integer.toString(valueP1);
			assert (eval.getValue(b1.rotate()) == valueP1) : "UnitTestBasicEval : Eval function is incorrect (player1) eval="+
												Integer.toString(eval.getValue(b1.rotate()))+" valueP1="+Integer.toString(valueP1);
		}

		System.out.println("UnitTestBasicEval: Ok.");
	}
}
