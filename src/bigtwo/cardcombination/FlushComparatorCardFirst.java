package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import java.util.Arrays;

import bigtwo.card.Card;

/*
 * compare the largest card in each flush
 */

public class FlushComparatorCardFirst extends FlushComparator{
	public static final FlushComparatorCardFirst flushComparatorCardFirst =
		new FlushComparatorCardFirst();
	
	private FlushComparatorCardFirst(){}
	
	@Override
	public int compare(Card[] cards1, Card[] cards2) {
		Card[]	sorted1 = Arrays.copyOf(cards1, 5),
				sorted2 = Arrays.copyOf(cards2, 5);
		Arrays.sort(sorted1, Card::compareTrueRankingDesc);
		Arrays.sort(sorted2, Card::compareTrueRankingDesc);
		for(int i=0; i<5; ++i){
			int difference = Card.compareTrueRankingAsc(sorted1[i], sorted2[i]);
			if(difference != 0) return difference;
		}
		
		return 0;
	}
	
	@Override
	public int type(){
		return FlushComparators.TYPE_CARD_FIRST;
	}
	
	
	
//	#debug
	public static void main(String[] args){
		System.out.println(flushComparatorCardFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s3}));
		System.out.println(flushComparatorCardFirst.compare(new Card[]{d1,d2,d13,d12,d11}, new Card[]{s3,s2,s6,s4,s5}));
	}
}
