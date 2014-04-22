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

    public HoldEmCalculator() {
        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        pane = getContentPane();

        setTitle("Texas Hold 'Em Odds Calculator");
        setupCardPanel();
        board = new Card[5];
        setupBoardPanel();
        setupHandPanels();

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
        handPanels = new HandPanel[9];
        JPanel handGrid = new JPanel(new GridLayout(1, 9, 2, 2));
        for (int i = 0; i < handPanels.length; i++) {
            handPanels[i] = new HandPanel();
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
        private Hand hand;
        JPanel panel;
        JPanel handDisplay;
        JButton[] cards;
        JLabel winDisplay;
        JLabel tieDisplay;

        public HandPanel() {
            super();

            handDisplay = new JPanel();
            cards = new JButton[2];
            for (int i = 0; i < 2; i++) {
                cards[i] = new JButton();
                cards[i].setPreferredSize(new Dimension((int) (24 * 2.1), (int) (35 * 2.1)));
                cards[i].addActionListener(new HandButtonListener());
                cards[i].setBorder(thinBlackBorder);
                cards[i].setFont(new Font("Helvetica", Font.PLAIN, 18));
                cards[i].setBackground(Color.lightGray);
                cards[i].setFocusPainted(false);
                cards[i].setContentAreaFilled(false);
                cards[i].setOpaque(true);
            }

            handDisplay.add(cards[0]);
            handDisplay.add(cards[1]);

            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

            winDisplay = new JLabel("Win: ");
//            winDisplay.setForeground(Color.white);
//            winDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
            tieDisplay = new JLabel("Tie: ");
//            tieDisplay.setForeground(Color.white);
//            tieDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(handDisplay);
            panel.add(winDisplay);
            panel.add(tieDisplay);
        }

        public JPanel getPanel() {
            return panel;
        }
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

                // Disable used card
                ((CardButton)e.getSource()).setEnabled(false);

                // Move to next board card
                for (int i = boardFocus; i < 5; i++)
                    if (board[i] == null) {
                        boardButtons[i].doClick();
                        return;
                    }
                // If all the board cards are filled up
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
            }

            // Clear card and focus indicator
            board[street] = null;
            boardButtons[street].setText("");
            for (int i = 0; i < 5; i++)
                boardButtons[i].setBorder(blackBorder);

            // Switch focus to earliest open card
            for (int i = 0; i < 5; i++) {
                if(board[i] == null) {
                    boardButtons[i].setBorder(redBorder);
                    boardButtons[i].setText("");
                    boardFocus = i;
                    break;
                }
            }
        }
    }

    public class HandButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}