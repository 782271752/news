package Investmentletters.android.entity;

/**
 * ����Entiry��
 * @author liang
 */
public class BaseEntity {
	
	/**��Ŀid*/
	private int id = 0;
	/**��Ŀ����*/
	private String name = null;
	/**�Ƿ�ѡ��*/
	private boolean isSelected = false;
	
	/**��Ŀid*/
	public int getId() {
		return id;
	}

	/**��Ŀid*/
	public void setId(int id) {
		this.id = id;
	}

	/**��Ŀ����*/
	public String getName() {
		return name;
	}

	/**��Ŀ����*/
	public void setName(String name) {
		this.name = name;
	}

	/**�Ƿ�ѡ��*/
	public boolean isSelected() {
		return isSelected;
	}

	/**�Ƿ�ѡ��*/
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
}
