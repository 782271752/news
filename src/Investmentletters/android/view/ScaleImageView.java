package Investmentletters.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 缩放ImageView
 * @author liang
 */
public class ScaleImageView extends ImageView {
	
	private Paint paint = null;
	
	/**可视区域宽度*/
	private int width = 0;
	/**可视区域高度*/
	private int height = 0;
	
	/**要画的图*/
	private Bitmap bitmap = null;
	
	private Rect tempRect = null;
	/**原始图片大小*/
	private Rect originRect = null;
	
	/**最大缩放倍数*/
	private final float MAX_SCALE = 1.5F;
	/**最大缩放宽度*/
	private int max_scale_width = 0;
	
	/**上一次移动平移的x坐标*/
	private float preMoveX = 0.0F;
	/**上一次移动平移的y坐标*/
	private float preMoveY = 0.0F;
	
	/**上一次缩放触点1的x坐标*/
	private float prePointOneX = 0.0F;
	/**上一次缩放触点1的y坐标*/
	private float prePointOneY = 0.0F;
	
	/**上一次缩放触点2的x坐标*/
	private float prePointTwoX = 0.0F;
	/**上一次缩放触点2的y坐标*/
	private float prePointTwoY = 0.0F;
	
	/**上一次单击时间*/
	private long preClickTime = 0L;
	/**属于点击的时间间隔*/
	private long CLICK_TIME = 1000L;
	/**是否已移动过，如果移动过，不算点击*/
	private boolean isMove = false;
	private OnClickListener onClickListener = null;
	
	/**是否已计算过*/
	private boolean isMeasuer = false;
	
	/**是否可缩放平移*/
	private boolean isScaleAble = false;
	
