package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.Share;

/**
 * 互动诊股详细dao
 * @author liang
 */
public class ShareDetailDao extends AbsOtherBase<Share> {

	@Override
	public Share doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Share doGet() {
		// TODO Auto-generated method stub
		Share result = null;
		String httpRes = getHttp().doGet(getUrl());
		try{
			JSONObject json = new JSONObject(httpRes);
			result = new Share();
			
			result.setName(json.getString("stock").trim());//名称(代码)
			result.setContent(json.getString("content"));
			result.setArea(Integer.valueOf(json.getString("area").trim()));
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = null;
		}
		
		return result;
	}

}
