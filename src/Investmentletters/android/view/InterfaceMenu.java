package Investmentletters.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 左右划动组件
 * @author Administrator
 */
public class InterfaceMenu extends RelativeLayout {

	/**中间部件*/
	private InterfaceView mSlidingView = null;
	/**左边部件*/
	private View mMenuView = null;
	/**右边部件*/
	private View mDetailView = null;

	public InterfaceMenu(Context context) {
		super(context);
	}

	public InterfaceMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterfaceMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addViews(View left, View center, View right) {
		setLeftView(left);
		setRightView(right);
		setCenterView(center);
	}

	public void setLeftView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		addView(view, behindParams);
		mMenuView = view;
//		mMenuView.setVisibility(View.GONE);
	}

	public void setRightView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(view, behindParams);
		mDetailView = view;
//		mDetailView.setVisibility(View.GONE);
	}

	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mSlidingView = new InterfaceView(getContext());
		addView(mSlidingView, aboveParams);
		mSlidingView.setView(view);
		mSlidingView.invalidate();
		mSlidingView.setMenuView(mMenuView);
		mSlidingView.setDetailView(mDetailView);
	}

	public void showLeftView() {
		mSlidingView.showLeftView();
	}
	
	public int getscroll() {
		return mSlidingView.getscroll();
	}
	
	public void showRightView() {
		mSlidingView.showRightView();
	}
	
	public void showCenterView(){
		mSlidingView.showCenter();
	}
	
	
}
