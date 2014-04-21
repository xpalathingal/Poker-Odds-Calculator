import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.border.Border;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */

public class HoldEmCalculator extends JFrame {
    Container pane = getContentPane();
    private CardButton[] cardButtons;
    private Card[] board;
    private JButton[] boardButtons;
    private final Border blackBorder = BorderFactory.createLineBorder(Color.black);
    private final Border redBorder = BorderFactory.createLineBorder(Color.red);
    private int boardFocus = -1;

    public HoldEmCalculator() {
        setTitle("Texas Hold 'Em Calculator");
        setupCardPanel();
        board = new Card[5];
        setupBoardPanel();

        //setSize(600, 400);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupCardPanel() {
        cardButtons = new CardButton[52];
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(2, 26));

        for (int i = 51; i >= 0; i--) {
            int passedVal = i % 13 + 2;
            char suit;
            switch (i/13) {
                case 3:
                    suit = 's';
                    break;
                case 2:
                    suit = 'h';
                    break;
                case 1:
                    suit = 'd';
                    break;
                default:
                    suit = 'c';
                    break;
            }

            cardButtons[i] = new CardButton(passedVal, suit);
            cardButtons[i].addActionListener(new CardButtonListener());
            cardButtons[i].setPreferredSize(new Dimension((int)(24*2.1), (int)(35*2.1)));
            cardPanel.add(cardButtons[i]);
        }

        pane.add(cardPanel, BorderLayout.PAGE_START);
    }

    private void setupBoardPanel() {
        boardButtons = new JButton[5];
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        for (int i = 0; i < 5; i++) {
            boardButtons[i] = new JButton();
            boardButtons[i].addActionListener(new BoardButtonListener(i));
            boardButtons[i].setBorder(blackBorder);
            boardButtons[i].setPreferredSize(new Dimension((int)(24*3), (int)(35*3)));
            boardPanel.add(boardButtons[i]);
        }

        pane.add(boardPanel, BorderLayout.CENTER);
    }

    private void otherSetup() {
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

    public static void main(String[] args) {
        HoldEmCalculator calc = new HoldEmCalculator();
    }

    public class CardButton extends JButton {
        private Card card;

        public CardButton(int val, char suit) {
            super(val + "" + suit);
            card = new Card(val, suit);
            setText(card.toString());

            if (suit == 'd' || suit == 'h')
                setBackground(Color.RED);
        }

        public Card getCard() {
            return card;
        }
    }

    public class CardButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            if (boardFocus != -1) {
                board[boardFocus] = ((CardButton)e.getSource()).getCard();
                boardButtons[boardFocus].setText(board[boardFocus].toString());
            }
            if (boardFocus != -1 && boardFocus < 4)
                boardButtons[boardFocus + 1].doClick();
            else {
                for (int i = 0; i < 5; i++)
                    boardButtons[i].setBorder(blackBorder);
                boardFocus = -1;
            }

        }
    }

    public class BoardButtonListener implements ActionListener {
        int street;

        public BoardButtonListener(int i) {
            super();
            street = i;
        }

        public void actionPerformed (ActionEvent e) {
            for (int i = 0; i < 5; i++)
                boardButtons[i].setBorder(blackBorder);
            boardFocus = street;
            boardButtons[street].setBorder(redBorder);
        }
    }
}