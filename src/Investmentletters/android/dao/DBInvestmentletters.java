package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 投资快讯新闻列表数据库DAo
 * @author liang
 */
public class DBInvestmentletters extends DBNewsListBase {

	public DBInvestmentletters(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_INVESTMENTLETTERS;
	}


}
