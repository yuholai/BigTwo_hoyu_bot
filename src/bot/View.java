package bot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import bigtwo.cardcombination.CardCombination;
import bigtwo.cardcombination.CardCombinationTypeComparator;
import bigtwo.cardcombination.FlushComparator;
import bigtwo.cardcombination.FlushComparators;
import bigtwo.cardcombination.StraightComparator;
import bigtwo.cardcombination.StraightComparatorCustom;
import bigtwo.cardcombination.StraightComparators;
import bigtwo.cardcombination.StraightValidatorChain;
import bigtwo.game.Game;
import bigtwo.game.Game.GameResult;
import bigtwo.game.GameHistory;
import bigtwo.game.GameHistory.RecordEntry;
import bigtwo.game.GameSettings;
import bigtwo.game.Group;
import bigtwo.game.GroupSettings;
import bigtwo.game.Hand;
import bigtwo.game.Handcard;
import bigtwo.game.InitiateSkip;
import bigtwo.game.Join;
import bigtwo.game.Leave;
import bigtwo.game.NewGame;
import bigtwo.game.NewGame.NewGameResult;
import bigtwo.game.Pass;
import bigtwo.game.Play;
import bigtwo.game.PlayCard;
import bigtwo.game.PlayCardResult;
import bigtwo.game.Player;
import bigtwo.game.SelectCard;
import bigtwo.game.StartGame;
import bigtwo.game.VoteSkip;
import formatingoptions.FormatingOptions;
import bigtwo.card.Card;
import bigtwo.card.Rank;
import bigtwo.card.Suit;

import static bot.Constants.*;

public class View {
	private static final String
	CBQD_SELECT_CARD_APPEND = CBQD_SELECT_CARD + " ",
	CBQD_PLAY_CARD_APPEND   = CBQD_PLAY_CARD   + " ",
	CBQD_PASS_APPEND        = CBQD_PASS        + " ",
	CBQD_UNSELECTALL_APPEND = CBQD_UNSELECTALL + " ",
	CBQD_VOTE_SKIP_APPEND   = CBQD_VOTE_SKIP   + " ",
	
	CBQD_SET_SETTINGS_APPEND           = CBQD_SET_SETTINGS           +" ",
	CBQD_SET_GAMERULES_APPEND          = CBQD_SET_GAMERULES          +" ",
	CBQD_SET_LANGUAGE_APPEND           = CBQD_SET_LANGUAGE           +" ",
	CBQD_SET_NCARD_APPEND              = CBQD_SET_NCARD              +" ",
	CBQD_SET_NCARD_10_APPEND           = CBQD_SET_NCARD_10           +" ",
	CBQD_SET_NCARD_13_APPEND           = CBQD_SET_NCARD_13           +" ",
	CBQD_SET_ALLOW_TRIPLE_APPEND       = CBQD_SET_ALLOW_TRIPLE       +" ",
	CBQD_SET_ALLOW_TRIPLE_TRUE_APPEND  = CBQD_SET_ALLOW_TRIPLE_TRUE  +" ",
	CBQD_SET_ALLOW_TRIPLE_FALSE_APPEND = CBQD_SET_ALLOW_TRIPLE_FALSE +" ",
	CBQD_SET_SV_APPEND                 = CBQD_SET_SV                 +" ",
	CBQD_SET_SVC_BEGIN_APPEND          = CBQD_SET_SVC_BEGIN          +" ",
	CBQD_SET_SVC_END_APPEND            = CBQD_SET_SVC_END            +" ",
	CBQD_SET_CCTC_APPEND               = CBQD_SET_CCTC               +" ",
	CBQD_SET_SF_APPEND                 = CBQD_SET_SF                 +" ",
	CBQD_SET_FS_APPEND                 = CBQD_SET_FS                 +" ",
	CBQD_SET_SC_APPEND                 = CBQD_SET_SC                 +" ",
	CBQD_SET_SC_RANKING_FIRST_APPEND   = CBQD_SET_SC_RANKING_FIRST   +" ",
	CBQD_SET_SC_CUSTOM_APPEND          = CBQD_SET_SC_CUSTOM          +" ",
	CBQD_SET_SCC_APPEND                = CBQD_SET_SCC                +" ",
	CBQD_SET_FC_APPEND                 = CBQD_SET_FC                 +" ",
	CBQD_SET_FC_SUIT_FIRST_APPEND      = CBQD_SET_FC_SUIT_FIRST      +" ",
	CBQD_SET_FC_CARD_FIRST_APPEND      = CBQD_SET_FC_CARD_FIRST      +" ",
	CBQD_SET_FC_RANKING_FIRST_APPEND   = CBQD_SET_FC_RANKING_FIRST   +" ",
	CBQD_VIEW_SETTINGS_APPEND          = CBQD_VIEW_SETTINGS          +" ",
	CBQD_VIEW_GAMERULES_APPEND         = CBQD_VIEW_GAMERULES         +" ",
	CBQD_VIEW_NCARD_APPEND             = CBQD_VIEW_NCARD             +" ",
	CBQD_VIEW_ALLOW_TRIPLE_APPEND      = CBQD_VIEW_ALLOW_TRIPLE      +" ",
	CBQD_VIEW_SV_APPEND                = CBQD_VIEW_SV                +" ",
	CBQD_VIEW_CCTC_APPEND              = CBQD_VIEW_CCTC              +" ",
	CBQD_VIEW_SC_APPEND                = CBQD_VIEW_SC                +" ",
	CBQD_VIEW_FC_APPEND                = CBQD_VIEW_FC                +" ";
	
	public static final char[] emoji_check_mark = Character.toChars(0x2705);
	
	private BigTwoBot bot;
	
	private HashMap<Integer, Integer> votemsgsid = new HashMap<>();
	
	public View(BigTwoBot bot){this.bot=bot;}
	
	private String text(String str, String alt){
		return (str == null)?(alt):(str);
	}
	
	private String displayname(Player player){
		if(player.getLastname() == null){
			return new StringBuilder().append(player.getFirstname()).toString();
		}else{
			return new StringBuilder().append(player.getFirstname()).append(' ').append(player.getLastname()).toString();
		}
	}
	
	private String escapeddisplayname(Player player){
		StringBuilder strb = new StringBuilder();
		FormatingOptions.escapeHTML(displayname(player), strb);
		return strb.toString();
	}
	
	private String textmention(Player player){
		return FormatingOptions.htmlInlineMention(player.getPlayerId(), escapeddisplayname(player));
	}
	
	private String textmentionplayerlist(Player[] player) {
		if(player.length > 0)
		{
			StringBuilder strb = new StringBuilder();
			strb.append(textmention(player[0]));
			for(int i=1; i<player.length; ++i) {
				strb.append(',').append(textmention(player[i]));
			}
			return strb.toString();
		}
		else
		{
			return "";
		}
	}
	
	private String textmentionplayerlist(Play[] plays) {
		Player[] players = new Player[plays.length];
		for(int i=0; i<plays.length; ++i)
			players[i] = plays[i].player;
		return textmentionplayerlist(players);
	}
	
	private String cardtext(Card card){
		if(card.rank != Rank.RANK_JOKER)
			return card.rank.text + card.suit.emoji;
		else{
			if(card.red == Boolean.FALSE){
				return "b" + Suit.JOKERS_EMOJI;
			}else{
				return "R" + Suit.JOKERS_EMOJI;
			}
		}	
	}
	
