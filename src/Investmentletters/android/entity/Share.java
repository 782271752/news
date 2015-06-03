package Investmentletters.android.entity;

/**
 * 股票实体类
 * @author liang
 */
public class Share {
	
	/**股票号*/
	private String number = null;
	
	/**股票名称*/
	private String name = null;
	
	/**动态PE*/
	private String pe = null;
	
	/**上年每股收益*/
	private String money = null;
	
	/**所处区域*/
	private int area = 0;
	
	/**股票基本信息*/
	private String content = null;

	
	
	/**股票号*/
	public String getNumber() {
		return number;
	}

	/**股票号*/
	public void setNumber(String number) {
		this.number = number;
	}

	/**股票名称*/
	public String getName() {
		return name;
	}

	/**股票名称*/
	public void setName(String name) {
		this.name = name;
	}

	/**动态PE*/
	public String getPe() {
		return pe;
	}

	/**动态PE*/
	public void setPe(String pe) {
		this.pe = pe;
	}

	/**上年每股收益*/
	public String getMoney() {
		return money;
	}

	/**上年每股收益*/
	public void setMoney(String money) {
		this.money = money;
	}

	/**所处区域*/
	public int getArea() {
		return area;
	}

	/**所处区域*/
	public void setArea(int area) {
		this.area = area;
	}

	/**股票基本信息*/
	public String getContent() {
		return content;
	}

	/**股票基本信息*/
	public void setContent(String content) {
		this.content = content;
	}
	
}
