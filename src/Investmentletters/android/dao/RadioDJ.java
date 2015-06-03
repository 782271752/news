package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.DJ;
import Investmentletters.android.utils.Constants;

/**
 * µçÌ¨DJ DAO
 * @author liang
 */
public class RadioDJ extends AbsOtherBase<List<DJ>> {

	@Override
	public List<DJ> doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DJ> doGet() {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_GET_RADIO_DJS);
		String result = getHttp().doGet(getUrl());
		try{
			JSONArray arr = new JSONArray(result);
			int len = arr.length();
			List<DJ> res = new ArrayList<DJ>();
			DJ dj = null;
			JSONObject json = null;
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				
				dj = new DJ();
				dj.setId(json.getInt("id"));
				dj.setName(json.getString("name").trim());
				dj.setImg(json.getString("image").trim());
				dj.setWeiboId(json.getString("weiboid").trim());
				res.add(dj);
			}
			
			return res;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}

}
