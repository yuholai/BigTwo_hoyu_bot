package bigtwo.card;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import StringView.StringView;

public class Card
{
//	static constants
	
	public final static String EMOJI_FLOWER_PLAYING_CARDS = "\uD83C\uDFB4";
	
	public final static char
	BLACKJOKER_ALPHABET = 'X',
	REDJOKER_ALPHABET   = 'Z';
	
	public final static Card
	d1  = new Card(Rank.RANK_A , Suit.DIAMONDS),
	c1  = new Card(Rank.RANK_A , Suit.CLUBS   ),
	h1  = new Card(Rank.RANK_A , Suit.HEARTS  ),
	s1  = new Card(Rank.RANK_A , Suit.SPADES  ),
	d2  = new Card(Rank.RANK_2 , Suit.DIAMONDS),
	c2  = new Card(Rank.RANK_2 , Suit.CLUBS   ),
	h2  = new Card(Rank.RANK_2 , Suit.HEARTS  ),
	s2  = new Card(Rank.RANK_2 , Suit.SPADES  ),
	d3  = new Card(Rank.RANK_3 , Suit.DIAMONDS),
	c3  = new Card(Rank.RANK_3 , Suit.CLUBS   ),
	h3  = new Card(Rank.RANK_3 , Suit.HEARTS  ),
	s3  = new Card(Rank.RANK_3 , Suit.SPADES  ),
	d4  = new Card(Rank.RANK_4 , Suit.DIAMONDS),
	c4  = new Card(Rank.RANK_4 , Suit.CLUBS   ),
	h4  = new Card(Rank.RANK_4 , Suit.HEARTS  ),
	s4  = new Card(Rank.RANK_4 , Suit.SPADES  ),
	d5  = new Card(Rank.RANK_5 , Suit.DIAMONDS),
	c5  = new Card(Rank.RANK_5 , Suit.CLUBS   ),
	h5  = new Card(Rank.RANK_5 , Suit.HEARTS  ),
	s5  = new Card(Rank.RANK_5 , Suit.SPADES  ),
	d6  = new Card(Rank.RANK_6 , Suit.DIAMONDS),
	c6  = new Card(Rank.RANK_6 , Suit.CLUBS   ),
	h6  = new Card(Rank.RANK_6 , Suit.HEARTS  ),
	s6  = new Card(Rank.RANK_6 , Suit.SPADES  ),
	d7  = new Card(Rank.RANK_7 , Suit.DIAMONDS),
	c7  = new Card(Rank.RANK_7 , Suit.CLUBS   ),
	h7  = new Card(Rank.RANK_7 , Suit.HEARTS  ),
	s7  = new Card(Rank.RANK_7 , Suit.SPADES  ),
	d8  = new Card(Rank.RANK_8 , Suit.DIAMONDS),
	c8  = new Card(Rank.RANK_8 , Suit.CLUBS   ),
	h8  = new Card(Rank.RANK_8 , Suit.HEARTS  ),
	s8  = new Card(Rank.RANK_8 , Suit.SPADES  ),
	d9  = new Card(Rank.RANK_9 , Suit.DIAMONDS),
	c9  = new Card(Rank.RANK_9 , Suit.CLUBS   ),
	h9  = new Card(Rank.RANK_9 , Suit.HEARTS  ),
	s9  = new Card(Rank.RANK_9 , Suit.SPADES  ),
	d10 = new Card(Rank.RANK_10, Suit.DIAMONDS),
	c10 = new Card(Rank.RANK_10, Suit.CLUBS   ),
	h10 = new Card(Rank.RANK_10, Suit.HEARTS  ),
	s10 = new Card(Rank.RANK_10, Suit.SPADES  ),
	d11 = new Card(Rank.RANK_J , Suit.DIAMONDS),
	c11 = new Card(Rank.RANK_J , Suit.CLUBS   ),
	h11 = new Card(Rank.RANK_J , Suit.HEARTS  ),
	s11 = new Card(Rank.RANK_J , Suit.SPADES  ),
	d12 = new Card(Rank.RANK_Q , Suit.DIAMONDS),
	c12 = new Card(Rank.RANK_Q , Suit.CLUBS   ),
	h12 = new Card(Rank.RANK_Q , Suit.HEARTS  ),
	s12 = new Card(Rank.RANK_Q , Suit.SPADES  ),
	d13 = new Card(Rank.RANK_K , Suit.DIAMONDS),
	c13 = new Card(Rank.RANK_K , Suit.CLUBS   ),
	h13 = new Card(Rank.RANK_K , Suit.HEARTS  ),
	s13 = new Card(Rank.RANK_K , Suit.SPADES  ),
	jb  = new Card(Boolean.FALSE),		//black joker
	jR  = new Card(Boolean.TRUE);		//red joker
	
