package main.java;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by Rick on 07-Oct-18.
 */
class ImageLoader {

    /**
     * Image of the board
     */
    static Image boardImage;

    /**
     * Image overlays on the pieces if a piece is promoted to king
     */
    static Image kingImageBlack;
    static Image kingImageWhite;

    /**
     * Observer that fires when the load state of the image is changed.
     * Used for redrawing the screen when the image is loaded
     */
    static ImageObserver loadObserver;

    /**
     * Loads the images for the entire program
     */
    static void load(Component component) {
        boardImage = component.getToolkit().getImage(".\\src\\main\\res\\images\\board.png");
        kingImageBlack = component.getToolkit().getImage(".\\src\\main\\res\\images\\crown_black.png");
        kingImageWhite = component.getToolkit().getImage(".\\src\\main\\res\\images\\crown_white.png");

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
