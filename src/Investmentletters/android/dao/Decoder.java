package Investmentletters.android.dao;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.utils.Constants;

/**
 * 数据解码dao类
 * @author liang
 */
public class Decoder extends NewsBase {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_DECODER;
	}

	@Override
	public String getDefaultUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_NEWS;
	}

	@Override
	public String getMoreUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_NEWS;
	}

	@Override
	public String getFreshUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_FRESH_NEWS;
	}

	@Override
	public String getUpdateUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_MODIFY_NEWS;
	}
	
}
