package Investmentletters.android.dao;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.utils.Constants;

/**
 * 今日快讯，含栏目的dao类<br/>
 * <strong>注意，本类为含栏目名称的，每次更换申请的栏目访问数据前，执行一次setType(int type)方法</strong>
 * @author liang
 */
public class TodayByBrand extends NewsBase {
	
	/**栏目类型*/
	private int brandId = 0;

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return brandId;
	}
	
	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		super.setType(type);
		brandId = type;
	}

	@Override
	public String getDefaultUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_WITH_BRAND_NEWS;
	}

	@Override
	public String getMoreUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_WITH_BRAND_NEWS;
	}

	@Override
	public String getFreshUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_WITH_BRAND_FRESH_NEWS;
	}

	@Override
	public String getUpdateUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_MODIFY_NEWS;
	}
	
}
