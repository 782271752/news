package Investmentletters.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.R;
import Investmentletters.android.adapter.NewsDetailPagerAdapter;
import Investmentletters.android.dao.DBToday;
import Investmentletters.android.dao.SubmitDiscuss;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ShareContent;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.NewsTextSizeDialog;
import Investmentletters.android.view.NewsTextSizeDialog.TextSize;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示详细新闻信息
 * 
 * @author liang
 */
public class NewsShowActivity extends Activity implements View.OnClickListener,
		ViewPager.OnPageChangeListener, DataVisitors.CallBack,
		NewsTextSizeDialog.OnTextSizeChangeListener{

	/** 数据访问协调类 */
	private DataVisitors dataVisitors = null;

	/** 数据共享类 */
	private MyApplication app = null;

	/** 返回按钮 */
	private View mBackButton;
	/** 收藏按钮 */
	private ImageView mCollectButton;
	/** 分享按钮 */
	private View mShareButton;
	/** 评论按钮 */
	private View mCommentButton;
	/** 调整字体大小按钮 */
	private View mChangeTextSizeButton;
	/** 提交评论按钮 */
	private View mSubmitCommentButton;
	/** 打开评论列表按钮 */
	private TextView mCommentListTextViewButton;

	/** 评论布局 */
	private LinearLayout mCommentLayout;

	private ViewPager viewPager = null;
	/** 详细新闻ViewPager容器adapter */
	private NewsDetailPagerAdapter adapter = null;
	/** 显示的内容列表 */
	private List<News> data = null;

	/** 分享对话框 */
	private Dialog shareDialog = null;

	/** 内容分享类 */
	private ShareContent shareContent = null;

	/** 字体大小 */
	private int textSizeFifteen = 15;

	/** 字体大小 */
	private int textSizeNineteen = 19;

	/** 字体大小 */
	private int textSizeTwentyTwo = 22;
	/** 字体大小 */
	private int textSizeTwentyFive = 25;

	/** what:收藏 */
	private final int WHAT_PRESERVE = 1;
	/** what:取消收藏 */
	private final int WHAT_CANCEL_PRESERVE = 2;
	/** what:评论 */
	private final int WHAT_COMMENT = 3;

	/** 随便一个db dao都行，都调用父类prserve */
	private DBToday preServeDao = null;

	private EditText mCommentEditText;
	/***/
	int newId = -1;

	String userName = null;
	
	/**新闻详细界面字体选择框*/
	private NewsTextSizeDialog textSizeDialog = null;

	@SuppressWarnings("unchecked")
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_news_show);

		app = (MyApplication) this.getApplication();
		dataVisitors = new DataVisitors();
		preServeDao = new DBToday(this);

		data = (List<News>) app.getShowDetailDatas();
		int index = getIntent().getExtras().getInt("INDEX", 0); // 初始打开的位置
		boolean isOk = true;
		if (data == null || index < 0) {
			isOk = false;
		}

		if (index >= data.size()) {
			isOk = false;
		}

		if (!isOk) {
			Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(this);
		adapter = new NewsDetailPagerAdapter(this, data);
		viewPager.setAdapter(adapter);

		mBackButton = findViewById(R.id.id_news_show_back_btn);// 返回按钮
		mCollectButton = (ImageView) findViewById(R.id.id_collect_btn);// 收藏
		mCollectButton.setTag(false);
		mShareButton = findViewById(R.id.id_share_btn);// 分享按钮
		mCommentButton = findViewById(R.id.id_comment_btn); // 评论按钮
		mChangeTextSizeButton = findViewById(R.id.id_change_textsize_btn); // 调整字体大小按钮
		mSubmitCommentButton = findViewById(R.id.id_submit_comment);
		mCommentListTextViewButton = (TextView) findViewById(R.id.id_comment_list_textview_btn);

		mCommentLayout = (LinearLayout) findViewById(R.id.comment_layout);
		viewPager.setOnPageChangeListener(this);

		mBackButton.setOnClickListener(this);
		mCollectButton.setOnClickListener(this);
		mShareButton.setOnClickListener(this);
		mCommentButton.setOnClickListener(this);
		mChangeTextSizeButton.setOnClickListener(this);
		mSubmitCommentButton.setOnClickListener(this);
		mCommentListTextViewButton.setOnClickListener(this);

		viewPager.setCurrentItem(index);
		onPageSelected(index);// 首次系统不执行，手机执行

		shareContent = new ShareContent(this);

		if (Utils.isLogin()) { // 　如果已登录

			userName = app.getUserName(); // 获取登录的用户名
		} else {
			userName = "游客"; // 设置用户名设置为游客
		}
		
		textSizeDialog = new NewsTextSizeDialog(this);
		textSizeDialog.setListener(this);
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		boolean isPreserve = preServeDao.isPreserve(data.get(position).getId());
		newId = data.get(position).getId(); // 得到当前新闻的id
		mCollectButton.setTag(isPreserve);
		if (isPreserve) {// 已收藏
			mCollectButton.setImageResource(R.drawable.btn_collect_normal);
		} else {// 未收藏
			mCollectButton.setImageResource(R.drawable.btn_collect);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		int pos = viewPager.getCurrentItem();
		switch (id) {
		case R.id.id_news_show_back_btn: // 返回
			finish();
			break;

		case R.id.id_comment_btn: // 显示或隐藏评论界面

			if (mCommentLayout.getVisibility() == View.VISIBLE) {
				mCommentLayout.setVisibility(View.GONE);
			} else {
				mCommentLayout.setVisibility(View.VISIBLE);
			}

			break;

		case R.id.id_change_textsize_btn:// 显示调整字体大小
			textSizeDialog.show();
			break;

		case R.id.id_collect_btn: // 收藏
			if ((Boolean) mCollectButton.getTag()) {// 取消收藏
				dataVisitors.cancelPreserve(preServeDao,
						data.get(viewPager.getCurrentItem()).getId(), this,
						WHAT_CANCEL_PRESERVE);
			} else {// 收藏
				dataVisitors.preserve(preServeDao,
						data.get(viewPager.getCurrentItem()).getId(), this,
						WHAT_PRESERVE);
			}
			break;
		case R.id.id_submit_comment: // 提交评论
			submitComment();
			break;
		case R.id.id_comment_list_textview_btn:
			Intent i = new Intent(NewsShowActivity.this,
					CommentListActivity.class);
			i.putExtra("newId", newId);
			i.putExtra("userName", userName);
			startActivity(i);
			break;
		case R.id.id_share_btn:// 弹出分享dislog
			showShareDialog();
			break;

		case R.id.id_weixin_share_btn:// 分享到微信

			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // 获取选中的新闻
				StringBuffer sb = new StringBuffer();

				sb.append("标题：");
				sb.append(item.getTitle());
				sb.append("\n\n内容：");
				sb.append(item.getContent());
				sb.append("\n\n时间：");
				sb.append(item.getTime());
				String toshare = sb.toString();
				shareContent.shareToWeiXin(toshare);
			}
			shareDialog.dismiss();
			break;

		case R.id.id_weixin_friends_share_btn: // 分享到朋友圈

			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // 获取选中的新闻
				StringBuffer sb = new StringBuffer();

				sb.append("标题：");
				sb.append(item.getTitle());
				sb.append("\n\n内容：");
				sb.append(item.getContent());
				sb.append("\n\n时间：");
				sb.append(item.getTime());
				String toshare = sb.toString();
				shareContent.shareToWeiXinFriends(toshare);
			}
			shareDialog.dismiss();

			break;

		case R.id.id_sina_weibo_share_btn:// 分享到新浪微博
			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // 获取选中的新闻
				StringBuffer sb = new StringBuffer();

				sb.append(item.getTitle());
				sb.append("\n");
				sb.append(item.getContent());
				sb.append("\n");
				sb.append(item.getTime());

				shareContent.shareToSinaWeibo(sb.toString());
			}
			shareDialog.dismiss();
			break;

		case R.id.id_tencent_weibo_share_btn:// 分享到腾讯微博
			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // 获取选中的新闻
				StringBuffer sb = new StringBuffer();

				sb.append(item.getTitle());
				sb.append("\n");
				sb.append(item.getContent());
				sb.append("\n");
				sb.append(item.getTime());

				shareContent.shareToTencentWeiBo(sb.toString());
			}
			shareDialog.dismiss();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		app.setShowDetailDatas(null);
		super.onDestroy();
	}

	/** 显示分享dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(NewsShowActivity.this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			View mWXShareButton = dialogView
					.findViewById(R.id.id_weixin_share_btn);// 微信
			View mWXFriendsShareButton = dialogView
					.findViewById(R.id.id_weixin_friends_share_btn);// 朋友圈
			View mSinaWeiBoShareButton = dialogView
					.findViewById(R.id.id_sina_weibo_share_btn); // 新浪微博
			View mTencentWeiboShareButton = (Button) dialogView
					.findViewById(R.id.id_tencent_weibo_share_btn);// 腾讯微博

			mWXShareButton.setOnClickListener(this);
			mWXFriendsShareButton.setOnClickListener(this);
			mSinaWeiBoShareButton.setOnClickListener(this);
			mTencentWeiboShareButton.setOnClickListener(this);
		}
		shareDialog.show();
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub

		if (what == WHAT_PRESERVE) {// 收藏
			if (res == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {// 收藏成功
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this, R.layout.toast_preserve_add,
							null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton
							.setImageResource(R.drawable.btn_collect_normal);
					mCollectButton.setTag(true);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			}

		} else if (what == WHAT_CANCEL_PRESERVE) {// 取消收藏
			if (res == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {// 取消收藏成功
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this,
							R.layout.toast_preserve_cancel, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setImageResource(R.drawable.btn_collect);
					mCollectButton.setTag(false);
				} else {
					Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
		} else if (what == WHAT_COMMENT) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					NewsShowActivity.this);
			dialog.setTitle("评论");
			dialog.setMessage("提交评论成功");
			dialog.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mCommentLayout.setVisibility(View.GONE);
				}
			});
			dialog.create().show();
		}

	}

	/** 提交评论 */
	private void submitComment() {

		mCommentEditText = (EditText) findViewById(R.id.id_comment_edittext);
		String commentContent = mCommentEditText.getText().toString().trim();

		if (commentContent.equals("")) {
			Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
			return;
		}

		SubmitDiscuss sd = new SubmitDiscuss();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("NewId", String.valueOf(newId)));
		params.add(new BasicNameValuePair("ComContent", commentContent));
		params.add(new BasicNameValuePair("UserId", userName));
		dataVisitors.doPost(sd, params, NewsShowActivity.this, WHAT_COMMENT);
	}

	@Override
	public void onTextSizeChanged(TextSize size) {
		// TODO Auto-generated method stub
		switch (size) {
			case TEXT_SIZE_SMALL:
				/** 第一个参数：标题字体大小；第二个参数：内容字体大小；第三个参数：时间字体大小 */
				adapter.setTextSize(textSizeNineteen, textSizeFifteen,textSizeFifteen);
				break;
	
			case TEXT_SIZE_NORMAL:
				adapter.setTextSize(textSizeTwentyTwo, textSizeNineteen,textSizeNineteen);
				break;
	
			case TEXT_SIZE_LARGE:
				adapter.setTextSize(textSizeTwentyFive, textSizeTwentyTwo,textSizeTwentyTwo);
				break;
	
		}
	}
}
