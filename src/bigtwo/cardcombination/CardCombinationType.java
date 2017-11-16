package bigtwo.cardcombination;

public class CardCombinationType {
	public static final CardCombinationType
		SINGLE			= new CardCombinationType("SINGLE"			),
		PAIR			= new CardCombinationType("PAIR"			),
		TRIPLE			= new CardCombinationType("TRIPLE"			),
		STRAIGHT		= new CardCombinationType("STRAIGHT"		),
		FLUSH			= new CardCombinationType("FLUSH"			),
		FULL_HOUSE		= new CardCombinationType("FULL_HOUSE"		),
		FOUR_OF_A_KING	= new CardCombinationType("FOUR_OF_A_KING"	),	// also named four_of_a_king_plus_one_card
		STRAIGHT_FLUSH	= new CardCombinationType("STRAIGHT_FLUSH"	),
		INVALID			= new CardCombinationType("INVALID"			);
	
	private final String _name;
	
	private CardCombinationType(String name){_name = name;}
	
	@Override
	public String toString(){
		return "CardCombinationType:{" + _name + ";}";
	}
	
	
	
//	#debug
	public static void main(String[] args){
		
	}
}
