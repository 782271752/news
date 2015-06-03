package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.Hot_News;
import Investmentletters.android.entity.Share;
import Investmentletters.android.utils.Constants;

/**
 * 热门诊股列表
 * @author liang
 */
public class HotShare extends AbsOtherBase<List<Share>> {

	@Override
	public List<Share> doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Share> doGet() {
		// TODO Auto-generated method stub
		String httpRes = getHttp().doGet(Constants.URL_GET_HOT_SHARE);
		System.out.println("http:热门诊股列表："+httpRes);
		List<Share> result = null;
		try{
			JSONArray arr = new JSONArray(httpRes.trim());
			int len = arr.length();
			JSONObject json = null;
			Share item = null;
			result = new ArrayList<Share>();
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				item = new Share();
				item.setNumber(json.getString("code".trim()));
				item.setName(json.getString("stock".trim()));
				item.setPe(json.getString("PE").trim());
				item.setMoney(json.getString("money").trim());
				
				result.add(item);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}

}
