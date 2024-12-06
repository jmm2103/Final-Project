package checkers;

import java.awt.Color;
import java.awt.Graphics2D;
import checkers.data.Point;

/**
 * Represents a checker piece on the game board
 *
 * @author Jonathan Makenene
 * @version Final Project
 */
public class Piece {
    // The color of the piece (Red or Black)
    private final Color color;
    // Current position on the board
    private Point position;
    // Whether this piece is a king
    private boolean isKing;

    /**
     * Creates a new checker piece
     *
     * @param color the color of the piece
     * @param position the initial position
     */
    public Piece(Color color, Point position) {
        this.color = color;
        this.position = position;
        this.isKing = false;
    }

    /**
     * Draws the piece on the board
     *
     * @param g the graphics context
     * @param x the x-coordinate to draw at
     * @param y the y-coordinate to draw at
     * @param size the size of the piece
     */
    public void draw(Graphics2D g, int x, int y, int size) {
        // Draw piece shadow for 3D effect
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(x + 2, y + 2, size - 4, size - 4);
        
        // Draw main piece
        g.setColor(color);
        g.fillOval(x, y, size - 4, size - 4);
        
        // Draw crown for king pieces
        if (isKing) {
            g.setColor(Color.YELLOW);
            int crownSize = size / 3;
            g.fillRect(x + size/3, y + size/3, crownSize, crownSize);
        }
    }

    /**
     * Updates the piece's position
     *
     * @param newPosition the new position
     */
    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    /**
     * Gets the current position
     *
     * @return the current position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Checks if this piece is a king
     *
     * @return true if king, false otherwise
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * Sets whether this piece is a king
     *
     * @param king true to make king, false otherwise
     */
    public void setKing(boolean king) {
        isKing = king;
    }

    /**
     * Gets the color of this piece
     *
     * @return the piece's color
     */
    public Color getColor() {
        return color;
    }
}
