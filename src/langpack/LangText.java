package langpack;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import org.telegram.telegrambots.api.objects.MessageEntity;

public class LangText {
	static abstract class Content
	{
		public abstract void appendTo(StringBuilder builder);
	}
	
	static class ConstantContent
	extends Content
	{
		public String content;
		
		public ConstantContent(String content){this.content = content;}
		
		@Override
		public void appendTo(StringBuilder builder)
		{
			builder.append(content);
		}
	}
	
	static class VariableContent
	extends Content
	{
		public String field, content;
		public UserTag tag;
		public UserTag[] tags;
		public ArrayList<MessageEntity> entities;
		
		public VariableContent(String field){this.field = field;}
		
		public void set(String content){
			this.content = content;
		}
		
		@Override
		public void appendTo(StringBuilder builder)
		{
			if(content != null)
				builder.append(content);
			else if(entities != null){
				if(tag != null){
					MessageEntity entity = new MessageEntity();
					
				}else{
					
				}
			}
			else
				builder.append(field);
		}
		
		public void set(UserTag tag, ArrayList<MessageEntity> entities){
			this.tag = tag;
			this.entities = entities;
		}
		
		public void set(UserTag[] tags, ArrayList<MessageEntity> entities){
			this.tags = tags;
			this.entities = entities;
		}
		
		public void reset(){
			content = null;
			tag = null;
			tags = null;
			entities = null;
		}
	}
	
	static class VariableContentEntry{
		private TreeMap<String, VariableContent> map;
		
		public void add(VariableContent vc)
		{
			map.put(vc.field, vc);
		}
		
		public void set(String field, String value)
		{
			VariableContent vc = map.get(field);
			if(vc != null)
				vc.content = value;
		}
		
		public VariableContent get(String field){
			return map.get(field);
		}
		
		public void reset()
		{
			for(Map.Entry<String, VariableContent> entry : map.entrySet())
			{
				entry.getValue().reset();
			}
		}
		
		public VariableContentEntry()
		{
			map = new TreeMap<>(String::compareTo);
		}
		
		public VariableContentEntry(VariableContent[] vcs){
			this();
			for(VariableContent content : vcs)
				add(content);
		}
	}

	public static class Version{
		private ArrayList<Content> _contents;
		private VariableContentEntry _entries;
		
		private Version(VariableContentEntry entries){
			_contents = new ArrayList<>();
			_entries = entries;
		}
		
		public Version(String raw, VariableContentEntry entries)
		{
			this(entries);
			
			int pos = 0;
			for(int i=0; i<raw.length(); ++i)
			{
				if(raw.charAt(i) == '$')
				{
					int j = i + 1;
					if(j < raw.length() && raw.charAt(j) == '(')
					{
						for(int k=j+1; k<raw.length(); ++k)
						{
							char chr = raw.charAt(k);
							if(chr == ')')
							{
								/*if(k == j+1)
								{
									
								}
								else
								{
									
								}*/
								if(i > pos)
									_contents.add(new ConstantContent(raw.substring(pos, i)));
								if(j+1 < k)
								{
									String field = raw.substring(j+1, k);
									VariableContent vc = _entries.get(field);
									if(vc == null){
										vc = new VariableContent(field);
										_entries.add(vc);
									}
									_contents.add(vc);
								}
								pos = k + 1;
								i = k;
								break;
							}
							else if(chr < 'a' || 'z' < chr)
							{
								if(chr == '$')
									i = k - 1;
								else
									i = k;
								break;
							}
						}
					}
					else
					{
						++i;
					}
				}
			}
			if(pos < raw.length()){
				_contents.add(new ConstantContent(raw.substring(pos)));
			}
		}

		public Version(String raw){
			this(raw, new VariableContentEntry());
		}
		
		public Version reset()
		{
			_entries.reset();
			return this;
		}
	
		public Version set(String field, String content)
		{
			_entries.set(field, content);
			return this;
		}
		
		public Version set(String field, UserTag tag, ArrayList<MessageEntity> entities){
			return this;
		}
		
		public String getText()
		{
			StringBuilder str = new StringBuilder();
			for(Content content : _contents)
			{
				content.appendTo(str);
			}
			return str.toString();
		}
	}
	
	public static class UserTag{
		public String name;
		public int userid;
		
		public UserTag(String name, int userid){
			this.name = name;
			this.userid = userid;
		}
		
		
	}
	
	
	public static final String 	argsIdentifier = "$()",
								argsIdentifierExpr = "\\$\\(\\)",
								VERSION_SEPERATOR = ",",
								LANGTEXT_SEPERATOR = ";";
	
	private Version[] _versions;
	private VariableContentEntry _entries;
	
	public LangText(String raw){
		//ArrayList<Version> versions = new ArrayList<>();
		_entries = new VariableContentEntry();

		String[] splits = raw.split("\n,\n");
		_versions = new Version[splits.length];
		for(int i=0; i<splits.length; ++i)
			_versions[i] = new Version(splits[i], _entries);
		//int pos = 0;
		//int end = raw.length() - 1;
		//for(int i=0; i<end; ++i){
		//	char chr1 = raw.charAt(i);
		//	if(chr1 == '\n'){
		//		++i;
		//		char chr2 = raw.charAt(i);
		//		if(chr2 == ','){
		//			//versions.add
		//		}
		//	}
		//}
	}
	
	public int getVersionCount(){
		return _versions.length;
	}
	private int getRand(){
		return ThreadLocalRandom.current().nextInt(_versions.length);
	}
	public Version getVersion(){
		return _versions[getRand()];
	}
	public Version getVersion(int index){
		return _versions[index];
	}
	public String getText(){
		return getVersion().reset().getText();
	}
	public String getText(String... args){
		Version version = _versions[getRand()];
		version.reset();
		for(int i=0; i<args.length; i+=2){
			version.set(args[i], args[i+1]);
		}
		return version.getText();
	}
	
	public void reset(){
		_entries.reset();
	}
	
	
	
/************************************************************/
	public static void main(String[] args){
		final String
		test = 
			"abcde$(fgh)ijk$(lmn)opq$()",
		test1 =
			"$(player)";
		Version v1 = new Version(test);
		v1.reset();
		v1.set("fgh", "FGH");
		v1.set("lmn", "LMN");
		v1.set("XYZ", "Xhc");
		System.out.println(v1.getText());
		Version v2 = new Version(test1);
		v2.set("fa", "afe");
		v2.set("player", "hahhaha");
		System.out.println(v2.getText());
	}
}
