package Investmentletters.android.dao.base;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.utils.Http;

/**
 * �����������б����ͷ��ʳ������
 * @author liang
 *
 * @param <T>
 */
public abstract class AbsOtherBase<T> {

	/**url��ַ*/
	private String url = null;
	/**������*/
	private Http http = null;
	
	public AbsOtherBase(){
		http = new Http();
	}
	
	/**
	 * ִ��Http post
	 * @param params post����
	 * @return null:ʧ��
	 */
	public abstract T doPost(List<NameValuePair> params);
	
	/**
	 * ִ��Http get
	 * @return null:ʧ��
	 */
	public abstract T doGet();
	
	/**
	 * ��ȡurl
	 * @return
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * ����url
	 * @param urlFormat ��ʽ
	 * @param args ����
	 */
	public void setUrl(String urlFormat,Object ...args){
		url = String.format(urlFormat, args);
	}
	
	/**
	 * ��ȡhttp
	 * @return
	 */
	public Http getHttp(){
		return http;
	}
	
}
