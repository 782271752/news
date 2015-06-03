package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * �û�����
 * @author liang
 */
public class FeedBack extends AbsOtherBase<Boolean> {

	/**
	 * �û�����
	 * @param params post������Ҫ��<br/>
	 * <strong>Content�ֶΣ�</strong>����
	 * <strong>type�ֶΣ�</strong>��Ӧѡ��1(��Ӧѡ������ݣ�������ABCD.....)
	 * <strong>type1�ֶΣ�</strong>��Ӧѡ��1
	 * <strong>type2�ֶΣ�</strong>��Ӧѡ��2
	 * <strong>type3�ֶΣ�</strong>��Ӧѡ��3
	 * <strong>type4�ֶΣ�</strong>��Ӧѡ��4
	 * <strong>UserId�ֶΣ�</strong>�û�ID(���Դ�Ҳ���Բ�����û��½�Ͳ���)
	 * 
	 * @return null:ʧ��  true:�ɹ� false:ʧ��
	 */
	@Override
	public Boolean doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		setUrl(Constants.URL_FEED_BACK);
		String result = getHttp().doPost(getUrl(), params);
		try{
			int res = Integer.valueOf(result);
			return res == 1;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}

	@Override
	public Boolean doGet() {
		// TODO Auto-generated method stub
		return null;
	}

}
