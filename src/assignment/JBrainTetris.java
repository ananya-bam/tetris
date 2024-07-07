package assignment;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class JBrainTetris extends JTetris {
    LameBrain lame = new LameBrain();
    BigBrain big = new BigBrain();
    protected javax.swing.Timer timer;

    JBrainTetris() {
        timer = new javax.swing.Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //tick(lame.nextMove(board));
                tick(big.nextMove(board));
            }
        });
    }

    // startgame
    public void startGame() {
        super.startGame();
        timer.start();
    }

    // stopgame
    public void stopGame() {
        timer.stop();
        super.stopGame();
    }

    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }

}
