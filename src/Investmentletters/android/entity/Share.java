package Investmentletters.android.entity;

/**
 * ��Ʊʵ����
 * @author liang
 */
public class Share {
	
	/**��Ʊ��*/
	private String number = null;
	
	/**��Ʊ����*/
	private String name = null;
	
	/**��̬PE*/
	private String pe = null;
	
	/**����ÿ������*/
	private String money = null;
	
	/**��������*/
	private int area = 0;
	
	/**��Ʊ������Ϣ*/
	private String content = null;

	
	
	/**��Ʊ��*/
	public String getNumber() {
		return number;
	}

	/**��Ʊ��*/
	public void setNumber(String number) {
		this.number = number;
	}

	/**��Ʊ����*/
	public String getName() {
		return name;
	}

	/**��Ʊ����*/
	public void setName(String name) {
		this.name = name;
	}

	/**��̬PE*/
	public String getPe() {
		return pe;
	}

	/**��̬PE*/
	public void setPe(String pe) {
		this.pe = pe;
	}

	/**����ÿ������*/
	public String getMoney() {
		return money;
	}

	/**����ÿ������*/
	public void setMoney(String money) {
		this.money = money;
	}

	/**��������*/
	public int getArea() {
		return area;
	}

	/**��������*/
	public void setArea(int area) {
		this.area = area;
	}

	/**��Ʊ������Ϣ*/
	public String getContent() {
		return content;
	}

	/**��Ʊ������Ϣ*/
	public void setContent(String content) {
		this.content = content;
	}
	
}
