package bigtwo.game;

import static bigtwo.cardcombination.CardCombinationType.INVALID;
import static bigtwo.game.GameStatus.ENDED_WIN;
import static bigtwo.game.Join.JoinResult.ACCEPT;
import static bigtwo.game.Join.JoinResult.REJECT_BUSY_USER;
import static bigtwo.game.Join.JoinResult.REJECT_GAME_FULL;
import static bigtwo.game.Join.JoinResult.REJECT_GAME_STARTED;
import static bigtwo.game.Join.JoinResult.REJECT_REDUNDANT;
import static bigtwo.game.Pass.PassResult.ACCEPT_ELDEST_HAND;
import static bigtwo.game.Pass.PassResult.ACCEPT_NEW_LEADER;
import static bigtwo.game.Pass.PassResult.ACCEPT_NORMAL;
import static bigtwo.game.Pass.PassResult.REJECT_NOT_CURRENT_PLAYER;
import static bigtwo.game.PlayCard.Result.ACCEPT;
import static bigtwo.game.PlayCard.Result.REJECT_CANNOT_BEAT_PREV_PLAY;
import static bigtwo.game.PlayCard.Result.REJECT_INVALID_TYPE;
import static bigtwo.game.PlayCard.Result.REJECT_MIN_CARD_NOT_PLAYED;
import static bigtwo.game.PlayCard.Result.REJECT_NOT_CURRENT_PLAYER;
import static bigtwo.game.PlayCard.Result.REJECT_NO_SELECTED;

import bigtwo.cardcombination.CardCombination;
import bigtwo.cardcombination.CardCombinationRule;
import bigtwo.game.Join.JoinResult;
import bigtwo.game.NewGame.NewGameResult;
import bigtwo.game.Pass.PassResult;
import bigtwo.game.PlayCard.Result;
import bigtwo.card.Card;

public class BigTwoSystem {
	
	
	private BigTwoSystem(){}
	
	
	public static NewGame newGame(Player player, Group group){
		NewGameResult result;
		Game game;
		
		if(Game.reachMaxGame()){
			result = NewGameResult.REJECT_BUSY_BOT;
			game = null;
			
			return new NewGame(player, group, result, game, null);
		}
		
		if(group.isReachMaxGame()){
			result = NewGameResult.REJECT_BUSY_GROUP;
			game = null;
			
			return new NewGame(player, group, result, game, null);
		}
		
		if(player.isPlaying()){
			result = NewGameResult.REJECT_BUSY_USER;
			game = null;
			
			return new NewGame(player, group, result, game, null);
		}
		
		
		result = NewGameResult.ACCEPT;
		
		GameSettings settings;
		settings = GameSettings.getSettings(group.groupid);
		Game newgame = Game.newGame(settings);
		newgame.setGroup(group);
		group.addGame(newgame);
		
		Play newPlay = new Play(newgame, player);
		newgame.acceptJoin(newPlay);
		player.addPlay(newPlay);
		
		Game.cache(newgame);
		
		return new NewGame(player, group, result, newgame, null);
	}

	public static Join join(Player player, Game game){
		JoinResult result;
		
		if(!game.getStatus().isForming()){
			//if(game.getStatus().isPlaying()){
			//	result = REJECT_GAME_STARTED;
			//}
			result = REJECT_GAME_STARTED;
			return new Join(result, player, game, null, null);
		}else if(player.isPlaying(game) != null){
			result = REJECT_REDUNDANT;
			return new Join(result, player, game, null, null);
		}else if(game.isFull()){
			result = REJECT_GAME_FULL;
			return new Join(result, player, game, null, null);
		}else if(player.reachMaxPlay()){
			result = REJECT_BUSY_USER;
			return new Join(result, player, game, null, null);
		}else{
			result = JoinResult.ACCEPT;
			
			Play newplay = new Play(game, player);
			game.acceptJoin(newplay);
			player.addPlay(newplay);
			
			return new Join(result, player, game, newplay, null);
		}
	}

	public static Join join(Player player, Group group){
		Game game = group.getGame();
		if(game == null){
			return new Join(
				JoinResult.FAILED_NO_GAME_IN_GROUP, 
				player, null, null, group);
		}else{
			return join(player, game);
		}
	}

