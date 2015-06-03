package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 今日快讯没栏目新闻列表数据库DAo
 * @author liang
 */
public class DBToday extends DBNewsListBase {

	public DBToday(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TODAY;
	}


}
