package Investmentletters.android.entity;

public class Comment {
	
	/**�����۵ģ��û���*/
	private String userName = null;
	
	/**�����۵ģ�ʱ��*/
	private String time = null;
	
	/**�����۵ģ�����*/
	private String content = null;
	
	/**�ظ���ʶ*/
	private int id = 0;

	/**�����۵ģ��û���*/
	public String getUserName() {
		return userName;
	}

	/**�����۵ģ��û���*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**�����۵ģ�ʱ��*/
	public String getTime() {
		return time;
	}

	/**�����۵ģ�ʱ��*/
	public void setTime(String time) {
		this.time = time;
	}

	/**�����۵ģ�����*/
	public String getContent() {
		return content;
	}

	/**�����۵ģ�����*/
	public void setContent(String content) {
		this.content = content;
	}

	/**�ظ���ʶ*/
	public int getId() {
		return id;
	}

	/**�ظ���ʶ*/
	public void setId(int id) {
		this.id = id;
	}
}
