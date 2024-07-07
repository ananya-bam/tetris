package assignment;

import java.util.*;

import assignment.Board.Action;

public class BigBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    @Override
    public Action nextMove(Board currentBoard) {

        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        int best = Integer.MIN_VALUE;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            //System.out.println("score: " + score);
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }
        // We want to return the first move on the way to the best Board
        //System.out.println("best ind: " + firstMoves.get(bestIndex) + " " + currentBoard.getCurrentPiece().getType());
        return firstMoves.get(bestIndex);

    }

    private void enumerateOptions(Board currentBoard) {
        // We can always drop our current Piece


        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMoves.add(Board.Action.DROP);
        Board.Action rotation = null;// current rotation

        for (int i = 0; i < 4; i++) { // loops through all rotations
            // Now we'll add all the places to the left we can DROP
            if(i==3){
                rotation= Board.Action.COUNTERCLOCKWISE;
            }

            Board left = currentBoard.testMove(Board.Action.LEFT);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                if(rotation== null)firstMoves.add(Board.Action.LEFT);
                else firstMoves.add(rotation);
                left.move(Board.Action.LEFT);
            }

            // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                if(rotation== null)firstMoves.add(Board.Action.RIGHT);
                else firstMoves.add(rotation);
                right.move(Board.Action.RIGHT);
            }

            currentBoard = currentBoard.testMove(Board.Action.CLOCKWISE);
            rotation = Board.Action.CLOCKWISE;

        }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private int scoreBoard(Board newBoard) {
        // return 100 - (newBoard.getMaxHeight() * 5);
        int score = 100;
        score -= (newBoard.getMaxHeight() * 5);
        
        // loop through the rows
        for (int i = 0; i < newBoard.getHeight(); i++) {
            if (newBoard.getRowWidth(i) == newBoard.getWidth() - 1) {
                score += 2;
            }
            // positive influence for large row width
            score += (newBoard.getRowWidth(i) * 10);
            // if (newBoard.getColumnHeight)
        }


        int minColumnHeight = Integer.MAX_VALUE;
        // loop through the columns
        for (int i = 0; i < newBoard.getWidth(); i++) {
            // negative influence for large column height
            score -= newBoard.getColumnHeight(i) * 10;
            if (newBoard.getColumnHeight(i) < minColumnHeight)
                minColumnHeight = newBoard.getColumnHeight(i);
        }

        score -= (newBoard.getMaxHeight() - minColumnHeight)*0.5;

        score -= boardHoles(newBoard) * 4;

        score -= boardBumps(newBoard) * 0.5;


        return score;
    }


    private int boardHoles(Board newBoard){
        int holes =0;
        for(int i=0; i<newBoard.getWidth(); i++){
            for(int j=0; j<newBoard.getHeight() - 1; j++){
                if(newBoard.getGrid(i, j) == null && newBoard.getGrid(i, j+1) != null){
                    holes++;
                }
            }
        }
        return holes;
    }


    private int boardBumps(Board newBoard){
        int bumps =0;
        
        //loop through the columns
        for(int i=0; i< newBoard.getWidth() -1; i++){
            int current = newBoard.getColumnHeight(i);
            int next = newBoard.getColumnHeight(i + 1);
            //bumpiness = (nextColumnHeight minus currentColumnHeight)^2 
            bumps += Math.pow(next - current, 2);
        }
        return bumps;
    }

}
