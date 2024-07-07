package assignment;
import java.util.*;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    private PieceType type;
    private int rindex;
    //private Piece clockPiece;
    //private Piece counterclockPiece;
    private int width;
    private int height;
    private Point[] body;
    private int[] skirt;

    //private PieceType prev;
    private TetrisPiece next;
    //private boolean[][] block;
    private Color color;

    static class Node {
        Node next;
    }

    TetrisPiece clockwise;
    TetrisPiece counterclockwise;

    public TetrisPiece(PieceType type) {

        //creates all the different tetris rotations for each peice
        this(type, 0);
        TetrisPiece rotation1 = new TetrisPiece(type, 1);
        TetrisPiece rotation2 = new TetrisPiece(type, 2);
        TetrisPiece rotation3 = new TetrisPiece(type, 3);

        //clockwise "linked list"
        this.clockwise = rotation1;
        rotation1.clockwise = rotation2;
        rotation2.clockwise = rotation3;
        rotation3.clockwise = this;

        //counterclockwise "linked list"
        this.counterclockwise = rotation3;
        rotation1.counterclockwise = this;
        rotation2.counterclockwise = rotation1;
        rotation3.counterclockwise = rotation2;



        // TODO: Implement me.
        // PieceType piece = new PieceType(type);
        // set the enum??? 
        // TetrisPiece piece = new TetrisPiece(type);
        this.type = type;
        this.rindex = 0;
        this.body = type.getSpawnBody();
        this.width = type.getBoundingBox().width;
        this.height = type.getBoundingBox().height;
        this.color = type.getColor();
        
    }



    //need to fix- add max int
    //this constructor creates all the different rotations for the piece types
    public TetrisPiece(PieceType type, int rotation) {
        this.type = type;
        this.rindex = rotation;
        Point[] temp = null;
        //temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 1)};
        //this.type = type;
        //this.rindex = rotation;
        this.body = type.getSpawnBody();
        this.width = type.getBoundingBox().width;
        this.height = type.getBoundingBox().height;
        this.color = type.getColor();

        
        

        //rotations for T
        if (type.equals(PieceType.T)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 1)};
            else if (rindex == 1)
                temp = new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 1)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 1), new Point(1, 0), new Point(1, 1), new Point(2, 1)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(1, 0)};

            //Point[][] rotations = {t1, t2, t3, t4};
        }

        //rotations for SQUARE
        else if (type.equals(PieceType.SQUARE)) {
            temp = new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)};
        }

        //rotations for STICK
        else if (type.equals(PieceType.STICK)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2)};
            else if (rindex == 1)
                temp = new Point[] {new Point(2, 3), new Point(2, 2), new Point(2, 1), new Point(2, 0)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(1, 3), new Point(1, 2), new Point(1, 1), new Point(1, 0)};
        }

        //rotations for LEFT L
        else if (type.equals(PieceType.LEFT_L)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(2, 1)};
            else if (rindex == 1)
                temp = new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(0, 0)};
        }

        //rotations for RIGHT L
        else if (type.equals(PieceType.RIGHT_L)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2)};
            else if (rindex == 1)
                temp = new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(1, 0)};
        }

        //rotations for LEFT DOG
        else if (type.equals(PieceType.LEFT_DOG)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1)};
            else if (rindex == 1)
                temp = new Point[] {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(2, 0)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)};
        }

        //rotations for RIGHT DOG
        else if (type.equals(PieceType.RIGHT_DOG)) {
            if (rindex == 0)
                temp = new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2)};
            else if (rindex == 1)
                temp = new Point[] {new Point(1, 1), new Point(1, 2), new Point(2, 0), new Point(2, 1)};
            else if (rindex == 2)
                temp = new Point[] {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)};
            else if (rindex == 3) 
                temp = new Point[] {new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(1, 1)};
        }


        Point p;
        skirt = new int[width];
        for (int i = 0; i < skirt.length; i++){
            skirt[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                p = new Point(i, j);
                if (Arrays.asList(temp).contains(p)) {
                    if (p.y < skirt[p.x]) {
                        skirt[p.x] = p.y;
                    }
                }
            }
        }
        //next.setBody(temp);
        body = temp;
    }




    @Override
    public PieceType getType() {
        // TODO: Implement me.
        return type;
    }

    @Override
    public int getRotationIndex() {
        // TODO: Implement me.
        return rindex;
    }

    //help at office hours
    @Override
    public Piece clockwisePiece() {
        // TODO: Implement me.
        return clockwise;
    }

    //help at office hours
    @Override
    public Piece counterclockwisePiece() {
        // TODO: Implement me.
        return counterclockwise;
    }

    @Override
    public int getWidth() {
        // TODO: Implement me.
        return width;
    }

    @Override
    public int getHeight() {
        // TODO: Implement me.
        return height;
    }

    @Override
    public Point[] getBody() {
        // TODO: Implement me.
        return body;
    }

    @Override
    public int[] getSkirt() {
        // TODO: Implement me.
        return skirt;
    }


    //work on this??
    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;


        // TODO: Implement me.
        if (!otherPiece.getType().equals(this.getType()))
            return false;
        if (otherPiece.getRotationIndex() != this.getRotationIndex())
            return false;
        return true;
    }
}