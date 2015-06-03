package Investmentletters.android.entity;

public class Hot_News {
	/** 新闻标题 */
	private String title = null;
	/** 新闻Url */
	private String url = null;
	/** 新闻时间 */
	private String time = null;

	public void settitle(String title) {
		this.title = title;
	}

	public String gettitle() {
		return title;
	}

	public void seturl(String url) {
		this.url = url;
	}

	public String geturl() {
		return url;
	}

	public void settime(String time) {
		this.time = time;
	}

	public String gettime() {
		return time;
	}
}
