package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

/**
 * Created by Rick on 07-Oct-18.
 */

class FinishedScreen extends JComponent implements MouseListener {

    /**
     * Window object
     */
    private final Window mWindow;

    /**
     * The winner to be shown on the screen
     */
    private final Board.Player mWinner;

    private final Rectangle mStartButton =
            new Rectangle(Window.WIDTH / 2 - 100, Window.HEIGHT / 2 - 50, 200, 50);
    private final Rectangle mEndButton =
            new Rectangle(Window.WIDTH / 2 - 100, Window.HEIGHT / 2 + 50, 200, 50);

    FinishedScreen(Window window, Board.Player winner) {
        this.mWindow = window;
        this.addMouseListener(this);
        this.mWinner = winner;
    }

    /**
     * Draw the finished screen
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
        String player = mWinner == Board.Player.BLACK ? "black" : "white";
        Window.drawCenteredString(g, "Congratulations " + player + " player", Window.WIDTH / 2, 50);

        // Draw background image with alpha
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        Board.drawBoard(g);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Draw button
        graphics.setFont(new Font(null, Font.PLAIN, 30));
        Window.drawButton(g, "Restart game", mStartButton);
        Window.drawButton(g, "Close game", mEndButton);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mStartButton.contains(e.getPoint())) {
            // Clicked on the start button

            // Reset board
            Board.resetBoard();

            // Show game mWindow
            mWindow.remove(this);
            mWindow.add(new GameScreen(mWindow), BorderLayout.CENTER);
            mWindow.revalidate();
            mWindow.repaint();

        } else if (mEndButton.contains(e.getPoint())) {
            // Clicked on the close button, close the game
            mWindow.dispatchEvent(new WindowEvent(mWindow, WindowEvent.WINDOW_CLOSING));
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
