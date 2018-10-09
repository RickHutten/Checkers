package main.java;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Rick on 07-Oct-18.
 */

class Window extends JFrame {

    /**
     * These fields determine the width and height of the window
     */
    static final int WIDTH = 800;
    static final int HEIGHT = 800;

    /**
     * Constructs a new window and pre-loads the images
     */
    Window() {
        // Set window properties
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Checkers");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

        // Load the images
        ImageLoader.load(this);
    }

    /**
     * Draws a string centered around the given point
     */
    static void drawCenteredString(Graphics g, String text, int x, int y) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        // Determine the X coordinate for the text
        x -= metrics.stringWidth(text) / 2;
        y -= (metrics.getHeight() / 2) - metrics.getAscent();
        // Draw the String
        g.drawString(text, x, y);
    }

    /**
     * Draws a button at the given center point
     */
    static void drawButton(Graphics g, String text, Rectangle rect) {
        // Save current color
        Color currentColor = g.getColor();

        // Draw box outline and fill with white
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(Color.white);
        g.fillRect(rect.x + 1, rect.y + 1, rect.width - 1, rect.height - 1);

        // Set color back to current color
        g.setColor(currentColor);

        // Draw text
        drawCenteredString(g, text, rect.x + rect.width / 2, rect.y + rect.height / 2);
    }
}
