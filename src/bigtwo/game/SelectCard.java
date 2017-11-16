package bigtwo.game;

public class SelectCard {
	public static SelectCard singleSelection(Play play, boolean changed, Handcard handcard){
		return new SelectCard(play, changed, handcard);
	}
	
	public static SelectCard multiSelection(Play play, boolean changed){
		return new SelectCard(play, changed, null);
	}
	
	public final Play     play    ;
	public final boolean  changed ;
	public final Handcard handcard;
	
	private SelectCard
	(Play     play    ,
	 boolean  changed ,
	 Handcard handcard)
	{
		this.play     = play    ;
		this.changed  = changed ;
		this.handcard = handcard;
	}

}
