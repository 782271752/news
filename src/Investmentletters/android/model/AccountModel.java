package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.Register;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * �û�Model��
 * @author liang
 */
public class AccountModel {
	
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	
	public AccountModel(){
		dataVisitors = new DataVisitors();
	}
	
	public AccountModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * �û�ע�� 
	 * @param user �û���
	 * @param cb �ص�,object�У�null:ʧ��  Register.OK:�ɹ�  Register.FAILURE:ʧ��  Register.REPEAT
	 * @param what �ص���what 
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
