package Investmentletters.android.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.CustomerImageLoadHandler;
import Investmentletters.android.utils.Http;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.OffLineUtils;
import Investmentletters.android.utils.Utils;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 右边菜单
 * @author Administrator
 */
public class RightFragment extends Fragment implements OnClickListener{
	
	/**下面切换按钮：快报*/
	private ImageView kuaibao;
	/**下面切换按钮：广播*/
	private ImageView guangbo;
	/**下面切换按钮：内参*/
	private ImageView neican;
	/**下面切换按钮：诊股*/
	private ImageView zhengu;
	
	// 方便起见 建个变量 保存login页面的上一个状态
	public static int LastStatus = 0;
	
	/**离线加载线程类*/
	private OffLineUtils offLineUtils = null;
	
	/**用户头像：head_view*/
	private ImageView ivHead = null;
	/**头像加载handler*/
	private CustomerImageLoadHandler cusImgHandler = null;
	
	/**数据共享类*/
	private MyApplication app = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cusImgHandler = new CustomerImageLoadHandler(R.drawable.ic_radio_dj);
		app = (MyApplication)getActivity().getApplication();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.right, null);
		
		view.findViewById(R.id.right_zhanghu).setOnClickListener(this);//帐户框
		
		ivHead = (ImageView)view.findViewById(R.id.head_view);//头像
		ivHead.setTag(-1);
		
		view.findViewById(R.id.right_outline).setOnClickListener(this);//离线下载
		view.findViewById(R.id.msg_push).setOnClickListener(this);//消息记录
		view.findViewById(R.id.right_shouc).setOnClickListener(this);//收藏
		view.findViewById(R.id.right_setting).setOnClickListener(this);//设置

		kuaibao = (ImageView) getActivity().findViewById(R.id.id_b_kuaibao);
		guangbo = (ImageView) getActivity().findViewById(R.id.id_b_guangbo);
		neican = (ImageView) getActivity().findViewById(R.id.id_b_neican);
		zhengu = (ImageView) getActivity().findViewById(R.id.id_b_zhengu);
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(((Integer)ivHead.getTag())<0 || app.isModifyHeadImg()){//未加过头像
			 String userName = app.getUserName();
			 new AsyncTask<String, String, String>(){
					@Override
					protected String doInBackground(String... params) {
						// TODO Auto-generated method stub
						if(params[0] == null){
							return null;
						}
						
						Http http = new Http();
						List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
						httpParams.add(new BasicNameValuePair("User", params[0]));
						String res = http.doPost(Constants.URL_MODIFY_USER, httpParams);
						if(res != null){
							res = res.trim();
							//显示头像
							Context context = getActivity();
							if(context != null){
								new ImageLoadThread(context, res, ivHead, 1, cusImgHandler, CustomerImageLoadHandler.HANDLER_LOAD_IMG).start();
								app.setModifyHeadImg(false);
							}
						}
						
						return null;
					}
					
				}.execute(userName);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
		switch(v.getId()){
			case R.id.right_zhanghu://修改用户头像
				LastStatus = 1;
				if (Utils.isLogin()) {
					Fragment_Account account;
					account = new Fragment_Account();
					t.replace(R.id.id_framelayout, account);
				} else {
					Fragment_Login login= new Fragment_Login();
					t.replace(R.id.id_framelayout, login);
				}
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				((InterfaceActivity)getActivity()).showCenter();
				break;
				
			case R.id.right_outline: //下载离线
				if(offLineUtils == null){
					offLineUtils = new OffLineUtils(getActivity());
				}
				offLineUtils.start();
				break;
				
			case R.id.msg_push://消息记录
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				t.replace(R.id.id_framelayout, new Fragment_jilu());
				((InterfaceActivity)getActivity()).showCenter();
				break;
				
			case R.id.right_shouc://收藏
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				t.replace(R.id.id_framelayout, new Fragment_Collect());
				((InterfaceActivity)getActivity()).showCenter();
				break;
				
			case R.id.right_setting://设置
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				t.replace(R.id.id_framelayout, new Fragment_Setting());
				((InterfaceActivity)getActivity()).showCenter();
				break;
		
			default:
				break;
		}
		t.commit();
	}

}
