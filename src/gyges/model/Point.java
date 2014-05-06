package gyges.model;

/**
 * Represent a 2D point.
 *
 * @author Valentin Lemiere
 * @author Guillaume Desquesnes
 */
public class Point
{
	/** The x value of the point. */
	public int x;
	/** The y value of the point. */
	public int y;

	/**
	 * Create a point.
	 *
	 * @param x The x value of the point.
	 * @param y The y value of the point.
	 */
	public Point (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Add 2 points.
	 *
	 * @param p1 The first point
	 * @param p2 The second point
	 *
	 * @return The point resulting in p1+p2
	 */
	public static Point add (Point p1, Point p2)
	{
		return new Point(p1.x+p2.x, p1.y+p2.y);
	}

	/**
	 * Test if the point is on the board.
	 *
	 * @return True if the point is on the board, else false.
	 */
	public boolean isInBound ()
	{
		return (0 <= x && x < 6 && 0 <= y && y < 6);
	}

	/**
	 * Generate a unique id for the position of the point.
	 * Work only for values of x and y between 0 and 5 (in the board).
	 *
	 * @return Unique id for position.
	 */
	public int hashCode ()
	{
		return x*6 + y;
	}

	/**
	 * Test if two point are equals.
	 * If other isnt a Point return always false.
	 *
	 * @param other Point to compare to.
	 *
	 * @return True if this equals other, else false.
	 */
	public boolean equals (Object other)
	{
		// Not an instance of Point, therefor not equal to this.
		if (!(other instanceof Point))
		{
			return false;
		}

		return this.hashCode() == other.hashCode();
	}

	/**
	 * Return a String version of the point.
	 *
	 * @return A String version of the point
	 */
	public String toString()
	{
		StringBuffer s = new StringBuffer("(");
		s.append(Integer.toString(this.x));
		s.append(",");
		s.append(Integer.toString(this.y));
		s.append(")");

		return s.toString();
	}
}
