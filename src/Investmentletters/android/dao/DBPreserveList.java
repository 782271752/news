package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.dao.base.DBNewsListBase;
import Investmentletters.android.dao.base.NewsListBase;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 新闻收藏列表DAO
 * @author liang
 */
public class DBPreserveList extends DBNewsListBase {
	
	/**Sqlite数据库帮助类*/
	private DBHelper dbHelper = null;
	
	/**默认每页数据数量*/
	private final int PAGE_ZISE = 20;
	
	public DBPreserveList(Context context){
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
	 * 查询数据
	 * @param minId 最小id,如果大于0，则查询“更多”，供getMore()使用，<br/>
	 *              如果小于0,则认为非更询“更多”
	 * @param maxId 最大id,如果大于0，则查询“更多更多的新闻”，供getFresh()使用，<br/>
	 *              如果小于0,则认为非更询“更多更多的新闻”
	 *              
	 *<br/><strong>注意：如果minId和maxId同时小于0，则默认查列表getListDefault()<strong>
	 * @return null:失败 
	 */
	private List<News> query(int minId,int maxId){
		
		List<News> res = null;
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return null;
		}
		
	
		String[] columns = {//查询字段
				DBHelper.COLUMN_SERVER_ID , //标识
				DBHelper.COLUMN_TITLE , //标题
				DBHelper.COLUMN_SUMMARIZE , //内容(标题中的内容提要）
				DBHelper.COLUMN_TIME , //时间
				DBHelper.COLUMN_MIN_IMAGE , //缩略图
				DBHelper.COLUMN_BIG_IMAGE , //大图
				DBHelper.COLUMN_CONTENT , //全部内容 
				DBHelper.COLUMN_TOP,  //置顶
				DBHelper.COLUMN_CONTENT_TYPE //新闻关型
		};
		StringBuffer sb = new StringBuffer();
		
		//生成查询条件
		sb.append(DBHelper.COLUMN_IS_PRESERVE);
		sb.append("='");
		sb.append(DBHelper.PRESERVE_YES);
		sb.append("'");
		
		sb.append(" and ");
		sb.append(DBHelper.COLUMN_CONTENT_TYPE);
		sb.append("<>");
		sb.append(DBHelper.TYPE_MEITU);
		
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
		
		//生成排序条件
		sb.delete(0, sb.length());
		sb.append(DBHelper.COLUMN_CONTENT_TYPE);
		sb.append(" DESC");
		String orderBy = sb.toString().trim();
		
		
		
		Cursor cursor = db.query(true, DBHelper.TABLE_NAME_NEWS,
				columns, 
				selection, null,
				null, null, 
				orderBy, String.valueOf(PAGE_ZISE));
		
		//获取位置
		int index_id = cursor.getColumnIndex(DBHelper.COLUMN_SERVER_ID);
		int index_title = cursor.getColumnIndex(DBHelper.COLUMN_TITLE);
		int index_summaryze = cursor.getColumnIndex(DBHelper.COLUMN_SUMMARIZE);
		int index_time = cursor.getColumnIndex(DBHelper.COLUMN_TIME);
		int index_minImg = cursor.getColumnIndex(DBHelper.COLUMN_MIN_IMAGE);
		int index_bigImg = cursor.getColumnIndex(DBHelper.COLUMN_BIG_IMAGE);
		int index_content = cursor.getColumnIndex(DBHelper.COLUMN_CONTENT);
		int index_top = cursor.getColumnIndex(DBHelper.COLUMN_TOP);
		int index_type = cursor.getColumnIndex(DBHelper.COLUMN_CONTENT_TYPE);
		
		res = new ArrayList<News>();
		News item = null;
		
		
		int type = 0;
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
			
			type = cursor.getInt(index_type);
			switch(type){
				case TYPE_INVESTMENTLETTERS://投资快讯
					type = NewsListBase.TYPE_INVESTMENTLETTERS;
					break;
					
				case TYPE_TODAY://今日快讯
					type = NewsListBase.TYPE_TODAY_NO_BRAND;
					break;
					
				case TYPE_FADAR://公司雷达
					type = NewsListBase.TYPE_FADAR;
					break;
					
				case TYPE_DECODER://数据解码
					type = NewsListBase.TYPE_DECODER;
					break;
					
				case TYPE_TRADER_EXPLAIN: //操盘解读
					type = NewsListBase.TYPE_TRADER_EXPLAIN;
					break;
					
				case TYPE_FOCUS: //时事聚焦
					type = NewsListBase.TYPE_FOCUS;
					break;
				
				default:
					type = 0;		
					break;
			}
			item.setType(type);
			
			res.add(item);
		}
		cursor.close();
		
//		db.close();
		return res;
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
