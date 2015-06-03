package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.entity.Image;

/**
 * ��ͼ������ϸDAO��
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
				image.setImg(json.getString("ͼƬ").trim());
				image.setTime(json.getString("ʱ��").trim());
				image.setContent(json.getString("����").trim());
				
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
