package bigtwo.card;

import StringView.StringView;

public class Rank {
	public static final int
	RANK_A_RANK     =  1,
	RANK_2_RANK     =  2,
	RANK_3_RANK     =  3,
	RANK_4_RANK     =  4,
	RANK_5_RANK     =  5,
	RANK_6_RANK     =  6,
	RANK_7_RANK     =  7,
	RANK_8_RANK     =  8,
	RANK_9_RANK     =  9,
	RANK_10_RANK    = 10,
	RANK_J_RANK     = 11,
	RANK_Q_RANK     = 12,
	RANK_K_RANK     = 13,
	RANK_JOKER_RANK = 15;
	
	public static final int
	RANK_A_RANKING     = 11,
	RANK_2_RANKING     = 12,
	RANK_3_RANKING     =  0,
	RANK_4_RANKING     =  1,
	RANK_5_RANKING     =  2,
	RANK_6_RANKING     =  3,
	RANK_7_RANKING     =  4,
	RANK_8_RANKING     =  5,
	RANK_9_RANKING     =  6,
	RANK_10_RANKING    =  7,
	RANK_J_RANKING     =  8,
	RANK_Q_RANKING     =  9,
	RANK_K_RANKING     = 10,
	RANK_JOKER_RANKING = 13;
	
	public static final String
	RANK_A_TEXT     = "A" ,
	RANK_2_TEXT     = "2" ,
	RANK_3_TEXT     = "3" ,
	RANK_4_TEXT     = "4" ,
	RANK_5_TEXT     = "5" ,
	RANK_6_TEXT     = "6" ,
	RANK_7_TEXT     = "7" ,
	RANK_8_TEXT     = "8" ,
	RANK_9_TEXT     = "9" ,
	RANK_10_TEXT    = "10",
	RANK_J_TEXT     = "J" ,
	RANK_Q_TEXT     = "Q" ,
	RANK_K_TEXT     = "K" ,
	RANK_JOKER_TEXT	= "?" ;
	
	public static final Rank
	RANK_A     = new Rank(RANK_A_RANK    , RANK_A_RANKING    , RANK_A_TEXT    ),
	RANK_2     = new Rank(RANK_2_RANK    , RANK_2_RANKING    , RANK_2_TEXT    ),
	RANK_3     = new Rank(RANK_3_RANK    , RANK_3_RANKING    , RANK_3_TEXT    ),
	RANK_4     = new Rank(RANK_4_RANK    , RANK_4_RANKING    , RANK_4_TEXT    ),
	RANK_5     = new Rank(RANK_5_RANK    , RANK_5_RANKING    , RANK_5_TEXT    ),
	RANK_6     = new Rank(RANK_6_RANK    , RANK_6_RANKING    , RANK_6_TEXT    ),
	RANK_7     = new Rank(RANK_7_RANK    , RANK_7_RANKING    , RANK_7_TEXT    ),
	RANK_8     = new Rank(RANK_8_RANK    , RANK_8_RANKING    , RANK_8_TEXT    ),
	RANK_9     = new Rank(RANK_9_RANK    , RANK_9_RANKING    , RANK_9_TEXT    ),
	RANK_10    = new Rank(RANK_10_RANK   , RANK_10_RANKING   , RANK_10_TEXT   ),
	RANK_J     = new Rank(RANK_J_RANK    , RANK_J_RANKING    , RANK_J_TEXT    ),
	RANK_Q     = new Rank(RANK_Q_RANK    , RANK_Q_RANKING    , RANK_Q_TEXT    ),
	RANK_K     = new Rank(RANK_K_RANK    , RANK_K_RANKING    , RANK_K_TEXT    ),
	RANK_JOKER = new Rank(RANK_JOKER_RANK, RANK_JOKER_RANKING, RANK_JOKER_TEXT);
	
	public static final Rank[]
	RANKS = {
			RANK_A     ,
			RANK_2     ,
			RANK_3     ,
			RANK_4     ,
			RANK_5     ,
			RANK_6     ,
			RANK_7     ,
			RANK_8     ,
			RANK_9     ,
			RANK_10    ,
			RANK_J     ,
			RANK_Q     ,
			RANK_K     ,
			RANK_JOKER 	
	},
	RANKINGS = {
			RANK_3     ,
			RANK_4     ,
			RANK_5     ,
			RANK_6     ,
			RANK_7     ,
			RANK_8     ,
			RANK_9     ,
			RANK_10    ,
			RANK_J     ,
			RANK_Q     ,
			RANK_K     ,
			RANK_A     ,
			RANK_2     ,
			RANK_JOKER
	};
	
	public static int compareRankAsc(Rank rank1, Rank rank2){
		return rank1.rank - rank2.rank;
	}
	
	public static int compareRankDesc(Rank rank1, Rank rank2){
		return rank2.rank - rank1.rank;
	}
	
	public static int compareRankingAsc(Rank rank1, Rank rank2){
		return rank1.ranking - rank2.ranking;
	}
	
	public static int compareRankingDesc(Rank rank1, Rank rank2){
		return rank2.ranking - rank1.ranking;
	}
	
	public static Rank fromRank(int rank){
		if(1 <= rank && rank <= 13)
			return RANKS[rank-1];
		else if(rank == 15)
			return RANKS[13];
		else
			return null;
	}
	
	public static Rank fromRanking(int ranking){
		if(0 <= ranking && ranking <= 13)
			return RANKINGS[ranking];
		else
			return null;
	}
	
	public static Rank fromText(StringView text){
		// read rank number from text
		char chr = Character.toLowerCase(text.charAt(0));
		int rank;
		
		int offset = 1;
		
		if('2' <= chr && chr <= '9'){rank = chr - '0';}
		else if(chr == '1'){if(text.length() >= 2 && text.charAt(1) == '0'){rank = 10; offset = 2;}else{rank = -1;}}
		else if(chr == 'a'){rank = 1;}
		else if(chr == 'j'){rank = 11;}
		else if(chr == 'q'){rank = 12;}
		else if(chr == 'k'){rank = 13;}
		else {rank = -1;}
		
		text.begin += offset;
		
		
		// get rank object by rank number
		return fromRank(rank);
	}
	
	
	
	
	
	public final int rank, ranking;
	public final String text;
	
	
	
	private Rank(int rank, int ranking, String text){
		this.rank = rank;
		this.ranking = ranking;
		this.text = text;
	}
	
	
	
	@Override
	public String toString(){
		return 
		new StringBuilder()
		
		.append("Rank{")
		
		.append("rank:").append(rank)
		
		.append(",ranking:").append(ranking)
		
		.append(",text:").append(text)
		
		.append('}')
		
		.toString();
	}
	
	public String toString_simple(){
		return 
		new StringBuilder()
		
		.append("Rank{")
		
		.append("text:").append(text)
		
		.append(",...}")
		
		.toString();
	}
	
	
	
//	#debug
	public static void main(String[] args){
		String jokersymbol = "🃏";
		char[] chrs = jokersymbol.toCharArray();
		System.out.println(chrs.length);
	}
	
}
