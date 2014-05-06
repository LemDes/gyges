import gyges.model.UnitTestBoard;
import gyges.model.UnitTestGyges;
import gyges.model.UnitTestPoint;
import gyges.model.evals.UnitTestBasicEval;
import gyges.model.movers.UnitTestIMover;

/**
 * Main class for unit testing.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
class UnitTest
{
	/**
	 * Main function.
	 *
	 * @param args The arguments of the program.
	 */
	public static void main (String args[])
	{
		// Call tests for Point.java
		UnitTestPoint.test();

		// Call tests for Board.java
		UnitTestBoard.test();

		// Call tests for BasicEval.java
		UnitTestBasicEval.test();

		// Call tests for INextMoves.java
		UnitTestIMover.test();

		// Call tests for Gyges.java
		UnitTestGyges.test();

		System.out.println("\n=============\nTests Ok.\n=============");
	}
}
