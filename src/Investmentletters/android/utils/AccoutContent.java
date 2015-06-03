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
 * ���ݷ�����
 * 
 * @author liang
 */
public class AccoutContent implements WeiboAuthListener {

	private Activity activity = null;
	public String information;
	LinearLayout Information;

	/** ���˷���ص� */
	private RequestListener sinaListester = null;

	public AccoutContent(Activity activity) {
		this.activity = activity;
		Information = (LinearLayout)this.activity.findViewById(R.id.weibo_account);
		sinaListester = new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ�io�쳣" + e.toString());
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AccoutContent.this.activity, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��쳣" + e.toString());
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AccoutContent.this.activity, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onComplete(String str) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��ɹ�" + str);
				information = str;
				AccoutContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						// TODO Auto-generated method stub
						// Toast.makeText(AccoutContent.this.activity, "����ɹ�",
						// Toast.LENGTH_SHORT).show();
					}
				});
			}
		};

	}

	// /**
	// * �����û�ID��ȡ�û���Ϣ
	// *
	// * @param uid
	// * ��Ҫ��ѯ���û�ID��
	// * @param listener
	// */
	// public void show(long uid, RequestListener listener) {
	// WeiboParameters params = new WeiboParameters();
	// params.add("uid", uid);
	// request("https://api.weibo.com/2/users/show.json", params, "GET",
	// listener);
	// }
	/**
	 * �ύ������΢��,
	 * 
	 * @param i
	 *            ����ѯ�û�ID
	 */
	private long dj_weiboid = 0;

	public void LoginToSinaWeibo(long i) {

		Weibo weibo = Weibo.getInstance(Constants.SINA_APP_KEY,
				Constants.SINA_REDIRECT_URL);

		dj_weiboid = i;
		weibo.authorize(activity, this);// ��֤

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		System.out.println("���ˣ�ȡ����֤");
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "����ʧ��",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onComplete(Bundle data) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ���֤�ɹ�" + data);
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
			// sb.append("[����Ͷ�ʿ챨�ֻ��ͻ���]");
			// System.out.println("���˿�ʼ����...");
			// weiboApi.update(sb.toString(), "90.00", "90.00", sinaListester);
			// //����

		} else {
			System.out.println("������֤ʧ��...");
		}
	}

	@Override
	public void onError(WeiboDialogError err) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ�����" + err.getMessage());
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "����ʧ��",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ��쳣");
		e.printStackTrace();
		AccoutContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(AccoutContent.this.activity, "����ʧ��",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
