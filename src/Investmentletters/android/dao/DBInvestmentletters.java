package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * Ͷ�ʿ�Ѷ�����б����ݿ�DAo
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
