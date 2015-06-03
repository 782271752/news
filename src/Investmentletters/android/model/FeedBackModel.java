package Investmentletters.android.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.dao.FeedBack;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;

/**
 * 意见反馈
 * @author liang
 */
public class FeedBackModel {
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	
	public FeedBackModel(){
		dataVisitors = new DataVisitors();
	}
	
	public FeedBackModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	
	/**
	 * 返馈
	 * @param content 内容
	 * @param type 对应选项1(对应选项卡的内容，但不传ABCD.....)
	 * @param type1 对应选项2(对应选项卡的内容，但不传ABCD.....)
	 * @param type2  对应选项3(对应选项卡的内容，但不传ABCD.....)
	 * @param type3  对应选项4(对应选项卡的内容，但不传ABCD.....)
	 * @param type4  对应选项5(对应选项卡的内容，但不传ABCD.....)
	 * @param UserId 用户ID  (可以传也可以不传，没登陆就不传,负数不传)
	 * @param cb 回调 ,回调的res中 null:失败  true:成功 false:失败
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
