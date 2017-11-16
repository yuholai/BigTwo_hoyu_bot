package bigtwo.cardcombination;

public class StraightComparators {
	/*
	 * sc_data
	 * type	data
	 * 000	000000000000000000000
	 * ^	^					^
	 * 23	20					0
	 * |3|	|<-      21       ->|	Total: 24
	 * 
	 */
	public static final int
	FILTER_TYPE = 0b111000000000000000000000,
	FILTER_DATA = 0b000111111111111111111111;
	
	public static final int
		TYPE_RANKING_FIRST = 0b000000000000000000000000,
		TYPE_CUSTOM     = 0b001000000000000000000000;
	
	public static StraightComparator get(int data){
		int type = data & FILTER_TYPE;
		if(type == TYPE_RANKING_FIRST){
			return StraightComparatorRankingFirst.straightComparatorRankingFirst;
		}else/* if(type == CUSTOM)*/{
			return new StraightComparatorCustom(data);
		}
	}
	
	private StraightComparators(){}
	
	
	/*	snippet
		
		
		//
		int type = data & 0B111000000000000000000000;
		if(type == TYPE_RANK_FIRST){
			
		}else if(type == CUSTOM){
			
		}
		
		
		//
		StraightComparator sc;
		int type = data & 0B111000000000000000000000;
		if(type == TYPE_RANK_FIRST){
			StraightComparatorRankFirst scrf = (StraightComparatorRankFirst) sc;
		}else if(type == CUSTOM){
			StraightComparatorCustom scc = (StraightComparatorCustom) sc;
		}
	 */
}
