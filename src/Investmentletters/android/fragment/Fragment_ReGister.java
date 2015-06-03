package Investmentletters.android.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Investmentletters.android.R;
import Investmentletters.android.dao.Register;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.model.UserModel;
import Investmentletters.android.utils.SharePreferenceUtils;
import Investmentletters.android.utils.Utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint({ "ShowToast", "Recycle" })
public class Fragment_ReGister extends Fragment implements
		View.OnClickListener, DataVisitors.CallBack {
	ImageButton showLeft;
	Button Submit;
	EditText UserName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.page_register, container, false);

		showLeft = (ImageButton) mView.findViewById(R.id.showLeft);
		showLeft.setOnClickListener(this);

		Submit = (Button) mView.findViewById(R.id.registerSubmit);
		Submit.setOnClickListener(this);

		UserName = (EditText) mView.findViewById(R.id.registerUserName);
		return mView;
	}

	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub

		InputMethodManager m = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.showLeft:
			m.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment_Login Login = new Fragment_Login();
			ft.replace(R.id.id_framelayout, Login);
			ft.commit();
			break;
		case R.id.registerSubmit:
			System.out.println("registerSubmit");
			m.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			String user = UserName.getText().toString().trim();
			UserModel model = new UserModel();
			model.register(user, this, 0);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		Context context = getActivity();
		if(context == null){
			return;
		}
		
		if (res == null){
			Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			return;
		}
		
		switch ((Integer) res) {
			case Register.OK:
				Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
				Utils.setLogin(true);
				String user = UserName.getText().toString().trim();
				
				//保存手机密码
				SharePreferenceUtils spu = new SharePreferenceUtils(getActivity());
				spu.saveUserName(user);
				String pwd = user.substring(7, 11);//后四位
				spu.savePasswd(pwd);
				
				FragmentTransaction ft =getFragmentManager().beginTransaction();
				if(RightFragment.LastStatus ==1){//个人账户
					Fragment_Account account = new Fragment_Account();
					ft.replace(R.id.id_framelayout, account);
				}else if(RightFragment.LastStatus ==2){//投资内参
					Fragment_Kuaibao kuaibao = new Fragment_Kuaibao();
					ft.replace(R.id.id_framelayout, kuaibao);
				}

				RightFragment.LastStatus = 0;
				break;
			case Register.FAILURE:
				Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
				break;
			case Register.REPEAT:
				Toast.makeText(context, "用户已存在", Toast.LENGTH_SHORT).show();
				break;

		}
	}
}
