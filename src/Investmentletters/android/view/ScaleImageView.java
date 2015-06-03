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
 * ����ImageView
 * @author liang
 */
public class ScaleImageView extends ImageView {
	
	private Paint paint = null;
	
	/**����������*/
	private int width = 0;
	/**��������߶�*/
	private int height = 0;
	
	/**Ҫ����ͼ*/
	private Bitmap bitmap = null;
	
	private Rect tempRect = null;
	/**ԭʼͼƬ��С*/
	private Rect originRect = null;
	
	/**������ű���*/
	private final float MAX_SCALE = 1.5F;
	/**������ſ��*/
	private int max_scale_width = 0;
	
	/**��һ���ƶ�ƽ�Ƶ�x����*/
	private float preMoveX = 0.0F;
	/**��һ���ƶ�ƽ�Ƶ�y����*/
	private float preMoveY = 0.0F;
	
	/**��һ�����Ŵ���1��x����*/
	private float prePointOneX = 0.0F;
	/**��һ�����Ŵ���1��y����*/
	private float prePointOneY = 0.0F;
	
	/**��һ�����Ŵ���2��x����*/
	private float prePointTwoX = 0.0F;
	/**��һ�����Ŵ���2��y����*/
	private float prePointTwoY = 0.0F;
	
	/**��һ�ε���ʱ��*/
	private long preClickTime = 0L;
	/**���ڵ����ʱ����*/
	private long CLICK_TIME = 1000L;
	/**�Ƿ����ƶ���������ƶ�����������*/
	private boolean isMove = false;
	private OnClickListener onClickListener = null;
	
	/**�Ƿ��Ѽ����*/
	private boolean isMeasuer = false;
	
	/**�Ƿ������ƽ��*/
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
	
	/**�ָ�ͼƬ״̬*/
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
	
	/**��ʼ�����ߴ�*/
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
		
		System.out.println("ͼƬ�߿�width:"+width+"  height:"+height+"  originImgWidht:"+originImgWidth+"   originImgHeight:"+originImgHeight);
		
		float ratio = (float)originImgWidth/originImgHeight;
		
		if(width >= originImgWidth && height>=originImgHeight){//ͼƬ����Ϊ�����߻��
			
			int tempHeight = (int)(width/ratio);
			
			if(tempHeight <= height){//�������
				originImgWidth = width;
				originImgHeight = tempHeight;
			}else{//�߶�����
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
			
			if(tempHeight <= height){//�������
				originImgWidth = width;
				originImgHeight = tempHeight;
			}else{//�߶�����
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
			System.out.println("��bitmap");
			return;
		}
		
		if(!isMeasuer){
			isMeasuer = true;
			initMeasure();
			reset();
		}
		
		canvas.drawARGB(255, 0, 0, 0);//������
		
		canvas.drawBitmap(bitmap, null , tempRect, paint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int pointCount = event.getPointerCount();//��������
		
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
				
				if(pointCount == 1){//��ָ
					System.out.println("��ָ:�ƶ�");
					
					if(preMoveX<1){
						preMoveX = event.getX();
						preMoveY = event.getY();
						System.out.println("���Σ���ʼ������...");
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
					
					//��֤�����Ƴ��߽�
					int tempWidth = tempRect.right - tempRect.left;//��ͼƬ���
					int tempHeight = tempRect.bottom - tempRect.top;//��ͼƬ�߶�
					
					if(tempRect.left < 0){ //��֤��������Ƴ��߽�
						if(Math.abs(tempRect.left) > (width - 20)){
							tempRect.left = 0 - width + 20;
							tempRect.right = tempRect.left + tempWidth;
						}
					}else{//��֤�����ұ��Ƴ�
						int rightBundler = width + width - 20;
						if(tempRect.right > rightBundler){
							tempRect.left = width - 20;
							tempRect.right = tempRect.left + tempWidth;
						}
					}
					
					if(tempRect.top < 0){//��֤�������Ƴ��߽�
						if(Math.abs(tempRect.top) > (tempHeight -20)){
							tempRect.top = 0 - tempHeight + 20;
							tempRect.bottom = tempRect.top + tempHeight;
						}
					}else{//��֤�������Ƴ��߽�
						if(tempRect.top > (height - 20)){
							tempRect.top = height - 20;
							tempRect.bottom = tempRect.top + tempHeight;
						}
					}
					
				}else if(pointCount == 2){//˫ָ
					System.out.println("˫ָ:�ƶ�");
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
					
					int tempWidth = tempRect.right - tempRect.left;//��ͼƬ���
					int tempHeight = tempRect.bottom - tempRect.top;//��ͼƬ�߶�
					
					double scale = distanceNow/distancePre;
					
					int nowWidth = (int)(tempWidth * scale);
					int nowHeight = (int)(tempHeight * scale);
					
					int diffX = (nowWidth - tempWidth)/2;
					int diffY = (nowHeight - tempHeight)/2;
					
					tempRect.left -= diffX;
					tempRect.right += diffX;
					
					tempRect.top -= diffY;
					tempRect.bottom += diffY;
					
					System.out.println("ԭ��"+tempWidth+" : "+tempHeight+"  ��:"+nowWidth+" : "+nowHeight+"  scale:"+scale+"  distancePre:"+distancePre+"  distanceNow:"+distanceNow);
					
					
					//��֤���������Ŵ�����
					tempWidth = tempRect.right - tempRect.left;//��ͼƬ���
					tempHeight = tempRect.bottom - tempRect.top;//��ͼƬ�߶�
					
					if(tempWidth > max_scale_width){//����������ţ�������󼶱�
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
				System.out.println("�ſ�");
				
				if((System.currentTimeMillis() - preClickTime) < CLICK_TIME && !isMove){//����¼�
					if(onClickListener != null){
						onClickListener.onClick(this);
						System.out.println("��ָ�ſ�������¼�...");
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
				System.out.println("��cancel������");
				break;
				
		}
		
		
		postInvalidate();//ˢ��
		return true;
	}
	
	/**
	 * �����Ƿ������
	 * @param enable
	 */
	public void setScale(boolean enable){
		isScaleAble = enable;
		if(enable){
			postInvalidate();
			return;
		}
		
		//�������ţ��ָ�ԭʼ״̬
		reset();
	}
	
	/**��ȡ�Ƿ������*/
	public boolean getScaleAble(){
		return isScaleAble;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		onClickListener = l;
	}
	
}
