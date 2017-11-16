package bigtwo.game;

public class GameStatus {
	/* forming - forming a group to play 
	 * playing - playing
	 * 		beginning - begin
	 * 		afterBeginning - after first cards sent
	 * ended - 
	 * 		win - a player win
	 * 		terminated - 
	 */
	/* ###
	 * ##-	6				--#	1
	 * 00-	0	forming		
	 * 01-	2	playing		010	2	beginning
	 *  					011	3	afterBeginning
	 * 10-	4	ended		100	4	win			
	 * 						101	5	terminated	
	 */
	
	private static final int
	FILTER0 = 7,
	FILTER1 = 6;
	
	private static final int
	FORMING_VALUE = 0,
	PLAYING_VALUE = 2,
	PLAYING_BEGINNING_VALUE = 2,
	PLAYING_AFTERBEGINNING_VALUE = 3,
	ENDED_VALUE = 4,
	ENDED_WIN_VALUE = 4,
	ENDED_TERMINATED_VALUE = 5; 
	
	public static GameStatus
	FORMING = new GameStatus(FORMING_VALUE),
	PLAYING_BEGINNING = new GameStatus(PLAYING_BEGINNING_VALUE),
	PLAYING_AFTERBEGINNING = new GameStatus(PLAYING_AFTERBEGINNING_VALUE),
	ENDED_WIN = new GameStatus(ENDED_WIN_VALUE),
	ENDED_TERMINATED = new GameStatus(ENDED_TERMINATED_VALUE);
	
	
	private int _value;
	
	private GameStatus(int value){
		_value = value;
	}
	
	public boolean isForming(){
		return _value == FORMING_VALUE;
	}
	
	public boolean isPlaying(){
		return (_value & FILTER1) == PLAYING_VALUE;
	}
	public boolean isBeginning(){
		return _value == PLAYING_BEGINNING_VALUE;
	}
	public boolean isAfterBeginning(){
		return _value == PLAYING_AFTERBEGINNING_VALUE;
	}
	
	public boolean isEnded(){
		return (_value & FILTER1) == ENDED_VALUE;
	}
	public boolean isWin(){
		return _value == ENDED_WIN_VALUE;
	}
	public boolean isTerminated(){
		return _value == ENDED_TERMINATED_VALUE;
	}
}
