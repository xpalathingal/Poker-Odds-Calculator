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

        if (value == 14)
            textVal = "A";
        else if (value == 13)
            textVal = "K";
        else if (value == 12)
            textVal = "Q";
        else if (value == 11)
            textVal = "J";
        else if (value == 10)
            textVal = "T";
        else
            textVal = value + "";

        textVal = textVal + suit;
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