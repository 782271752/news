package Investmentletters.android.entity;

/**
 * 新闻实体类
 * 
 * @author liang
 */
public class News {

	/** 标识 */
	private int id = -1;
	
	/** 标题 */
	private String title = null;
	
	/** 内容(标题中的内容提要） */
	private String summary = null;
	
	/** 时间 */
	private String time = null;
	
	/** 缩略图 */
	private String minImg = null;
	
	/** 大图 */
	private String bigImg = null;
	
	/** 全部内容 */
	private String content = null;
	
	/** 置顶 */
	private int top = -1;
	
	/**是否选中,在收藏夹中设置*/
	private boolean isSelect = false;
	
	/**新闻在服务器中的类型<strong>注意，与本地数据库的类型不相同</strong>*/
	private int type = -1;
	
	/** 标识 */
	public int getId() {
		return id;
	}

	/** 标识 */
	public void setId(int id) {
		this.id = id;
	}

	/** 标题 */
	public String getTitle() {
		return title;
	}

	/** 标题 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** 内容(标题中的内容提要） */
	public String getSummary() {
		return summary;
	}

	/** 内容(标题中的内容提要） */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/** 时间 */
	public String getTime() {
		return time;
	}

	/** 时间 */
	public void setTime(String time) {
		this.time = time;
	}

	/** 缩略图 */
	public String getMinImg() {
		return minImg;
	}

	/** 缩略图 */
	public void setMinImg(String minImg) {
		this.minImg = minImg;
	}

	/** 大图 */
	public String getBigImg() {
		return bigImg;
	}

	/** 大图 */
	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}

	/** 全部内容 */
	public String getContent() {
		return content;
	}

	/** 全部内容 */
	public void setContent(String content) {
		this.content = content;
	}

	/** 置顶 */
	public int getTop() {
		return top;
	}

	/** 置顶 */
	public void setTop(int top) {
		this.top = top;
	}

	/**是否选中,在收藏夹中设置*/
	public boolean isSelect() {
		return isSelect;
	}

	/**是否选中,在收藏夹中设置*/
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	/**新闻在服务器中的类型<strong>注意，与本地数据库的类型不相同</strong>*/
	public int getType() {
		return type;
	}

	/**新闻在服务器中的类型<strong>注意，与本地数据库的类型不相同</strong>*/
	public void setType(int type) {
		this.type = type;
	}
	
}

