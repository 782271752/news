package Investmentletters.android.entity;

/**
 * ��̨DJ
 * @author liang
 */
public class DJ extends BaseEntity {

	/**ͷ��*/
	private String img = null;
	/**΢��ID*/
	private String weiboId = null;
	/**΢��ID*/
	private long Id = 0;

	/**ͷ��*/
	public String getImg() {
		return img;
	}

	/**ͷ��*/
	public void setImg(String img) {
		this.img = img;
	}

	/**΢��ID*/
	public String getWeiboId() {
		return weiboId;
	}

	/**΢��ID*/
	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	/**΢��ID*/
	public long getwbId() {
		return Id;
	}

	/**΢��ID*/
	public void setwbId(long weiboId) {
		this.Id = weiboId;
	}
}
