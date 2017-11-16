package bigtwo.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.telegram.telegrambots.logging.BotLogger;

import bot.BigTwoBot;
import bot.Lang;

import static bigtwo.game.Group.TABLE_GROUP;
import static bigtwo.game.Group.FIELD_GROUPID;;

public class GroupSettings {
	private final long groupid;
	public GroupSettings(long groupid){
		this.groupid = groupid;
	}
	
	public Lang getLang(){
		try{
			Connection conn = BigTwoBot.bot().getConnection();
			
			String sql =
				"SELECT "
				+ GameSettings.FIELD_LANG
				+ " FROM "
				+ TABLE_GROUP
				+ " WHERE "
				+ FIELD_GROUPID + " = ?;";
			
			PreparedStatement ppdstt = conn.prepareStatement(sql);
			ppdstt.setLong(1, groupid);
			
			ResultSet result = ppdstt.executeQuery();
			
			if(result.next()){
				return Lang.getLang(result.getString(1));
			}else{
				return Lang.primaryLang;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}
