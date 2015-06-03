package Investmentletters.android.xinlang;

import java.io.IOException;


import Investmentletters.android.activity.Dj_WeiboActivity;
import android.content.Context;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

public class GetWeiBoInformation {
	public String result;
	/** ���˻ص� */
	private RequestListener sinaListester = null;

	private Dj_WeiboActivity activity = null;
	/**�ص���������*/
	private int re_type = 0;

	/**
	 * ��ȡ˫�������ע��Ϣ
	 * */
	public GetWeiBoInformation(Oauth2AccessToken token, long targetid,
			long sourceid, Context context, Dj_WeiboActivity activity) {
		re_type =1;
		this.activity = activity;
		setlistener(context);
		FriendshipsAPI Friendships = new FriendshipsAPI(token);
		Friendships.show(targetid, sourceid, sinaListester);
	}

	/** ��עĳ�û� */
	public GetWeiBoInformation(Oauth2AccessToken token, long targetid,
			String screen_name, Context context, Dj_WeiboActivity activity) {
		re_type =2;
		this.activity = activity;
		setlistener(context);
		FriendshipsAPI Friendships = new FriendshipsAPI(token);
		Friendships.create(targetid, screen_name, sinaListester);
	}
	/** ȡ����עĳ�û� */
	@SuppressWarnings("deprecation")
	public GetWeiBoInformation(Oauth2AccessToken token,
			String screen_name, Context context, Dj_WeiboActivity activity) {
		re_type =3;
		this.activity = activity;
		setlistener(context);
		FriendshipsAPI Friendships = new FriendshipsAPI(token);
		Friendships.destroy(screen_name, sinaListester);
	}
	void setlistener(Context context) {
		sinaListester = new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ�io�쳣" + e.toString());
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(activity, "��ע��Ϣ��ȡ�쳣", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��쳣" + e.toString());
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(activity, "��ע��Ϣ��ȡ�쳣", Toast.LENGTH_SHORT)
								.show();
					}
				});
			}

			@Override
			public void onComplete(final String str) {
				// TODO Auto-generated method stub
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						activity.callback(str,re_type);
						// TODO Auto-generated method stub
					}
				});
			}
		};
	}
}
