package bot;

import org.telegram.telegrambots.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;

import StringView.StringView;
import bigtwo.cardcombination.CardCombinationTypeComparator;
import bigtwo.cardcombination.FlushComparatorCardFirst;
import bigtwo.cardcombination.FlushComparatorRankingFirst;
import bigtwo.cardcombination.FlushComparatorSuitFirst;
import bigtwo.cardcombination.StraightComparatorCustom;
import bigtwo.cardcombination.StraightComparatorRankingFirst;
import bigtwo.cardcombination.StraightValidatorChain;
import bigtwo.game.BigTwoSystem;
import bigtwo.game.Game;
import bigtwo.game.GameSettings;
import bigtwo.game.Group;
import bigtwo.game.InitiateSkip;
import bigtwo.game.Join;
import bigtwo.game.NewGame;
import bigtwo.game.Play;
import bigtwo.game.PlayCard;
import bigtwo.game.PlayCardResult;
import bigtwo.game.Player;
import bigtwo.card.Card;

import static bot.Constants.*;

public class Controller{
	private View view;
	private BigTwoBot bot;
	
	public Controller(View view, BigTwoBot bot){this.view = view;this.bot=bot;}
	
	public void handleCommand(String cmd, Message msg){
		if(cmd.equals(Constants.NEW_GAME_CMD_NAME      )){
			if(msg.getChat().isGroupChat() || msg.getChat().isSuperGroupChat())
			{	
				Player player = Player.getPlayer(msg.getFrom());
				Group group = Group.getGroup(msg.getChat());
				NewGame newgame = BigTwoSystem.newGame(player, group);
				view.display(newgame);
			}
		}
		else if(cmd.equals(Constants.JOIN_CMD_NAME          )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			Join join = BigTwoSystem.join(
				player, 
				group
			);
			view.display(join);
		}
		else if(cmd.equals(Constants.LEAVE_CMD_NAME         )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			view.display(
				BigTwoSystem.leave(player, group)
			);
		}
		else if(cmd.equals(Constants.STARTGAME_CMD_NAME     )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			view.display(
				BigTwoSystem.startGamePlayerForce(player, group)
			);
		}
		else if(cmd.equals(Constants.CALL_IKB_CMD_NAME      )){
			// suspended
		}
		else if(cmd.equals(Constants.LIST_HAND_CMD_NAME     )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			Game game = group.getGame();
			if(game != null){
				view.displayListHand(
					game
				);
			}		
		}
		else if(cmd.equals(Constants.VIEW_HISTORY_CMD_NAME  )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			Game game = group.getGame();
			if(game != null){
				view.display(
					game.getHistory(),
					game
				);
			}
		}
		else if(cmd.equals(Constants.INITIATE_SKIP_CMD_NAME )){
			Player player = Player.getPlayer(msg.getFrom());
			Group group = Group.getGroup(msg.getChat());
			Play play = player.getPlay();
			Game game = group.getGame();
			if(play != null && game != null && play.game == game){
				InitiateSkip iskip = BigTwoSystem.initiateSkip(play);
				if(iskip.result == InitiateSkip.InitiateSkipResult.ACCEPT){
					view.display(
						BigTwoSystem.voteskip(play)
					);
				}else{
					view.display(iskip);
				}
			}
		}
		else if(cmd.equals(Constants.SETTING_CMD_NAME       )){
			if(msg.getChat().isGroupChat() || msg.getChat().isSuperGroupChat())
			{
				Group group = Group.getGroup(msg.getChat());
				if(bot.isAdministrator(msg.getFrom(), msg.getChat())){
					view.displaySetGameSettings(
						new Long(msg.getFrom().getId()),
						group,
						msg.getChat().getTitle(),
						'0'
					);
				}else{
					view.displayViewGameSettings(group, msg.getChat().getTitle(), '0');
				}
			}
		}
		else if(cmd.equals(Constants.SET_GROUP_LINK_CMD_NAME)){
			// suspended
		}
		else if(cmd.equals(Constants.STOP_GAME_CMD_NAME     )){
			Group group = Group.getGroup(msg.getChat());
			Game game = group.getGame();
			if(game != null){
				if(bot.isAdministrator(msg.getFrom(), msg.getChat())){
					BigTwoSystem.stopByAdmin(game);
					view.displayStopByAdmin(game);
				}
			}
		}
		else if(cmd.equals(Constants.ABOUT_CMD_NAME         )){
			Group group = Group.getGroup(msg.getChat());
			view.displayAbout(group);
		}
		else if(cmd.equals(Constants.DEBUG_GAME_CMD_NAME)){
			Group group = Group.getGroup(msg.getChat());
			Game game = group.getGame();
			if(game != null){
				view.displayDebugGame(game, msg.getChatId());
			}
		}
		else if(cmd.equals(Constants.DEBUG_PLAY_CMD_NAME)){
			Player player = Player.getPlayer(msg.getFrom());
			Play play = player.getPlay();
			if(play != null){
				view.displayDebugPlay(play, msg.getChatId());
			}
		}
		else if(cmd.equals(Constants.DEBUG_REPLY_KEYBOARD_REMOVE)){
			view.displayDebugRkr(msg.getChatId());
		}
	}
	
