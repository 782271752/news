package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * ���տ�Ѷ������Ŀ�����б����ݿ�DAo
 * @author liang
 */
public class DBTodayByBrand extends DBNewsListBase {
	
	/**��Ŀid*/
	private int brandId = -1;

	public DBTodayByBrand(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TODAY;
	}
	
	
	/**
	 * ��ȡ���տ�Ѷ��Ŀid
	 * 
	 * @return -1:δ����
	 */
	@Override
	public int getBrandId() {
		// TODO Auto-generated method stub
		return brandId;
	}
	
	@Override
	public void setBrandId(int id) {
		// TODO Auto-generated method stub
		super.setBrandId(id);
		brandId = id;
	}

}
