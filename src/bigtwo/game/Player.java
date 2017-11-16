package bigtwo.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static bot.BigTwoBot.bot;

import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.logging.BotLogger;

import bot.BigTwoBot;

public class Player {
	public static class PlayerRecord{
		public static PlayerRecord loadFromDatabase(int playerid){
			Connection connection = BigTwoBot.bot().getConnection();
			
			final String sql = 
				"SELECT "
				+ FIELD_GAME_PLAYED + ", "
				+ FIELD_GAME_WON    
				+ " FROM " + TABLE_PLAYER
				+ " WHERE " + FIELD_PLAYERID + " = ?;";
			
			try{
				PreparedStatement ppdst = connection.prepareStatement(sql);
				ppdst.setInt(1, playerid);
				
				ResultSet result = ppdst.executeQuery();
				
				if(result.next()){
					int ngameplayed, ngamewon;
					ngameplayed = result.getInt(1);
					ngamewon = result.getInt(2);
					
					return new PlayerRecord(ngameplayed, ngamewon);
				}else{
					return new PlayerRecord();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}finally{
				try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
			}
		}
		
		private int _ngameplayed, _ngamewon;
		
		public PlayerRecord
		(
			int numberOfGamePlayed, 
			int numberOfGameWon
		)
		{
			_ngameplayed = numberOfGamePlayed;
			_ngamewon = numberOfGameWon;
		}
		
		// copyer
		public PlayerRecord(PlayerRecord record){
			this(record._ngameplayed, record._ngamewon);
		}
		
		// default
		public PlayerRecord(){
			_ngameplayed = 0;
			_ngamewon = 0;
		}
		
		
		// record items
		public int numberOfGamePlayed(){
			return _ngameplayed;
		}
		
		public int numberOfGameWon(){
			return _ngamewon;
		}
		
		public int numberOfGameLost(){
			return _ngameplayed - _ngamewon;
		}
		
		public float winPercentage(){
			return (float)_ngamewon / _ngameplayed;
		}
	
		
		// modify record
		public void gameWon(){
			++_ngameplayed;
			++_ngamewon;
		}
		
		public void gameLost(){
			++_ngameplayed;
		}
		
		
		// combine
		public void combine(PlayerRecord record){
			_ngameplayed += record._ngameplayed;
			_ngamewon += record._ngamewon;
		}
		
		
		public void mergeToDatabase(int playerid){
			Connection connection = BigTwoBot.bot().getConnection();
			
			String sql = 
				"UPDATE " + TABLE_PLAYER
				+ " SET " 
				+ FIELD_GAME_PLAYED + " = " + FIELD_GAME_PLAYED + " + ?, "
				+ FIELD_GAME_WON    + " = " + FIELD_GAME_WON    + " + ?"
				+ " WHERE " + FIELD_PLAYERID + " = ?;";
			
			try{
				PreparedStatement ppdst = connection.prepareStatement(sql);
				ppdst.setInt(1, _ngameplayed);
				ppdst.setInt(2, _ngamewon);
				ppdst.setInt(3, playerid);
				
				ppdst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
			}
		}
		
		@Override
		public String toString(){
			return "PlayerRecord{ngameplayed:" + _ngameplayed
				+ ",ngamewon:" + _ngamewon + "}";
		}
	}
	
//	class constants:
	/*Maximum game can a player join at the same time*/
	public static final int MAX_PLAY = 1;
	
	public static final String
	TABLE_PLAYER   = "player",
	FIELD_PLAYERID = "playerid",
	FIELD_GAME_PLAYED = "gameplayed",
	FIELD_GAME_WON    = "gamewon";
	
	

	
//	static fields:
	private static HashMap<Integer, Player>
	cachedplayers = new HashMap<>();	// Integer: user id 
	

	
//	static methods:
	public static void cache(Player player){
		cachedplayers.put(player._playerid, player);
	}
	
	public static Player getCachedPlayer(Integer playerid){
		return cachedplayers.get(playerid);
	}
	
	public static Player uncache(Integer playerid){
		return cachedplayers.remove(playerid);
	}
	
