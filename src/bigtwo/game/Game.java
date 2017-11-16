package bigtwo.game;

import java.util.Arrays;
import java.util.HashMap;

import static bigtwo.game.GameStatus.*;

import bot.BigTwoBot;
import bigtwo.card.Card;

public class Game {
	public static class GameResult{
		public final Play winner;
		public final Play[] loser;
		
		public GameResult(Play winner, Play[] loser){
			this.winner = winner;
			this.loser = loser;
		}
	}
	
//	static constants
	
	// game parameters
	/*The maximum no. of plays can join in a game*/
	public static final int MAX_PLAY = 4;
	
	// The maximun no. of games can be hold by bot
	public static final int MAX_GAME = 10;
	

	
//	static fields:
	private static HashMap<Integer, Game>
	games = new HashMap<>();
	
	
	
//	static methods
	protected static int comparatorPlayMinimunCardAsc(Play play1, Play play2){
		return play1.minTrueRankingCard().trueRanking - play2.minTrueRankingCard().trueRanking;
	}
	
	
	public static void cache(Game game){
		games.put(game.gameid, game);
	}
	
	public static Game getGame(Integer gameid){
		return games.get(gameid);
	}
	
	public static Game uncache(Integer gameid){
		return games.remove(gameid);
	}
	
	public static Game uncache(Game game){
		return games.remove(game.gameid);
	}
	
	public static boolean reachMaxGame(){return games.size() >= MAX_GAME;}
	
	public static Game newGame(GameSettings settings){
		Integer newgameid = obtainNextGameId();
		Game newgame = new Game(newgameid, settings);
		occupyGameId();
		games.put(newgameid, newgame);
		return newgame;
	}
	
	private static int obtainNextGameId(){
		return BigTwoBot.bot().obtainNextGameId();
	}
	
	private static void occupyGameId(){
		BigTwoBot.bot().occupyGameId();
	}
	

	
//	instance fields	
	// player
	protected Play[] plays;
	protected int nPlay;
	
	protected GameStatus status;
	
	protected Play[] positions;
	protected int positionIndex;
	
	protected Play currentPlay;
	protected PlayCard previousPlayCard;
	protected boolean flagNewLeader;
	
	protected GameHistory history;
	
	public final Integer gameid;
	
	private Group group;
	
	private GameSettings settings;
	
	long playeractionstarttimes;
	int votes;
	

	
//	constructors
	private Game(Integer gameid, GameSettings settings){
		this.gameid = gameid;
		
		plays = new Play[MAX_PLAY];
		nPlay = 0;
		
		this.settings = settings;
		status = GameStatus.FORMING;
	}
	
	
	
	
	
// instance methods
	// getters
	public Play[] getSeats(){
		return plays;
	}
	
	public int getNPlay(){
		return nPlay;
	}
	
	public Play[] getPlays(){
		Play[] r = new Play[nPlay];
		for(int i=0, j=0; i<MAX_PLAY; ++i, ++j)
			if(plays[i] != null)
				r[j] = plays[i];
		return r;
	}

	public Play[] getPositions(){
		return positions;
	}
	
	public Play getEldestHand(){
		return positions[positionIndex];
	}
	
	public Play getCurrentPlay(){
		return currentPlay;
	}
	
	public PlayCard getPreviousPlayCard(){
		return previousPlayCard;
	}
	
	public boolean getFlagNewLeader(){
		return flagNewLeader;
	}

	public GameStatus getStatus(){
		return status;
	}
	
	public GameSettings getSettings(){
		return settings;
	}
	
	public int getSkipVote(){
		return votes;
	}
	
	public int getNFulfillVoteSkip(){
		return nPlay-1;
	}
	
	public Group getGroup(){
		return group;
	}
	
	public GameHistory getHistory(){
		return history;
	}
	
	// setters
	
	
	// game related
	public boolean isFull(){
		return nPlay >= MAX_PLAY;
	}
	
	public int nextEmptyPlayIndex(){
		for(int i=0; i<MAX_PLAY; ++i)
			if(plays[i]==null)
				return i;
		return -1;
	}
	
	public boolean isEnoughPlayers(){
		return nPlay >= 2;
	}
	
	boolean makeEasterEgg(){
		//final float prob = 0.7f;
		//if(rule.EasterEgg){
		//	return Math.random() * prob > 0.5;
		//}else{
		//	return false;
		//}
		return true;
	}

	protected void nextEldestHand(){
		++positionIndex;
		currentPlay = positions[positionIndex];
		
		playeractionstarttimes = System.currentTimeMillis();
		
		votes = -999;
	}
	
	protected void nextPlayer(){
		// find next play
		for(int i=(currentPlay.playindex+1)%MAX_PLAY;
				i!=currentPlay.playindex;
				i=(i+1)%MAX_PLAY)
		{
			if(plays[i]!=null){
				currentPlay = plays[i];
				if(previousPlayCard.sender == currentPlay)
					flagNewLeader = true;
				break;
			}
		}
		
		playeractionstarttimes = System.currentTimeMillis();
		
		votes = -999;
	}
	
