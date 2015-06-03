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
 * ��ʾ��ϸ������Ϣ
 * 
 * @author liang
 */
public class NewsShowActivity extends Activity implements View.OnClickListener,
		ViewPager.OnPageChangeListener, DataVisitors.CallBack,
		NewsTextSizeDialog.OnTextSizeChangeListener{

	/** ���ݷ���Э���� */
	private DataVisitors dataVisitors = null;

	/** ���ݹ����� */
	private MyApplication app = null;

	/** ���ذ�ť */
	private View mBackButton;
	/** �ղذ�ť */
	private ImageView mCollectButton;
	/** ����ť */
	private View mShareButton;
	/** ���۰�ť */
	private View mCommentButton;
	/** ���������С��ť */
	private View mChangeTextSizeButton;
	/** �ύ���۰�ť */
	private View mSubmitCommentButton;
	/** �������б�ť */
	private TextView mCommentListTextViewButton;

	/** ���۲��� */
	private LinearLayout mCommentLayout;

	private ViewPager viewPager = null;
	/** ��ϸ����ViewPager����adapter */
	private NewsDetailPagerAdapter adapter = null;
	/** ��ʾ�������б� */
	private List<News> data = null;

	/** ����Ի��� */
	private Dialog shareDialog = null;

	/** ���ݷ����� */
	private ShareContent shareContent = null;

	/** �����С */
	private int textSizeFifteen = 15;

	/** �����С */
	private int textSizeNineteen = 19;

	/** �����С */
	private int textSizeTwentyTwo = 22;
	/** �����С */
	private int textSizeTwentyFive = 25;

	/** what:�ղ� */
	private final int WHAT_PRESERVE = 1;
	/** what:ȡ���ղ� */
	private final int WHAT_CANCEL_PRESERVE = 2;
	/** what:���� */
	private final int WHAT_COMMENT = 3;

	/** ���һ��db dao���У������ø���prserve */
	private DBToday preServeDao = null;

	private EditText mCommentEditText;
	/***/
	int newId = -1;

	String userName = null;
	
	/**������ϸ��������ѡ���*/
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
		int index = getIntent().getExtras().getInt("INDEX", 0); // ��ʼ�򿪵�λ��
		boolean isOk = true;
		if (data == null || index < 0) {
			isOk = false;
		}

		if (index >= data.size()) {
			isOk = false;
		}

		if (!isOk) {
			Toast.makeText(this, "�����쳣", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(this);
		adapter = new NewsDetailPagerAdapter(this, data);
		viewPager.setAdapter(adapter);

		mBackButton = findViewById(R.id.id_news_show_back_btn);// ���ذ�ť
		mCollectButton = (ImageView) findViewById(R.id.id_collect_btn);// �ղ�
		mCollectButton.setTag(false);
		mShareButton = findViewById(R.id.id_share_btn);// ����ť
		mCommentButton = findViewById(R.id.id_comment_btn); // ���۰�ť
		mChangeTextSizeButton = findViewById(R.id.id_change_textsize_btn); // ���������С��ť
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
		onPageSelected(index);// �״�ϵͳ��ִ�У��ֻ�ִ��

		shareContent = new ShareContent(this);

		if (Utils.isLogin()) { // ������ѵ�¼

			userName = app.getUserName(); // ��ȡ��¼���û���
		} else {
			userName = "�ο�"; // �����û�������Ϊ�ο�
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
		newId = data.get(position).getId(); // �õ���ǰ���ŵ�id
		mCollectButton.setTag(isPreserve);
		if (isPreserve) {// ���ղ�
			mCollectButton.setImageResource(R.drawable.btn_collect_normal);
		} else {// δ�ղ�
			mCollectButton.setImageResource(R.drawable.btn_collect);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		int pos = viewPager.getCurrentItem();
		switch (id) {
		case R.id.id_news_show_back_btn: // ����
			finish();
			break;

		case R.id.id_comment_btn: // ��ʾ���������۽���

			if (mCommentLayout.getVisibility() == View.VISIBLE) {
				mCommentLayout.setVisibility(View.GONE);
			} else {
				mCommentLayout.setVisibility(View.VISIBLE);
			}

			break;

		case R.id.id_change_textsize_btn:// ��ʾ���������С
			textSizeDialog.show();
			break;

		case R.id.id_collect_btn: // �ղ�
			if ((Boolean) mCollectButton.getTag()) {// ȡ���ղ�
				dataVisitors.cancelPreserve(preServeDao,
						data.get(viewPager.getCurrentItem()).getId(), this,
						WHAT_CANCEL_PRESERVE);
			} else {// �ղ�
				dataVisitors.preserve(preServeDao,
						data.get(viewPager.getCurrentItem()).getId(), this,
						WHAT_PRESERVE);
			}
			break;
		case R.id.id_submit_comment: // �ύ����
			submitComment();
			break;
		case R.id.id_comment_list_textview_btn:
			Intent i = new Intent(NewsShowActivity.this,
					CommentListActivity.class);
			i.putExtra("newId", newId);
			i.putExtra("userName", userName);
			startActivity(i);
			break;
		case R.id.id_share_btn:// ��������dislog
			showShareDialog();
			break;

		case R.id.id_weixin_share_btn:// ����΢��

			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // ��ȡѡ�е�����
				StringBuffer sb = new StringBuffer();

				sb.append("���⣺");
				sb.append(item.getTitle());
				sb.append("\n\n���ݣ�");
				sb.append(item.getContent());
				sb.append("\n\nʱ�䣺");
				sb.append(item.getTime());
				String toshare = sb.toString();
				shareContent.shareToWeiXin(toshare);
			}
			shareDialog.dismiss();
			break;

		case R.id.id_weixin_friends_share_btn: // ��������Ȧ

			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // ��ȡѡ�е�����
				StringBuffer sb = new StringBuffer();

				sb.append("���⣺");
				sb.append(item.getTitle());
				sb.append("\n\n���ݣ�");
				sb.append(item.getContent());
				sb.append("\n\nʱ�䣺");
				sb.append(item.getTime());
				String toshare = sb.toString();
				shareContent.shareToWeiXinFriends(toshare);
			}
			shareDialog.dismiss();

			break;

		case R.id.id_sina_weibo_share_btn:// ��������΢��
			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // ��ȡѡ�е�����
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

		case R.id.id_tencent_weibo_share_btn:// ������Ѷ΢��
			if (pos >= 0 && data.size() > pos) {
				News item = data.get(pos); // ��ȡѡ�е�����
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

	/** ��ʾ����dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(NewsShowActivity.this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			View mWXShareButton = dialogView
					.findViewById(R.id.id_weixin_share_btn);// ΢��
			View mWXFriendsShareButton = dialogView
					.findViewById(R.id.id_weixin_friends_share_btn);// ����Ȧ
			View mSinaWeiBoShareButton = dialogView
					.findViewById(R.id.id_sina_weibo_share_btn); // ����΢��
			View mTencentWeiboShareButton = (Button) dialogView
					.findViewById(R.id.id_tencent_weibo_share_btn);// ��Ѷ΢��

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

		if (what == WHAT_PRESERVE) {// �ղ�
			if (res == null) {
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {// �ղسɹ�
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
					Toast.makeText(this, "�ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}

		} else if (what == WHAT_CANCEL_PRESERVE) {// ȡ���ղ�
			if (res == null) {
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {// ȡ���ղسɹ�
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this,
							R.layout.toast_preserve_cancel, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setImageResource(R.drawable.btn_collect);
					mCollectButton.setTag(false);
				} else {
					Toast.makeText(this, "ȡ���ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		} else if (what == WHAT_COMMENT) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					NewsShowActivity.this);
			dialog.setTitle("����");
			dialog.setMessage("�ύ���۳ɹ�");
			dialog.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mCommentLayout.setVisibility(View.GONE);
				}
			});
			dialog.create().show();
		}

	}

	/** �ύ���� */
	private void submitComment() {

		mCommentEditText = (EditText) findViewById(R.id.id_comment_edittext);
		String commentContent = mCommentEditText.getText().toString().trim();

		if (commentContent.equals("")) {
			Toast.makeText(this, "��������������", Toast.LENGTH_SHORT).show();
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
				/** ��һ�����������������С���ڶ������������������С��������������ʱ�������С */
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
