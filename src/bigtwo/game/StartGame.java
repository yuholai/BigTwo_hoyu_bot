package bigtwo.game;

public class StartGame {
	public static enum StartGameTrigger{
		PLAYER_FORCE_START,
		TEMPORAL,
		FULL_SEAT
	}
	
	public static enum StartGameResult{
		ACCEPT,
		REJECT_NOT_ENOUGH_PLAYER,
		REJECT_NOT_JOINED,
		FAILED_NO_GAME_IN_GROUP,
		REJECT_GAME_STARTED,
		SUCESSFULLY,
		FAILED_NOT_ENOUGH_PLAYER
	}
	
	static StartGame newStartGamePlayerForceStartAccept(Game game)
	{
		return new StartGame(
			StartGameTrigger.PLAYER_FORCE_START,
			StartGameResult.ACCEPT,
			game);
	}
	
	static StartGame newStartGamePlayerForceStartRejectNotEnoughPlayer(Game game){
		return new StartGame(
			StartGameTrigger.PLAYER_FORCE_START,
			StartGameResult.REJECT_NOT_ENOUGH_PLAYER,
			game);
	}
	
	static StartGame newStartGamePlayerForceStartRejectNotJoined(Game game){
		return new StartGame(
			StartGameTrigger.PLAYER_FORCE_START,
			StartGameResult.REJECT_NOT_JOINED,
			game);
	}
	
	static StartGame newStartGamePlayerForceStartFailedNoGameInGroup(){
		return new StartGame(
			StartGameTrigger.PLAYER_FORCE_START,
			StartGameResult.FAILED_NO_GAME_IN_GROUP,
			null);
	}
	
	static StartGame newStartGamePlayerForceStartRejectGameStarted(){
		return new StartGame(
			StartGameTrigger.PLAYER_FORCE_START,
			StartGameResult.REJECT_GAME_STARTED,
			null);
	}
	
	static StartGame newStartGameTemporalSucessfully(Game game)
	{
		return new StartGame(
			StartGameTrigger.TEMPORAL,
			StartGameResult.SUCESSFULLY,
			game);
	}
	
	static StartGame newStartGameTemporalFailedNotEnoughPlayer(Game game)
	{
		return new StartGame(
			StartGameTrigger.TEMPORAL,
			StartGameResult.FAILED_NOT_ENOUGH_PLAYER,
			game);
	}
	
	static StartGame newStartGameFullSeat(Game game){
		return new StartGame(
			StartGameTrigger.FULL_SEAT,
			null,
			game);
	}
	
	public final StartGameTrigger trigger  ;
	public final StartGameResult  result   ;
	public final Game             game     ;
	
	private StartGame
	(StartGameTrigger trigger  ,
	 StartGameResult  result   ,
	 Game             game     )
	{	this.trigger   = trigger  ;
		this.result    = result   ;
		this.game      = game     ;
	}
}
