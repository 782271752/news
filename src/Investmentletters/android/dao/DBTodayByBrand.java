package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 今日快讯含有栏目新闻列表数据库DAo
 * @author liang
 */
public class DBTodayByBrand extends DBNewsListBase {
	
	/**栏目id*/
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
	 * 获取今日快讯栏目id
	 * 
	 * @return -1:未设置
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
