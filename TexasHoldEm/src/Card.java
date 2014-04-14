/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */
class Card implements Comparable<Card> {
    int value;
    char suit;

    public Card(String input) {
        String val = input.substring(0,1);
        suit = input.charAt(1);

        if (val.equals("A"))
            value = 14;
        else if (val.equals("K"))
            value = 13;
        else if (val.equals("Q"))
            value = 12;
        else if (val.equals("J"))
            value = 11;
        else if (val.equals("T"))
            value = 10;
        else
            value = Integer.parseInt(val);
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
}