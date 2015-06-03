package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.DiagnosisShare;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * 互动诊股model
 * @author liang
 */
public class DiagnosisShareModel {
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	
	public DiagnosisShareModel(){
		dataVisitors = new DataVisitors();
	}
	
	public DiagnosisShareModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * 互动诊股
	 * @param number 股票号
	 * @param mobile 手号号码
	 * @param cb 回调 ,回调的res中 null:失败  true:成功 false:失败
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
