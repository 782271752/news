package Investmentletters.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 首页投资列表头部大图新闻ViewPager。用于解决划动冲突。<br/>
 * <strong>解决与ListView 冲突思路：</strong>按ios，以首次划动方向为动作原型。
 * @author liang
 */
public class IndexInvestmentViewPager extends ViewPager {
	
	private float preX = -1.0F;
	private float preY = -1.0F;
	
	private boolean isInit = false;
	/**是否可滚动*/
	private boolean isScrollEnable = false;
	
	public IndexInvestmentViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public IndexInvestmentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch(action){
			case MotionEvent.ACTION_DOWN:
				preX = event.getX();
				preY = event.getY();
				System.out.println("在onInterxxx():按下");
				onTouchEvent(event);
				isInit = false;
				isScrollEnable = false;
				break;
				
			case MotionEvent.ACTION_MOVE:
				System.out.println("在onInterxxx():移动");		
				if(!isInit){
					float x = event.getX();
					float y = event.getY();
					
					if(Math.abs(x-preX) > Math.abs(y-preY)){
						isScrollEnable = true;
					}
					
					isInit = true;
				}
				
				if(isScrollEnable){
					getParent().requestDisallowInterceptTouchEvent(true);
					super.onTouchEvent(event);
					return true;
				}
					
				getParent().requestDisallowInterceptTouchEvent(false);					
				
				break;
							
			case MotionEvent.ACTION_UP:
				System.out.println("在onInterxxx():放开");
				onTouchEvent(event);
				break;
	
			case MotionEvent.ACTION_CANCEL:
				System.out.println("在onInterxxx():取消");
				onTouchEvent(event);
				break;
				
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch(action){
			case MotionEvent.ACTION_DOWN:
				System.out.println("在onTouchEvent():按下");
				super.onTouchEvent(event);
				break;
				
			case MotionEvent.ACTION_MOVE:
				System.out.println("在onTouchEvent():移动");
				if(isScrollEnable){
					System.out.println("可移动");
					super.onTouchEvent(event);		
					getParent().requestDisallowInterceptTouchEvent(true);
				}else{
					getParent().requestDisallowInterceptTouchEvent(false);
					System.out.println("不能移动");
				}
				break;
							
			case MotionEvent.ACTION_UP:
				System.out.println("在onTouchEvent():放开");
				super.onTouchEvent(event);
				getParent().requestDisallowInterceptTouchEvent(false);
				
				preX = -1.0F;
				preY = -1.0F;
				isInit = false;
				isScrollEnable = false;
				break;
	
			case MotionEvent.ACTION_CANCEL:
				System.out.println("在onTouchEvent():取消");
				getParent().requestDisallowInterceptTouchEvent(false);
				super.onTouchEvent(event);
				
				preX = -1.0F;
				preY = -1.0F;
				isInit = false;
				isScrollEnable = false;
				break;
	
		}
		return true;
	}
	
	

}
