package Investmentletters.android.fragment;


import Investmentletters.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
public class Fragment_About extends Fragment {
	ImageButton showLeft;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mView = inflater.inflate(R.layout.page_about, container, false);
		showLeft = (ImageButton) mView.findViewById(R.id.showLeft);

	//	StatService.setSessionTimeOut(100);
	//	StatService.setOn(getActivity(), StatService.EXCEPTION_LOG);
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				Fragment_Setting set = new Fragment_Setting();
				ft.replace(R.id.id_framelayout, set);
				ft.commit();
			}
		});
	}
}
