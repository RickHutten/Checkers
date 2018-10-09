package main.java;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created by Rick on 07-Oct-18.
 */

class Board {

    /**
     * Padding of the board in pixels to the window frame
     */
    static final int BOARD_PADDING = 100;

    /**
     * Size of the board in pixels
     */
    static final int BOARD_SIZE = Math.min(Window.WIDTH, Window.HEIGHT) - 2 * BOARD_PADDING;

    /**
     * Size of one individual box (square) on the board
     */
    private static final int BOARD_BOX_SIZE = BOARD_SIZE / 8;

    /**
     * Padding for the piece (circle) in reference to the box
     */
    private static final int PIECE_PADDING = (int) (BOARD_BOX_SIZE * 0.1);

    /**
     * Listener that will fire when the game is finished
     */
    private static OnGameFinishedListener sGameFinishedListener = null;

    /**
     * Listener that is fired when the state of the board has changed
     * and needs to be redrawn
     */
    private static OnStateChangedListener sStateChangedListener = null;

    /**
     * The board represented as 2d-array of integers of size 8x8, int[y][x].
     * The values are represented by the integers from class BoxState.
     * int[0][0] is top left corner, int[7][0] bottom left corner,
     * int[7][7] bottom right corner, int[0][7] top right corner
     */
    private static final int[][] sState = new int[8][8];

    /**
     * Coordinate on the board of the piece that is selected.
     * If no piece is selected, the value is null
     */
    private static Point sSelectedPiece = null;

    /**
     * List of possible move coordinates of the selected piece.
     * If no piece is selected, the value is null
     */
    private static ArrayList<Point> sPossibleMoves = null;

    /**
     * The player who's turn it currently is
     */
    private static Player sActivePlayer = Player.BLACK;

    /**
     * List of pieces that have capturing moves.
     * These pieces have to be played (eg. mandatory)
     */
    private static ArrayList<Point> sMandatoryPieces = new ArrayList<>();

    /**
     * Field which describes if the player can
     */
    private static boolean sIsMultipleMove = false;

    /**
     * The two players
     */
    enum Player {
        WHITE,
        BLACK
    }

    /**
     * The values representing the state of the individual boxes in
     * state variable sState
     */
    private final static class BoxState {
        private final static int EMPTY = 0;
        private final static int WHITE = 1;
        private final static int BLACK = 2;
        private final static int WHITE_KING = 3;
        private final static int BLACK_KING = 4;
    }

    /**
     * Directions that the pieces can move to
     */
    private final static class Direction {
        private final static Point UP_LEFT = new Point(-1, -1);
        private final static Point UP_RIGHT = new Point(1, -1);
        private final static Point DOWN_LEFT = new Point(-1, 1);
        private final static Point DOWN_RIGHT = new Point(1, 1);
        private final static Point[] AllDirections = {
                Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT
        };
    }

    /**
     * Draws the board including pieces and highlights
     */
    static void draw(Graphics graphics) {
        // Draw the board image
        drawBoard(graphics);

        // Highlight mandatory pieces (pieces that can make a capture)
        drawMandatoryPieces(graphics);

        // Highlight selected piece
        drawSelection(graphics);

        // Highlight the possible moves
        drawPossibleMoves(graphics);

        // Draw the pieces
        drawPieces(graphics);
    }

    /**
     * Draw board background image
     */
    static void drawBoard(Graphics graphics) {
        graphics.drawImage(ImageLoader.boardImage, BOARD_PADDING, BOARD_PADDING,
                BOARD_SIZE, BOARD_SIZE, ImageLoader.loadObserver);
    }

