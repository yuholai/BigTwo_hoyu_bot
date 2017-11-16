package bigtwo.game;

public class NewGame {
	public final Player initiator;
	public final Group group;
	public static enum NewGameResult{
		ACCEPT,
		REJECT_BUSY_USER,
		REJECT_BUSY_GROUP,
		REJECT_BUSY_BOT
	}
	public final NewGameResult result;
	public final Game game;
	public final Play play;
	
	NewGame
	(Player initiator, Group group, NewGameResult result, Game game, Play autojoin)
	{
		this.initiator = initiator;
		this.group = group;
		this.result = result;
		this.game = game;
		this.play = autojoin;
	}
}
