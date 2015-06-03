package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * 用户登陆
 * @author liang
 */
public class Login extends AbsOtherBase<Boolean> {

	/**
	 * 用户登陆
	 * @param params post参数，要求<br/>
	 * <strong>User字段：</strong>用户名
	 * <string>Pwd字段：</string>密码
	 * 
	 * @return null:失败  true:成功 false:失败
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
