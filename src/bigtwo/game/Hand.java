package bigtwo.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import bigtwo.card.Card;
import static bigtwo.card.Card.*;


public class Hand {
//	class constants:
	public static final int MAX_selected = 5;
	
	
	
//	instance fields:
	private ArrayList<Handcard> handcards;
	private int nHandcardSelected;
	
	
	
//	constructors:
	public Hand(int handlen){
		handcards = new ArrayList<Handcard>(handlen);
		nHandcardSelected = 0;
	}
	
	public Hand(Card[] cards){
		this(cards.length);
		Arrays.sort(cards, Card::compareTrueRankingAsc);
		for(Card card : cards)
			handcards.add(new Handcard(card));
	}
	
	
	
//	instance methods:
	public ArrayList<Handcard> getHandcards(){
		return handcards;
	}
	
	public int getNumberOfHandcards(){
		return handcards.size();
	}
	
	public void appendNumberOfHandcardsArtTo(StringBuilder strb){
		for(int i=0; i<handcards.size(); ++i)
			strb.append(Card.EMOJI_FLOWER_PLAYING_CARDS);
	}
	
	public int getNumberOfCardsSelected(){
		return nHandcardSelected;
	}
	
	
	public Card[] selectedCards(){
		Card[] selectedhandcards = new Card[nHandcardSelected];
		int i = 0;
		for(Handcard handcard : handcards)
			if(handcard.selected){
				selectedhandcards[i] = handcard.card;
				++i;
			}
		return selectedhandcards;
	}
	
	
	/* for receiving cards in dealing */
	public void add(Card card){
		handcards.add(new Handcard(card));
	}
	
	public void add(Card... cards){
		for(Card card : cards)
			add(card);
	}
	
	
	public Handcard select(int index){
		if(index < handcards.size()){
			Handcard handcard = handcards.get(index);
			if(handcard.selected){
				handcard.selected = false;
				--nHandcardSelected;
			}else if(nHandcardSelected < MAX_selected){
				handcard.selected = true;
				++nHandcardSelected;
			}
			return handcard;
		}
		return null;
	}
	
	public Handcard select(Card select){
		for(Handcard handcard : handcards){
			if(handcard.equal(select)){
				if(handcard.selected){
					handcard.selected = false;
					--nHandcardSelected;
				}else if(nHandcardSelected < MAX_selected){
					handcard.selected = true;
					++nHandcardSelected;
				}
				return handcard;
			}
		}
		return null;
	}
	
	
	public boolean unselectAll(){
		boolean changed = nHandcardSelected != 0;
		if(nHandcardSelected != 0){
			nHandcardSelected = 0;
			for(Handcard handcard : handcards)
				handcard.selected = false;
		}
		return changed;
	}
	
	
	public boolean contain(Card card){
		for(Handcard handcard : handcards)
			if(handcard.card.equals(card))
				return true;
		return false;
	}
	
	
	/* remove selected cards */
	public void removeSelectedHandcard(){
		for(int i=0; i<handcards.size(); ++i){
			Handcard handcard = handcards.get(i);
			if(handcard.selected){
				handcards.remove(i);
				--i;
				--nHandcardSelected;
			}
		}
	}
	
	/* remove cards to be sent*/
	public void removeHandCard(Card... card){
		for(int i=0; i<handcards.size(); ++i){
			Handcard handcard = handcards.get(i);
			if(handcard.selected){
				handcards.remove(i);
				--i;
				--nHandcardSelected;
			}
		}
	}
	
	
	public Card minTrueRankingCard(){
		Card min = handcards.get(0).card;
		for(int i=1; i<handcards.size(); ++i){
			Card card1 = handcards.get(i).card;
			if(Card.compareTrueRankingAsc(min, card1) > 0) min = card1;
		}
		return min;
	}
	
	public boolean isEmpty(){
		return handcards.size() == 0;
	}
	

	@Override
	public String toString(){
		String str = new String();
		for(Handcard handcard : handcards)
			str += handcard.toString() + "\n";
		return str;
	}
	
	public void sort(Comparator<Handcard> cmp){
		handcards.sort(cmp);
	}
	
	//public int line(){
	//	int r = 0;
	//	r += Math.ceil(pairs.size() / 3f);
	//	r += Math.ceil(triples.size() / 2f);
	//	r += straights.size() + flushes.size() + full_houses.size() + four_of_a_kings.size() + straight_flushes.size();
	//	return r;
	//}
	
//	#debug
	public void sort(){
		handcards.sort(Handcard::comparatorTrueRankingAsc);
	}
	public static void main(String[] args){
		/* test 1
		 * test find()
		 */
		Hand hand = new Hand(
				/*Card.randomDeck(13, false)*/
				/*new Card[]{d1, c1, d2, c2, d3, c3, d4, c5, c4, d5, s5, h5, h1}*/
				/*new Card[]{d1, c1, s1, h1, d2, s2, c2, h2, d3, d4, d5, d6, d7}*/
				new Card[]{d1, d2, d3, d4, d5, d6, d7, d8, d9, c1, c2, c3, c4}
				);
		hand.sort();
		System.out.println(hand.toString());
		System.out.println(hand.minTrueRankingCard().toString());
		
		/* test 2
		 * test availaleSingles() methods
		 */
		//for(int i=0; i<8; ++i){
		//	Hand hand = new Hand(Card.randomDeck(13, false));
		//	hand.sort();
		//	CardCombination oppo = new CardCombination(
		//			Card.randomDeck(1, false), 1);
		//	ArrayList<CardCombination> ccs = 
		//		hand.availableCardCombinations(oppo);
		//	System.out.println(hand.toString());
		//	System.out.println(oppo.toString());
		//	for(CardCombination cc : ccs){
		//		System.out.println(cc.toString());
		//	}
		//}
		
		/* test 3
		 * test availaleTriples() methods
		 */
		//for(int i=0; i<8; ++i){
		//	Hand hand = new Hand(Card.randomDeck(13, false));
		//	hand.sort();
		//	CardCombination oppo = new CardCombination(
		//			new Card[]{d9, c9, h9}, 3, CardCombinationType.TRIPLE);
		//	ArrayList<CardCombination> ccs = 
		//		hand.availableCardCombinations(oppo);
		//	System.out.println(hand.toString());
		//	System.out.println(oppo.toString());
		//	for(CardCombination cc : ccs){
		//		System.out.println(cc.toString());
		//	}
		//}
	}
}
