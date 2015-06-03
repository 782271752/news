package Investmentletters.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import Investmentletters.android.R;
import Investmentletters.android.activity.ZoomImageActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * �첽����ͼƬ�������̴߳����Handler��һ��Ҫ�����߳�ʵ������
 * 
 * @author liang
 */
public class ImageLoadHandler extends Handler {

	/** handler��what������ͼƬ */
	public static final int HANDLER_LOAD_IMG = 0;
	/** handler��what������ͼƬʧ�� */
	public static final int HANDLER_LOAD_IMG_ERROR = 1;
	/** handler��what������ͼƬ��� */
	public static final int HANDLER_LOAD_IMG_WH = 3;

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		ImageLoadModel model = (ImageLoadModel) msg.obj;
		switch (msg.what) {
		case HANDLER_LOAD_IMG: // ����ͼƬ
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageBitmap(model.getBmp());// ��ʾͼƬ
					model.getImageView().setVisibility(View.VISIBLE);
				}
			}
			break;
		case HANDLER_LOAD_IMG_ERROR: // ����ͼƬʧ��
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageResource(
							R.drawable.bg_listitem_big);
				}
			}
			break;
		case HANDLER_LOAD_IMG_WH: // ����ͼƬ
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageBitmap(model.getBmp());// ��ʾͼƬ
					model.getImageView().setVisibility(View.VISIBLE);
					model.getActivity().getBitmap(model.getBmp());
					// ZoomImageActivity act = new ZoomImageActivity();

				}
			}
			break;
		}
	}
}