	public ScaleImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ScaleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		paint = new Paint();
		tempRect = new Rect();
	}
	
	/**恢复图片状态*/
	public void reset(){
		if(originRect == null){
			return;
		}
		
		tempRect.left = originRect.left;
		tempRect.right = originRect.right;
		tempRect.top = originRect.top;
		tempRect.bottom = originRect.bottom;
		
		postInvalidate();
	}
	
	/**初始化各尺寸*/
	private void initMeasure(){
		
		if(bitmap == null){
			return;
		}
		
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
		System.out.println("top:"+getTop()+"  bottom:"+getBottom()+"  left:"+getLeft()+"  right:"+getRight());
		
		int originImgWidth = 0;
		int originImgHeight = 0;
		
		int marginLeft = 0;
		int marginTop = 0;
		
		originImgWidth = bitmap.getWidth();
		originImgHeight = bitmap.getHeight();
		
		if(originImgWidth == 0){
			originImgWidth = width;
		}
		
		if(originImgHeight == 0){
			originImgHeight = height;
		}
		
		System.out.println("图片高宽：width:"+width+"  height:"+height+"  originImgWidht:"+originImgWidth+"   originImgHeight:"+originImgHeight);
		
		float ratio = (float)originImgWidth/originImgHeight;
		
		if(width >= originImgWidth && height>=originImgHeight){//图片缩放为刚满高或宽
			
			int tempHeight = (int)(width/ratio);
			
			if(tempHeight <= height){//宽度满屏
				originImgWidth = width;
				originImgHeight = tempHeight;
			}else{//高度满屏
				originImgHeight = height;
				originImgWidth = (int)(ratio * height);
			}
		}else if(width >= originImgWidth && height<originImgHeight){
			originImgWidth = (int)(height * ratio);
			originImgHeight = height;
		}else if(width < originImgWidth && height>=originImgHeight){
			originImgHeight = (int)(width / ratio);
			originImgWidth = width;
		}else{//width < originImgWidth && height<originImgHeight
			
			
			int tempHeight = (int)(width/ratio);
			
			if(tempHeight <= height){//宽度满屏
				originImgWidth = width;
				originImgHeight = tempHeight;
			}else{//高度满屏
				originImgHeight = height;
				originImgWidth = (int)(ratio * height);
			}
			
		}
		
		marginLeft = (width - originImgWidth)/2;
		marginTop = (height - originImgHeight)/2;
		originRect = new Rect(marginLeft , marginTop , originImgWidth + marginLeft , originImgHeight + marginTop);
		
		max_scale_width = (int)(originImgWidth * MAX_SCALE);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		if(bm == null){
			return;
		}
		bitmap = bm;
		postInvalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(bitmap == null){
			System.out.println("无bitmap");
			return;
		}
		
		if(!isMeasuer){
			isMeasuer = true;
			initMeasure();
			reset();
		}
		
		canvas.drawARGB(255, 0, 0, 0);//画复盖
		
		canvas.drawBitmap(bitmap, null , tempRect, paint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int pointCount = event.getPointerCount();//触点数量
		
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		
		switch(action){
			case MotionEvent.ACTION_DOWN:
				preClickTime = System.currentTimeMillis();
				isMove = false;
				break;
		
			case MotionEvent.ACTION_MOVE:
				if(!isScaleAble){
					return true;
				}
				
				if(pointCount == 1){//单指
					System.out.println("单指:移动");
					
					if(preMoveX<1){
						preMoveX = event.getX();
						preMoveY = event.getY();
						System.out.println("初次，初始化数据...");
						return true;
					}
					
					float x = event.getX();
					float y = event.getY();
					
					int diffX = (int)(preMoveX - x);
					int diffY = (int)(preMoveY - y);
					
					if(diffX > 10 || diffY > 10){
						isMove = true;
					}
					
					
					preMoveX = x;
					preMoveY = y;
					
					tempRect.left -= diffX;
					tempRect.right -= diffX;
					
					tempRect.top -= diffY;
					tempRect.bottom -= diffY;
					
					//保证不被移出边界
					int tempWidth = tempRect.right - tempRect.left;//现图片宽度
					int tempHeight = tempRect.bottom - tempRect.top;//现图片高度
					
					if(tempRect.left < 0){ //保证不向左边移出边界
						if(Math.abs(tempRect.left) > (width - 20)){
							tempRect.left = 0 - width + 20;
							tempRect.right = tempRect.left + tempWidth;
						}
					}else{//保证不向右边移出
						int rightBundler = width + width - 20;
						if(tempRect.right > rightBundler){
							tempRect.left = width - 20;
							tempRect.right = tempRect.left + tempWidth;
						}
					}
					
					if(tempRect.top < 0){//保证不向上移出边界
						if(Math.abs(tempRect.top) > (tempHeight -20)){
							tempRect.top = 0 - tempHeight + 20;
							tempRect.bottom = tempRect.top + tempHeight;
						}
					}else{//保证不向下移出边界
						if(tempRect.top > (height - 20)){
							tempRect.top = height - 20;
							tempRect.bottom = tempRect.top + tempHeight;
						}
					}
					
				}else if(pointCount == 2){//双指
					System.out.println("双指:移动");
					isMove = true;
					float oneX = event.getX(0);
					float oneY = event.getY(0);
					
					float twoX = event.getX(1);
					float twoY = event.getY(1);
					
					if(prePointOneX<1){
						prePointOneX = oneX;
						prePointOneY = oneY;
						prePointTwoX = twoX;
						prePointTwoY = twoY;	
						return true;
					}
					
					double distancePre = Math.sqrt(
													(prePointOneX - prePointTwoX) * 
													(prePointOneX - prePointTwoX) +
													(prePointOneY - prePointTwoY) * 
													(prePointOneY - prePointTwoY));
					
					double distanceNow = Math.sqrt(
													(oneX - twoX) * 
													(oneX - twoX) +
													(oneY - twoY) * 
													(oneY - twoY));
					
					int tempWidth = tempRect.right - tempRect.left;//现图片宽度
					int tempHeight = tempRect.bottom - tempRect.top;//现图片高度
					
					double scale = distanceNow/distancePre;
					
					int nowWidth = (int)(tempWidth * scale);
					int nowHeight = (int)(tempHeight * scale);
					
					int diffX = (nowWidth - tempWidth)/2;
					int diffY = (nowHeight - tempHeight)/2;
					
					tempRect.left -= diffX;
					tempRect.right += diffX;
					
					tempRect.top -= diffY;
					tempRect.bottom += diffY;
					
					System.out.println("原："+tempWidth+" : "+tempHeight+"  新:"+nowWidth+" : "+nowHeight+"  scale:"+scale+"  distancePre:"+distancePre+"  distanceNow:"+distanceNow);
					
					
					//保证不超过最大放大限制
					tempWidth = tempRect.right - tempRect.left;//现图片宽度
					tempHeight = tempRect.bottom - tempRect.top;//现图片高度
					
					if(tempWidth > max_scale_width){//超过最大缩放，保持最大级别
						scale = (double)max_scale_width/tempWidth;
						
						nowHeight = (int)(tempHeight * scale);
						
						diffX = (max_scale_width - tempWidth)/2;
						diffY = (nowHeight - tempHeight)/2;
						
						tempRect.left -= diffX;
						tempRect.right += diffX;
						
						tempRect.top -= diffY;
						tempRect.bottom += diffY;
						
					}
					
					prePointOneX = oneX;
					prePointOneY = oneY;
					
					prePointTwoX = twoX;
					prePointTwoY = twoY;
				}
				break;
				
			case MotionEvent.ACTION_UP:
				System.out.println("放开");
				
				if((System.currentTimeMillis() - preClickTime) < CLICK_TIME && !isMove){//点击事件
					if(onClickListener != null){
						onClickListener.onClick(this);
						System.out.println("单指放开，点击事件...");
					}
				}
				
				prePointOneX = 0.0F;
				prePointOneY = 0.0F;
				prePointTwoX = 0.0F;
				prePointTwoY = 0.0F;
				isMove = false;
				
				preMoveX = 0;
				preMoveY = 0;
				
				break;
				
			case MotionEvent.ACTION_CANCEL:
				prePointOneX = 0.0F;
				prePointOneY = 0.0F;
				prePointTwoX = 0.0F;
				prePointTwoY = 0.0F;
				isMove = false;
				
				preMoveX = 0;
				preMoveY = 0;
				System.out.println("在cancel。。。");
				break;
				
		}
		
		
		postInvalidate();//刷新
		return true;
	}
	
	/**
	 * 设置是否可缩放
	 * @param enable
	 */
	public void setScale(boolean enable){
		isScaleAble = enable;
		if(enable){
			postInvalidate();
			return;
		}
		
		//不可缩放，恢复原始状态
		reset();
	}
	
	/**获取是否可缩放*/
	public boolean getScaleAble(){
		return isScaleAble;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		onClickListener = l;
	}
	
}
