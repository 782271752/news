package Investmentletters.android.utils;

import Investmentletters.android.activity.ZoomActivity;
import Investmentletters.android.activity.ZoomImageActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片线程加载所用model
 * @author liang
 */
public class ImageLoadModel {
	
	/**要显示的图片容器*/
	private ImageView imageView = null;
	/**要显示的图片*/
	private Bitmap bmp = null;
	/**imageView的tag。如果在adapter中，因重用view，造成当前不需要加载此bmp时，不显示*/
	private int position = -1;
	private ZoomActivity activity;
	public ImageLoadModel(ImageView imageView , Bitmap bmp , int position,ZoomActivity activity){
		this.imageView = imageView;
		this.bmp = bmp;
		this.position = position;
		this.activity = activity;
	}

	public ImageLoadModel(ImageView imageView , Bitmap bmp , int position){
		this.imageView = imageView;
		this.bmp = bmp;
		this.position = position;
	}
	/**获取要显示的ImageView*/
	public ZoomActivity getActivity() {
		return activity;
	}
	/**获取要显示的ImageView*/
	public ImageView getImageView() {
		return imageView;
	}

	/**获取要显示的Bitmap*/
	public Bitmap getBmp() {
		return bmp;
	}

	/**获取要显示的ImageView的位置。用于判断是否显示*/
	public int getPosition() {
		return position;
	}
	
}
