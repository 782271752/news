package Investmentletters.android.utils;

import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.ZoomActivity;
import Investmentletters.android.activity.ZoomImageActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 加载图片线程<br/>
 * 
 * @author 次类可以自定义修改图片名
 */
public class CNImageLoadThead extends Thread {

	private Handler handler = null;
	private Message msg = null;
	/** 图片地址 */
	private String url = null;
	/** 图片处理类 */
	private ImageUtils imgUtils = null;
	/** 要显示的ImageView */
	private ImageView imageView = null;
	/** imageView的tag。如果在adapter中，因重用view，造成当前不需要加载此bmp时，不显示 */
	private int position = -1;
	private Context context = null;
	private String ex_filename = null;
	ProgressDialog Dialog = null;
	private ZoomActivity activity;

	/**
	 * 构造
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 * @param imageView
	 * @param position
	 *            imageView的tag。如果在adapter中，因重用view，造成当前不需要加载此bmp时，不显示
	 * @param ex_filename
	 *            图片修改名
	 * @param handler
	 * @param what
	 *            完成下载图片后，handler回调需要的what
	 */
	public CNImageLoadThead(ZoomActivity activity, Context context, String url,
			ImageView imageView, int position, Handler handler, int what,
			String ex_filename, ProgressDialog Dialog) {

		this.context = context;
		this.url = url;
		this.imageView = imageView;
		this.position = position;
		this.handler = handler;
		this.ex_filename = ex_filename;
		this.Dialog = Dialog;
		imageView.setTag(position);
		msg = handler.obtainMessage();
		msg.what = what;

		imgUtils = new ImageUtils();
		if (activity != null) {
			this.activity = activity;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		Bitmap bmp = null;

		if (url != null) {
			String fileName = ex_filename + Utils.getFileNameFromUrl(url);
			if (context == null) {
				return;
			}
//			MyApplication app = (MyApplication) context.getApplicationContext();
//			bmp = app.getCacheImg(fileName); // 内存中是否有缓存
//			
//			System.out.println("zoom 图片是否在内存中" + bmp);
//			
//			if (bmp == null) { // 内存中没有图片缓存
				if (Utils.isTempDirExist()) { // 本地临时文件夹存在
					// 先显示原始的图片，以后再优化
					if (Utils.isTempFileExist(fileName)) { // 本地有缓存，采用本地图片
						System.out.println("本地有缓存图片");
						bmp = imgUtils.getBitmapFromTempDir(fileName, -1, -1);

					} else { // 采用网络图片
						fileName = imgUtils.downLoadwbImage(url, ex_filename); // 下载文件
						if (fileName == null) {// 下载图片文件失败，直接采用网络显示
							bmp = imgUtils.getBitmapFromWeb(url, -1, -1);
						} else { // 下载图片文件成功
							bmp = imgUtils.getBitmapFromTempDir(fileName, -1,
									-1);
						}
					}
				} else {// 临时文件夹不存在，直接采用网络显示
					bmp = imgUtils.getBitmapFromWeb(url, -1, -1);
				}

//				if (bmp != null) { // 图片生成成功，放到内存中
//					app.addCacheImg(fileName, bmp);
//				}
//....			}

		}

		if (bmp != null) {
			if ((Integer) imageView.getTag() == position) {
				ImageLoadModel model = new ImageLoadModel(imageView, bmp,
						position, activity);
				msg.obj = model;
				handler.sendMessage(msg);// 回调
			}
		} else { // 加载图片失败
			if ((Integer) imageView.getTag() == position) {
				ImageLoadModel model = new ImageLoadModel(imageView, bmp,
						position);
				msg.what = ImageLoadHandler.HANDLER_LOAD_IMG_ERROR;
				msg.obj = model;
				handler.sendMessage(msg);// 回调
			}
		}
		if (Dialog != null) {
			Dialog.dismiss();
		}
	}

}
