package gyges.model;

import gyges.model.Point;

/**
 * Unit test for Point.java.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class UnitTestPoint
{
	/**
	 * Launch the tests.
	 */
	public static void test ()
	{
		// Basic tests: try them randomly. (valid points)
		for (int i=0; i<100; i++)
		{
			int x = (int)(Math.random() * 6);
			int y = (int)(Math.random() * 6);

			Point p = new Point(x, y);

			int x2 = (int)(Math.random() * 6);
			int y2 = (int)(Math.random() * 6);

			Point p2 = new Point(x2, y2);

			checkStore(p, x, y);
			checkStore(p2, x2, y2);

			checkInRange(p, true);
			checkInRange(p2, true);

			checkEqual(p, p);
			checkEqual(p, p2);
			checkEqual(p2, p2);

			checkAdd(p, p);
			checkAdd(p, p2);
			checkAdd(p2, p2);

			// Check hashCode
			assert p.hashCode() == p.hashCode() : "UnitTestPoint : Ask twice get two hash keys";
			Point copyP = new Point(p.x, p.y);
			assert p.hashCode() == copyP.hashCode() : "UnitTestPoint : Same point (2 objects) two keys";
			Point diffP = new Point(p.x+1, p.y+1);
			assert p.hashCode() != diffP.hashCode() : "UnitTestPoint : Different points have the same key";
		}

		// Basic tests: try them randomly. (invalid points)
		for (int i=0; i<100; i++)
		{
			// invalid too big point
			int x = 6 + (int)(Math.random() * 9999);
			int y = 6 + (int)(Math.random() * 9999);

			Point p = new Point(x, y);

			checkStore(p, x, y);
			checkInRange(p, false);
			checkAdd(p, p);

			// invalid too small point
			x = -1 * x;
			y = -1 * y;

			p = new Point(x, y);

			checkStore(p, x, y);
			checkInRange(p, false);
			checkAdd(p, p);
		}

		System.out.println("UnitTestPoint: Ok.");
	}

	/**
	 * Check that x and y value are stored correctly.
	 */
	protected static void checkStore(Point p, int x, int y)
	{
		assert p.x==x : "UnitTestPoint.checkStore : x value not stored correctly p.x="+Integer.toString(p.x)+" and x="+Integer.toString(x);
		assert p.y==y : "UnitTestPoint.checkStore : y value not stored correctly p.y="+Integer.toString(p.x)+" and y="+Integer.toString(x);
	}

	/**
	 * Check the function that test if a Point is a valid Gyges Board point.
	 */
	protected static void checkInRange(Point p, boolean expect)
	{
		assert p.isInBound()==expect : "UnitTestPoint.checkInRange : expected "+Boolean.toString(expect)+" got "+Boolean.toString(!expect);
	}

	/**
	 * Check the equal() function
	 */
	protected static void checkEqual(Point p, Point p2)
	{
		if (p.x == p2.x && p.y == p2.y)
		{
			// Points are equals
			assert p.equals(p2) : "UnitTestPoint.checkEqual : points should have been equals p="+p+" p2="+p2;
		}
		else
		{
			// Points arent equals
			assert !p.equals(p2) : "UnitTestPoint.checkEqual : points shouldnt have been equals p="+p+" p2="+p2;
		}
	}

	/**
	 * Check the add function of adding points.
	 */
	protected static void checkAdd(Point p, Point p2)
	{
		Point p3 = Point.add(p, p2);
		Point p4 = Point.add(p2, p);

		// addition is commutative
		assert p3.equals(p4) : "UnitTestPoint.checkAdd addition should have been commutative p3="+p3+" p4="+p4;

		int nx = p.x + p2.x;
		int ny = p.y + p2.y;
		assert p3.x==nx : "UnitTestPoint.checkAdd x value is incorrect p3.x="+Integer.toString(p3.x)+" and nx="+Integer.toString(nx);
		assert p3.y==ny : "UnitTestPoint.checkAdd y value is incorrect p3.y="+Integer.toString(p3.x)+" and ny="+Integer.toString(nx);
	}
}
