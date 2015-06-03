package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.dao.TodayByBrand;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Http;

/**
 * 新闻列表基类
 * @author liang
 */
public abstract class NewsBase extends NewsListBase<News> {
	
	/**网络类*/
	private Http http = null;
	
	public NewsBase(){
		http = new Http();
	}

	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		System.out.println("下拉.......1111");
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
		System.out.println("下拉url....333"+ getUrl(getFreshUrlFormat(), getType(),maxId));
		return json2List(getHttp().doGet(getUrl(getFreshUrlFormat(), getType(),maxId)));
	}

	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		System.out.println("下拉url....4444"+ getUrl(getUpdateUrlFormat(),lastTime ,getType()));
		return json2List(getHttp().doGet(getUrl(getUpdateUrlFormat(),lastTime ,getType())));
	}
	
	/**
	 * 转换json为list
	 * @param jsonStr json串
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
			
			if(this instanceof TodayByBrand){//今日快讯类型
				type = TYPE_TODAY_NO_BRAND;
			}
			
			for(int i=0 ; i<len ; i++){
				json = arr.getJSONObject(i);
				item = new News();
				item.setId(json.getInt("标识"));
				item.setTitle(json.getString("标题").trim());
				item.setSummary(json.getString("内容").trim());
				item.setTime(json.getString("时间").trim());
				item.setMinImg(json.getString("缩略图").trim());
				item.setBigImg(json.getString("大图").trim());
				item.setContent(json.getString("全部内容").trim());
				item.setTop(json.getInt("置顶"));
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
	 * 获取接口地址
	 * @param format 格式
	 * @param args 参数
	 * @return
	 */
	public String getUrl(String format,Object ...args){
		return String.format(format, args);
	}
	
	/**获取接口格式：默认列表*/
	public abstract String getDefaultUrlFormat();
	
	/**获取接口格式：更多列表*/
	public abstract String getMoreUrlFormat();
	
	/**获取接口格式：刷新列表*/
	public abstract String getFreshUrlFormat();
	
	/**获取接口格式：修改列表*/
	public abstract String getUpdateUrlFormat();
	
	/**获取Http工具类实例*/
	public Http getHttp(){
		return http;
	}

}
