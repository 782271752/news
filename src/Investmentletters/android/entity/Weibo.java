package Investmentletters.android.entity;

/**
 * ����Entiry��
 * 
 * @author liang
 */
public class Weibo {

	/** ΢��id */
	private int id = 0;
	/** ΢�� */
	private String text = null;
	/** ͼƬ��ַ */
	private String Url = null;
	/** ������ */
	private int Comments = 0;
	/** ת���� */
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
