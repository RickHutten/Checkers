package main.java;

import java.awt.*;

/**
 * Created by Rick on 07-Oct-18.
 */

public class Main {

    public static void main(String[] args) {
        // Create a new window
        Window window = new Window();

        // Display the main menu screen
        window.add(new StartScreen(window), BorderLayout.CENTER);
    }
}
