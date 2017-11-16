package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import java.util.Arrays;

import bigtwo.card.Card;

public class FlushComparatorRankingFirst extends FlushComparator {
	public static final FlushComparatorRankingFirst flushComparatorRankingFirst =
		new FlushComparatorRankingFirst();
	
	private FlushComparatorRankingFirst(){}
	
	@Override
	public int compare(Card[] cards1, Card[] cards2) {
		Card[]	sorted1 = Arrays.copyOf(cards1, 5),
				sorted2 = Arrays.copyOf(cards2, 5);
		Arrays.sort(sorted1, Card::compareRankingDesc);
		Arrays.sort(sorted2, Card::compareRankingDesc);
		
		for(Card card:sorted1)
			System.out.println(card);
		for(Card card:sorted2)
			System.out.println(card);
		
		for(int i=0; i<5; ++i){
			int difference = Card.compareRankingAsc(sorted1[i], sorted2[i]);
			if(difference != 0) return difference;
		}
		
		return Card.compareSuitAsc(sorted1[0], sorted2[0]);
	}
	
	@Override
	public int type(){
		return FlushComparators.TYPE_RANKING_FIRST;
	}
	
	
	
//	#debug
	public static void main(String[] args){
		System.out.println(flushComparatorRankingFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s3}));
		System.out.println(flushComparatorRankingFirst.compare(new Card[]{d1,d2,d4,d5,d7}, new Card[]{s1,s2,s7,s4,s5}));
		System.out.println(flushComparatorRankingFirst.compare(new Card[]{d1,d2,d13,d12,d11}, new Card[]{s3,s2,s6,s4,s5}));
	}
}
