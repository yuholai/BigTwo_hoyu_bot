package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;

public class Triple {
	static boolean isTriple(Card card1, Card card2, Card card3){
		return	card1.rank == card2.rank &&
				card1.rank == card3.rank;
	}
	static boolean isTriple(Card[] cards){
		//return	cards[0].rank == cards[1].rank &&
		//		cards[1].rank == cards[2].rank;
		return isTriple(cards[0], cards[1], cards[2]);
	}
	static int compare(Card[] triple1, Card[] triple2){
		return Card.compareRankingAsc(triple1[0], triple2[0]);
	}
	
//	#debug
	public static void main(String[] args){
		System.out.println(Triple.isTriple(d5, c5, h5));
		System.out.println(Triple.isTriple(h5, s4, d5));
		System.out.println(Triple.isTriple(s4, d5, s6));
		System.out.println(Triple.isTriple(s4, s10, h10));
		
		System.out.println(Triple.compare(new Card[]{d5, c5, h5}, new Card[]{d1, s1, c1}));
		System.out.println(Triple.compare(new Card[]{d5, c5, h5}, new Card[]{d8, s8, c8}));
		System.out.println(Triple.compare(new Card[]{c5, d5, h5}, new Card[]{d5, c5, s5}));
	}
}
