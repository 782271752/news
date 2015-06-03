package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * 数据解码新闻列表数据库DAo
 * @author liang
 */
public class DBDecode extends DBNewsListBase {

	public DBDecode(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_DECODER;
	}

}
