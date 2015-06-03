package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.Register;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * 用户Model类
 * @author liang
 */
public class AccountModel {
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	
	public AccountModel(){
		dataVisitors = new DataVisitors();
	}
	
	public AccountModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * 用户注册 
	 * @param user 用户名
	 * @param cb 回调,object中，null:失败  Register.OK:成功  Register.FAILURE:失败  Register.REPEAT
	 * @param what 回调的what 
	 */
	public void changeaccoumt(String User,String Password,String Phone,int url,CallBack cb,int what){
		Register register = new Register();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User",User));
		params.add(new BasicNameValuePair("pwd",Password));
		params.add(new BasicNameValuePair("phone",Phone));
	//	params.add(new BasicNameValuePair("usersImg",url));
		dataVisitors.doPost(register, params, cb, what);
	}
}
