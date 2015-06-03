package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.Image;

/**
 * 美图在线详细DAO类
 * @author liang
 */
public class ImageOnLineDetail extends AbsOtherBase<List<Image>> {

	
	
	@Override
	public List<Image> doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Image> doGet() {
		// TODO Auto-generated method stub
		String result = getHttp().doGet(getUrl());
		try{
			JSONArray arr = new JSONArray(result);
			int len = arr.length();
			List<Image> res = new ArrayList<Image>();
			Image image = null;
			JSONObject json = null;
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				
				image = new Image();
				image.setImg(json.getString("图片").trim());
				image.setTime(json.getString("时间").trim());
				image.setContent(json.getString("内容").trim());
				
				res.add(image);
			}
			
			return res;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	

}
