package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CheckersGame extends JFrame {
    private Board board;
    private JLabel statusLabel;
    private JButton resetButton;

    /**
     * Constructs a new CheckersGame object
     */
    public CheckersGame() {
        setTitle("Checkers Game"); //creates the game window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closes the game if the window is closed
        setLayout(new BorderLayout(10, 10));
        
        // Initialize game components
        board = new Board();
        statusLabel = new JLabel("Red's Turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create control panel
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.add(statusLabel, BorderLayout.CENTER);
        controlPanel.add(resetButton, BorderLayout.EAST);
        
        // Add components to frame
        add(board, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        // Add mouse listeners for drag and drop
        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                board.startDrag(e.getX(), e.getY());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                board.endDrag(e.getX(), e.getY());
                updateStatus();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                handleBoardClick(e.getX(), e.getY());
            }
        });
        
        //updates the position of a dragged piece
        board.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                board.updateDrag(e.getX(), e.getY());
            }
        });
        
        resetButton.addActionListener(e -> resetGame());
        
        // Set window properties
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    // Handles clicks on the game board
    // if the player clicks on a piece, it selects it
    // if the player clicks on an empty space, it moves the selected piece
    private void handleBoardClick(int x, int y) {
        if (!board.selectPiece(x, y)) {
            if (board.movePiece(x, y)) {
                updateStatus();
            }
        }
    }
    
    // Updates the status label
    private void updateStatus() {
        if (board.hasWon()) {
            String winner = (board.getCurrentPlayer() == Color.RED) ? "Black" : "Red";
            statusLabel.setText(winner + " Wins!");
            return;
        }
        
        if (board.getCurrentPlayer() == Color.RED) {
            statusLabel.setText("Red's Turn");
        } else {
            statusLabel.setText("Black's Turn");
        }
    }
    
    //resets the game
    private void resetGame() {
        board.resetBoard();
        statusLabel.setText("Red's Turn");
    }
    
    public static void main(String[] args) {
        // Start the game
        //using SwingUtilities.invokeLater to ensure the GUI is created on the Swing thread
        SwingUtilities.invokeLater(() -> {
            CheckersGame game = new CheckersGame();
            game.setVisible(true);
        });
    }
}
