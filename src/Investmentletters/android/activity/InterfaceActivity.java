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

	/** ������ */
	private InterfaceMenu mSlidingMenu;

	/** �л��������տ챨 */
	private ImageView jinrikuaibao;
	/** �л��������й㲥 */
	private ImageView gushiguangbo;
	/** �л�����Ͷ���ڲ� */
	private ImageView touzineican;
	/** �л������������ */
	private ImageView hudongzhengu;

	/** ���ѿ�ѶFragment */
	private Fragment_Kuaibao fKuaibao = null;
	/** ���й㲥Fragment */
	private Fragment_Guangbo fGuangbo = null;
	/** Ͷ���ڲ�Fragment */
	private Fragment_Neican fNeican = null;
	/** �������Fragment */
	private Fragment_Zhengu fZhengu = null;
	/** ��¼״̬ */
	public static boolean IsLogind;

	/** ��߲˵�Fragment */
	private LeftFragment fLeft = null;
	/** �ұ߲˵�Fragment */
	private RightFragment fRight = null;
	/** ����dialog */
	private AlertDialog updateDialog = null;
	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;

	// ��ȡ��ǰ����İ汾��
	private String getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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
				builder.setTitle("��ǰ�汾Ϊ�� v" + V_old);
				builder.setMessage("���°汾Ϊ: v" + V_new + ",�Ƿ����?");
				builder.setPositiveButton("����",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new UpdateThread(InterfaceActivity.this, Url)
										.start();
							}
						});
				builder.setNegativeButton("������",
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

		jinrikuaibao = (ImageView) findViewById(R.id.id_b_kuaibao);// �л��������տ챨
		jinrikuaibao.setOnClickListener(this);

		gushiguangbo = (ImageView) findViewById(R.id.id_b_guangbo);// �л��������й㲥
		gushiguangbo.setOnClickListener(this);

		touzineican = (ImageView) findViewById(R.id.id_b_neican);// �л�����Ͷ���ڲ�
		touzineican.setOnClickListener(this);

		hudongzhengu = (ImageView) findViewById(R.id.id_b_zhengu);// �л������������
		hudongzhengu.setOnClickListener(this);

		fKuaibao = new Fragment_Kuaibao(); // ���տ�Ѷ
		Update upversion = new Update();
		dataVisitors.doGet(upversion, this, 0);// ��ȡ�汾��Ϣ
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();

		// �����ұ߿�
		fRight = new RightFragment();
		t.replace(R.id.right_frame, fRight);
		
		// ������߿�
		fLeft = new LeftFragment();
		t.replace(R.id.left_frame, fLeft);
		fLeft.setKuaiBaoFragment(fKuaibao);// ���ý��տ챨Fragment

		// Ĭ����ʾ���տ챨�б�
		t.replace(R.id.id_framelayout, fKuaibao);

		SharePreferenceUtils spu = new SharePreferenceUtils(this);
		String userName = spu.getUserName();
		String passwd = spu.getPasswd();
		if (!passwd.equals("") && !userName.equals("")) {// �м�¼������Ϊ�ѵ�½
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

		if (spu.getPushState()) {// ������
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
					builder.setMessage("�Ƿ��˳�Ͷ�ʿ챨")
							.setPositiveButton("��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											finish();
										}
									})
							.setNegativeButton("��",
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
