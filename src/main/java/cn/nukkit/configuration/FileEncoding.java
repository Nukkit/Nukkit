package main.java.cn.Nugget.configuration;

public enum FileEncoding {
	UTF8("UTF-8"),
	ASCII("US-ASCII"),
	UTF16("UTF-16");
	
	
	private String context;
	private FileEncoding(String i){
		setContext(i);
	}
	public String getContext() {
		return context;
	}
	private void setContext(String context) {
		this.context = context;
	}
}
