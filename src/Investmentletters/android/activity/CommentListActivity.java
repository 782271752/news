package Investmentletters.android.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.R;
import Investmentletters.android.adapter.CommentAdapter;
import Investmentletters.android.dao.CommentDao;
import Investmentletters.android.dao.SubmitDiscuss;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.Comment;
import Investmentletters.android.entity.News;
import Investmentletters.android.view.RefreshListView;
import Investmentletters.android.view.RefreshListView.IXListViewListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class CommentListActivity extends Activity implements OnClickListener,
		IXListViewListener, DataVisitors.CallBack, OnItemClickListener {

	private View mBackButton; // 回退按钮
	/** 评论列表 listview */
	private RefreshListView mCommentListView;
	/** 评论列表 adapter */
	private CommentAdapter mCommentAdapter = null;
	/** 评论列表数据 */
	private List<Comment> mCommentData = null;
	/** 评论 dao 类 */
	private CommentDao mCommentDao = null;
	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;
	/** 评论 */
	private final int WHAT_GET_COMMENT_DATA = 0;
	/** 评论 下拉刷新/上拉加载更多 */
	private final int WHAT_GET_COMMENT_REFRESH_DATA = 1;
	/** what:评论 */
	private final int WHAT_COMMENT = 2;
	/** 获取设备的时间 */
	private SimpleDateFormat dateFormat = null;
	/** 当前新闻的id(标识） */
	int newId = -1;
	/**用户账号*/
	private String userName = null;
	/**评论内容*/
	private String commentContent = null;
	/**回复内容*/
	private String replyCommentContent = null;
	/**获取评论的内容*/
	private String getCommentContent = null;

	private EditText mCommentEditText;
	private EditText mReplyCommentEditText;
	/** 提交回复按钮 */
	private View mSubmitReplyCommentButton;
	/** 提交评论按钮 */
	private View mSubmitCommentButton;
	/** 评论布局 */
	private LinearLayout mReplyCommentLayout;
	/** 评论布局 */
	private LinearLayout mCommentLayout;

	private PopupWindow popupWindow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_comment_list);

		newId = getIntent().getExtras().getInt("newId", -1);
		userName = getIntent().getExtras().getString("userName");
		Log.i("newId", newId + "");
		Log.i("username", userName);
		dataVisitors = new DataVisitors(); // 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系
		mCommentData = new ArrayList<Comment>();
		mCommentDao = new CommentDao();
		dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);

		mBackButton = findViewById(R.id.id_back_btn);
		mBackButton.setOnClickListener(this);

		mCommentAdapter = new CommentAdapter(this, mCommentData);
		mCommentListView = (RefreshListView) findViewById(R.id.id_comment_listview);
		mCommentListView.setAdapter(mCommentAdapter);
		mCommentListView.setPullLoadEnable(true);
		mCommentListView.setPullRefreshEnable(true);
		mCommentListView.setXListViewListener(this);

		mCommentListView.setOnItemClickListener(this);

		mReplyCommentLayout = (LinearLayout) findViewById(R.id.reply_comment_layout);
		mCommentLayout = (LinearLayout) findViewById(R.id.comment_layout);
		mSubmitReplyCommentButton = findViewById(R.id.id_submit_reply_comment);
		mSubmitCommentButton = findViewById(R.id.id_submit_comment);
		mSubmitReplyCommentButton.setOnClickListener(this);
		mSubmitCommentButton.setOnClickListener(this);

		mCommentListView.refreshVisible(); // 刷新数据
	}

	/** 下拉刷新 */
	@Override
	public void onRefresh() {
		dataVisitors.getFreshComment(mCommentDao, newId, this,
				WHAT_GET_COMMENT_DATA);
	}

	@Override
	public void onLoadMore() {
		int minId = -1;
		minId = mCommentAdapter.getMinId(); // 获取列表最小的一条新闻Id
		dataVisitors.getCommentMore(mCommentDao, newId, minId, this,
				WHAT_GET_COMMENT_REFRESH_DATA);

	}

	@Override
	public void onResult(int what, Object res) {
		List<?> data = null;

		switch (what) {
		case WHAT_GET_COMMENT_DATA:
			data = (List<?>) res;
			if (data == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			} else {
				mCommentData.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					mCommentData.add((Comment) data.get(i));
				}
			}

			mCommentAdapter.notifyDataSetChanged();
			mCommentListView.stopRefresh();
			mCommentListView.stopLoadMore();
			mCommentListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));

			break;
		case WHAT_GET_COMMENT_REFRESH_DATA:
			data = (List<?>) res;
			if (data == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			} else {
				int size = data.size();
				for (int i = 0; i < size; i++) {
					mCommentData.add((Comment) data.get(i));
				}
			}

			mCommentAdapter.notifyDataSetChanged();
			mCommentListView.stopRefresh();
			mCommentListView.stopLoadMore();
			mCommentListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));
			break;
		case WHAT_COMMENT:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					CommentListActivity.this);
			dialog.setTitle("评论");
			dialog.setMessage("提交评论成功");

			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mReplyCommentLayout.setVisibility(View.GONE);
							mCommentLayout.setVisibility(View.GONE);
						}
					});
			dialog.create().show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_back_btn:
			this.finish();
			break;

		case R.id.id_reply_item_btn:// 回复
			mReplyCommentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_comment_item_btn:// 评论
			mCommentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_copy_item_btn: // 复制
			ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(getCommentContent);
			cm.getText();
			Toast.makeText(this, "评论内容已复制", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_submit_reply_comment: // 提交回复
			submitReply();
			break;
		case R.id.id_submit_comment: // 提交评论
			submitComment();

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		position--;

		Comment item = mCommentData.get(position);
		getCommentContent = item.getContent();

		if (popupWindow == null) {
			popupWindow = new PopupWindow(this);
			popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
			popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
			  //设置整个popupwindow的样式。
			popupWindow.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_comment_item));
			View contentView = View.inflate(this, R.layout.layout_comment_item,
					null);
			popupWindow.setContentView(contentView);

			// 还未在onClick（View）中实现，自已需要做
			contentView.findViewById(R.id.id_reply_item_btn)
					.setOnClickListener(this);// 回复
			contentView.findViewById(R.id.id_comment_item_btn)
					.setOnClickListener(this);// 评论
			contentView.findViewById(R.id.id_copy_item_btn).setOnClickListener(
					this);// 复制

			//使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。    
	        //如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。    
	        //listview的话就没有了作用。 
			popupWindow.setFocusable(false);
			popupWindow.setOutsideTouchable(true);

			contentView.measure(0, 0);
		}

		Rect rect = new Rect();
		view.getGlobalVisibleRect(rect);

		View contentView = popupWindow.getContentView();

		final int width = contentView.getMeasuredWidth();
		final int height = contentView.getMeasuredHeight();
		final int left = (rect.right - rect.left - width) / 2;
		final int top = rect.top - height;

		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, left, top);
	}

	/** 提交回复 */
	private void submitReply() {

		mReplyCommentEditText = (EditText) findViewById(R.id.id_reply_comment_edittext);
		replyCommentContent = mReplyCommentEditText.getText().toString().trim();

		if (replyCommentContent.equals("")) {
			Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
			return;
		}

		SubmitDiscuss sd = new SubmitDiscuss();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("NewId", String.valueOf(newId)));
		params.add(new BasicNameValuePair("ComContent", replyCommentContent));
		params.add(new BasicNameValuePair("UserId", userName));
		dataVisitors.doPost(sd, params, CommentListActivity.this, WHAT_COMMENT);
	}

	/** 提交评论 */
	private void submitComment() {

		mCommentEditText = (EditText) findViewById(R.id.id_comment_edittext);
		commentContent = mCommentEditText.getText().toString().trim();

		if (commentContent.equals("")) {
			Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
			return;
		}

		SubmitDiscuss sd = new SubmitDiscuss();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("NewId", String.valueOf(newId)));
		params.add(new BasicNameValuePair("ComContent", commentContent + "\n"
				+ "\n " + userName + "\n  " + getCommentContent));
		params.add(new BasicNameValuePair("UserId", userName));
		dataVisitors.doPost(sd, params, CommentListActivity.this, WHAT_COMMENT);
	}
}
