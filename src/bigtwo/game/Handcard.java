package bigtwo.game;

import bigtwo.card.Card;

public class Handcard {
	public static int comparatorTrueRankingAsc(Handcard handcard1, Handcard handcard2){
		return Card.compareTrueRankingAsc(handcard1.card, handcard2.card);
	}
	public static int comparatorTrueRankingDesc(Handcard handcard1, Handcard handcard2){
		return Card.compareTrueRankingDesc(handcard1.card, handcard2.card);
	}
	public static int comparatorRankAsc(Handcard handcard1, Handcard handcard2){
		return Card.compareRankAsc(handcard1.card, handcard2.card);
	}
	public static int comparatorRankDesc(Handcard handcard1, Handcard handcard2){
		return Card.compareRankDesc(handcard1.card, handcard2.card);
	}
	public static int comparatorSelectedAsc(Handcard handcard1, Handcard handcard2){
		return (handcard1.selected?1:0) - (handcard2.selected?1:0);
	}
	public static int comparatorSelectedDesc(Handcard handcard1, Handcard handcard2){
		return (handcard2.selected?1:0) - (handcard1.selected?1:0);
	}
	
	
	public Card card;
	public boolean selected;
	
	public Handcard(Card card, boolean selected){
		this.card = card;
		this.selected = selected;
	}
	
	public Handcard(Card card){
		this(card, false);
	}
	
	@Override
	public String toString(){
		return "Handcard{" +
			"card:" + card.toString() + ";" +
			"selected:" + selected + ";" +
			"}";
	}
	
	public boolean equal(Card card){
		return this.card == card;
	}
}
