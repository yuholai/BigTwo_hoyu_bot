package bot;

import static bigtwo.game.Group.FIELD_GROUPID;
import static bigtwo.game.Group.TABLE_GROUP;
import static bot.Constants.MSGTXT_PASS;
import static bot.Constants.MSGTXT_PLAY;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.api.methods.groupadministration.LeaveChat;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.inlinequery.ChosenInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.stickers.Sticker;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import bigtwo.card.Card;
import bigtwo.game.BigTwoSystem;
import bigtwo.game.Game;
import bigtwo.game.GameSettings;
import bigtwo.game.Group;
import bigtwo.game.Play;
import bigtwo.game.PlayCard;
import bigtwo.game.PlayCardResult;
import bigtwo.game.Player;
import misc.logging.stdlogger.StdLogger;


public class BigTwoBot extends TelegramLongPollingBot{
//	static:
	// bot
	private static BigTwoBot _bot;
	
	
    // Database
	public static final String
	TABLE_BOT           = "bot",
	FIELD_NEXT_GAMEID   = "nextgameid",
	FIELD_NEXT_PLAYID   = "nextplayid",
	FIELD_BOT_ID        = "botid"     ;
	
	
	
    // command
    public final static String 			/*command*/
		STATUS_CMD_NAME           = "status"      ,
		STOP_MAINTENANCE_CMD_NAME = "???????"     ;
		


//	static methods:
    public static BigTwoBot bot(){return _bot;}
    
