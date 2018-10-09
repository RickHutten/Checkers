package main.java;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Rick on 07-Oct-18.
 */
class ImageLoader {

    /**
     * Image object and path of the board image
     */
    static Image boardImage;
    private static final Path boardPath = Paths.get("src", "main", "res", "images", "board.png");

    /**
     * Image object and path of the overlay on white pieces showing a black crown
     */
    static Image kingImageBlack;
    private static final Path blackKing = Paths.get("src", "main", "res", "images", "crown_black.png");

    /**
     * Image object and path of the overlay on black pieces showing a white crown
     */
    static Image kingImageWhite;
    private static final Path whiteKing = Paths.get("src", "main", "res", "images", "crown_white.png");

    /**
     * Observer that fires when the load state of the image is changed.
     * Used for redrawing the screen when the image is loaded
     */
    static ImageObserver loadObserver;

    /**
     * Loads the images for the entire program
     */
    static void load(Component component) {
        // Get images
        boardImage = component.getToolkit().getImage(boardPath.toString());
        kingImageBlack = component.getToolkit().getImage(blackKing.toString());
        kingImageWhite = component.getToolkit().getImage(whiteKing.toString());

        // Define the image observer
        loadObserver = (img, infoflags, x2, y2, width2, height2) -> {
            if ((infoflags & ImageObserver.ALLBITS) != 0) {
                // Redraw screen if the image has been loaded
                component.revalidate();
                component.repaint();
            }
            return true;
        };

        // Set the observer to the image
        component.prepareImage(boardImage, loadObserver);
        component.prepareImage(kingImageBlack, loadObserver);
        component.prepareImage(kingImageWhite, loadObserver);
    }
}
