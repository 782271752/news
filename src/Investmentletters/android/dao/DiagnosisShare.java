package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * �������DAO
 * @author liang
 */
public class DiagnosisShare extends AbsOtherBase<Boolean> {

	/**
	 * �������
	 * @param params post������Ҫ��<br/>
	 * <strong>Content�ֶΣ�</strong>ƴ�ӹ�Ʊ�����ֻ���
	 * 
	 * @return null:ʧ��  true:�ɹ� false:ʧ��
	 */
	@Override
	public Boolean doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_DIAGNOSIS_SHARE);
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
