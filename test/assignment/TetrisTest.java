package assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Point;
import java.util.ArrayList;
import assignment.Piece.PieceType;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

/*
 * Create a temp board that you manipulate
 * assert equals that the actions you do produce the expected result
 * 
 * test piece: perform the rotation on a piece and see if the rotation of that piece 
 * is the expected rotation new point array and assign the x and y values
 * 
 * 
 */


public class TetrisTest implements Board {

    PieceType pieceType;
    ArrayList<int[]> skirtArray;
    Point[] body;
    int height;
    int width;
    ArrayList<Point[]> rotationArray;
    int rotationIndex;

    TetrisPiece t = new TetrisPiece(PieceType.T);
    TetrisPiece square =  new TetrisPiece(PieceType.SQUARE);
    TetrisPiece stick =  new TetrisPiece(PieceType.STICK);
    TetrisPiece left_l =  new TetrisPiece(PieceType.LEFT_L);
    TetrisPiece right_l =  new TetrisPiece(PieceType.RIGHT_L);
    TetrisPiece left_dog =  new TetrisPiece(PieceType.LEFT_DOG);
    TetrisPiece right_dog =  new TetrisPiece(PieceType.RIGHT_DOG);

    BigBrain brain = new BigBrain();



    // This will run ONCE before all other tests. It can be useful to setup up
    // global variables and anything needed for all of the tests.

    @BeforeAll
    static void setupAll() {

    }

    // This will run before EACH test.
    @BeforeEach
    void setupEach() {
    }

    // You can test execute critter here. You may want to make additional tests and
    // your own testing harness. See spec section 2.5 for more details.
    @Test
    void testTetrisPiece() {

    }

    // Test load species. You may want to make more tests for different cases here.
    @Test
    void testTetrisBoard() {

    }

    public TetrisBoard tempBoard(){
        int width =10;
        int height =20;
        PieceType[][] currentGrid = new Piece.PieceType[width][height];
        TetrisPiece currentPiece = new TetrisPiece(PieceType.T);
        Point startingPiecePosition = new Point(width/2 - currentPiece.getWidth()/2, height);
        int rowsCleared =0;
        Result previousResult = Result.NO_PIECE;
        Action previousAction = Action.NOTHING;
        int[] rowWidth = new int[height];
        int[] columnHeight = new int[width];
        int maxHeight =0;

        TetrisBoard testBoard = new TetrisBoard(width, height);
        return testBoard;
    }

