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
 * �ұ߲˵�
 * @author Administrator
 */
public class RightFragment extends Fragment implements OnClickListener{
	
	/**�����л���ť���챨*/
	private ImageView kuaibao;
	/**�����л���ť���㲥*/
	private ImageView guangbo;
	/**�����л���ť���ڲ�*/
	private ImageView neican;
	/**�����л���ť�����*/
	private ImageView zhengu;
	
	// ������� �������� ����loginҳ�����һ��״̬
	public static int LastStatus = 0;
	
	/**���߼����߳���*/
	private OffLineUtils offLineUtils = null;
	
	/**�û�ͷ��head_view*/
	private ImageView ivHead = null;
	/**ͷ�����handler*/
	private CustomerImageLoadHandler cusImgHandler = null;
	
	/**���ݹ�����*/
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
		
		view.findViewById(R.id.right_zhanghu).setOnClickListener(this);//�ʻ���
		
		ivHead = (ImageView)view.findViewById(R.id.head_view);//ͷ��
		ivHead.setTag(-1);
		
		view.findViewById(R.id.right_outline).setOnClickListener(this);//��������
		view.findViewById(R.id.msg_push).setOnClickListener(this);//��Ϣ��¼
		view.findViewById(R.id.right_shouc).setOnClickListener(this);//�ղ�
		view.findViewById(R.id.right_setting).setOnClickListener(this);//����

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
		if(((Integer)ivHead.getTag())<0 || app.isModifyHeadImg()){//δ�ӹ�ͷ��
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
							//��ʾͷ��
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
			case R.id.right_zhanghu://�޸��û�ͷ��
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
				
			case R.id.right_outline: //��������
				if(offLineUtils == null){
					offLineUtils = new OffLineUtils(getActivity());
				}
				offLineUtils.start();
				break;
				
			case R.id.msg_push://��Ϣ��¼
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				t.replace(R.id.id_framelayout, new Fragment_jilu());
				((InterfaceActivity)getActivity()).showCenter();
				break;
				
			case R.id.right_shouc://�ղ�
				kuaibao.setBackgroundResource(R.drawable.foot1);
				guangbo.setBackgroundResource(R.drawable.foot2);
				neican.setBackgroundResource(R.drawable.foot3);
				zhengu.setBackgroundResource(R.drawable.foot4);
				t.replace(R.id.id_framelayout, new Fragment_Collect());
				((InterfaceActivity)getActivity()).showCenter();
				break;
				
			case R.id.right_setting://����
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
