package bigtwo.game;

import java.util.ArrayList;
import java.util.HashMap;

import bigtwo.game.Pass;
import bigtwo.card.Card;

import static bot.BigTwoBot.bot;

/* play game */
public class Play {
	private static final HashMap<Integer, Play> plays = new HashMap<>();
	
	private static void cache(Play play){
		plays.put(play.playid, play);
	}
	
	private static Play uncache(Integer playid){
		return plays.remove(playid);
	}
	
	private static Play uncache(Play play){
		return plays.remove(play.playid);
	}
	
	private static int obtainNextPlayId(){
		return bot().obtainNextPlayId();
	}
	
	private static void occupyPlayId(){
		bot().occupyPlayId();
	}
	
	
	
//	instance constants:

	
//	instance fields:
	public final int playid;
	
	public final Game game;
	public final Player player;
	
	public final int playindex;
	
	
	private Hand hand;
		
	private boolean playingstatus;		// false: waiting, true: playing
	
	private WinStatus winstatus;
	
	private int skippedTimes;
	
	boolean voted;
	
	
	
	
//	constructors:
	public Play(Game game, Player player)
	{
		this.playid = obtainNextPlayId();
		this.game = game;
		this.player = player;
		this.playindex = game.nextEmptyPlayIndex();
		playingstatus = false;
		winstatus = WinStatus.UNDEFINED;
		
		occupyPlayId();
		cache(this);
	}
	
	
	
//	instance methods:
	// getters
	public Hand getHand(){
		return hand;
	}
	
	public int getSkippedTimes(){
		return skippedTimes;
	}
	
	public boolean getPlayingStatus(){
		return playingstatus;
	}
	
	public WinStatus getWinStatus(){
		return winstatus;
	}

	// setters
	
	
	// 
	public void onPlayEnded(){
		Play.uncache(this);
		player.removePlay();
	}
	
	private void win(){
		winstatus = WinStatus.WON;
		player.notifyWon();
		onPlayEnded();
	}
	
	private void lose(){
		winstatus = WinStatus.LOST;
		player.notifyLost();
		onPlayEnded();
	}
	
	public Card minTrueRankingCard(){
		return hand.minTrueRankingCard();
	}
	
	
	// notify
	
	//public void notifyGameStarted(int handlen){
	//	hand = new Hand(handlen);
	//	playingstatus = true;
	//	winstatus = WinStatus.UNDEFINED;
	//}
	public void notifyGameStarted(Card[] cards){
		hand = new Hand(cards);
		playingstatus = true;
		winstatus = WinStatus.UNDEFINED;
	}
	
	public void notifyWin(){
		win();
	}
	
	public void notifyLose(){
		lose();
	}
	
	public void notifyTerminated(){
		player.notifyTerminated();
		onPlayEnded();
	}
	
	
	// actions
	public void leave(){
		onPlayEnded();
	}
	
	public void startgame(){
		game.start();
	}
	
	public Handcard select(int index){
		return hand.select(index);
	}
	
	public Handcard select(Card card){
		return hand.select(card);
	}
	
	public boolean unselectAll(){
		return hand.unselectAll();
	}
	
	/*public void playcard(){
		PlayCard playcard = new PlayCard(this, hand.selectedCards());
		
		if(playcard.result == PlayCard.Result.ACCEPT){
			view.notifyPlayCard(playcard);
			
			hand.removeSelectedHandcard();
			game.acceptPlayCard(playcard);
		}else{
			view.notifyPlayCard(playcard);
		}
	}
	
	public void pass(){
		Pass request = new Pass(this);
		
		if(request.result == Pass.PassResult.ACCEPT_NORMAL ||
			request.result == Pass.PassResult.ACCEPT_NEW_LEADER ||
			request.result == Pass.PassResult.ACCEPT_ELDEST_HAND){
			view.notifyPass(request);
			
			game.acceptPass(request);
		}else{
			view.notifyPass(request);
		}
	}*/
	
	public void resetVoted(){
		voted = false;
	}
	
	public void setVoted(){
		voted = true;
	}
	

	/*public void initiateSkip(){
		game.onInitiateSkip(this);
	}
	
	public void voteSkip(){
		if(!voted){
			voted = true;
			game.onVote();
		}
	}*/
	
	public void notifySkipped(){
		++skippedTimes;
	}
	
	
	private boolean _floop = false;
	
	public String toString_idonly(){
		return "Play:{playid:" + playid + ",...}"; 
	}
	
	@Override
	public String toString(){
		if(_floop){
			return toString_idonly();
		}else{
			StringBuilder strb = new StringBuilder();
			
			strb.append("Play{");
			
			strb.append(',');
			
			strb.append("playid:").append(playid);
			
			strb.append(',');
			
			strb.append("game:").append(game.toString_gameidonly());
			
			strb.append(',');
			
			strb.append("player:").append(player.toString());
			
			strb.append(',');
			
			strb.append("hand:");
			if(hand!=null){
				strb.append('[');
				ArrayList<Handcard> handcards = hand.getHandcards();
				for(int i=0; i<handcards.size(); ++i){
					if(i!=0)
						strb.append(',');
					
					Handcard handcard = handcards.get(i);
					strb.append(handcard.toString());
				}
				strb.append("]");
			}else{
				strb.append("null");
			}
			
			strb.append(',');
			
			strb.append("skippedTimes:").append(skippedTimes);
			
			strb.append(',');
			
			strb.append("voted:").append(voted);
			
			strb.append("}");
			
			return strb.toString();
		}
	}
}
