package Investmentletters.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ��ҳͶ���б�ͷ����ͼ����ViewPager�����ڽ��������ͻ��<br/>
 * <strong>�����ListView ��ͻ˼·��</strong>��ios�����״λ�������Ϊ����ԭ�͡�
 * @author liang
 */
public class IndexInvestmentViewPager extends ViewPager {
	
	private float preX = -1.0F;
	private float preY = -1.0F;
	
	private boolean isInit = false;
	/**�Ƿ�ɹ���*/
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
				System.out.println("��onInterxxx():����");
				onTouchEvent(event);
				isInit = false;
				isScrollEnable = false;
				break;
				
			case MotionEvent.ACTION_MOVE:
				System.out.println("��onInterxxx():�ƶ�");		
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
				System.out.println("��onInterxxx():�ſ�");
				onTouchEvent(event);
				break;
	
			case MotionEvent.ACTION_CANCEL:
				System.out.println("��onInterxxx():ȡ��");
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
				System.out.println("��onTouchEvent():����");
				super.onTouchEvent(event);
				break;
				
			case MotionEvent.ACTION_MOVE:
				System.out.println("��onTouchEvent():�ƶ�");
				if(isScrollEnable){
					System.out.println("���ƶ�");
					super.onTouchEvent(event);		
					getParent().requestDisallowInterceptTouchEvent(true);
				}else{
					getParent().requestDisallowInterceptTouchEvent(false);
					System.out.println("�����ƶ�");
				}
				break;
							
			case MotionEvent.ACTION_UP:
				System.out.println("��onTouchEvent():�ſ�");
				super.onTouchEvent(event);
				getParent().requestDisallowInterceptTouchEvent(false);
				
				preX = -1.0F;
				preY = -1.0F;
				isInit = false;
				isScrollEnable = false;
				break;
	
			case MotionEvent.ACTION_CANCEL:
				System.out.println("��onTouchEvent():ȡ��");
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
