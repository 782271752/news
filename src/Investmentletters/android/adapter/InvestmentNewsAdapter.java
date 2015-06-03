package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.NewsListImageLoadHandler;
import Investmentletters.android.view.MyImageView;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Ͷ�ʿ�Ѷ�б�Adapter
 * @author Administrator
 *
 */
public class InvestmentNewsAdapter extends BaseAdapter{
	
	/**��ʾ������*/
	private List<News> data = null;
	/**ͷ����ͼ������*/
	private final int HEAD_SIZE = 3;
	private Context context = null;
	/**�첽����ͼƬ�������̴߳����Handler�����û��ͼƬ������ʾImageView��һ��Ҫ�����߳�ʵ������*/
	private NewsListImageLoadHandler handler = null;

	public InvestmentNewsAdapter(Context context, List<News> data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		handler = new NewsListImageLoadHandler();
	}
	
	@Override
	public int getCount() {
		final int HEAD_SIZE = 3;//ͷ����ͼ������
		if(data == null){
			return 0;
		}
		
		int size = data.size();
		
		if(size < HEAD_SIZE){ //С����������ȫ���ڴ�ͼ������ʾ
			return 0;
		}
		
		return size-HEAD_SIZE ;
	}

	@Override
	public Object getItem(int position) {
		return position+HEAD_SIZE;
	}

	@Override
	public long getItemId(int position) {
		return position+HEAD_SIZE;
	}
	
	/**��ȡ�б����һ������ID*/
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
	
	/**��ȡ�б���С��һ������id*/
	public int getMinId() {
		int id = -1;
		int tempId = 0;
		for (News temp:data) {
			tempId = temp.getId();
			if (id == -1) {
				id = tempId;
				continue;
			}
			if (id > tempId ){
				id = tempId;
			}
		}
		return id;
	}
	
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		final int MAX_CONTENT_LENGTH = 30;//������Ҫ��������������У�������ʮ�����ң�
		
		ViewHolper viewHolper = null;
		
		if (view == null) {
			view = View.inflate(context, R.layout.listitem_investment_news,null);
			viewHolper = new ViewHolper();

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

		
		News item = data.get(position+HEAD_SIZE);//��ǰҪ��ʾ������
		
		viewHolper.mTitleTextView.setText(item.getTitle());//���ñ���
		//��ȡ������Ҫ����listView��߼����ٶ�
		String content = FormatString(item.getSummary());
		System.out.println("���ż�Ѷ��"+content);
		if(content.length() > 30){
			content = content.substring(0, MAX_CONTENT_LENGTH);
		}
		viewHolper.mContentTextView.setText(content.replaceAll("###", "\n"));
		
		viewHolper.thumbnail_img.setTag(position);
		
		System.out.println("Ͷ�ʿ챨ͼ��"+context);
		new ImageLoadThread(context,
							item.getMinImg(), 
							viewHolper.thumbnail_img, 
							position, 
							handler, 
							NewsListImageLoadHandler.HANDLER_LOAD_IMG).start();
		
		return view;
	}

	private String FormatString(String str) {
		str = str.replace("&ldquo", "");
		str = str.replace("&rdquo", "");
		return str;
	}

	/** ������ */
	private class ViewHolper {
		private TextView mTitleTextView;//����
		private TextView mContentTextView; //������Ҫ
		private MyImageView thumbnail_img; //����ͼ
	}

}
