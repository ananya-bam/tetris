package assignment;

import java.util.*;

import javax.sound.sampled.SourceDataLine;

import assignment.Piece.PieceType;

import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or
 * nulls). Supports
 * tetris pieces and row clearing. Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    private int width;
    private int height;
    PieceType[][] grid;

    private int[] boardRowWidth;
    private int[] boardColumnHeight;
    private int[] currentPieceSkirt;
    private int maxHeight;
    private Piece currentPiece;
    // private TetrisPiece currentPiece;
    private Point currentPiecePosition;
    private int dropHeight;
    private Action action;
    private Result result;
    private int rowsCleared;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Piece.PieceType[width][height];

        // this.columnHeight = new int [width];
        // this.rowWidth = new int [height];
        this.maxHeight = 0;
        this.action = null;
        this.result = null;
        this.boardColumnHeight = getBoardColumnHeight();
        this.boardRowWidth = getBoardRowWidth();
        this.rowsCleared =0;
        // this.rowWidth = null;
        // this.columnHeight = null;
    }

    public TetrisBoard(int width, int height, PieceType[][] grid, int[] boardRowWidth, int[] boardColumnHeight,
            int[] currentPieceSkirt, int maxHeight, Piece currentPiece, Point currentPiecePosition, int dropHeight,
            Action action, Result result) {
        this.width = width;
        this.height = height;
        this.grid = grid; //
        this.boardRowWidth = boardRowWidth;
        this.boardColumnHeight = boardColumnHeight;
        this.currentPieceSkirt = currentPieceSkirt; //
        this.maxHeight = maxHeight;
        this.currentPiece = currentPiece;
        this.currentPiecePosition = currentPiecePosition; //
        this.dropHeight = dropHeight;
        this.action = action;
        this.result = result;
    }

    @Override
    public Result move(Action act) {

        clearRows();

        // for (int i = 0; i < width; i++) {
        // if (getColumnHeight(i) > maxHeight)
        // maxHeight = getColumnHeight(i);
        // }

        // code for dropHeight
        dropHeight = currentPiecePosition.y;
        while (doesNotCollideDown(currentPiece, dropHeight)) {
            dropHeight--;
        }

        // use a switch case to change the position of the current peice
        // make sure to check if it is being moved out of bounds
        // write helper methods to get different values like the current peice's
        // position

        if (currentPiece == null) {
            result = Result.NO_PIECE;
            return Result.NO_PIECE;
        }
        switch (act) {
            case LEFT:
                int nextPiecePositionX = currentPiecePosition.x - 1;
                if (!isInLeftBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideLeft(currentPiece, nextPiecePositionX)) {
                    result = Result.OUT_BOUNDS;
                    action = Action.NOTHING;
                    return Result.OUT_BOUNDS;
                }
                currentPiecePosition.x--;
                result = Result.SUCCESS;
                action = Action.LEFT;
                return Result.SUCCESS;

            case RIGHT:
                nextPiecePositionX = currentPiecePosition.x + 1;
                if (!isInRightBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideRight(currentPiece, nextPiecePositionX)) {
                    result = Result.OUT_BOUNDS;
                    action = Action.NOTHING;
                    return Result.OUT_BOUNDS;
                }
                result = Result.SUCCESS;
                action = Action.RIGHT;
                currentPiecePosition.x++;
                return Result.SUCCESS;

            case DOWN:
                int nextPiecePositionY = currentPiecePosition.y - 1;

                if (!isInBottomBound(currentPiece, nextPiecePositionY)
                        || !doesNotCollideDown(currentPiece, currentPiecePosition.y)) {
                    // add current piece to game board
                    // create a helper method bc it is in down and drop
                    result = Result.PLACE;
                    action = Action.DOWN;
                    placePiece(currentPiece, currentPiecePosition.y);
                    return Result.PLACE;
                }
                currentPiecePosition.y--;
                // placePiece(currentPiece);
                result = Result.SUCCESS;
                action = Action.DOWN;
                return Result.SUCCESS;

            // use skirt to help with drop
            case DROP:
                placePiece(currentPiece, dropHeight(currentPiece, currentPiecePosition.x));
                action = Action.DROP;
                result = Result.PLACE;
                return Result.PLACE;

            case CLOCKWISE:
                // sets current piece equal to the new rotation
                currentPiece = currentPiece.clockwisePiece();
                currentPieceSkirt = currentPiece.getSkirt();

                int nextPiecePositionXLeft = currentPiecePosition.x - 1;
                int nextPiecePositionXRight = currentPiecePosition.x + 1;

                nextPiecePositionY = currentPiecePosition.y - 1;

                if (!isInLeftBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideLeft(currentPiece, nextPiecePositionXLeft)
                        || !isInRightBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideRight(currentPiece, nextPiecePositionXRight)
                        || !isInBottomBound(currentPiece, nextPiecePositionY)) {// || !doesNotCollideDown(currentPiece,
                                                                                // currentPiecePosition.y)){
                    CWWallKick();
                    result = Result.OUT_BOUNDS;
                    action = Action.NOTHING;
                    return Result.OUT_BOUNDS;
                }

                result = Result.SUCCESS;
                action = Action.CLOCKWISE;
                return Result.SUCCESS;

            case COUNTERCLOCKWISE:
                currentPiece = currentPiece.counterclockwisePiece();
                currentPieceSkirt = currentPiece.getSkirt();
                nextPiecePositionX = currentPiecePosition.x + 1;
                nextPiecePositionY = currentPiecePosition.y - 1;

                if (!isInLeftBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideLeft(currentPiece, nextPiecePositionX)
                        || !isInRightBound(currentPiece, currentPiecePosition)
                        || !doesNotCollideRight(currentPiece, nextPiecePositionX)) { // ||
                                                                                     // !isInBottomBound(currentPiece,
                                                                                     // nextPiecePositionY) ||
                                                                                     // !doesNotCollideDown(currentPiece,
                                                                                     // currentPiecePosition.y)){
                    CCWWallKick();
                    result = Result.OUT_BOUNDS;
                    action = Action.NOTHING;
                    return Result.OUT_BOUNDS;
                }

                action = Action.COUNTERCLOCKWISE;
                result = Result.SUCCESS;
                return Result.SUCCESS;

            case NOTHING:

                nextPiecePositionY = currentPiecePosition.y - 1;

                if (!isInBottomBound(currentPiece, nextPiecePositionY)) {
                    result = Result.NO_PIECE;
                    action = Action.NOTHING;
                    return Result.NO_PIECE;
                }
                result = Result.NO_PIECE;
                action = Action.NOTHING;
                return Result.NO_PIECE;

            case HOLD:
                action = Action.HOLD;

        }

        return Result.NO_PIECE;
    }

    private int[] getBoardColumnHeight() {
        int[] columnHeight = new int[width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j <height; j++) {
                //System.out.println(i);
                if (grid[i][j] != null) {
                    columnHeight[i] = j + 1;
                }
            }
        }
        return columnHeight;
    }

    private int[] getBoardRowWidth() {
        int[] rowWidth = new int[height];
        for (int i = 0; i < height; i++) {
            int setRowWidth = 0;
            for (int j = 0; j < width; j++) {
                if (grid[j][i] != null) {
                    setRowWidth = setRowWidth + 1;
                    rowWidth[i] = setRowWidth;
                }
            }
        }
        return rowWidth;
    }

    public Result CCWWallKick() {

        // check if the piece type is a STICK
        if (currentPiece.getType().equals(PieceType.STICK)) {
            // loops through the 5 different possible transformations
            for (int i = 0; i < 5; i++) {
                Point p = Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()][i];
                Point[] bodyPoints = currentPiece.getBody();
                Point[] updatedPoints = new Point[4];

                // loops through the 4 points on the body of the current piece to get the
                // hypothetical location of the piece on the grid
                for (int j = 0; j < 4; j++) {
                    updatedPoints[j] = new Point((int) bodyPoints[j].getX() + currentPiecePosition.x + (int) p.getX(),
                            (int) bodyPoints[j].getY() + currentPiecePosition.y + (int) p.getY());
                }

                boolean transform = true;
                for (int a = 0; a < 4; a++) {
                    if ((int) updatedPoints[a].getY() < 0) {
                        transform = false;
                        break;
                    } else if (updatedPoints[a].getX() < 0 || updatedPoints[a].getX() > width - 1) {
                        transform = false;
                        break;
                    } else if (grid[(int) updatedPoints[a].getX()][(int) updatedPoints[a].getY()] != null) {
                        transform = false;
                        break;
                    }

                }
                
                if (transform == true) {
                    currentPiecePosition.x += p.getX();
                    currentPiecePosition.y += p.getY();
                    return Result.SUCCESS;
                }

            }
        }
        // the piece type is NOT a stick
        else {
            for (int i = 0; i < 5; i++) {
                Point p = Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()][i];
                Point[] bodyPoints = currentPiece.getBody();
                Point[] updatedPoints = new Point[4];

                // loops through the 4 points on the body of the current piece to get the
                // hypothetical location of the piece on the grid
                for (int j = 0; j < 4; j++) {
                    updatedPoints[j] = new Point((int) bodyPoints[j].getX() + currentPiecePosition.x + (int) p.getX(),
                            (int) bodyPoints[j].getY() + currentPiecePosition.y + (int) p.getY());
                }

                // 10/3/23
                // check that the updated points are in bounds and not colliding (pieces don't
                // matter, just blocks)
                // check that the spots are open

                // loops through each of the 4 updatedPoints to see if it collides with
                // something
                // if it does not collide with anything on all three sides, then it means that
                // transformation works
                // if it works then you should break out of all the for loops

                boolean transform = true;
                for (int a = 0; a < 4; a++) {
                    if ((int) updatedPoints[a].getY() < 0) {
                        transform = false;
                        break;
                    } else if (updatedPoints[a].getX() < 0 || updatedPoints[a].getX() > width - 1) {
                        transform = false;
                        break;
                    } else if (grid[(int) updatedPoints[a].getX()][(int) updatedPoints[a].getY()] != null) {
                        transform = false;
                        break;
                    }

                }
                if (transform == true) {
                    currentPiecePosition.x += p.getX();
                    currentPiecePosition.y += p.getY();
                    return Result.SUCCESS;
                }
            }
        }

        return Result.NO_PIECE;
    }

    public Result CWWallKick() {
        // iterate through points in piece and check if they're colliding w anything/out
        // of bounds (should be if the piece is right on the edge of the grid)
        // if true; implement wall kick logic
        // check if stick bc that's a different array
        // based on which way we're turning, access the correct array and make a for
        // loop or while loop
        // to go through that array
        // while the piece

        // check if the piece type is a STICK
        if (currentPiece.getType().equals(PieceType.STICK)) {
            // loops through the 5 different possible transformations
            for (int i = 0; i < 5; i++) {
                Point p = Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()][i];
                Point[] bodyPoints = currentPiece.getBody();
                Point[] updatedPoints = new Point[4];

                // loops through the 4 points on the body of the current piece to get the
                // hypothetical location of the piece on the grid
                for (int j = 0; j < 4; j++) {
                    updatedPoints[j] = new Point((int) bodyPoints[j].getX() + currentPiecePosition.x + (int) p.getX(),
                            (int) bodyPoints[j].getY() + currentPiecePosition.y + (int) p.getY());
                }

                boolean transform = true;
                for (int a = 0; a < 4; a++) {
                    if ((int) updatedPoints[a].getY() < 0) {
                        transform = false;
                        break;
                    } else if (updatedPoints[a].getX() < 0 || updatedPoints[a].getX() > width - 1) {
                        transform = false;
                        break;
                    } else if (grid[(int) updatedPoints[a].getX()][(int) updatedPoints[a].getY()] != null) {
                        transform = false;
                        break;
                    }

                }
                if (transform == true) {
                    currentPiecePosition.x += p.getX();
                    currentPiecePosition.y += p.getY();
                    return Result.SUCCESS;
                }

            }
        }
        // the piece type is NOT a stick
        else {
            for (int i = 0; i < 5; i++) {
                Point p = Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()][i];
                Point[] bodyPoints = currentPiece.getBody();
                Point[] updatedPoints = new Point[4];

                // loops through the 4 points on the body of the current piece to get the
                // hypothetical location of the piece on the grid
                for (int j = 0; j < 4; j++) {
                    updatedPoints[j] = new Point((int) bodyPoints[j].getX() + currentPiecePosition.x + (int) p.getX(),
                            (int) bodyPoints[j].getY() + currentPiecePosition.y + (int) p.getY());
                }

                boolean transform = true;
                for (int a = 0; a < 4; a++) {
                    if ((int) updatedPoints[a].getY() < 0) {
                        transform = false;
                        break;
                    } else if (updatedPoints[a].getX() < 0 || updatedPoints[a].getX() > width - 1) {
                        transform = false;
                        break;
                    } else if (grid[(int) updatedPoints[a].getX()][(int) updatedPoints[a].getY()] != null) {
                        transform = false;
                        break;
                    }

                }
                if (transform == true) {
                    currentPiecePosition.x += p.getX();
                    currentPiecePosition.y += p.getY();
                    return Result.SUCCESS;
                }
            }
        }
        return Result.NO_PIECE;

    }

    private void placePiece(Piece piece, int yPos) {
        // create new grid and add piece coordinates to grid coordinates
        PieceType[][] updated = grid;
        Point[] bodyPoints = piece.getBody();
        Point[] updatedBodyPoints = new Point[4];
        for (int i = 0; i < 4; i++) {
            // System.out.println("bp.getx: " + (int) bodyPoints[i].getX());
            // System.out.println("currpp.x: " + currentPiecePosition.x);
            updatedBodyPoints[i] = new Point((int) bodyPoints[i].getX() + currentPiecePosition.x,
                    (int) bodyPoints[i].getY() + yPos);
        }

        for (int i = 0; i < 4; i++) {
            updated[(int) updatedBodyPoints[i].getX()][(int) updatedBodyPoints[i].getY()] = piece.getType();
            grid = updated;
        }

        grid = updated;

        boardColumnHeight= getBoardColumnHeight();
        boardRowWidth = getBoardRowWidth();

        maxHeight = 0;
        for (int i = 0; i < width; i++) {
            if (getColumnHeight(i) > maxHeight)
                maxHeight = getColumnHeight(i);
        }
    }

    private boolean doesNotCollideDown(Piece piece, int yPos) {
        Point[] updatedPoints = new Point[4];
        Point[] bodyPoints = piece.getBody();

        for (int i = 0; i < 4; i++) {
            updatedPoints[i] = new Point((int) bodyPoints[i].getX() + currentPiecePosition.x,
                    (int) bodyPoints[i].getY() + yPos);
        }

        for (int i = 0; i < 4; i++) {
            // System.out.println("testing" + (int)updatedPoints[i].getX());
            if ((int) updatedPoints[i].getY() == 0 || (int) updatedPoints[i].getX() < 0
                    || (int) updatedPoints[i].getX() > width - 1) {
                return false;
            } else if (grid[(int) updatedPoints[i].getX()][(int) updatedPoints[i].getY() - 1] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean doesNotCollideLeft(Piece piece, int nextXPos) {
        Point[] updatedPoints = new Point[4];
        Point[] bodyPoints = piece.getBody();

        for (int i = 0; i < 4; i++) {
            updatedPoints[i] = new Point((int) bodyPoints[i].getX() + nextXPos,
                    (int) bodyPoints[i].getY() + currentPiecePosition.y);
        }

        // this is what we need for other thing too
        for (int i = 0; i < 4; i++) {
            if ((int) updatedPoints[i].getY() == 0) {
                return false;
            } else if (updatedPoints[i].getX() < 0 || updatedPoints[i].getX() > width - 1) {
                return false;
            } else if (grid[(int) updatedPoints[i].getX()][(int) updatedPoints[i].getY()] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean doesNotCollideRight(Piece piece, int nextXPos) {
        Point[] updatedPoints = new Point[4];
        Point[] bodyPoints = piece.getBody();

        for (int i = 0; i < 4; i++) {
            updatedPoints[i] = new Point((int) bodyPoints[i].getX() + nextXPos,
                    (int) bodyPoints[i].getY() + currentPiecePosition.y);
        }

        for (int i = 0; i < 4; i++) {
            if ((int) updatedPoints[i].getY() == 0) {
                return false;
            } else if (updatedPoints[i].getX() > width - 1) {
                return false;
            } else if (grid[(int) updatedPoints[i].getX()][(int) updatedPoints[i].getY()] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean isInLeftBound(Piece piece, Point p) {
        int pieceWidth = piece.getWidth();

        int[] sk = currentPieceSkirt;

        for (int i = 0; i < sk.length; i++) {
            if (sk[i] == Integer.MAX_VALUE)
                pieceWidth--;
        }

        int location = p.x;
        boolean firstNotFound = true;

        for (int i = 0; i < sk.length; i++) {
            if (sk[i] == Integer.MAX_VALUE)
                location++;
            else {
                firstNotFound = false;
                break;
            }
        }

        if (p.y < 0) {
            return false;
        } else if (location - 1 < 0 || grid[location - 1][p.y] != null)
            return false;
        return true;
    }

    private boolean isInRightBound(Piece piece, Point p) {
        int pieceWidth = piece.getWidth();
        int[] sk = piece.getSkirt();
        for (int i = 0; i < sk.length; i++) {
            if (sk[i] == Integer.MAX_VALUE)
                pieceWidth--;
        }

        int location = p.x;
        boolean firstNotFound = true;


        for (int i = 0; i < sk.length; i++) {
            if (sk[i] == Integer.MAX_VALUE)
                location++;
            else {
                firstNotFound = false;
                break;
            }
        }

        if (p.y < 0) {
            return false;
        }
        if (location + pieceWidth > width - 1 || grid[location + pieceWidth][p.y] != null) {
            return false;
        }
        return true;
    }

    private boolean isInBottomBound(Piece piece, int nextPosY) {
    

        int min = piece.getHeight() - 1;
        int[] sk = piece.getSkirt();

        for (int i = 0; i < sk.length; i++) {
            if (sk[i] < min)
                min = sk[i];
        }

        if (nextPosY + min + 1 < 0)
            return false;
        return true;
    }

    // method for testing
    @Override
    public Board testMove(Action act) {


        PieceType[][] gridCopy = new PieceType[width][height];
        for (int i = 0; i < gridCopy.length; i++) {
            gridCopy[i] = Arrays.copyOf(grid[i], gridCopy[i].length);
        }

        // deep copy of array
        int[] boardRowWidthCopy = Arrays.copyOf(boardRowWidth, boardRowWidth.length);
        // repeat for other int arrrays and Piece
        int[] boardColumnHeightCopy = Arrays.copyOf(boardColumnHeight, boardColumnHeight.length);
        int[] currentPieceSkirtCopy = Arrays.copyOf(currentPieceSkirt, currentPieceSkirt.length);
        //Piece currentPieceCopy = new TetrisPiece(currentPiece.getType(), currentPiece.getRotationIndex());
        Point currentPiecePositionCopy = new Point(currentPiecePosition.x, currentPiecePosition.y);
        

        Board newBoard = new TetrisBoard(width, height, gridCopy, boardRowWidthCopy, boardColumnHeightCopy, currentPieceSkirtCopy,
                maxHeight, currentPiece, currentPiecePositionCopy, dropHeight, act, result);
        newBoard.move(act);
        return newBoard;
    }

    @Override
    public Piece getCurrentPiece() {
        return currentPiece;
    }

    @Override
    public Point getCurrentPiecePosition() {
        return currentPiecePosition;
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        // check for edge cases like to make sure the piece does not spawn outside of the grid

        currentPiece = p;
        currentPieceSkirt = p.getSkirt();
        currentPiecePosition = spawnPosition;
    }

    // use to compare the tetris boards for test
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TetrisBoard))
            return false;
        TetrisBoard otherBoard = (TetrisBoard) other;
        
        if (!otherBoard.getCurrentPiece().equals(this.getCurrentPiece()))
            return false;
        if (otherBoard.getCurrentPiecePosition() != this.getCurrentPiecePosition())
            return false;
        if (otherBoard.getHeight() != this.getHeight())
            return false;
        if (otherBoard.getWidth() != this.getWidth())
            return false;
        return true;

    }

    @Override
    public Result getLastResult() {

        return result;
    }

    @Override
    public Action getLastAction() {
        return action;
    }

    public void clearRows() {
        int startClearing = 0;
        for (int i = 0; i < height - 1; i++) {

            if (getRowWidth(i) == width) {
                rowsCleared++;
                for (int j = 0; j < width; j++) {
                    grid[j][i] = null;
                }
                startClearing = i;

                while (startClearing < height - 1) {
                    for (int j = 0; j < width; j++) {
                        grid[j][startClearing] = grid[j][startClearing + 1];
                    }

                    startClearing++;
                }

                break;
            }
        }

        boardColumnHeight= getBoardColumnHeight();
        boardRowWidth = getBoardRowWidth();

        maxHeight = 0;
        for (int i = 0; i < width; i++) {
            if (getColumnHeight(i) > maxHeight)
                maxHeight = getColumnHeight(i);
        }
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        // System.out.println("dropHeight: " + dropHeight);
        return dropHeight;
    }

    @Override
    public int getColumnHeight(int x) {
        return boardColumnHeight[x];
    }

    @Override
    // change this later to be in constant time
    public int getRowWidth(int y) {
        return boardRowWidth[y];
    }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        if(x<0 || x> width-1)
            return null;

        if(y <0 || y> height-1)
            return null;

        if (grid[x][y] != null) {
            return grid[x][y];
        } else {
            return null;
        }
    }

}
