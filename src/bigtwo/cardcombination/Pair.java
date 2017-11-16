package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;
import bigtwo.card.Suit;

public class Pair {
	static boolean isPair(Card card1, Card card2){
		return card1.rank == card2.rank;
	}
	static boolean isPair(Card[] cards){
		//return	cards[0].rank == cards[1].rank;
		return isPair(cards[0], cards[1]);
	}
	static int compare(Card[] pair1, Card[] pair2){
		int rankdiff = Card.compareRankingAsc(pair1[0], pair2[0]);
		if(rankdiff == 0){
			Suit max1 = (
					(Suit.compareSuitAsc(pair1[0].suit, pair1[1].suit)>0)?
					(pair1[0].suit):(pair1[1].suit)), 
				 max2 = (
					(Suit.compareSuitAsc(pair2[0].suit, pair2[1].suit)>0)?
					(pair2[0].suit):(pair2[1].suit));
			return Suit.compareSuitAsc(max1, max2);
		}else{
			return rankdiff;
		}
	}
	
//	#debug
	public static void main(String[] args){
		System.out.println(Pair.isPair(d1, d4));
		System.out.println(Pair.isPair(d1, s4));
		System.out.println(Pair.isPair(d1, c1));
		System.out.println(Pair.isPair(h1, d4));
		
		System.out.println(Pair.compare(new Card[]{d1, c1}, new Card[]{d2, c2}));
		System.out.println(Pair.compare(new Card[]{d1, c1}, new Card[]{s3, h3}));
		System.out.println(Pair.compare(new Card[]{d1, c1}, new Card[]{s1, h1}));
		System.out.println(Pair.compare(new Card[]{d1, s1}, new Card[]{c1, h1}));
	}
}