	public static Player uncache(Player player){
		return cachedplayers.remove(player._playerid);
	}

	
	private static Boolean existInDatabase(int playerid){
		Connection connection = BigTwoBot.bot().getConnection();
		
		final String sql = 
			"SELECT count(*) AS total"
			+ " FROM " + TABLE_PLAYER
			+ " WHERE " + FIELD_PLAYERID + " = ?;";
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, playerid);
			
			ResultSet result = ppdst.executeQuery();
			
			if(result.next()){
				int total;
				total = result.getInt(1);
				
				return total > 0;
			}else{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	private static void insertNewPlayerIntoDatabase
	(Player newplayer)
	{
		Connection connection = BigTwoBot.bot().getConnection();
		
		final String sql = 
			"INSERT INTO " + TABLE_PLAYER
			+ "(" 
			+ FIELD_PLAYERID    + ", "
			+ FIELD_GAME_PLAYED + ", "
			+ FIELD_GAME_WON    
			+ ") VALUES (?, ?, ?);";
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, newplayer._playerid);
			ppdst.setInt(2, 0);
			ppdst.setInt(3, 0);
			
			ppdst.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	//public static Player loadFromDataBase(Integer playerid){/*TODO*/return null;};
	
	// load from database and update user info to object
	// no update to database
	//public static Player loadFromDataBase(User user){/*TODO*/return null;};
	
	// return no null
	public static Player getPlayer(User user){
		Player r;
		
		// read from table
		r = cachedplayers.get(user.getId());
		if(r == null){
			r = new Player(user);
		}
		
		return r;
	}
	
	
	
//	data member:
	private Play play;
	
	private final int	_playerid;
	private String	_username,
					_firstname,
					_lastname;
	
	private PlayerRecord _record_difference;
	
	
	
//	constructors
	private Player(int playerid, String username, 
		String firstName, String lastName)
	{
		play = null;
		
		_playerid = playerid;
		_username = username;
		_firstname = firstName;
		_lastname = lastName;
		
		_record_difference = new PlayerRecord();
	}
	
	public Player(User user)
	{
		this(user.getId(), user.getUserName(), 
			user.getFirstName(), user.getLastName());
	}
	
	

//	association methods:
	public void addPlay(Play play){
		this.play = play;
		Player.cache(this);
	}
	
	public void removePlay(){
		this.play = null;
		Player.uncache(this);
	}
	
	

//	properties:
	// getters:
	public Play getPlay(){
		return play;
	}
	
	public Integer getPlayerId(){
		return _playerid;
	}
	
	public Integer getUserId(){
		return _playerid;
	}
	
	public String getUsername() {
		return _username;
	}

	public String getFirstname() {
		return _firstname;
	}

	public String getLastname() {
		return _lastname;
	}
	

	
	// setters:
	
	
	// others:
	// update player info
	public void update(User user){
		_username = user.getUserName();
		_firstname = user.getFirstName();
		_lastname = user.getLastName();
	}
	
	
	
//	instance methods:
	public boolean reachMaxPlay(){
		return play != null;
	}
	
	public boolean isPlaying(){
		return play != null;
	}
	
	public boolean isPlaying(Play play){
		return this.play == play;
	}
	
	public Play isPlaying(Game game){
		if(play != null){
			if(play.game == game){
				return play;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public void join(Game game){
		Play newplay = new Play(game, this);
		play = newplay;
		
		Player.cache(this);
	}
	
	
	// called by Play object
	public void notifyLeaveWhileForming(){uncache(this);}
	
	public void notifyWon(){
		_record_difference.gameWon();
		updateToDatabase();
	}
	
	public void notifyLost(){
		_record_difference.gameLost();
		updateToDatabase();
	}
	
	public void notifyLeaveWhilePlaying(){uncache(this);}
	
	public void notifyTerminated(){uncache(this);}
	// end - called by Play object
	
	
	
	public void updateToDatabase(){
		if(_record_difference != null)
			_record_difference.mergeToDatabase(_playerid);
	}
	
	
	
	@Override
	public String toString(){
		String r = "Player{playerid:" + _playerid +
			",username:" + _username +
			",firstname:" + _firstname +
			",lastname:" + _lastname +
			",play:" + ((play==null)?("null"):(play.toString_idonly())) +
			",record_difference:" + _record_difference
			+ "}";
		return r;
	}
	
	public boolean equal(Player player){
		return _playerid == player._playerid;
	}
}
