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
public class HotNews extends AbsOtherBase<List<Hot_News>> {

	@Override
	public List<Hot_News> doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Hot_News> doGet() {
		// TODO Auto-generated method stub
		String httpRes = getHttp().doGet(Constants.URL_HOTNEWS_SRARCH);
		System.out.println("http:热门新闻列表："+httpRes);
		List<Hot_News> result = null;
		try{
			JSONObject res = new JSONObject(httpRes.trim());
			String Result = res.getString("搜索结果");
			JSONArray arr = new JSONArray(Result.trim());
			int len = arr.length();
			JSONObject json = null;
			Hot_News item = null;
			result = new ArrayList<Hot_News>();
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				item = new Hot_News();
				item.settitle(json.getString("Nci_Title"));
				item.seturl(json.getString("Nci_Url"));
				item.settime(json.getString("Nci_Time"));
//				
//				item.setNumber(json.getString("code".trim()));
//				item.setName(json.getString("stock".trim()));
//				item.setPe(json.getString("PE").trim());
//				item.setMoney(json.getString("money").trim());
				
				result.add(item);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}

}
