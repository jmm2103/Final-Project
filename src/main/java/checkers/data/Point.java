package checkers.data;

/**
 * An immutable class representing a position on the checkers board
 *
 * @author Jonathan Makenene
 * @version Final Project
 */
public class Point {
    // The x-coordinate (0-7, left to right)
    private final int x;
    // The y-coordinate (0-7, top to bottom)
    private final int y;

    /**
     * Creates a new Point with the given coordinates
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate
     *
     * @return the x-coordinate
     */
    public int getX() { 
        return x; 
    }
    
    /**
     * Gets the y-coordinate
     *
     * @return the y-coordinate
     */
    public int getY() { 
        return y; 
    }

    /**
     * Checks if this Point equals another object
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point)) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    /**
     * Generates a hash code for this Point
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
