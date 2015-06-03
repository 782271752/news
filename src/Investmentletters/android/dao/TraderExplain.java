package Investmentletters.android.dao;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.utils.Constants;

/**
 * ≤Ÿ≈ÃΩ‚∂¡dao¿‡
 * @author liang
 */
public class TraderExplain extends NewsBase {

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TRADER_EXPLAIN;
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

