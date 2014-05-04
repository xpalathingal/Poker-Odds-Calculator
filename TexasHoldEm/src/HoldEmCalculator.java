import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */

public class HoldEmCalculator extends JFrame {
    Container pane;
    private CardButton[] cardButtons;
    private Card[] board;
    private JButton[] boardButtons;
    private HandPanel[] handPanels;
    private final Border blackBorder = BorderFactory.createLineBorder(Color.black, 2);
    private final Border thinBlackBorder = BorderFactory.createLineBorder(Color.black, 1);
    private final Border redBorder = BorderFactory.createLineBorder(Color.red, 2);
    private final Border thinRedBorder = BorderFactory.createLineBorder(Color.red, 1);
    private int boardFocus = 0;
    private int handFocus = -1;

    public HoldEmCalculator() {
        JPanel contentPane = new JPanel( new BorderLayout(0, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        pane = getContentPane();

        setTitle("Texas Hold 'Em Odds Calculator");
        setupCardPanel();
        setupBoardPanel();
        setupHandPanels();
        //TEST
        //Card a = new Card(6, 'd');
        //Card b = new Card(6, 'h');
        //Card c = new Card(8, 's');
        //Card d = new Card(6, 'h');
        //Card e = new Card(2, 'd');
        //Card f = new Card(3, 'd');
        //Card g = new Card(2, 'd');
        		
       //Card[] board = {c, d, e, f, g};
       // Hand test = new Hand(a, b, board);
       // System.out.println(test.evaluate());
        
        //TEST
        //setSize(600, 400);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setupCardPanel() {
        cardButtons = new CardButton[52];
        JPanel cardPanel = new JPanel(new GridLayout(2, 26, 2, 2));

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
            cardButtons[i].setPreferredSize(new Dimension((int) (24 * 2.1), (int) (35 * 2.1)));
            cardButtons[i].setFont(new Font("Helvetica", Font.PLAIN, 18));
            cardButtons[i].setFocusPainted(false);
            cardPanel.add(cardButtons[i]);
        }

        pane.add(cardPanel, BorderLayout.PAGE_START);
    }

    private void setupBoardPanel() {
        board = new Card[5];
        boardButtons = new JButton[5];
        JPanel boardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 25));
        boardPanel.setBackground(new Color(0, 64, 0));

        for (int i = 0; i < 5; i++) {
            boardButtons[i] = new JButton();
            boardButtons[i].addActionListener(new BoardButtonListener(i));
            boardButtons[i].setBorder(blackBorder);
            boardButtons[i].setPreferredSize(new Dimension((int) (24 * 4), (int) (35 * 4)));
            boardButtons[i].setFont(new Font("Helvetica", Font.BOLD, 30));
            boardButtons[i].setBackground(Color.lightGray);
            boardButtons[i].setFocusPainted(false);
            boardButtons[i].setContentAreaFilled(false);
            boardButtons[i].setOpaque(true);
            if (i > 2) {
                boardPanel.add(new JLabel("   "));
            }
            boardPanel.add(boardButtons[i]);
        }

