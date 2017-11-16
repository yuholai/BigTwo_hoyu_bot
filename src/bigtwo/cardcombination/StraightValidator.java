package bigtwo.cardcombination;

import bigtwo.card.Card;

public abstract class StraightValidator {
	public abstract boolean valid
	(Card card1, Card card2, Card card3, Card card4, Card card5);
	
	public abstract boolean valid(Card[] straight);
}
