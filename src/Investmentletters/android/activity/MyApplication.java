package Investmentletters.android.activity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/** ���ݹ����� */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler{

	/** �ڴ�ͼƬ�����С��2*1024*1024 = 2M */
	private int IMG_CACHE_SIZE = 2*1024*1024;
	/** �ڴ��е�ͼƬ���� */
	private LruCache<String, Bitmap> imgCache = null;
	/** Ҫ��ʾ��ϸ���ŵ��б����� */
	private List<?> showDetailDatas = null;

	/** ��Ļ������ص� */
	private int screenWidth = 0;
	
	/**�û���*/
	private String userName = null;
	/**����*/
	private String passwd = null;
	
	/**�Ƿ��޸���ͷ��*/
	private boolean isModifyHeadImg = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(this);

		imgCache = new LruCache<String, Bitmap>(IMG_CACHE_SIZE);
	}

	/**
	 * ��ȡ��Ļ������ص�
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * ������Ļ������ص�
	 * 
	 * @param screenWidth
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * ��ȡ�ڴ��е�ͼƬ����
	 * 
	 * @param key
	 *            ��ֵ�Եļ�
	 * @return null:û�л���
	 */
	public Bitmap getCacheImg(String key) {
		return imgCache.get(key);
	}

	/**
	 * ����ͼƬ���ڴ��л���
	 * 
	 * @param key
	 * @param bmp
	 */
	public void addCacheImg(String key, Bitmap bmp) {
		if (key != null && bmp != null) {
			key = key.trim();
			if (!key.equals("") && !bmp.isRecycled()) {
				imgCache.put(key, bmp);
			}
		}
	}

	/** Ҫ��ʾ��ϸ���ŵ��б����� */
	public List<?> getShowDetailDatas() {
		return showDetailDatas;
	}

	/** Ҫ��ʾ��ϸ���ŵ��б����� */
	public void setShowDetailDatas(List<?> showDetailDatas) {
		this.showDetailDatas = showDetailDatas;
	}

	/**�û���*/
	public String getUserName() {
		return userName;
	}

	/**�û���*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**����*/
	public String getPasswd() {
		return passwd;
	}

	/**����*/
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**�Ƿ��޸���ͷ��*/
	public boolean isModifyHeadImg() {
		return isModifyHeadImg;
	}

	/**�Ƿ��޸���ͷ��*/
	public void setModifyHeadImg(boolean isModifyHeadImg) {
		this.isModifyHeadImg = isModifyHeadImg;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		
		System.out.println("�ֻ���Ϣ��"+Build.MODEL+"   "+Build.BRAND+"  "+Build.VERSION.RELEASE );
		
		System.out.println("�����쳣:"+ex.toString()+"   ");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		ex.printStackTrace(pw);
		
		System.out.println("��ȡ���Ĵ�"+sw.toString());
		
		Intent intent = new Intent(this, ReportCrashErrorServer.class);
		intent.putExtra("brand", Build.BRAND);
		intent.putExtra("model", Build.MODEL);
		intent.putExtra("release", Build.VERSION.RELEASE);
		intent.putExtra("error_msg", sw.toString());
		
		startService(intent);
		
		Intent errIntent = new Intent(this, ShowCrash.class);
		errIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(errIntent);
		
		System.exit(1);
	}
	
	
	
}
