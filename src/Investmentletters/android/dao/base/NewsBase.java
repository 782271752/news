package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.TodayByBrand;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Http;

/**
 * �����б����
 * @author liang
 */
public abstract class NewsBase extends NewsListBase<News> {
	
	/**������*/
	private Http http = null;
	
	public NewsBase(){
		http = new Http();
	}

	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		System.out.println("����.......1111");
		return json2List(getHttp().doGet(getUrl(getDefaultUrlFormat(), getType(),0)));
	}

	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return json2List(getHttp().doGet(getUrl(getMoreUrlFormat(), getType(),minId)));
	}

	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		System.out.println("����url....333"+ getUrl(getFreshUrlFormat(), getType(),maxId));
		return json2List(getHttp().doGet(getUrl(getFreshUrlFormat(), getType(),maxId)));
	}

	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		System.out.println("����url....4444"+ getUrl(getUpdateUrlFormat(),lastTime ,getType()));
		return json2List(getHttp().doGet(getUrl(getUpdateUrlFormat(),lastTime ,getType())));
	}
	
	/**
	 * ת��jsonΪlist
	 * @param jsonStr json��
	 * 
	 * @return
	 */
	public List<News> json2List(String jsonStr){
		List<News> result = null;
		try{
			JSONArray arr = new JSONArray(jsonStr);
			int len = arr.length();
			News item = null;
			JSONObject json = null;
			result = new ArrayList<News>();
			int type = getType();
			
			if(this instanceof TodayByBrand){//���տ�Ѷ����
				type = TYPE_TODAY_NO_BRAND;
			}
			
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				item = new News();
				item.setId(json.getInt("��ʶ"));
				item.setTitle(json.getString("����").trim());
				item.setSummary(json.getString("����").trim());
				item.setTime(json.getString("ʱ��").trim());
				item.setMinImg(json.getString("����ͼ").trim());
				item.setBigImg(json.getString("��ͼ").trim());
				item.setContent(json.getString("ȫ������").trim());
				item.setTop(json.getInt("�ö�"));
				item.setType(type);
				
				result.add(item);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(result != null){
				result.clear();				
			}
		}
		
		return result;
	}
	
	/**
	 * ��ȡ�ӿڵ�ַ
	 * @param format ��ʽ
	 * @param args ����
	 * @return
	 */
	public String getUrl(String format,Object ...args){
		return String.format(format, args);
	}
	
	/**��ȡ�ӿڸ�ʽ��Ĭ���б�*/
	public abstract String getDefaultUrlFormat();
	
	/**��ȡ�ӿڸ�ʽ�������б�*/
	public abstract String getMoreUrlFormat();
	
	/**��ȡ�ӿڸ�ʽ��ˢ���б�*/
	public abstract String getFreshUrlFormat();
	
	/**��ȡ�ӿڸ�ʽ���޸��б�*/
	public abstract String getUpdateUrlFormat();
	
	/**��ȡHttp������ʵ��*/
	public Http getHttp(){
		return http;
	}

}
