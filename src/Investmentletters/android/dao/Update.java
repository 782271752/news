package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * °æ±¾¸üÐÂ
 * 
 * @author liang
 */
public class Update extends AbsOtherBase<String> {

	@Override
	public String doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doGet() {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_UPDATE);
		String result = getHttp().doGet(getUrl());
		if (result != null && result.startsWith("\ufeff")) {
			result = result.substring(1);
		}
		return result;
	}

}
