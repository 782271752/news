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
 * ��ͼ������ϸ��ϢViewPager����adapter
 * 
 * @author liang
 */
public class ImageNewsDetailPagerAdapter extends PagerAdapter implements View.OnClickListener{

	private Context context = null;
	/** xml������ */
	private LayoutInflater inflater = null;
	
	private OnClickListener onClickListener = null;

	private List<Image> data = null;

	/** �첽����ͼƬ�������̴߳����Handler��һ��Ҫ�����߳�ʵ������ */
	private ImageLoadHandler imgHandler = null;

	/** ��Ļ������ص� */
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

		ScaleImageView ivImg = (ScaleImageView) view.findViewById(R.id.img);// ͼƬ
		ivImg.setScale(false);
		ivImg.setOnClickListener(this);
		
		TextView tvContent = (TextView) view.findViewById(R.id.content);// ����
		tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

		System.out.println("��:instantiateItem()");
		
		/*
		 * ����ͼƬΪ����ԭͼΪ190:320 ԼΪ 1:1.68
		 */
		ivImg.setMinimumWidth(screenWidth);
		ivImg.setMaxWidth(screenWidth);
		int height = (int) ((float) screenWidth / 1.68F);
		ivImg.setMinimumHeight(height);
		ivImg.setMaxHeight(height);

		Image item = data.get(position); 
		// ����ͼƬ
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
		return POSITION_NONE;// ÿ��ˢ�½���
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// ��ֹ�����ص�
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
