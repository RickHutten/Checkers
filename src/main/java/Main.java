package main.java;

import java.awt.*;

/**
 * Created by Rick on 07-Oct-18.
 */

public class Main {

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Create a new window
            Window window = new Window();

            // Display the main menu screen
            window.add(new StartScreen(window), BorderLayout.CENTER);
        });
    }
}
