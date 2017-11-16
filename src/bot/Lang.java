package bot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import langpack.LangPack;
import langpack.LangText;
import langpack.LangText.Version;


//	body
public class Lang extends LangPack{
//	static members:
	private static ArrayList<Lang> langs = new ArrayList<Lang>();
	public static Lang primaryLang;

//	static methods:
	public static void initialize(){
		try {
			loadLangs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getLangsName(){
		ArrayList<String> r = new ArrayList<String>();
		langs.forEach(lang->{
			r.add(lang.langpackName);
		});
		return r;
	}
	
	public static int getLangsCount(){
		return langs.size();
	}
	
	public static Lang getLang(String langname){
		for(Lang lang : langs)
			if(lang.langpackName.compareTo(langname)==0)
				return lang;
		return null;
	}
	
	
//	data members:
	private String filename;
	
	// langtext
	private LangText
	new_game_accept         ,
	new_game_reject         ,
	join_accept             ,
	join_reject             ,
	leave_accept            ,
	start_game_sucessfully  ,
	start_game_failed       ,
	busy_bot                ,
	busy_group              ,
	busy_user               ,
	havent_pm               ,
	join_redundantly        ,
	game_full               ,
	game_started            ,
	no_game_in_group        ,
	not_enough_players      ,
	eldest_hand             ,
	next_eldest_hand        ,
	selected_cards          ,
	no_cards_selected       ,
	played_card_accept      ,
	played_card_reject      ,
	pass_accept             ,
	next_player             ,
	skip_vote               ,
	list_hand_single_entry  ,
	player_skipped          ,
	game_cancelled          ,
	stop_by_admin           ,
	stop_by_bot             ,
	btn_play                ,
	btn_pass                ,
	btn_unselectall         ,
	btn_vote_skip           ,
	win                     ,
	lose                    ,
	too_early               ,
	about                   ,
	game_settings           ,
	btn_gamerule            ,
	btn_language            ,
	btn_save                ,
	btn_back                ,
	gamerule_description    ,
	btn_ncard               ,
	btn_allow_triple        ,
	btn_sv                  ,
	btn_svc                 ,
	btn_cctc                ,
	btn_sc                  ,
	btn_fc                  ,
	btn_ranking_first       ,
	btn_suit_first          ,
	btn_card_first          ,
	btn_allow               ,
	btn_disallow            ,
	ncard_description       ,
	allow_triple_description,
	svc_description         ,
	btn_set_svc             ,
	svc_begin_description   ,
	svc_end_description     ,
	cctc_description        ,
	btn_f_s                 ,
	btn_s_f                 ,
	sc_description          ,
	btn_custom              ,
	sc_custom_description   ,
	fc_description          ,
	language_description    ,
	not_group_admin         ,
	save_description        ,
	pm_bot                  ,
	btn_pm                  ;
	// 
	private Path path;
	

	
//	constructors:
	public Lang(Path filePath) throws Exception{
		filename = filePath.getFileName().toString();
		
		// load lang pack name and strings from file
		List<String> lines = Files.readAllLines(filePath);
		ArrayList<String> items = new ArrayList<>();
		StringBuilder builder = new StringBuilder();

		for(String str:lines){
			if(str.equals(";")){
				items.add(builder.toString());
				builder = new StringBuilder();
			}else{
				if(builder.length() == 0){
					builder.append(str);
				}else{
					builder.append("\n").append(str);
				}
			}
		}
		items.add(builder.toString());
		
		langpackName = items.get(0);
		new_game_accept          = new LangText(items.get(1 ));
		new_game_reject          = new LangText(items.get(2 ));
		join_accept              = new LangText(items.get(3 ));
		join_reject              = new LangText(items.get(4 ));
		leave_accept             = new LangText(items.get(5 ));
		start_game_sucessfully   = new LangText(items.get(6 ));
		start_game_failed        = new LangText(items.get(7 ));
		busy_bot                 = new LangText(items.get(8 ));
		busy_group               = new LangText(items.get(9 ));
		busy_user                = new LangText(items.get(10));
		havent_pm                = new LangText(items.get(11));
		join_redundantly         = new LangText(items.get(12));
		game_full                = new LangText(items.get(13));
		game_started             = new LangText(items.get(14));
		no_game_in_group         = new LangText(items.get(15));
		not_enough_players       = new LangText(items.get(16));
		eldest_hand              = new LangText(items.get(17));
		next_eldest_hand         = new LangText(items.get(18));
		selected_cards           = new LangText(items.get(19));
		no_cards_selected        = new LangText(items.get(20));
		played_card_accept       = new LangText(items.get(21));
		played_card_reject       = new LangText(items.get(22));
		pass_accept              = new LangText(items.get(23));
		next_player              = new LangText(items.get(24));
		skip_vote                = new LangText(items.get(25));
		list_hand_single_entry   = new LangText(items.get(26));
		player_skipped           = new LangText(items.get(27));
		game_cancelled           = new LangText(items.get(28));
		stop_by_admin            = new LangText(items.get(29));
		stop_by_bot              = new LangText(items.get(30));
		btn_play                 = new LangText(items.get(31));
		btn_pass                 = new LangText(items.get(32));
		btn_unselectall          = new LangText(items.get(33));
		btn_vote_skip            = new LangText(items.get(34));
		win                      = new LangText(items.get(35));
		lose                     = new LangText(items.get(36));
		too_early                = new LangText(items.get(37));
		about                    = new LangText(items.get(38));
		game_settings            = new LangText(items.get(39));
		btn_gamerule             = new LangText(items.get(40));
		btn_language             = new LangText(items.get(41));
		btn_save                 = new LangText(items.get(42));
		btn_back                 = new LangText(items.get(43));
		gamerule_description     = new LangText(items.get(44));
		btn_ncard                = new LangText(items.get(45));
		btn_allow_triple         = new LangText(items.get(46));
		btn_sv                   = new LangText(items.get(47));
		btn_svc                  = new LangText(items.get(48));
		btn_cctc                 = new LangText(items.get(49));
		btn_sc                   = new LangText(items.get(50));
		btn_fc                   = new LangText(items.get(51));
		btn_ranking_first        = new LangText(items.get(52));
		btn_suit_first           = new LangText(items.get(53));
		btn_card_first           = new LangText(items.get(54));
		btn_allow                = new LangText(items.get(55));
		btn_disallow             = new LangText(items.get(56));
		ncard_description        = new LangText(items.get(57));
		allow_triple_description = new LangText(items.get(58));
		svc_description          = new LangText(items.get(59));
		btn_set_svc              = new LangText(items.get(60));
		svc_begin_description    = new LangText(items.get(61));
		svc_end_description      = new LangText(items.get(62));
		cctc_description         = new LangText(items.get(63));
		btn_f_s                  = new LangText(items.get(64));
		btn_s_f                  = new LangText(items.get(65));
		sc_description           = new LangText(items.get(66));
		btn_custom               = new LangText(items.get(67));
		sc_custom_description    = new LangText(items.get(68));
		fc_description           = new LangText(items.get(69));
		language_description     = new LangText(items.get(70));
		not_group_admin          = new LangText(items.get(71));
		save_description         = new LangText(items.get(72));
		pm_bot                   = new LangText(items.get(73));
		btn_pm                   = new LangText(items.get(74));
	}
	
	
	
	
//	methods:
	public String getFileName(){
		return filename;
	}
	
	// codegen
	public String newGameAccept(){
		Version v = new_game_accept.getVersion()
			.reset();
		return v.getText();
	}
	
	public String newGameReject(){
		Version v = new_game_reject.getVersion()
			.reset();
		return v.getText();
	}
	
	public String joinAccept(){
		Version v = join_accept.getVersion()
			.reset();
		return v.getText();
	}
	
	public String joinReject(){
		Version v = join_reject.getVersion()
			.reset();
		return v.getText();
	}
	
	public String leaveAccept(String play){
		Version v = leave_accept.getVersion()
			.reset()
			.set("player", play);
		return v.getText();
	}
	
	public String startGameSucessfully(){
		Version v = start_game_sucessfully.getVersion()
			.reset();
		return v.getText();
	}
	
	public String startGameFailed(){
		Version v = start_game_failed.getVersion()
			.reset();
		return v.getText();
	}
	
	public String busyBot(){
		Version v = busy_bot.getVersion()
			.reset();
		return v.getText();
	}
	
	public String busyGroup(){
		Version v = busy_group.getVersion()
			.reset();
		return v.getText();
	}
	
	public String busyUser(){
		Version v = busy_user.getVersion()
			.reset();
		return v.getText();
	}
	
	public String haventPm(){
		Version v = havent_pm.getVersion()
			.reset();
		return v.getText();
	}
	
	public String joinRedundantly(){
		Version v = join_redundantly.getVersion()
			.reset();
		return v.getText();
	}
	
	public String gameFull(){
		Version v = game_full.getVersion()
			.reset();
		return v.getText();
	}
	
	public String gameStarted(){
		Version v = game_started.getVersion()
			.reset();
		return v.getText();
	}
	
	public String noGameInGroup(){
		Version v = no_game_in_group.getVersion()
			.reset();
		return v.getText();
	}
	
	public String notEnoughPlayers(){
		Version v = not_enough_players.getVersion()
			.reset();
		return v.getText();
	}
	
	public String eldestHand(String eldesthand, String mincard){
		Version v = eldest_hand.getVersion()
			.reset()
			.set("eldesthand", eldesthand)
		.set("mincard", mincard);
		return v.getText();
	}
	
	public String nextEldestHand(String eldesthand, String mincard){
		Version v = next_eldest_hand.getVersion()
			.reset()
			.set("eldesthand", eldesthand)
		.set("mincard", mincard);
		return v.getText();
	}
	
	public String selectedCards(){
		Version v = selected_cards.getVersion()
			.reset();
		return v.getText();
	}
	
	public String noCardsSelected(){
		Version v = no_cards_selected.getVersion()
			.reset();
		return v.getText();
	}
	
	public String playedCardAccept(String play, String cards, String nhandcard){
		Version v = played_card_accept.getVersion()
			.reset()
			.set("player", play)
			.set("play", cards)
			.set("left", nhandcard);
		return v.getText();
	}
	
	public String playedCardReject(){
		Version v = played_card_reject.getVersion()
			.reset();
		return v.getText();
	}
	
	public String passAccept(String play){
		Version v = pass_accept.getVersion()
			.reset()
			.set("player", play);
		return v.getText();
	}
	
	public String nextPlayer(String nextplay){
		Version v = next_player.getVersion()
			.reset()
			.set("nextplayer", nextplay);
		return v.getText();
	}
	
	public String skipVote(String play, String nvote, String nfulfillvote){
		Version v = skip_vote.getVersion()
			.reset()
			.set("player", play)
		.set("vote", nvote)
		.set("minvote", nfulfillvote);
		return v.getText();
	}
	
	public String listHandSingleEntry(String play, String ncard){
		Version v = list_hand_single_entry.getVersion()
			.reset()
			.set("player", play)
		.set("hand", ncard);
		return v.getText();
	}
	
	public String playerSkipped(String play){
		Version v = player_skipped.getVersion()
			.reset()
			.set("player", play);
		return v.getText();
	}
	
	public String gameCancelled(){
		Version v = game_cancelled.getVersion()
			.reset();
		return v.getText();
	}
	
	public String stopByAdmin(String players){
		Version v = stop_by_admin.getVersion()
			.reset()
			.set("players", players);
		return v.getText();
	}
	
	public String stopByBot(String players){
		Version v = stop_by_bot.getVersion()
			.reset()
			.set("players", players);
		return v.getText();
	}
	
	public String btnPlay(){
		Version v = btn_play.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnPass(){
		Version v = btn_pass.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnUnselectall(){
		Version v = btn_unselectall.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnVoteSkip(){
		Version v = btn_vote_skip.getVersion()
			.reset();
		return v.getText();
	}
	
	public String win(String winner){
		Version v = win.getVersion()
			.reset()
			.set("winner", winner);
		return v.getText();
	}
	
	public String lose(String losers){
		Version v = lose.getVersion()
			.reset()
			.set("losers", losers);
		return v.getText();
	}
	
	public String tooEarly(String remains){
		Version v = too_early.getVersion()
			.reset()
			.set("remains", remains);
		return v.getText();
	}
	
	public String about(String version){
		Version v = about.getVersion()
			.reset()
			.set("version", version);
		return v.getText();
	}
	
	public String gameSettings(String group){
		Version v = game_settings.getVersion()
			.reset()
			.set("group", group);
		return v.getText();
	}
	
	public String btnGamerule(){
		Version v = btn_gamerule.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnLanguage(){
		Version v = btn_language.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSave(){
		Version v = btn_save.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnBack(){
		Version v = btn_back.getVersion()
			.reset();
		return v.getText();
	}
	
	public String gameruleDescription(){
		Version v = gamerule_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnNcard(){
		Version v = btn_ncard.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnAllowTriple(){
		Version v = btn_allow_triple.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSv(){
		Version v = btn_sv.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSvc(){
		Version v = btn_svc.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnCctc(){
		Version v = btn_cctc.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSc(){
		Version v = btn_sc.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnFc(){
		Version v = btn_fc.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnRankingFirst(){
		Version v = btn_ranking_first.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSuitFirst(){
		Version v = btn_suit_first.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnCardFirst(){
		Version v = btn_card_first.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnAllow(){
		Version v = btn_allow.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnDisallow(){
		Version v = btn_disallow.getVersion()
			.reset();
		return v.getText();
	}
	
	public String ncardDescription(){
		Version v = ncard_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String allowTripleDescription(){
		Version v = allow_triple_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String svcDescription(String sv){
		Version v = svc_description.getVersion()
			.reset()
			.set("sv", sv);
		return v.getText();
	}
	
	public String btnSetSvc(){
		Version v = btn_set_svc.getVersion()
			.reset();
		return v.getText();
	}
	
	public String svcBeginDescription(){
		Version v = svc_begin_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String svcEndDescription(String svcbegin){
		Version v = svc_end_description.getVersion()
			.reset()
			.set("svcbegin", svcbegin);
		return v.getText();
	}
	
	public String cctcDescription(){
		Version v = cctc_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnFS(){
		Version v = btn_f_s.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnSF(){
		Version v = btn_s_f.getVersion()
			.reset();
		return v.getText();
	}
	
	public String scDescription(String sc){
		Version v = sc_description.getVersion()
			.reset()
			.set("sc", sc);
		return v.getText();
	}
	
	public String btnCustom(){
		Version v = btn_custom.getVersion()
			.reset();
		return v.getText();
	}
	
	public String scCustomDescription(String scc){
		Version v = sc_custom_description.getVersion()
			.reset()
			.set("scc", scc);
		return v.getText();
	}
	
	public String fcDescription(){
		Version v = fc_description.getVersion()
			.reset();
		return v.getText();
	}
	
	public String languageDescription(String language){
		Version v = language_description.getVersion()
			.reset()
			.set("language", language);
		return v.getText();
	}
	
	public String notGroupAdmin(String group){
		Version v = not_group_admin.getVersion()
			.reset()
			.set("group", group);
		return v.getText();
	}
	
	public String saveDescription(String group){
		Version v = save_description.getVersion()
			.reset()
			.set("group", group);
		return v.getText();
	}
	
	public String pmBot(){
		Version v = pm_bot.getVersion()
			.reset();
		return v.getText();
	}
	
	public String btnPm(){
		Version v = btn_pm.getVersion()
			.reset();
		return v.getText();
	}
	// /codegen

	
	
	
	
	public static void loadLangs() throws Exception{
		final String PATH_LANG_FOLDER = new String("./langs/");
		final String PRIMARY_LANG     = "Chinese.txt";
		
		Path primaryFilePath = Paths.get(PATH_LANG_FOLDER + PRIMARY_LANG);
		

		primaryLang = null;
		
		// load and add langs
		try(Stream<Path> filePaths = Files.walk(Paths.get(PATH_LANG_FOLDER))){
			filePaths
				.filter(Files::isRegularFile)
				.forEach(filePath->{
					if(filePath.compareTo(primaryFilePath)==0){
						try {
							primaryLang = new Lang(filePath);
						} catch (Exception e) {
							e.printStackTrace();
						}
						langs.add(primaryLang);
					}else{
						try {
							langs.add(new Lang(filePath));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
				});
		}
	}
	
	public static void reloadLangs() throws Exception{
		langs.clear();
		loadLangs();
	}
	
	
	//debug
	public static void main(String[] args) throws Exception{
		Lang.initialize();
		System.out.println("lnags: " + langs.size());
		langs.forEach(lang->{
			System.out.print(
				lang.langpackName
				+ "\n" + lang.newGameAccept()
				+ "\n" + lang.newGameAccept()
				+ "\n" + lang.newGameReject()
				+ "\n" + lang.skipVote("SKIPPEE", "1", "3")
				+ "\n" + lang.lose("LOSER")
				+ "\n" + lang.lose("LOSER")
				+ "\n\n");
		});
		System.out.println(primaryLang.langpackName);
	}
}
