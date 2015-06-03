package Investmentletters.android.utils;

import Investmentletters.android.activity.MyApplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * ����ͼƬ�߳�<br/>
 * @author liang
 */
public class ImageLoadThread extends Thread {
	
	private Handler handler = null;
	private Message msg = null;
	/**ͼƬ��ַ*/
	private String url = null;
	/**ͼƬ������*/
	private ImageUtils imgUtils = null;
	/**Ҫ��ʾ��ImageView*/
	private ImageView imageView = null;
	/** imageView��tag�������adapter�У�������view����ɵ�ǰ����Ҫ���ش�bmpʱ������ʾ*/
	private int position = -1;
	private Context context = null;
	
	/**
	 * ����
	 * @param context ������
	 * @param url
	 * @param imageView
	 * @param position imageView��tag�������adapter�У�������view����ɵ�ǰ����Ҫ���ش�bmpʱ������ʾ
	 * @param handler
	 * @param what �������ͼƬ��handler�ص���Ҫ��what
	 */
	public ImageLoadThread(Context context , String url , ImageView imageView , int position , Handler handler , int what){
		this.context = context;
		this.url = url;
		this.imageView = imageView;
		this.position = position;
		this.handler = handler;
		
		imageView.setTag(position);
		
		msg = handler.obtainMessage();
		msg.what = what;
		
		imgUtils = new ImageUtils();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		
		Bitmap bmp = null;
		
		
		if(url != null){
			String fileName = Utils.getFileNameFromUrl(url);
			if(context == null){
				return ;
			}
			MyApplication app = (MyApplication)context.getApplicationContext();
			
			bmp = app.getCacheImg(fileName); //�ڴ����Ƿ��л���
				
			if(bmp == null){ //�ڴ���û��ͼƬ����
				if(Utils.isTempDirExist()){ //������ʱ�ļ��д���
					//����ʾԭʼ��ͼƬ���Ժ����Ż�
					if(Utils.isTempFileExist(fileName)){ //�����л��棬���ñ���ͼƬ
						bmp = imgUtils.getBitmapFromTempDir(fileName, -1, -1);
					}else{ //��������ͼƬ
						fileName = imgUtils.downLoadImage(url); //�����ļ�
						if(fileName == null){//����ͼƬ�ļ�ʧ�ܣ�ֱ�Ӳ���������ʾ
							bmp = imgUtils.getBitmapFromWeb(url, -1, -1);	
						}else{ //����ͼƬ�ļ��ɹ�
							bmp = imgUtils.getBitmapFromTempDir(fileName, -1, -1);
						}
					}
				}else{//��ʱ�ļ��в����ڣ�ֱ�Ӳ���������ʾ
					bmp = imgUtils.getBitmapFromWeb(url, -1, -1);	
				}
				
				if(bmp != null){ //ͼƬ���ɳɹ����ŵ��ڴ���
					app.addCacheImg(fileName, bmp);	
				}
				
			}
			
		}
		
		if(bmp != null){
			if((Integer)imageView.getTag() == position){
				ImageLoadModel model = new ImageLoadModel(imageView, bmp, position);
				msg.obj = model;
				handler.sendMessage(msg);//�ص�
			}
		}else{ //����ͼƬʧ��
			if((Integer)imageView.getTag() == position){
				ImageLoadModel model = new ImageLoadModel(imageView, bmp, position);
				msg.what = ImageLoadHandler.HANDLER_LOAD_IMG_ERROR;
				msg.obj = model;
				handler.sendMessage(msg);//�ص�
			}
		}
		
	}
	
}
