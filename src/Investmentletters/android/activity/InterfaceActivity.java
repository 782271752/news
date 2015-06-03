package Investmentletters.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import Investmentletters.android.R;
import Investmentletters.android.dao.Update;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.fragment.Fragment_Guangbo;
import Investmentletters.android.fragment.Fragment_Kuaibao;
import Investmentletters.android.fragment.Fragment_Neican;
import Investmentletters.android.fragment.Fragment_Zhengu;
import Investmentletters.android.fragment.LeftFragment;
import Investmentletters.android.fragment.RightFragment;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.DBHelper;
import Investmentletters.android.utils.SharePreferenceUtils;
import Investmentletters.android.utils.UpdateThread;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.InterfaceMenu;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class InterfaceActivity extends FragmentActivity implements
		View.OnClickListener, DataVisitors.CallBack {

	/** 界面框架 */
	private InterfaceMenu mSlidingMenu;

	/** 切换栏：今日快报 */
	private ImageView jinrikuaibao;
	/** 切换栏：股市广播 */
	private ImageView gushiguangbo;
	/** 切换栏：投资内参 */
	private ImageView touzineican;
	/** 切换栏：互动诊股 */
	private ImageView hudongzhengu;

	/** 今已快讯Fragment */
	private Fragment_Kuaibao fKuaibao = null;
	/** 股市广播Fragment */
	private Fragment_Guangbo fGuangbo = null;
	/** 投资内参Fragment */
	private Fragment_Neican fNeican = null;
	/** 互动诊股Fragment */
	private Fragment_Zhengu fZhengu = null;
	/** 登录状态 */
	public static boolean IsLogind;

	/** 左边菜单Fragment */
	private LeftFragment fLeft = null;
	/** 右边菜单Fragment */
	private RightFragment fRight = null;
	/** 更新dialog */
	private AlertDialog updateDialog = null;
	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;

	// 获取当前程序的版本号
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}

	private void Updates(String V_new, final String Url) {
		try {
			final String V_old = getVersionName();
			float v1 = Float.parseFloat(V_new);
			float v2 = Float.parseFloat(V_old);
			if (v1 > v2) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						InterfaceActivity.this);
				builder.setTitle("当前版本为： v" + V_old);
				builder.setMessage("最新版本为: v" + V_new + ",是否更新?");
				builder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new UpdateThread(InterfaceActivity.this, Url)
										.start();
							}
						});
				builder.setNegativeButton("不更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				updateDialog = builder.create();
				updateDialog.show();

			}
			// upversion.doGet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	protected void onCreate(Bundle data) {
		super.onCreate(data);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		dataVisitors = new DataVisitors();
		mSlidingMenu = (InterfaceMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.right_frame, null));
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		jinrikuaibao = (ImageView) findViewById(R.id.id_b_kuaibao);// 切换栏：今日快报
		jinrikuaibao.setOnClickListener(this);

		gushiguangbo = (ImageView) findViewById(R.id.id_b_guangbo);// 切换栏：股市广播
		gushiguangbo.setOnClickListener(this);

		touzineican = (ImageView) findViewById(R.id.id_b_neican);// 切换栏：投资内参
		touzineican.setOnClickListener(this);

		hudongzhengu = (ImageView) findViewById(R.id.id_b_zhengu);// 切换栏：互动诊股
		hudongzhengu.setOnClickListener(this);

		fKuaibao = new Fragment_Kuaibao(); // 今日快讯
		Update upversion = new Update();
		dataVisitors.doGet(upversion, this, 0);// 获取版本信息
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();

		// 设置右边框
		fRight = new RightFragment();
		t.replace(R.id.right_frame, fRight);
		
		// 设置左边框
		fLeft = new LeftFragment();
		t.replace(R.id.left_frame, fLeft);
		fLeft.setKuaiBaoFragment(fKuaibao);// 设置今日快报Fragment

		// 默认显示今日快报列表
		t.replace(R.id.id_framelayout, fKuaibao);

		SharePreferenceUtils spu = new SharePreferenceUtils(this);
		String userName = spu.getUserName();
		String passwd = spu.getPasswd();
		if (!passwd.equals("") && !userName.equals("")) {// 有记录，设置为已登陆
			MyApplication app = (MyApplication) getApplication();
			app.setUserName(userName);
			app.setPasswd(passwd);
			Utils.setLogin(true);
		}

		t.commit();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication app = (MyApplication) getApplication();
		app.setScreenWidth(dm.widthPixels);

		if (spu.getPushState()) {// 可推送
			if (!PushManager.isPushEnabled(this)
					&& !PushManager.isConnected(this)) {
				PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,
						Constants.BAIDU_PUSH_APP_ID);
			}
		} else {
			PushManager.stopWork(this);
		}
		
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
		fLeft.onResume();
	}

	public int getscroll() {
		return mSlidingMenu.getscroll();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
		fRight.onResume();
	}

	public void showCenter() {
		mSlidingMenu.showCenterView();
	}

	@SuppressLint("Recycle")
	@Override
	public void onClick(View v) {
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		switch (v.getId()) {
		case R.id.id_b_kuaibao:
			jinrikuaibao.setBackgroundResource(R.drawable.foot1_1);
			gushiguangbo.setBackgroundResource(R.drawable.foot2);
			touzineican.setBackgroundResource(R.drawable.foot3);
			hudongzhengu.setBackgroundResource(R.drawable.foot4);
			t.replace(R.id.id_framelayout, fKuaibao);
			t.commit();
			break;

		case R.id.id_b_guangbo:
			jinrikuaibao.setBackgroundResource(R.drawable.foot1);
			gushiguangbo.setBackgroundResource(R.drawable.foot2_1);
			touzineican.setBackgroundResource(R.drawable.foot3);
			hudongzhengu.setBackgroundResource(R.drawable.foot4);

			if (fGuangbo == null) {
				fGuangbo = new Fragment_Guangbo();
			}

			t.replace(R.id.id_framelayout, fGuangbo);
			t.commit();
			break;

		case R.id.id_b_neican:
			jinrikuaibao.setBackgroundResource(R.drawable.foot1);
			gushiguangbo.setBackgroundResource(R.drawable.foot2);
			touzineican.setBackgroundResource(R.drawable.foot3_1);
			hudongzhengu.setBackgroundResource(R.drawable.foot4);
			RightFragment.LastStatus = 2;
			if (fNeican == null) {
				fNeican = new Fragment_Neican();
			}
			t.replace(R.id.id_framelayout, fNeican);

			t.commit();
			break;

		case R.id.id_b_zhengu:
			jinrikuaibao.setBackgroundResource(R.drawable.foot1);
			gushiguangbo.setBackgroundResource(R.drawable.foot2);
			touzineican.setBackgroundResource(R.drawable.foot3);
			hudongzhengu.setBackgroundResource(R.drawable.foot4_1);

			if (fZhengu == null) {
				fZhengu = new Fragment_Zhengu();
			}

			t.replace(R.id.id_framelayout, fZhengu);
			t.commit();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

			// long currentTime = System.currentTimeMillis();
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				Fragment_Kuaibao fragment = new Fragment_Kuaibao();
				if (fragment.isAdded() == false) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("是否退出投资快报")
							.setPositiveButton("是",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									})
							.setNegativeButton("否",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
					AlertDialog ad = builder.create();
					ad.show();
				} else {
					showLeft();
				}
			} else {
				getSupportFragmentManager().popBackStack();
			}

		}
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			mSlidingMenu.showRightView();
		}

		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DBHelper.getInstance(this).close();
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		JSONObject json;
		if (res == null)
			return;
		try {
			json = new JSONObject(res.toString().trim());
			String version_new = json.getString("version");
			String Url = json.getString("url");
			Updates(version_new, Url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
