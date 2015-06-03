package Investmentletters.android.activity;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.adapter.ImageNewsDetailPagerAdapter;
import Investmentletters.android.dao.DBImageDetail;
import Investmentletters.android.dao.DBImageOnLine;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;
import Investmentletters.android.entity.Image;
import Investmentletters.android.model.ImageOnLineDetailModel;
import Investmentletters.android.utils.ShareContent;
import Investmentletters.android.view.ImageNewsDetailViewPager;
import Investmentletters.android.view.ScaleImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ͼ����������ϸ
 * 
 * @author liang
 */
public class ImageNewsDetail extends Activity implements View.OnClickListener,
		CallBack, OnPageChangeListener {

	/** ͨ��Intent��bundle������Ҫ�鿴����ϸ����ͼƬ������id */
	public static final String BUNDLE_KEY_ID = "INDEX";

	/** ���ذ�ť */
	private View mBackButton;
	/** �ղذ�ť */
	private ImageView mCollectButton;
	/** ����ť */
	private View mShareButton;

	/** ��ͼ��ϸ����viewPager */
	private ImageNewsDetailViewPager viewPager = null;

	/** handler��what����ȡ����ϸ�������� */
	private final int HANDLER_WHAT_GET_DATA = 0;
	/** what:�ղ� */
	private final int WHAT_PRESERVE = 1;
	/** what:ȡ���ղ� */
	private final int WHAT_CANCEL_PRESERVE = 2;

	/** ���̶Ի��� */
	private ProgressDialog progressDialog = null;

	/** ��ͼ������ϸ��ϢViewPager����adapter */
	private ImageNewsDetailPagerAdapter adapter = null;
	/** ��ϸͼƬ���� */
	private List<Image> data = null;

	/** ��Ҫ��ȡ��ϸ������id */
	private int id = -1;

	/** ��Ҫ��ȡ��ϸ�����ű��� */
	private String title = null;

	/** ��ͼ����model */
	private ImageOnLineDetailModel imageOnLineDetailModel = null;

	/** λ�ñ��� */
	private TextView tvIndex = null;

	/** ����Ի��� */
	private Dialog shareDialog = null;

	/** ��ͼ���������б����ݿ�DAo */
	private DBImageOnLine dbPreserve = null;
	/** ��ͼ������ϸ�������ݿ�DAo */
	private DBImageDetail gbImgDetail = null;
	/** ���ݷ���Э���� */
	private DataVisitors dataVisitors = null;

	/** �Ƿ���ղش� */
	private boolean isFromPreserve = false;
	/** �Ƿ��Ѽ��ع� */
	private boolean isLoaded = false;

	/** ���ݷ����� */
	private ShareContent shareContent = null;

	/** ������title_bar */
	private View vTitleBar = null;
	/** �ײ���layout_bottom */
	private View vBottonBar = null;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_image_news_detail);

		id = getIntent().getExtras().getInt(BUNDLE_KEY_ID, -1);
		title = getIntent().getExtras().getString("TITLE");

		if (id < 0) {
			Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		dataVisitors = new DataVisitors();
		dbPreserve = new DBImageOnLine(this);
		gbImgDetail = new DBImageDetail(this);

		vTitleBar = findViewById(R.id.title_bar);// ������
		vBottonBar = findViewById(R.id.layout_bottom);// �ײ���

		viewPager = (ImageNewsDetailViewPager) findViewById(R.id.viewPager);

		mBackButton = findViewById(R.id.id_news_show_back_btn);// ���ذ�ť
		mBackButton.setOnClickListener(this);

		mCollectButton = (ImageView) findViewById(R.id.id_collect_btn);// �ղ�
		isFromPreserve = dbPreserve.isPreserve(id);
		mCollectButton.setTag(isFromPreserve);
		if (isFromPreserve) {
			mCollectButton.setImageResource(R.drawable.btn_collect_press);
		}

		mCollectButton.setOnClickListener(this);

		mShareButton = findViewById(R.id.id_share_btn);
		mShareButton.setOnClickListener(this);

		tvIndex = (TextView) findViewById(R.id.index);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("���ڻ�ȡ����");
		progressDialog.show();

		data = new ArrayList<Image>();
		adapter = new ImageNewsDetailPagerAdapter(this, data);
		viewPager.setAdapter(adapter);
		viewPager.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);

		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					finish();
					return true;
				}
				return false;
			}
		});

		progressDialog.show();
		imageOnLineDetailModel = new ImageOnLineDetailModel(dataVisitors);

		shareContent = new ShareContent(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isLoaded) {
			return;
		}

		isLoaded = true;

		if (isFromPreserve) {// �����ݿ��
			imageOnLineDetailModel.getDetailFromDb(gbImgDetail, id, this,
					HANDLER_WHAT_GET_DATA);
		} else {
			imageOnLineDetailModel.getDetail(id, this, HANDLER_WHAT_GET_DATA);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.id_news_show_back_btn: // ����
				finish();
				break;
				
			case R.id.id_collect_btn: // �ղ�
				if ((Boolean) mCollectButton.getTag()) {// ȡ���ղ�
					dataVisitors.cancelPreserve(dbPreserve, id, this,
							WHAT_CANCEL_PRESERVE);
				} else {// �ղ�
					dataVisitors.preserve(dbPreserve, id, this, WHAT_PRESERVE);
				}
	
				break;
	
			case R.id.id_share_btn:// ����ť
				showShareDialog();
				break;
	
			case R.id.img:// ���ͼƬ
				System.out.println("���ͼƬ��...");
				ScaleImageView siv = (ScaleImageView) v;
				if (siv.getScaleAble()) {// �����ű䲻������
					siv.reset();
					siv.setScale(false);
					viewPager.setScrollAble(true);
					vTitleBar.setVisibility(View.VISIBLE);
					vBottonBar.setVisibility(View.VISIBLE);
					System.out.println("���ͼƬ��������...");
				} else {// �������ű������
					siv.setScale(true);
					viewPager.setScrollAble(false);
					vTitleBar.setVisibility(View.GONE);
					vBottonBar.setVisibility(View.GONE);
					System.out.println("���ͼƬ���������ű������...");
				}
	
				break;
	
			case R.id.id_weixin_share_btn:// ����΢��
	
				shareContent.shareToWeiXin(title);
				shareDialog.dismiss();
				break;
				
			case R.id.id_weixin_friends_share_btn:// ����΢��
				
				shareContent.shareToWeiXinFriends(title);
				shareDialog.dismiss();
				break;
	
			case R.id.id_sina_weibo_share_btn:// ��������΢��
	
				shareContent.shareToSinaWeibo(title);
				shareDialog.dismiss();
				break;
	
			case R.id.id_tencent_weibo_share_btn:// ������Ѷ΢��
	
				shareContent.shareToTencentWeiBo(title);
				shareDialog.dismiss();
				break;
			default:
				break;
		}
	}

	@Override
	public void onResult(int what, Object res) {

		switch (what) {
		case HANDLER_WHAT_GET_DATA:
			data.clear();
			try {
				List<?> resData = (List<?>) res;
				for (Object item : resData) {
					data.add((Image) item);
				}

				if (!isFromPreserve) {
					dataVisitors.dbAdd(gbImgDetail, data, id, null, 0);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
				finish();
			}

			adapter.notifyDataSetChanged();
			int index = viewPager.getCurrentItem() + 1;
			tvIndex.setText("(" + index + "/" + data.size() + ")");

			progressDialog.dismiss();

			break;

		case WHAT_PRESERVE: // �ղ�
			if (res == null) {
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this, R.layout.toast_preserve_add, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setTag(true);
					mCollectButton.setImageResource(R.drawable.btn_collect_press);
				} else {
					Toast.makeText(this, "�ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case WHAT_CANCEL_PRESERVE: // ȡ���ղ�
			if (res == null) {
				Toast.makeText(this, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {
					
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this, R.layout.toast_preserve_cancel, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setTag(false);
					mCollectButton.setImageResource(R.drawable.btn_img_preserve);
				} else {
					Toast.makeText(this, "ȡ���ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
	}

	/** ��ʾ����dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(ImageNewsDetail.this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			dialogView.findViewById(R.id.id_weixin_share_btn)
					.setOnClickListener(this);// ΢��
			dialogView.findViewById(R.id.id_weixin_friends_share_btn)
			.setOnClickListener(this);// ����Ȧ
			dialogView.findViewById(R.id.id_sina_weibo_share_btn)
					.setOnClickListener(this); // ����΢��
			dialogView.findViewById(R.id.id_tencent_weibo_share_btn)
					.setOnClickListener(this);// ��Ѷ΢��
		}
		shareDialog.show();
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
		tvIndex.setText("(" + (position + 1) + "/" + data.size() + ")");
	}

}