	private void appendInvisibleHandTo
	(Hand hand, ArrayList<List<InlineKeyboardButton>> btns)
	{
		for(int i=0; i<hand.getNumberOfHandcards(); ){
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			for(int iend=i+8; i<iend; ++i){
				InlineKeyboardButton btn;
				if(i<hand.getNumberOfHandcards()){
					btn = 
						new InlineKeyboardButton()
						.setText(Card.EMOJI_FLOWER_PLAYING_CARDS)
						.setCallbackData(
							new StringBuilder()
							.append(CBQD_SELECT_CARD_APPEND)
							.append(i)
							.toString()
						);
				}else{
					btn = 
						new InlineKeyboardButton()
						.setText(" ")
						.setCallbackData(
							new StringBuilder()
							.append(CBQD_BLANK)
							.toString()
						);
				}
				row.add(btn);
			}
			btns.add(row);
		}
	}
	
	private void appendVisibleHandTo
	(Hand hand, ArrayList<List<InlineKeyboardButton>> btns)
	{
		ArrayList<Handcard> handcards = hand.getHandcards();
		for(int i=0; i<hand.getNumberOfHandcards(); ){
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			for(int iend=i+8; i<iend; ++i){
				InlineKeyboardButton btn;
				if(i<hand.getNumberOfHandcards()){
					Handcard handcard = handcards.get(i);
					Card card = handcard.card;
					btn = new InlineKeyboardButton();
					if(handcard.selected){
						btn.setText(">" + cardtext(card) + "<");
					}else{
						btn.setText(cardtext(card));
					}
					btn.setCallbackData(
						new StringBuilder()
						.append(CBQD_SELECT_CARD_APPEND)
						.append(i)
						.toString()
					);
				}else{
					btn = 
						new InlineKeyboardButton()
						.setText(" ")
						.setCallbackData(
							new StringBuilder()
							.append(CBQD_BLANK)
							.toString()
						);
				}
				row.add(btn);
			}
			btns.add(row);
		}
	}

	private void appendActionButtonsTo
	(Lang lang, ArrayList<List<InlineKeyboardButton>> btns)
	{
		ArrayList<InlineKeyboardButton> row = new ArrayList<>();
		row.add(
			new InlineKeyboardButton()
			.setText(lang.btnPlay())
			.setCallbackData(CBQD_PLAY_CARD)
		);
		row.add(
			new InlineKeyboardButton()
			.setText(lang.btnPass())
			.setCallbackData(CBQD_PASS)
		);
		row.add(
			new InlineKeyboardButton()
			.setText(lang.btnUnselectall())
			.setCallbackData(CBQD_UNSELECTALL)
		);
		btns.add(row);
	}

	private void appendHandTo
	(Hand hand, ArrayList<KeyboardRow> btns)
	{
		KeyboardRow row = null;
		ArrayList<Handcard> handcards = hand.getHandcards();
		
		for(int i=0, iend=handcards.size(); i<iend; )
		{
			row = new KeyboardRow();
			for(int iend2=Math.min(i+8, iend); i<iend2; ++i)
			{
				row.add(cardtext(handcards.get(i).card));
			}
			btns.add(row);
		}
		
		if(8 - row.size() < 2)
		{
			for(int i=row.size(); i<8; ++i)
				row.add("-");
			row = new KeyboardRow();
			btns.add(row);
		}
		for(int i=row.size(); i<6; ++i)
		{
			row.add("-");
		}
		row.add(MSGTXT_PLAY);
		row.add(MSGTXT_PASS);
	}
	
	private void appendPlayCardTo(PlayCard playcard, boolean textmention, Lang lang, StringBuilder strb){
		StringBuilder strbsender = new StringBuilder();
		if(textmention){
			strbsender.append(textmention(playcard.sender.player));
		}else{
			FormatingOptions.escapeHTML(
				displayname(playcard.sender.player),
				strbsender
			);
		}
		
		StringBuilder strbplaycard = new StringBuilder();
		
		strbplaycard
		.append(cardtext(playcard.cardcomb.getCards()[0]));
		for(int i=1; i<playcard.cardcomb.getCards().length; ++i)
			strbplaycard.append(' ').append(cardtext(playcard.cardcomb.getCards()[i]));
		
		String nhandcards = new String();
		if(playcard.sender.getHand().getNumberOfHandcards() > 0)
			nhandcards += playcard.sender.getHand().getNumberOfHandcards();
		
		strb.append(lang.playedCardAccept(strbsender.toString(), strbplaycard.toString(), nhandcards));
	}
	
	private void appendPlayCardRecordTo(RecordEntry record, Lang lang, StringBuilder strb)
	{
		PlayCard playcard = record.playcard;
		StringBuilder strbsender = new StringBuilder();
		strbsender.append(textmention(playcard.sender.player));
		
		StringBuilder strbplaycard = new StringBuilder();
		
		strbplaycard
		.append(cardtext(playcard.cardcomb.getCards()[0]));
		for(int i=1; i<playcard.cardcomb.getCards().length; ++i)
			strbplaycard.append(' ').append(cardtext(playcard.cardcomb.getCards()[i]));
		
		String nhandcards = new String();
		if(record.nhandcard > 0)
			nhandcards += record.nhandcard;
		
		strb.append(lang.playedCardAccept(strbsender.toString(), strbplaycard.toString(), nhandcards));
	}
	
	private void appendResultTo(GameResult result, Game game, StringBuilder strb){
		Lang lang = game.getSettings().getLang();
		
		StringBuilder
			strblosers = new StringBuilder();
			
		strb
		.append(lang.win(
			textmention(result.winner.player)
		));
		
		Play[] losers = result.loser;
		for(int i=0; i<losers.length; ++i){
			if(i==0){
				strblosers.append(
					FormatingOptions.htmlInlineMention(
						losers[i].player.getUserId(),
						FormatingOptions.escapeHTML(displayname(losers[i].player))
					)
				);
			}else{
				strblosers
				.append(", ")
				.append(
					FormatingOptions.htmlInlineMention(
						losers[i].player.getUserId(),
						FormatingOptions.escapeHTML(displayname(losers[i].player))
					)
				);
			}
		}
		
		strb.append('\n')
		.append(lang.lose(strblosers.toString()));
	}
	
	private String escapedGroupName(String groupname){
		return FormatingOptions.escapeHTML(groupname);
	}
	
	private ReplyKeyboardMarkup playerRKM(Play play){
		ArrayList<KeyboardRow> kbs = new ArrayList<>();
		appendHandTo(play.getHand(), kbs);
		
		return new ReplyKeyboardMarkup()
		.setKeyboard(kbs)
		.setSelective(true)
		.setResizeKeyboard(true)
		.setOneTimeKeyboard(false);
	}
	
	private InlineKeyboardMarkup playerVisibleIKM(Play play, Lang lang){
		ArrayList<List<InlineKeyboardButton>> kbs = new ArrayList<>();
		appendVisibleHandTo(play.getHand(), kbs);
		appendActionButtonsTo(lang, kbs);
		return new InlineKeyboardMarkup().setKeyboard(kbs);
	}
	
