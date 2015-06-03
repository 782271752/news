package Investmentletters.android.utils;

import android.os.Message;
import android.view.View;

/**
 * 异步加载图片，在主线程处理的Handler，如果没有图片，则不显示ImageView。一定要在主线程实例化。
 * @author Administrator
 */
public class NewsListImageLoadHandler extends ImageLoadHandler {

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
			case HANDLER_LOAD_IMG_ERROR:
				ImageLoadModel model = (ImageLoadModel)msg.obj;
				if(model != null){
						if(model.getPosition() == (Integer)model.getImageView().getTag()){
							model.getImageView().setVisibility(View.GONE);
						}
				}
				break;
			default:
				super.handleMessage(msg);
				break;
		}
	}
	
}
