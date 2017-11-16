package bigtwo.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.telegram.telegrambots.logging.BotLogger;

import static bigtwo.game.Group.TABLE_GROUP;
import static bigtwo.game.Group.FIELD_GROUPID;

import bigtwo.cardcombination.CardCombinationRule;
import bigtwo.cardcombination.CardCombinationTypeComparator;
import bigtwo.cardcombination.CardCombinationTypeValidator;
import bigtwo.cardcombination.FlushComparator;
import bigtwo.cardcombination.FlushComparators;
import bigtwo.cardcombination.StraightComparator;
import bigtwo.cardcombination.StraightComparators;
import bigtwo.cardcombination.StraightValidator;
import bigtwo.cardcombination.StraightValidatorChain;
import bot.BigTwoBot;
import bot.Lang;

public class GameSettings {
	public static final String
	FIELD_EASTEREGG     = "easteregg",
	FIELD_LANG          = "lang",
	FIELD_LINK          = "link",
	FIELD_NCARD      	= "ncard",
	FIELD_ALLOWTRIPLE	= "allowtriple",
	FIELD_SVC_DATA      = "svc_data",
	FIELD_CCTC_TYPE     = "cctc_type",
	FIELD_SC_DATA      = "sc_data",
	FIELD_FC_TYPE       = "fc_type";
	public static final int
	FILED_INDEX_EASTEREGG   =  2,
	FILED_INDEX_LANG        =  3,
	FILED_INDEX_LINK        =  4,	
	FILED_INDEX_NCARD      	=  5,	
	FILED_INDEX_ALLOWTRIPLE =  6,	
	FILED_INDEX_SVC_DATA    =  7,	
	FILED_INDEX_CCTC_TYPE   =  8,	
	FILED_INDEX_SC_DATA     =  9,	
	FILED_INDEX_FC_TYPE     = 10;
	
	
	private static void insertIntoDatabase(final GameSettings settings, final Long groupid){
		Connection connection = BigTwoBot.bot().getConnection();
		
		final String sql =
		"INSERT INTO " + TABLE_GROUP
		+ "("
		+ FIELD_GROUPID		+ ", "
		+ FIELD_EASTEREGG   + ", "
		+ FIELD_LANG        + ", "
		+ FIELD_LINK        + ", "
		+ FIELD_NCARD       + ", "
		+ FIELD_ALLOWTRIPLE + ", "
		+ FIELD_SVC_DATA    + ", "
		+ FIELD_CCTC_TYPE   + ", "
		+ FIELD_SC_DATA     + ", "
		+ FIELD_FC_TYPE     + ")"
		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		try {
			PreparedStatement pstt = connection.prepareStatement(sql);
			pstt.setLong(1, groupid);
			pstt.setBoolean(2, settings._easteregg);
			pstt.setString(3, settings._lang.getLangName());
			pstt.setString(4, settings._link);
			pstt.setInt(5, settings._gamerule.getNCard());
			pstt.setBoolean(6, settings._gamerule.getAllowTriple());
			pstt.setInt(7, ((StraightValidatorChain)settings._gamerule.getStraightValidator()).data());
			pstt.setBoolean(8, settings._gamerule.getCanStraightBeatFlush());
			pstt.setInt(9, settings._gamerule.getStraightComparator().data());
			pstt.setInt(10, settings._gamerule.getFlushComparator().type());
			
			
			pstt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static GameSettings getSettings(final Long groupid){
		Connection connection = BigTwoBot.bot().getConnection();
		
		final String query =
		"SELECT " + 
		"*" +
		//FIELD_EASTEREGG   + ", " +
		//FIELD_LANG        + ", " +
		//FIELD_LINK        + ", " +
		//FIELD_NCARD       + ", " +
		//FIELD_ALLOWTRIPLE + ", " +
		//FIELD_SVC_DATA    + ", " +
		//FIELD_CCTC_TYPE   + ", " +
		//FIELD_SC_TYPE     + ", " +
		//FIELD_SCC_DATA    + ", " +
		//FIELD_FC_TYPE     +
		" FROM " + Group.TABLE_GROUP + 
		" WHERE " + Group.FIELD_GROUPID + " = ?;"; 
		
		try {
			PreparedStatement pstt = connection.prepareStatement(query);
			pstt.setLong(1, groupid);
			
			ResultSet result = pstt.executeQuery();
			
			if(result.next()){
				String link;
				int ncard;
				boolean allowtriple;
				
				link = result.getString(FILED_INDEX_LINK);
				ncard = result.getInt(FILED_INDEX_NCARD);
				allowtriple = result.getBoolean(FILED_INDEX_ALLOWTRIPLE);
				
				String langname;
				langname = result.getString(FILED_INDEX_LANG);
				Lang lang;
				lang = Lang.getLang(langname);
				
				int svc_data;
				svc_data = result.getInt(FILED_INDEX_SVC_DATA);
				StraightValidatorChain svc;
				svc = StraightValidatorChain.getValidator(svc_data);
				
				int cctc_type;
				cctc_type = result.getInt(FILED_INDEX_CCTC_TYPE);
				CardCombinationTypeComparator cctc;
				if(cctc_type == 0){
					cctc = CardCombinationTypeComparator.FS;
				}else{
					cctc = CardCombinationTypeComparator.SF;
				}
				
				int sc_type;
				sc_type = result.getInt(FILED_INDEX_SC_DATA);
				StraightComparator sc;
				sc = StraightComparators.get(sc_type);
				
				int fc_type;
				fc_type = result.getInt(FILED_INDEX_FC_TYPE);
				FlushComparator fc;
				fc = FlushComparators.get(fc_type);
				
				return new GameSettings(
					new GameRule(
						ncard, 
						new CardCombinationRule(
							new CardCombinationTypeValidator(svc, 
								allowtriple), 
							cctc, 
							sc, 
							fc)), 
					false, 
					lang, 
					link);
			}else{
				GameSettings defaultSettings = new GameSettings();
				insertIntoDatabase(defaultSettings, groupid);
				return defaultSettings;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	
	// setters
	public static void setNCard(Long groupid, int ncard){
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_NCARD + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, ncard);
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}

	public static void setStraightValidator(Long groupid, StraightValidator sv){
		StraightValidatorChain svc = (StraightValidatorChain) sv;
		
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_SVC_DATA + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, svc.data());
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}

	public static void setAllowTriple(Long groupid, boolean allowtriple){
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_ALLOWTRIPLE + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setBoolean(1, allowtriple);
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static void setCardCombinationTypeComparator
	(final Long groupid, CardCombinationTypeComparator cctc)
	{
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_CCTC_TYPE + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
		
		int cctc_data = (cctc.canStraightBeatsFlush)?(1):(0);
		
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, cctc_data);
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static void setStraightComparator
	(final Long groupid, 
	 StraightComparator straightComparator)
	{
		final String sql = 
			"UPDATE " + TABLE_GROUP 
			+ " SET " + FIELD_SC_DATA + " = ?"
			+ " WHERE " + FIELD_GROUPID + " = ?;";
		
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, straightComparator.data());
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static void setFlushComparator
	(Long groupid,
	 FlushComparator flushComparator)
	{
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_FC_TYPE + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setInt(1, flushComparator.type());
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static void setEasterEgg(Long groupid, boolean easteregg){
		final String sql = 
			"UPDATE " + TABLE_GROUP 
			+ " SET " + FIELD_EASTEREGG + " = ?"
			+ " WHERE " + FIELD_GROUPID + " = ?;";
		
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setBoolean(1, easteregg);
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	public static void setLang(Long groupid, Lang lang){
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_LANG + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setString(1, lang.getLangName());
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}

	public static void setLink(Long groupid, String link){
		final String sql = 
				"UPDATE " + TABLE_GROUP 
				+ " SET " + FIELD_LINK + " = ?"
				+ " WHERE " + FIELD_GROUPID + " = ?;";
			
		Connection connection = BigTwoBot.bot().getConnection();
		
		try{
			PreparedStatement ppdst = connection.prepareStatement(sql);
			ppdst.setString(1, link);
			ppdst.setLong(2, groupid);
			
			ppdst.executeUpdate();
		}catch(SQLException ex){
			BotLogger.error("", ex);
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
	
	
	
	private GameRule _gamerule;
	private boolean _easteregg;
	private Lang _lang;
	private String _link;
	

	
	public GameSettings
	(GameRule gamerule, 
	 boolean EasterEgg, Lang lang, String link)
	{
		this._gamerule = gamerule;
		this._link = link;
		this._easteregg = EasterEgg;
		this._lang = lang;
	}

	// default settings
	public GameSettings()
	{
		this(new GameRule(), false, Lang.primaryLang, null);
	}


	
	// getters
	public GameRule getGameRule(){
		return _gamerule;
	}
	
	public int getNCard(){
		return _gamerule.getNCard();
	}
	
	public CardCombinationRule getCardCombinationRule(){
		return _gamerule.getCardCombinationRule();
	}
	
	public boolean getAllowTriple(){
		return _gamerule.getAllowTriple();
	}
	
	public StraightValidator getStraightValidator(){
		return _gamerule.getStraightValidator();
	}
	
	public boolean getCanStraightBeatFlush(){
		return _gamerule.getCanStraightBeatFlush();
	}
	
	public StraightComparator getStraightComparator(){
		return _gamerule.getStraightComparator();
	}
	
	public FlushComparator getFlushComparator(){
		return _gamerule.getFlushComparator();
	}
	
	public boolean getEasterEgg(){
		return _easteregg;
	}
	
	public Lang getLang(){
		return _lang;
	}
	
	public String getLink(){
		return _link;
	}
	
	// setters
	public void setGameRule(GameRule value){
		this._gamerule = value;
	}
	
	public void setNCard(int value){
		this._gamerule.setNCard(value);
	}
	
	public void setCardCombinationRule(CardCombinationRule rule){
		this._gamerule.setCardCombinationRule(rule);
	}
	
	public void setAllowTriple(boolean value){
		_gamerule.setAllowTriple(value);
	}
	
	public void setStraightValidator(StraightValidator straightValidator){
		this._gamerule.setStraightValidator(straightValidator);
	}
	
	public void setStraightValidator(int begin, int end){
		this._gamerule.setStraightValidator(
			StraightValidatorChain.getValidator(begin, end)
		);
	}
	
	public void setStraightValidator(int data){
		this._gamerule.setStraightValidator(
			StraightValidatorChain.getValidator(data)
		);
	}
	
	public void setTypeComparator(CardCombinationTypeComparator typeComparator){
		this._gamerule.setTypeComparator(typeComparator);
	}
	
	public void setTypeComparator(boolean canStraightBeatFlush){
		_gamerule.setTypeComparator(canStraightBeatFlush);
	}
	
	public void setStraightComparator(StraightComparator straightComparator){
		_gamerule.setStraightComparator(straightComparator);
	}
	
	public void setStraightComparator(int sc_data){
		_gamerule.setStraightComparator(sc_data);
	}
	
	public void setFlushComparator(FlushComparator flushComparator){
		_gamerule.setFlushComparator(flushComparator);
	}
	
	public void setFlushComparator(int fc_type){
		_gamerule.setFlushComparator(fc_type);
	}
	
	public void setEasterEgg(boolean value){
		this._easteregg = value;
	}
	
	public void setLang(Lang value){
		this._lang = value;
	}
	
	public void setLink(String value){
		this._link = value;
	}
	
	
	public void updateToDatabase(Long groupid){
		Connection connection = BigTwoBot.bot().getConnection();
		
		final String sql =
		"UPDATE " + TABLE_GROUP
		+ " SET "
		+ FIELD_EASTEREGG   + " = ?, "
		+ FIELD_LANG        + " = ?, "
		+ FIELD_LINK        + " = ?, "
		+ FIELD_NCARD       + " = ?, "
		+ FIELD_ALLOWTRIPLE + " = ?, "
		+ FIELD_SVC_DATA    + " = ?, "
		+ FIELD_CCTC_TYPE   + " = ?, "
		+ FIELD_SC_DATA     + " = ?, "
		+ FIELD_FC_TYPE     + " = ?"
		+ " WHERE " + FIELD_GROUPID + " = ?;";
		
		try {
			PreparedStatement pstt = connection.prepareStatement(sql);
			pstt.setLong(1, groupid);
			pstt.setBoolean(2, _easteregg);
			pstt.setString(3, _lang.getLangName());
			pstt.setString(4, _link);
			pstt.setInt(5, _gamerule.getNCard());
			pstt.setBoolean(6, _gamerule.getAllowTriple());
			pstt.setInt(7, ((StraightValidatorChain)_gamerule.getStraightValidator()).data());
			pstt.setBoolean(8, _gamerule.getCanStraightBeatFlush());
			pstt.setInt(9, _gamerule.getStraightComparator().data());
			pstt.setInt(10, _gamerule.getFlushComparator().type());
			
			
			pstt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{connection.close();}catch(SQLException ex){BotLogger.error("", ex);}
		}
	}
}
