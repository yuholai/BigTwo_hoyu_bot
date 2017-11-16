package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;

public class Flush {
	static boolean isFlush
	(Card card1, Card card2, Card card3, Card card4, Card card5){
		return	card1.suit == card2.suit &&
				card1.suit == card3.suit &&
				card1.suit == card4.suit &&
				card1.suit == card5.suit;
	}
	
	static boolean isFlush
	(Card[] cards){
		return	cards[1].suit == cards[2].suit &&
				cards[1].suit == cards[3].suit &&
				cards[1].suit == cards[4].suit &&
				cards[1].suit == cards[0].suit;
	}
	
//	#debug
	public static void main(String[] args){
		System.out.println(isFlush(d1,d6,d7,d3,d4));
		System.out.println(isFlush(d1,c6,d7,d3,d4));
		System.out.println(isFlush(d1,d2,d3,d5,d4));
	}
}
