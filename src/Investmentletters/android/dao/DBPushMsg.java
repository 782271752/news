package Investmentletters.android.dao;

import Investmentletters.android.dao.base.DBNewsListBase;
import android.content.Context;

/**
 * ��Ϣ���������б����ݿ�DAo
 * @author liang
 */
public class DBPushMsg extends DBNewsListBase {

	public DBPushMsg(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_PUSH_MSG;
	}


}
