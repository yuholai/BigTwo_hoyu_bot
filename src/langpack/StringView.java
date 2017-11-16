package langpack;

public class StringView {
	public String string;
	public int begin, end;
	
	public StringView(String string, int begin, int end){
		this.string = string;
		this.begin = begin;
		this.end = end;
	}
	
	public StringView(String string, int begin){
		this(string, begin, string.length() - 1);
	}
	
	public StringView(String string){
		this(string, 0);
	}
	
	public String substring(){
		return string.substring(begin, end + 1);
	}
}
