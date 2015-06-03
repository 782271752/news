package Investmentletters.android.utils;

import Investmentletters.android.activity.ZoomActivity;
import Investmentletters.android.activity.ZoomImageActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * ͼƬ�̼߳�������model
 * @author liang
 */
public class ImageLoadModel {
	
	/**Ҫ��ʾ��ͼƬ����*/
	private ImageView imageView = null;
	/**Ҫ��ʾ��ͼƬ*/
	private Bitmap bmp = null;
	/**imageView��tag�������adapter�У�������view����ɵ�ǰ����Ҫ���ش�bmpʱ������ʾ*/
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
	/**��ȡҪ��ʾ��ImageView*/
	public ZoomActivity getActivity() {
		return activity;
	}
	/**��ȡҪ��ʾ��ImageView*/
	public ImageView getImageView() {
		return imageView;
	}

	/**��ȡҪ��ʾ��Bitmap*/
	public Bitmap getBmp() {
		return bmp;
	}

	/**��ȡҪ��ʾ��ImageView��λ�á������ж��Ƿ���ʾ*/
	public int getPosition() {
		return position;
	}
	
}
