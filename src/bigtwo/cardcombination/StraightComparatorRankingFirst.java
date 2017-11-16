package bigtwo.cardcombination;

import java.util.Arrays;

import bigtwo.card.Card;
import bigtwo.card.Suit;

import static bigtwo.cardcombination.StraightComparators.TYPE_RANKING_FIRST;
import static bigtwo.card.Card.*;

public class StraightComparatorRankingFirst 
extends StraightComparator{
	public static final StraightComparatorRankingFirst straightComparatorRankingFirst = 
		new StraightComparatorRankingFirst();
	
	private StraightComparatorRankingFirst() {
		super();
	}
	
	@Override
	public int compare(Straight straight1, Straight straight2) {
		Card[] 	cards1 = straight1.getCards(), 
				cards2 = straight2.getCards();
		
		cards1 = Arrays.copyOf(cards1, cards1.length);
		cards2 = Arrays.copyOf(cards2, cards2.length);
		Arrays.sort(cards1, Card::compareTrueRankingDesc);
		Arrays.sort(cards2, Card::compareTrueRankingDesc);
		
		for(int i=0; i<5; ++i){
			int rankdiff = cards1[i].rank.ranking - cards2[i].rank.ranking;
			if(rankdiff != 0){
				return rankdiff;
			}
		}
		
		for(int i=0; i<5; ++i){
			int suitdiff = Suit.compareSuitAsc(cards1[i].suit, cards2[i].suit);
			if(suitdiff != 0)
				return suitdiff;
		}
		
		return 0;
	}
	
	@Override
	public int getComparatorType(){
		return TYPE_RANKING_FIRST;
	}
	
	@Override
	public int data(){
		return TYPE_RANKING_FIRST;
	}
	
	
	
//	#debug
	public static void main(String[] args){
		//case 1
		StraightComparatorRankingFirst cmp = straightComparatorRankingFirst;
		System.out.println(
				cmp.compare(
						new Straight(new Card[]{
								d3, d4, d5, d6, d7
						}), 
						new Straight(new Card[]{
								d3, d4, d5, d6, s7
						})
						)
				);
		System.out.println(
				cmp.compare(
						new Straight(new Card[]{
								d3, d4, d5, d6, d7
						}), 
						new Straight(new Card[]{
								d4, d5, d6, d7, d8
						})
						)
				);
		System.out.println(
				cmp.compare(
						new Straight(new Card[]{
								d4, d5, d6, d7, d8
						}), 
						new Straight(new Card[]{
								d3, d4, d5, d6, d7
						})
						)
				);
		System.out.println(
				cmp.compare(
						new Straight(new Card[]{
								d1, d2, d3, d4, d5
						}), 
						new Straight(new Card[]{
								d2, d3, d4, d5, s6
						})
						)
				);
		System.out.println(
				cmp.compare(
						new Straight(new Card[]{
								d1, s2, d3, d4, d5
						}), 
						new Straight(new Card[]{
								d2, d3, d4, d5, s6
						})
						)
				);
		Card[] 	cards1 = new Card[]{
								d1, s2, d3, d4, d5
						};
		Arrays.sort(cards1, Card::compareTrueRankingDesc);
		for(Card card: cards1)
			System.out.println(card);
		cards1 = new Card[]{
				d2, d3, d4, d5, s6
		};
		Arrays.sort(cards1, Card::compareTrueRankingDesc);
		for(Card card: cards1)
			System.out.println(card);
	}
}
