package Investmentletters.android.entity;

/**
 * 基本Entiry类
 * 
 * @author liang
 */
public class Weibo {

	/** 微博id */
	private int id = 0;
	/** 微博 */
	private String text = null;
	/** 图片地址 */
	private String Url = null;
	/** 评论数 */
	private int Comments = 0;
	/** 转发数 */
	private int Reposts = 0;

	public String Url() {
		return Url;
	}

	public void setUrl(String Url) {
		this.Url = Url;
	}

	public int setComments() {
		return Comments;
	}

	public void setComments(int Comments) {
		this.Comments = Comments;
	}

	public int getReposts() {
		return Reposts;
	}

	public void setReposts(int Reposts) {
		this.Reposts = Reposts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String gettext() {
		return text;
	}

	public void settext(String text) {
		this.text = text;
	}

}
