package Investmentletters.android.fragment;

import java.util.ArrayList;

import Investmentletters.android.R;
import Investmentletters.android.adapter.FeedbackAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 意见反馈
 * @author Administrator
 */
public class Fragment_Feedback extends Fragment implements OnClickListener{
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ArrayList<View> aViews = new ArrayList<View>();
		
		View mView = inflater.inflate(R.layout.feedback, container, false);

		mView.findViewById(R.id.back).setOnClickListener(this);//返回

		View v1 = View.inflate(getActivity(), R.layout.page_feedback0, null);
		View v2 = View.inflate(getActivity(), R.layout.page_feedback1, null);
		View v3 = View.inflate(getActivity(), R.layout.page_feedback2, null);
		View v4 = View.inflate(getActivity(), R.layout.page_feedback3, null);
		View v5 = View.inflate(getActivity(), R.layout.page_feedback4, null);
		View v6 = View.inflate(getActivity(), R.layout.page_feedback5, null);
		aViews.add(v1);
		aViews.add(v2);
		aViews.add(v3);
		aViews.add(v4);
		aViews.add(v5);
		aViews.add(v6);
		
		FeedbackAdapter adapter=new FeedbackAdapter(getActivity(),aViews);
		((ViewPager) mView.findViewById(R.id.vPager)).setAdapter(adapter);
		  
		return mView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back://返回
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment_Setting set = new Fragment_Setting();
				ft.replace(R.id.id_framelayout, set);
				ft.commit();
				break;
		}
	}
}
