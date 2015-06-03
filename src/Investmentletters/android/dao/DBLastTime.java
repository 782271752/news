package Investmentletters.android.dao;

import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ��ȡ����б�һ��ˢ��ʱ��
 * @author liang
 */
public class DBLastTime {

	/**Sqlite���ݿ������*/
	private DBHelper dbHelper = null;
	
	public DBLastTime(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * ��ȡ���һ��ˢ��ʱ��
	 * @param type ���ͣ���ϸ�μ�DBHelper.TYPE_xxx
	 * @return null:��ȡʧ��
	 */
	public String getLastTime(int type){
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return null;
		}
		
		String time = null;
		
		Cursor cursor = db.query(DBHelper.TABLE_NAME_NEWS, 
				new String[]{DBHelper.COLUMN_TIME}, 
				DBHelper.COLUMN_CONTENT_TYPE + "="+type, null, 
				null, null, 
				DBHelper.COLUMN_TIME+ " DESC ", 
				String.valueOf("1"));
		
		if(cursor.moveToNext()){
			time = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TIME));
		}
		cursor.close();
//		db.close();
		
		return time;
	}
	
	
	
	/**
	 * ���ؿɶ����ݿ�ʵ����ʹ����ɺ󣬼ǵ�close
	 * @return null:��ȡʧ��
	 */
	public SQLiteDatabase getReadableDatabase(){
		try{
			return dbHelper.getReadableDatabase();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
}
