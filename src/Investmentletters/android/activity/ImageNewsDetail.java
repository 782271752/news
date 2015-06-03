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
 * 美图在线新闻详细
 * 
 * @author liang
 */
public class ImageNewsDetail extends Activity implements View.OnClickListener,
		CallBack, OnPageChangeListener {

	/** 通过Intent的bundle传输需要查看的详细新闻图片的新闻id */
	public static final String BUNDLE_KEY_ID = "INDEX";

	/** 返回按钮 */
	private View mBackButton;
	/** 收藏按钮 */
	private ImageView mCollectButton;
	/** 分享按钮 */
	private View mShareButton;

	/** 美图详细新闻viewPager */
	private ImageNewsDetailViewPager viewPager = null;

	/** handler的what，获取到详细新闻数据 */
	private final int HANDLER_WHAT_GET_DATA = 0;
	/** what:收藏 */
	private final int WHAT_PRESERVE = 1;
	/** what:取消收藏 */
	private final int WHAT_CANCEL_PRESERVE = 2;

	/** 过程对话框 */
	private ProgressDialog progressDialog = null;

	/** 美图在线详细信息ViewPager容器adapter */
	private ImageNewsDetailPagerAdapter adapter = null;
	/** 详细图片数组 */
	private List<Image> data = null;

	/** 需要获取详细的新闻id */
	private int id = -1;

	/** 需要获取详细的新闻标题 */
	private String title = null;

	/** 美图详情model */
	private ImageOnLineDetailModel imageOnLineDetailModel = null;

	/** 位置标题 */
	private TextView tvIndex = null;

	/** 分享对话框 */
	private Dialog shareDialog = null;

	/** 美图在线新闻列表数据库DAo */
	private DBImageOnLine dbPreserve = null;
	/** 美图在线详细新闻数据库DAo */
	private DBImageDetail gbImgDetail = null;
	/** 数据访问协调类 */
	private DataVisitors dataVisitors = null;

	/** 是否从收藏打开 */
	private boolean isFromPreserve = false;
	/** 是否已加载过 */
	private boolean isLoaded = false;

	/** 内容分享类 */
	private ShareContent shareContent = null;

	/** 标题栏title_bar */
	private View vTitleBar = null;
	/** 底部栏layout_bottom */
	private View vBottonBar = null;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_image_news_detail);

		id = getIntent().getExtras().getInt(BUNDLE_KEY_ID, -1);
		title = getIntent().getExtras().getString("TITLE");

		if (id < 0) {
			Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		dataVisitors = new DataVisitors();
		dbPreserve = new DBImageOnLine(this);
		gbImgDetail = new DBImageDetail(this);

		vTitleBar = findViewById(R.id.title_bar);// 标题栏
		vBottonBar = findViewById(R.id.layout_bottom);// 底部栏

		viewPager = (ImageNewsDetailViewPager) findViewById(R.id.viewPager);

		mBackButton = findViewById(R.id.id_news_show_back_btn);// 返回按钮
		mBackButton.setOnClickListener(this);

		mCollectButton = (ImageView) findViewById(R.id.id_collect_btn);// 收藏
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
		progressDialog.setMessage("正在获取数据");
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

		if (isFromPreserve) {// 从数据库查
			imageOnLineDetailModel.getDetailFromDb(gbImgDetail, id, this,
					HANDLER_WHAT_GET_DATA);
		} else {
			imageOnLineDetailModel.getDetail(id, this, HANDLER_WHAT_GET_DATA);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.id_news_show_back_btn: // 返回
				finish();
				break;
				
			case R.id.id_collect_btn: // 收藏
				if ((Boolean) mCollectButton.getTag()) {// 取消收藏
					dataVisitors.cancelPreserve(dbPreserve, id, this,
							WHAT_CANCEL_PRESERVE);
				} else {// 收藏
					dataVisitors.preserve(dbPreserve, id, this, WHAT_PRESERVE);
				}
	
				break;
	
			case R.id.id_share_btn:// 分享按钮
				showShareDialog();
				break;
	
			case R.id.img:// 点击图片
				System.out.println("点击图片：...");
				ScaleImageView siv = (ScaleImageView) v;
				if (siv.getScaleAble()) {// 可缩放变不可缩放
					siv.reset();
					siv.setScale(false);
					viewPager.setScrollAble(true);
					vTitleBar.setVisibility(View.VISIBLE);
					vBottonBar.setVisibility(View.VISIBLE);
					System.out.println("点击图片：可缩放...");
				} else {// 不可缩放变可缩放
					siv.setScale(true);
					viewPager.setScrollAble(false);
					vTitleBar.setVisibility(View.GONE);
					vBottonBar.setVisibility(View.GONE);
					System.out.println("点击图片：不可缩放变可缩放...");
				}
	
				break;
	
			case R.id.id_weixin_share_btn:// 分享到微信
	
				shareContent.shareToWeiXin(title);
				shareDialog.dismiss();
				break;
				
			case R.id.id_weixin_friends_share_btn:// 分享到微信
				
				shareContent.shareToWeiXinFriends(title);
				shareDialog.dismiss();
				break;
	
			case R.id.id_sina_weibo_share_btn:// 分享到新浪微博
	
				shareContent.shareToSinaWeibo(title);
				shareDialog.dismiss();
				break;
	
			case R.id.id_tencent_weibo_share_btn:// 分享到腾讯微博
	
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
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				finish();
			}

			adapter.notifyDataSetChanged();
			int index = viewPager.getCurrentItem() + 1;
			tvIndex.setText("(" + index + "/" + data.size() + ")");

			progressDialog.dismiss();

			break;

		case WHAT_PRESERVE: // 收藏
			if (res == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case WHAT_CANCEL_PRESERVE: // 取消收藏
			if (res == null) {
				Toast.makeText(this, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
			break;

		default:
			break;
		}
	}

	/** 显示分享dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(ImageNewsDetail.this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			dialogView.findViewById(R.id.id_weixin_share_btn)
					.setOnClickListener(this);// 微信
			dialogView.findViewById(R.id.id_weixin_friends_share_btn)
			.setOnClickListener(this);// 朋友圈
			dialogView.findViewById(R.id.id_sina_weibo_share_btn)
					.setOnClickListener(this); // 新浪微博
			dialogView.findViewById(R.id.id_tencent_weibo_share_btn)
					.setOnClickListener(this);// 腾讯微博
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
