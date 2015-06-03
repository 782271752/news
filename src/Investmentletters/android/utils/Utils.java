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
 * ������
 * 
 * @author liang
 */
@SuppressLint("DefaultLocale")
public class Utils {

	/** �Ƿ��ѵ�½ */
	private static boolean isLogin = false;

	/** ����Ϊ�ѵ�½ */
	public static void setLogin(boolean Login) {
		isLogin = Login;
	}

	/**
	 * ��ȡ�Ƿ��ѵ�½
	 * 
	 * @return
	 */
	public static boolean isLogin() {
		return isLogin;
	}

	/**
	 * ����Ƿ����SDCard
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

	/** http��ַ */
	private static final String HTTTP_URL_HEAD = "http://";

	/**
	 * �ж��Ƿ�Ϊhttp·��
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
	 * ��ʱ�ļ����Ƿ���ڣ���������ڣ�����
	 * 
	 * @return true:���ڣ�false:������
	 */
	public static boolean isTempDirExist() {
		boolean result = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// sd���ɶ�д
			File file = new File(Constants.TEMP_DIR_PATH);
			result = file.exists();
			if (!result) { // �ļ��в����ڣ����Ŵ���
				result = file.mkdir();
			}
		}
		return result;
	}

	/**
	 * ������ʱ�ļ�Ŀ¼���Ƿ����ָ���ļ�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return true:���� false:������
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
				Environment.MEDIA_MOUNTED)) {// sd���ɶ�д
			File file = new File(Constants.TEMP_DIR_PATH + "/" + fileName);
			result = file.exists();
		}
		return result;
	}

	/**
	 * ��url����·���л�ȡͼƬ�ļ���
	 * 
	 * @param url
	 *            ͼƬurl����·��
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		String result = null;

		int index = url.lastIndexOf("/");
		result = url.substring(index + 1);

		return result;
	}

	/** ���sd���з���������ͼƬ */
	public static void cleanCacheImg() {

		if (isTempDirExist()) { // �ļ��д���
			File file = new File(Constants.TEMP_DIR_PATH);
			if (file.isDirectory()) { // �ļ����ļ���
				File[] files = file.listFiles();// ��ȡ���ļ���
				File tempFile = null;
				long maxLastTime = 0;// ���µ��ļ��޸�ʱ��
				long currentLastTime = 0;// ��ǰ�ļ����޸�ʱ��

				// ��ȡ�ļ��������µ��ļ��޸�ʱ��
				for (int i = 0; i < files.length; i++) {
					tempFile = files[i];
					if (tempFile.isFile()) {
						currentLastTime = tempFile.lastModified();
						maxLastTime = maxLastTime > currentLastTime ? maxLastTime
								: currentLastTime;
					}
				}

				// ɾ������ļ��޸�ʱ�������ǰ�����ļ�
				maxLastTime = maxLastTime - 259200000;// ����ǰ 3*24*3600*1000
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
	 * �������б����ݽ��д��ö�=����=��������
	 * 
	 * @param data
	 */
	public static void SortTopMaxToMin(List<News> data) {
		// �����ȶ��ö����������ζԷ��ö�����
		final int size = data.size();
		final int nSize = size - 1;

		News currentNews = null;// ��ǰ��Ҫ���������
		int currentId = 0;// ��ǰ��Ҫ���������id
		int currentTop = 0;// ��ǰ��Ҫ����������ö����

		int tempTop = 0;// ��ʱ�ö����
		int insertIndex = 0;// ����λ��

		// ���ö�����
		for (int i = 0; i < size; i++) {
			currentTop = data.get(i).getTop();

			if (currentTop < 1) {// ���ö�������
				continue;
			}

			insertIndex = i;
			for (int k = 0; k < nSize; k++) {
				tempTop = data.get(k).getTop();
				if (tempTop > 0 && tempTop > currentTop) {
					currentTop = k + 1;
				} else {// ��ǰ���Ա�����ö���
					insertIndex = k;
					break;
				}
			}

			currentNews = data.remove(i);
			data.add(insertIndex, currentNews);
		}

		// �Է��ö�����
		int topSize = 0;// �ö�������
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
	 * ��ȡ��ǰ�Ƿ��Ƿ�Ϊ��������״̬
	 * 
	 * @param context
	 * @return true:���������� false:��
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

	// ��ȡ��ǰ����İ汾��
	public static String getVersionName(Context context) {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
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
