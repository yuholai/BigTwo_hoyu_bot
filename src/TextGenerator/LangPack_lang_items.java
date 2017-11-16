package TextGenerator;

public class LangPack_lang_items {
	public static class Parameter{
		public String	field,
						arg;
		public Parameter(String field, String arg){
			this.field = field;
			this.arg = arg;
		}
	}
	
	public String 	langtextname;
	public String   functionname;
	public Parameter[] params;
	
	public LangPack_lang_items(String args){
		String[] splits = args.split(" ");
		params = new Parameter[(splits.length-1)/2];
		
		langtextname = splits[0];
		for(int i=1; i<splits.length;){
			Parameter param = new Parameter(splits[i], splits[i+1]);
			params[i/2] = param;
			i+=2;
		}
		
		StringBuilder bfn = new StringBuilder();
		for(int i=0; i<langtextname.length(); ++i){
			if(langtextname.charAt(i) == '_'){
				++i;
				if(i < langtextname.length()){
					char chr2 = langtextname.charAt(i);
					if(Character.isAlphabetic(chr2)){
						chr2 = Character.toUpperCase(chr2);
					}
					bfn.append(chr2);
				}
			}else{
				bfn.append(langtextname.charAt(i));
			}
		}
		functionname = bfn.toString();
	}
	
	public void appendGetTo(StringBuilder builder){
		builder.append("public String ").append(functionname)
			.append("(");
		if(params.length != 0){
			builder.append("String ").append(params[0].arg);
			for(int i=1; i<params.length; ++i)
				builder.append(", String ").append(params[i].arg);
		}
		builder.append("){\n\tVersion v = ").append(langtextname)
		.append(".getVersion()\n\t\t.reset()");
		for(Parameter param:params)
			//if(!param.field.equals("player"))
				builder.append("\n\t\t.set(\"").append(param.field).append("\", ")
				.append(param.arg).append(")");
		builder.append(";\n\treturn v.getText();\n}");
	}
	
	
	
	public static String[] padding(String[] strs){
		String[] r = new String[strs.length];
		
		int max = 0;
		for(int i=0; i<strs.length; ++i){
			if(strs[i].length() > max) max = strs[i].length(); 
		}
		
		for(int i=0; i<strs.length; ++i){
			if(strs[i].length() < max){
				StringBuilder str = new StringBuilder(max - strs[i].length());
				str.append(strs[i]);
				for(int j=strs[i].length(); j<max; ++j)
					str.append(' ');
				r[i] = str.toString();
			}else{
				r[i] = strs[i];
			}
		}
		
		return r;
	}
	
	public static String code_gen(LangPack_lang_items[] items){
		StringBuilder r = new StringBuilder();
		
		String[] items_name = new String[items.length];
		for(int i=0; i<items.length; ++i)
			items_name[i] = items[i].langtextname;
		
		String[] padded = padding(items_name);
		
		// fields
		r.append("private LangText\n");
		for(int i=0; i<items_name.length; ++i){
			r.append("\t").append(padded[i]);
			if(i!=items_name.length-1){
				r.append(",\n");
			}else{
				r.append(";\n");
			}
		}
		r.append("\n\n\n");
		
		// read to string array
		for(int i=0; i<items_name.length; ++i){
			r.append(padded[i]).append(" = ").append("new LangText(items[").append(i+1).append("]);\n");
		}
		
		r.append("\n\n\n");
		
		for(int i=0; i<items.length; ++i){
			items[i].appendGetTo(r);
			r.append("\n\n");
		}
		
		return r.toString();
	}
	
	public static LangPack_lang_items[] a(String... strs){
		LangPack_lang_items[] r = new LangPack_lang_items[strs.length];
		for(int i=0; i<strs.length; ++i)
			r[i] = new LangPack_lang_items(strs[i]);
		
		return r;
	}
	
	public static void main(String[] args){
		LangPack_lang_items[] items = a(
			"new_game_accept",
			"new_game_reject",
			"join_accept",        
			"join_reject",
			"leave_accept player play",
			"start_game_sucessfully",
			"start_game_failed",
			"busy_bot",   
			"busy_group",   
			"busy_user",   
			"havent_pm",  
			"join_redundantly",       
			"game_full",       
			"game_started",
			"no_game_in_group",
			"not_enough_players",
			"eldest_hand eldesthand eldesthand mincard mincard",
			"next_eldest_hand eldesthand eldesthand mincard mincard",
			"selected_cards",
			"no_cards_selected",
			"played_card_accept player play play cards left nhandcard",
			"played_card_reject",
			"pass_accept player play",
			"next_player nextplayer nextplay",
			"skip_vote player play vote nvote minvote nfulfillvote",
			"list_hand_single_entry player play hand ncard",
			"player_skipped player play",
			"game_cancelled",
			"stop_by_admin",                   
			"stop_by_bot",
			"btn_play",
			"btn_pass",
			"btn_unselectall",
			"btn_vote_skip",
			"win winner winner",
			"lose losers losers",
			"too_early remains remains",
			"about version version",
			
			"game_settings group group",
			"btn_gamerule",
			"btn_language",
			"btn_save",
			"btn_back",
			"gamerule_description",
			"btn_ncard",
			"btn_allow_triple",
			"btn_sv",
			"btn_svc",
			"btn_cctc",
			"btn_sc",
			"btn_fc",
			"btn_ranking_first",
			"btn_suit_first",
			"btn_card_first",
			"btn_allow",
			"btn_disallow",
			"ncard_description",
			"allow_triple_description",
			"svc_description sv sv",
			"btn_set_svc",
			"svc_begin_description",
			"svc_end_description svcbegin svcbegin",
			"cctc_description",
			"btn_f_s",
			"btn_s_f",
			"sc_description sc sc",
			"btn_custom",
			"sc_custom_description scc scc",
			"fc_description",
			"language_description language language",
			"not_group_admin group group",
			"save_description group group",
			"pm_bot",
			"btn_pm"
		);
		
		System.out.println(code_gen(items));
	}
}
