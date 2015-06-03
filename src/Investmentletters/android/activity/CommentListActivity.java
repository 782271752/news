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

	private View mBackButton; // ���˰�ť
	/** �����б� listview */
	private RefreshListView mCommentListView;
	/** �����б� adapter */
	private CommentAdapter mCommentAdapter = null;
	/** �����б����� */
	private List<Comment> mCommentData = null;
	/** ���� dao �� */
	private CommentDao mCommentDao = null;
	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;
	/** ���� */
	private final int WHAT_GET_COMMENT_DATA = 0;
	/** ���� ����ˢ��/�������ظ��� */
	private final int WHAT_GET_COMMENT_REFRESH_DATA = 1;
	/** what:���� */
	private final int WHAT_COMMENT = 2;
	/** ��ȡ�豸��ʱ�� */
	private SimpleDateFormat dateFormat = null;
	/** ��ǰ���ŵ�id(��ʶ�� */
	int newId = -1;
	/**�û��˺�*/
	private String userName = null;
	/**��������*/
	private String commentContent = null;
	/**�ظ�����*/
	private String replyCommentContent = null;
	/**��ȡ���۵�����*/
	private String getCommentContent = null;

	private EditText mCommentEditText;
	private EditText mReplyCommentEditText;
	/** �ύ�ظ���ť */
	private View mSubmitReplyCommentButton;
	/** �ύ���۰�ť */
	private View mSubmitCommentButton;
	/** ���۲��� */
	private LinearLayout mReplyCommentLayout;
	/** ���۲��� */
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
		dataVisitors = new DataVisitors(); // ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ
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

		mCommentListView.refreshVisible(); // ˢ������
	}

	/** ����ˢ�� */
	@Override
	public void onRefresh() {
		dataVisitors.getFreshComment(mCommentDao, newId, this,
				WHAT_GET_COMMENT_DATA);
	}

	@Override
	public void onLoadMore() {
		int minId = -1;
		minId = mCommentAdapter.getMinId(); // ��ȡ�б���С��һ������Id
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
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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
			dialog.setTitle("����");
			dialog.setMessage("�ύ���۳ɹ�");

			dialog.setPositiveButton("ȷ��",
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

		case R.id.id_reply_item_btn:// �ظ�
			mReplyCommentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_comment_item_btn:// ����
			mCommentLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.id_copy_item_btn: // ����
			ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cm.setText(getCommentContent);
			cm.getText();
			Toast.makeText(this, "���������Ѹ���", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_submit_reply_comment: // �ύ�ظ�
			submitReply();
			break;
		case R.id.id_submit_comment: // �ύ����
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
			  //��������popupwindow����ʽ��
			popupWindow.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_comment_item));
			View contentView = View.inflate(this, R.layout.layout_comment_item,
					null);
			popupWindow.setContentView(contentView);

			// ��δ��onClick��View����ʵ�֣�������Ҫ��
			contentView.findViewById(R.id.id_reply_item_btn)
					.setOnClickListener(this);// �ظ�
			contentView.findViewById(R.id.id_comment_item_btn)
					.setOnClickListener(this);// ����
			contentView.findViewById(R.id.id_copy_item_btn).setOnClickListener(
					this);// ����

			//ʹ��������Ŀռ���ʾ����Ӧ��Ч�����Ƚϵ��buttonʱ������ɫ�ı䡣    
	        //���Ϊfalse�����صĿռ������û�з�Ӧ�����¼��ǿ��Լ������ġ�    
	        //listview�Ļ���û�������á� 
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

	/** �ύ�ظ� */
	private void submitReply() {

		mReplyCommentEditText = (EditText) findViewById(R.id.id_reply_comment_edittext);
		replyCommentContent = mReplyCommentEditText.getText().toString().trim();

		if (replyCommentContent.equals("")) {
			Toast.makeText(this, "��������������", Toast.LENGTH_SHORT).show();
			return;
		}

		SubmitDiscuss sd = new SubmitDiscuss();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("NewId", String.valueOf(newId)));
		params.add(new BasicNameValuePair("ComContent", replyCommentContent));
		params.add(new BasicNameValuePair("UserId", userName));
		dataVisitors.doPost(sd, params, CommentListActivity.this, WHAT_COMMENT);
	}

	/** �ύ���� */
	private void submitComment() {

		mCommentEditText = (EditText) findViewById(R.id.id_comment_edittext);
		commentContent = mCommentEditText.getText().toString().trim();

		if (commentContent.equals("")) {
			Toast.makeText(this, "��������������", Toast.LENGTH_SHORT).show();
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
