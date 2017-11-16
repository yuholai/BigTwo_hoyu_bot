package TextGenerator;

import bigtwo.game.Play;
import bigtwo.game.PlayCardResult;
import bigtwo.game.PlayerPosition;
import bigtwo.game.VoteSkip.VoteSkipRequestResult;

public class class_1 {
	private static String codegen_class(String classname, String[] types, String[] varnames){
		int max_type_len, max_varname_len;
		
		max_type_len = 0;
		for(String type : types)
			if(type.length() > max_type_len)
				max_type_len = type.length();
		
		max_varname_len = 0;
		for(String varname : varnames)
			if(varname.length() > max_varname_len)
				max_varname_len = varname.length();
		
		
		StringBuilder[] filledtypes, filledvars;
		filledtypes = new StringBuilder[types.length];
		filledvars  = new StringBuilder[varnames.length];
		for(int i=0; i<types.length; ++i){
			StringBuilder filledtype, filledvar;
			
			filledtype = new StringBuilder(max_type_len);
			filledtype.append(types[i]);
			for(int j=types[i].length(); j<max_type_len; ++j)
				filledtype.append(' ');
			filledtypes[i] = filledtype;
			
			filledvar = new StringBuilder(max_varname_len);
			filledvar.append(varnames[i]);
			for(int j=varnames[i].length(); j<max_varname_len; ++j)
				filledvar.append(' ');
			filledvars [i] = filledvar;
		}
		

		
		StringBuilder fields = new StringBuilder();
		fields.append("public final ").append(filledtypes[0]).append(' ')
			.append(filledvars[0]).append(";");
		for(int i=1; i<types.length; ++i)
			fields.append("\npublic final ").append(filledtypes[i]).append(' ')
			.append(filledvars[i]).append(";");
		
		StringBuilder constructor = new StringBuilder();
		constructor.append("private ").append(classname).append("\n(");
		constructor.append(filledtypes[0]).append(' ')
			.append(filledvars[0]);
		for(int i=1; i<types.length; ++i)
			constructor.append(",\n ").append(filledtypes[i]).append(' ')
			.append(filledvars[i]);
		constructor.append(")\n{\n");
		constructor.append("\tthis.").append(filledvars[0]).append(" = ").append(filledvars[0]).append(';');
		for(int i=1; i<types.length; ++i)
			constructor.append("\n\tthis.").append(filledvars[i]).append(" = ").append(filledvars[i]).append(';');
		constructor.append("\n}");
		
		StringBuilder r = new StringBuilder();
		r.append(fields.toString()).append('\n').append(constructor.toString());
		return r.toString();
	}
	private static String codegen_class1(String classname, String[] seq){
		int size = seq.length / 2;
		String[] types = new String[size],
				 vars  = new String[size];
		
		for(int i=0, j=0; j<size; ++j){
			types[j] = seq[i];
			++i;
			vars[j] = seq[i];
			++i;
		}
		
		return codegen_class(classname, types, vars);
	}
	
	public static void main(String[] args){
		/*
		System.out.println(codegen_class(
			"",
			new String[]{}, 
			new String[]{}
		));
		*/
		
		//System.out.println(codegen_class("StartGame",
		//	new String[]{
		//		"StartGameTrigger",
		//		"StartGameResult ",
		//		"Game            ",
		//		"Player          "
		//	},
		//	new String[]{
		//		"trigger  ",
		//		"result   ",
		//		"game     ",
		//		"initiator"
		//	}));
		
		//System.out.println(codegen_class(
		//		"Pass",
		//		new String[]{
		//				"Play",
		//				"PassResult"
		//		}, 
		//		new String[]{
		//				"play",
		//				"result"
		//		}
		//	));
		
		//System.out.println(codegen_class(
		//		"PlayCardResult",
		//		new String[]{
		//				"PlayCard",
		//				"Game.GameResult"
		//		}, 
		//		new String[]{
		//				"playcard",
		//				"result"
		//		}
		//	));
		
		//System.out.println(codegen_class(
		//		"InitiateSkip",
		//		new String[]{
		//				"Play",
		//				"InitiateSkipResult"
		//		}, 
		//		new String[]{
		//				"initiator",
		//				"result"
		//		}
		//	));
		
		System.out.println(codegen_class1(
				"VoteSkip",
				new String[]{
						"Play",                  "voter",        
						"VoteSkipRequestResult", "requestresult",
						"int",                   "nvote",        
						"int",                   "nfulfill",     
						"boolean",               "skip",
						"Play",                  "skipped",
						"PlayerPosition",        "position"
				}
			));
	}
}