	private InlineKeyboardMarkup playerInvisibleIKM(Play play, Lang lang){
		ArrayList<List<InlineKeyboardButton>> kbs = new ArrayList<>();
		appendInvisibleHandTo(play.getHand(), kbs);
		appendActionButtonsTo(lang, kbs);
		return new InlineKeyboardMarkup().setKeyboard(kbs);
	}
	
	
	private void nextEldestHand(Play eldesthand, Lang lang, Game game){
		String txtnextplayer = lang.nextEldestHand(
			textmention(eldesthand.player),
			cardtext(eldesthand.getHand().minTrueRankingCard())
		);
		ReplyKeyboardMarkup rkm = playerRKM(eldesthand);
		
		SendMessage sdmsgnextplayer = new SendMessage()
		.setChatId(game.getGroup().getChatId())
		.setText(txtnextplayer)
		.setParseMode(ParseMode.HTML)
		.setReplyMarkup(rkm);
		
		bot.sendAsync(sdmsgnextplayer);
	}
	
	private void nextPlayer(Play nextplay, Lang lang, Game game){
		String txtnextplayer = lang.nextPlayer(textmention(nextplay.player));
		ReplyKeyboardMarkup rkm = playerRKM(nextplay);
		
		SendMessage sdmsgnextplayer = new SendMessage()
		.setChatId(game.getGroup().getChatId())
		.setText(txtnextplayer)
		.setParseMode(ParseMode.HTML)
		.setReplyMarkup(rkm);
		
		bot.sendAsync(sdmsgnextplayer);
	}
	

