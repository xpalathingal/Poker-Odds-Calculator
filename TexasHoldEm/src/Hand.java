import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */
public class Hand {
    ArrayList<Card> myHand;
    ArrayList<Integer> handStr;

    public Hand(Card first, Card second, Card[] board) {
        myHand = new ArrayList<Card>();
        handStr = new ArrayList<Integer>();
        myHand.add(first);
        myHand.add(second);
        Collections.addAll(myHand, board);
    }

//    private void addCard(Card card) {
//        myHand.add(card);
//    }
//
//    public void addBoard(Card[] board) {
//        for (int i = 0; i < board.length; i++)
//            addCard(board[i]);
//    }

    public ArrayList<Integer> evaluate() {
        // Code to evaluate hand here
        return handStr;
    }
}