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
 * �û�Model��
 * @author liang
 */
public class UserModel {
	
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	
	public UserModel(){
		dataVisitors = new DataVisitors();
	}
	
	public UserModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * �û�ע�� 
	 * @param user �û���
	 * @param cb �ص�,object�У�null:ʧ��  Register.OK:�ɹ�  Register.FAILURE:ʧ��  Register.REPEAT
	 * @param what �ص���what 
	 */
	public void register(String user,CallBack cb,int what){
		Register register = new Register();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User",user));
		dataVisitors.doPost(register, params, cb, what);
	}
	
	/**
	 * �û���½
	 * @param user �û���
	 * @param passwd ����
	 * @param cb �ص�,object�У�null:ʧ��  true:�ɹ� false:ʧ��
	 * @param what �ص���what 
	 */
	public void login(String user,String passwd,CallBack cb,int what){
		Login login = new Login();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("User",user));
		params.add(new BasicNameValuePair("Pwd",passwd));
		dataVisitors.doPost(login, params, cb, what);
	}
	
	
	
	
	

}
