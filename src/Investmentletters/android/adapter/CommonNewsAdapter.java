package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.NewsListImageLoadHandler;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.MyImageView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 通用新闻Adapter(投资快讯 ，公司雷达，数据解码，操盘解读、消息推送),除了投资快讯,美图在线外。
 * 
 * @author liang
 */
public class CommonNewsAdapter extends BaseAdapter {

	private Context mContext;
	/** 新闻数据集 */
	private List<News> mData = null;
	/** 异步加载图片，在主线程处理的Handler，如果没有图片，则不显示ImageView。一定要在主线程实例化。 */
	private NewsListImageLoadHandler handler = null;

	/**
	 * 构造
	 * 
	 * @param context
	 * @param data
	 *            新闻数据集
	 * @param isTouzi
	 *            是否为投资列表。由于投资列表具有一个大图，且不在listview中，需要另外处理。显示具体新闻切换时用到的又需要显示大图新闻
	 *            。
	 */
	public CommonNewsAdapter(Context context, List<News> data) {
		mContext = context;
		mData = data;
		handler = new NewsListImageLoadHandler();
	}

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取当前新闻列表中最新的一条新闻id
	 * 
	 * @return -1:空列表
	 */
	public int getLatestId() {
		if (mData == null) {
			return -1;
		}

		if (mData.size() < 1) {
			return -1;
		}

		int id = -1;
		int tempId = 0;
		for (News item : mData) {
			tempId = item.getId();
			if (id < tempId) {
				id = tempId;
			}
		}
		return id;
	}

	public int getMinId() {
		int id = -1;
		int tempId = 0;
		for (News temp : mData) {
			tempId = temp.getId();
			if (id == -1) {
				id = tempId;
				continue;
			}
			if (id > tempId) {
				id = tempId;
			}
		}
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolper viewHolper = null;

		if (view == null) {
			view = View.inflate(mContext, R.layout.listitem_investment_news,
					null);
			viewHolper = new ViewHolper();

			viewHolper.mTitleTextView = (TextView) view
					.findViewById(R.id.id_title);// 标题
			viewHolper.mContentTextView = (TextView) view
					.findViewById(R.id.id_content);// 内要提要
			viewHolper.thumbnail_img = (MyImageView) view
					.findViewById(R.id.id_thumbnail_img);// 缩略图

			viewHolper.thumbnail_img.setColour(Color.rgb(180, 180, 180));
			viewHolper.thumbnail_img.setBorderWidth(1);
			viewHolper.thumbnail_img.setBackgroundColor(Color.WHITE);
			viewHolper.thumbnail_img.setPadding(2, 2, 2, 2);
			view.setTag(viewHolper);

		} else {
			viewHolper = (ViewHolper) view.getTag();
		}

		News item = mData.get(position);// 当前要显示的新闻

		viewHolper.mTitleTextView.setText(item.getTitle());// 设置标题
		viewHolper.mContentTextView.setText(item.getSummary());

		viewHolper.thumbnail_img.setTag(position);

		new ImageLoadThread(mContext, item.getMinImg(),
				viewHolper.thumbnail_img, position, handler,
				NewsListImageLoadHandler.HANDLER_LOAD_IMG).start();

		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		if (mData != null) {
			Utils.SortTopMaxToMin(mData);// 对新闻列表数据进行从置顶=》新=》旧排序
		}

		super.notifyDataSetChanged();
	}

	/** 缓存类 */
	private class ViewHolper {
		private TextView mTitleTextView;// 标题
		private TextView mContentTextView; // 内容提要
		private MyImageView thumbnail_img; // 缩略图
	}
}
