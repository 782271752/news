package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.Login;
import Investmentletters.android.dao.Register;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * 用户Model类
 * @author liang
 */
public class UserModel {
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	
	public UserModel(){
		dataVisitors = new DataVisitors();
	}
	
	public UserModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * 用户注册 
	 * @param user 用户名
	 * @param cb 回调,object中，null:失败  Register.OK:成功  Register.FAILURE:失败  Register.REPEAT
	 * @param what 回调的what 
	 */
	public void register(String user,CallBack cb,int what){
		Register register = new Register();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User",user));
		dataVisitors.doPost(register, params, cb, what);
	}
	
	/**
	 * 用户登陆
	 * @param user 用户名
	 * @param passwd 密码
	 * @param cb 回调,object中，null:失败  true:成功 false:失败
	 * @param what 回调的what 
	 */
	public void login(String user,String passwd,CallBack cb,int what){
		Login login = new Login();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User",user));
		params.add(new BasicNameValuePair("Pwd",passwd));
		dataVisitors.doPost(login, params, cb, what);
	}
	
	
	
	
	

}
