package checkers;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import checkers.data.Point;

/**
 * The main game board for Checkers
 * Manages the game state, piece movement, and rendering
 *
 * @author Jonathan Makenene
 * @version Final Project
 */
public class Board extends JPanel {
    // Board dimensions and appearance
    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = 8;
    private static final Color DARK_CELL = new Color(139, 69, 19);  // Brown
    private static final Color LIGHT_CELL = new Color(255, 228, 181);  // Light brown
    
    // Game state
    private final Map<Point, Piece> pieces;  // //the hashMap is telling us where the pieces are
    private Point selectedPiece;             // Currently selected piece
    private List<Point> validMoves;          // Valid moves for selected piece
    private Color currentPlayer;             // Current player's turn
    private Point draggedPiece;              // Piece being dragged
    private Point draggedTo;                 // Current drag position
    
   /**
    * Creates a new game board
    * Initializes the pieces HashMap to store piece positions
    * Sets Red as starting player
    * Sets board size to 8x8 squares
    */
    public Board() {
        pieces = new HashMap<>();
        validMoves = new ArrayList<>();
        currentPlayer = Color.RED;
        setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));
        initializeBoard();
    }
    
    /**
     * Sets up the initial game state
     * Places 12 red pieces in top 3 rows
     * Places 12 black pieces in bottom 3 rows
     * Pieces only go on dark squares
     */
    private void initializeBoard() {
        pieces.clear();
        // Initialize red pieces (top)
        for (int row = 0; row < 3; row++) {
            for (int col = (row % 2 == 0) ? 1 : 0; col < BOARD_SIZE; col += 2) {
                Point pos = new Point(col, row);
                pieces.put(pos, new Piece(Color.RED, pos));
            }
        }
        
        // Initialize black pieces (bottom)
        for (int row = BOARD_SIZE - 3; row < BOARD_SIZE; row++) {
            for (int col = (row % 2 == 0) ? 1 : 0; col < BOARD_SIZE; col += 2) {
                Point pos = new Point(col, row);
                pieces.put(pos, new Piece(Color.BLACK, pos));
            }
        }
    }
    
   /**
    * Draws the game board and pieces
    * Draws checkerboard pattern
    * Highlights valid moves in green
    * Highlights selected piece in yellow
    * Draws all pieces in their current positions
    * Shows dragged piece following mouse
    */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw checkerboard pattern
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color cellColor = (row + col) % 2 == 0 ? LIGHT_CELL : DARK_CELL;
                g2d.setColor(cellColor);
                g2d.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Highlight valid moves
        g2d.setColor(new Color(0, 255, 0, 100));
        for (Point move : validMoves) {
            g2d.fillRect(move.getX() * CELL_SIZE, move.getY() * CELL_SIZE, 
                        CELL_SIZE, CELL_SIZE);
        }
        
        // Highlight selected piece
        if (selectedPiece != null) {
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillRect(selectedPiece.getX() * CELL_SIZE, 
                        selectedPiece.getY() * CELL_SIZE, 
                        CELL_SIZE, CELL_SIZE);
        }
        
        // Draw all pieces except dragged piece
        for (Map.Entry<Point, Piece> entry : pieces.entrySet()) {
            Point pos = entry.getKey();
            if (pos.equals(draggedPiece)) continue;
            Piece piece = entry.getValue();
            piece.draw(g2d, pos.getX() * CELL_SIZE, pos.getY() * CELL_SIZE, CELL_SIZE);
        }
        
        // Draw dragged piece on top
        if (draggedPiece != null && draggedTo != null) {
            Piece piece = pieces.get(draggedPiece);
            piece.draw(g2d, draggedTo.getX() - CELL_SIZE/2, 
                      draggedTo.getY() - CELL_SIZE/2, CELL_SIZE);
        }
    }
    
    /**
     * Starts dragging a piece
     */
    public void startDrag(int x, int y) {
        Point boardPos = new Point(x / CELL_SIZE, y / CELL_SIZE);
        Piece piece = pieces.get(boardPos);
        
        if (piece != null && piece.getColor() == currentPlayer) {
            draggedPiece = boardPos;
            draggedTo = new Point(x, y);
            selectedPiece = boardPos;
            validMoves = getValidMoves(piece);
            repaint();
        }
    }
    
    /**
     * Updates the position of a dragged piece
     */
    public void updateDrag(int x, int y) {
        if (draggedPiece != null) {
            draggedTo = new Point(x, y);
            repaint();
        }
    }
    
    /**
     * Handles the end of a drag operation
     */
    public void endDrag(int x, int y) {
        if (draggedPiece != null) {
            Point targetPos = new Point(x / CELL_SIZE, y / CELL_SIZE);
            if (validMoves.contains(targetPos)) {
                movePiece(x, y);
            }
            draggedPiece = null;
            draggedTo = null;
            repaint();
        }
    }
    
   /**
    * Selects a piece at given position
    * Checks if position has a piece
    * Checks if it's current player's piece
    * Shows valid moves if selected
    * Returns true if piece was selected
     */
    public boolean selectPiece(int x, int y) {
        Point boardPos = new Point(x / CELL_SIZE, y / CELL_SIZE);
        Piece piece = pieces.get(boardPos);
        
        if (piece != null && piece.getColor() == currentPlayer) {
            selectedPiece = boardPos;
            validMoves = getValidMoves(piece);
            repaint();
            return true;
        }
        return false;
    }
    
    /**
    * Moves selected piece to new position
    * Checks if move is valid
    * Handles captures by removing jumped pieces
    * Promotes to king if reaching opposite end
    * Changes turn to other player
    * Returns true if move was successful
    */
    public boolean movePiece(int x, int y) {
        if (selectedPiece == null) return false;
        
        Point targetPos = new Point(x / CELL_SIZE, y / CELL_SIZE);
        if (!validMoves.contains(targetPos)) return false;
        
        Piece piece = pieces.get(selectedPiece);
        
        // Handle captures
        if (Math.abs(targetPos.getX() - selectedPiece.getX()) == 2) {
            int capturedX = (targetPos.getX() + selectedPiece.getX()) / 2;
            int capturedY = (targetPos.getY() + selectedPiece.getY()) / 2;
            pieces.remove(new Point(capturedX, capturedY));
        }
        
        // Move piece
        pieces.remove(selectedPiece);
        piece.setPosition(targetPos);
        pieces.put(targetPos, piece);
        
        // Check for king promotion
        if (piece.getColor() == Color.RED && targetPos.getY() == BOARD_SIZE - 1 ||
            piece.getColor() == Color.BLACK && targetPos.getY() == 0) {
            piece.setKing(true);
        }
        
        selectedPiece = null;
        validMoves.clear();
        currentPlayer = (currentPlayer == Color.RED) ? Color.BLACK : Color.RED;
        repaint();
        return true;
    }
    
    /**
     * Calculates valid moves for a piece
     */
    private List<Point> getValidMoves(Piece piece) {
        List<Point> moves = new ArrayList<>();
        Point pos = piece.getPosition();
        int direction = (piece.getColor() == Color.RED) ? 1 : -1;
        
        // Regular moves
        addMoveIfValid(moves, pos.getX() - 1, pos.getY() + direction, false);
        addMoveIfValid(moves, pos.getX() + 1, pos.getY() + direction, false);
        
        // King moves
        if (piece.isKing()) {
            addMoveIfValid(moves, pos.getX() - 1, pos.getY() - direction, false);
            addMoveIfValid(moves, pos.getX() + 1, pos.getY() - direction, false);
        }
        
        // Capture moves
        addMoveIfValid(moves, pos.getX() - 2, pos.getY() + 2 * direction, true);
        addMoveIfValid(moves, pos.getX() + 2, pos.getY() + 2 * direction, true);
        if (piece.isKing()) {
            addMoveIfValid(moves, pos.getX() - 2, pos.getY() - 2 * direction, true);
            addMoveIfValid(moves, pos.getX() + 2, pos.getY() - 2 * direction, true);
        }
        
        return moves;
    }
    
    /**
     * Helper method to add valid moves to the list
     */
    private void addMoveIfValid(List<Point> moves, int x, int y, boolean isCapture) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) return;
        
        Point targetPos = new Point(x, y);
        if (pieces.containsKey(targetPos)) return;
        
        if (isCapture) {
            int midX = (x + selectedPiece.getX()) / 2;
            int midY = (y + selectedPiece.getY()) / 2;
            Point midPoint = new Point(midX, midY);
            Piece capturedPiece = pieces.get(midPoint);
            if (capturedPiece != null && capturedPiece.getColor() != currentPlayer) {
                moves.add(targetPos);
            }
        } else {
            moves.add(targetPos);
        }
    }
    
    /**
     * Checks if the current player has won
     */
    public boolean hasWon() {
        Color opponent = (currentPlayer == Color.RED) ? Color.BLACK : Color.RED;
        return pieces.values().stream().noneMatch(p -> p.getColor() == opponent);
    }
    
    /**
     * Resets the board to its initial state
     */
    public void resetBoard() {
        initializeBoard();
        selectedPiece = null;
        validMoves.clear();
        currentPlayer = Color.RED;
        draggedPiece = null;
        draggedTo = null;
        repaint();
    }
    
    /**
     * Gets the current player's color
     */
    public Color getCurrentPlayer() {
        return currentPlayer;
    }
}