    @Test
    void testClockwise(){
        assertArrayEquals(new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 1)}, 
        t.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}, 
        square.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(2, 3), new Point(2, 2), new Point(2, 1), new Point(2, 0)}, 
        stick.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2)}, 
        left_l.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0)}, 
        right_l.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2)}, 
        left_dog.clockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 1), new Point(1, 2), new Point(2, 0), new Point(2, 1)}, 
        right_dog.clockwisePiece().getBody());
    }

    @Test
    void testCounterclockwise(){
        assertArrayEquals(new Point[] {new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(1, 0)}, 
        t.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1)}, 
        square.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 3), new Point(1, 2), new Point(1, 1), new Point(1, 0)}, 
        stick.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(1, 2), new Point(1, 1), new Point(1, 0), new Point(0, 0)}, 
        left_l.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(1, 0)}, 
        right_l.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2)}, 
        left_dog.counterclockwisePiece().getBody());
        assertArrayEquals(new Point[] {new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(1, 1)}, 
        right_dog.counterclockwisePiece().getBody());
    }

    @Test 
    void testSkirt(){
        assertArrayEquals(new int[]{2,2,2,2}, stick.getSkirt());
        assertArrayEquals(new int[]{1,1,1}, left_l.getSkirt());
        assertArrayEquals(new int[]{1,1,1}, right_l.getSkirt());
        assertArrayEquals(new int[]{0,0}, square.getSkirt());
        assertArrayEquals(new int[]{1,1,2}, right_dog.getSkirt());
        assertArrayEquals(new int[]{1,1,1}, t.getSkirt());
        assertArrayEquals(new int[]{2,1,1}, left_dog.getSkirt());
    }

    @Test 
    void testDown(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
        for(int i=0; i<4; i++){
            assertEquals(Result.SUCCESS, testBoard.move(Action.DOWN));
        }
    }

    @Test 
    void testRight(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
        for(int i=0; i<4; i++){
            assertEquals(Result.SUCCESS, testBoard.move(Action.RIGHT));
        }
    }

    @Test 
    void testLeft(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
     
        assertEquals(Result.SUCCESS, testBoard.move(Action.LEFT));
    }

    @Test 
    void testDrop(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
     
        assertEquals(Result.PLACE, testBoard.move(Action.DROP));
    }


    @Test 
    void testIsInLeftBound(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);

        for(int i=0; i<2; i++){
            assertEquals(Result.SUCCESS, testBoard.move(Action.LEFT));
        }
        assertEquals(Result.OUT_BOUNDS, testBoard.move(Action.LEFT));
    }

    @Test 
    void testIsInRightBound(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);

        for(int i=0; i<5; i++){
            assertEquals(Result.SUCCESS, testBoard.move(Action.RIGHT));
        }
        assertEquals(Result.OUT_BOUNDS, testBoard.move(Action.RIGHT));
    }

    @Test 
    void testIsInBottomBound(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);

        for(int i=0; i<18; i++){
            assertEquals(Result.SUCCESS, testBoard.move(Action.DOWN));
        }
        assertEquals(Result.PLACE, testBoard.move(Action.DOWN));
    }

    @Test 
    void testCWWallKick(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(7, 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
        assertEquals(Result.SUCCESS, testBoard.CWWallKick());

    }

    @Test 
    void testCCWWallKick(){
                TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(7, 17);
        testBoard.nextPiece(currPiece, currPiecePosition);
        assertEquals(Result.SUCCESS, testBoard.CCWWallKick());
    }

    @Test 
    void testClearRows(){
        TetrisBoard testBoard = tempBoard();
        TetrisPiece currPiece = new TetrisPiece(PieceType.T);
        Point currPiecePosition = new Point(testBoard.getWidth()/2 - currPiece.getWidth(), 17);
        testBoard.nextPiece(currPiece, currPiecePosition);

        for(int i =0; i<width; i++){
            testBoard.grid[3][i] = PieceType.STICK;
        }
        
        testBoard.clearRows();

        for(int i=0; i<width; i++){
            assertEquals(null, testBoard.grid[3][i]);
        }

    }

    public BigBrain brain(){
        BigBrain testBrain = new BigBrain();
        return testBrain;
    }

    //change variables to public in BigBrain to run test    
    /*
    @Test
    void testNextMove() {
        BigBrain brain = brain();
        TetrisBoard testBoard = tempBoard();
        
        brain.nextMove(testBoard);
        assertTrue(brain.scoreGame == scoreBoard(brain.options.get(i)));
        assertTrue(best == brain.firstMoves.get(best));
        
    }*/

    @Test
    void testEnumerateOptions() {
        BigBrain brain = brain();
        TetrisBoard testBoard = tempBoard();

        brain.enumerateOptions(testBoard);
        assertEquals(testBoard, testBoard.testMove(Board.Action.LEFT));
        
    }

    @Override
    public Result move(Action act) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'move'");
    }

    @Override
    public Board testMove(Action act) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'testMove'");
    }

    @Override
    public Piece getCurrentPiece() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPiece'");
    }

    @Override
    public Point getCurrentPiecePosition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPiecePosition'");
    }

    @Override
    public void nextPiece(Piece p, Point startingPosition) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextPiece'");
    }

    @Override
    public Result getLastResult() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastResult'");
    }

    @Override
    public Action getLastAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastAction'");
    }

    @Override
    public int getRowsCleared() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowsCleared'");
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWidth'");
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHeight'");
    }

    @Override
    public int getMaxHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxHeight'");
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dropHeight'");
    }

    @Override
    public int getColumnHeight(int x) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getColumnHeight'");
    }

    @Override
    public int getRowWidth(int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowWidth'");
    }

    @Override
    public PieceType getGrid(int x, int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGrid'");
    }

}
