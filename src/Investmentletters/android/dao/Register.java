package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * 用户注册
 * @author liang
 */
public class Register extends AbsOtherBase<Integer> {
	
	/**注册失败*/
	public static final int FAILURE = 0x00;
	/**注册成功*/
	public static final int OK = 0x01;
	/**重复注册*/
	public static final int REPEAT = 0x02;

	/**
	 * 执行用户注册
	 * @param params post参数，要求<br/>
	 * <strong>User字段：</strong>用户名
	 * 
	 * @return null:失败  其他，见：Register.FAILURE  Register.OK  Register.REPEAT
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
