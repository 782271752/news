package Investmentletters.android.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * ����TextView
 * @author liang
 */
public class ScrollText extends View {
	
	/**Ҫ��ʾ���ı�*/
	private String str = null;
	
	private Paint paint = null;
	private Handler handler = null;
	
	/**�����߳�*/
	private RunThread runThread = null;
	
	/**��һ�����е�xλ��*/
	private float preX = 0.0F;
	
	/**�״�����*/
	private boolean isFirstRun = true;
	
	private int dpi = 160;
	
	public ScrollText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public ScrollText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public ScrollText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		
		if(runThread != null){
			runThread.disabled();
		}
		
		runThread = new RunThread();
		runThread.start();
		
	}
	
	@SuppressLint("HandlerLeak")
	private void init(Context context){
		paint = new Paint();
		paint.setColor(Color.rgb(99, 99, 99));
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(20.0F);
		paint.setAntiAlias(true);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				postInvalidate();
			}
		};
		
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		dpi = dm.densityDpi;
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(str == null){
			return;
		}
		
		float strLen = paint.measureText(str);
		int width = getWidth();
		
		if(isFirstRun){
			isFirstRun = false;
			preX = width;
		}
		
		if(preX < (-strLen)){
			preX = width; 
		}
		canvas.drawText(str, preX, paint.getTextSize(), paint);
	}
	
	
	private class RunThread extends Thread{
		
		private boolean enable = true;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(enable){
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				preX -= 7.0F;
				handler.sendEmptyMessage(0);
			}
		}
		
		public void disabled(){
			enable = false;
		}
	}
	
	public void setText(String str){
		this.str = str;
	}
	
	/**
	 * �����ı���С
	 * @param textSize ��С����λsp
	 */
	public void setTextSize(float textSize){
		paint.setTextSize(textSize* dpi / 160);
	}
	
	/**
	 * �����ı�ɫ
	 * @param color Color.rgb()�����
	 */
	public void setTextColor(int color){
		paint.setColor(color);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		if(runThread != null){
			runThread.disabled();
		}
	}
	

}
