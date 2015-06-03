package Investmentletters.android.utils;

import android.os.Message;
import android.view.View;

/**
 * �첽����ͼƬ�������̴߳����Handler�����û��ͼƬ������ʾImageView��һ��Ҫ�����߳�ʵ������
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
