package Investmentletters.android.entity;

/**
 * 基本Entiry类
 * @author liang
 */
public class BaseEntity {
	
	/**栏目id*/
	private int id = 0;
	/**栏目名称*/
	private String name = null;
	/**是否选中*/
	private boolean isSelected = false;
	
	/**栏目id*/
	public int getId() {
		return id;
	}

	/**栏目id*/
	public void setId(int id) {
		this.id = id;
	}

	/**栏目名称*/
	public String getName() {
		return name;
	}

	/**栏目名称*/
	public void setName(String name) {
		this.name = name;
	}

	/**是否选中*/
	public boolean isSelected() {
		return isSelected;
	}

	/**是否选中*/
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
