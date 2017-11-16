package bigtwo.cardcombination;

import static bigtwo.cardcombination.CardCombinationType.*;

import java.util.Arrays;

import bigtwo.card.Card;


public class CardCombinationTypeValidator {
	public static class Result{
		public CardCombinationType type;
		public Card[] formatted;
		
		public Result(CardCombinationType type, Card[] cards){
			this.type = type;
			this.formatted = cards;
		}
	}
	
	private StraightValidator _sv;
	private boolean _allowTriple;
	
	public CardCombinationTypeValidator
	(StraightValidator straightValidator, boolean alloweTriple){
		this._sv = straightValidator;
		this._allowTriple = alloweTriple;
	}
	
	public StraightValidator getStraightValidator(){
		return _sv;			
	}
	
	public boolean getAllowTriple(){
		return _allowTriple;
	}
	
	public void setStraightValidator(StraightValidator value){
		this._sv = value;
	}
	
	public void setAllowTriple(boolean value){
		this._allowTriple = value;
	}
	
	public Result determineTypeFormatted
	(Card[] cards){
		switch(cards.length){
		case 1:
			//if(Single.isSingle(WTF
			return new Result(SINGLE, null);
		case 2:
			if(Pair.isPair(cards[0], cards[1]))
				return new Result(PAIR, null);
			else
				return new Result(INVALID, null);
		case 3:
			if(_allowTriple){
				if(Triple.isTriple(cards[0], cards[1], cards[2])){
					return new Result(TRIPLE, null);
				}else{
					return new Result(INVALID, null);
				}
			}else{
				return new Result(INVALID, null);
			}
		case 5:
			Card[] cardscopy = Arrays.copyOf(cards, 5);
			Arrays.sort(cardscopy, Card::compareRankAsc);
			
			Straight straight = 
				Straight.toStraight(cardscopy, _sv);
			if(straight != null)
			{
				if(Flush.isFlush(cardscopy[0], cardscopy[1], cardscopy[2], 
						cardscopy[3], cardscopy[4]))
				{
					return new Result(STRAIGHT_FLUSH, null);
				}
				else
				{
					return new Result(STRAIGHT, straight.getCards());
				}
			}
			else if(Flush.isFlush(cardscopy))
			{
				return new Result(FLUSH, null);
			}
			else if(FullHouse.isFullHouse(cardscopy[0], cardscopy[1], cardscopy[2], 
					cardscopy[3], cardscopy[4])){
				return new Result(FULL_HOUSE, cardscopy);
			}
			else if(FullHouse.isFullHouse(cardscopy[2], cardscopy[3], cardscopy[4], 
					cardscopy[0], cardscopy[1]))
			{
				Card temp = cardscopy[0];
				cardscopy[0] = cardscopy[2];
				cardscopy[2] = cardscopy[4];
				cardscopy[4] = cardscopy[1];
				cardscopy[1] = cardscopy[3];
				cardscopy[3] = temp;
				return new Result(FULL_HOUSE, cardscopy);
			}
			else if(FourOfAKing.isFourOfAKing(cardscopy[0], cardscopy[1], cardscopy[2], 
					cardscopy[3], cardscopy[4])){
				return new Result(FOUR_OF_A_KING, cardscopy);
			}
			else if(FourOfAKing.isFourOfAKing(cardscopy[1], 
					cardscopy[2], cardscopy[3], cardscopy[4], cardscopy[0]))
			{
				Card[] formatted = {cardscopy[1], cardscopy[2], 
					cardscopy[3], cardscopy[4], cardscopy[0]};
				return new Result(FOUR_OF_A_KING, formatted);
			}
			else
			{
				return new Result(INVALID, null);
			}
		default:
				return new Result(INVALID, null);
		}
	}

	public CardCombinationType determineType
	(Card[] cards){
		switch(cards.length){
		case 1:
			//if(Single.isSingle(
			return SINGLE;
		case 2:
			if(Pair.isPair(cards[0], cards[1]))
				return PAIR;
			else
				return INVALID;
		case 3:
			if(_allowTriple){
				if(Triple.isTriple(cards[0], cards[1], cards[2])){
					return TRIPLE;
				}else{
					return INVALID;
				}
			}else
				return INVALID;
		case 5:
			Card[] cardscopy = Arrays.copyOf(cards, 5);
			Arrays.sort(cardscopy, Card::compareRankAsc);
			
			
			if(Straight.isStraight(cardscopy, _sv))
			{
				if(Flush.isFlush(cardscopy[0], cardscopy[1], cardscopy[2], 
						cardscopy[3], cardscopy[4]))
				{
					return STRAIGHT_FLUSH;
				}
				else
				{
					return STRAIGHT;
				}
			}
			else if(Flush.isFlush(cardscopy))
			{
				return FLUSH;
			}
			else if(FullHouse.isFullHouse(cardscopy[0], cardscopy[1], cardscopy[2], 
					cardscopy[3], cardscopy[4])		|| 
					FullHouse.isFullHouse(cardscopy[2], cardscopy[3], cardscopy[4], 
					cardscopy[0], cardscopy[1]))
			{
				return FULL_HOUSE;
			}
			else if(FourOfAKing.isFourOfAKing(cardscopy[0], cardscopy[1], cardscopy[2], 
					cardscopy[3], cardscopy[4]) 	||
					FourOfAKing.isFourOfAKing(cardscopy[1], 
					cardscopy[2], cardscopy[3], cardscopy[4], cardscopy[0]))
			{
				return FOUR_OF_A_KING;
			}
			else
			{
				return INVALID;
			}
		default:
				return INVALID;
		}
	}
}