	/*public GameResult gameresult(){
		Play winner = null;
		Play[] loser = new Play[nPlay-1];
		
		for(int i=0, j=0; i<MAX_PLAY; ++i){
			Play play = plays[i];
			if(play != null){
				if(play.getWinStatus() == WinStatus.WON){
					winner = play;
				}else if(play.getWinStatus() == WinStatus.LOST){
					loser[j] = play;
					++j;
				}
			}
		}
		
		return new GameResult(winner, loser);
	}*/

	public void setGroup(Group group){
		this.group = group;
	}
	
		
	// interaction
	public void acceptJoin(Play newplay){
		plays[newplay.playindex] = newplay;
		++nPlay;
	}
	
	public void removePlay(Play play){
		plays[play.playindex] = null;
		--nPlay;
	}
	
	public void recordLeave(Play play){
		history.recordLeave(play);
	}
	
	public void start(){
		status = PLAYING_BEGINNING;
		previousPlayCard = null;
		
		
		// shuttling
		Card[] randdeck = Card.randomDeck(52, false);
		
		// dealing
		Card[][] hands = new Card[MAX_PLAY][];
		for(int i=0; i<MAX_PLAY; ++i)
			if(plays[i]!=null)
				hands[i] = new Card[settings.getNCard()];
		int k = 0;
		for(int i=0; i<settings.getNCard(); ++i){
			for(int j=0; j<MAX_PLAY; ++j)
				if(plays[j]!=null){
					hands[j][i] = randdeck[k];
					++k;
				}
		}
		for(int i=0; i<MAX_PLAY; ++i){
			Play play = plays[i];
			if(play!=null){
				play.notifyGameStarted(hands[i]);
			}
		}
		
		// generate positions
		positions = new Play[nPlay];
		int i = 0;
		for(Play play : plays){
			if(play != null){
				positions[i] = play;
				++i;
			}
		}
		Arrays.sort(positions, Game::comparatorPlayMinimunCardAsc);
		positionIndex = 0;
		
		// set up eldest hand
		currentPlay = positions[0];
		flagNewLeader = true;
		
		playeractionstarttimes = System.currentTimeMillis();
		
		history = new GameHistory();
	}
	
	public void onInitiateSkip(Play initiator){
		long 	now = System.currentTimeMillis(), 
				elpased = playeractionstarttimes - now;
		final int tolerance = 40000;
		if(elpased < tolerance){
			
		}
		
		votes = 0;
		
		for(bigtwo.game.Play play:plays){
			if(play!=null){
				play.resetVoted();
			}
		}
		initiator.setVoted();
	}
	
	public void onVote(){
		++votes;
		if(votes >= nPlay-1){
			votes = -999;
			
			currentPlay.notifySkipped();
			
			if(status.isAfterBeginning()){
				nextPlayer();
			}else{
				nextEldestHand();
			}
		}
	}
	
	protected void terminate(){
		status = ENDED_TERMINATED;
		ended();
	}
	
	public void notifyStopByGroupAdmin(){
		terminate();
	}
	
	public void notifyStopByBot(){
		terminate();
	}
	
	private void ended(){
		for(Play play:plays){
			if(play!=null)
				play.onPlayEnded();
		}
		uncache(this);
		group.removeGame();
	}
	
	void win(){
		status = ENDED_WIN;
		ended();
	}
	
	
	private boolean _floop = false;
	
	public String toString_gameidonly()
	{
		return "Game:{gameid:" + gameid + ", ...}";
	}
	
	@Override
	public String toString(){
		if(_floop){
			return toString_gameidonly();
		}else{
			StringBuilder strb = new StringBuilder();
			
			strb.append("Game:{");
			
			strb.append("gameid:").append(gameid);
			
			strb.append(",");
			
			strb.append("plays:");
			strb.append("[");
			for(int i=0; i<MAX_PLAY; ++i){
				if(i!=0)
					strb.append(',');
					
				Play play = plays[i];
				if(play==null){
					strb.append("null");
				}else{
					strb.append(play.toString_idonly());
				}
			}
			strb.append(']');
			
			strb.append(",");
			
			strb.append("nPlay:").append(nPlay);
			
			strb.append(",");
			
			strb.append("status:").append(status);
			
			strb.append(",");
			
			strb.append("position:");
			strb.append("[");
			for(int i=0; i<nPlay; ++i){
				if(i!=0)
					strb.append(',');
					
				Play play = positions[i];
				strb.append(play.toString());
			}
			strb.append(']');
			
			strb.append(",");
			
			strb.append("positionIndex:").append(positionIndex);
			
			strb.append(",");
			
			strb.append("currentPlay:").append(currentPlay.toString());
			
			strb.append(",");
			
			if(previousPlayCard != null)
			strb.append("previousPlayCard:").append(previousPlayCard.toString());
			
			strb.append(",");
			
			strb.append("flagNewLeader:").append(flagNewLeader);
			
			strb.append(",");
			
			strb.append("history:").append("...");
			
			strb.append(",");
			
			strb.append("}");
			
			return strb.toString();
		}
	}
}
