package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.entity.Image;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.view.ScaleImageView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 美图在线详细信息ViewPager容器adapter
 * 
 * @author liang
 */
public class ImageNewsDetailPagerAdapter extends PagerAdapter implements View.OnClickListener{

	private Context context = null;
	/** xml解析类 */
	private LayoutInflater inflater = null;
	
	private OnClickListener onClickListener = null;

	private List<Image> data = null;

	/** 异步加载图片，在主线程处理的Handler。一定要在主线程实例化。 */
	private ImageLoadHandler imgHandler = null;

	/** 屏幕宽度像素点 */
	private int screenWidth = 0;

	public ImageNewsDetailPagerAdapter(Context context, List<Image> data) {
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		imgHandler = new ImageLoadHandler();

		screenWidth = ((MyApplication) context.getApplicationContext()).getScreenWidth();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (data == null) {
			return 0;
		}

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

		View view = inflater.inflate(R.layout.image_news_detail_pager_adapter,null);

		ScaleImageView ivImg = (ScaleImageView) view.findViewById(R.id.img);// 图片
		ivImg.setScale(false);
		ivImg.setOnClickListener(this);
		
		TextView tvContent = (TextView) view.findViewById(R.id.content);// 内容
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

		System.out.println("在:instantiateItem()");
		
		/*
		 * 设置图片为满宽。原图为190:320 约为 1:1.68
		 */
		ivImg.setMinimumWidth(screenWidth);
		ivImg.setMaxWidth(screenWidth);
		int height = (int) ((float) screenWidth / 1.68F);
		ivImg.setMinimumHeight(height);
		ivImg.setMaxHeight(height);

		Image item = data.get(position); 
		// 加载图片
		new ImageLoadThread(context, item.getImg(), ivImg, position,
				imgHandler, ImageLoadHandler.HANDLER_LOAD_IMG).start();

		String content = item.getContent();
		content = content.replaceAll("###", "\n\t\t");
		content = "\t\t" + content;
		tvContent.setText(content);

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
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(onClickListener != null){
			onClickListener.onClick(v);
		}
	}
	
}
