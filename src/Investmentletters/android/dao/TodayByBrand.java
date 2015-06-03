package Investmentletters.android.dao;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.utils.Constants;

/**
 * ���տ�Ѷ������Ŀ��dao��<br/>
 * <strong>ע�⣬����Ϊ����Ŀ���Ƶģ�ÿ�θ����������Ŀ��������ǰ��ִ��һ��setType(int type)����</strong>
 * @author liang
 */
public class TodayByBrand extends NewsBase {
	
	/**��Ŀ����*/
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
