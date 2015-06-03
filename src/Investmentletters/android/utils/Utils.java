package Investmentletters.android.utils;

import java.io.File;
import java.util.List;

import Investmentletters.android.entity.News;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;

/**
 * 工具类
 * 
 * @author liang
 */
@SuppressLint("DefaultLocale")
public class Utils {

	/** 是否已登陆 */
	private static boolean isLogin = false;

	/** 设置为已登陆 */
	public static void setLogin(boolean Login) {
		isLogin = Login;
	}

	/**
	 * 获取是否已登陆
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return isLogin;
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/** http地址 */
	private static final String HTTTP_URL_HEAD = "http://";

	/**
	 * 判断是否为http路径
	 * 
	 * @param url
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static boolean isHttpUrl(String url) {
		if (url != null) {
			url = url.trim();
			url = url.toLowerCase();
			return url.indexOf(HTTTP_URL_HEAD) == 0;
		}
		return false;
	}

	/**
	 * 临时文件夹是否存在，如果不存在，创建
	 * 
	 * @return true:存在，false:不存在
	 */
	public static boolean isTempDirExist() {
		boolean result = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// sd卡可读写
			File file = new File(Constants.TEMP_DIR_PATH);
			result = file.exists();
			if (!result) { // 文件夹不存在，试着创建
				result = file.mkdir();
			}
		}
		return result;
	}

	/**
	 * 查找临时文件目录下是否存在指定文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return true:存在 false:不存在
	 */
	public static boolean isTempFileExist(String fileName) {
		boolean result = false;

		if (fileName == null) {
			return false;
		}

		fileName = fileName.trim();

		if (fileName.equals("")) {
			return false;
		}

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// sd卡可读写
			File file = new File(Constants.TEMP_DIR_PATH + "/" + fileName);
			result = file.exists();
		}
		return result;
	}

	/**
	 * 从url绝对路径中获取图片文件名
	 * 
	 * @param url
	 *            图片url绝对路径
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		String result = null;

		int index = url.lastIndexOf("/");
		result = url.substring(index + 1);

		return result;
	}

	/** 清除sd卡中非最后三天的图片 */
	public static void cleanCacheImg() {

		if (isTempDirExist()) { // 文件夹存在
			File file = new File(Constants.TEMP_DIR_PATH);
			if (file.isDirectory()) { // 文件是文件夹
				File[] files = file.listFiles();// 获取子文件夹
				File tempFile = null;
				long maxLastTime = 0;// 最新的文件修改时间
				long currentLastTime = 0;// 当前文件的修改时间

				// 获取文件夹中最新的文件修改时间
				for (int i = 0; i < files.length; i++) {
					tempFile = files[i];
					if (tempFile.isFile()) {
						currentLastTime = tempFile.lastModified();
						maxLastTime = maxLastTime > currentLastTime ? maxLastTime
								: currentLastTime;
					}
				}

				// 删除最后文件修改时间的三天前所有文件
				maxLastTime = maxLastTime - 259200000;// 三天前 3*24*3600*1000
				for (int i = 0; i < files.length; i++) {
					tempFile = files[i];
					if (tempFile.isFile()) {
						currentLastTime = tempFile.lastModified();
						if (currentLastTime < maxLastTime) {
							tempFile.delete();
						}
					}
				}

			}
		}

	}

	/**
	 * 对新闻列表数据进行从置顶=》新=》旧排序
	 * 
	 * @param data
	 */
	public static void SortTopMaxToMin(List<News> data) {
		// 排序：先对置顶排序，再依次对非置顶排序
		final int size = data.size();
		final int nSize = size - 1;

		News currentNews = null;// 当前需要排序的新闻
		int currentId = 0;// 当前需要排序的新闻id
		int currentTop = 0;// 当前需要排序的新闻置顶标记

		int tempTop = 0;// 临时置顶标记
		int insertIndex = 0;// 插入位置

		// 对置顶排序
		for (int i = 0; i < size; i++) {
			currentTop = data.get(i).getTop();

			if (currentTop < 1) {// 非置顶，跳过
				continue;
			}

			insertIndex = i;
			for (int k = 0; k < nSize; k++) {
				tempTop = data.get(k).getTop();
				if (tempTop > 0 && tempTop > currentTop) {
					currentTop = k + 1;
				} else {// 当前被对比项非置顶项
					insertIndex = k;
					break;
				}
			}

			currentNews = data.remove(i);
			data.add(insertIndex, currentNews);
		}

		// 对非置顶排序
		int topSize = 0;// 置顶的数量
		for (int i = 0; i < size; i++) {
			if (data.get(i).getTop() > 0) {
				topSize++;
			} else {
				break;
			}
		}

		for (int i = topSize; i < size; i++) {
			currentId = data.get(i).getId();
			insertIndex = i;

			for (int k = topSize; k < size; k++) {
				if (data.get(k).getId() > currentId) {
					continue;
				}

				insertIndex = k;
				break;
			}

			if (insertIndex != i) {
				currentNews = data.remove(i);
				data.add(insertIndex, currentNews);
			}
		}
	}

	/**
	 * 获取当前是否是否为网络连接状态
	 * 
	 * @param context
	 * @return true:网络已连接 false:否
	 */
	public static boolean isNetWork(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()
				&& (info.getState() == State.CONNECTED)) {
			return true;
		}

		return false;
	}

	// 获取当前程序的版本号
	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		String versionName = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			versionName = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versionName = "unknow_version";
		}
		return versionName;
	}

}
