package Investmentletters.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences������
 * @author liang
 */
public class SharePreferenceUtils {

	private SharedPreferences sp = null;
	/**�ļ���*/
	private final String FILE_NAME = "sp_name_inv";
	
	/**�û���*/
	private final String KEY_USER_NAME = "MAP_LOGIN_USERNAME";
	/**����*/
	private final String KEY_PASSWD = "MAP_LOGIN_PASSWORD";
	/**�Ƿ�����*/
	private final String KEY_IS_PUSH_MSG = "IS_PUSH_MSG";
	
	public SharePreferenceUtils(Context context){
		sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * ��������
	 * @param passwd
	 */
	public void savePasswd(String passwd){
		save(KEY_PASSWD,passwd);
	}
	
	/**
	 * ��ȡ����
	 * @return
	 */
	public String getPasswd(){
		return sp.getString(KEY_PASSWD, "");
	}
	
	/**
	 * �����û���
	 * @param passwd
	 */
	public void saveUserName(String userName){
		save(KEY_USER_NAME,userName);
	}
	
	/**
	 * ��ȡ�û���
	 * @return
	 */
	public String getUserName(){
		return sp.getString(KEY_USER_NAME, "");
	}
	
	/**
	 * ������Ϣ����
	 * @param enable true:������  false:��������
	 */
	public void savePushState(boolean enable){
		save(KEY_IS_PUSH_MSG , enable);
	}
	
	/**
	 * ��ȡ����״̬
	 * @return true:������  false:��������
	 */
	public boolean getPushState(){
		return sp.getBoolean(KEY_IS_PUSH_MSG, true);
	}
	
	
	/**
	 * ����
	 * @param key
	 * @param value
	 */
	private void save(String key,String value){
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * ����
	 * @param key
	 * @param value
	 */
	private void save(String key,boolean value){
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
}
