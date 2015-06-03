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
 * 异步加载图片，在主线程处理的Handler。一定要在主线程实例化。
 * 
 * @author liang
 */
public class ImageLoadHandler extends Handler {

	/** handler的what，加载图片 */
	public static final int HANDLER_LOAD_IMG = 0;
	/** handler的what，加载图片失败 */
	public static final int HANDLER_LOAD_IMG_ERROR = 1;
	/** handler的what，加载图片宽高 */
	public static final int HANDLER_LOAD_IMG_WH = 3;

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		ImageLoadModel model = (ImageLoadModel) msg.obj;
		switch (msg.what) {
		case HANDLER_LOAD_IMG: // 加载图片
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageBitmap(model.getBmp());// 显示图片
					model.getImageView().setVisibility(View.VISIBLE);
				}
			}
			break;
		case HANDLER_LOAD_IMG_ERROR: // 加载图片失败
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageResource(
							R.drawable.bg_listitem_big);
				}
			}
			break;
		case HANDLER_LOAD_IMG_WH: // 加载图片
			if (model != null) {
				if (model.getPosition() == (Integer) model.getImageView()
						.getTag()) {
					model.getImageView().setImageBitmap(model.getBmp());// 显示图片
					model.getImageView().setVisibility(View.VISIBLE);
					model.getActivity().getBitmap(model.getBmp());
					// ZoomImageActivity act = new ZoomImageActivity();

				}
			}
			break;
		}
	}
}
