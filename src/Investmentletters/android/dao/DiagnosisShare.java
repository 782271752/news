package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * 互动诊股DAO
 * @author liang
 */
public class DiagnosisShare extends AbsOtherBase<Boolean> {

	/**
	 * 互动诊股
	 * @param params post参数，要求<br/>
	 * <strong>Content字段：</strong>拼接股票号与手机号
	 * 
	 * @return null:失败  true:成功 false:失败
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