	public static Leave leave(Play play){
		Leave r;
		
		Game game = play.game;
		
		
		game.removePlay(play);
		play.leave();
		
		
		
		if(game.getStatus().isPlaying()){
			game.recordLeave(play);
			
			if(game.isEnoughPlayers()){
				if(game.getCurrentPlay() == play){
					if(game.getStatus().isBeginning()){
						game.nextEldestHand();
						
						Play nextPlay = game.getCurrentPlay();
						r = Leave.newLeaveEldestHand(play, nextPlay);
					}else{
						if(game.getFlagNewLeader()){
							game.nextPlayer();
							
							Play nextPlay = game.getCurrentPlay();
							r = Leave.newLeaveNewLeader(play, nextPlay);
						}else{
							game.nextPlayer();
							
							Play nextPlay = game.getCurrentPlay();
							r = Leave.newLeaveWhilePlayingOnTurn(play, nextPlay);
						}
					}
				}else{
					r = Leave.newLeaveWhilePlayingNotOnTurn(play);
				}
			}else{
				Play remains  = game.getPlays()[0];
				
				game.terminate();
				
				r = Leave.newLeaveTerminateNotEnoughPlayer(play, remains);
			}
		}else{
			int	nplay    = game.getNPlay(),
				maxnplay = Game.MAX_PLAY;
			
			r = Leave.newLeaveWhileForming(play, nplay, maxnplay);
		}
		
		
		return r;
	}
	
	public static Leave leave(Player player, Game game){
		if(player.getPlay().game != game){
			return Leave.newLeaveRejectNotInGame();
		}else{
			return leave(player.getPlay());
		}
	}
	
	public static Leave leave(Player player, Group group){
		Game game = group.getGame();
		
		if(game == null){
			return Leave.newLeaveFailedNoGameInGroup(player, group);
		}else{
			return leave(player, game);
		}
	}
	
	public static StartGame startGamePlayerForce(Player player, Group group){
		Game game = group.getGame();
		
		if(game == null){
			return StartGame.newStartGamePlayerForceStartFailedNoGameInGroup();
		}else if(game.getStatus().isPlaying()){
			return StartGame.newStartGamePlayerForceStartRejectGameStarted();
		}else{
			Play play = player.getPlay();
			
			if(play != null & play.game == game){
				if(game.isEnoughPlayers()){
					game.start();
					
					return StartGame.newStartGamePlayerForceStartAccept(game);
				}else{
					return StartGame.newStartGamePlayerForceStartRejectNotEnoughPlayer(game);
				}
			}else{
				return StartGame.newStartGamePlayerForceStartRejectNotJoined(game);
			}
		}
	}
	
	public static StartGame startGameFullSeat(Game game){
		game.start();
		
		return StartGame.newStartGameFullSeat(game);
	}

	public static SelectCard selectCard(Play play, int index){
		Handcard handcard = play.select(index);
		return SelectCard.singleSelection(play, handcard!=null, handcard);
	}

	public static SelectCard selectCard
	(Play play, Card card){
		Handcard handcard = play.select(card);
		return SelectCard.singleSelection(play, handcard!=null, handcard);
	}
	
	public static SelectCard selectCard
	(Play play, Card[] cards)
	{
		boolean changed = false;
		Handcard handcard;
		for(Card card:cards){
			handcard = play.select(card);
			if(handcard != null)
				changed = true;
		}
		
		return SelectCard.multiSelection(play, changed);
	}

	public static boolean unselectAll
	(Play play)
	{
		return play.unselectAll();
	}
	
