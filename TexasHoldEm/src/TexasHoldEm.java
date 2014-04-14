import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */

public class TexasHoldEm {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
//        System.out.print("How many hands are you inputting?");
        ArrayList<Hand> hands = new ArrayList<Hand>();

        for (int i = 0; i < 2; i++) {
            System.out.print("Hand #" + i + " (e.g. 'Ts 4c'):");
            String[] hand = in.next().split(" ");
            if (hand.length != 2) {
                System.out.println("Invalid hand!");
                i--;
            }
            hands.add(new Hand(hand[0], hand[1]));
        }

        System.out.print("Please input the community cards: ");
        String[] board = in.next().split(" ");
        while (board.length < 3 || board.length > 5) {
            System.out.print("Invalid board!");
            board = in.next().split(" ");
        }

        for(int i = 0; i < hands.size(); i++) {
            hands.get(i).addBoard(board);
            hands.get(i).evaluate();
        }
    }
}