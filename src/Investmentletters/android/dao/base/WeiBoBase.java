package Investmentletters.android.dao.base;

public class WeiBoBase {

	/** ΢��id */
	public int id = 0;
	/** ΢�� ���� */
	public String text = null;
	/** ת��΢������ */
	public String retweet_text = null;
	/** ͼƬ��ַ */
	public String[] ImageUrl = null;
	/** ������ */
	public int Comments = 0;
	/** ת���� */
	public int Reposts = 0;
	/** ΢��ʱ�� */
	public String Time = null;
	/** ΢��ͼƬ��ͼ��ַ */
	public String original_pic = null;

	public WeiBoBase(String text, String retweet_text, String[] ImageUrl,
			int Comments, int Reposts, String Time,String original_pic) {
		this.text = text;
		this.ImageUrl = ImageUrl;
		this.Comments = Comments;
		this.Reposts = Reposts;
		this.Time = Time;
		this.retweet_text = retweet_text;
		this.original_pic = original_pic;
	}

	public WeiBoBase(boolean isRepost,String retweet_text) {

	}
}
