package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * �û�ע��
 * @author liang
 */
public class Register extends AbsOtherBase<Integer> {
	
	/**ע��ʧ��*/
	public static final int FAILURE = 0x00;
	/**ע��ɹ�*/
	public static final int OK = 0x01;
	/**�ظ�ע��*/
	public static final int REPEAT = 0x02;

	/**
	 * ִ���û�ע��
	 * @param params post������Ҫ��<br/>
	 * <strong>User�ֶΣ�</strong>�û���
	 * 
	 * @return null:ʧ��  ����������Register.FAILURE  Register.OK  Register.REPEAT
	 */
	@Override
	public Integer doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_REGISTER);
		
		String result = getHttp().doPost(getUrl(), params);
		try{
			int res = Integer.valueOf(result);
			return res;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Integer doGet() {
		// TODO Auto-generated method stub
		return null;
	}

}
