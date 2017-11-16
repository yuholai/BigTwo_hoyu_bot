package bigtwo.cardcombination;

import static bigtwo.cardcombination.StraightComparators.TYPE_CUSTOM;
import static bigtwo.card.Card.*;

import java.util.Arrays;

import bigtwo.card.Card;
import bigtwo.card.Suit;

public class StraightComparatorCustom 
extends StraightComparator{
	private final int[] _rule;
	private final int BEGIN, END;
	
	public StraightComparatorCustom(int[] rule) {
		_rule = Arrays.copyOf(rule, rule.length);
		
		{
			int n = _rule[0];
			if(3<=n && n<=9){n = 3;}
			int min = n, max = n;
			for(int i=1; i<_rule.length; ++i){
				n = _rule[i];
				if(3<=n && n<=9){n = 3;}
				
				if(min > n){min = n;}
				if(max < n){max = n;}
			}
			
			BEGIN = min;
			END = max;
		}
	}
	
	public StraightComparatorCustom(int data){
		int[] rule = new int[7];
		int size = 0;

		int n;
		int min, max;
		{
			n = data & 8;
			if(n >= 4) n += 6;
			
			rule[size] = min = max = n;
			size = 1;
			
			for(int i=1; i<7; ++i){
				data = data >> 3;
				n = data & 8;
				
				if(n == 0){
					break;
				}else if(n >= 4){
					n += 6;
				}
				
				rule[size] = n;
				++size;
					
				if(min > n) min = n;
				if(max<n) max=n;
			}
		}
		
		_rule = Arrays.copyOf(rule, size);
		BEGIN = min;
		END = max;
	}
	
	public int getBegin(){
		return BEGIN;
	}
	
	public int getEnd(){
		return END;
	}
	
	public int[] getRule(){
		return Arrays.copyOf(_rule, _rule.length);
	}
	
	@Override
	public int compare(Straight straight1, Straight straight2) {
		int type1 = straight1.type(),
			type2 = straight2.type();
		
		for(int i=0; i<_rule.length; ++i){
			int rule = _rule[i];
			if(type1 == rule){
				if(type2 == type1){
					Card[] cards1 = straight1.getCards(),
						cards2 = straight2.getCards();
					Card max1 = cards1[0],
						max2 = cards2[0];
					
					for(int j=1; j<5; ++j)
						if(max1.trueRanking < cards1[j].trueRanking)
							max1 = cards1[j];
					for(int j=1; j<5; ++j)
						if(max2.trueRanking < cards2[j].trueRanking)
							max2 = cards2[j];
					
					return Suit.compareSuitAsc(max1.suit, max2.suit);
				}else{
					return 1;
				}
			}else if(type2 == rule){
				return -1;
			}
		}
		return 0;
	}
	
	@Override
	public int data(){
		int r = 0;
		{
			int i = _rule.length - 1;
			r = _rule[i];
			for(; i>=0; --i){
				r <<= 3;
				r = _rule[i];
			}
		}
		
		r |= TYPE_CUSTOM;
		
		return r;
	}
	
	@Override
	public int getComparatorType(){
		return TYPE_CUSTOM;
	}
	
	
//	#debug
	public static void main(String[] args){
		//StraightComparatorCustom cmp = new StraightComparatorCustom(new int[]{3,10,1,2});
		//System.out.println(cmp.BEGIN + " " + cmp.END);
		//cmp = new StraightComparatorCustom(new int[]{9,10,11});
		//System.out.println(cmp.BEGIN + " " + cmp.END);
		StraightComparatorCustom cmp = new StraightComparatorCustom(new int[]{3,10,1,2});
		System.out.println(cmp.compare(
			Straight.toStraight(new Card[]{d3,d4,d5,d6,d7}, StraightValidatorChain.getValidator(1,10)), 
			Straight.toStraight(new Card[]{d3,d4,d5,d6,s7}, StraightValidatorChain.getValidator(1,10))
			));
		System.out.println(cmp.compare(
				Straight.toStraight(new Card[]{d3,d4,d5,d6,d7}, StraightValidatorChain.getValidator(1,10)), 
				Straight.toStraight(new Card[]{d1,d2,d3,d4,s5}, StraightValidatorChain.getValidator(1,10))
				));
		System.out.println(cmp.compare(
				Straight.toStraight(new Card[]{d4,d5,d6,d7,d8}, StraightValidatorChain.getValidator(1,10)), 
				Straight.toStraight(new Card[]{d10,d11,d12,d13,s1}, StraightValidatorChain.getValidator(1,10))
				));
		System.out.println(cmp.compare(
				Straight.toStraight(new Card[]{s1,h2,c3,c4,c5}, StraightValidatorChain.getValidator(1,10)), 
				Straight.toStraight(new Card[]{d10,d11,d12,d13,s1}, StraightValidatorChain.getValidator(1,10))
				));
	}
}
