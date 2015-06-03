package Investmentletters.android.dao.base;

import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * �����������ݿ�DAO����
 * @author liang
 * @param <T>
 */
public abstract class AbsDBBase<T> {
	
	/**Sqlite���ݿ������*/
	private DBHelper dbHelper = null;
	
	public AbsDBBase(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * ��ѯ
	 * @param id
	 * @return null:ʧ��
	 */
	public abstract T query(int id);
	
	/**
	 * �������ӵ����ݿ�
	 * @param data ����
	 * @param id
	 * @return true:�ɹ� false:ʧ��
	 */
	public abstract boolean add(T data,int id);
	
	/**
	 * ��������
	 * @param data ����
	 * @param id
	 * 
	 * @return true���ɹ� false:ʧ��
	 */
	public abstract boolean update(T data,int id);
	
	/**
	 * ɾ��
	 * @param id
	 * 
	 * @return true:�ɹ� false:ʧ��
	 */
	public abstract boolean delete(int id);
	
	/**
	 * ���ؿ�д���ݿ�ʵ��,ʹ����ɺ󣬼ǵ�close
	 * @return null:��ȡʧ��
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
