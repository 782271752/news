package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 时事聚焦新闻列表数据库DAo
 * @author liang
 */
public class DBFocusNews extends DBNewsListBase {

	public DBFocusNews(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_FOCUS;
	}


}
