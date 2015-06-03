package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 公司雷达新闻列表数据库DAo
 * @author liang
 */
public class DBRadar extends DBNewsListBase {

	public DBRadar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_FADAR;
	}


}
