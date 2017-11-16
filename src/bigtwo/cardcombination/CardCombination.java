package bigtwo.cardcombination;

import bigtwo.card.Card;

public class CardCombination {
	private Card[] _cards;
	public final CardCombinationType type;
	
	CardCombination(Card[] cards, CardCombinationType type){
		_cards = cards;
		this.type = type;
	}
	
	public Card[] getCards(){
		return _cards;
	}
	
	//public CardCombinationType getType(){
	//	return type;
	//}
	
	public boolean contain(Card card){
		for(Card card1 : _cards){
			if(card1.equals(card1))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		String r = new String();
		r += "CardCombination{type:" + type + "cards:{";
		if(0<_cards.length)
			r += _cards[0].toString();
		for(int i=1; i<_cards.length; ++i)
			r += "," + _cards[i].toString();
		r += "}";
		return r;
	}
	
//	#debug
	public static void main(String[] args){
		/* test 1 */
		//CardCombinationRule rule = 
		//	new CardCombinationRule(
		//		new Validator(StraightValidatorChain.getValidator(1, 10), true),
		//			CardCombinationType.Comparator.FS,
		//			StraightComparatorRankingFirst.straightComparatorRankingFirst,
		//			FlushComparatorSuitFirst.flushComparatorSuitFirst);
		//// test case 1
		//System.out.println(rule.cardCombination(
		//	new Card[]{}
		//).toString());
		//// test case 2
		//System.out.println(rule.cardCombination(
		//	new Card[]{d1, d2, d3, d4, d5}
		//).toString());
		//// test case 3
		//System.out.println(rule.cardCombination(
		//	new Card[]{d6, d7, d9, d10, d8}
		//).toString());
		//// test case 4
		//System.out.println(rule.cardCombination(
		//	new Card[]{d10, d11, d12, d13, d1}
		//).toString());
		//// test case 5
		//System.out.println(rule.cardCombination(
		//	new Card[]{d11, d12, d13, d1, d2}
		//).toString());
		//// test case 6
		//System.out.println(
		//	Straight.isStraight(
		//		new Card[]{d1, d2, d3, d4, d5},
		//		StraightValidatorChain.getValidator(1, 10)
		//	)
		//);
		//// test case 7
		//System.out.println(
		//	StraightValidatorChain.getValidator(1, 10).valid(new Card[]{d1, d2, d3, d4, d5})
		//);
		
		// test 2
		// straightflush comparison
		//System.out.println(rule.cardCombination(new Card[]{d1,d2,d3,d4,d5}).type);
		//System.out.println(rule.cardCombination(new Card[]{d1,d2,c3,d4,d5}).type);
		//System.out.println(rule.cardCombination(new Card[]{d5,d2,d3,d1,d5}).type);
		//System.out.println(rule.cardCombination(new Card[]{d10,d11,d1,d13,d12}).type);
		//
		//System.out.println(rule.compare(
		//		rule.cardCombination(new Card[]{d1,c2,d3,d4,c5}),
		//		rule.cardCombination(new Card[]{s1,d2,s3,s4,d5})));
		
	}
}
