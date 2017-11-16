package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import java.util.Arrays;

import bigtwo.card.Card;
import bigtwo.card.Suit;

public class FlushComparatorSuitFirst extends FlushComparator {
	public static final FlushComparatorSuitFirst flushComparatorSuitFirst = 
		new FlushComparatorSuitFirst();
	
	private FlushComparatorSuitFirst() {}

	@Override
	public int compare(Card[] cards1, Card[] cards2) {
		int suitDifference = Suit.compareSuitAsc(cards1[0].suit, cards2[0].suit);
		if(suitDifference != 0){
			return suitDifference;
		}else{
			Card[]	sorted1 = Arrays.copyOf(cards1, 5),
					sorted2 = Arrays.copyOf(cards2, 5);
			Arrays.sort(sorted1, Card::compareRankDesc);
			Arrays.sort(sorted2, Card::compareRankDesc);
			for(int i=0; i<5; ++i){
				int difference = Card.compareRankAsc(sorted1[i], sorted2[i]);
				if(difference != 0) return difference;
			}
			return 0;
		}
	}
	
	@Override
	public int type(){
		return FlushComparators.TYPE_SUIT_FIRST;
	}
	

	
//	#debug
	public static void main(String[] args){
		System.out.println(flushComparatorSuitFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s3}));
		System.out.println(flushComparatorSuitFirst.compare(new Card[]{d1,d2,d13,d12,d11}, new Card[]{s3,s2,s6,s4,s5}));
		System.out.println(flushComparatorSuitFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s3}));   
		System.out.println(flushComparatorSuitFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s5}));   
		System.out.println(flushComparatorSuitFirst.compare(new Card[]{d1,d2,d13,d12,d11}, new Card[]{s3,s2,s6,s4,s5}));
	}
}
