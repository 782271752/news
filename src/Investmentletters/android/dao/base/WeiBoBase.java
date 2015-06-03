package Investmentletters.android.dao.base;

public class WeiBoBase {

	/** 微博id */
	public int id = 0;
	/** 微博 内容 */
	public String text = null;
	/** 转播微博内容 */
	public String retweet_text = null;
	/** 图片地址 */
	public String[] ImageUrl = null;
	/** 评论数 */
	public int Comments = 0;
	/** 转发数 */
	public int Reposts = 0;
	/** 微博时间 */
	public String Time = null;
	/** 微博图片大图地址 */
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
