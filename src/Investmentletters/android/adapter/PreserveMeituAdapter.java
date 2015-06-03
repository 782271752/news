package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.view.PreserveCheckBox;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 收藏夹美图在线Adapter
 * @author liang
 */
public class PreserveMeituAdapter extends BaseAdapter {
	
	private LayoutInflater inflater = null;
	/** 数据 */
	private List<News> data = null;
	/** 异步加载图片，在主线程处理的Handler。一定要在主线程实例化。 */
	private ImageLoadHandler handler = null;
	private Context context = null;
	
	/**是否为编辑模式*/
	private boolean isEditMode = false;

	public PreserveMeituAdapter(Context context, List<News> data) {
		this.context = context;
		this.data = data;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		handler = new ImageLoadHandler();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	/**获取列表最新的新闻Id*/
	public int getLatestId() {
		int id = -1;
		int tempId = 0;
		for (News temp:data) {
			tempId = temp.getId();
			if (id < tempId ) {
				id = tempId;
			}
		}
		return id;
	}
	
	public int getMinId(){
		int id = -1;
		int tempId = 0;
		for (News temp:data ) {
			 tempId = temp.getId();
			 if (id == -1) {
				 id = tempId;
				 continue;
			 }
			 if (id > tempId ) {
				 id = tempId;
			 }
		}
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.preserve_meitu_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.checkBox = (PreserveCheckBox)convertView.findViewById(R.id.check_box);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		News model = data.get(position);
		
		if(isEditMode){
			viewHolder.checkBox.setVisibility(View.VISIBLE);
			viewHolder.checkBox.setChecked(model.isSelect());
		}else{
			viewHolder.checkBox.setVisibility(View.GONE);
		}
		
		viewHolder.title.setText(model.getTitle());
		String img = model.getMinImg();
		if (img != null) {
			if (!"".equals(img.trim())) {
				new ImageLoadThread(context, img, viewHolder.img, position,
						handler, ImageLoadHandler.HANDLER_LOAD_IMG).start();
			} else {
				viewHolder.img.setImageResource(R.drawable.bg_listitem_big);
			}
		} else {// 没图
			viewHolder.img.setImageResource(R.drawable.bg_listitem_big);
		}

		return convertView;
	}
	
	/**
	 * 设置是否为编辑模式
	 * @param enable true:是  false:否
	 */
	public void setEditableMode(boolean enable){
		isEditMode = enable;
		super.notifyDataSetChanged();
	}

	/** 缓存类 */
	private class ViewHolder {
		/**复选框*/
		public PreserveCheckBox checkBox = null;
		/** 图片 */
		public ImageView img = null;
		/** 标题 */
		private TextView title = null;
	}

}
