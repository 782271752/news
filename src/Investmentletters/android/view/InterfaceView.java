package Investmentletters.android.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 左右划动组件中间部份。关键
 * 
 * @author Administrator
 */
public class InterfaceView extends ViewGroup {

	private FrameLayout mContainer;
	private Scroller mScroller;
	/**左边框*/
	private View mMenuView;
	/**右边框*/
	private View mDetailView;

	/** 移动时的遮罩层，用于点击恢复 */
	private LinearLayout coverLayout = null;

	public InterfaceView(Context context) {
		super(context);
		init();
	}

	public InterfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InterfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContainer.layout(0, 0, width, height);
	}

	private void init() {
		mContainer = new FrameLayout(getContext());
		mContainer.setBackgroundColor(0xff000000);
		mScroller = new Scroller(getContext());
		super.addView(mContainer);

		coverLayout = new LinearLayout(getContext());
		coverLayout.setBackgroundColor(Color.argb(0, 0, 0, 0));
		coverLayout.setVisibility(View.GONE);
		coverLayout.setOnTouchListener(new OnTouchListener() {// 恢复
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						showCenter();//显示中间界面
						return true;
					}
				});
	}

	public void setView(View v) {
		if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		mContainer.addView(v);

		mContainer.addView(coverLayout);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		if (x == 0) {
			coverLayout.setVisibility(View.GONE);
			
			if(mMenuView != null){
				mMenuView.setVisibility(View.GONE);	
			}
			
			if(mDetailView != null){
				mDetailView.setVisibility(View.GONE);
			}
		} else {
			coverLayout.setVisibility(View.VISIBLE);
			
			if(getScrollX()>0){
				if(mMenuView != null){
					mMenuView.setVisibility(View.GONE);	
				}
				
				if(mDetailView != null){
					mDetailView.setVisibility(View.VISIBLE);
				}
			}else{
				if(mMenuView != null){
					mMenuView.setVisibility(View.VISIBLE);	
				}
				
				if(mDetailView != null){
					mDetailView.setVisibility(View.GONE);
				}
			}
			
		}
	}

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
	}

	public View getDetailView() {
		return mDetailView;
	}

	public void setDetailView(View mDetailView) {
		this.mDetailView = mDetailView;
	}

	public View getMenuView() {
		return mMenuView;
	}

	public void setMenuView(View mMenuView) {
		this.mMenuView = mMenuView;
	}

	public void showLeftView() {
		
//		if(mDetailView != null){
//			mDetailView.setVisibility(View.GONE);
//		}
		
		mMenuView.setVisibility(View.VISIBLE);
		
		int menuWidth = mMenuView.getWidth();
		int oldScrollX = getScrollX();
		if (oldScrollX == 0) {
			smoothScrollTo(-menuWidth);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
		} else {
			smoothScrollTo(-oldScrollX);
		}

	}

	public int getscroll() {
		int oldScrollX = getScrollX();
		return oldScrollX;
	}

	public void showRightView() {
		
//		if(mMenuView != null){
//			mMenuView.setVisibility(View.GONE);
//		}
		
		mDetailView.setVisibility(View.VISIBLE);
		
		int menuWidth = mDetailView.getWidth();
		int oldScrollX = getScrollX();
		
		if (oldScrollX == 0) {
			smoothScrollTo(menuWidth);
		} else if (oldScrollX == menuWidth) {
			smoothScrollTo(-menuWidth);
		} else {
			smoothScrollTo(-oldScrollX);
		}
	}

	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = getScrollX();
		mScroller.startScroll(oldScrollX, getScrollY(), dx, getScrollY(),
				duration);
		invalidate();

		if (getScrollX() == 0) {
			coverLayout.setVisibility(View.GONE);
			
			if(mMenuView != null){
				mMenuView.setVisibility(View.GONE);	
			}
			
			if(mDetailView != null){
				mDetailView.setVisibility(View.GONE);
			}
		} else {
			coverLayout.setVisibility(View.VISIBLE);
			
			if(getScrollX()>0){
				if(mMenuView != null){
					mMenuView.setVisibility(View.GONE);	
				}
				
				if(mDetailView != null){
					mDetailView.setVisibility(View.VISIBLE);
				}
			}else{
				if(mMenuView != null){
					mMenuView.setVisibility(View.VISIBLE);	
				}
				
				if(mDetailView != null){
					mDetailView.setVisibility(View.GONE);
				}
			}
			
		}
		
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}
	
	/**
	 * 显示中间界面
	 */
	public void showCenter(){
		smoothScrollTo(-getScrollX());
		if(mMenuView != null){
//			mMenuView.setVisibility(View.GONE);	
		}
		
		if(mDetailView != null){
//			mDetailView.setVisibility(View.GONE);
		}
	}
	
	

}
