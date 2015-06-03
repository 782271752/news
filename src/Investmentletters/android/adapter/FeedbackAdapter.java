package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.fragment.Fragment_Setting;
import Investmentletters.android.model.FeedBackModel;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 详细新闻ViewPager容器adapter
 * 
 * @author liang
 */
public class FeedbackAdapter extends PagerAdapter implements View.OnClickListener,DataVisitors.CallBack,RadioGroup.OnCheckedChangeListener{
	
	private FragmentActivity activity;
	private EditText feedback_tx = null;
	private RadioGroup radiogroup;
	private String feedback[] = new String[6];
	private Button feedback_btn;
	
	private ProgressDialog progressDialog = null;

	/** 新闻列表 */
	private List<View> data = null;

	public FeedbackAdapter(FragmentActivity activity, List<View> data) {
		this.activity=activity;
		this.data = data;

		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("正在提交");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == (View) obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub

		View view = data.get(position);

		if(position<5){
			radiogroup = (RadioGroup) view.findViewById(R.id.radioGroup);
			radiogroup.setTag(position);
			radiogroup.setOnCheckedChangeListener(this);
		}else{
			feedback_tx = (EditText)view.findViewById(R.id.feedback_text);
			feedback_btn = (Button)view.findViewById(R.id.feedback_sub);
			feedback_btn.setOnClickListener(this);
		}
		ViewPager pager = (ViewPager) container;
		pager.addView(view);
		
		return view;
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;// 每次刷新界面
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// 防止界面重叠
		ViewPager viewPager = (ViewPager) container;
		viewPager.removeView((View) object);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		progressDialog.show();
		FeedBackModel model =  new FeedBackModel();
		feedback[5] = feedback_tx.getText().toString().trim();
		model.feedBack(feedback[5], feedback[0], feedback[1], feedback[2], feedback[3], feedback[4], null, this, 0);
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		if(res == null){
			Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT).show();
		} else {
			if ((Boolean) res) {
				FragmentTransaction ft =activity.getSupportFragmentManager().beginTransaction();
				Fragment_Setting set = new Fragment_Setting();
				ft.replace(R.id.id_framelayout, set);
				ft.commit();
				Toast.makeText(activity, "感谢您的支持", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(activity, "提交失败，请检查您的网络后重试", Toast.LENGTH_SHORT).show();
			}
		}
		progressDialog.dismiss();
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int count = group.getChildCount();
		RadioButton rb = null;
		String value = "none";
		for(int i=0 ; i<count ; i++){
			rb = (RadioButton)group.getChildAt(i);
			if(rb.getId() == checkedId){
				value = rb.getText().toString().trim();
			}
		}
		
		int index = (Integer)group.getTag();
		feedback[index] = value;
		
		System.out.println("结果："+value);
	}
}
