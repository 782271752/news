package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * 提交评论
 * @author Administrator
 *
 */
public class SubmitDiscuss extends AbsOtherBase<Boolean> {

	
	/**
	 * 提交评论
	 * @param params post参数：NewId,ComContent,UserId
	 * @return
	 */
	@Override
	public Boolean doPost(List<NameValuePair> params) {
		// TODO Auto-generated method stub
		String res = getHttp().doPost(Constants.URL_SUBMIT_COMMENT, params);
		try{
			return res.trim().equals("1");
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean doGet() {
		// TODO Auto-generated method stub
		return false;
	}

}