    // bot related	TODO review
	public boolean isCommandToBot(Message msg){
		if(msg == null) return false;
		String text = msg.getText();
		if(text == null)	return false;
		if(text.length() == 0) return false;
		if(text.charAt(0)!='/') return false;
		try{
	    	if(text.substring(text.indexOf('@')).startsWith("@" + BOT_USERNAME))
	    		return true;
	    	else
	    		return false;
		}catch(IndexOutOfBoundsException ex){
			return false;
		}
	}

	
	public static String solveCmd(String message){
		String r = "";
		try{
			r += message.substring(1, message.indexOf('@'));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return r;
	}
	public static void solveCmdArgs(String message, String cmdRef, String argsRef)
	/* wasted
	 * doesnt verify command*/
	{
		solveCmd(message);
		try{
			argsRef += message.substring(message.indexOf(' ') + 1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static String[] solveArgs(String cmd){
	try{
		return cmd.substring(cmd.indexOf(' ') + 1).split(" ");
	}catch(Exception ex){
		ex.printStackTrace();
	}
	return null;
}

    
//	constants:
	// bot info
	private final int    BOT_ID;
	private final int    BOT_USERID;
	private final String BOT_USERNAME;
	private final String BOT_TOKEN;
	// bot version
	public final String  VERSION;
	// bot owner
	public final int     BOT_OWNER_USERID;
	// bot database
	private final String DATABASE_CONNECTION_URL,
						 DATABASE_CONNECTION_USER,
						 DATABASE_CONNECTION_PASSWORD;
	
	private final int CLEAR_REMAINS;
	
	private StdLogger logger;
	
	private long[] whitelist;
	
	
//	fields:
	private boolean fresponseupdate; 
	
	// clear reamins
	private boolean fclearremains;
	private Timer clearremains_timer;
	final static class ClearRemainsTask extends TimerTask{
		private final BigTwoBot bot;
		public ClearRemainsTask(BigTwoBot bot){
			this.bot = bot;
		}
		
		@Override
		public void run() {
			bot.onClearRemainsEnd(this);
		}
	}
	private ClearRemainsTask clearremains_task;
	
	private Controller controller;
	
	
	
//	constructors
	private BigTwoBot
	(
	 int bot_id, int bot_userid,
	 String bot_username, String bot_token,
	 String version, int bot_owner_userid,
	 String database_connection_url, 
	 String database_connection_user, 
	 String database_connection_password,
	 int clear_remains
	)
	{
		super();
		
		BOT_ID           = bot_id;
		BOT_USERID       = bot_userid;
		BOT_USERNAME     = bot_username;
		BOT_TOKEN        = bot_token; 
		VERSION          = version;      
		BOT_OWNER_USERID = bot_owner_userid;
		                             
		DATABASE_CONNECTION_URL      = database_connection_url;
		DATABASE_CONNECTION_USER     = database_connection_user;
		DATABASE_CONNECTION_PASSWORD = database_connection_password;
		
		CLEAR_REMAINS = clear_remains;
		
		fresponseupdate = false;
		
		controller = new Controller(new View(this), this);
		
		logger = new StdLogger();
		
		whitelist = new long[]
		{
			-224005464L,
			-1001096775043L
		};
	}
    
	
//	methods:
	
	// getters
//	methods:
	@Override
    public String getBotUsername()	/*must be implemented*/
    {
    	return BOT_USERNAME;
    }
	
	@Override
    public String getBotToken()		/*must be implemented*/
    {
        return BOT_TOKEN;
    }

	
	// methods
	private void determineFresponseupdate(){
		fresponseupdate = !fclearremains;
	}
	
	private void startClearRemains(){
		if(CLEAR_REMAINS == 0){
			fclearremains = false;
		}else{
			clearremains_timer = new Timer();
			clearremains_task = new ClearRemainsTask(this);
			clearremains_timer.schedule(clearremains_task, CLEAR_REMAINS);
			fclearremains = true;
		}
		determineFresponseupdate();
	}
	
	public void onClearRemainsEnd(TimerTask key){
		if(clearremains_task == key){
			fclearremains = false;
		}
	}
	
	private void notifyRegistered(){
		startClearRemains();
	}

	
	// bots
    public boolean isAdministrator(User user, Chat chat){
    	return isAdministrator(user, chat.getId().toString());
    }
    public boolean isAdministrator(User user, String chatid){
    	try{
    		GetChatAdministrators para = new GetChatAdministrators();
    		List<ChatMember> admins;
    		para.setChatId(chatid);
    		
    		admins = this.getChatAdministrators(para);
    		
    		if(admins != null){
    			for(ChatMember admin:admins)
					if(admin.getUser().getId().compareTo(user.getId())==0)
						return true;
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return false;
    }
    public boolean isMember(User user, Chat chat){
    	try{
    		GetChatMember para = new GetChatMember();
    		ChatMember member;
    		para.setChatId(chat.getId().toString());
    		
    		member = this.getChatMember(para);
    		
    		if(member!=null){
    			return true;
    		}else{
    			return false;
    		}
    	}catch(Exception ex){
    		
    	}
    	return false;
    }
    
    
    // my methods:
    private void bigtwoUpdate(Update update)
    {
    	try{
    		logger.log(update);
	    	if(fresponseupdate){
	    		if(update.hasMessage()){
	    			Message message = update.getMessage();
	    			if(isCommandToBot(message)){
	    				// is command
	    				String messagetxt = message.getText();
	    				String command = 
	    					messagetxt.substring(1, messagetxt.indexOf("@")).toLowerCase();
	    				controller.handleCommand(command, message);
	    			}else{
	    				// is not command
	    				controller.handleMessage(message);
	    			}
	    		}else if(update.hasCallbackQuery()){
	    			CallbackQuery callbackquery = update.getCallbackQuery();
	    			controller.handleCallbackQuery(callbackquery);
	    		}
	    	}
    	}catch(Exception ex){
    		BotLogger.error("", ex);
    	}
    }
    
    private void testUpdate(Update update)
    {
    	logger.log(update);
    	
    	Message msg = update.getMessage();
    	
    	if(msg != null){
    		if(msg.hasText()){
				String text = msg.getText();
				
				SendMessage sdmsg = new SendMessage()
				.setChatId(msg.getChatId());
							
				if(text.equals(MSGTXT_PASS)){
					sdmsg.setText("PASS");
				}else if(text.equals(MSGTXT_PLAY)){
					sdmsg.setText("PLAY");
				}else{
					String[] cardstext = text.split(" ");
					Card[] cards = new Card[cardstext.length];
					int i;
					for(i=0; i<cardstext.length; ++i){
						Card card = Card.fromText(cardstext[i]);
						if(card != null)
							cards[i] = card;
						else
							break;
					}
					
					StringBuilder strb = new StringBuilder();
					if(i ==	cardstext.length){
						strb.append("success\n");
					}else{
						strb.append("failed\n");
					}
					
					for(Card card : cards){
						if(card!=null)
							strb.append(card.toString()).append(' ');
					}
					
					sdmsg.setText(strb.toString());					
				}
				
				try{sendApiMethod(sdmsg);}catch(Exception ex){}
			}
    	}
    }
    
    @Override
    public void onUpdateReceived(Update update)	/*must be implemented*/
    {
    	/*if(update.hasMessage()){
    		long chatid = update.getMessage().getChatId();
    		for(int i=0; i<whitelist.length; ++i){
    			if(chatid == whitelist[i]){*/
    				bigtwoUpdate(update);
    	/*			break;
    			}
    		}
    	}*/
    	//testUpdate(update);
    }
    
    public String userfullnameText(User user){
		return ((user.getFirstName()!=null)?(user.getFirstName()):(" "))
			+ ((user.getLastName()!=null)?(user.getLastName()):(" "));
	}
	public String usernameText(User user){
		return (user.getUserName()!=null)?(user.getUserName()):(" ");
	}
	public void printUser(User user, String intention){
		System.out.println(
				  intention
				+ "User " + user.getId()
				+ " - @" + usernameText(user)
				+ ", " + userfullnameText(user)
				  );
	}
	public String chattypeText(Chat chat){
		if(chat.isUserChat()){
			return "private";
		}else if(chat.isGroupChat()){
			return "group";
		}else if(chat.isSuperGroupChat()){
			return "supergroup";
		}else if(chat.isChannelChat()){
			return "channel";
		}else{
			return "ukn";
		}
	}
	public void printChat(Chat chat, String intention){
		System.out.print(
				intention
				+ chattypeText(chat)
				+ " - " + chat.getId()
				);
		if(chat.isGroupChat() || chat.isSuperGroupChat() || chat.isChannelChat()){
			System.out.print(", title:" + chat.getTitle());
		}
		if(chat.isUserChat() || chat.isSuperGroupChat() || chat.isChannelChat()){
			if(chat.getUserName() != null)
				System.out.print(", @" + chat.getUserName());
		}
		if(chat.isGroupChat() || chat.isSuperGroupChat() || chat.isChannelChat()){
			GetChatMemberCount c = new GetChatMemberCount();
			c.setChatId(chat.getId().toString());
			try{
				System.out.print(", count:" + _bot.getChatMemberCount(c));
			}catch(Exception ex){
				
			}
		}
		System.out.println();
	}
	public void printInlineQuery(InlineQuery iq, String intention){
		System.out.println(
				  intention + "iq " + iq.getId()
				+ " - query:\"" + iq.getQuery() + "\""
				);
		String subintt = intention + " ";
		printUser(iq.getFrom(), subintt);
	}
	public void printChosenInlineQuery(ChosenInlineQuery ciq, String intention){
		System.out.println(
				  intention + "ciq " + ciq.getInlineMessageId()
				+ " " + ciq.getResultId()
				+ " - query:\"" + ciq.getQuery() + "\""
				);
		String subintt = intention + " ";
		printUser(ciq.getFrom(), subintt);
	}
	public void printCallbackQuery(CallbackQuery cbq, String intention){
		System.out.print(
				  intention + "cbq " + cbq.getId()
				);
		if(cbq.getInlineMessageId()!=null)
			System.out.print(" imsgid " + cbq.getInlineMessageId());
		if((cbq.getData()!=null))
			System.out.print(" - data:" + cbq.getData());
		System.out.println();
		
		String subintt = intention + " ";
		printUser(cbq.getFrom(), subintt);
		if(cbq.getMessage()!=null){
			System.out.println(subintt + "msgid: "
					+ cbq.getMessage().getMessageId()
					);
			printChat(cbq.getMessage().getChat(), subintt + " ");
		}else if(cbq.getChatInstance()!=null){
			System.out.println(subintt + "chat id:" + cbq.getChatInstance());
		}
	}
	public void printMessage(Message msg, String intention){
		System.out.print(
			intention + "msg " + msg.getMessageId() + " - ");
		if(msg.hasText()){
			System.out.print(msg.getText() + " ");
		}
		if(msg.hasDocument()){
			System.out.print("Doc " + msg.getDocument().getFileName() + " " + msg.getDocument().getFileId() + " ");
		}
		Sticker stk = msg.getSticker();
		if(stk != null){
			System.out.print("Stk " + stk.getFileId() + " ");
		}
		List<PhotoSize> photos = msg.getPhoto();
		if(photos != null){
			System.out.print("Photos ");
		}
		Audio audio = msg.getAudio();
		if(audio != null){
			System.out.print("Ad ");
		}
		Video video = msg.getVideo();
		if(video != null){
			System.out.print("Vd ");
		}
		Voice voice = msg.getVoice();
		if(voice != null){
			System.out.print("Vc");
		}
		User newuser = msg.getNewChatMembers().get(0);
		if(newuser != null){
			System.out.print("User_join ");
			if(newuser.getId().compareTo(BOT_USERID)==0)
				System.out.print(BOT_USERNAME + " ");
		}
		User olduser = msg.getLeftChatMember();
		if(olduser != null){
			System.out.print("User_left ");
			if(olduser.getId().compareTo(BOT_USERID)==0)
				System.out.print(BOT_USERNAME + " ");
		}
		System.out.println();
		
		String subintt = intention + " ";
		printUser(msg.getFrom(), subintt);
		printChat(msg.getChat(), subintt);
	}
	public void printUpdate(Update update){
		System.out.println("Update received");
		if(update.hasMessage()){
			printMessage(update.getMessage(), " ");
		}
		if(update.hasCallbackQuery()){
			printCallbackQuery(update.getCallbackQuery(), " ");
		}
		if(update.hasChosenInlineQuery()){
			printChosenInlineQuery(update.getChosenInlineQuery(), " ");
		}
		if(update.hasInlineQuery()){
			printInlineQuery(update.getInlineQuery(), " ");
		}
		if(update.hasEditedMessage()){
			
		}
	}
	
	public void stop(){
		
	}
	
	
	// database
	public static void loadDriver(){
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations

			Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        	System.err.println("Failed to load mysql jdbc driver.");
        }
	}
	
	public Connection getConnection(){
    	try {
			Connection con = 
				DriverManager.getConnection(
					DATABASE_CONNECTION_URL,
					DATABASE_CONNECTION_USER,
					DATABASE_CONNECTION_PASSWORD
				);
			return con;
		} catch (SQLException ex) {
			BotLogger.error("", ex.getMessage());
			return null;
		}
    	
    	// snippet
    	/*
		Connection connection = bot.getConnection();
		
		final String sql = "";
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
    	 */
    }
	
	public int obtainNextPlayId(){
		Connection connection = _bot.getConnection();
		
		final String sql =
			"SELECT " + FIELD_NEXT_PLAYID
			+ " FROM " + TABLE_BOT;
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			
			ResultSet result = ppdst.executeQuery();
			
			if(result.next()){
				int playid;
				
				playid = result.getInt(1);
				
				return playid;
			}else{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public void occupyPlayId(){
		Connection connection = _bot.getConnection();
		
		final String sql =
			"UPDATE " + TABLE_BOT
			+ " SET "
			+ FIELD_NEXT_PLAYID + " = " + FIELD_NEXT_PLAYID + " + 1"
			+ " WHERE " + FIELD_BOT_ID + " = ?;";
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, BOT_ID);
			
			ppdst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public int obtainNextGameId(){
		Connection connection = _bot.getConnection();
		
		final String sql = 
			"SELECT " + FIELD_NEXT_GAMEID
			+ " FROM " + TABLE_BOT;
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			
			ResultSet result = ppdst.executeQuery();
			
			if(result.next()){
				int gameid;
				
				gameid = result.getInt(1);
				
				return gameid;		
			}else{
				return 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public void occupyGameId(){
		Connection connection = _bot.getConnection();
		
		final String sql =
			"UPDATE " + TABLE_BOT
			+ " SET "
			+ FIELD_NEXT_GAMEID + " = " + FIELD_NEXT_GAMEID + " + 1"
			+ " WHERE " + FIELD_BOT_ID + " = ?;";
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, BOT_ID);
			
			ppdst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	
    // visible to package
    <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>>
    void sendAsync(Method method, Callback callback)
    {
    	logger.log(method);
        sendApiMethodAsync(method, callback);
    }
    
    <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>>
    void sendAsync(Method method)
    {
        sendApiMethodAsync(method, new defaultCallback<T>());
    }

    <T extends Serializable, Method extends BotApiMethod<T>>
    T send(Method method) throws TelegramApiException {
    	logger.log(method);
        return sendApiMethod(method);
    }
	
	
    
//	entering point
	private static void initializeNewBot(){
		@SuppressWarnings("resource")
		final Scanner scanner = new Scanner(System.in);
		final PrintStream out = System.out;
		
		
		// initialization
		out.println("Start initialization");
		
		out.println("> api context initialization");
		ApiContextInitializer.init();
		out.println("> ...done");
		
		out.println("> read bot config file");
		int    bot_id;							// line 1
		int    bot_userid;                      // line 2
		String bot_username;                    // line 3
		String bot_key;                         // line 4
		String version;                         // line 5
		int    bot_owner_userid;                // line 6
		String database_connection_url;         // line 7
		String database_connection_user;        // line 8
		String database_connection_password;    // line 9
		
		final String configfilepathname = "bot.cnf";
		FileReader reader;
		BufferedReader breader = null;
		String lines[] = new String[9];
		int linescount;
		try{
			reader = new FileReader(configfilepathname);
			breader = new BufferedReader(reader);
			
			for(linescount=0; linescount<9; ++linescount)
				if(breader.ready())
					lines[linescount] = breader.readLine();
				else
					break;
			
		}catch(FileNotFoundException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}catch(IOException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}finally{
			if(breader != null)
				try {
					breader.close();
				} catch (IOException ex) {
					BotLogger.error("", ex.getMessage());
				}
		}
		
		if(linescount != 9){
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		try{
			bot_id                       = Integer.parseInt(lines[0]);
			bot_userid                   = Integer.parseInt(lines[1]);
			bot_username                 =                  lines[2];
			bot_key                      =                  lines[3];
			version                      =                  lines[4];
			bot_owner_userid             = Integer.parseInt(lines[5]);
			database_connection_url      =                  lines[6];
			database_connection_user     =                  lines[7];
			database_connection_password =                  lines[8];
		}catch(NumberFormatException ex){
			out.println("> " + ex.toString());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		out.println("> ...done");
		
		
		out.println("> clear remains");
		out.println("> please input the clear remains\n"
				+   "   0 for no response to reamins\n"
				+   "  >0 for miliseconds");
		int clear_remains;
		clear_remains = 0;/*scanner.nextInt();*/
		if(clear_remains < -1){
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		out.println("> ...done");
		
		out.println("> load database driver");
		loadDriver();
		out.println("> ...done");
		
		// modules initialization
		out.println("> modules initialization");
		//Game.initialize();
		//Player.initialize();
		//Play.initialize();
		//Group.initialize();
		Lang.initialize();
		out.println("> ...done");
		
		
		out.println("> starting bot");
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		String bot_token = bot_userid + ":" + bot_key;
	    _bot = new BigTwoBot(
	    		bot_id,
	    		bot_userid,
	    		bot_username,
	    		bot_token,
	    		version,
	    		bot_owner_userid,
	    		database_connection_url,
	    		database_connection_user,
	    		database_connection_password,
	    		clear_remains
	    		);
	    try {
	        telegramBotsApi.registerBot(_bot);
		    _bot.notifyRegistered();
	    } catch (TelegramApiException e) {
	        BotLogger.error("Error", e);
	        return;
	    }
		out.println("> ...done");
		
		
		System.out.println("initialization done");
		// end - initialization

	    //Calendar clear_cache_time = Calendar.getInstance();
	    //clear_cache_time.add(Calendar.SECOND, SEC_ClearingRemainsDuration);
	    //while(clear_cache_time.compareTo(Calendar.getInstance())>0){
	    //}
	    //clearingRemains = false;
	    
	    System.out.println("--------Bot Started--------");
	    
	    // in running
	    //while(true){
	    //	String input = scanner.nextLine();
	    //	if(input.compareTo("stop")==0){
	    //		bot.stop();
	    //		break;
	    //	}else if(input.compareTo("debugall")==0){
	    //		String str = "";
		//		try{
		//			str += Game.allgameinfo() + "\n";
		//			str += Play.allplayinfo() + "\n";
		//			str += Group.allgroupinfo() + "\n";
		//			str += Player.allplayerinfo() + "\n";
		//		}catch(Exception ex){
		//			ex.printStackTrace();
		//		}
		//		try {
		//			FileOutputStream fout = new FileOutputStream("debugall.txt");
		//			PrintWriter prw = new PrintWriter(fout);
		//			prw.print(str);
		//			prw.close();
		//		} catch (FileNotFoundException e) {
		//			e.printStackTrace();
		//		}
	    //	}
	    //}
	    //scanner.close();
	}
	
	private static void testApi(){
		final PrintStream out = System.out;
		
		
		// initialization
		out.println("Start initialization");
		
		out.println("> api context initialization");
		ApiContextInitializer.init();
		out.println("> ...done");
		
		out.println("> read bot config file");
		int    bot_id;							// line 1
		int    bot_userid;                      // line 2
		String bot_username;                    // line 3
		String bot_key;                         // line 4
		String version;                         // line 5
		int    bot_owner_userid;                // line 6
		String database_connection_url;         // line 7
		String database_connection_user;        // line 8
		String database_connection_password;    // line 9
		
		final String configfilepathname = "bot.cnf";
		FileReader reader;
		BufferedReader breader = null;
		String lines[] = new String[9];
		int linescount;
		try{
			reader = new FileReader(configfilepathname);
			breader = new BufferedReader(reader);
			
			for(linescount=0; linescount<9; ++linescount)
				if(breader.ready())
					lines[linescount] = breader.readLine();
				else
					break;
			
		}catch(FileNotFoundException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}catch(IOException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}finally{
			if(breader != null)
				try {
					breader.close();
				} catch (IOException ex) {
					BotLogger.error("", ex.getMessage());
				}
		}
		
		if(linescount != 9){
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		try{
			bot_id                       = Integer.parseInt(lines[0]);
			bot_userid                   = Integer.parseInt(lines[1]);
			bot_username                 =                  lines[2];
			bot_key                      =                  lines[3];
			version                      =                  lines[4];
			bot_owner_userid             = Integer.parseInt(lines[5]);
			database_connection_url      =                  lines[6];
			database_connection_user     =                  lines[7];
			database_connection_password =                  lines[8];
		}catch(NumberFormatException ex){
			out.println("> " + ex.toString());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		out.println("> ...done");
		
		String bot_token = bot_userid + ":" + bot_key;
		
		BigTwoBot bot = new BigTwoBot(
	    		bot_id,
	    		bot_userid,
	    		bot_username,
	    		bot_token,
	    		version,
	    		bot_owner_userid,
	    		database_connection_url,
	    		database_connection_user,
	    		database_connection_password,
	    		0
	    		);
		
		out.println("> starting bot");
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
	    try {
	        telegramBotsApi.registerBot(bot);
		    bot.notifyRegistered();
	    } catch (TelegramApiException e) {
	        BotLogger.error("Error", e);
	        return;
	    }
		out.println("> ...done");
	}
	
	private static void test(){
		final PrintStream out = System.out;
		
		
		// initialization
		out.println("Start initialization");
		
		out.println("> api context initialization");
		ApiContextInitializer.init();
		out.println("> ...done");
		
		out.println("> read bot config file");
		int    bot_id;							// line 1
		int    bot_userid;                      // line 2
		String bot_username;                    // line 3
		String bot_key;                         // line 4
		String version;                         // line 5
		int    bot_owner_userid;                // line 6
		String database_connection_url;         // line 7
		String database_connection_user;        // line 8
		String database_connection_password;    // line 9
		
		final String configfilepathname = "bot.cnf";
		FileReader reader;
		BufferedReader breader = null;
		String lines[] = new String[9];
		int linescount;
		try{
			reader = new FileReader(configfilepathname);
			breader = new BufferedReader(reader);
			
			for(linescount=0; linescount<9; ++linescount)
				if(breader.ready())
					lines[linescount] = breader.readLine();
				else
					break;
			
		}catch(FileNotFoundException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}catch(IOException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}finally{
			if(breader != null)
				try {
					breader.close();
				} catch (IOException ex) {
					BotLogger.error("", ex.getMessage());
				}
		}
		
		if(linescount != 9){
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		try{
			bot_id                       = Integer.parseInt(lines[0]);
			bot_userid                   = Integer.parseInt(lines[1]);
			bot_username                 =                  lines[2];
			bot_key                      =                  lines[3];
			version                      =                  lines[4];
			bot_owner_userid             = Integer.parseInt(lines[5]);
			database_connection_url      =                  lines[6];
			database_connection_user     =                  lines[7];
			database_connection_password =                  lines[8];
		}catch(NumberFormatException ex){
			out.println("> " + ex.toString());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		out.println("> ...done");
		
		String bot_token = bot_userid + ":" + bot_key;
		
		BigTwoBot bot = new BigTwoBot(
	    		bot_id,
	    		bot_userid,
	    		bot_username,
	    		bot_token,
	    		version,
	    		bot_owner_userid,
	    		database_connection_url,
	    		database_connection_user,
	    		database_connection_password,
	    		0
	    		);
		
		System.out.println(database_connection_user + " " + database_connection_password);
		
		try{
			Connection conn = bot.getConnection();
			
			String sql =
				"SELECT "
				+ GameSettings.FIELD_LANG
				+ " FROM "
				+ TABLE_GROUP
				+ " WHERE "
				+ FIELD_GROUPID + " = ?;";
			
			PreparedStatement ppdstt = conn.prepareStatement(sql);
			ppdstt.setLong(1, 1);
			
			ResultSet result = ppdstt.executeQuery();
			
			if(result.next()){
				System.out.println("1");
			}else{
				System.out.println("0");				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static void test_sql_connection()
	{
		PrintStream out = System.out;
		
		out.println("> read bot config file");
		int    bot_id;							// line 1
		int    bot_userid;                      // line 2
		String bot_username;                    // line 3
		String bot_key;                         // line 4
		String version;                         // line 5
		int    bot_owner_userid;                // line 6
		String database_connection_url;         // line 7
		String database_connection_user;        // line 8
		String database_connection_password;    // line 9
		
		final String configfilepathname = "bot.cnf";
		FileReader reader;
		BufferedReader breader = null;
		String lines[] = new String[9];
		int linescount;
		try{
			reader = new FileReader(configfilepathname);
			breader = new BufferedReader(reader);
			
			for(linescount=0; linescount<9; ++linescount)
				if(breader.ready())
					lines[linescount] = breader.readLine();
				else
					break;
			
		}catch(FileNotFoundException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}catch(IOException ex){
			BotLogger.error("", ex.getMessage());
			out.println("> " + ex.getMessage());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}finally{
			if(breader != null)
				try {
					breader.close();
				} catch (IOException ex) {
					BotLogger.error("", ex.getMessage());
				}
		}
		
		if(linescount != 9){
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		try{
			bot_id                       = Integer.parseInt(lines[0]);
			bot_userid                   = Integer.parseInt(lines[1]);
			bot_username                 =                  lines[2];
			bot_key                      =                  lines[3];
			version                      =                  lines[4];
			bot_owner_userid             = Integer.parseInt(lines[5]);
			database_connection_url      =                  lines[6];
			database_connection_user     =                  lines[7];
			database_connection_password =                  lines[8];
		}catch(NumberFormatException ex){
			out.println("> " + ex.toString());
			out.println("> ...failed");
			out.println("initialization failed");
			return;
		}
		
		out.println("> ...done");
		
		

		out.println("> load database driver");
		loadDriver();
		out.println("> ...done");
		
		out.println("database connection test");
		Connection connection;
		
		try {
			Connection con = 
				DriverManager.getConnection(
					database_connection_url     ,
					database_connection_user    ,
					database_connection_password
				);
			connection = con;
		} catch (SQLException ex) {
			BotLogger.error("", ex.getMessage());
			return;
		}
		
		
		final String sql = 
			"SELECT " + FIELD_NEXT_GAMEID
			+ " FROM " + TABLE_BOT;
		
		int gameid = -1;
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			
			ResultSet result = ppdst.executeQuery();
			
			if(result.next()){
				
				gameid = result.getInt(1);
					
			}else{
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
		out.println("gameid" + gameid);
	}
	
	public static void main(String[] args){
		initializeNewBot();
		//testApi();
		//test_sql_connection();
	}
}


class defaultCallback<T extends Serializable>
implements SentCallback<T>{
	@Override
	public void onResult(BotApiMethod<T> method, T response) {
	}

	@Override
	public void onError(BotApiMethod<T> method, TelegramApiRequestException apiException) {
		BotLogger.error("", apiException.toString());
	}

	@Override
	public void onException(BotApiMethod<T> method, Exception exception) {
		BotLogger.error("", exception.toString());
	}
}
