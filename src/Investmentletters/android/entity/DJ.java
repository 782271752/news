package Investmentletters.android.entity;

/**
 * µçÌ¨DJ
 * @author liang
 */
public class DJ extends BaseEntity {

	/**Í·Ïñ*/
	private String img = null;
	/**Î¢²©ID*/
	private String weiboId = null;
	/**Î¢²©ID*/
	private long Id = 0;

	/**Í·Ïñ*/
	public String getImg() {
		return img;
	}

	/**Í·Ïñ*/
	public void setImg(String img) {
		this.img = img;
	}

	/**Î¢²©ID*/
	public String getWeiboId() {
		return weiboId;
	}

	/**Î¢²©ID*/
	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	/**Î¢²©ID*/
	public long getwbId() {
		return Id;
	}

	/**Î¢²©ID*/
	public void setwbId(long weiboId) {
		this.Id = weiboId;
	}
}
