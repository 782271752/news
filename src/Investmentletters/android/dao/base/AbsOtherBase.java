package Investmentletters.android.dao.base;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.utils.Http;

/**
 * 其他非新闻列表类型访问抽像基类
 * @author liang
 *
 * @param <T>
 */
public abstract class AbsOtherBase<T> {

	/**url地址*/
	private String url = null;
	/**网络类*/
	private Http http = null;
	
	public AbsOtherBase(){
		http = new Http();
	}
	
	/**
	 * 执行Http post
	 * @param params post参数
	 * @return null:失败
	 */
	public abstract T doPost(List<NameValuePair> params);
	
	/**
	 * 执行Http get
	 * @return null:失败
	 */
	public abstract T doGet();
	
	/**
	 * 获取url
	 * @return
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * 设置url
	 * @param urlFormat 格式
	 * @param args 参数
	 */
	public void setUrl(String urlFormat,Object ...args){
		url = String.format(urlFormat, args);
	}
	
	/**
	 * 获取http
	 * @return
	 */
	public Http getHttp(){
		return http;
	}
	
}
