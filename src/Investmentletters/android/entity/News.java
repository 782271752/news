package Investmentletters.android.entity;

/**
 * ����ʵ����
 * 
 * @author liang
 */
public class News {

	/** ��ʶ */
	private int id = -1;
	
	/** ���� */
	private String title = null;
	
	/** ����(�����е�������Ҫ�� */
	private String summary = null;
	
	/** ʱ�� */
	private String time = null;
	
	/** ����ͼ */
	private String minImg = null;
	
	/** ��ͼ */
	private String bigImg = null;
	
	/** ȫ������ */
	private String content = null;
	
	/** �ö� */
	private int top = -1;
	
	/**�Ƿ�ѡ��,���ղؼ�������*/
	private boolean isSelect = false;
	
	/**�����ڷ������е�����<strong>ע�⣬�뱾�����ݿ�����Ͳ���ͬ</strong>*/
	private int type = -1;
	
	/** ��ʶ */
	public int getId() {
		return id;
	}

	/** ��ʶ */
	public void setId(int id) {
		this.id = id;
	}

	/** ���� */
	public String getTitle() {
		return title;
	}

	/** ���� */
	public void setTitle(String title) {
		this.title = title;
	}

	/** ����(�����е�������Ҫ�� */
	public String getSummary() {
		return summary;
	}

	/** ����(�����е�������Ҫ�� */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/** ʱ�� */
	public String getTime() {
		return time;
	}

	/** ʱ�� */
	public void setTime(String time) {
		this.time = time;
	}

	/** ����ͼ */
	public String getMinImg() {
		return minImg;
	}

	/** ����ͼ */
	public void setMinImg(String minImg) {
		this.minImg = minImg;
	}

	/** ��ͼ */
	public String getBigImg() {
		return bigImg;
	}

	/** ��ͼ */
	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}

	/** ȫ������ */
	public String getContent() {
		return content;
	}

	/** ȫ������ */
	public void setContent(String content) {
		this.content = content;
	}

	/** �ö� */
	public int getTop() {
		return top;
	}

	/** �ö� */
	public void setTop(int top) {
		this.top = top;
	}

	/**�Ƿ�ѡ��,���ղؼ�������*/
	public boolean isSelect() {
		return isSelect;
	}

	/**�Ƿ�ѡ��,���ղؼ�������*/
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	/**�����ڷ������е�����<strong>ע�⣬�뱾�����ݿ�����Ͳ���ͬ</strong>*/
	public int getType() {
		return type;
	}

	/**�����ڷ������е�����<strong>ע�⣬�뱾�����ݿ�����Ͳ���ͬ</strong>*/
	public void setType(int type) {
		this.type = type;
	}
	
}

