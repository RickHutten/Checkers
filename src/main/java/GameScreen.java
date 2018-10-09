package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Rick on 07-Oct-18.
 */

class GameScreen extends JComponent implements MouseListener {

    /**
     * Rectangle (bounds) of the game board
     */
    private final Rectangle mBoardBounds =
            new Rectangle(Board.BOARD_PADDING, Board.BOARD_PADDING,
                    Board.BOARD_SIZE, Board.BOARD_SIZE);

    GameScreen(Window window) {
        this.addMouseListener(this);

        // Set board to default value
        Board.resetBoard();

        // Set listener that fires when the game is finished
        Component component = this;
        Board.setOnGameFinishedListener(winner -> {
            // Start the game
            window.remove(component);
            window.add(new FinishedScreen(window, winner), BorderLayout.CENTER);
        });

        // Add listener that fires when the game state is updated
        Board.setOnStateChangedListener(() -> {
            // Redraw the screen
            window.revalidate();
            window.repaint();
        });
    }

    /**
     * Draw the game screen
     */
    public void paint(Graphics g) {
        // Set anti-alias
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Draw title
        graphics.setFont(new Font(null, Font.PLAIN, 40));
        Window.drawCenteredString(g, "Checkers", Window.WIDTH / 2, 50);

        // Draw active player
        graphics.setFont(new Font(null, Font.PLAIN, 20));
        String player = Board.getActivePlayer() == Board.Player.BLACK ? "Black" : "White";
        Window.drawCenteredString(g, player + " player is next", Window.WIDTH / 2, 80);

        // Draw board
        Board.draw(graphics);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();

        // If clicked on the board, fire the Board.onClick function
        if (mBoardBounds.contains(point)) {
            // Adjust reference point to the board
            point.translate(-Board.BOARD_PADDING, -Board.BOARD_PADDING);
            Board.onClick(point);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