	public final static Card[] deck = {
		d1 , 
		c1 ,
		h1 ,
		s1 ,
		d2 ,
		c2 ,
		h2 ,
		s2 ,
		d3 ,
		c3 ,
		h3 ,
		s3 ,
		d4 ,
		c4 ,
		h4 ,
		s4 ,
		d5 ,
		c5 ,
		h5 ,
		s5 ,
		d6 ,
		c6 ,
		h6 ,
		s6 ,
		d7 ,
		c7 ,
		h7 ,
		s7 ,
		d8 ,
		c8 ,
		h8 ,
		s8 ,
		d9 ,
		c9 ,
		h9 ,
		s9 ,
		d10,
		c10,
		h10,
		s10,
		d11,
		c11,
		h11,
		s11,
		d12,
		c12,
		h12,
		s12,
		d13,
		c13,
		h13,
		s13,
		jb ,
		jR
	};

	
	
//	static methods
	public static int compareTrueRankingAsc(Card a, Card b){
		return a.trueRanking - b.trueRanking;
	}
	
	public static int compareTrueRankingDesc(Card a, Card b){
		return b.trueRanking - a.trueRanking;
	}
	
	public static int compareRankAsc(Card a, Card b){
		return Rank.compareRankAsc(a.rank, b.rank);
	}
	
	public static int compareRankDesc(Card a, Card b){
		return Rank.compareRankDesc(a.rank, b.rank);
	}
	
	public static int compareRankingAsc(Card a, Card b){
		return Rank.compareRankingAsc(a.rank, b.rank);
	}
	
	public static int compareRankingDesc(Card a, Card b){
		return Rank.compareRankingDesc(a.rank, b.rank);
	}
	
	public static int compareSuitAsc(Card a, Card b){
		return Suit.compareSuitAsc(a.suit, b.suit);
	}
	public static int compareSuitDesc(Card a, Card b){
		return Suit.compareSuitDesc(a.suit, b.suit);
	}
	
	

	public static Card[] randomDeck(int size, boolean includingJoker){
		Card[] randList = new Card[size];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int max_rand = (includingJoker)?(54):(52);
		LinkedList<Card> ll = new LinkedList<>(Arrays.asList(deck));
		
		for(int i=0; i<size; ++i, --max_rand){
			int rand = random.nextInt(max_rand);
			randList[i] = ll.remove(rand);
		}
		
		return randList;
	}
	
	public static Card fromText(String str){
		return fromText(new StringView(str));
	}
	
	public static Card fromText(StringView text){
		if(text.length() > 0){
			// read rank
			
			if(text.length() == 1){
				// jokers
				char chr0 = Character.toUpperCase(text.charAt(0));
				if(chr0 == BLACKJOKER_ALPHABET){
					return jb;
				}else if(chr0 == REDJOKER_ALPHABET){
					return jR;
				}else{
					return null;
				}
			}else if(text.length() > 1){
				// non-jokers
				// read rank
				char chr0 = text.charAt(0);
				Rank rank;
				if(chr0 != '?'){
					rank = Rank.fromText(text);
					
					// read suit
					Suit suit;
					if(rank != null && text.length() > 0){
						suit = Suit.from(text.toString());
						
						if(suit != null){
							return deck[(rank.rank - 1)*4 + suit.value];
						}else{
							return null;
						}
					}else{
						return null;
					}
				}else{
					return null;
				}
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	
//	instance variables
	public final Suit suit;
	public final Rank rank;
	public final int trueRanking;
	public final Boolean red;		// red joker
	
	
	
//	constructors
	private Card(Rank rank, Suit suit, Boolean red){
		this.rank = rank;
		this.suit = suit;
		if(red == null || red == Boolean.FALSE)
			this.trueRanking = rank.ranking * 4 + suit.value;
		else
			this.trueRanking = rank.ranking * 4 + suit.value + 1;
		this.red = red;
	}
	
	private Card(Rank rank, Suit suit){
		this(rank, suit, null);
	}
	
	private Card(Boolean red){
		this(Rank.RANK_JOKER, Suit.JOKERS, red);
	}
	
	
	
//	instance methods
	

	@Override
	public String toString(){
		return
		new StringBuilder()
		.append("Card{rank:").append(rank)
		.append(",suit:").append(suit)
		.append(",trueRanking:").append(trueRanking)
		.append(",red:").append(red)
		.append("}")
		.toString();
	}
	
	
	
//	note
	/* RKM representation
	 * a card 2x8 
	 * pair 3
	 * triple 2
	 * 5 1
	 * fullhoust 2
	 */

	
	
//	#debug
	public static boolean check(Card card){
		return card.rank.ranking*4+card.suit.value == card.trueRanking;
	}
	
	public static void main(String[] args){
		/* test 1
		 * checking point matching rank and colour
		 */
		//for(int i=0; i<54; ++i){
		//	System.out.println(deck[i] + " " + check(deck[i]));
		//}
		//for(int i=0; i<54; ++i){
		//	System.out.println(deck[i].rank.ranking*4 + deck[i].suit.value);
		//}
		
		/* test 2
		 * comperator
		 */
		// test case 1
		Card[] cards1 = {d1, s5};
		Arrays.sort(cards1, Card::compareTrueRankingAsc);
		System.out.println(cards1[0]);
		// test case 2
		Card[] cards2 = {d10, s10, c10, d9, h10};
		Arrays.sort(cards2, Card::compareTrueRankingAsc);
		for(Card card : cards2)
			System.out.println(card.toString());
	}
}