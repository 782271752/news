package Investmentletters.android.dao.base;

import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 其他类型数据库DAO基类
 * @author liang
 * @param <T>
 */
public abstract class AbsDBBase<T> {
	
	/**Sqlite数据库帮助类*/
	private DBHelper dbHelper = null;
	
	public AbsDBBase(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * 查询
	 * @param id
	 * @return null:失败
	 */
	public abstract T query(int id);
	
	/**
	 * 插入增加到数据库
	 * @param data 数据
	 * @param id
	 * @return true:成功 false:失败
	 */
	public abstract boolean add(T data,int id);
	
	/**
	 * 更新数据
	 * @param data 数据
	 * @param id
	 * 
	 * @return true：成功 false:失败
	 */
	public abstract boolean update(T data,int id);
	
	/**
	 * 删除
	 * @param id
	 * 
	 * @return true:成功 false:失败
	 */
	public abstract boolean delete(int id);
	
	/**
	 * 返回可写数据库实例,使用完成后，记得close
	 * @return null:获取失败
	 */
	public SQLiteDatabase getWritableDatabase(){
		try{
			return dbHelper.getWritableDatabase();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
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
