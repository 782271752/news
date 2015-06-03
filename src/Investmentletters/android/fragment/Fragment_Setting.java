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
	
	/**�Ƿ�������Ϣ��ѡ��*/
	private CheckBox cbPushMessage;
	
	private ProgressDialog progressDialog = null;
	/**���߼����߳���*/
	private OffLineUtils offLineUtils = null;
	/**SharedPreferences������*/
	private SharePreferenceUtils spu = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Context context = getActivity();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("���Ժ�");
		
		offLineUtils = new OffLineUtils(context);
		spu = new SharePreferenceUtils(context);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_setting, null);

		view.findViewById(R.id.showRight).setOnClickListener(this);//��ʾ�ұ߿�˵�
		view.findViewById(R.id.preserve_article).setOnClickListener(this);//�����ղ�
		view.findViewById(R.id.preserve_img).setOnClickListener(this);//ͼƬ�ղ�
		view.findViewById(R.id.button6).setOnClickListener(this);//�û�����
		view.findViewById(R.id.button7).setOnClickListener(this); //����
		view.findViewById(R.id.clean_cache).setOnClickListener(this);//�������
		view.findViewById(R.id.off_line).setOnClickListener(this);//���߼���
		view.findViewById(R.id.push_settting_frame).setOnClickListener(this);//�Ƿ�������Ϣ��
		
		cbPushMessage = (CheckBox) view.findViewById(R.id.checkBox1);//�Ƿ����͸�ѡ��
		cbPushMessage.setChecked(spu.getPushState());//����ԭ������״̬
		cbPushMessage.setOnCheckedChangeListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.showRight://��ʾ�ұ߿�˵�
				((InterfaceActivity) getActivity()).showRight();
				break;
				
			case R.id.preserve_article://�����ղ�
				FragmentTransaction fta = getFragmentManager().beginTransaction();
				Fragment_Collect preserveArticle = new Fragment_Collect();
				fta.replace(R.id.id_framelayout, preserveArticle);
				fta.commit();
				break;
				
			case R.id.preserve_img: //ͼƬ�ղ�
				FragmentTransaction ftp = getFragmentManager().beginTransaction();
				Fragment_Collect preserveImg = new Fragment_Collect();
				preserveImg.setIsInitShowArticle(false);
				ftp.replace(R.id.id_framelayout, preserveImg);
				ftp.commit();
				break;
				
			case R.id.button6://�û�����
				FragmentTransaction ftf = getFragmentManager().beginTransaction();
				Fragment_Feedback feedback = new Fragment_Feedback();
				ftf.replace(R.id.id_framelayout, feedback);
				ftf.commit();
				break;
				
			case R.id.button7: //����
				FragmentTransaction ftabout = getFragmentManager().beginTransaction();
				Fragment_About about = new Fragment_About();
				ftabout.replace(R.id.id_framelayout, about);
				ftabout.commit();
				break;
				
			case R.id.clean_cache: //�������
				
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
						Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
					}
				}.execute();
				
				break;
				
			case R.id.off_line: //���߼���
				offLineUtils.start();
				break;
				
			case R.id.push_settting_frame://�Ƿ�������Ϣ
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
		if(isChecked){//����Ϊ����
			if(!PushManager.isPushEnabled(context)){
				if(!PushManager.isPushEnabled(context) && !PushManager.isConnected(context)){
					System.out.println("��ʼ���ÿ�����");
					PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_PUSH_APP_ID);				
				}
			}
		}else{//����Ϊ������
			System.out.println("��������");
			PushManager.stopWork(context);
		}
	}

	
	

}
