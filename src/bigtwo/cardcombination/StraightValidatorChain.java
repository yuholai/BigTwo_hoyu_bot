package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;

/*
 * applied for chain-style straights
 */
public class StraightValidatorChain extends StraightValidator{
	private static StraightValidatorChain[][] table = {
		new StraightValidatorChain[]{
			null,	// AA
			null,	// A2
			null,	// A3
			null,	// A4
			null,	// A5
			null,	// A6
			null,	// A7
			null,	// A8
			new StraightValidatorChain(1, 9),	// A9
			new StraightValidatorChain(1, 10),	// A10
			new StraightValidatorChain(1, 11),	// AJ
			null,	// AQ
			null	// AK
		},
		new StraightValidatorChain[]{
			null,	// 22
			null,	// 23
			null,	// 24
			null,	// 25
			null,	// 26
			null,	// 27
			null,	// 28
			new StraightValidatorChain(2, 9),	// 29
			new StraightValidatorChain(2, 10),	// 210
			new StraightValidatorChain(2, 11),	// 2J
			null,	// 2Q
			null	// 2K
		},
		null,	// 3~

		null,	// 4~

		null,	// 5~

		null,	// 6~

		null,	// 7~

		null,	// 8~

		null,	// 9~

		null,	// 10~

		null,	// J~

		null,	// Q~

		null	// K~
	};
	
	public static StraightValidatorChain getValidator(int begin, int end){
		StraightValidatorChain[] array = table[begin-1];
		if(array != null){
			int offset = end-begin;
			StraightValidatorChain element = array[offset];
			if(element!=null){
				return element;
			}else{
				StraightValidatorChain newSV = new StraightValidatorChain(begin, end);
				array[offset] = newSV;
				return newSV;
			}
		}else{
			array = new StraightValidatorChain[13-begin+1];
			table[begin-1] = array;
			int offset = end-begin;
			StraightValidatorChain newSV = new StraightValidatorChain(begin, end);
			array[offset] = newSV;
			return newSV;
		}
	}
	
	public static StraightValidatorChain getValidator(int data){
		return getValidator(data & 15, data >> 4);
	}
	
	/*
	 * the rank of the card at the begin/end of straight chain
	 */
	public final int BEGIN, END;
	
	private StraightValidatorChain(int begin, int end){
		BEGIN = begin;
		END = end;
	}
	
	public boolean valid
	(Card card1, Card card2, Card card3, Card card4, Card card5){
		//Straight straight = Straight.toStraight(card1, card2, card3, card4, card5);
		//
		//if(straight != null){
		//	Card begincard = straight.getCards()[0];
		//	return BEGIN <= begincard.rank && begincard.rank <= END;
		//}else{
		//	return false;
		//}
		Card begincard = card1;
		return BEGIN <= begincard.rank.rank && begincard.rank.rank <= END;
	}
	
	public boolean valid(Card[] straight){
		Card begincard = straight[0];
		return BEGIN <= begincard.rank.rank && begincard.rank.rank <= END;
	}
	
	public int data(){
		return (END << 4) | BEGIN;
	}
	
	
//	#debug
	public static void main(String[] args){
		StraightValidatorChain sv = getValidator(1, 10);
		System.out.println(sv.valid(d1,d2,d3,d4,d5));
		System.out.println(sv.valid(d3,d4,d5,d1,d2));
		System.out.println(sv.valid(d1,d2,d3,d4,d5));
		System.out.println(sv.valid(d9,d2,d3,d4,d5));
		System.out.println(sv.valid(d10,d2,d3,d4,d5));
		System.out.println(sv.valid(d11,d2,d3,d4,d5));
	}
}

