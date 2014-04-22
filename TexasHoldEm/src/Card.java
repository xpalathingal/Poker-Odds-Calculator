/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */
class Card implements Comparable<Card> {
    int value;
    char suit;
    private String textVal;

    public Card(int value, char suit) {
        this.value = value;
        this.suit = suit;

        textVal = "<html><center>";

        if (value == 14)
            textVal = textVal + "A";
        else if (value == 13)
            textVal = textVal + "K";
        else if (value == 12)
            textVal = textVal + "Q";
        else if (value == 11)
            textVal = textVal + "J";
        else if (value == 10)
            textVal = textVal + "T";
        else
            textVal = textVal + value;

        textVal = textVal + "<br>";

        if(suit == 's')
            textVal = textVal + '\u2660';
        else if(suit == 'h')
            textVal = textVal + '\u2665';
        else if(suit == 'd')
            textVal = textVal + '\u2666';
        else if(suit == 'c')
            textVal = textVal + '\u2663';

        textVal = textVal + "</center></html>";
    }

    int getValue() {
        return value;
    }

    int getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card o) {
        return getValue() - o.getValue();
    }

    @Override
    public String toString() {
        return textVal;
    }
}