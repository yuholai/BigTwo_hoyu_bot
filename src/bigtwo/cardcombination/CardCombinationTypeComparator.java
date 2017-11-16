package bigtwo.cardcombination;

import static bigtwo.cardcombination.CardCombinationType.*;

public class CardCombinationTypeComparator {
	public static final CardCombinationTypeComparator
	FS = new CardCombinationTypeComparator(false), 
	SF = new CardCombinationTypeComparator(true);
	
	public static CardCombinationTypeComparator getComparator(boolean canStraightBeatsFlush){
		if(canStraightBeatsFlush)
			return SF;
		else
			return FS;
	}
	
	public final boolean canStraightBeatsFlush;
	
	private CardCombinationTypeComparator(boolean canStraightBeatsFlush){
		this.canStraightBeatsFlush = canStraightBeatsFlush;
	}
	
	public int compare(CardCombinationType type1, CardCombinationType type2){
		if(type1 == SINGLE){
			if(type2 == SINGLE){
				return 0;
			}else{
				return -1;
			}
		}else if(type1 == PAIR){
			if(type2 == PAIR)
				return 0;
			else
				return -1;
		}else if(type1 == TRIPLE){
			if(type2 == type1)
				return 0;
			else
				return -1;
		}else if(type1 == STRAIGHT){
			if(type2 == type1)
				return 0;
			else if(canStraightBeatsFlush && type2 == FLUSH)
				return 1;
			else 
				return -1;
		}else if(type1 == FLUSH){
			if(type2 == type1)
				return 0;
			else if(!canStraightBeatsFlush && type2 == STRAIGHT)
				return 1;
			else
				return 0;
		}else if(type1 == FULL_HOUSE){
			if(type2 == type1)
				return 0;
			else if(type2 == STRAIGHT || type2 == FLUSH)
				return 1;
			else
				return -1;
		}else if(type1 == FOUR_OF_A_KING){
			if(type2 == type1)
				return 0;
			else if(type2 == STRAIGHT || type2 == FLUSH || type2 == FULL_HOUSE)
				return 1;
			else
				return -1;
		}else if(type1 == STRAIGHT_FLUSH){
			if(type2 == type1)
				return 0;
			else if(type2 == STRAIGHT || type2 == FLUSH || type2 == FULL_HOUSE || type2 == FOUR_OF_A_KING)
				return 1;
			else
				return -1;
		}else{
			return -1;
		}
	}
}
