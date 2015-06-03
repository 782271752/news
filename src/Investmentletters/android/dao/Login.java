package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * �û���½
 * @author liang
 */
public class Login extends AbsOtherBase<Boolean> {

	/**
	 * �û���½
	 * @param params post������Ҫ��<br/>
	 * <strong>User�ֶΣ�</strong>�û���
	 * <string>Pwd�ֶΣ�</string>����
	 * 
	 * @return null:ʧ��  true:�ɹ� false:ʧ��
	 */
	@Override
	public Boolean doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_LOGIN);
		String result = getHttp().doPost(getUrl(), params);
		try{
			int res = Integer.valueOf(result);
			return res == 1;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Boolean doGet() {
		// TODO Auto-generated method stub
		return null;
	}

}
