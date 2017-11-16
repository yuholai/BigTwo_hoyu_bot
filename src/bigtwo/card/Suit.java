package bigtwo.card;

public class Suit {
	public static final int
	DIAMONDS_VALUE		= 0,
	CLUBS_VALUE			= 1,
	HEARTS_VALUE		= 2,
	SPADES_VALUE		= 3,
	JOKERS_VALUE	    = 4;
	
	public static final char
	DIAMONDS_SYMBOL = '♦',
	CLUBS_SYMBOL    = '♣',
	HEARTS_SYMBOL   = '♥',
	SPADES_SYMBOL   = '♠',
	JOKERS_SYMBOL   = '?';
	
	public static final char
	DIAMONDS_ALPHABET = 'D',
	CLUBS_ALPHABET    = 'C',
	HEARTS_ALPHABET   = 'H',
	SPADES_ALPHABET   = 'S',
	JOKERS_ALPHABET   = 'J';
	
	public static final String
	DIAMONDS_NAME = "DIAMONDS",
	CLUBS_NAME    = "CLUBS"   ,
	HEARTS_NAME   = "HEARTS"  ,
	SPADES_NAME   = "SPADES"  ,
	JOKERS_NAME   = "JOKERS"  ;
	
	public static final String
	DIAMONDS_EMOJI = "\u2666\uFE0F",
	CLUBS_EMOJI    = "\u2663\uFE0F",
	HEARTS_EMOJI   = "\u2665\uFE0F",
	SPADES_EMOJI   = "\u2660\uFE0F",
	JOKERS_EMOJI   = "\uD83C\uDCCF";
	
	public static final Suit
	DIAMONDS = new Suit(DIAMONDS_VALUE, DIAMONDS_SYMBOL, DIAMONDS_ALPHABET, DIAMONDS_NAME, DIAMONDS_EMOJI),
	CLUBS    = new Suit(CLUBS_VALUE, CLUBS_SYMBOL, CLUBS_ALPHABET, CLUBS_NAME, CLUBS_EMOJI),
	HEARTS   = new Suit(HEARTS_VALUE, HEARTS_SYMBOL, HEARTS_ALPHABET, HEARTS_NAME, HEARTS_EMOJI),
	SPADES   = new Suit(SPADES_VALUE, SPADES_SYMBOL, SPADES_ALPHABET, SPADES_NAME, SPADES_EMOJI),
	JOKERS   = new Suit(JOKERS_VALUE, JOKERS_SYMBOL, JOKERS_ALPHABET, JOKERS_NAME, JOKERS_EMOJI);
	
	public static int compareSuitAsc(Suit a, Suit b){
		return a.value - b.value;
	}
	public static int compareSuitDesc(Suit a, Suit b){
		return b.value - a.value;
	}
	
	public static Suit fromSymbol(char symbol)
	{
		if(symbol == DIAMONDS_SYMBOL){return DIAMONDS;}
		else if(symbol == CLUBS_SYMBOL){return CLUBS;}
		else if(symbol == HEARTS_SYMBOL){return HEARTS;}
		else if(symbol == SPADES_SYMBOL){return SPADES;}
		else {return null;}
	}
	
	public static Suit fromAlphabet(char alphab)
	{
		alphab = Character.toUpperCase(alphab);
		switch(alphab)
		{
		case DIAMONDS_ALPHABET: return DIAMONDS;
		case CLUBS_ALPHABET   : return CLUBS;
		case HEARTS_ALPHABET  : return HEARTS;
		case SPADES_ALPHABET  : return SPADES;
		case JOKERS_ALPHABET  : return JOKERS;
		default: return null;
		}
	}
	
	public static Suit fromName(String name)
	{
		name = name.toUpperCase();
		if(name.equals(DIAMONDS_NAME)){return DIAMONDS;}
		else if(name.equals(CLUBS_NAME)){return CLUBS;}
		else if(name.equals(HEARTS_NAME)){return HEARTS;}
		else if(name.equals(SPADES_NAME)){return SPADES;}
		else if(name.equals(JOKERS_NAME)){return JOKERS;}
		else {return null;}
	}
	
	public static Suit fromEmoji(String emoji)
	{
		if(emoji.equals(DIAMONDS_EMOJI)){return DIAMONDS;}
		else if(emoji.equals(CLUBS_EMOJI)){return CLUBS;}
		else if(emoji.equals(HEARTS_EMOJI)){return HEARTS;}
		else if(emoji.equals(SPADES_EMOJI)){return SPADES;}
		else if(emoji.equals(JOKERS_EMOJI)){return JOKERS;}
		else {return null;}
	}
	
	public static Suit from(String str)
	{
		Suit r;
		
		if(str.length() == 1)
		{
			char chr = str.charAt(0);
			
			r = fromSymbol(chr);
			if(r == null)	r = fromAlphabet(chr);
		}
		else
		{
			r = fromName(str);
			if(r == null)	r = fromEmoji(str);
		}
		
		return r;
	}
	
	
	
	
	
	public final int value;
	public final char symbol;
	public final char alphabet;
	public final String text;
	public final String emoji;
	
	
	
	private Suit(int value, char symbol, char alphabet, String text, String emoji)
	{
		this.value = value;
		this.symbol = symbol;
		this.alphabet = alphabet;
		this.text = text;
		this.emoji = emoji;
	}
	
	
	
	/*public boolean equals(Suit suit1)
	{
		return super.equals(suit1);
	}*/
	
	
	@Override
	public String toString()
	{
		return
		new StringBuilder(32)
		
		.append("Suit{")
		
		.append("text:").append(text)
		
		.append(",alphabet:").append(alphabet)
		
		.append(",value:").append(value)
		
		.append(",symbol:").append(symbol)
		
		.append(",emoji:").append(emoji)
		
		.append('}')
		
		.toString();
	}
	
	public String toString_simple()
	{
		return
		new StringBuilder(32)
		
		.append("Suit{")
		
		.append("alphabet:").append(alphabet)
		
		.append(",...}")
		
		.toString();
	}
}
