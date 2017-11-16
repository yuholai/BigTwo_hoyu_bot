package bigtwo.cardcombination;

public class FlushComparators {
	public static final int
	TYPE_SUIT_FIRST    = 0,
	TYPE_CARD_FIRST    = 1,
	TYPE_RANKING_FIRST = 2;
	
	public static FlushComparator get(int type){
		if(type == TYPE_SUIT_FIRST){
			return FlushComparatorSuitFirst.flushComparatorSuitFirst;
		}else if(type == TYPE_CARD_FIRST){
			return FlushComparatorCardFirst.flushComparatorCardFirst;
		}else{
			return FlushComparatorRankingFirst.flushComparatorRankingFirst;
		}
	}
	
	private FlushComparators(){}
	
	
	/*	snippet
		
		//
		int type;
		if(type == TYPE_SUIT_FIRST){
			
		}else if(type == TYPE_CARD_FIRST){
			
		}else if(type == TYPE_RANKING_FIRST){
			
		}
		
		
		//
		FlushComparator fc;
		int type;
		if(type == TYPE_SUIT_FIRST){
			FlushComparatorSuitFirst fcsf = (FlushComparatorSuitFirst) fc;
		}else if(type == TYPE_CARD_FIRST){
			FlushComparatorCardFrist fccf = (FlushComparatorCardFirst) fc;
		}else if(type == TYPE_RANKING_FIRST){
			FlushComparatorRankingFirst fcrf = (FlushComparatorRankingFirst) fc;
		}
	 */
}