	public void handleMessage(Message msg){
		if(msg.hasText()){
			String text = msg.getText();
			Player player = Player.getCachedPlayer(msg.getFrom().getId());
			Group group = Group.getCachedGroup(msg.getChatId());
			
			if(player != null && group != null){
				Play play = player.getPlay();
				Game game = group.getGame();
				
				if(play != null && game != null & play.game == game){
					if(text.equals(MSGTXT_PASS)){
						view.display(BigTwoSystem.pass(play));
					}else if(text.equals(MSGTXT_PLAY)){
						PlayCardResult playcard = BigTwoSystem.playcard(play);
						if(playcard.playcard.result == PlayCard.Result.ACCEPT){
							view.display(playcard);
						}
					}else{
						String[] cardstext = text.split(" ");
						Card[] cards = new Card[cardstext.length];
						int i;
						for(i=0; i<cardstext.length; ++i){
							StringView strv = new StringView(cardstext[i]);
							Card card = Card.fromText(strv);
							if(card != null)
								cards[i] = card;
							else
								break;
						}
						if(i ==	cardstext.length){
							BigTwoSystem.selectCard(play, cards);
						}
					}
				}
			}
		}
	}
	
	public void handleCallbackQuery(CallbackQuery cbq){
		String[] data = cbq.getData().split(" ");
		if(data.length > 0){
			String key = data[0];
			if(key.equals(CBQD_SELECT_CARD)){
				Player player = Player.getCachedPlayer(cbq.getFrom().getId());
				Group group;
				try{
					group = Group.getCachedGroup(Long.parseLong(cbq.getChatInstance()));
				}catch(NumberFormatException ex){
					group = null;
				}
				if(player != null && group != null){
					Play play = player.getPlay();
					Game game = group.getGame();
					if(play != null && game != null && play.game == game){
						if(data.length >= 2)
							try{
								view.display(
									BigTwoSystem.selectCard(play, Integer.parseInt(data[1])),
									cbq
								);
							}catch(NumberFormatException ex){};
					}
				}
			}else if(key.equals(CBQD_PLAY_CARD)){
				Player player = Player.getCachedPlayer(cbq.getFrom().getId());
				Group group;
				try{
					group = Group.getCachedGroup(Long.parseLong(cbq.getChatInstance()));
				}catch(NumberFormatException ex){
					group = null;
				}
				if(player != null && group != null){
					Play play = player.getPlay();
					Game game = group.getGame();
					if(play != null && game != null && play.game == game){
						view.display(
							BigTwoSystem.playcard(play),
							cbq
						);
					}
				}
			}else if(key.equals(CBQD_PASS)){
				Player player = Player.getCachedPlayer(cbq.getFrom().getId());
				Group group;
				try{
					group = Group.getCachedGroup(Long.parseLong(cbq.getChatInstance()));
				}catch(NumberFormatException ex){
					group = null;
				}
				if(player != null && group != null){
					Play play = player.getPlay();
					Game game = group.getGame();
					if(play != null && game != null && play.game == game){
						view.display(BigTwoSystem.pass(play), cbq);
					}
				}
			}else if(key.equals(CBQD_UNSELECTALL)){
				Player player = Player.getCachedPlayer(cbq.getFrom().getId());
				Group group;
				try{
					group = Group.getCachedGroup(Long.parseLong(cbq.getChatInstance()));
				}catch(NumberFormatException ex){
					group = null;
				}
				if(player != null && group != null){
					Play play = player.getPlay();
					Game game = group.getGame();
					if(play != null && game != null && play.game == game){
						if(BigTwoSystem.unselectAll(play))
							view.displayUnselectAll(play, cbq);
					}
				}
			}else if(key.equals(CBQD_VOTE_SKIP)){
				Player player = Player.getCachedPlayer(cbq.getFrom().getId());
				Group group;
				try{
					group = Group.getCachedGroup(Long.parseLong(cbq.getChatInstance()));
				}catch(NumberFormatException ex){
					group = null;
				}
				if(player != null && group != null){
					Play play = player.getPlay();
					Game game = group.getGame();
					if(play != null && game != null && play.game == game){
						view.display(BigTwoSystem.voteskip(play));
					}
				}
			}else if(key.equals(CBQD_SET_PREFIX)){
				if(data.length>=4){
					String subkey = data[1];
					if(subkey.equals(CBQD_SETTINGS          )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							String title;
							try{
								Chat chat = bot.send(new GetChat().setChatId(groupid));
								title = chat.getTitle();
							}catch(Exception ex){
								title = "";
							}
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetGameSettings(groupid, diff, title, chatid, msgid);
							}else{
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_GAMERULES         )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetGameRule(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_LANGUAGE          )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetLanguage(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==5){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[4].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							String langname = data[3];
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								Lang lang = Lang.getLang(langname);
								if(lang!=null){
									GameSettings.setLang(groupid, lang);
								}
							
								view.displaySetLanguage(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_NCARD             )){
						if(data.length==5){
							int ncard;
							String strncard = data[2];
							
							long groupid;
							try{groupid=Long.parseLong(data[3]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[4].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								if(strncard.equals(CBQD_NCARD_10)){
									GameSettings.setNCard(groupid, 10);
								}else if(strncard.equals(CBQD_NCARD_13)){
									GameSettings.setNCard(groupid, 13);
								}
							
								view.displaySetNCard(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetNCard(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_ALLOW_TRIPLE      )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetAllowTriple(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==5){
							long groupid;
							try{groupid=Long.parseLong(data[3]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[4].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								
								String allowtriple = data[2];
								if(allowtriple.equals(CBQD_TRUE)){
									GameSettings.setAllowTriple(groupid, true);
								}else if(allowtriple.equals(CBQD_FALSE)){
									GameSettings.setAllowTriple(groupid, false);
								}
							
								view.displaySetAllowTriple(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_SV                )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetSVC(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==5){
							if(CBQD_SVC_BEGIN.equals(data[2])){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									view.displaySetSVCbegin(groupid, diff, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}
						}else if(data.length==6){
							if(CBQD_SVC_END.equals(data[2])){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[5].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									int begin;
									try{begin=Integer.parseInt(data[4]);}
									catch(NumberFormatException ex){return;};
									
									if(1<=begin && begin <=13){
										view.displaySetSVCend(groupid, diff, begin, chatid, msgid);
									}
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}
						}else if(data.length==7){
							if(CBQD_SVC_END.equals(data[2])){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[6].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									int begin, end;
									try{begin=Integer.parseInt(data[4]);
										end=Integer.parseInt(data[5]);}
									catch(NumberFormatException ex){return;};
									
									if(1 <= begin && begin <= end && end <= 13){
										StraightValidatorChain svc = StraightValidatorChain.getValidator(begin, end);
										GameSettings.setStraightValidator(groupid, svc);
										
										view.displaySetSVC(groupid, diff, chatid, msgid);
									}
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}
						}
					}else if(subkey.equals(CBQD_CCTC              )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetCCTC(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==5){
							long groupid;
							try{groupid=Long.parseLong(data[3]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[4].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								
								String strcctc = data[2];
								CardCombinationTypeComparator cctc = null;
								if(strcctc.equals(CBQD_SF)){
									cctc = CardCombinationTypeComparator.getComparator(true);
								}else if(strcctc.equals(CBQD_FS)){
									cctc = CardCombinationTypeComparator.getComparator(false);
								}
								
								if(cctc != null){
									GameSettings.setCardCombinationTypeComparator(groupid, cctc);
								
									view.displaySetCCTC(groupid, diff, chatid, msgid);
								}
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_SC                )){
						if(data.length==4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[3].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								view.displaySetSC(groupid, diff, chatid, msgid);
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}else if(data.length==5){
							String param = data[2];
							if(param.equals(CBQD_RANKING_FIRST)){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									GameSettings.setStraightComparator(groupid, StraightComparatorRankingFirst.straightComparatorRankingFirst);
								
									view.displaySetSC(groupid, diff, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}else if(param.equals(CBQD_CUSTOM)){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									StraightValidatorChain svc = (StraightValidatorChain) GameSettings.getSettings(groupid).getStraightValidator();
									int remainssize = svc.END-svc.BEGIN+1;
									int[]	begins = new int[0],
											remains = new int[remainssize];
									for(int i=0,j=svc.BEGIN; j<=svc.END; ++i, ++j)
										remains[i] = j;
									view.displaySetSCC(groupid, diff, begins, remains, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}
						}
					}else if(subkey.equals(CBQD_SCC               )){
						if(data.length > 4){
							long groupid;
							try{groupid=Long.parseLong(data[2]);}
							catch(NumberFormatException ex){return;};
							
							char diff;
							diff=data[data.length-1].charAt(0); ++diff;
							
							Long chatid = cbq.getMessage().getChatId();
							Integer msgid = cbq.getMessage().getMessageId();
							
							if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
								int[]	begins = new int[data.length-4];
								for(int i=0, j=4; i<begins.length; ++i, ++j){
									try{
										int n = Integer.parseInt(data[j]);
										if(( 1<=n && n<=3 ) || ( 9<=n && n<=13)){
											begins[i] = n;
										}else{
											return;
										}
									}
									catch(NumberFormatException ex){return;};
								}
								
								StraightValidatorChain svc = (StraightValidatorChain) GameSettings.getSettings(groupid).getStraightValidator();
								int remainssize = svc.END-svc.BEGIN+1 - begins.length;
								if(remainssize==0){
									boolean[] flag = new boolean[svc.END-svc.BEGIN+1];
									for(int i=0; i<flag.length; ++i)
										flag[i] = false;
									for(int i=svc.BEGIN, j=0; i<=svc.END; ++i, ++j){
										int k;
										for(k=0; k<begins.length; ++k){
											if(i==begins[k]){
												if(flag[j]){
													return;
												}else{
													flag[j] = true;
												}
											}
										}
										if(!flag[j]){
											return;
										}
									}
									
									StraightComparatorCustom scc = new StraightComparatorCustom(begins);
									GameSettings.setStraightComparator(groupid, scc);
									
									view.displaySetSC(groupid, diff, chatid, msgid);
								}else{
									int[]	remains = new int[remainssize];
									int remains_index = 0;
									for(int i=svc.BEGIN; i<=svc.END; ++i){
										int j;
										for(j=0; j<begins.length; ++j){
											if(i==begins[j])
												break;
										}
										if(j==begins.length){
											remains[remains_index] = i;
											++remains_index;
										}
									}
									view.displaySetSCC(groupid, diff, begins, remains, chatid, msgid);
								}
							}else{
								String title;
								try{
									Chat chat = bot.send(new GetChat().setChatId(groupid));
									title = chat.getTitle();
								}catch(Exception ex){
									title = "";
								}
								
								view.displayNotGroupAdmin(groupid, title, chatid, msgid);
							}
						}
					}else if(subkey.equals(CBQD_FC                )){
						if(data.length == 5){
							String whatf = data[2];
							if(whatf.equals(CBQD_SUIT_FIRST   )){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									GameSettings.setFlushComparator(groupid, FlushComparatorSuitFirst.flushComparatorSuitFirst);
								
									view.displaySetFC(groupid, diff, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}else if(whatf.equals(CBQD_CARD_FIRST   )){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									GameSettings.setFlushComparator(groupid, FlushComparatorCardFirst.flushComparatorCardFirst);
								
									view.displaySetFC(groupid, diff, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}else if(whatf.equals(CBQD_RANKING_FIRST)){
								long groupid;
								try{groupid=Long.parseLong(data[3]);}
								catch(NumberFormatException ex){return;};
								
								char diff;
								diff=data[4].charAt(0); ++diff;
								
								Long chatid = cbq.getMessage().getChatId();
								Integer msgid = cbq.getMessage().getMessageId();
								
								if(bot.isAdministrator(cbq.getFrom(), ""+groupid)){
									GameSettings.setFlushComparator(groupid, FlushComparatorRankingFirst.flushComparatorRankingFirst);
								
									view.displaySetFC(groupid, diff, chatid, msgid);
								}else{
									String title;
									try{
										Chat chat = bot.send(new GetChat().setChatId(groupid));
										title = chat.getTitle();
									}catch(Exception ex){
										title = "";
									}
									
									view.displayNotGroupAdmin(groupid, title, chatid, msgid);
								}
							}
						}
					}
				}
			}else if(key.equals(CBQD_VIEW_PREFIX)){
				if(data.length == 3){
					Chat chat = cbq.getMessage().getChat();
					if(chat.isGroupChat() || chat.isSuperGroupChat()){
						Group group = Group.getGroup(chat);
						
						Integer msgid = cbq.getMessage().getMessageId();
						
						String subkey = data[1];
						
						char diff;
						try{diff=data[2].charAt(0); ++diff;}
						catch(Exception ex){return;}
						
						
						if(subkey.equals(CBQD_SETTINGS    )){
							view.displayViewGameSettings(group, chat.getTitle(), diff, msgid);
						}else if(subkey.equals(CBQD_GAMERULES   )){
							view.displayViewGameRule(group, diff, msgid);
						}else if(subkey.equals(CBQD_NCARD       )){
							view.displayViewNCard(group, diff, msgid);
						}else if(subkey.equals(CBQD_ALLOW_TRIPLE)){
							view.displayViewAllowTriple(group, diff, msgid);
						}else if(subkey.equals(CBQD_SV          )){
							view.displayViewSVC(group, diff, msgid);
						}else if(subkey.equals(CBQD_CCTC        )){
							view.displayViewCCTC(group, diff, msgid);
						}else if(subkey.equals(CBQD_SC          )){
							view.displaySC(group, diff, msgid);
						}else if(subkey.equals(CBQD_FC          )){
							view.displayFC(group, diff, msgid);
						}
					}
				}
			}else if(key.equals(CBQD_MEANINGLESS_RESPONSE)){
				view.displayMeaninglessResponse(cbq.getId());
			}else if(key.equals(CBQD_SAVE)){
				if(data.length==2){
					long groupid;
					try{groupid=Long.parseLong(data[1]);}
					catch(NumberFormatException ex){return;};
					
					String title;
					try{
						Chat chat = bot.send(new GetChat().setChatId(groupid));
						title = chat.getTitle();
					}catch(Exception ex){
						title = "";
					}
					
					view.displaySave(groupid, title, cbq.getMessage().getChatId(), cbq.getMessage().getMessageId());
				}
			}
		}
	}
}
