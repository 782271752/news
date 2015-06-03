package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.NewsListImageLoadHandler;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.MyImageView;
import Investmentletters.android.view.PreserveCheckBox;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * �ղؼ�����adapter
 * @author liang
 */
public class PreserveNewsAdapter extends BaseAdapter {

	private Context mContext;
	/**�������ݼ�*/
	private List<News> mData = null;
	/**�첽����ͼƬ�������̴߳����Handler�����û��ͼƬ������ʾImageView��һ��Ҫ�����߳�ʵ������*/
	private NewsListImageLoadHandler handler = null;
	
	/**�Ƿ�Ϊ�༭״̬�������Ƿ���ʾ��ѡ��*/
	private boolean isEditMode = false;
	
	/**
	 * ����
	 * @param context 
	 * @param data �������ݼ�
	 * @param isTouzi �Ƿ�ΪͶ���б�����Ͷ���б����һ����ͼ���Ҳ���listview�У���Ҫ���⴦����ʾ���������л�ʱ�õ�������Ҫ��ʾ��ͼ���š�
	 */
	public PreserveNewsAdapter(Context context,List<News> data) {
		mContext = context;
		mData = data;
		handler = new NewsListImageLoadHandler();
	}

	@Override
	public int getCount() {
		if(mData == null){
			return 0;
		}
		return mData.size() ;
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
	 * ��ȡ��ǰ�����б������µ�һ������id
	 * @return -1:���б�
	 */
	public int getLatestId() {
		if(mData == null){
			return -1;
		}
		
		if(mData.size()<1){
			return -1;
		}
		
		int id = -1;
		int tempId = 0;
		for (News item:mData) {
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
		for (News temp:mData) {
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
			view = View.inflate(mContext, R.layout.preserve_new_adapter,null);
			viewHolper = new ViewHolper();

			viewHolper.checkBox = (PreserveCheckBox)view.findViewById(R.id.check_box);
			viewHolper.mTitleTextView = (TextView) view.findViewById(R.id.id_title);//����
			viewHolper.mContentTextView = (TextView)view.findViewById(R.id.id_content);//��Ҫ��Ҫ
			viewHolper.thumbnail_img = (MyImageView) view.findViewById(R.id.id_thumbnail_img);//����ͼ

			viewHolper.thumbnail_img.setColour(Color.rgb(180, 180, 180));
			viewHolper.thumbnail_img.setBorderWidth(1);
			viewHolper.thumbnail_img.setBackgroundColor(Color.WHITE);
			viewHolper.thumbnail_img.setPadding(2, 2, 2, 2);
			view.setTag(viewHolper);
			
		}else{
			viewHolper = (ViewHolper) view.getTag();
		}

		
		News item = mData.get(position);//��ǰҪ��ʾ������
		
		if(isEditMode){//�༭״̬
			viewHolper.checkBox.setVisibility(View.VISIBLE);
			viewHolper.checkBox.setChecked(item.isSelect());
		}else{
			viewHolper.checkBox.setVisibility(View.GONE);
		}
		
		viewHolper.mTitleTextView.setText(item.getTitle());//���ñ���
		viewHolper.mContentTextView.setText(item.getSummary().replaceAll("&ldquo;", "��").replaceAll("&rdquo;", "��"));
		
		viewHolper.thumbnail_img.setTag(position);
		
		new ImageLoadThread(mContext,
							item.getMinImg(), 
							viewHolper.thumbnail_img, 
							position, 
							handler, 
							NewsListImageLoadHandler.HANDLER_LOAD_IMG).start();
		
		return view;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		if(mData != null){
			Utils.SortTopMaxToMin(mData);//�������б����ݽ��д��ö�=����=��������
		}
		
		super.notifyDataSetChanged();
	}
	
	/**
	 * �����Ƿ�Ϊ�༭ģʽ
	 * @param enable true:��  false:��
	 */
	public void setEditableMode(boolean enable){
		isEditMode = enable;
		super.notifyDataSetChanged();
	}

	/**������*/
	private class ViewHolper {
		private PreserveCheckBox checkBox = null;//��ѡ��
		private TextView mTitleTextView;//����
		private TextView mContentTextView; //������Ҫ
		private MyImageView thumbnail_img; //����ͼ
	}
}