	public static PlayCardResult playcard
	(Play play)
	{
		Game game = play.game;
		PlayCard playcard = new PlayCard(play, play.getHand().selectedCards());
		
		PlayCardResult playcardresult;
		
		boolean won;
		if(playcard.result == Result.ACCEPT){
			if(game.status.isBeginning()){
				game.status = GameStatus.PLAYING_AFTERBEGINNING;
			}
			
			game.previousPlayCard = playcard;
			play.getHand().removeSelectedHandcard();
			game.history.recordPlayCard(playcard);
			
			
			if(play.getHand().isEmpty()){
				won = true;
				
				game.win();
				

				play.notifyWin();
				
				Play winner = play;
				Play[] loser = new Play[game.nPlay-1];
				
				for(int i=0, j=0; i<Game.MAX_PLAY; ++i){
					Play play1 = game.plays[i];
					if(play1 != null){
						if(play1 != winner){
							play1.notifyLose();
							loser[j] = play1;
							++j;
						}
					}
				}
				
				Game.GameResult result = new Game.GameResult(winner, loser);
				
				playcardresult = PlayCardResult.win(playcard, result);
			}else{
				won = false;
				
				game.flagNewLeader = false;
				game.nextPlayer();
				
				playcardresult = PlayCardResult.playcard(playcard);
			}
		}else{
			won = false;
			
			playcardresult = PlayCardResult.playcard(playcard);
		}
		
		return playcardresult;
	}
	
	public static Pass pass(Play play)
	{
		Game game = play.game;
		PassResult result;
		
		if(!play.equals(game.getCurrentPlay())){
			result = PassResult.REJECT_NOT_CURRENT_PLAYER;
		}
		
		if(game.getStatus().isBeginning()){
			result = PassResult.ACCEPT_ELDEST_HAND;
			
			play.getHand().unselectAll();
			
			game.history.recordPass(play);
			game.nextEldestHand();
		}else if(game.getFlagNewLeader()){
			result = PassResult.ACCEPT_NEW_LEADER;
			
			play.getHand().unselectAll();
			
			game.history.recordPass(play);
			game.nextPlayer();
		}else{
			result = PassResult.ACCEPT_NORMAL;
			
			play.getHand().unselectAll();
			
			game.history.recordPass(play);
			game.nextPlayer();
		}
		
		return Pass.pass(play, result);
	}

	public static InitiateSkip initiateSkip(Play play){
		Game game = play.game;
		InitiateSkip r;
		
		long 	now = System.currentTimeMillis(), 
				elpased = game.playeractionstarttimes - now;
		final int tolerance = 40000;
		int    remainsTime = (int)(tolerance - elpased);
		
		Play playertobeskipped = game.getCurrentPlay();
		
		if(remainsTime > 0){
			r = InitiateSkip.initiateskipTooEarly(play, playertobeskipped, (int)remainsTime);
		}else{
			game.votes = 0;
			
			for(Play play0 : game.plays){
				if(play!=null)
					play.resetVoted();
			}
			
			r = InitiateSkip.initiateskipAccept(play, playertobeskipped, false);
		}
		
		return r;
	}

	public static VoteSkip voteskip(Play play){
		VoteSkip r = null;
		
		Game game = play.game;
		
		if(!play.voted){
			play.voted = true;
			
			++game.votes;
			if(game.votes >= game.getNFulfillVoteSkip()){
				game.votes = -999;
				
				game.history.recordSkipped(game.currentPlay);
				game.currentPlay.notifySkipped();
				
				if(game.status.isAfterBeginning()){
					Play skipped = game.currentPlay;
					
					game.nextPlayer();
					if(game.flagNewLeader){
						r = VoteSkip.voteskipSkipped(play, game.getNFulfillVoteSkip(), skipped, PlayerPosition.NEW_LEADER);
					}else{
						r = VoteSkip.voteskipSkipped(play, game.getNFulfillVoteSkip(), skipped,  PlayerPosition.PLAYER);
					}
				}else{
					Play skipped = game.currentPlay;
					
					game.nextEldestHand();
					
					r = VoteSkip.voteskipSkipped(play, game.getNFulfillVoteSkip(), skipped, PlayerPosition.ELDEST_HAND);
				}
			}else{
				r = VoteSkip.voteskipAccept(play, game.votes, game.getNFulfillVoteSkip());
			}
		}else{
			
		}
		
		return r;
	}

	public static void stopByAdmin
	(Game game)
	{
		game.notifyStopByGroupAdmin();
	}
	
	public static void stopByBot
	(Game game)
	{
		game.notifyStopByBot();
	}
	
	
//	#debug
	public static void main(String[] args){
		
	}
}