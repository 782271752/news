package Investmentletters.android.xinlang;

import java.io.IOException;
import java.security.PublicKey;

import Investmentletters.android.activity.Dj_WeiboActivity;
import android.app.Activity;
import android.content.Context;
import android.widget.Button;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

public class CreatFriendships {
	public String result = null;
	/** ���˻ص� */
	private RequestListener sinaListester = null;
	
	private Dj_WeiboActivity activity = null;

	@SuppressWarnings("deprecation")
	public CreatFriendships(Oauth2AccessToken token, String screen_name,
			Context context, Dj_WeiboActivity activity, Button follow) {

		this.activity = activity;
		setlistener(activity, context, follow);
		FriendshipsAPI Friendships = new FriendshipsAPI(token);
		Friendships.create(screen_name, sinaListester);
	}

	void setlistener(final Activity activity, final Context context,
			Button follow) {
		sinaListester = new RequestListener() {

			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ�io�쳣" + e.toString());
				return;
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��쳣" + e.toString());
				return;
			}

			@Override
			public void onComplete(String str) {
				// TODO Auto-generated method stub
				result = str;
			}
		};
		if(result != null){
			follow.setText("�ѹ�ע");
		}
	}
}
