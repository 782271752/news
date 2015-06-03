package Investmentletters.android.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 自定义加载失败图标，异步加载图片，在主线程处理的Handler。一定要在主线程实例化。
 * @author liang
 */
public class CustomerImageLoadHandler extends Handler{
	
	/**handler的what，加载图片*/
	public static final int HANDLER_LOAD_IMG = 0;
	/**handler的what，加载图片失败*/
	public static final int HANDLER_LOAD_IMG_ERROR = 1;
	
	/**图片资源id*/
	private int resId = -1;
	
	/**
	 * 构造
	 * @param resId 图片资源id
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
			case HANDLER_LOAD_IMG: //加载图片
				if(model != null){
					if(model.getPosition() == (Integer)model.getImageView().getTag()){
						model.getImageView().setImageBitmap(model.getBmp());//显示图片
						model.getImageView().setVisibility(View.VISIBLE);
					}
				}
				break;
			case HANDLER_LOAD_IMG_ERROR: //加载图片失败
				if(model != null){
					if(model.getPosition() == (Integer)model.getImageView().getTag()){
						model.getImageView().setImageResource(resId);
					}
				}
				break;
		}
	}
	
}
