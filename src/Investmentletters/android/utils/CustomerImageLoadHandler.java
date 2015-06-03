package Investmentletters.android.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * �Զ������ʧ��ͼ�꣬�첽����ͼƬ�������̴߳����Handler��һ��Ҫ�����߳�ʵ������
 * @author liang
 */
public class CustomerImageLoadHandler extends Handler{
	
	/**handler��what������ͼƬ*/
	public static final int HANDLER_LOAD_IMG = 0;
	/**handler��what������ͼƬʧ��*/
	public static final int HANDLER_LOAD_IMG_ERROR = 1;
	
	/**ͼƬ��Դid*/
	private int resId = -1;
	
	/**
	 * ����
	 * @param resId ͼƬ��Դid
	 */
	public CustomerImageLoadHandler(int resId){
		this.resId = resId;
	}
	
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		ImageLoadModel model = (ImageLoadModel)msg.obj;
		switch(msg.what){
			case HANDLER_LOAD_IMG: //����ͼƬ
				if(model != null){
					if(model.getPosition() == (Integer)model.getImageView().getTag()){
						model.getImageView().setImageBitmap(model.getBmp());//��ʾͼƬ
						model.getImageView().setVisibility(View.VISIBLE);
					}
				}
				break;
			case HANDLER_LOAD_IMG_ERROR: //����ͼƬʧ��
				if(model != null){
					if(model.getPosition() == (Integer)model.getImageView().getTag()){
						model.getImageView().setImageResource(resId);
					}
				}
				break;
		}
	}
	
}
