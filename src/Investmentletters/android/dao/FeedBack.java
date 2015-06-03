package Investmentletters.android.dao;

import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.base.AbsOtherBase;
import Investmentletters.android.utils.Constants;

/**
 * 用户反馈
 * @author liang
 */
public class FeedBack extends AbsOtherBase<Boolean> {

	/**
	 * 用户反馈
	 * @param params post参数，要求<br/>
	 * <strong>Content字段：</strong>内容
	 * <strong>type字段：</strong>对应选项1(对应选项卡的内容，但不传ABCD.....)
	 * <strong>type1字段：</strong>对应选项1
	 * <strong>type2字段：</strong>对应选项2
	 * <strong>type3字段：</strong>对应选项3
	 * <strong>type4字段：</strong>对应选项4
	 * <strong>UserId字段：</strong>用户ID(可以传也可以不传，没登陆就不传)
	 * 
	 * @return null:失败  true:成功 false:失败
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