    /**
     * Draw the pieces that are on the board
     */
    private static void drawPieces(Graphics graphics) {
        Color drawColor;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                // Determine color
                switch (sState[y][x]) {
                    case BoxState.WHITE:
                    case BoxState.WHITE_KING:
                        drawColor = Color.WHITE;
                        break;
                    case BoxState.BLACK:
                    case BoxState.BLACK_KING:
                        drawColor = new Color(32, 32, 32);
                        break;
                    default:
                        // Box is empty, draw nothing
                        continue;
                }

                // Draw shadow if not the selected piece at 2 pixels offset
                Point point = new Point(x, y);
                if (!(point.equals(sSelectedPiece) || sMandatoryPieces.contains(point))) {
                    graphics.setColor(Color.BLACK);
                    graphics.fillOval(
                            x * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING + 2,
                            y * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING + 2,
                            BOARD_BOX_SIZE - 2 * PIECE_PADDING,
                            BOARD_BOX_SIZE - 2 * PIECE_PADDING
                    );
                }

                // Draw piece
                graphics.setColor(drawColor);
                graphics.fillOval(
                        x * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING,
                        y * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING,
                        BOARD_BOX_SIZE - 2 * PIECE_PADDING,
                        BOARD_BOX_SIZE - 2 * PIECE_PADDING
                );

                // Draw king image
                if (sState[y][x] == BoxState.WHITE_KING) {
                    // Black image on white piece
                    graphics.drawImage(
                            ImageLoader.kingImageBlack,
                            x * BOARD_BOX_SIZE + BOARD_PADDING + 3 * PIECE_PADDING,
                            y * BOARD_BOX_SIZE + BOARD_PADDING + 3 * PIECE_PADDING,
                            BOARD_BOX_SIZE - 6 * PIECE_PADDING,
                            BOARD_BOX_SIZE - 6 * PIECE_PADDING,
                            ImageLoader.loadObserver
                    );
                } else if (sState[y][x] == BoxState.BLACK_KING) {
                    // White image on black piece
                    graphics.drawImage(
                            ImageLoader.kingImageWhite,
                            x * BOARD_BOX_SIZE + BOARD_PADDING + 3 * PIECE_PADDING,
                            y * BOARD_BOX_SIZE + BOARD_PADDING + 3 * PIECE_PADDING,
                            BOARD_BOX_SIZE - 6 * PIECE_PADDING,
                            BOARD_BOX_SIZE - 6 * PIECE_PADDING,
                            ImageLoader.loadObserver
                    );
                }
            }
        }
    }

    /**
     * Highlights the mandatory pieces
     */
    private static void drawMandatoryPieces(Graphics graphics) {
        for (Point point : sMandatoryPieces) {
            graphics.setColor(Color.GREEN);
            graphics.fillOval(
                    point.x * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING / 2,
                    point.y * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING / 2,
                    BOARD_BOX_SIZE - PIECE_PADDING,
                    BOARD_BOX_SIZE - PIECE_PADDING
            );
        }
    }

    /**
     * Highlights the selected piece if a piece is selected
     */
    private static void drawSelection(Graphics graphics) {
        // If no piece is selected, don't draw
        if (sSelectedPiece == null) return;

        // Draw selected piece
        graphics.setColor(Color.YELLOW);
        graphics.fillOval(
                sSelectedPiece.x * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING / 2,
                sSelectedPiece.y * BOARD_BOX_SIZE + BOARD_PADDING + PIECE_PADDING / 2,
                BOARD_BOX_SIZE - PIECE_PADDING,
                BOARD_BOX_SIZE - PIECE_PADDING
        );
    }

    /**
     * Draw the possible moves for the selected piece if a piece is selected
     */
    private static void drawPossibleMoves(Graphics graphics) {
        // If no piece is selected, don't draw
        if (sSelectedPiece == null) return;

        // Cast Graphics to Graphics2D to set stroke width
        Graphics2D graphics2D = (Graphics2D) graphics;

        // Save default stroke width
        Stroke defaultStroke = graphics2D.getStroke();

        // Draw the moves
        for (Point move : sPossibleMoves) {
            graphics2D.setColor(Color.YELLOW);
            graphics2D.setStroke(new BasicStroke(5));
            graphics2D.drawRect(
                    move.x * BOARD_BOX_SIZE + BOARD_PADDING,
                    move.y * BOARD_BOX_SIZE + BOARD_PADDING,
                    BOARD_BOX_SIZE,
                    BOARD_BOX_SIZE);
        }

        // Restore default stroke width
        graphics2D.setStroke(defaultStroke);
    }

    /**
     * Gets the moves possible for the given point. Assumes the point is a valid piece.
     */
    private static ArrayList<Point> getMoves(Point point) {
        // Checks whether the piece is a king
        boolean isKing = sState[point.y][point.x] == BoxState.WHITE_KING ||
                sState[point.y][point.x] == BoxState.BLACK_KING;

        ArrayList<Point> optionalMoves = new ArrayList<>();
        ArrayList<Point> mandatoryMoves = new ArrayList<>();
        Point move;
        Point captureMove;
        for (Point direction : Direction.AllDirections) {
            if (isKing) {
                // If the piece is a king, different rules apply

                // Whether the path is behind an opposite piece (that makes it mandatory)
                boolean behindOppositePiece = false;

                // Calculate new move
                move = new Point(point.x + direction.x, point.y + direction.y);

                // Kings can go as far as the board allows
                while (!isOutsideBoard(move)) {
                    // If the space is empty, it is allowed
                    if (sState[move.y][move.x] == BoxState.EMPTY) {
                        if (behindOppositePiece) {
                            // If we're behind an opposite piece, its mandatory
                            mandatoryMoves.add(move);
                            sMandatoryPieces.add(point);
                        } else {
                            // If we're not behind an opposite piece, its optional
                            optionalMoves.add(move);
                        }
                    } else if (isPlayerPiece(sActivePlayer, move)) {
                        // Can't continue in this direction, can't jump over own pieces
                        break;
                    } else if (isOppositePlayerPiece(sActivePlayer, move)) {
                        // From now on, empty spaces are behind an opposite player piece
                        // and therefore are mandatory
                        behindOppositePiece = true;
                    }
                    // Go one step further in the same direction
                    move = new Point(move.x + direction.x, move.y + direction.y);
                }
            } else {
                // If the piece is not a king, you can only take one step
                // Calculate new point for the move
                move = new Point(point.x + direction.x, point.y + direction.y);

                // If move is not outside board, can't go that direction
                if (isOutsideBoard(move)) {
                    continue;
                }

                // Check the validity of the move
                if (sState[move.y][move.x] == BoxState.EMPTY) {
                    // Check if the move is in a valid direction
                    if (sActivePlayer == Player.BLACK && direction.y == -1) {
                        // Black player can only move in -y direction
                        optionalMoves.add(move);
                    } else if (sActivePlayer == Player.WHITE && direction.y == 1) {
                        // White player can only move in +y direction
                        optionalMoves.add(move);
                    }
                } else if (isOppositePlayerPiece(sActivePlayer, move)) {
                    // Box is occupied with other player, check if we can capture it
                    // Move one step further
                    captureMove = new Point(move.x + direction.x, move.y + direction.y);

                    // If move is not outside board, can't go that direction
                    if (isOutsideBoard(captureMove)) {
                        continue;
                    }

                    // If next move is empty, move is valid and mandatory
                    if (sState[captureMove.y][captureMove.x] == BoxState.EMPTY) {
                        // Move is valid, can capture piece
                        mandatoryMoves.add(captureMove);
                        sMandatoryPieces.add(point);
                    }
                }
            }
        }
        // Return mandatory moves if present, otherwise return optional moves
        return mandatoryMoves.size() == 0 ? optionalMoves : mandatoryMoves;
    }

    /**
     * Sets the state of the board in the starting position
     */
    static void resetBoard() {
        // Loop through all positions
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((x + y) % 2 == 0) {
                    sState[y][x] = BoxState.EMPTY; // All white boxes on the board
                } else if (y <= 2) {
                    sState[y][x] = BoxState.WHITE; // Bottom three rows are white
                } else if (y >= 5) {
                    sState[y][x] = BoxState.BLACK; // Top three rows are black
                } else {
                    sState[y][x] = BoxState.EMPTY; // Middle two rows are empty
                }
            }
        }
        // Set active player
        sActivePlayer = Player.BLACK;

        // Clear active piece and possible moves
        sSelectedPiece = null;
        sPossibleMoves = null;
        sMandatoryPieces = new ArrayList<>();
        sIsMultipleMove = false;
    }

    /**
     * Called when clicked inside the board.
     * Point is in reference to the top-left board corner, not the screen
     */
    static void onClick(Point point) {
        // Get the position of the clicked box
        int x = 8 * point.x / BOARD_SIZE;
        int y = 8 * point.y / BOARD_SIZE;
        Point clickedPoint = new Point(x, y);

        if (sSelectedPiece == null) {
            // Check if the piece is the same color as the current player
            if (isPlayerPiece(sActivePlayer, clickedPoint) &&
                    (sMandatoryPieces.size() == 0 || sMandatoryPieces.contains(clickedPoint))) {
                // Select the piece
                sSelectedPiece = clickedPoint;
                sPossibleMoves = getMoves(sSelectedPiece);
            }
        } else {
            // If a piece is already selected
            if (sSelectedPiece == clickedPoint && !sIsMultipleMove) {
                // Clicked on the same piece, unselect piece. Can't unselect in continued turn
                sSelectedPiece = null;
                sPossibleMoves = null;
            } else {
                // Check if clicked on a possible move
                if (sPossibleMoves.contains(clickedPoint)) {
                    // Move is made, move the piece
                    onMove(clickedPoint);
                } else if (!sIsMultipleMove) { // Can't unselect in continued turn
                    // Clicked on something else
                    if (isPlayerPiece(sActivePlayer, clickedPoint)
                            && sMandatoryPieces.size() == 0) {
                        // Clicked on other piece, select that piece
                        sSelectedPiece = clickedPoint;
                        sPossibleMoves = getMoves(sSelectedPiece);
                    } else {
                        // Clicked on something else, unselect
                        sSelectedPiece = null;
                        sPossibleMoves = null;
                    }
                }
            }
        }
        // Let the game screen know the state has changed
        sStateChangedListener.onBoardStateChanged();
    }

    /**
     * Called when a piece is moved. Moves the piece and removes captured pieces
     */
    private static void onMove(Point moveToPoint) {
        // Check if a piece is captured
        ArrayList<Point> capturedPieces = getCapturedPieces(sSelectedPiece, moveToPoint);

        // Set the next position of the piece, check if the piece promotes to king
        if (sState[sSelectedPiece.y][sSelectedPiece.x] == BoxState.WHITE
                && moveToPoint.y == 7) {
            // Piece is white and position is the back of the board, change piece to king
            sState[moveToPoint.y][moveToPoint.x] = BoxState.WHITE_KING;
        } else if (sState[sSelectedPiece.y][sSelectedPiece.x] == BoxState.BLACK
                && moveToPoint.y == 0) {
            // Piece is black and position is the back of the board, change piece to king
            sState[moveToPoint.y][moveToPoint.x] = BoxState.BLACK_KING;
        } else {
            // Keep piece type
            sState[moveToPoint.y][moveToPoint.x] = sState[sSelectedPiece.y][sSelectedPiece.x];
        }

        // Set old position to empty
        sState[sSelectedPiece.y][sSelectedPiece.x] = BoxState.EMPTY;

        // Check if we captured a piece
        if (capturedPieces.size() == 0) {
            // If we didn't capture a piece
            endTurn();
            return;
        } else {
            // If we did capture a piece

            // Remove the captured pieces
            for (Point piece : capturedPieces) {
                // Set the box to empty
                sState[piece.y][piece.x] = BoxState.EMPTY;
            }
            // Calculate next moves to see if we can capture more pieces
            sSelectedPiece = moveToPoint;

            // Clear the mandatory list and check if the point has further mandatory moves
            sMandatoryPieces = new ArrayList<>();
            sPossibleMoves = getMoves(sSelectedPiece);

            if (sMandatoryPieces.size() == 0) {
                // If we can't capture any more pieces, end turn
                endTurn();
                return;
            }
        }
        // If the turn is not ended, set sIsMultipleMove to true to prevent unselecting the piece
        sIsMultipleMove = true;
        sMandatoryPieces = new ArrayList<>();
    }

    /**
     * Checks whether given point is inside or outside the board => true if outside
     */
    private static boolean isOutsideBoard(Point point) {
        return (point.x < 0 || point.x > 7 || point.y < 0 || point.y > 7);
    }

    /**
     * Check if a piece is captured by the given move
     */
    private static ArrayList<Point> getCapturedPieces(Point origin, Point destination) {
        // Calculate difference between the points
        int stepSize = Math.abs(destination.x - origin.x);
        if (stepSize < 2) return new ArrayList<>(); // Only one step was made, no piece is captured

        // Get the direction of the step
        int xDirection = (int) Math.signum(destination.x - origin.x);
        int yDirection = (int) Math.signum(destination.y - origin.y);

        // Initialize the list to be filled with captured pieces
        ArrayList<Point> capturedPieces = new ArrayList<>();
        // Loop through steps made/intermediate points (multiple if king)
        for (int step = 1; step < stepSize; step++) {
            // Get the intermediate point
            Point point = new Point(origin.x + step * xDirection, origin.y + step * yDirection);
            // Check if point is empty of taken
            if (sState[point.y][point.x] != BoxState.EMPTY) {
                // Point contains a piece, add it to the list
                capturedPieces.add(point);
            }
        }
        return capturedPieces;
    }

    /**
     * Ends the turn and switches player, checks if the game is won
     */
    private static void endTurn() {
        // Switch player
        sActivePlayer = sActivePlayer == Player.BLACK ? Player.WHITE : Player.BLACK;

        // Check if the game is won
        Player winner = getWinningPlayer();
        if (winner != null) {
            // The game is finished,
            sGameFinishedListener.onGameFinished(winner);
            return;
        }

        // Unselect piece
        sSelectedPiece = null;
        sPossibleMoves = null;

        // Reset multiple move
        sIsMultipleMove = false;

        // Check for the next player if he has to make a capture
        getPlayerMandatoryPieces(sActivePlayer);
    }

    /**
     * Checks if the board is won at the current state.
     * First, it counts how many pieces are left on the board for both players.
     * If one player has zero pieces left, return the other player since he has won.
     * Secondly, it checks if both players have moves, if not, the other player wins
     * If no player has won, return null
     */
    private static Player getWinningPlayer() {
        int remainingWhitePieces = 0;
        int remainingBlackPieces = 0;
        int possibleWhiteMoves = 0;
        int possibleBlackMoves = 0;

        // Count number of remaining pieces
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (sState[y][x] == BoxState.WHITE || sState[y][x] == BoxState.WHITE_KING) {
                    remainingWhitePieces++;
                    possibleWhiteMoves += getMoves(new Point(x, y)).size();
                } else if (sState[y][x] == BoxState.BLACK || sState[y][x] == BoxState.BLACK_KING) {
                    remainingBlackPieces++;
                    possibleBlackMoves += getMoves(new Point(x, y)).size();
                }
            }
        }
        if (remainingWhitePieces == 0 || possibleWhiteMoves == 0) return Player.BLACK;
        if (remainingBlackPieces == 0 || possibleBlackMoves == 0) return Player.WHITE;
        return null;
    }

    /**
     * Gets the pieces of the given player that can capture a piece
     * and therefore will have to be played
     */
    private static void getPlayerMandatoryPieces(Player player) {
        sMandatoryPieces = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Point point = new Point(x, y);
                if (isPlayerPiece(player, point)) {
                    getMoves(point);
                }
            }
        }
    }

    /**
     * Returns whether the given piece is of the player
     */
    private static boolean isPlayerPiece(Player player, Point piece) {
        int state = sState[piece.y][piece.x];
        return (state == BoxState.WHITE || state == BoxState.WHITE_KING)
                && player == Player.WHITE ||
                (state == BoxState.BLACK || state == BoxState.BLACK_KING)
                        && player == Player.BLACK;
    }

    /**
     * Returns whether the given piece is of the opposite player
     */
    private static boolean isOppositePlayerPiece(Player player, Point piece) {
        int state = sState[piece.y][piece.x];
        return (state == BoxState.WHITE || state == BoxState.WHITE_KING)
                && player == Player.BLACK ||
                (state == BoxState.BLACK || state == BoxState.BLACK_KING)
                        && player == Player.WHITE;
    }

    /**
     * Adds listener that fires when the game ends
     */
    static void setOnGameFinishedListener(OnGameFinishedListener onGameFinishedListener) {
        sGameFinishedListener = onGameFinishedListener;
    }

    /**
     * Adds listener that fires when the state changes and the screen needs to be updated
     */
    static void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        sStateChangedListener = onStateChangedListener;
    }

    /**
     * Returns the active player
     */
    static Player getActivePlayer() {
        return sActivePlayer;
    }
}
