package Investmentletters.android.entity;

public class Hot_News {
	/** ���ű��� */
	private String title = null;
	/** ����Url */
	private String url = null;
	/** ����ʱ�� */
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
