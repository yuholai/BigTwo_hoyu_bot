package bigtwo.cardcombination;

import static bigtwo.card.Card.*;

import bigtwo.card.Card;

public class Straight {
	public static boolean isStraight
	(Card card1, Card card2, Card card3, Card card4, Card card5, StraightValidator validator){
		int breakpoint = 0;
		if(card1.rank.rank+1 != card2.rank.rank){breakpoint=1;}
		if(card2.rank.rank+1 != card3.rank.rank){if(breakpoint!=0){return false;}else{breakpoint = 2;}}
		if(card3.rank.rank+1 != card4.rank.rank){if(breakpoint!=0){return false;}else{breakpoint = 3;}}
		if(card4.rank.rank+1 != card5.rank.rank){if(breakpoint!=0){return false;}else{breakpoint = 4;}}
		return (breakpoint==0 || (card1.rank.rank == 1 && card5.rank.rank == 13)) && 
				validator.valid(new Straight(new Card[]{card1, card2, card3, card4, card5}, breakpoint).cards);
	}
	
	public static boolean isStraight(Card[] cards, StraightValidator validator){
		return isStraight(
		    cards[0],
			cards[1],
			cards[2],
			cards[3],
			cards[4],
			validator);
	}
	
	public static Straight toStraight(Card[] cards, StraightValidator validator){
		int breakpoint = 0;
		for(int i=0; i<3; ++i){
			if(cards[i].rank.rank+1 != cards[i+1].rank.rank){
				if(breakpoint != 0){
					return null;
				}else{
					breakpoint = i + 1;
				}
			}
		}
		if(breakpoint != 0){if(cards[0].rank.rank != 1 || cards[4].rank.rank != 13){
			return null;
		}}
		
		Straight straight = new Straight(cards, breakpoint);
		
		if(validator.valid(straight.cards)){
			return straight;
		}else{
			return null;
		}
	}
	
	public static Straight toStraight
	(Card card1, Card card2, Card card3, Card card4, Card card5, 
			StraightValidator validator){
		return toStraight(new Card[]{card1, card2, card3, card4, card5}, validator);
	}
	
	private final Card[] cards;
	
	private Straight(Card[] cards, int breakpoint){
		this.cards = new Card[5];
		
		int i=0;
		for(int j=breakpoint; i<5 && j<5; ++i, ++j){
			this.cards[i] = cards[j];
		}
		for(int j=0; i<5 && j<breakpoint; ++i, ++j){
			this.cards[i] = cards[j];
		}
	}
	
	public Straight(Card[] cards){
		this.cards = cards;
	}
	
	public Card[] getCards(){
		return cards;
	}
	
	public int type(){
		if(3 <= cards[0].rank.rank && cards[0].rank.rank <= 9)
			return 3;
		else
			return cards[0].rank.rank;
	}
	
//	#debug
	public static boolean isStraight(Card card1, Card card2, Card card3, Card card4, Card card5){
		int flagbreak = 0;
		if(card1.rank.rank+1 != card2.rank.rank){flagbreak=1;}
		if(card2.rank.rank+1 != card3.rank.rank){if(flagbreak!=0){return false;}else{flagbreak = 2;}}
		if(card3.rank.rank+1 != card4.rank.rank){if(flagbreak!=0){return false;}else{flagbreak = 3;}}
		if(card4.rank.rank+1 != card5.rank.rank){if(flagbreak!=0){return false;}else{flagbreak = 4;}}
		return (flagbreak==0 || (card1.rank.rank == 1 && card5.rank.rank == 13));
	}
	
	public static void main(String[] args){
		//System.out.println(isStraight(d1,d2,d3,d4,d5));
		//System.out.println(isStraight(c3,c4,c5,c6,c7));
		//System.out.println(isStraight(h9,h10,h11,h12,h13));
		//System.out.println(isStraight(h10,h11,h12,h13,jb));
		//System.out.println(isStraight(h10,h11,h12,h13,jR));
		//System.out.println(isStraight(d1,h10,h11,h12,h13));
		//System.out.println(isStraight(h11,h12,h13,d1,d2));
		//System.out.println(isStraight(d1,d2,h11,h12,h13));
		//System.out.println(isStraight(h13,d1,d2,d3,d4));
		//System.out.println(isStraight(d1,d2,d3,d4,h13));
		//System.out.println(isStraight(d2,d3,d5,d6,d7));
		
		//StraightValidatorChain sv = StraightValidatorChain.getValidator(1, 10);
		//System.out.println(isStraight(d1,d2,d3,d4,d5,sv));
		//System.out.println(isStraight(c3,c4,c5,c6,c7,sv));
		//System.out.println(isStraight(h9,h10,h11,h12,h13,sv));
		//System.out.println(isStraight(d1,h10,h11,h12,h13,sv));
		//System.out.println(isStraight(h11,h12,h13,d1,d2,sv));
		//System.out.println(isStraight(d1,d2,h11,h12,h13,sv));
		//System.out.println(isStraight(h13,d1,d2,d3,d4,sv));
		//System.out.println(isStraight(d1,d2,d3,d4,h13,sv));
		//System.out.println(isStraight(d2,d3,d5,d6,d7,sv));
		
		//System.out.println(isStraight(new Card[]{d3,d4,d5,d6,d7}, StraightValidatorChain.getValidator(1,10)));
		//System.out.println(toStraight(new Card[]{d3,d4,d5,d6,d7}, StraightValidatorChain.getValidator(1,10)).type());
		//System.out.println(Straight.toStraight(new Card[]{d3,d4,d5,d6,s7}, StraightValidatorChain.getValidator(1,10)).type());
	}
}
