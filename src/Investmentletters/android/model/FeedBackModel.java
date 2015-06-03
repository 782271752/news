package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.FeedBack;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * �������
 * @author liang
 */
public class FeedBackModel {
	
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	
	public FeedBackModel(){
		dataVisitors = new DataVisitors();
	}
	
	public FeedBackModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	
	/**
	 * ����
	 * @param content ����
	 * @param type ��Ӧѡ��1(��Ӧѡ������ݣ�������ABCD.....)
	 * @param type1 ��Ӧѡ��2(��Ӧѡ������ݣ�������ABCD.....)
	 * @param type2  ��Ӧѡ��3(��Ӧѡ������ݣ�������ABCD.....)
	 * @param type3  ��Ӧѡ��4(��Ӧѡ������ݣ�������ABCD.....)
	 * @param type4  ��Ӧѡ��5(��Ӧѡ������ݣ�������ABCD.....)
	 * @param UserId �û�ID  (���Դ�Ҳ���Բ�����û��½�Ͳ���,��������)
	 * @param cb �ص� ,�ص���res�� null:ʧ��  true:�ɹ� false:ʧ��
	 * @param what
	 */
	public void feedBack(String content,String type,
						String type1,String type2,
						String type3,String type4,
						String UserId,
						CallBack cb,int what){
		FeedBack feedBack = new FeedBack();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Content", content));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("type1", type1));
		params.add(new BasicNameValuePair("type2", type2));
		params.add(new BasicNameValuePair("type3", type3));
		params.add(new BasicNameValuePair("type4", type4));
		if(UserId != null){
			params.add(new BasicNameValuePair("UserId", String.valueOf(UserId)));	
		}
		
		dataVisitors.doPost(feedBack, params, cb, what);
	}
	
}
