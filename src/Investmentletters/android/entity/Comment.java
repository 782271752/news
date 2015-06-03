package Investmentletters.android.entity;

public class Comment {
	
	/**（评论的）用户名*/
	private String userName = null;
	
	/**（评论的）时间*/
	private String time = null;
	
	/**（评论的）内容*/
	private String content = null;
	
	/**回复标识*/
	private int id = 0;

	/**（评论的）用户名*/
	public String getUserName() {
		return userName;
	}

	/**（评论的）用户名*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**（评论的）时间*/
	public String getTime() {
		return time;
	}

	/**（评论的）时间*/
	public void setTime(String time) {
		this.time = time;
	}

	/**（评论的）内容*/
	public String getContent() {
		return content;
	}

	/**（评论的）内容*/
	public void setContent(String content) {
		this.content = content;
	}

	/**回复标识*/
	public int getId() {
		return id;
	}

	/**回复标识*/
	public void setId(int id) {
		this.id = id;
	}
}
