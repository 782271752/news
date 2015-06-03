package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.BaseEntity;
import Investmentletters.android.utils.Constants;

/**
 * ��̨��Ŀ�б�dao
 * @author liang
 */
public class RadioList extends AbsOtherBase<List<BaseEntity>> {

	@Override
	public List<BaseEntity> doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BaseEntity> doGet() {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_GET_RADIO_LIST);
		String result = getHttp().doGet(getUrl());
		try{
			JSONArray arr = new JSONArray(result);
			int len = arr.length();
			List<BaseEntity> res = new ArrayList<BaseEntity>();
			BaseEntity item = null;
			JSONObject json = null;
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				
				item = new BaseEntity();
				item.setId(json.getInt("id"));
				item.setName(json.getString("item").trim());
				
				res.add(item);
			}
			
			return res;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
