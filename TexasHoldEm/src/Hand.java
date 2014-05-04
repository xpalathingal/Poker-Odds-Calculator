import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Xavier Palathingal (xvp2he) on 4/13/14.
 */
public class Hand implements Comparable<Hand>, Cloneable{
    ArrayList<Card> myHand;
    ArrayList<Integer> handStrength;
    boolean isEvaluated;

    public Hand() {
        myHand = new ArrayList<Card>();
        handStrength = new ArrayList<Integer>();
        isEvaluated = false;
    }

//    public Hand(Card first, Card second, Card[] board) {
//        myHand = new ArrayList<Card>();
//        handStrength = new ArrayList<Integer>();
//        myHand.add(first);
//        myHand.add(second);
//        Collections.addAll(myHand, board);
//    }

    public void addCard(Card card) {
        myHand.add(card);
        isEvaluated = false;
    }

    public void removeCard(Card card) {
        myHand.remove(card);
        isEvaluated = false;
    }

    public int getSize() { return myHand.size(); }

//
//    public void addBoard(Card[] board) {
//        for (int i = 0; i < board.length; i++)
//            addCard(board[i]);
//    }

    public void evaluate() {
        handStrength = evaluateHelper();
    }

    public ArrayList<Integer> evaluateHelper() {
        if (myHand.size() < 7) {
            ArrayList<Integer> lose = new ArrayList<Integer>();
            for (int i = 0; i < 7; i++)
                lose.add(-1);
            return lose;
        }

        Collections.sort(myHand);

        // Code to evaluate hand here
        //HAND MUST CONTAIN 7 CARDS
        ArrayList<Integer> bestHand = new ArrayList<Integer>();
        ArrayList<Card> diamond = new ArrayList<Card>();
        ArrayList<Card> spade = new ArrayList<Card>();
        ArrayList<Card> heart = new ArrayList<Card>();
        ArrayList<Card> club = new ArrayList<Card>();
        for (int i = 0; i < myHand.size(); i++) {
            char suit = myHand.get(i).getSuit();
            if (suit == 'd')
                diamond.add(myHand.get(i));
            if (suit == 's')
                spade.add(myHand.get(i));
            if (suit == 'h')
                heart.add(myHand.get(i));
            if (suit == 'c')
                club.add(myHand.get(i));
        }
        Collections.sort(diamond);
        Collections.sort(spade);
        Collections.sort(heart);
        Collections.sort(club);

        ArrayList<Integer> straightVals = new ArrayList<Integer>();
        for (int values = 0; values < myHand.size(); values++)
            if (!(straightVals.contains(myHand.get(values).getValue())))
                straightVals.add(myHand.get(values).getValue());
        int pairVal = -1;
        int highCardsAdded = 0;

        //check straight flush // 8
        boolean checksf = false;
        ArrayList<Card> sf = new ArrayList<Card>();
        if (diamond.size() >= 5) {
            checksf = true;
            sf = diamond;
        }
        if (spade.size() >= 5) {
            checksf = true;
            sf = spade;
        }
        if (heart.size() >= 5) {
            checksf = true;
            sf = heart;
        }
        if (club.size() >= 5) {
            checksf = true;
            sf = club;
        }
        if (checksf) {
            ArrayList<Integer> straightfVals = new ArrayList<Integer>();
            for (int transfer = 0; transfer < sf.size(); transfer++)
                straightfVals.add(sf.get(transfer).getValue());
            int highStraightf = -1;
            for (int straightf = 0; straightf < sf.size() - 4; straightf++)
                if (straightfVals.get(straightf) + straightfVals.get(straightf + 1) + straightfVals.get(straightf + 2) + straightfVals.get(straightf + 3) + straightfVals.get(straightf + 4) == straightfVals.get(straightf) * 5 - 10)
                    if (straightfVals.get(straightf) > highStraightf)
                        highStraightf = straightfVals.get(straightf);
            if (straightfVals.contains(5) && straightfVals.contains(4) && straightfVals.contains(3) && straightfVals.contains(2) && straightfVals.contains(14) && highStraightf == -1)
                highStraightf = 5;
            if (highStraightf != -1) {
                bestHand.add(8);
                bestHand.add(highStraightf);
                bestHand.add(highStraightf - 1);
                bestHand.add(highStraightf - 2);
                bestHand.add(highStraightf - 3);
                bestHand.add(highStraightf - 4);
                return bestHand;
            }
        }
        //check four of a kind // 7
        pairVal = -1;
        highCardsAdded = 0;
        for (int four = 0; four < myHand.size() - 3; four++)
            if ((myHand.get(four).getValue() == myHand.get(four + 1).getValue()) && (myHand.get(four).getValue() == myHand.get(four + 2).getValue()) && (myHand.get(four).getValue() == myHand.get(four + 3).getValue()))
                if (myHand.get(four).getValue() > pairVal)
                    pairVal = myHand.get(four).getValue();
        if (pairVal != -1) {
            bestHand.add(7);
            for (int adder = 0; adder < myHand.size(); adder++)
                if (myHand.get(adder).getValue() == pairVal)
                    bestHand.add(myHand.get(adder).getValue());
            for (int highCardAdder = 0; highCardAdder < myHand.size(); highCardAdder++)
                if ((myHand.get(highCardAdder).getValue() != pairVal) && highCardsAdded < 1) {
                    bestHand.add(myHand.get(highCardAdder).getValue());
                    highCardsAdded++;
                }
            return bestHand;
        }
        //check full house // 6
        int threeVal = -1;
        pairVal = -1;
        for (int boat1 = 0; boat1 < myHand.size() - 2; boat1++)
            if ((myHand.get(boat1).getValue() == myHand.get(boat1 + 1).getValue()) && (myHand.get(boat1).getValue() == myHand.get(boat1 + 2).getValue()))
                if (myHand.get(boat1).getValue() > threeVal)
                    threeVal = myHand.get(boat1).getValue();
        for (int boat2 = 0; boat2 < myHand.size() - 1; boat2++)
            if (myHand.get(boat2).getValue() == myHand.get(boat2 + 1).getValue())
                if ((myHand.get(boat2).getValue() > pairVal) && (myHand.get(boat2).getValue() != threeVal))
                    pairVal = myHand.get(boat2).getValue();
        if (threeVal != -1 && pairVal != -1) {
            bestHand.add(6);
            for (int adder = 0; adder < myHand.size(); adder++)
                if (myHand.get(adder).getValue() == threeVal)
                    bestHand.add(myHand.get(adder).getValue());
            for (int adder2 = 0; adder2 < myHand.size(); adder2++)
                if (myHand.get(adder2).getValue() == pairVal)
                    bestHand.add(myHand.get(adder2).getValue());
            return bestHand;
        }

        //check flush // 5
        if (diamond.size() >= 5) {
            bestHand.add(5);
            for (int flush = 0; flush < 5; flush++)
                bestHand.add(diamond.get(flush).getValue());
            return bestHand;
        }
        if (spade.size() >= 5) {
            bestHand.add(5);
            for (int flush = 0; flush < 5; flush++)
                bestHand.add(spade.get(flush).getValue());
            return bestHand;
        }
        if (heart.size() >= 5) {
            bestHand.add(5);
            for (int flush = 0; flush < 5; flush++)
                bestHand.add(heart.get(flush).getValue());
            return bestHand;
        }
        if (club.size() >= 5) {
            bestHand.add(5);
            for (int flush = 0; flush < 5; flush++)
                bestHand.add(club.get(flush).getValue());
            return bestHand;
        }
        //check straight // 4
        int highStraight = -1;
        for (int straight = 0; straight < straightVals.size() - 4; straight++)
            if (straightVals.get(straight) + straightVals.get(straight + 1) + straightVals.get(straight + 2) + straightVals.get(straight + 3) + straightVals.get(straight + 4) == straightVals.get(straight) * 5 - 10)
                if (straightVals.get(straight) > highStraight)
                    highStraight = straightVals.get(straight);
        if (straightVals.contains(5) && straightVals.contains(4) && straightVals.contains(3) && straightVals.contains(2) && straightVals.contains(14) && highStraight == -1)
            highStraight = 5;
        if (highStraight != -1) {
            bestHand.add(4);
            bestHand.add(highStraight);
            bestHand.add(highStraight - 1);
            bestHand.add(highStraight - 2);
            bestHand.add(highStraight - 3);
            bestHand.add(highStraight - 4);
            return bestHand;
        }
        //check three of a kind // 3
        pairVal = -1;
        highCardsAdded = 0;
        for (int three = 0; three < myHand.size() - 2; three++)
            if ((myHand.get(three).getValue() == myHand.get(three + 1).getValue()) && (myHand.get(three).getValue() == myHand.get(three + 2).getValue()))
                if (myHand.get(three).getValue() > pairVal)
                    pairVal = myHand.get(three).getValue();
        if (pairVal != -1) {
            bestHand.add(3);
            for (int adder = 0; adder < myHand.size(); adder++)
                if (myHand.get(adder).getValue() == pairVal)
                    bestHand.add(myHand.get(adder).getValue());
            for (int highCardAdder = 0; highCardAdder < myHand.size(); highCardAdder++)
                if ((myHand.get(highCardAdder).getValue() != pairVal) && highCardsAdded < 2) {
                    bestHand.add(myHand.get(highCardAdder).getValue());
                    highCardsAdded++;
                }
            return bestHand;
        }
        //check two pair // 2
        int pairVal2 = -1;
        pairVal = -1;
        highCardsAdded = 0;
        for (int pair1 = 0; pair1 < myHand.size() - 1; pair1++)
            if (myHand.get(pair1).getValue() == myHand.get(pair1 + 1).getValue())
                if (myHand.get(pair1).getValue() > pairVal)
                    pairVal = myHand.get(pair1).getValue();
        for (int pair2 = 0; pair2 < myHand.size() - 1; pair2++)
            if (myHand.get(pair2).getValue() == myHand.get(pair2 + 1).getValue())
                if ((myHand.get(pair2).getValue() > pairVal2) && (myHand.get(pair2).getValue() != pairVal))
                    pairVal2 = myHand.get(pair2).getValue();
        if (pairVal != -1 && pairVal2 != -1) {
            bestHand.add(2);
            for (int adder = 0; adder < myHand.size(); adder++)
                if (myHand.get(adder).getValue() == pairVal)
                    bestHand.add(myHand.get(adder).getValue());
            for (int adder2 = 0; adder2 < myHand.size(); adder2++)
                if (myHand.get(adder2).getValue() == pairVal2)
                    bestHand.add(myHand.get(adder2).getValue());
            for (int highCardAdder = 0; highCardAdder < myHand.size(); highCardAdder++)
                if ((myHand.get(highCardAdder).getValue() != pairVal) && (myHand.get(highCardAdder).getValue() != pairVal2) && highCardsAdded < 1) {
                    bestHand.add(myHand.get(highCardAdder).getValue());
                    highCardsAdded++;
                }
            return bestHand;
        }
        //check pair // 1
        pairVal = -1;
        highCardsAdded = 0;
        for (int two = 0; two < myHand.size() - 1; two++)
            if (myHand.get(two).getValue() == myHand.get(two + 1).getValue())
                if (myHand.get(two).getValue() > pairVal)
                    pairVal = myHand.get(two).getValue();
        if (pairVal != -1) {
            bestHand.add(1);
            for (int adder = 0; adder < myHand.size(); adder++)
                if (myHand.get(adder).getValue() == pairVal)
                    bestHand.add(myHand.get(adder).getValue());
            for (int highCardAdder = 0; highCardAdder < myHand.size(); highCardAdder++)
                if ((myHand.get(highCardAdder).getValue() != pairVal) && highCardsAdded < 3) {
                    bestHand.add(myHand.get(highCardAdder).getValue());
                    highCardsAdded++;
                }
            return bestHand;
        }
        //check high card // 0
        bestHand.add(0);
        for (int high = 0; high < 5; high++)
            bestHand.add(myHand.get(high).getValue());
        return bestHand;
    }
    
    @Override
    public int compareTo(Hand o) {
        if(!isEvaluated)
            evaluate();
        if (!o.isEvaluated)
            o.evaluate();

        for(int i = 0; i < handStrength.size(); i++) {
    		int difference = handStrength.get(i) - o.handStrength.get(i);
    		if(difference != 0)
    			return difference;
    	}
    	return 0;
    }

    public String toString() {
        String output = "";
        for (Card card : myHand)
            output = output + card.getValue() + card.getSuit() + " ";
        return output;
    }
}