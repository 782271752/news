package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * ���̽�������б����ݿ�DAo
 * @author liang
 */
public class DBTraderExplain extends DBNewsListBase {

	public DBTraderExplain(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TRADER_EXPLAIN;
	}

}
