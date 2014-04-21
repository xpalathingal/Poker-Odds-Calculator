import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */
public class Hand {
    ArrayList<Card> myHand;
    ArrayList<Integer> handStr;

    public Hand(String first, String second) {
        myHand = new ArrayList<Card>();
        handStr = new ArrayList<Integer>();
        addCard(first);
        addCard(second);
    }

    private void addCard(String card) {
//        myHand.add(new Card(card));
    }

    public void addBoard(String[] board) {
        for (int i = 0; i < board.length; i++)
            addCard(board[i]);
    }

    public void evaluate() {
        // Code to evaluate hand here
    }
}