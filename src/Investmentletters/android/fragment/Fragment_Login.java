package Investmentletters.android.fragment;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.NewsShowActivity;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.model.UserModel;
import Investmentletters.android.utils.SharePreferenceUtils;
import Investmentletters.android.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

public class Fragment_Login extends Fragment implements View.OnClickListener,
		DataVisitors.CallBack {

	ImageButton showRight;
	ImageButton signin;// µÇÂ½
	ImageView showmid;
	ImageButton register;// ×¢²á
	ScrollView screen;
	private EditText view_userName;
	private EditText view_password;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mView = inflater.inflate(R.layout.page_login, container, false);

		showRight = (ImageButton) mView.findViewById(R.id.showRight);
		showRight.setOnClickListener(this);

		register = (ImageButton) mView.findViewById(R.id.register);
		register.setOnClickListener(this);

		signin = (ImageButton) mView.findViewById(R.id.signin_button);
		signin.setOnClickListener(this);

		view_userName = (EditText) mView.findViewById(R.id.username_edit);
		view_password = (EditText) mView.findViewById(R.id.password_edit);

		SharePreferenceUtils spu = new SharePreferenceUtils(getActivity());
		view_userName.setText(spu.getUserName());
		view_password.setText(spu.getPasswd());
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		InputMethodManager m = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		m.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		switch (v.getId()) {
		case R.id.showLeft:
			((InterfaceActivity) getActivity()).showLeft();
			break;
		case R.id.showRight:
			((InterfaceActivity) getActivity()).showRight();
			break;
		case R.id.signin_button:
			UserModel model = new UserModel();
			String user = view_userName.getText().toString().trim();
			String pwd = view_password.getText().toString().trim();
			model.login(user, pwd, this, 0);
			break;

		case R.id.register:
			Fragment_ReGister reg = new Fragment_ReGister();
			ft.replace(R.id.id_framelayout, reg);
			ft.addToBackStack(null);
			ft.commit();
			break;
		default:
			break;
		}

	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub

		Context context = getActivity();

		if (context == null) {
			return;
		}

		if (res == null) {
			Toast.makeText(context, "ÄúµÄÍøËÙ²»¸øÁ¦Ñ½", Toast.LENGTH_SHORT).show();
		} else {
			if ((Boolean) res) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();

				String user = view_userName.getText().toString().trim();
				String pwd = view_password.getText().toString().trim();

				SharePreferenceUtils spu = new SharePreferenceUtils(
						getActivity());
				spu.saveUserName(user);
				spu.savePasswd(pwd);

				MyApplication app = (MyApplication) getActivity()
						.getApplication();
				app.setUserName(user);
				app.setPasswd(pwd);

				Utils.setLogin(true);// ÉèÖÃÒÑµÇÂ½
				if (RightFragment.LastStatus == 1) {
					Fragment_Account account = new Fragment_Account();
					ft.replace(R.id.id_framelayout, account);
					ft.commit();
				} else if (RightFragment.LastStatus == 2) {
					Fragment_Neican neican = new Fragment_Neican();
					ft.replace(R.id.id_framelayout, neican);
					ft.commit();
					
					Activity activity = getActivity();
					Intent intent = new Intent(activity, NewsShowActivity.class);
					intent.putExtra("INDEX", Fragment_Neican.getPosition());
					app.setShowDetailDatas(Fragment_Neican.getDetailDatas());
					startActivity(intent);
				}

				RightFragment.LastStatus = 0;
				Toast.makeText(context, "µÇÂ¼³É¹¦", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "µÇÂ¼Ê§°Ü£¬Çë¼ì²éÄúµÄÍøÂçºóÖØÊÔ", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
