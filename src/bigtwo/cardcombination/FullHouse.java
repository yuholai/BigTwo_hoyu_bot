package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;

public class FullHouse {
	static boolean isFullHouse
	(Card tri1, Card tri2, Card tri3, Card pair1, Card pair2){
		return Triple.isTriple(tri1, tri2, tri3) && Pair.isPair(pair1, pair2);
	}
	static boolean isFullHouse
	(Card[] triple, Card[] pair){
		//return Triple.isTriple(triple[0], triple[1], triple[2]) &&
		//		Pair.isPair(pair[0], pair[1]);
		return isFullHouse(triple[0], triple[1], triple[2], pair[0], pair[1]);
	}
	static int compare(Card[] fullhouse1, Card[] fullhouse2){
		return Card.compareRankAsc(fullhouse1[0], fullhouse2[0]);
	}
	
	
//	#debug
	public static void main(String[] args){
		System.out.println(isFullHouse(d1,c1,h1,d2,c2));
		System.out.println(isFullHouse(d1,s1,h1,c2,d2));
		System.out.println(isFullHouse(s8,c8,h8,jR,jb));
		
		System.out.println(compare(new Card[]{d1,c1,h1,d2,c2},new Card[]{d13,c13,h13,d2,c2}));
		
	}
}
