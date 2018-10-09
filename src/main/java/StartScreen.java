package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Rick on 07-Oct-18.
 */

class StartScreen extends JComponent implements MouseListener {

    /**
     * Window object
     */
    private final Window mWindow;

    /**
     * Rectangle (bounds) of the button that starts the game
     */
    private final Rectangle mStartButton =
            new Rectangle(Window.WIDTH / 2 - 100, Window.HEIGHT / 2 - 25, 200, 50);

    StartScreen(Window window) {
        this.mWindow = window;
        this.addMouseListener(this);
    }

    /**
     * Draw the main menu
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

        // Draw background image with alpha
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        Board.draw(g);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Draw start button
        graphics.setFont(new Font(null, Font.PLAIN, 30));
        Window.drawButton(g, "Start game", mStartButton);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mStartButton.contains(e.getPoint())) {
            // Clicked on the start button

            // Start the game
            mWindow.remove(this);
            mWindow.add(new GameScreen(mWindow), BorderLayout.CENTER);
            mWindow.revalidate();
            mWindow.repaint();
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
