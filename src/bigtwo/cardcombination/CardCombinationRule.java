package bigtwo.cardcombination;

import static bigtwo.cardcombination.CardCombinationType.FLUSH;
import static bigtwo.cardcombination.CardCombinationType.FOUR_OF_A_KING;
import static bigtwo.cardcombination.CardCombinationType.FULL_HOUSE;
import static bigtwo.cardcombination.CardCombinationType.PAIR;
import static bigtwo.cardcombination.CardCombinationType.SINGLE;
import static bigtwo.cardcombination.CardCombinationType.STRAIGHT;
import static bigtwo.cardcombination.CardCombinationType.STRAIGHT_FLUSH;
import static bigtwo.cardcombination.CardCombinationType.TRIPLE;

import bigtwo.cardcombination.CardCombinationTypeValidator.Result;
import bigtwo.card.Card;

public class CardCombinationRule {
	private CardCombinationTypeValidator typeValidator;
	
	private CardCombinationTypeComparator typeComparator;
	private StraightComparator straightComparator;
	private FlushComparator flushComparator;
	
	
	public CardCombinationRule
	(CardCombinationTypeValidator validator,
	 CardCombinationTypeComparator typeComparator,
	 StraightComparator straightComparator,
	 FlushComparator flushComparator)
	{
		this.typeValidator = validator;
		this.typeComparator = typeComparator;
		this.straightComparator = straightComparator;
		this.flushComparator = flushComparator;
	}
	
	public CardCombinationRule
	(int sv_data,
	 boolean allowTriple,
	 boolean canStraightBeatsFlush,
	 int sc_data,
	 int fc_type)
	{
		this(
			new CardCombinationTypeValidator
			(StraightValidatorChain.getValidator(sv_data), 
			 allowTriple), 
			CardCombinationTypeComparator.getComparator(canStraightBeatsFlush),
			StraightComparators.get(sc_data),
			FlushComparators.get(fc_type)
			);
	}
	
	// default rule
	public CardCombinationRule(){
		this(new CardCombinationTypeValidator(StraightValidatorChain.getValidator(1, 10), true), 
			CardCombinationTypeComparator.FS,
			StraightComparatorRankingFirst.straightComparatorRankingFirst, 
			FlushComparatorSuitFirst.flushComparatorSuitFirst);
	}
	
	// getters
	public boolean getAllowTriple(){
		return typeValidator.getAllowTriple();
	}
	
	public StraightValidator getStraightValidator(){
		return typeValidator.getStraightValidator();
	}
	
	public boolean getCanStraightBeatFlush(){
		return typeComparator.canStraightBeatsFlush;
	}
	
	public StraightComparator getStraightComparator(){
		return straightComparator;
	}
	
	public FlushComparator getFlushComparator(){
		return flushComparator;
	}
	
	// setters
	public void setAllowTriple(boolean value){
		typeValidator.setAllowTriple(value);
	}
	
	public void setStraightValidator(StraightValidator straightValidator){
		this.typeValidator.setStraightValidator(straightValidator);
	}
	
	public void setStraightValidator(int begin, int end){
		this.typeValidator.setStraightValidator(
			StraightValidatorChain.getValidator(begin, end)
		);
	}
	
	public void setStraightValidator(int data){
		this.typeValidator.setStraightValidator(
			StraightValidatorChain.getValidator(data)
		);
	}
	
	public void setTypeComparator(CardCombinationTypeComparator typeComparator){
		this.typeComparator = typeComparator;
	}
	
	public void setTypeComparator(boolean canStraightBeatFlush){
		this.typeComparator = 
			CardCombinationTypeComparator
				.getComparator(canStraightBeatFlush);
	}
	
	public void setStraightComparator(StraightComparator straightComparator){
		this.straightComparator = straightComparator;
	}
	
	public void setStraightComparator(int sc_data){
		this.straightComparator = StraightComparators.get(sc_data);
	}
	
	public void setFlushComparator(FlushComparator flushComparator){
		this.flushComparator = flushComparator;
	}
	
	public void setFlushComparator(int fc_type){
		this.flushComparator = FlushComparators.get(fc_type);
	}
	
	
	public CardCombination cardCombination(Card[] cards){
		Result cct = typeValidator.determineTypeFormatted(cards);
		if(cct.formatted != null)
			cards = cct.formatted;
		return new CardCombination(
			cards, 
			cct.type
		);
	}
	
	public int compare(CardCombination cc1, CardCombination cc2){
		int typediff = typeComparator.compare(cc1.type, cc2.type);
		Card[]	cards1 = cc1.getCards(),
				cards2 = cc2.getCards();
		
		if(typediff == 0){
			if(cc1.type == SINGLE){return Card.compareTrueRankingAsc(cards1[0], cards2[0]);}
			else if(cc1.type == PAIR){return Pair.compare(cards1, cards2);}
			else if(cc1.type == TRIPLE){return Triple.compare(cards1, cards2);}
			else if(cc1.type == STRAIGHT)
				{return straightComparator.compare(new Straight(cards1), new Straight(cards2));}
			else if(cc1.type == FLUSH){return flushComparator.compare(cards1, cards2);}
			else if(cc1.type == FULL_HOUSE){return FullHouse.compare(cards1, cards2);}
			else if(cc1.type == FOUR_OF_A_KING){return FourOfAKing.compare(cards1, cards2);}
			else if(cc1.type == STRAIGHT_FLUSH)
				{return straightComparator.compare(new Straight(cards1), new Straight(cards2));}
			else{
				return -1;
			}
		}else{
			return typediff;
		}
	}
}
