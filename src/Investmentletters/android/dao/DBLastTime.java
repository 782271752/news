package Investmentletters.android.dao;

import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 获取最后列表一次刷新时间
 * @author liang
 */
public class DBLastTime {

	/**Sqlite数据库帮助类*/
	private DBHelper dbHelper = null;
	
	public DBLastTime(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * 获取最后一次刷新时间
	 * @param type 类型，详细参见DBHelper.TYPE_xxx
	 * @return null:获取失败
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
	 * 返回可读数据库实例，使用完成后，记得close
	 * @return null:获取失败
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
