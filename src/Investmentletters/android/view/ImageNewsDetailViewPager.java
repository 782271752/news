package Investmentletters.android.view;

import Investmentletters.android.adapter.ImageNewsDetailPagerAdapter;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 美图详细新闻viewPager
 * @author liang
 */
public class ImageNewsDetailViewPager extends ViewPager {
	
	/**是否可滑动*/
	private boolean isScrollAble = true;
	
	public ImageNewsDetailViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ImageNewsDetailViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		
		ImageNewsDetailPagerAdapter adapter = (ImageNewsDetailPagerAdapter)getAdapter();
		adapter.setOnClickListener(l);
	}
	

	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		if(isScrollAble){
			super.scrollTo(x, y);			
		}
	}
	
	@Override
	public void scrollBy(int x, int y) {
		// TODO Auto-generated method stub
		if(isScrollAble){
			super.scrollBy(x, y);			
		}
	}
	
	/**设置是否可滑动*/
	public void setScrollAble(boolean enable){
		isScrollAble = enable;
	}
	
	
	
	
}
