package Investmentletters.android.xinlang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI;
import com.weibo.sdk.android.net.RequestListener;

import Investmentletters.android.R;
import Investmentletters.android.adapter.WeiboListAdaper;
import Investmentletters.android.dao.base.WeiBoBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("ViewConstructor")
public class XinLangWeiBoListview extends LinearLayout implements
		OnItemClickListener {

	/** 微博列表数据 */
	private List<WeiBoBase> WeibolistData = null;
	/** 微博列表 */
	private ListView WeiboListView = null;
	/** 微博栏目列表adapter */
	private WeiboListAdaper WeiboAdapter = null;

	Oauth2AccessToken token;
	long id;
	RequestListener sinaListester;
	Activity activity = new Activity();
	Context context;

	public XinLangWeiBoListview(Context context, Oauth2AccessToken token,
			long id) {
		super(context);
		// TODO Auto-generated constructor stub
		this.token = token;
		this.id = id;
		this.context = context;
		WeibolistData = new ArrayList<WeiBoBase>();
		SetListester();

		long A = 0;
		long B = 0;
		if (token.isSessionValid()) {
			StatusesAPI statuses = new StatusesAPI(token);
			statuses.userTimeline(id, A, B, 20, 1, false, WeiboAPI.FEATURE.ALL,
					true, sinaListester);

		} else {
			System.out.println("新浪认证失败...");
		}
	}

	void SetListester() {
		sinaListester = new RequestListener() {
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：io异常" + e.toString());
			}

			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：异常" + e.toString());
			}

			@Override
			public void onComplete(final String str) {
				// TODO Auto-generated method stub
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject weibo_json = new JSONObject(str);
							JSONArray statuses = weibo_json
									.getJSONArray("statuses");
							int len = statuses.length();
							JSONObject sigle_weibo = null;
							for (int i = 0; i < len; i++) {
								sigle_weibo = statuses.getJSONObject(i);
								getjson(sigle_weibo);
							}

							// setListAdapter(adapter);

							InitView(context);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// TODO Auto-generated method stub
					}
				});
			}
		};
	}

	private void getjson(JSONObject json) {
		String creat_time = null;
		String text = null;
		String retweet_text = null;
		String pic_url = null;
		String original_pic = null;
		int reposts = 0;
		int comments = 0;
		String[] image_url = null;
		String retweeted_status = null;
		try {
			creat_time = json.getString("created_at");
			text = json.getString("text");
			pic_url = json.getString("pic_urls");
			image_url = getimageurl(pic_url);
			reposts = json.getInt("reposts_count");
			comments = json.getInt("comments_count");
			try {
				original_pic = json.getString("original_pic");
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				retweeted_status = json.getString("retweeted_status");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (retweeted_status != null) {
				JSONObject json_retweet = new JSONObject(retweeted_status);
				try {
					original_pic = json_retweet.getString("original_pic");
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					pic_url = json_retweet.getString("pic_urls");
				} catch (Exception e) {
					// TODO: handle exception
				}
				image_url = getimageurl(pic_url);
				try {
					retweet_text = json_retweet.getString("text");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else {

			}
			WeiBoBase data = new WeiBoBase(text, retweet_text, image_url,
					comments, reposts, creat_time,original_pic);
			WeibolistData.add(data);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] getimageurl(String str) {
		if (str.equals("[]")) {
			return null;
		}
		JSONArray json_image_url;
		String[] url = null;
		try {
			json_image_url = new JSONArray(str);
			int len = json_image_url.length();
			url = new String[len];
			JSONObject json_url = null;
			for (int i = 0; i < len; i++) {
				json_url = json_image_url.getJSONObject(i);
				try {
					url[i] = json_url.getString("thumbnail_pic");
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}

	void InitView(Context context) {
		LinearLayout View = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.weibolistview, null);
		addView(View);
		View.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		WeiboAdapter = new WeiboListAdaper((Activity) context, WeibolistData);
		WeiboListView = (ListView) View.findViewById(R.id.weibo_listview);
		WeiboListView.setAdapter(WeiboAdapter);
		WeiboListView.setOnItemClickListener(this);

		WeiboAdapter.notifyDataSetChanged();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
