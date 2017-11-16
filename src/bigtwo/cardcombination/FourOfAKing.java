package bigtwo.cardcombination;

import bigtwo.card.Card;

public class FourOfAKing {
	static boolean isFourOfAKing
	(Card quadruple1, Card quadruple2, Card quadruple3, Card quadruple4, Card card1){
		return	quadruple1.rank == quadruple2.rank &&
				quadruple1.rank == quadruple3.rank &&
				quadruple1.rank == quadruple4.rank;
	}
	static boolean isFourOfAKing
	(Card[] quadruple, Card card1){
		return isFourOfAKing(
			quadruple[0],
			quadruple[1],
			quadruple[2],
			quadruple[3],
			card1);
	}
	static int compare(Card[] fourofaking1, Card[] fourofaking2){
		return Card.compareRankAsc(fourofaking1[0], fourofaking2[0]);
	}
}
