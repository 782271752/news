package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.DiagnosisShare;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * �������model
 * @author liang
 */
public class DiagnosisShareModel {
	
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	
	public DiagnosisShareModel(){
		dataVisitors = new DataVisitors();
	}
	
	public DiagnosisShareModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * �������
	 * @param number ��Ʊ��
	 * @param mobile �ֺź���
	 * @param cb �ص� ,�ص���res�� null:ʧ��  true:�ɹ� false:ʧ��
	 * @param what
	 */
	public void diagnosisShare(String number,String mobile,CallBack cb,int what){
		DiagnosisShare diagnosisShare = new DiagnosisShare();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		StringBuffer sb = new StringBuffer();
		sb.append(number);
		sb.append(mobile);
		params.add(new BasicNameValuePair("Content",sb.toString().trim()));
		dataVisitors.doPost(diagnosisShare, params, cb, what);
	}
	
	
	
}
