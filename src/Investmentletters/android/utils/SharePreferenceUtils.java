package Investmentletters.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences工具类
 * @author liang
 */
public class SharePreferenceUtils {

	private SharedPreferences sp = null;
	/**文件名*/
	private final String FILE_NAME = "sp_name_inv";
	
	/**用户名*/
	private final String KEY_USER_NAME = "MAP_LOGIN_USERNAME";
	/**密码*/
	private final String KEY_PASSWD = "MAP_LOGIN_PASSWORD";
	/**是否推送*/
	private final String KEY_IS_PUSH_MSG = "IS_PUSH_MSG";
	
	public SharePreferenceUtils(Context context){
		sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}
	
	/**
	 * 保存密码
	 * @param passwd
	 */
	public void savePasswd(String passwd){
		save(KEY_PASSWD,passwd);
	}
	
	/**
	 * 获取密码
	 * @return
	 */
	public String getPasswd(){
		return sp.getString(KEY_PASSWD, "");
	}
	
	/**
	 * 保存用户名
	 * @param passwd
	 */
	public void saveUserName(String userName){
		save(KEY_USER_NAME,userName);
	}
	
	/**
	 * 获取用户名
	 * @return
	 */
	public String getUserName(){
		return sp.getString(KEY_USER_NAME, "");
	}
	
	/**
	 * 设置消息推送
	 * @param enable true:可推送  false:不可推送
	 */
	public void savePushState(boolean enable){
		save(KEY_IS_PUSH_MSG , enable);
	}
	
	/**
	 * 获取推送状态
	 * @return true:可推送  false:不可推送
	 */
	public boolean getPushState(){
		return sp.getBoolean(KEY_IS_PUSH_MSG, true);
	}
	
	
	/**
	 * 保存
	 * @param key
	 * @param value
	 */
	private void save(String key,String value){
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 保存
	 * @param key
	 * @param value
	 */
	private void save(String key,boolean value){
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
}
