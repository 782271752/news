package Investmentletters.android.fragment;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.DBHelper;
import Investmentletters.android.utils.OffLineUtils;
import Investmentletters.android.utils.SharePreferenceUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class Fragment_Setting extends Fragment implements View.OnClickListener,OnCheckedChangeListener{
	
	/**是否推送消息复选框*/
	private CheckBox cbPushMessage;
	
	private ProgressDialog progressDialog = null;
	/**离线加载线程类*/
	private OffLineUtils offLineUtils = null;
	/**SharedPreferences工具类*/
	private SharePreferenceUtils spu = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Context context = getActivity();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("请稍候");
		
		offLineUtils = new OffLineUtils(context);
		spu = new SharePreferenceUtils(context);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_setting, null);

		view.findViewById(R.id.showRight).setOnClickListener(this);//显示右边框菜单
		view.findViewById(R.id.preserve_article).setOnClickListener(this);//文章收藏
		view.findViewById(R.id.preserve_img).setOnClickListener(this);//图片收藏
		view.findViewById(R.id.button6).setOnClickListener(this);//用户反馈
		view.findViewById(R.id.button7).setOnClickListener(this); //关于
		view.findViewById(R.id.clean_cache).setOnClickListener(this);//清除缓存
		view.findViewById(R.id.off_line).setOnClickListener(this);//离线加载
		view.findViewById(R.id.push_settting_frame).setOnClickListener(this);//是否推送消息框
		
		cbPushMessage = (CheckBox) view.findViewById(R.id.checkBox1);//是否推送复选框
		cbPushMessage.setChecked(spu.getPushState());//设置原来推送状态
		cbPushMessage.setOnCheckedChangeListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.showRight://显示右边框菜单
				((InterfaceActivity) getActivity()).showRight();
				break;
				
			case R.id.preserve_article://文章收藏
				FragmentTransaction fta = getFragmentManager().beginTransaction();
				Fragment_Collect preserveArticle = new Fragment_Collect();
				fta.replace(R.id.id_framelayout, preserveArticle);
				fta.commit();
				break;
				
			case R.id.preserve_img: //图片收藏
				FragmentTransaction ftp = getFragmentManager().beginTransaction();
				Fragment_Collect preserveImg = new Fragment_Collect();
				preserveImg.setIsInitShowArticle(false);
				ftp.replace(R.id.id_framelayout, preserveImg);
				ftp.commit();
				break;
				
			case R.id.button6://用户反馈
				FragmentTransaction ftf = getFragmentManager().beginTransaction();
				Fragment_Feedback feedback = new Fragment_Feedback();
				ftf.replace(R.id.id_framelayout, feedback);
				ftf.commit();
				break;
				
			case R.id.button7: //关于
				FragmentTransaction ftabout = getFragmentManager().beginTransaction();
				Fragment_About about = new Fragment_About();
				ftabout.replace(R.id.id_framelayout, about);
				ftabout.commit();
				break;
				
			case R.id.clean_cache: //清除缓存
				
				progressDialog.show();
				new AsyncTask<Integer, Integer, Boolean>(){
					
					protected void onPreExecute() {
						progressDialog.show();
					};

					@Override
					protected Boolean doInBackground(Integer... params) {
						// TODO Auto-generated method stub
						return DBHelper.getInstance(getActivity()).cleanCache();
					}
					
					@Override
					protected void onPostExecute(Boolean result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);
						progressDialog.dismiss();
						Toast.makeText(getActivity(), "清理完成", Toast.LENGTH_SHORT).show();
					}
				}.execute();
				
				break;
				
			case R.id.off_line: //离线加载
				offLineUtils.start();
				break;
				
			case R.id.push_settting_frame://是否推送消息
				cbPushMessage.setChecked(!cbPushMessage.isChecked());
				break;
				
			default:
				break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		Context context = getActivity();
		spu.savePushState(isChecked);
		if(isChecked){//设置为推送
			if(!PushManager.isPushEnabled(context)){
				if(!PushManager.isPushEnabled(context) && !PushManager.isConnected(context)){
					System.out.println("开始设置可推送");
					PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_PUSH_APP_ID);				
				}
			}
		}else{//设置为不推送
			System.out.println("不可推送");
			PushManager.stopWork(context);
		}
	}

	
	

}
