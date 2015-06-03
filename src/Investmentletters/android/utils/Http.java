package Investmentletters.android.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 网络类
 * @author liang
 */
public class Http {
	/**
	 * post网络请求
	 * @param url 网络地址
	 * @param params post参数
	 * @return
	 */
	public String doPost(String url , List<NameValuePair> params){
		
		//返回数据
		String result = null;
		
		HttpPost post = new HttpPost(url);
		try {
			if(params != null){
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}		
		HttpResponse response = null;
		HttpClient client = new DefaultHttpClient();
		try {
			response = client.execute(post);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result =  EntityUtils.toString(response.getEntity());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 非文件流类数据申请
	 * @param url
	 * @return
	 */
	public String doGet(String url){
		HttpGet get = new HttpGet(url);
		HttpResponse response = null;
		
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			
			HttpClient client = new DefaultHttpClient(httpParams);
			response = client.execute(get);
		} catch (Exception e){
			e.printStackTrace();
		}

		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 从指定url获取输入流，调用后，记得使用close()。
	 * @param url
	 * @return
	 */
	public InputStream getInputStream(String url){
		InputStream is = null;
		
		try {
			URL hUrl = new URL(url);
			HttpURLConnection http = (HttpURLConnection)hUrl.openConnection();
			is = http.getInputStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return is;
	}
}
