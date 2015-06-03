package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.BaseEntity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �����б�Adapter
 * 
 * @author Administrator
 */
public class TodayBrandListAdapter extends BaseAdapter {

	private Context mContext;
	/** �������ݼ� */
	private List<BaseEntity> mData = null;
	private int imgList[] = { R.drawable.smallpic12x, R.drawable.smallpic22x,
			R.drawable.smallpic32x, R.drawable.smallpic42x,
			R.drawable.smallpic52x, R.drawable.smallpic62x };

	public TodayBrandListAdapter(Context context, List<BaseEntity> data) {
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

	public View getView(int position, View view, ViewGroup parent) {
		ViewHolper viewHolper = null;
		if (view == null) {
			view = View.inflate(mContext,
					R.layout.listitem_today_news_program_name, null);
			viewHolper = new ViewHolper();

			viewHolper.mNameTextView = (TextView) view
					.findViewById(R.id.id_today_news_program_name_textivew);// ����
			viewHolper.img = (ImageView) view.findViewById(R.id.img);
			viewHolper.vBg = view.findViewById(R.id.view_bg);

			view.setTag(viewHolper);

		} else {
			viewHolper = (ViewHolper) view.getTag();
		}

		BaseEntity item = mData.get(position);// ��ǰҪ��ʾ������
		viewHolper.mNameTextView.setText(item.getName());

		viewHolper.img.setImageResource(imgList[position % imgList.length]);

		if (item.isSelected()) {
			viewHolper.vBg.setBackgroundResource(R.drawable.leftbutton_1);
		} else {
			viewHolper.vBg.setBackgroundResource(R.drawable.list_left);
		}

		return view;
	}

	/**
	 * ����ѡ�е��±�
	 * 
	 * @param position
	 *            >=0Ϊѡ�е��� <0Ϊȡ��ѡ��
	 */
	public void setSelected(int position) {
		int size = getCount();
		BaseEntity item = null;
		for (int i = 0; i < size; i++) {
			item = mData.get(i);

			if (i == position) {
				item.setSelected(true);
				continue;
			}

			item.setSelected(false);
		}
		notifyDataSetChanged();
	}

	/** ������ */
	private class ViewHolper {
		View vBg = null;
		ImageView img = null;
		TextView mNameTextView;// ����
	}
}