	public void display(NewGame newgame){
		NewGameResult result = newgame.result;
		
		SendMessage sdmsg = new SendMessage()
			.setChatId(newgame.group.getChatId())
			.setParseMode(ParseMode.HTML);
		
		Lang lang;
		
		switch(result)
		{
		case ACCEPT           :
			lang = newgame.game.getSettings().getLang();
			sdmsg.setText(
				new StringBuilder()
				.append(lang.newGameAccept())
				.append("\n\n")
				.append(lang.joinAccept())
				.toString()
				);
			break;
		case REJECT_BUSY_USER : 
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.newGameReject())
				.append("\n\n")
				.append(lang.busyUser())
				.toString()
				);
			break;
		case REJECT_BUSY_GROUP: 
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.newGameReject())
				.append("\n\n")
				.append(lang.busyGroup())
				.toString()
				);
			break;
		case REJECT_BUSY_BOT  :
		default               :
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.newGameReject())
				.append("\n\n")
				.append(lang.busyBot())
				.toString()
				);
			break;
		}
		
		bot.sendAsync(sdmsg);
	}

	public void display(Join join){
		SendMessage sdmsg = new SendMessage();
		sdmsg.setParseMode(ParseMode.HTML);
		
		Lang lang;
		
		switch(join.result){
		case ACCEPT:
			lang = join.game.getSettings().getLang();
			sdmsg.setText(lang.joinAccept())
			.setChatId(join.play.game.getGroup().getChatId());
			break;
		case REJECT_GAME_STARTED:
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.joinReject())
				.append("\n\n")
				.append(lang.gameStarted())
				.toString()
			)
			.setChatId(join.group.getChatId());
			break;
		case REJECT_BUSY_USER:
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.joinReject())
				.append("\n\n")
				.append(lang.busyUser())
				.toString()
				)
			.setChatId(join.group.getChatId());
			break;
		case REJECT_GAME_FULL:
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.joinReject())
				.append("\n\n")
				.append(lang.gameFull())
				.toString()
				)
			.setChatId(join.group.getChatId());
			break;
		case REJECT_REDUNDANT:
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.joinReject())
				.append("\n\n")
				.append(lang.joinRedundantly())
				.toString()
				)
			.setChatId(join.group.getChatId());
			break;
		case FAILED_NO_GAME_IN_GROUP:
		default:
			lang = Lang.primaryLang;
			sdmsg.setText(
				new StringBuilder()
				.append(lang.joinReject())
				.append("\n\n")
				.append(lang.noGameInGroup())
				.toString()
				)
			.setChatId(join.group.getChatId());
			break;
		}
		
		bot.sendAsync(sdmsg);
	}

	public void display(Leave leave){
		SendMessage sdmsg = new SendMessage();
		sdmsg.setChatId(leave.group.getChatId());
		
		Lang lang;
		
		switch(leave.result){
		case ACCEPT:
			lang = leave.play.game.getSettings().getLang();
			
			{
				StringBuilder strb = new StringBuilder();
				sdmsg.setText(
					lang.leaveAccept(
						FormatingOptions.htmlInlineMention(
							leave.play.player.getUserId(),
							FormatingOptions.escapeHTML(
								displayname(leave.play.player)
							)
						)
					)
				);
				
			}
			break;
		case REJECT_NOT_IN_GAME:
			lang = leave.play.game.getSettings().getLang();
		
			sdmsg.setText(
				new StringBuilder()
				.append("Not in game")
				.toString()
				);
			break;
		case FAILED_NO_GAME_IN_GROUP:
		default:
			lang = leave.group.getSettings().getLang();
			
			sdmsg.setText(lang.noGameInGroup());
			break;
		}
		
		bot.sendAsync(sdmsg);
	}

	public void display(StartGame startgame){
		SendMessage sdmsg = new SendMessage();
		sdmsg.setChatId(startgame.game.getGroup().getChatId());
		
		Lang lang = startgame.game.getSettings().getLang();
		
		StringBuilder textb = new StringBuilder();
		
		switch(startgame.result){
		case ACCEPT:
		{
			String	eldesthand = 
						textmention(startgame.game.getEldestHand().player),
					mincard    = cardtext(startgame.game.getEldestHand().minTrueRankingCard());
			textb
			.append(lang.startGameSucessfully())
			.append("\n")
			.append(lang.eldestHand(eldesthand, mincard));
			
			sdmsg.setReplyMarkup(playerRKM(startgame.game.getEldestHand()));
		}
			break;
		case REJECT_NOT_ENOUGH_PLAYER:
			textb
			.append(lang.startGameFailed())
			.append("\n")
			.append(lang.notEnoughPlayers());
			break;
		case REJECT_NOT_JOINED:
			textb
			.append(lang.startGameFailed())
			.append("\n")
			.append("Not joined");
			break;
		case FAILED_NO_GAME_IN_GROUP:
			textb
			.append(lang.startGameFailed())
			.append("\n")
			.append(lang.noGameInGroup());
			break;
		case REJECT_GAME_STARTED:
			textb
			.append(lang.startGameFailed())
			.append("\n")
			.append(lang.gameStarted());
			break;	
		case SUCESSFULLY:
		{
			String	eldesthand = 
					FormatingOptions.htmlInlineMention(
						startgame.game.getEldestHand().player.getUserId(), 
						FormatingOptions.escapeHTML(displayname(startgame.game.getEldestHand().player))
					),
						mincard    = cardtext(startgame.game.getEldestHand().minTrueRankingCard());
			switch(startgame.trigger){
			case FULL_SEAT:
				textb
				.append(lang.gameFull());
				break;
			default:
				textb.append(lang.startGameSucessfully());
				break;
			}
			textb
			.append("\n")
			.append(lang.eldestHand(eldesthand, mincard));
		}
			break;
		case FAILED_NOT_ENOUGH_PLAYER:
			textb
			.append(lang.startGameFailed())
			.append("\n")
			.append(lang.notEnoughPlayers());
			break;
		}
		
		sdmsg.setText(textb.toString())
		.setParseMode(ParseMode.HTML);
		
		bot.sendAsync(sdmsg);
	}

	public void display(SelectCard selectcard, CallbackQuery cbq)
	{
		Card[] selected = selectcard.play.getHand().selectedCards();

		String text;
		
		if(selected.length > 0){
			StringBuilder txtbselected = new StringBuilder();
			txtbselected.append(cardtext(selected[0]));
			for(int i=1; i<selected.length; ++i)
				txtbselected.append(' ').append(cardtext(selected[i]));
			text = txtbselected.toString();
		}else{
			text = selectcard.play.game.getSettings().getLang().noCardsSelected();
		}
		
		
		AnswerCallbackQuery acbq = new AnswerCallbackQuery()
			.setCallbackQueryId(cbq.getId())
			.setShowAlert(false)
			.setText(text);
		
		bot.sendAsync(acbq);
	}

	public void displayUnselectAll(Play play, CallbackQuery cbq){
		AnswerCallbackQuery acbq = new AnswerCallbackQuery()
			.setCallbackQueryId(cbq.getId())
			.setShowAlert(false)
			.setText(play.game.getSettings().getLang().noCardsSelected());
		
		bot.sendAsync(acbq);
	}

	public void display(PlayCardResult playcardresult){
		PlayCard playcard = playcardresult.playcard;
		Game game = playcard.sender.game;
		Lang lang = game.getSettings().getLang();
		
		Play sender = playcard.sender;
		CardCombination cc = playcard.cardcomb;
		Card[] cards = cc.getCards();
		
		Integer votemsgid = votemsgsid.remove(game.gameid);
		if(votemsgid!=null){
			bot.sendAsync(
				new EditMessageReplyMarkup()
				.setChatId(game.getGroup().getChatId())
				.setMessageId(votemsgid)
				.setReplyMarkup(null)
			);
		}
		
		if(game.getStatus().isPlaying()){
			// not won
			StringBuilder strbplay = new StringBuilder();
			appendPlayCardTo(playcard, true, lang, strbplay);
			
			ReplyKeyboardMarkup rkm = playerRKM(sender);
			
			SendMessage sdmsgplay = new SendMessage()
			.setChatId(game.getGroup().getChatId())
			.setText(strbplay.toString())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(rkm);
			
			bot.sendAsync(sdmsgplay);
			
			nextPlayer(game.getCurrentPlay(), lang, game);
		}else{
			// won
			SendMessage sdmsg = new SendMessage();
			sdmsg.setChatId(game.getGroup().getChatId());
			
			StringBuilder
				txtb = new StringBuilder(),
				strblosers = new StringBuilder();
			appendPlayCardTo(playcard, false, lang, txtb);
			txtb.append('\n');
			appendResultTo(playcardresult.result, game, txtb);
			
			sdmsg.setText(txtb.toString())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(new ReplyKeyboardRemove().setSelective(true));
			
			bot.sendAsync(sdmsg);
		}
	}

	public void display(PlayCardResult playcardresult, CallbackQuery cbq){
		PlayCard playcard = playcardresult.playcard;
		Game game = playcard.sender.game;
		
		switch(playcard.result){
		case ACCEPT:
			EditMessageReplyMarkup emrm = new EditMessageReplyMarkup()
				.setMessageId(cbq.getMessage().getMessageId())
				.setReplyMarkup(null);
			
			bot.sendAsync(emrm);
			
			display(playcardresult);
			break;
		case REJECT_MIN_CARD_NOT_PLAYED:
		case REJECT_INVALID_TYPE:
		case REJECT_CANNOT_BEAT_PREV_PLAY:
		case REJECT_NOT_CURRENT_PLAYER:
		case REJECT_NO_SELECTED:
		default:
			AnswerCallbackQuery acq = new AnswerCallbackQuery()
			.setCallbackQueryId(cbq.getId())
			.setText(game.getSettings().getLang().playedCardReject());
			bot.sendAsync(acq);
			break;
		}
	}

	public void display(Pass pass){
		Play play = pass.play;
		Game game = play.game;
		
		switch(pass.result){
		case ACCEPT_NORMAL:
		case ACCEPT_NEW_LEADER:
		{
			Integer votemsgid = votemsgsid.remove(game.gameid);
			if(votemsgid!=null){
				bot.sendAsync(
					new EditMessageReplyMarkup()
					.setChatId(game.getGroup().getChatId())
					.setMessageId(votemsgid)
					.setReplyMarkup(null)
				);
			}
			
			SendMessage sdmsg = new SendMessage();
			sdmsg.setChatId(pass.play.game.getGroup().getChatId());
			
			Lang lang = pass.play.game.getGroup().getSettings().getLang();
			StringBuilder txtb = new StringBuilder();
			txtb.append(lang.passAccept(escapeddisplayname(pass.play.player)));
			
			sdmsg.setText(txtb.toString());
			
			bot.sendAsync(sdmsg);
			
			
			nextPlayer(pass.play.game.getCurrentPlay(), lang, pass.play.game);
			break;
		}
		case ACCEPT_ELDEST_HAND:
		{
			Integer votemsgid = votemsgsid.remove(game.gameid);
			if(votemsgid!=null){
				bot.sendAsync(
					new EditMessageReplyMarkup()
					.setChatId(game.getGroup().getChatId())
					.setMessageId(votemsgid)
					.setReplyMarkup(null)
				);
			}
			
			SendMessage sdmsg = new SendMessage();
			sdmsg.setChatId(pass.play.game.getGroup().getChatId());
			
			Lang lang = pass.play.game.getGroup().getSettings().getLang();
			StringBuilder txtb = new StringBuilder();
			txtb.append(lang.passAccept(escapeddisplayname(pass.play.player)));
			
			sdmsg.setText(txtb.toString());
			
			bot.sendAsync(sdmsg);
			
			
			nextEldestHand(pass.play.game.getCurrentPlay(), lang, pass.play.game);
			break;
		}
		case REJECT_NOT_CURRENT_PLAYER:
		default:
			break; 
		}
	}

	public void display(Pass pass, CallbackQuery cbq){
		switch(pass.result){
		case ACCEPT_NORMAL:
		case ACCEPT_NEW_LEADER:
		case ACCEPT_ELDEST_HAND:
		{
			EditMessageReplyMarkup emrm = new EditMessageReplyMarkup()
					.setMessageId(cbq.getMessage().getMessageId())
					.setReplyMarkup(null);
				
			bot.sendAsync(emrm);
			
			display(pass);
			
			break;
		}
		case REJECT_NOT_CURRENT_PLAYER:
		default:
			break;
		}
	}

	public void display(InitiateSkip skip){
		Game game = skip.initiator.game;
		Play playtobeskipped = skip.playertobeskipped;
		Lang lang = game.getSettings().getLang();
		switch(skip.result){
		case ACCEPT:
			break;
		case REJECT_REDUNDANT:
			break;
		case REJECT_TOO_EARLY:
			bot.sendAsync(
				new SendMessage()
				.setChatId(skip.initiator.game.getGroup().getChatId())
				.setText(
					skip.initiator.game.getSettings().getLang()
					.tooEarly(""+skip.remainsTime)
				)
			);
			break;
		}
	}

	private void displayPlayerSkipped(Play skippedplay, Game game, Lang lang){
		bot.sendAsync(
			new SendMessage()
			.setChatId(game.getGroup().getChatId())
			.setText(lang.playerSkipped(escapeddisplayname(skippedplay.player)))
			.setParseMode(ParseMode.HTML)
		);
	}

	public void display(VoteSkip vote){
		Game game = vote.voter.game;
		Lang lang = game.getSettings().getLang();
		
		if(vote.skip){
			displayPlayerSkipped(vote.skipped, game, lang);
			
			switch(vote.position){
			case PLAYER:
			case NEW_LEADER:
				nextPlayer(game.getCurrentPlay(), lang, game);
				break;
			case ELDEST_HAND:
				nextEldestHand(game.getCurrentPlay(), lang, game);
				break;
			}
		}else{
			Play playtobeskipped = game.getCurrentPlay();
			
			try {
				Message votemsg = bot.send(
					new SendMessage()
					.setChatId(game.getGroup().getChatId())
					.setText(
						lang.skipVote(
							escapeddisplayname(playtobeskipped.player), 
							""+game.getSkipVote(), 
							""+game.getNFulfillVoteSkip()
						)
					)
					.setParseMode(ParseMode.HTML)
					.setReplyMarkup(
						new InlineKeyboardMarkup()
						.setKeyboard(
							Arrays.asList(
								Arrays.asList(
									new InlineKeyboardButton()
									.setText(lang.btnVoteSkip())
									.setCallbackData(CBQD_VOTE_SKIP)
								)
							)
						)
					)
				);
				
				votemsgsid.put(game.gameid, votemsg.getMessageId());
			} catch (TelegramApiException ex) {
				BotLogger.error("", ex.toString());
			}
		}
	}
	
	public void display(VoteSkip vote, CallbackQuery cbq){
		Game game = vote.voter.game;
		Lang lang = game.getSettings().getLang();
		
		switch(vote.requestresult){
		case ACCEPT:
			if(vote.skip){
				Play skippedplay = vote.skipped;
				
				bot.sendAsync(
					new EditMessageText()
					.setChatId(cbq.getChatInstance())
					.setMessageId(cbq.getMessage().getMessageId())
					.setText(lang.playerSkipped(escapeddisplayname(skippedplay.player)))
					.setParseMode(ParseMode.HTML)
					.setReplyMarkup(null)
				);
			}
			display(vote);
			break;
		case REJECT_NOT_IN_GAME:
		default:
			bot.sendAsync(
				new AnswerCallbackQuery()
				.setCallbackQueryId(cbq.getId())
				.setText(lang.noGameInGroup())
			);
			break;
		}
	}

	public void displayStopByAdmin(Game game){
		Integer votemsgid = votemsgsid.remove(game.gameid);
		if(votemsgid!=null){
			bot.sendAsync(
				new EditMessageReplyMarkup()
				.setChatId(game.getGroup().getChatId())
				.setMessageId(votemsgid)
				.setReplyMarkup(null)
			);
		}
		
		bot.sendAsync(
			new SendMessage()
			.setChatId(game.getGroup().getChatId())
			.setText(game.getSettings().getLang().stopByAdmin(textmentionplayerlist(game.getPlays())))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(new ReplyKeyboardRemove().setSelective(true))
		);
	}
	
	public void displayStopByBot(Game game){
		Integer votemsgid = votemsgsid.remove(game.gameid);
		if(votemsgid!=null){
			bot.sendAsync(
				new EditMessageReplyMarkup()
				.setChatId(game.getGroup().getChatId())
				.setMessageId(votemsgid)
				.setReplyMarkup(null)
			);
		}
		
		bot.sendAsync(
			new SendMessage()
			.setChatId(game.getGroup().getChatId())
			.setText(game.getSettings().getLang().stopByBot(textmentionplayerlist(game.getPlays())))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(new ReplyKeyboardRemove().setSelective(true))
		);
	}
	
	public void display(GameHistory history, Game game){
		ArrayList<RecordEntry> entries = history.getEntries();
		StringBuilder strb = new StringBuilder();
		Lang lang = game.getSettings().getLang();
		entries.forEach(entry->{
			switch(entry.type){
			case RecordEntry.PLAY_CARD:
				appendPlayCardRecordTo(entry, lang, strb);
				strb.append('\n');
				break;
			case RecordEntry.PASS     :
				strb.append(lang.passAccept(escapeddisplayname(entry.play.player)))
				.append('\n');
				break;
			case RecordEntry.LEAVE    :
				strb.append(lang.leaveAccept(escapeddisplayname(entry.play.player)))
				.append('\n');
				break;
			case RecordEntry.RESULT   :
				appendResultTo(entry.result, game, strb);
				strb.append('\n');
				break;
			case RecordEntry.SKIPPED  :
				strb.append(lang.playerSkipped(escapeddisplayname(entry.play.player)))
				.append('\n');
				break;
			}
		});
		
		bot.sendAsync(
			new SendMessage()
			.setChatId(game.getGroup().groupid)
			.setText(strb.toString())
			.setParseMode(ParseMode.HTML)
		);
	}
	
	public void displayListHand(Game game){
		StringBuilder strb = new StringBuilder();
		Play[] plays = game.getPlays();
		for(Play play : plays){
			strb
			.append(escapeddisplayname(play.player))
			.append('\n');
			play.getHand().appendNumberOfHandcardsArtTo(strb);
			strb.append('\n');
		}
		
		SendMessage sdmsg = new SendMessage()
		.setChatId(game.getGroup().getChatId())
		.setText(strb.toString())
		.setParseMode(ParseMode.HTML);
		
		bot.sendAsync(sdmsg);
	}
	
	public void displayAbout(Group group){
		Lang lang = group.getSettings().getLang();
		
		bot.sendAsync(
			new SendMessage()
			.setChatId(group.getChatId())
			.setText(lang.about(bot.VERSION))
			.setParseMode(ParseMode.HTML)
		);
	}
	
	
	// remarks: MUST develop a codegen for the followings  _(:( Z)_
	private String gensettingscbqd
	(String cmd_append, long groupid, char diff)
	{
		return new StringBuilder()
		.append(cmd_append)
		.append(groupid)
		.append(' ')
		.append(diff)
		.toString();
	}
	
	private String gensettingscbqd
	(String cmd_append, long groupid, String params, char diff)
	{
		return new StringBuilder()
		.append(cmd_append)
		.append(groupid)
		.append(' ')
		.append(params)
		.append(' ')
		.append(diff)
		.toString();
	}
	
	public void displaySetGameSettings
	(Long chatid, Group group, String title, char diff)
	{
		Lang lang = group.getSettings().getLang();
		bot.sendAsync(
			new SendMessage()
			.setChatId(chatid)
			.setText(
				lang.gameSettings(escapedGroupName(title))
			)
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnGamerule())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_SET_GAMERULES_APPEND)
								.append(group.getChatId()).append(' ')
								.append(diff)
								.toString()
							),
							new InlineKeyboardButton()
							.setText(lang.btnLanguage())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_SET_LANGUAGE_APPEND)
								.append(group.getChatId()).append(' ')
								.append(diff)
								.toString()
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSave())
							.setCallbackData(CBQD_SAVE + " " + group.groupid)
						)
					)
				)
			)
		);
	}
	
	public void displaySetGameSettings
	(long groupid, char diff, String title, Long chatid, Integer msgid)
	{
		Lang lang = new GroupSettings(groupid).getLang();
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(
				lang.gameSettings(escapedGroupName(title))
			)
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnGamerule())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_SET_GAMERULES_APPEND)
								.append(groupid).append(' ')
								.append(diff)
								.toString()
							),
							new InlineKeyboardButton()
							.setText(lang.btnLanguage())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_SET_LANGUAGE_APPEND)
								.append(groupid).append(' ')
								.append(diff)
								.toString()
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSave())
							.setCallbackData(CBQD_SAVE + " " + groupid)
						)
					)
				)
			)
		);
	}
	
	public void displaySetGameRule
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		Lang lang = new GroupSettings(groupid).getLang();
		if(lang == null) lang = Lang.primaryLang;
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.gameruleDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnNcard())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_NCARD_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnAllowTriple())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_ALLOW_TRIPLE_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSv())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SV_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnCctc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_CCTC_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SC_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnFc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_FC_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SETTINGS_APPEND,
									groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displaySetNCard
	(long groupid, char diff, long chatid, int msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		if(settings != null){
			Lang lang = settings.getLang();
			if(lang == null) lang = Lang.primaryLang;
			
			int ncard = settings.getNCard();
			if(ncard != 10 && ncard != 13) ncard = 10;
			
			String msgtxt;
			msgtxt = lang.ncardDescription();
			
			String btn10txt, btn10cbqd, btn13txt, btn13cbqd;
			StringBuilder	strbbtn10txt = new StringBuilder(),
							strbbtn13txt = new StringBuilder();
			if(ncard == 10){
				strbbtn10txt.append(emoji_check_mark).append(' ');
			}else{
				strbbtn13txt.append(emoji_check_mark).append(' ');
			}
			
			strbbtn10txt.append("10");
			strbbtn13txt.append("13");
			
			btn10txt = strbbtn10txt.toString();
			btn10cbqd = gensettingscbqd(CBQD_SET_NCARD_10_APPEND, groupid, diff);
			btn13txt = strbbtn13txt.toString();
			btn13cbqd = gensettingscbqd(CBQD_SET_NCARD_13_APPEND, groupid, diff);
			
			bot.sendAsync(
				new EditMessageText()
				.setChatId(chatid)
				.setMessageId(msgid)
				.setText(msgtxt)
				.setParseMode(ParseMode.HTML)
				.setReplyMarkup(
					new InlineKeyboardMarkup()
					.setKeyboard(
						Arrays.asList(
							Arrays.asList(
								new InlineKeyboardButton()
								.setText(btn10txt)
								.setCallbackData(btn10cbqd)
								,
								new InlineKeyboardButton()
								.setText(btn13txt)
								.setCallbackData(btn13cbqd)
							),
							Arrays.asList(
								new InlineKeyboardButton()
								.setText(lang.btnBack())
								.setCallbackData(
									gensettingscbqd(
										CBQD_SET_GAMERULES_APPEND,
										groupid,
										diff
									)
								)
							)
						)
					)
				)
			);
		}
	}
	
	public void displaySetAllowTriple
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		
		Lang lang = settings.getLang();
		
		boolean allowtriple = settings.getAllowTriple();
		
		
		StringBuilder	strbbtnallowtxt = new StringBuilder(),
						strbbtndisallowtxt = new StringBuilder(),
						strbcurrbtn;
		
		if(allowtriple)
			strbcurrbtn = strbbtnallowtxt;
		else
			strbcurrbtn = strbbtndisallowtxt;
		
		strbcurrbtn.append(emoji_check_mark).append(' ');
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.allowTripleDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(
								strbbtnallowtxt
								.append(lang.btnAllow())
								.toString()
							)
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_ALLOW_TRIPLE_TRUE_APPEND,
									groupid,
									diff
								)
							),
							new InlineKeyboardButton()
							.setText(
								strbbtndisallowtxt
								.append(lang.btnDisallow())
								.toString()
							)
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_ALLOW_TRIPLE_FALSE_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_GAMERULES_APPEND,
									groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	private StringBuilder appendSVCTo(int begin, StringBuilder strb){
		int index = begin - 1;
		for(int i=index, iend=(i+5)%(13); i!=iend; i=(i+1)%13)
			strb.append(Rank.RANKS[i].text);
		return strb;
	}
	
	private StringBuilder appendSVCTo(StraightValidatorChain svc, StringBuilder strb){
		appendSVCTo(svc.BEGIN, strb).append('~');
		return appendSVCTo(svc.END, strb);
	}
	
	public void displaySetSVC
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		StraightValidatorChain svc = (StraightValidatorChain)settings.getStraightValidator();
		
		StringBuilder strbsvc = new StringBuilder();
		appendSVCTo(svc, strbsvc);
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.svcDescription(strbsvc.toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSetSvc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SVC_BEGIN_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_GAMERULES_APPEND,
									groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displaySetSVCbegin
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		
		List<List<InlineKeyboardButton>> btns = new ArrayList<>();
		for(int i=0; i<13; ++i){
			int begin = i+1;
			InlineKeyboardButton btn = 
				new InlineKeyboardButton()
				.setText(appendSVCTo(begin, new StringBuilder()).toString())
				.setCallbackData(
					gensettingscbqd(
						CBQD_SET_SVC_END_APPEND,
						groupid,
						""+begin,
						diff
					)
				);
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			row.add(btn);
			btns.add(row);
		}
		ArrayList<InlineKeyboardButton> rowback = new ArrayList<>();
		rowback.add(
			new InlineKeyboardButton()
			.setText(lang.btnBack())
			.setCallbackData(gensettingscbqd(CBQD_SET_SV_APPEND, groupid, diff))
		);
		btns.add(rowback);
			
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.svcBeginDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(btns)
			)
		);
	}
	
	public void displaySetSVCend
	(long groupid, char diff, int begin, Long chatid, Integer msgid)
	{
		Lang lang = new GroupSettings(groupid).getLang();
		
		StringBuilder strbsvcbegin = new StringBuilder(5);
		appendSVCTo(begin, strbsvcbegin);
		
		ArrayList<List<InlineKeyboardButton>> btns = new ArrayList<>();
		for(int i=begin; i<=13; ++i){
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			StringBuilder strbbtn = new StringBuilder(),
				strbparam = new StringBuilder();
			strbparam.append(begin).append(' ').append(i);
			InlineKeyboardButton btn = new InlineKeyboardButton()
				.setText(appendSVCTo(i, strbbtn).toString())
				.setCallbackData(gensettingscbqd(CBQD_SET_SVC_END_APPEND, groupid, strbparam.toString(), diff));
			row.add(btn);
			btns.add(row);
		}
		ArrayList<InlineKeyboardButton> rowback = new ArrayList<>();
		rowback.add(
			new InlineKeyboardButton()
			.setText(lang.btnBack())
			.setCallbackData(gensettingscbqd(CBQD_SET_SV_APPEND, groupid, diff))
		);
		btns.add(rowback);
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.svcEndDescription(strbsvcbegin.toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(btns))
		);
	}
	
	public void displaySetCCTC
	(long groupid, char diff, Long chatid, Integer msgid){
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		boolean cctc = settings.getCardCombinationRule().getCanStraightBeatFlush();
		
		StringBuilder	strbfs = new StringBuilder(),
						strbsf = new StringBuilder(),
						strbcurr;
		
		if(cctc){
			strbcurr = strbsf;
		}else{
			strbcurr = strbfs;
		}
		strbcurr.append(emoji_check_mark).append(' ');
		strbfs.append(lang.btnFS());
		strbsf.append(lang.btnSF());
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.cctcDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbfs.toString())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_FS_APPEND,
									groupid,
									diff
								)
							),
							new InlineKeyboardButton()
							.setText(strbsf.toString())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SF_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_GAMERULES_APPEND
									,groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	private StringBuilder appendSCCTo(int sccdata, StringBuilder strb){
		final String STR3 = "34567~910JQK";
		if(sccdata == 3){
			strb.append(STR3);
		}
		else{
			for(int i=sccdata-1, iend=(i+5)%13; i!=iend; i=(i+1)%13)
				strb.append(Rank.RANKS[i].text);
		}
		return strb;
	}
	
	private StringBuilder appendSCCTo(int[] sccdata, StringBuilder strb){
		if(sccdata.length > 0){
			strb.append(sccdata);
			for(int i=1; i<sccdata.length; ++i)
				appendSCCTo(i, strb.append('<'));
		}
		return strb;
	}
	
	private StringBuilder appendSCCTo(StraightComparatorCustom scc, StringBuilder strb){
		return appendSCCTo(scc.getRule(), strb);
	}
	
	private StringBuilder appendSCTo(StraightComparator sc, Lang lang, StringBuilder strb){
		if(sc.getComparatorType() == StraightComparators.TYPE_RANKING_FIRST){
			strb.append(lang.btnRankingFirst());
		}else{
			appendSCCTo((StraightComparatorCustom) sc, strb);
		}
		return strb;
	}
	
	public void displaySetSC
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		StraightComparator sc = settings.getCardCombinationRule().getStraightComparator();
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.scDescription(appendSCTo(sc, lang, new StringBuilder()).toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnRankingFirst())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SC_RANKING_FIRST_APPEND,
									groupid,
									diff
								)
							),
							new InlineKeyboardButton()
							.setText(lang.btnCustom())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_SC_CUSTOM_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_GAMERULES_APPEND
									,groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displaySetSCC
	(long groupid, char diff, 
	 int[] begins, int[] remains, 
	 Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		
		ArrayList<List<InlineKeyboardButton>> btns = new ArrayList<>();
		StringBuilder strbparams = new StringBuilder();
		strbparams.append(CBQD_SET_SCC_APPEND).append(groupid).append(' ');
		for(int i=0; i<begins.length; ++i){
			strbparams.append(begins[i]).append(' ');
		}
		String strparams = strbparams.toString();
		for(int i=0; i<remains.length; ++i){
			int remain = remains[i];
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			StringBuilder strbspecparam 
				= new StringBuilder(strparams)
				.append(remain)
				.append(' ')
				.append(diff);
			row.add(
				new InlineKeyboardButton()
				.setText(appendSCCTo(remain, new StringBuilder()).toString())
				.setCallbackData(strbspecparam.toString())
			);
			btns.add(row);
		}
		// back button
		ArrayList<InlineKeyboardButton> rowback = new ArrayList<>();
		rowback.add(
			new InlineKeyboardButton()
			.setText(lang.btnBack())
			.setCallbackData(gensettingscbqd(CBQD_SET_SC_APPEND, groupid, diff))
		);
		btns.add(rowback);	
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.scCustomDescription(appendSCCTo(begins, new StringBuilder()).toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(btns)
			)
		);
	}
	
	public void displaySetFC
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		FlushComparator fc = settings.getFlushComparator();
		
		StringBuilder	strbsf = new StringBuilder(),
						strbcf = new StringBuilder(),
						strbrf = new StringBuilder(),
						currstrb;
		
		switch(fc.type()){
		case FlushComparators.TYPE_SUIT_FIRST:
			currstrb = strbsf;
			break;
		case FlushComparators.TYPE_CARD_FIRST:
			currstrb = strbcf;
			break;
		case FlushComparators.TYPE_RANKING_FIRST:
		default:
			currstrb = strbrf;
			break;
		}
		
		currstrb.append(emoji_check_mark).append(' ');
		strbsf.append(lang.btnSuitFirst());
		strbcf.append(lang.btnCardFirst());
		strbrf.append(lang.btnRankingFirst());
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.fcDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbsf.toString())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_FC_SUIT_FIRST_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbcf.toString())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_FC_CARD_FIRST_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbrf.toString())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_FC_RANKING_FIRST_APPEND,
									groupid,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_SET_GAMERULES_APPEND
									,groupid,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displaySetLanguage
	(long groupid, char diff, Long chatid, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(groupid);
		Lang lang = settings.getLang();
		
		ArrayList<String> langsname = Lang.getLangsName();
		
		ArrayList<List<InlineKeyboardButton>> btns = new ArrayList<>();
		for(int i=0, iend=langsname.size()+1; i<iend; ){
			ArrayList<InlineKeyboardButton> row = new ArrayList<>();
			for(int j=0,jend=2; i<langsname.size() && j<jend; ++i, ++j){
				row.add(
					new InlineKeyboardButton()
					.setText(langsname.get(i))
					.setCallbackData(
						new StringBuilder()
						.append(CBQD_SET_LANGUAGE).append(' ')
						.append(groupid).append(' ')
						.append(langsname.get(i)).append(' ')
						.append(diff)
						.toString()	
					)
				);
			}
			if(i==langsname.size()){
				if(row.size()==1){
				}else{
					btns.add(row);
					row = new ArrayList<>();
				}
				row.add(
					new InlineKeyboardButton()
					.setText(lang.btnBack())
					.setCallbackData(
						new StringBuilder()
						.append(CBQD_SET_SETTINGS_APPEND)
						.append(groupid).append(' ')
						.append(diff)
						.toString()	
					)
				);
				++i;
			}
			btns.add(row);
		}
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.languageDescription(lang.getLangName()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(btns))
		);
	}
	
	public void displaySave
	(long groupid, String title, Long chatid, Integer msgid)
	{
		GroupSettings settings = new GroupSettings(groupid);
		Lang lang = settings.getLang();
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.saveDescription(title))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(null)
		);
	}
	
	public void displayNotGroupAdmin
	(long groupid, String title, Long chatid, Integer msgid)
	{
		GroupSettings settings = new GroupSettings(groupid);
		Lang lang = settings.getLang();
		bot.sendAsync(
			new EditMessageText()
			.setChatId(chatid)
			.setMessageId(msgid)
			.setText(lang.notGroupAdmin(title))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(null)
		);
	}
	
	
	private String gensettingscbqd
	(String cmd_append, char diff)
	{
		return new StringBuilder()
		.append(cmd_append)
		.append(' ')
		.append(diff)
		.toString();
	}

	public void displayViewGameSettings
	(Group group, String title, char diff)
	{
		Lang lang = group.getSettings().getLang();
		bot.sendAsync(
			new SendMessage()
			.setChatId(group.getChatId())
			.setText(
				lang.gameSettings(escapedGroupName(title))
			)
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnGamerule())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_VIEW_GAMERULES_APPEND)
								.append(diff)
								.toString()
							)
						)
					)
				)
			)
		);
	}
	
	public void displayViewGameSettings
	(Group group, String title, char diff, Integer msgid)
	{
		Lang lang = group.getSettings().getLang();
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(
				lang.gameSettings(escapedGroupName(title))
			)
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnGamerule())
							.setCallbackData(
								new StringBuilder()
								.append(CBQD_VIEW_GAMERULES_APPEND)
								.append(diff)
								.toString()
							)
						)
					)
				)
			)
		);
	}
	
	public void displayViewGameRule
	(Group group, char diff, Integer msgid)
	{
		Lang lang = group.getSettings().getLang();
		if(lang == null) lang = Lang.primaryLang;
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.gameruleDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnNcard())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_NCARD,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnAllowTriple())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_ALLOW_TRIPLE,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSv())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_SV,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnCctc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_CCTC,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnSc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_SC,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnFc())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_FC,
									diff
								)
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_SETTINGS,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displayViewNCard
	(Group group, char diff, int msgid)
	{
		GameSettings settings = GameSettings.getSettings(group.groupid);
		if(settings != null){
			Lang lang = settings.getLang();
			if(lang == null) lang = Lang.primaryLang;
			
			int ncard = settings.getNCard();
			if(ncard != 10 || ncard != 13) ncard = 10;
			
			String msgtxt;
			msgtxt = lang.ncardDescription();
			
			String btn10txt, btn10cbqd, btn13txt, btn13cbqd;
			StringBuilder	strbbtn10txt = new StringBuilder(),
							strbbtn13txt = new StringBuilder();
			if(ncard == 10){
				strbbtn10txt.append(emoji_check_mark).append(' ');
			}else{
				strbbtn13txt.append(emoji_check_mark).append(' ');
			}
			strbbtn10txt.append("10");
			strbbtn13txt.append("13");			
			
			btn10txt = strbbtn10txt.toString();
			btn10cbqd = CBQD_MEANINGLESS_RESPONSE;
			btn13txt = strbbtn13txt.toString();
			btn13cbqd = CBQD_MEANINGLESS_RESPONSE;
			
			bot.sendAsync(
				new EditMessageText()
				.setChatId(group.getChatId())
				.setMessageId(msgid)
				.setText(msgtxt)
				.setParseMode(ParseMode.HTML)
				.setReplyMarkup(
					new InlineKeyboardMarkup()
					.setKeyboard(
						Arrays.asList(
							Arrays.asList(
								new InlineKeyboardButton()
								.setText(btn10txt)
								.setCallbackData(btn10cbqd)
								,
								new InlineKeyboardButton()
								.setText(btn13txt)
								.setCallbackData(btn13cbqd)
							),
							Arrays.asList(
								new InlineKeyboardButton()
								.setText(lang.btnBack())
								.setCallbackData(
									gensettingscbqd(
										CBQD_VIEW_GAMERULES,
										diff
									)
								)
							)
						)
					)
				)
			);
		}
	}
	
	public void displayViewAllowTriple
	(Group group, char diff, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(group.groupid);
		
		Lang lang = settings.getLang();
		
		boolean allowtriple = settings.getAllowTriple();
		
		
		StringBuilder	strbbtnallowtxt = new StringBuilder(),
						strbbtndisallowtxt = new StringBuilder(),
						strbcurrbtn;
		
		if(allowtriple)
			strbcurrbtn = strbbtnallowtxt;
		else
			strbcurrbtn = strbbtndisallowtxt;
		strbcurrbtn.append(emoji_check_mark).append(' ');
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.allowTripleDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(
								strbbtnallowtxt
								.append(lang.btnAllow())
								.toString()
							)
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							),
							new InlineKeyboardButton()
							.setText(
								strbbtndisallowtxt
								.append(lang.btnDisallow())
								.toString()
							)
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_GAMERULES,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	
	public void displayViewSVC
	(Group group, char diff, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(group.groupid);
		Lang lang = settings.getLang();
		StraightValidatorChain svc = (StraightValidatorChain)settings.getStraightValidator();
		
		StringBuilder strbsvc = new StringBuilder();
		appendSVCTo(svc, strbsvc);
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.svcDescription(strbsvc.toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_GAMERULES,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displayViewCCTC
	(Group group, char diff, Integer msgid){
		GameSettings settings = GameSettings.getSettings(group.groupid);
		Lang lang = settings.getLang();
		boolean cctc = settings.getCardCombinationRule().getCanStraightBeatFlush();
		
		StringBuilder	strbfs = new StringBuilder(),
						strbsf = new StringBuilder(),
						strbcurr;
		
		if(cctc){
			strbcurr = strbsf;
		}else{
			strbcurr = strbfs;
		}
		strbcurr.append(emoji_check_mark).append(' ');
		strbfs.append(lang.btnFS());
		strbsf.append(lang.btnSF());
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.cctcDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbfs.toString())
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							),
							new InlineKeyboardButton()
							.setText(strbsf.toString())
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_GAMERULES,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	
	public void displaySC
	(Group group, char diff, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(group.groupid);
		Lang lang = settings.getLang();
		StraightComparator sc = settings.getCardCombinationRule().getStraightComparator();
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.scDescription(appendSCTo(sc, lang, new StringBuilder()).toString()))
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_GAMERULES,
									diff
								)
							)
						)
					)
				)
			)
		);
	}
	
	public void displayFC
	(Group group, char diff, Integer msgid)
	{
		GameSettings settings = GameSettings.getSettings(group.groupid);
		Lang lang = settings.getLang();
		FlushComparator fc = settings.getFlushComparator();
		
		StringBuilder	strbsf = new StringBuilder(),
						strbcf = new StringBuilder(),
						strbrf = new StringBuilder(),
						currstrb;
		
		switch(fc.type()){
		case FlushComparators.TYPE_SUIT_FIRST:
			currstrb = strbsf;
			break;
		case FlushComparators.TYPE_CARD_FIRST:
			currstrb = strbcf;
			break;
		case FlushComparators.TYPE_RANKING_FIRST:
		default:
			currstrb = strbrf;
			break;
		}
		
		currstrb.append(emoji_check_mark).append(' ');
		strbsf.append(lang.btnSuitFirst());
		strbcf.append(lang.btnCardFirst());
		strbrf.append(lang.btnRankingFirst());
		
		bot.sendAsync(
			new EditMessageText()
			.setChatId(group.getChatId())
			.setMessageId(msgid)
			.setText(lang.fcDescription())
			.setParseMode(ParseMode.HTML)
			.setReplyMarkup(
				new InlineKeyboardMarkup()
				.setKeyboard(
					Arrays.asList(
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbsf.toString())
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbcf.toString())
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(strbrf.toString())
							.setCallbackData(
								CBQD_MEANINGLESS_RESPONSE
							)
						),
						Arrays.asList(
							new InlineKeyboardButton()
							.setText(lang.btnBack())
							.setCallbackData(
								gensettingscbqd(
									CBQD_VIEW_GAMERULES,
									diff
								)
							)
						)
					)
				)
			)
		);
	}


	public void displayAbout
	(Long chatid)
	{
		Lang lang = new GroupSettings(chatid).getLang();
		bot.sendAsync(
			new SendMessage()
			.setChatId(chatid)
			.setText(lang.about(bot.VERSION))
			.setParseMode(ParseMode.HTML)
		);
	}
	
	public void displayMeaninglessResponse
	(String cbqid)
	{
		bot.sendAsync(
			new AnswerCallbackQuery()
			.setCallbackQueryId(cbqid)
			.setText(".")
			.setShowAlert(false)
		);
	}
	
	public void displayDebugGame(Game game, long chatid){
		bot.sendAsync(
			new SendMessage()
			.setChatId(chatid)
			.setText(game.toString())
		);
	}
	
	public void displayDebugPlay(Play play, long chatid){
		bot.sendAsync(
			new SendMessage()
			.setChatId(chatid)
			.setText(play.toString())
		);
	}
	
	public void displayDebugRkr(long chatid){
		bot.sendAsync(
			new SendMessage()
			.setChatId(chatid)
			.setText("remove reply keyboard")
			.setReplyMarkup(
				new ReplyKeyboardRemove()
			)
		);
	}
}