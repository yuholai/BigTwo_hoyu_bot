package bigtwo.game;

public class Database {
	public static final String
	TABLE_GROUP               = "gamegroup"  ,
	FIELD_GROUP_GROUPID       = "groupid"    ,
	FIELD_GROUP_EASTEREGG     = "easteregg"  ,
	FIELD_GROUP_LANG          = "lang"       ,
	FIELD_GROUP_LINK          = "link"       ,
	FIELD_GROUP_NCARD      	  = "ncard"      ,
	FIELD_GROUP_ALLOWTRIPLE	  = "allowtriple",
	FIELD_GROUP_SVC_DATA      = "svc_data"   ,
	FIELD_GROUP_CCTC_TYPE     = "cctc_type"  ,
	FIELD_GROUP_SC_DATA       = "sc_data"    ,
	FIELD_GROUP_FC_TYPE       = "fc_type"    ;
	
	public static final int
	FILED_INDEX_GROUP_GROUPID     =  1,
	FILED_INDEX_GROUP_EASTEREGG   =  2,
	FILED_INDEX_GROUP_LANG        =  3,
	FILED_INDEX_GROUP_LINK        =  4,	
	FILED_INDEX_GROUP_NCARD       =  5,	
	FILED_INDEX_GROUP_ALLOWTRIPLE =  6,	
	FILED_INDEX_GROUP_SVC_DATA    =  7,	
	FILED_INDEX_GROUP_CCTC_TYPE   =  8,	
	FILED_INDEX_GROUP_SC_DATA     =  9,	
	FILED_INDEX_GROUP_FC_TYPE     = 10;
	
	
	public static final String
	TABLE_PLAYER             = "player"    ,
	FIELD_PLAYER_PLAYERID    = "playerid"  ,
	FIELD_PLAYER_GAME_PLAYED = "gameplayed",
	FIELD_PLAYER_GAME_WON    = "gamewon"   ;
}
