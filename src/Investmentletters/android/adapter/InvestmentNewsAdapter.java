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
 * 投资快讯列表Adapter
 * @author Administrator
 *
 */
public class InvestmentNewsAdapter extends BaseAdapter{
	
	/**显示的数据*/
	private List<News> data = null;
	/**头部大图新闻数*/
	private final int HEAD_SIZE = 3;
	private Context context = null;
	/**异步加载图片，在主线程处理的Handler，如果没有图片，则不显示ImageView。一定要在主线程实例化。*/
	private NewsListImageLoadHandler handler = null;

	public InvestmentNewsAdapter(Context context, List<News> data) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
		handler = new NewsListImageLoadHandler();
	}
	
	@Override
	public int getCount() {
		final int HEAD_SIZE = 3;//头部大图新闻数
		if(data == null){
			return 0;
		}
		
		int size = data.size();
		
		if(size < HEAD_SIZE){ //小于三条，则全部在大图部份显示
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
	
	/**获取列表最后一条新闻ID*/
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
	
	/**获取列表最小的一条新闻id*/
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
		
		final int MAX_CONTENT_LENGTH = 30;//内容提要最长的字数。（两行，估计三十字左右）
		
		ViewHolper viewHolper = null;
		
		if (view == null) {
			view = View.inflate(context, R.layout.listitem_investment_news,null);
			viewHolper = new ViewHolper();

			viewHolper.mTitleTextView = (TextView) view.findViewById(R.id.id_title);//标题
			viewHolper.mContentTextView = (TextView)view.findViewById(R.id.id_content);//内要提要
			viewHolper.thumbnail_img = (MyImageView) view.findViewById(R.id.id_thumbnail_img);//缩略图

			viewHolper.thumbnail_img.setColour(Color.rgb(180, 180, 180));
			viewHolper.thumbnail_img.setBorderWidth(1);
			viewHolper.thumbnail_img.setBackgroundColor(Color.WHITE);
			viewHolper.thumbnail_img.setPadding(2, 2, 2, 2);
			view.setTag(viewHolper);
			
		}else{
			viewHolper = (ViewHolper) view.getTag();
		}

		
		News item = data.get(position+HEAD_SIZE);//当前要显示的新闻
		
		viewHolper.mTitleTextView.setText(item.getTitle());//设置标题
		//截取内容提要，给listView提高加载速度
		String content = FormatString(item.getSummary());
		System.out.println("新闻简讯："+content);
		if(content.length() > 30){
			content = content.substring(0, MAX_CONTENT_LENGTH);
		}
		viewHolper.mContentTextView.setText(content.replaceAll("###", "\n"));
		
		viewHolper.thumbnail_img.setTag(position);
		
		System.out.println("投资快报图："+context);
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

	/** 缓存类 */
	private class ViewHolper {
		private TextView mTitleTextView;//标题
		private TextView mContentTextView; //内容提要
		private MyImageView thumbnail_img; //缩略图
	}

}
