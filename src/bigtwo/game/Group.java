package bigtwo.game;

import java.util.HashMap;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;

public class Group {
//	class constants:
	public static final int MAX_GAME = 1;
	
	public static final String
	TABLE_GROUP   = "gamegroup",
	FIELD_GROUPID = "groupid"  ;
	
	
	
//	class fields:
	private static HashMap<Long, Group>
	groupTable/*cachedgroups*/ = new HashMap<>();	// Long: telegram group chat id
	
	
	
//	class methods:
	public static void cache(Group group){
		groupTable.put(group.groupid, group);
	}
	
	public static Group getCachedGroup(Long groupid){
		return groupTable.get(groupid);
	}
	
	public static Group uncached(Long groupid){
		return groupTable.remove(groupid);
	}
	
	public static Group uncache(Group group){
		return groupTable.remove(group.groupid);
	}
	
	public static Group getGroup(Chat chat){
		Group group;
		
		group = getCachedGroup(chat.getId());
		
		if(group == null)
		{
			group = new Group(chat);
		}
		
		return group;
	}
	
	
	
//	instance fields:
	public final Long groupid;
	
	private GroupSettings settings;
	
	private Game games;
	
	//private GameSettings settings;		// asso
	
	
	
//	constructors:
	public Group(Chat chat){
		this.groupid = chat.getId();
		
		games = null;
		
		settings = new GroupSettings(groupid);
	}
	
	
	
//	association methods:
	public void addGame(Game game){
		games = game;
		Group.cache(this);
	}
	
	public Game getGame(){
		return games;
	}
	
	public void removeGame(){
		games = null;
		Group.uncache(this);
	}
	
	
	
	public GroupSettings getSettings(){
		return settings;
	}
	
	
	
//	instance methods:
	public Long getChatId(){
		return groupid;
	}
	
	public boolean isReachMaxGame(){
		return games != null;
	}
	
	
	public void newGame(User initiator){
		Player player = Player.getPlayer(initiator);
		
		if(Game.reachMaxGame()){
			return;
		}
		
		if(games != null){
			return;
		}
		
		if(player.isPlaying()){
			return;
		}
		
		
		GameSettings settings = GameSettings.getSettings(groupid);
		Game newgame = Game.newGame(settings);
		
		addGame(newgame);
		
		player.join(newgame);
	}
}
