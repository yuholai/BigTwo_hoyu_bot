package bigtwo.cardcombination;

import static bigtwo.cardcombination.CardCombinationType.FOUR_OF_A_KING;
import static bigtwo.cardcombination.CardCombinationType.FULL_HOUSE;

import java.util.ArrayList;

import bigtwo.card.Card;

public class Searcher {
	
	//private void availableSingles(CardCombination cc, ArrayList<CardCombination> r){
	//	Card pivot = cc.getCards()[0];
	//	int firstcardindex = 0;
	//	
	//	// binary spilt
	//	if(handcards.size() > 1){
	//		int a, z;
	//		a = 0;
	//		z = handcards.size() - 1;
	//		do{
	//			if(a == z){
	//				if(pivot.trueRanking >= handcards.get(a).card.trueRanking){
	//					firstcardindex = a + 1;
	//				}else{
	//					firstcardindex = a;
	//				}
	//				break;
	//			}
	//			
	//			int mid = (a + z)/2;
	//			if(pivot.trueRanking >= handcards.get(mid).card.trueRanking){
	//				a = mid + 1;
	//			}else{
	//				if(a == mid){
	//					firstcardindex = a;
	//					break;
	//				}else{
	//					z = mid - 1;
	//				}
	//			}
	//		}while(true);
	//	}
	//	
	//	for(int i=firstcardindex; i<handcards.size(); ++i){
	//		r.add(new CardCombination(new Card[]{handcards.get(i).card}, 1, CardCombinationType.SINGLE));
	//	}
	//}
	//private void availablePairs(CardCombination cc, ArrayList<CardCombination> r){
	//	Card pivot = cc.getCards()[1];
	//	
	//	int firstcardindex /*= 0*/;
	//	
	//	// binary spilt
	//	{
	//		int a, z;
	//		a = 0;
	//		z = handcards.size() - 1;
	//		do{
	//			if(a == z){
	//				if(pivot.getRank() > handcards.get(a).card.getRank()){
	//					firstcardindex = a + 1;
	//				}else{
	//					firstcardindex = a;
	//				}
	//				break;
	//			}
	//			
	//			int mid = (a + z)/2;
	//			if(pivot.trueRanking > handcards.get(mid).card.trueRanking){
	//				a = mid + 1;
	//			}else if(pivot.trueRanking < handcards.get(mid).card.trueRanking){
	//				if(a == mid){
	//					firstcardindex = a;
	//					break;
	//				}else{
	//					z = mid - 1;
	//				}
	//			}else{
	//				// traversal backwards
	//				if(mid>0 && handcards.get(mid-1).card.getRank()==pivot.getRank()){
	//					firstcardindex = mid - 1;
	//				}else{
	//					firstcardindex = mid;
	//				}
	//			}
	//		}while(true);
	//	}
	//	
	//	if(handcards.get(firstcardindex).card.getRank() == pivot.getRank() && 
	//		firstcardindex+1 < handcards.size() && 
	//		handcards.get(firstcardindex).card.getRank() == handcards.get(firstcardindex+1).card.getRank() &&
	//		handcards.get(firstcardindex).card.trueRanking > pivot.trueRanking)
	//	{
	//		r.add(new CardCombination(
	//			new Card[]{handcards.get(firstcardindex).card,  handcards.get(firstcardindex+1).card}, 
	//			2, CardCombinationType.PAIR));
	//		firstcardindex += 2;
	//		
	//	}
	//	for(int i=firstcardindex; i<handcards.size()-1; ++i){
	//		if(handcards.get(i).card.getRank() == handcards.get(i+1).card.getRank()){
	//			if(i+2 >= handcards.size()  ||  handcards.get(i+1).card.getRank() != handcards.get(i+2).card.getRank()){
	//				r.add(new CardCombination(new Card[]{handcards.get(i).card, handcards.get(i+1).card}, 2, CardCombinationType.PAIR));
	//				++i;
	//			}else if(i+3 >= handcards.size()||  handcards.get(i+2).card.getRank() != handcards.get(i+3).card.getRank()){
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card}, 2, CardCombinationType.PAIR));
	//				i+=2;
	//			}else{
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+3).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i+1).card, handcards.get(i+3).card}, 2, CardCombinationType.PAIR));
	//				r.add(new CardCombination(new Card[]{handcards.get(i+2).card, handcards.get(i+3).card}, 2, CardCombinationType.PAIR));
	//				i+=3;
	//			}
	//		}
	//	}
	//}
	//private void availableTriples(CardCombination cc, ArrayList<CardCombination> r){
	//	Card pivot = cc.getCards()[0];
	//	int firstcardindex /*= 0*/;
	//	
	//	// binary spilt
	//	{
	//		int a, z;
	//		a = 0;
	//		z = handcards.size() - 1;
	//		do{
	//			if(a == z){
	//				if(pivot.trueRanking > handcards.get(a).card.trueRanking){
	//					firstcardindex = a + 1;
	//				}else{
	//					firstcardindex = a;
	//				}
	//				break;
	//			}
	//			
	//			int mid = (a + z)/2;
	//			if(pivot.trueRanking > handcards.get(mid).card.trueRanking){
	//				a = mid + 1;
	//			}else{
	//				if(a == mid){
	//					firstcardindex = a;
	//					break;
	//				}else{
	//					z = mid - 1;
	//				}
	//			}
	//		}while(true);
	//	}
	//	
	//	for(int i=firstcardindex; i<handcards.size()-2; ++i){
	//		if(handcards.get(i).card.getRank() == handcards.get(i+1).card.getRank()){
	//			if(handcards.get(i+1).card.getRank() != handcards.get(i+2).card.getRank()){
	//				++i;
	//			}else if(i+3 >= handcards.size()  ||  handcards.get(i+2).card.getRank() != handcards.get(i+3).card.getRank()){
	//				r.add(new CardCombination(new Card[]{handcards.get(i).card, handcards.get(i+1).card, handcards.get(i+2).card}, 3, CardCombinationType.TRIPLE));
	//				i+=2;
	//			}else{
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card, handcards.get(i+2).card}, 3, CardCombinationType.TRIPLE));
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card, handcards.get(i+3).card}, 3, CardCombinationType.TRIPLE));
	//				r.add(new CardCombination(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card, handcards.get(i+3).card}, 3, CardCombinationType.TRIPLE));
	//				r.add(new CardCombination(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card, handcards.get(i+3).card}, 3, CardCombinationType.TRIPLE));
	//				i+=3;
	//			}
	//		}
	//	}
	//}
	//private void availableStraights(CardCombination cc, ArrayList<CardCombination> r){
	//	//
	//	availableFullHouses(cc, r);
	//	availableFourOfAKings(cc, r);
	//}
	//private void availableFlushes(CardCombination cc, ArrayList<CardCombination> r){
	//	//
	//	availableFullHouses(cc, r);
	//	availableFourOfAKings(cc, r);
	//}
	//private void availableFullHouses(CardCombination cc, ArrayList<CardCombination> r){
	//	ArrayList<Card[]>
	//		pairs = new ArrayList<>(),
	//		triples = new ArrayList<>();
	//	// find pairs and triples
	//	for(int i=0, end=handcards.size()-1; i<end; ++i){
	//		if(handcards.get(i).card.getRank() == handcards.get(i+1).card.getRank()){
	//			if(i+2 >= handcards.size()  ||  handcards.get(i+1).card.getRank() != handcards.get(i+2).card.getRank()){
	//				pairs.add(new Card[]{handcards.get(i).card, handcards.get(i+1).card});
	//				++i;
	//			}else if(i+3 >= handcards.size()  ||  handcards.get(i+2).card.getRank() != handcards.get(i+3).card.getRank()){
	//				pairs.add(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card});
	//				pairs.add(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card});
	//				pairs.add(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card});
	//				triples.add(new Card[]{handcards.get(i).card, handcards.get(i+1).card, handcards.get(i+2).card});
	//				i+=2;
	//			}else{
	//				pairs.add(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card});
	//				pairs.add(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card});
	//				pairs.add(new Card[]{handcards.get(i  ).card, handcards.get(i+3).card});
	//				pairs.add(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card});
	//				pairs.add(new Card[]{handcards.get(i+1).card, handcards.get(i+3).card});
	//				pairs.add(new Card[]{handcards.get(i+2).card, handcards.get(i+3).card});
	//				triples.add(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card, handcards.get(i+2).card});
	//				triples.add(new Card[]{handcards.get(i  ).card, handcards.get(i+1).card, handcards.get(i+3).card});
	//				triples.add(new Card[]{handcards.get(i  ).card, handcards.get(i+2).card, handcards.get(i+3).card});
	//				triples.add(new Card[]{handcards.get(i+1).card, handcards.get(i+2).card, handcards.get(i+3).card});
	//				i+=3;
	//			}
	//		}
	//	}
	//	int firsttripleindex = 0;
	//	Card pivot = cc.getCards()[2];
	//	if(cc.getType() == FULL_HOUSE){
	//		// binary spilt
	//		{
	//			int a, z;
	//			a = 0;
	//			z = handcards.size() - 1;
	//			do{
	//				if(a == z){
	//					if(pivot.trueRanking > handcards.get(a).card.trueRanking){
	//						firsttripleindex = a + 1;
	//					}else{
	//						firsttripleindex = a;
	//					}
	//					break;
	//				}
	//				
	//				int mid = (a + z)/2;
	//				if(pivot.trueRanking > handcards.get(mid).card.trueRanking){
	//					a = mid + 1;
	//				}else{
	//					if(a == mid){
	//						firsttripleindex = a;
	//						break;
	//					}else{
	//						z = mid - 1;
	//					}
	//				}
	//			}while(true);
	//		}
	//	}
	//	// find full house
	//	for(int i=firsttripleindex; i<triples.size(); ++i){
	//		Card[] triple = triples.get(i);
	//		for(Card[] pair : pairs){
	//			if(triple[0].getRank() != pair[0].getRank()){
	//				r.add(new CardCombination(
	//					new Card[]{triple[0], triple[1], triple[2], pair[0], pair[1]},
	//					5,
	//					FULL_HOUSE));
	//			}
	//		}
	//	}
	//	
	//	
	//	//
	//	availableFourOfAKings(cc, r);
	//}
	//private void availableFourOfAKings(CardCombination cc, ArrayList<CardCombination> r){
	//	ArrayList<Card[]> quadruples = new ArrayList<>();
	//	// find quadruples
	//	for(int i=0, end=handcards.size()-3; i<end; ++i){
	//		if(handcards.get(i).card.getRank() == handcards.get(i+1).card.getRank()){
	//			if(i+2 >= handcards.size()  ||  handcards.get(i+1).card.getRank() != handcards.get(i+2).card.getRank()){
	//				++i;
	//			}else if(i+3 >= handcards.size()  ||  handcards.get(i+2).card.getRank() != handcards.get(i+3).card.getRank()){
	//				i+=2;
	//			}else{
	//				quadruples.add(new Card[]{handcards.get(i).card, handcards.get(i+1).card, handcards.get(i+2).card, handcards.get(i+3).card});
	//				i+=3;
	//			}
	//		}
	//	}
	//	int firstquadrupleindex = 0;
	//	Card pivot = cc.getCards()[3];
	//	if(cc.getType() == FOUR_OF_A_KING){
	//		// binary spilt
	//		if(handcards.size() > 0){
	//			int a, z;
	//			a = 0;
	//			z = handcards.size() - 1;
	//			do{
	//				if(a == z){
	//					if(pivot.trueRanking > handcards.get(a).card.trueRanking){
	//						firstquadrupleindex = a + 1;
	//					}else{
	//						firstquadrupleindex = a;
	//					}
	//					break;
	//				}
	//				
	//				int mid = (a + z)/2;
	//				if(pivot.trueRanking > handcards.get(mid).card.trueRanking){
	//					a = mid + 1;
	//				}else{
	//					if(a == mid){
	//						firstquadrupleindex = a;
	//						break;
	//					}else{
	//						z = mid - 1;
	//					}
	//				}
	//			}while(true);
	//		}
	//	}
	//	// find four of a king
	//	for(int i=firstquadrupleindex; i<quadruples.size(); ++i){
	//		Card[] quadruple = quadruples.get(i);
	//		for(int j=0; j<handcards.size(); ++j){
	//			if(quadruple[0].getRank() != handcards.get(i).card.getRank()){
	//				r.add(new CardCombination(
	//						new Card[]{quadruple[0], quadruple[1], quadruple[2], quadruple[3], handcards.get(i).card},
	//						5,
	//						CardCombinationType.FOUR_OF_A_KING));
	//			}else{
	//				i+=3;
	//			}
	//		}
	//	}
	//	 
	//	//
	//	
	//}
	//private void availableStraightFlushes(CardCombination cc, ArrayList<CardCombination> r){
	//	
	//}
	//
	//public ArrayList<CardCombination> availableCardCombinations(CardCombination cc){
	//	ArrayList<CardCombination> r = new ArrayList<>();
	//	
	//	if(cc.getType() == CardCombinationType.SINGLE){
	//		availableSingles(cc, r);
	//	}else if(cc.getType() == CardCombinationType.PAIR){
	//		availablePairs(cc, r);
	//	}else if(cc.getType() == CardCombinationType.TRIPLE){
	//		availableTriples(cc, r);
	//	}else if(cc.getType() == CardCombinationType.STRAIGHT){
	//		availableStraights(cc, r);
	//	}else if(cc.getType() == CardCombinationType.FLUSH){
	//		availableFlushes(cc, r);
	//	}else if(cc.getType() == CardCombinationType.FULL_HOUSE){
	//		availableFullHouses(cc, r);
	//	}else if(cc.getType() == CardCombinationType.FOUR_OF_A_KING){
	//		availableFourOfAKings(cc, r);
	//	}else if(cc.getType() == CardCombinationType.STRAIGHT_FLUSH){
	//		availableStraightFlushes(cc, r);
	//	}
	//	
	//	return r;
	//}
	
}
