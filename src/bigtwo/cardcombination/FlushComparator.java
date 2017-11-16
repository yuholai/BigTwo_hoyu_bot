package bigtwo.cardcombination;

import bigtwo.card.Card;

public abstract class FlushComparator {
	public abstract int compare(Card[] cards1, Card[] cards2);
	public abstract int type();
}
