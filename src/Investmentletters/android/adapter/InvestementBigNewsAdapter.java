package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.NewsShowActivity;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.utils.ImageLoadThread;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * Ͷ�ʿ�Ѷ�б�ͷͼ��ͼ��ViewPager����adapter
 * @author liang
 */
public class InvestementBigNewsAdapter extends PagerAdapter implements View.OnClickListener {
	
	/**�����б�*/
	private List<News> data = null;
	
	/**�첽����ͼƬ�������̴߳����Handler��һ��Ҫ�����߳�ʵ������*/
	private ImageLoadHandler imgHandler = null;
	
	private Context context = null;
	
	/**
	 * ����
	 * @param context
	 * @param data �����б�
	 */
	public InvestementBigNewsAdapter(Context context , List<News> data){
		this.data = data;
		this.context = context;
		
		imgHandler = new ImageLoadHandler();
	}
	

	@Override
	public int getCount() {//�����ʾ��������
		// TODO Auto-generated method stub
		if(data == null){
			return 0;
		}
		
		int size = data.size();
		if(size<3){
			return size;
		}
		
		return 3;
	}
	
	public int getLatestId() {
		int id = 0;
		int tempId = 0;
		for (News temp:data) {
			tempId = temp.getId();
			if (id < tempId ) {
				id = tempId;
			}
		}
		return id;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == (View)obj;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		
		ImageView ivImg = new ImageView(context);
		ivImg.setScaleType(ScaleType.FIT_XY);
		
		News param = data.get(position);
		ivImg.setScaleType(ImageView.ScaleType.FIT_XY);
		ivImg.setAdjustViewBounds(false);
		
		ivImg.setImageResource(R.drawable.bg_listitem_thumbnail);//Ĭ����ʾͼƬ
		
		System.out.println("��ͼ����adateper:"+context);
		//����ͼƬ
		System.out.println("bigimage_url = " + param.getBigImg());
		new ImageLoadThread(context,
							param.getBigImg(), 
							ivImg, 
							position, 
							imgHandler, 
							ImageLoadHandler.HANDLER_LOAD_IMG).start();
		
		ViewPager pager = (ViewPager)container;
		pager.addView(ivImg);
		
		ivImg.setOnClickListener(this);
		
		return ivImg;
	}
	
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;//ÿ��ˢ�½���
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//��ֹ�����ص�
		ViewPager viewPager = (ViewPager)container;
		viewPager.removeView((View)object);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer)v.getTag();
		Intent intent = new Intent(context, NewsShowActivity.class);
		MyApplication app = (MyApplication)context.getApplicationContext();
		intent.putExtra("INDEX", position);
		app.setShowDetailDatas(data);
		
		context.startActivity(intent);
	}
}
