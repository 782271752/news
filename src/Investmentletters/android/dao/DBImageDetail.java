package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.dao.base.AbsDBBase;
import Investmentletters.android.entity.Image;
import Investmentletters.android.utils.DBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 美图在线详细新闻数据库DAo
 * @author liang
 *
 */
public class DBImageDetail extends AbsDBBase<List<Image>> {

	public DBImageDetail(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Image> query(int id) {
		// TODO Auto-generated method stub
		List<Image> res = null;
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return null;
		}
		res = new ArrayList<Image>();
		
		String[] columns = {//查询字段
				DBHelper.COLUMN_TIME,//时间
				DBHelper.COLUMN_MIN_IMAGE,//缩略图
				DBHelper.COLUMN_CONTENT//内容
		};
		
		//查询条件
		String selection = DBHelper.COLUMN_SERVER_ID+"="+id;
		
		//排序
		String orderBy = DBHelper.COLUMN_ID+" ASC";
		
		
		Cursor cursor = db.query(DBHelper.TABLE_NAME_IMAGE_NEWS_DETAIL, 
								columns, selection, null, null, null, orderBy);
		
		int index_time = cursor.getColumnIndex(DBHelper.COLUMN_TIME);//位置：时间
		int index_minImg = cursor.getColumnIndex(DBHelper.COLUMN_MIN_IMAGE);//位置：缩略图
		int index_content = cursor.getColumnIndex(DBHelper.COLUMN_CONTENT);//位置：内容
		Image item = null;
		while(cursor.moveToNext()){
			item = new Image();
			
			item.setTime(cursor.getString(index_time));
			item.setImg(cursor.getString(index_minImg));
			item.setContent(cursor.getString(index_content));
			
			res.add(item);
		}
		
		cursor.close();
//		db.close();
		return res;
	}

	@Override
	public synchronized boolean add(List<Image> data,int id) {
		// TODO Auto-generated method stub
		
		delete(id);//先删除
		
		SQLiteDatabase db = getWritableDatabase();
		boolean res = true;
		if(db == null){
			return false;
		}
		
		
		ContentValues cv = new ContentValues();
		for(Image item:data){
			if(!res){
				break;
			}
			
			cv.put(DBHelper.COLUMN_SERVER_ID, id);//所属的新闻在 t_news的serverId	
			cv.put(DBHelper.COLUMN_TITLE, "null");//标题	
			cv.put(DBHelper.COLUMN_TIME, item.getTime());//时间	
			cv.put(DBHelper.COLUMN_MIN_IMAGE, item.getImg());//图片
			cv.put(DBHelper.COLUMN_CONTENT, item.getContent());//内容	
			
			res = db.insert(DBHelper.TABLE_NAME_IMAGE_NEWS_DETAIL, null, cv) >= 0;
		}
//		db.close();
		
		return res;
	}

	@Override
	public boolean update(List<Image> data,int id) {
		// TODO Auto-generated method stub
//		delete(id);//先删除
		return add(data,id);//增加
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		if(db == null){
			return false;
		}
		db.execSQL("delete from "+DBHelper.TABLE_NAME_IMAGE_NEWS_DETAIL+" where "+DBHelper.COLUMN_SERVER_ID+"="+id);
//		db.close();
		return false;
	}

}
