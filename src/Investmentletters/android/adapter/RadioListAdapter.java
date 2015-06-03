package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.BaseEntity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 广播列表Adapter
 * 
 * @author Administrator
 */
public class RadioListAdapter extends BaseAdapter {

	private Context mContext;
	/** 新闻数据集 */
	private List<BaseEntity> mData = null;

	public RadioListAdapter(Context context, List<BaseEntity> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolper viewHolper = null;

		if (view == null) {
			view = View.inflate(mContext, R.layout.radioitem_name, null);
			viewHolper = new ViewHolper();

			viewHolper.mNameTextView = (TextView) view.findViewById(R.id.id_radioitem_textivew);// 标题
			view.setTag(viewHolper);

			view.setTag(viewHolper);
		} else {
			viewHolper = (ViewHolper) view.getTag();
		}

		viewHolper.mNameTextView.setGravity(Gravity.LEFT);
		viewHolper.mNameTextView.setText(mData.get(position).getName());
		

		return view;
	}

	/** 缓存类 */
	private class ViewHolper {
		private TextView mNameTextView;// 节目
	}
}
