package Investmentletters.android.utils;

import java.io.IOException;

import Investmentletters.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 内容分享类
 * 
 * @author liang
 */
public class AccoutContent implements WeiboAuthListener {

	private Activity activity = null;
	public String information;
	LinearLayout Information;

	/** 新浪分享回调 */
	private RequestListener sinaListester = null;

	public AccoutContent(Activity activity) {
		this.activity = activity;
		Information = (LinearLayout)this.activity.findViewById(R.id.weibo_account);
		sinaListester = new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：io异常" + e.toString());
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AccoutContent.this.activity, "分享失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：异常" + e.toString());
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AccoutContent.this.activity, "分享失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onComplete(String str) {
				// TODO Auto-generated method stub
				System.out.println("新浪：成功" + str);
				information = str;
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						// TODO Auto-generated method stub
						// Toast.makeText(AccoutContent.this.activity, "分享成功",
						// Toast.LENGTH_SHORT).show();
					}
				});
			}
		};

	}

	// /**
	// * 根据用户ID获取用户信息
	// *
	// * @param uid
	// * 需要查询的用户ID。
	// * @param listener
	// */
	// public void show(long uid, RequestListener listener) {
	// WeiboParameters params = new WeiboParameters();
	// params.add("uid", uid);
	// request("https://api.weibo.com/2/users/show.json", params, "GET",
	// listener);
	// }
	/**
	 * 提交到新浪微博,
	 * 
	 * @param i
	 *            所查询用户ID
	 */
	private long dj_weiboid = 0;

	public void LoginToSinaWeibo(long i) {

		Weibo weibo = Weibo.getInstance(Constants.SINA_APP_KEY,
				Constants.SINA_REDIRECT_URL);

		dj_weiboid = i;
		weibo.authorize(activity, this);// 认证

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		System.out.println("新浪：取消认证");
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "分享失败",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onComplete(Bundle data) {
		// TODO Auto-generated method stub
		System.out.println("新浪：认证成功" + data);
		Oauth2AccessToken token = new Oauth2AccessToken(
				data.getString("access_token"), data.getString("expires_in"));

		if (token.isSessionValid()) {
			UsersAPI user = new UsersAPI(token);
			user.show(dj_weiboid, sinaListester);

			// StatusesAPI weiboApi = new StatusesAPI(token);
			// StringBuffer sb = new StringBuffer();
			// if(sinaShareContent.length()>100){
			// sb.append(sinaShareContent.substring(0, 100));
			// }else{
			// sb.append(sinaShareContent);
			// }
			// sb.append("[来自投资快报手机客户端]");
			// System.out.println("新浪开始分享...");
			// weiboApi.update(sb.toString(), "90.00", "90.00", sinaListester);
			// //分享

		} else {
			System.out.println("新浪认证失败...");
		}
	}

	@Override
	public void onError(WeiboDialogError err) {
		// TODO Auto-generated method stub
		System.out.println("新浪：出错" + err.getMessage());
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "分享失败",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		System.out.println("新浪：异常");
		e.printStackTrace();
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "分享失败",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