        boardButtons[0].setBorder(redBorder);
        pane.add(boardPanel, BorderLayout.CENTER);
    }

    private void setupHandPanels() {
        handPanels = new HandPanel[3];
        JPanel handGrid = new JPanel(new GridLayout(1, 3, 2, 2));
        for (int i = 0; i < handPanels.length; i++) {
            handPanels[i] = new HandPanel(i);
            handGrid.add(handPanels[i].getPanel());
        }
//        handGrid.setOpaque(false);
        pane.add(handGrid, BorderLayout.PAGE_END);
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

            setBackground(Color.white);
            if (suit == 'd' || suit == 'h')
                setForeground(Color.red);
        }

        public Card getCard() {
            return card;
        }
    }

    public class HandPanel {
        public Card cards[];
        private Hand hand;
        JPanel panel;
        JPanel handDisplay;
        private JButton[] cardButtons;
        JLabel winDisplay;
        JLabel tieDisplay;

        public HandPanel(int index) {
            super();
            cards = new Card[2];
            hand = new Hand();

            handDisplay = new JPanel();
            cardButtons = new JButton[2];
            for (int i = 0; i < 2; i++) {
                cardButtons[i] = new JButton();
                cardButtons[i].setPreferredSize(new Dimension((int) (24 * 2.1), (int) (35 * 2.1)));
                cardButtons[i].addActionListener(new HandButtonListener(index*2+i));
                cardButtons[i].setBorder(thinBlackBorder);
                cardButtons[i].setFont(new Font("Helvetica", Font.PLAIN, 18));
                cardButtons[i].setBackground(Color.lightGray);
                cardButtons[i].setFocusPainted(false);
                cardButtons[i].setContentAreaFilled(false);
                cardButtons[i].setOpaque(true);
            }

            handDisplay.add(cardButtons[0]);
            handDisplay.add(cardButtons[1]);

            panel = new JPanel(new BorderLayout());

            winDisplay = new JLabel("Win: 0%");
            winDisplay.setBorder(new EmptyBorder(0, 185, 0, 0));
//            winDisplay.setForeground(Color.white);
//            winDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
            tieDisplay = new JLabel("Tie:  100%");
            tieDisplay.setBorder(new EmptyBorder(0, 185, 0, 0));
//            tieDisplay.setForeground(Color.white);
//            tieDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(handDisplay, BorderLayout.PAGE_START);
            panel.add(winDisplay, BorderLayout.CENTER);
            panel.add(tieDisplay, BorderLayout.PAGE_END);
        }

        public void addCard(Card card) {
            if (cards[0] == null) {
                cards[0] = card;
                hand.addCard(card);
                cardButtons[0].setBorder(thinBlackBorder);
            } else if (cards[1] == null) {
                cards[1] = card;
                hand.addCard(card);
                cardButtons[1].setBorder(thinBlackBorder);
            }

            // Set hand card display to clicked card
            cardButtons[handFocus%2].setText(card.toString());
            if (card.getSuit() == 'd' || card.getSuit() == 'h')
                cardButtons[handFocus%2].setForeground(Color.red);
            else
                cardButtons[handFocus%2].setForeground(Color.black);

            // Move to next hand card
            if (cards[1] == null) {
                cardButtons[1].doClick();
            // Remove focus and calculate odds if both cards are not null
            } else {
                handFocus = -1;
            }
        }

        public Card removeCard(int index) {
            Card temp;
            if (index == 0) {
                temp = cards[0];
                hand.removeCard(cards[0]);
                cards[0] = null;
            } else if (index == 1) {
                temp = cards[1];
                hand.removeCard(cards[1]);
                cards[1] = null;
            } else {
                temp = null;
            }

            return temp;
        }

        public JPanel getPanel() { return panel; }
    }

    public class CardButtonListener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            if (boardFocus != -1) {
                // Set board card to clicked card
                board[boardFocus] = ((CardButton)e.getSource()).getCard();
                boardButtons[boardFocus].setText(board[boardFocus].toString());
                if (board[boardFocus].getSuit() == 'd' || board[boardFocus].getSuit() == 'h')
                    boardButtons[boardFocus].setForeground(Color.red);
                else
                    boardButtons[boardFocus].setForeground(Color.black);

                // Add card to hands
                for(int i = 0; i < handPanels.length; i++)
                    handPanels[i].hand.addCard(board[boardFocus]);

                // Move to next board card
                boolean isFull = true;
                for (int i = boardFocus; i < 5; i++)
                    if (board[i] == null) {
                        boardButtons[i].doClick();
                        isFull = false;
                        break;
                    }

                // If all the board cards are filled up
                if(isFull) {
                    for (int i = 0; i < 5; i++)
                        boardButtons[i].setBorder(blackBorder);
                    boardFocus = -1;
                }

                // Disable used card
                ((CardButton)e.getSource()).setEnabled(false);
            } else if (handFocus != -1) {
                // Set hand card to clicked card
                handPanels[handFocus/2].addCard(((CardButton)e.getSource()).getCard());

                // Disable used card if hand isn't full
                ((CardButton)e.getSource()).setEnabled(false);
            }

            calculateOdds();
        }
    }

    private void calculateOdds() {
        int first = 0;
        int second = 0;
        int third = 0;
        int tie12 = 0;
        int tie13 = 0;
        int tie23 = 0;
        int tie123 = 0;


        int boardCount = 0;
        for(int i = 0; i < board.length; i++)
            if(board[i] != null)
                boardCount++;

        ArrayList<Card> remainingCards = new ArrayList<Card>();
        for (CardButton cb: cardButtons) {
            if (cb.isEnabled() == true) {
                remainingCards.add(cb.getCard());
            }
        }

        if (boardCount == 3) {
            for (Card card : remainingCards) {
                Hand firstHand = handPanels[0].hand;
                Hand secondHand = handPanels[1].hand;
                Hand thirdHand = handPanels[2].hand;

                firstHand.addCard(card);
                secondHand.addCard(card);
                thirdHand.addCard(card);

                for (Card card2 : remainingCards) {
                    if(card.getSuit() == card2.getSuit() && card.getValue() == card2.getValue())
                        continue;

                    firstHand.addCard(card2);
                    secondHand.addCard(card2);
                    thirdHand.addCard(card2);

                    int firstComp = firstHand.compareTo(secondHand);
                    int secondComp = firstHand.compareTo(thirdHand);
                    int thirdComp = secondHand.compareTo(thirdHand);

                    if (firstComp > 0 && secondComp > 0)
                        first++;
                    else if (firstComp < 0 && thirdComp > 0)
                        second++;
                    else if (secondComp < 0 && thirdComp < 0)
                        third++;
                    else if (firstComp == 0 && secondComp > 0)
                        tie12++;
                    else if (secondComp == 0 && firstComp > 0)
                        tie13++;
                    else if (thirdComp == 0 && firstComp < 0)
                        tie23++;
                    else
                        tie123++;

                    firstHand.removeCard(card2);
                    secondHand.removeCard(card2);
                    thirdHand.removeCard(card2);
                }

                firstHand.removeCard(card);
                secondHand.removeCard(card);
                thirdHand.removeCard(card);

//                    System.out.println(firstHand);
//                    System.out.println(secondHand);
//                    System.out.println(thirdHand);
            }
        }
        if (boardCount == 4) {
            for (Card card : remainingCards) {
                Hand firstHand = handPanels[0].hand;
                Hand secondHand = handPanels[1].hand;
                Hand thirdHand = handPanels[2].hand;

                firstHand.addCard(card);
                secondHand.addCard(card);
                thirdHand.addCard(card);

                int firstComp = firstHand.compareTo(secondHand);
                int secondComp = firstHand.compareTo(thirdHand);
                int thirdComp = secondHand.compareTo(thirdHand);

                if (firstComp > 0 && secondComp > 0)
                    first++;
                else if (firstComp < 0 && thirdComp > 0)
                    second++;
                else if (secondComp < 0 && thirdComp < 0)
                    third++;
                else if (firstComp == 0 && secondComp > 0)
                    tie12++;
                else if (secondComp == 0 && firstComp > 0)
                    tie13++;
                else if (thirdComp == 0 && firstComp < 0)
                    tie23++;
                else
                    tie123++;

                firstHand.removeCard(card);
                secondHand.removeCard(card);
                thirdHand.removeCard(card);

//                    System.out.println(firstHand);
//                    System.out.println(secondHand);
//                    System.out.println(thirdHand);
            }
        } else {
            Hand firstHand = handPanels[0].hand;
            Hand secondHand = handPanels[1].hand;
            Hand thirdHand = handPanels[2].hand;

            int firstComp = firstHand.compareTo(secondHand);
            int secondComp = firstHand.compareTo(thirdHand);
            int thirdComp = secondHand.compareTo(thirdHand);

            if(firstComp > 0 && secondComp > 0)
                first++;
            else if (firstComp < 0 && thirdComp > 0)
                second++;
            else if (secondComp < 0 && thirdComp < 0)
                third++;
            else if (firstComp == 0 && secondComp > 0)
                tie12++;
            else if (secondComp == 0 && firstComp > 0)
                tie13++;
            else if (thirdComp == 0 && firstComp < 0)
                tie23++;
            else
                tie123++;

//                System.out.println(firstHand);
//                System.out.println(secondHand);
//                System.out.println(thirdHand);
        }

        if (boardCount == 3) {
            tie123--;
        }
        double total = first + second + third + tie12 + tie13 + tie23 + tie123;
//        System.out.println(first + " " + second + " " + third + " " + tie12 + " " + tie13 + " " + tie23 + " " + tie123 + " " + total);

        DecimalFormat df = new DecimalFormat("##0.00");

        handPanels[0].winDisplay.setText("Win: " + df.format(first / total * 100) + "%");
        handPanels[0].tieDisplay.setText("Tie:  " + df.format((tie12 + tie13 + tie123) / total * 100) + "%");
        handPanels[1].winDisplay.setText("Win: " + df.format(second/total * 100) + "%");
        handPanels[1].tieDisplay.setText("Tie:  " + df.format((tie12 + tie23 + tie123) / total * 100) + "%");
        handPanels[2].winDisplay.setText("Win: " + df.format(third/total * 100) + "%");
        handPanels[2].tieDisplay.setText("Tie:  " + df.format((tie13 + tie23 + tie123) / total * 100) + "%");
    }

    public class BoardButtonListener implements ActionListener {
        int street;

        public BoardButtonListener(int i) {
            super();
            street = i;
        }

        public void actionPerformed (ActionEvent e) {
            // Re-enable now available card
            if(board[street] != null) {
                Card temp = board[street];
                switch (temp.getSuit()) {
                    case 'c':
                        cardButtons[temp.getValue()-2].setEnabled(true);
                        break;
                    case 'd':
                        cardButtons[11 + temp.getValue()].setEnabled(true);
                        break;
                    case 'h':
                        cardButtons[24 + temp.getValue()].setEnabled(true);
                        break;
                    case 's':
                        cardButtons[37 + temp.getValue()].setEnabled(true);
                        break;
                }

                // Remove card from hands
                for(int i = 0; i < handPanels.length; i++)
                    handPanels[i].hand.removeCard(board[street]);
            }

            // Clear card and focus indicator
            board[street] = null;
            boardButtons[street].setText("");
            for (int i = 0; i < 5; i++)
                boardButtons[i].setBorder(blackBorder);

            // Clear focus on hands
            for (int i = 0; i < handPanels.length; i++) {
                handPanels[i].cardButtons[0].setBorder(thinBlackBorder);
                handPanels[i].cardButtons[1].setBorder(thinBlackBorder);
            }

            // Switch focus to earliest open card
            for (int i = 0; i < 5; i++) {
                if(board[i] == null) {
                    boardButtons[i].setBorder(redBorder);
                    handFocus = -1;
                    boardFocus = i;
                    break;
                }
            }

            calculateOdds();
        }
    }

    public class HandButtonListener implements ActionListener {

        int index;

        public HandButtonListener(int i) {
            super();
            index = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Re-enable now available card
            if (handPanels[index / 2].cards[index % 2] != null) {
                Card temp = handPanels[index / 2].cards[index % 2];
                switch (temp.getSuit()) {
                    case 'c':
                        cardButtons[temp.getValue() - 2].setEnabled(true);
                        break;
                    case 'd':
                        cardButtons[11 + temp.getValue()].setEnabled(true);
                        break;
                    case 'h':
                        cardButtons[24 + temp.getValue()].setEnabled(true);
                        break;
                    case 's':
                        cardButtons[37 + temp.getValue()].setEnabled(true);
                        break;
                }

                // Remove card from hand
                handPanels[index / 2].removeCard(index % 2);
            }

            // Clear card and focus indicator
            handPanels[index / 2].cardButtons[index % 2].setText("");
            for (int i = 0; i < handPanels.length; i++) {
                handPanels[i].cardButtons[0].setBorder(thinBlackBorder);
                handPanels[i].cardButtons[1].setBorder(thinBlackBorder);
            }

            // Clear focus on board
            for (int i = 0; i < boardButtons.length; i++) {
                boardButtons[i].setBorder(blackBorder);
            }

            // Switch focus to earliest open card
            for (int i = 0; i < 2; i++) {
                if (handPanels[index / 2].cards[i] == null) {
                    handPanels[index / 2].cardButtons[i].setBorder(thinRedBorder);
                    boardFocus = -1;
                    handFocus = 2*(index / 2) + i;
                    break;
                }
            }

            calculateOdds();
        }
    }
}