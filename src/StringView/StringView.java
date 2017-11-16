package StringView;

public class StringView {
	private String meta;
	
	public int begin, end;
	
	public StringView(String string, int begin, int end){
		this.meta = string;
		this.begin = begin;
		this.end = end;
	}
	
	public StringView(String string){
		this(string, 0, string.length()-1);
	}
	
	public String getMeta(){
		return meta;
	}
	
	public char charAt(int index){
		return meta.charAt(begin + index);
	}
	
	public int length(){
		return end - begin + 1;
	}
	
	@Override
	public String toString(){
		return meta.substring(begin, end);
	}
}
