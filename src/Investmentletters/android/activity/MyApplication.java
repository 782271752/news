package Investmentletters.android.activity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/** 数据共享类 */
public class MyApplication extends Application implements Thread.UncaughtExceptionHandler{

	/** 内存图片缓存大小：2*1024*1024 = 2M */
	private int IMG_CACHE_SIZE = 2*1024*1024;
	/** 内存中的图片缓存 */
	private LruCache<String, Bitmap> imgCache = null;
	/** 要显示详细新闻的列表数据 */
	private List<?> showDetailDatas = null;

	/** 屏幕宽度像素点 */
	private int screenWidth = 0;
	
	/**用户名*/
	private String userName = null;
	/**密码*/
	private String passwd = null;
	
	/**是否修改了头像*/
	private boolean isModifyHeadImg = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Thread.setDefaultUncaughtExceptionHandler(this);

		imgCache = new LruCache<String, Bitmap>(IMG_CACHE_SIZE);
	}

	/**
	 * 获取屏幕宽度像素点
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * 设置屏幕宽度像素点
	 * 
	 * @param screenWidth
	 */
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * 获取内存中的图片缓存
	 * 
	 * @param key
	 *            键值对的键
	 * @return null:没有缓存
	 */
	public Bitmap getCacheImg(String key) {
		return imgCache.get(key);
	}

	/**
	 * 增加图片到内存中缓中
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

	/** 要显示详细新闻的列表数据 */
	public List<?> getShowDetailDatas() {
		return showDetailDatas;
	}

	/** 要显示详细新闻的列表数据 */
	public void setShowDetailDatas(List<?> showDetailDatas) {
		this.showDetailDatas = showDetailDatas;
	}

	/**用户名*/
	public String getUserName() {
		return userName;
	}

	/**用户名*/
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**密码*/
	public String getPasswd() {
		return passwd;
	}

	/**密码*/
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**是否修改了头像*/
	public boolean isModifyHeadImg() {
		return isModifyHeadImg;
	}

	/**是否修改了头像*/
	public void setModifyHeadImg(boolean isModifyHeadImg) {
		this.isModifyHeadImg = isModifyHeadImg;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		
		System.out.println("手机信息："+Build.MODEL+"   "+Build.BRAND+"  "+Build.VERSION.RELEASE );
		
		System.out.println("程序异常:"+ex.toString()+"   ");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		ex.printStackTrace(pw);
		
		System.out.println("获取到的错："+sw.toString());
		
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
