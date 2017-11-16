package bigtwo.game;

public class Pass {
	public static Pass pass(Play play, PassResult result){
		return new Pass(play, result);
	}
	
	public static enum PassResult {
		ACCEPT_NORMAL,
		ACCEPT_NEW_LEADER,
		ACCEPT_ELDEST_HAND,
		REJECT_NOT_CURRENT_PLAYER
	}
	
	public final Play       play;
	public final PassResult result;
	
	private Pass
	(Play       play,
	 PassResult result)
	{
		this.play = play;
		this.result = result;
	}
}
