package bigtwo.game;

import static bigtwo.cardcombination.CardCombinationType.INVALID;
import static bigtwo.game.PlayCard.Result.*;

import bigtwo.cardcombination.CardCombination;
import bigtwo.card.Card;

public class PlayCard {
	public static enum Result{
		ACCEPT,
		REJECT_MIN_CARD_NOT_PLAYED,
		REJECT_INVALID_TYPE,
		REJECT_CANNOT_BEAT_PREV_PLAY,
		REJECT_NOT_CURRENT_PLAYER,
		REJECT_NO_SELECTED
	}
	
	public final CardCombination cardcomb;
	public final Play            sender  ;
	public final Result          result  ;
	
	public PlayCard(Play sender, Card[] cards){
		this.sender = sender;
		this.cardcomb = sender.game.getSettings().getGameRule().
			getCardCombinationRule().cardCombination(cards);
		
		Game game = sender.game;
		
		if(sender != game.getCurrentPlay()){
			result = REJECT_NOT_CURRENT_PLAYER;
		}else if(cardcomb.getCards().length == 0){
			result = REJECT_NO_SELECTED;
		}else if(cardcomb.type == INVALID){
			result = REJECT_INVALID_TYPE;
		}else if(game.getStatus().isBeginning() && 
			!cardcomb.contain(sender.minTrueRankingCard())){
				result = REJECT_MIN_CARD_NOT_PLAYED;
		}else if(game.getStatus().isAfterBeginning() && 
			!canCover(game.getPreviousPlayCard())){
				result = REJECT_CANNOT_BEAT_PREV_PLAY;
		}else{
			result = ACCEPT;
		}
	}

	public boolean canCover(PlayCard playcard){
		return 	(sender.game.getFlagNewLeader() || sender == playcard.sender) || 
				sender.game.getSettings().getGameRule().getCardCombinationRule().compare(cardcomb, playcard.cardcomb) > 0;
	}
}
