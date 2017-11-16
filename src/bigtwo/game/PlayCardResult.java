package bigtwo.game;

public class PlayCardResult {
	public static PlayCardResult win
	(PlayCard playcard, 
	 Game.GameResult result)
	{
		return new PlayCardResult(playcard, result);
	}
	
	public static PlayCardResult playcard
	(PlayCard playcard)
	{
		return new PlayCardResult(playcard, null);
	}
	
	public final PlayCard        playcard;
	public final Game.GameResult result  ;
	
	private PlayCardResult
	(PlayCard        playcard,
	 Game.GameResult result  )
	{
		this.playcard = playcard;
		this.result   = result  ;
	}
}
