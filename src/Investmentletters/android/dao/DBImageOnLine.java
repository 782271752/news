package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 美图在线新闻列表数据库DAo
 * @author liang
 */
public class DBImageOnLine extends DBNewsListBase {

	public DBImageOnLine(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_IMAGES_ON_LINE;
	}


}
