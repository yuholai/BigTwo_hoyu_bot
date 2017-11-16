package bigtwo.game;

public class Join {
	public static enum JoinResult {
		ACCEPT,
		REJECT_GAME_STARTED,  
		REJECT_BUSY_USER,  
		REJECT_GAME_FULL,     
		REJECT_REDUNDANT,
		FAILED_NO_GAME_IN_GROUP
	}
	
	public final JoinResult result;
	public final Player player;
	public final Group group;
	public final Game game;
	public final Play play;
	
	Join
	(JoinResult result,
	 Player player, 
	 Game game,
	 Play play,
	 Group group)
	{
		this.result = result;
		this.player = player;
		this.game = game;
		this.play = play;
		this.group = group;
	}
}
