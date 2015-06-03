package Investmentletters.android.activity;

import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import Investmentletters.android.R;
import Investmentletters.android.utils.CNImageLoadThead;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.xinlang.CreatFriendships;
import Investmentletters.android.xinlang.GetWeiBoInformation;
import Investmentletters.android.xinlang.XinLangWeiBoListview;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Groups;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

@SuppressLint("ResourceAsColor")
public class Dj_WeiboActivity extends Activity implements WeiboAuthListener,
		View.OnClickListener {
	/** 主持人id */
	long index = 0;
	/** 当前用户id */
	long user_id = 0;
	Oauth2AccessToken token;
	/** 新浪回调 */
	private RequestListener sinaListester = null;

	/** 主持人新浪微博头像 */
	private ImageView ivweiboHead;
	/** 主持人新浪微博昵称 */
	private TextView tvweiboName;
	/** 主持人新浪微博说说 */
	private TextView tvweiboMood;
	/** 返回按钮 */
	private View mBackButton;
	/** 关注按钮 */
	private Button follow;
	/**是否正在关注此主持人*/
	private boolean followed_by;
	private ImageLoadHandler handler = null;
	LinearLayout page_list;
	XinLangWeiBoListview weibolist;
	int texttimes = 0;
	private String screen_name =null;
	ProgressDialog Dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SetListester();
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dj_weibo);
		ivweiboHead = (ImageView) findViewById(R.id.imageView2);
		tvweiboName = (TextView) findViewById(R.id.name);
		tvweiboMood = (TextView) findViewById(R.id.mood);
		follow = (Button) findViewById(R.id.id_btn_follow);
		follow.setOnClickListener(this);
		page_list = (LinearLayout) findViewById(R.id.id_weibolist);
		mBackButton = findViewById(R.id.id_news_show_back_btn);// 返回按钮
		index = getIntent().getExtras().getLong("INDEX", -1L);

		mBackButton.setOnClickListener(this);
		handler = new ImageLoadHandler();

		Weibo weibo = Weibo.getInstance(Constants.SINA_APP_KEY,
				Constants.SINA_REDIRECT_URL);
		weibo.authorize(Dj_WeiboActivity.this, this);// 认证
	}


	void SetListester() {
		sinaListester = new RequestListener() {
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(Dj_WeiboActivity.this, "失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(Dj_WeiboActivity.this, "失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onComplete(final String str) {
				// TODO Auto-generated method stub

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String account = str;
						try {
							JSONObject json_acc = new JSONObject(account);
							screen_name = json_acc.getString("screen_name");
							String description = json_acc
									.getString("description");
							String img_url = json_acc
									.getString("profile_image_url");
							tvweiboName.setText(screen_name);
							tvweiboMood.setText(description);
							new CNImageLoadThead(null,Dj_WeiboActivity.this,
									img_url, ivweiboHead, 0, handler,
									ImageLoadHandler.HANDLER_LOAD_IMG, index
											+ "_", null).start();
							weibolist = new XinLangWeiBoListview(
									Dj_WeiboActivity.this, token, index);
							page_list.addView(weibolist);
							Dialog.dismiss();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		};
	}

	public void writeFileData(String filename, String message) {
		try {
			FileOutputStream fout = openFileOutput(filename, MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	public void callback(String ret,int type){
		switch (type) {
		case 0:
			
			break;
		case 1:
			try {
				JSONObject json = new JSONObject(ret);
				JSONObject json_source = json.getJSONObject("source");
				followed_by = json_source.getBoolean("followed_by");
				if (followed_by) {
					follow.setText("已关注");
				} else {
					follow.setText("关注");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			followed_by = true;
			follow.setText("已关注");
			break;
		case 3:
			followed_by = false;
			follow.setText("关注");
			break;
		case 4:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Bundle arg0) {
		// TODO Auto-generated method stub
		String s_token = arg0.getString("access_token");
		String uid = arg0.getString("uid");
		user_id = Long.parseLong(uid);
		token = new Oauth2AccessToken(s_token, arg0.getString("expires_in"));
		if (token.isSessionValid()) {
			Dialog = new ProgressDialog(Dj_WeiboActivity.this);
			if (Dialog != null) {
				Dialog.setMessage("请稍候");
			}
			Dialog.show();
			Dialog.setCanceledOnTouchOutside(false);
			UsersAPI user = new UsersAPI(token);
			user.show(index, sinaListester);

			new GetWeiBoInformation(token,
					index, user_id, Dj_WeiboActivity.this, this);
		} else {
		}
	}

	@Override
	public void onError(WeiboDialogError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub

	}

	public String setid(String Str) {
		return Str;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_news_show_back_btn: // 返回
			finish();
			break;
		case R.id.id_btn_follow:
			if(followed_by){
				new GetWeiBoInformation(token,screen_name, Dj_WeiboActivity.this, this);
			}else{
				new GetWeiBoInformation(token,index,screen_name, Dj_WeiboActivity.this, this);
			}
			break;
		default:
			break;
		}
	}

}
