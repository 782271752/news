package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.dao.base.DBNewsListBase;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ͼƬ�����ղ��б�DAO
 * @author liang
 */
public class DBImagePreserveList extends DBNewsListBase {
	
	/**Sqlite���ݿ������*/
	private DBHelper dbHelper = null;
	
	/**Ĭ��ÿҳ��������*/
	private final int PAGE_ZISE = 20;
	
	public DBImagePreserveList(Context context){
		super(context);
		dbHelper = DBHelper.getInstance(context);
	}

	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		return query(-1,-1);
	}

	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return query(minId,-1);
	}

	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		return query(-1,maxId);
	}

	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * ��ѯ����
	 * @param minId ��Сid,�������0�����ѯ�����ࡱ����getMore()ʹ�ã�<br/>
	 *              ���С��0,����Ϊ�Ǹ�ѯ�����ࡱ
	 * @param maxId ���id,�������0�����ѯ�������������š�����getFresh()ʹ�ã�<br/>
	 *              ���С��0,����Ϊ�Ǹ�ѯ�������������š�
	 *              
	 *<br/><strong>ע�⣺���minId��maxIdͬʱС��0����Ĭ�ϲ��б�getListDefault()<strong>
	 * @return null:ʧ�� 
	 */
	private List<News> query(int minId,int maxId){
		
		List<News> res = null;
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return null;
		}
		
	
		String[] columns = {//��ѯ�ֶ�
				DBHelper.COLUMN_SERVER_ID , //��ʶ
				DBHelper.COLUMN_TITLE , //����
				DBHelper.COLUMN_SUMMARIZE , //����(�����е�������Ҫ��
				DBHelper.COLUMN_TIME , //ʱ��
				DBHelper.COLUMN_MIN_IMAGE , //����ͼ
				DBHelper.COLUMN_BIG_IMAGE , //��ͼ
				DBHelper.COLUMN_CONTENT , //ȫ������ 
				DBHelper.COLUMN_TOP  //�ö�
		};
		StringBuffer sb = new StringBuffer();
		
		//���ɲ�ѯ����
		sb.append(DBHelper.COLUMN_IS_PRESERVE);
		sb.append("='");
		sb.append(DBHelper.PRESERVE_YES);
		sb.append("'");
		
		sb.append(" and ");
		sb.append(DBHelper.COLUMN_CONTENT_TYPE);
		sb.append("='");
		sb.append(DBHelper.TYPE_MEITU);
		sb.append("'");
		
		if(minId>=0 && maxId<0){
			sb.append(" and ");
			sb.append(DBHelper.COLUMN_SERVER_ID);
			sb.append("<");
			sb.append(minId);
		}else if(minId<0 && maxId>=0){
			sb.append(" and ");
			sb.append(DBHelper.COLUMN_SERVER_ID);
			sb.append(">");
			sb.append(maxId);
		}
		
		String selection = sb.toString().trim();
		
		//������������
		sb.delete(0, sb.length());
		sb.append(DBHelper.COLUMN_CONTENT_TYPE);
		sb.append(" DESC");
		String orderBy = sb.toString().trim();
		
		
		
		Cursor cursor = db.query(true, DBHelper.TABLE_NAME_NEWS,
				columns, 
				selection, null,
				null, null, 
				orderBy, String.valueOf(PAGE_ZISE));
		
		//��ȡλ��
		int index_id = cursor.getColumnIndex(DBHelper.COLUMN_SERVER_ID);
		int index_title = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
		int index_summaryze = cursor.getColumnIndex(DBHelper.COLUMN_SUMMARIZE);
		int index_time = cursor.getColumnIndex(DBHelper.COLUMN_TIME);
		int index_minImg = cursor.getColumnIndex(DBHelper.COLUMN_MIN_IMAGE);
		int index_bigImg = cursor.getColumnIndex(DBHelper.COLUMN_BIG_IMAGE);
		int index_content = cursor.getColumnIndex(DBHelper.COLUMN_CONTENT);
		int index_top = cursor.getColumnIndex(DBHelper.COLUMN_TOP);
		
		res = new ArrayList<News>();
		News item = null;
		
		while(cursor.moveToNext()){
			item = new News();
			
			item.setId(cursor.getInt(index_id));
			item.setTitle(cursor.getString(index_title));
			item.setSummary(cursor.getString(index_summaryze));
			item.setTime(cursor.getString(index_time));
			item.setMinImg(cursor.getString(index_minImg));
			item.setBigImg(cursor.getString(index_bigImg));
			item.setContent(cursor.getString(index_content));
			item.setTop(cursor.getInt(index_top));
			res.add(item);
		}
		cursor.close();
		
//		db.close();
		return res;
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
