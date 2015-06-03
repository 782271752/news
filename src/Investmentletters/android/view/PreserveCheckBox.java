package Investmentletters.android.view;

import Investmentletters.android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义复选框
 * @author liang
 */
public class PreserveCheckBox extends View {
	
	private Paint paint = null;
	private Rect rect = null;
	/**是否选中*/
	private boolean checked = false;
	
	private Context context = null;
	
	/**选中的图像*/
	private Bitmap checkBitmap = null;
	/**未选中的图像*/
	private Bitmap unCheckBitmap = null;
	
	public PreserveCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	
	public PreserveCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	
	public PreserveCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	
	private void init(){
		paint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(rect == null){
			rect = new Rect(0, 0, getWidth(), getHeight());
		}
		
		System.out.println("复选框:"+rect);
		
		Bitmap bmp = null;
		
		if(checked){//选中
			if(checkBitmap == null){
				checkBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkbox_checked);
			}
			bmp = checkBitmap;
		}else{//未选中
			if(unCheckBitmap == null){
				unCheckBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.checkbox_un_check);
			}
			bmp = unCheckBitmap;
		}

		canvas.drawBitmap(bmp, null, rect, paint);
	}

	public boolean isChecked(){
		return checked;
	}
	
	public void setChecked(boolean checked){
		this.checked = checked;
		postInvalidate();
	}
	

}
