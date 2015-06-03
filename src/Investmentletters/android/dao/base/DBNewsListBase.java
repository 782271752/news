package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.entity.News;
import Investmentletters.android.utils.DBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 新闻列表数据库DAo基类
 * @author liang
 */
public abstract class DBNewsListBase extends NewsListBase<News> {
	
	/**新闻类型:投资快讯*/
	public static final int TYPE_INVESTMENTLETTERS = DBHelper.TYPE_TOUZI;
	/**新闻类型:今日快讯*/
	public static final int TYPE_TODAY = DBHelper.TYPE_JINRI;
	/**新闻类型：公司雷达*/
	public static final int TYPE_FADAR = DBHelper.TYPE_LEIDA;
	/**新闻类型：数据解码*/
	public static final int TYPE_DECODER = DBHelper.TYPE_JIEMA;
	/**新闻类型:美图在线*/
	public static final int TYPE_IMAGES_ON_LINE = DBHelper.TYPE_MEITU;
	/**新闻类型：操盘解读*/
	public static final int TYPE_TRADER_EXPLAIN = DBHelper.TYPE_CAOPAN;
	/**新闻类型：时事聚焦*/
	public static final int TYPE_FOCUS = DBHelper.TYPE_FOCUS;
	/**新闻类型：消息推送*/
	public static final int TYPE_PUSH_MSG = DBHelper.TYPE_PUSH_MSG;
	
	/**Sqlite数据库帮助类*/
	private DBHelper dbHelper = null;
	
	/**默认每页数据数量*/
	private final int PAGE_ZISE = 20;
	/**非今日快讯栏目默认值*/
	private final int DEFAULT_NOT_TODAY_BRAND = -1;
	
	public DBNewsListBase(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * 增加数据<br/>
	 * <strong>注意：如果是今日快讯含栏目的，需要先执行：setBrandId(int id)</strong>
	 * @param data
	 * @return true:成功 false:失败
	 */
	public synchronized boolean add(List<News> data){
		SQLiteDatabase db = getWritableDatabase();
		boolean res = true;
		if(db == null){
			return false;
		}
		
		ContentValues cv = new ContentValues();
		Cursor cursor = null;
		for(News item:data){
			if(!res){
				break;
			}
			
//			System.out.println("查找:"+item.getId()+"  "+db.isOpen());
			
			cursor = db.query(DBHelper.TABLE_NAME_NEWS, new String[]{}, DBHelper.COLUMN_SERVER_ID+"="+item.getId()+" and "+DBHelper.COLUMN_CONTENT_TYPE+"="+getType(), null, null, null, null);
			if(cursor.getCount()>0){//已有数据，不重复插入
				cursor.close();
				continue;
			}
			
			cursor.close();
			
			cv.put(DBHelper.COLUMN_SERVER_ID, item.getId());//新闻在服务器数据库的id
			cv.put(DBHelper.COLUMN_TITLE, item.getTitle());//标题
			cv.put(DBHelper.COLUMN_SUMMARIZE, item.getSummary());//内容提要
			cv.put(DBHelper.COLUMN_TIME, item.getTime());//时间
			cv.put(DBHelper.COLUMN_MIN_IMAGE, item.getMinImg());//缩略图
			cv.put(DBHelper.COLUMN_BIG_IMAGE, item.getBigImg());//大图
			cv.put(DBHelper.COLUMN_CONTENT, item.getContent());//内容
			cv.put(DBHelper.COLUMN_TOP, item.getTop());//置顶
			cv.put(DBHelper.COLUMN_CONTENT_TYPE, getType());//新闻类型,这里要注意
			cv.put(DBHelper.COLUMN_BRAND_ID, getBrandId());//今日快讯栏日ID，含今日快讯栏目的子类需要重写getBrandId()
			cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.VALUE_IS_PRESERVE_NO);//是否为收藏
			cv.put(DBHelper.COLUMN_USER, "");//收藏者
			
//			System.out.println("插入:"+item.getId());
			res = db.insert(DBHelper.TABLE_NAME_NEWS, null, cv) >= 0;
		}
		
//		db.close();
		
		
		return res;
	}
	
	/**
	 * 更新新闻列表数据库
	 * @param data 要更新的新闻列表
	 * @return true:成功 false:失败
	 */
	public boolean update(List<News> data) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		if(db == null){
			return false;
		}
		
		ContentValues cv = new ContentValues();
		int id = 0;
		for(News item:data){
			id = item.getId();
			if(item.getTitle().trim().equals("")){//删除
				db.delete(DBHelper.TABLE_NAME_NEWS, DBHelper.COLUMN_SERVER_ID+"="+id, null);
				db.delete(DBHelper.TABLE_NAME_IMAGE_NEWS_DETAIL, DBHelper.COLUMN_SERVER_ID+"="+id, null);//删除美图在线相关
			}else{//修改
				cv.put(DBHelper.COLUMN_SERVER_ID, item.getId());//新闻在服务器数据库的id
				cv.put(DBHelper.COLUMN_TITLE, item.getTitle());//标题
				cv.put(DBHelper.COLUMN_SUMMARIZE, item.getSummary());//内容提要
				cv.put(DBHelper.COLUMN_TIME, item.getTime());//时间
				cv.put(DBHelper.COLUMN_MIN_IMAGE, item.getMinImg());//缩略图
				cv.put(DBHelper.COLUMN_BIG_IMAGE, item.getBigImg());//大图
				cv.put(DBHelper.COLUMN_CONTENT, item.getContent());//内容
				cv.put(DBHelper.COLUMN_TOP, item.getTop());//置顶
				cv.put(DBHelper.COLUMN_CONTENT_TYPE, getType());//新闻类型,这里要注意
				cv.put(DBHelper.COLUMN_BRAND_ID, getBrandId());//今日快讯栏日ID，含今日快讯栏目的子类需要重写getBrandId()
//				cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.VALUE_IS_PRESERVE_NO);//是否为收藏
//				cv.put(DBHelper.COLUMN_USER, "");//收藏者
				
				db.update(DBHelper.TABLE_NAME_NEWS,cv, DBHelper.COLUMN_SERVER_ID+"="+id, null);	
			}
			
		}
		
//		db.close();
		
		return true;
	}
	
	/**
	 * 获取黙认列表<br/>
	 * <strong>注意：如果是今日快讯含栏目的，需要先执行：setBrandId(int id)</strong>
	 * @return null:失败 
	 */
	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		return query(-1,-1);
	}
	
	/**
	 * 获取更多<br/>
	 * <strong>注意：如果是今日快讯含栏目的，需要先执行：setBrandId(int id)</strong>
	 * @param minId 最小的id
	 * @return null:失败 
	 */
	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return query(minId,-1);
	}
	
	/**
	 * 刷新<br/>
	 * <strong>注意：如果是今日快讯含栏目的，需要先执行：setBrandId(int id)</strong>
	 * @param maxId 最大的id
	 * @return null:失败 
	 */
	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		return query(-1,maxId);
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
				DBHelper.COLUMN_TOP  //置顶
		};
		StringBuffer sb = new StringBuffer();
		
		//生成查询条件
		sb.append(DBHelper.COLUMN_CONTENT_TYPE);
		sb.append("=");
		sb.append(getType());
		
		sb.append(" and ");
		sb.append(DBHelper.COLUMN_BRAND_ID);
		sb.append("=");
		sb.append(getBrandId());
		
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
		
		res = new ArrayList<News>();
		News item = null;
		
		int type = -1;//新闻在后台的类型，用于相关新闻
		switch(getType()){
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
		}
		
		while(cursor.moveToNext()){
			item = new News();
			
			item.setType(type);//设置新闻类型
			
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
	 * 收藏新闻
	 * @param id 新闻id
	 * @return true:成功 false:失败
	 */
	public boolean preserve(int id){
		return doPreserve(id,true);
	}

	/**
	 * 取消收藏新闻
	 * @param id 新闻id
	 * @return true:成功 false:失败
	 */
	public boolean cancelPreserve(int id){
		return doPreserve(id,false);
	}
	
	/**
	 * 收藏或取消收藏
	 * @param id 
	 * @param preserve true:收藏  false:取消收藏
	 * @return true:成功  false:失败
	 */
	private boolean doPreserve(int id ,boolean preserve){
		SQLiteDatabase db = getWritableDatabase();
		if(db == null){
			return false;
		}
		
		ContentValues cv = new ContentValues();
		if(preserve){
			cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.PRESERVE_YES);
		}else{
			cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.PRESERVE_NO);
		}
		
		db.update(DBHelper.TABLE_NAME_NEWS, cv, DBHelper.COLUMN_SERVER_ID + "=" + id, null);
		
//		db.close();
		return true;
	}
	
	/**
	 * 取消收藏
	 * @param data 新闻列表
	 * @return true:成功 false:失败
	 */
	public boolean cancelPreserveList(List<News> data){
		SQLiteDatabase db = getWritableDatabase();
		if(db == null || data == null){
			return false;
		}
		
		StringBuffer idCollect = new StringBuffer("(");//id集
		int size = data.size();
		for(int i=0 ; i<size ; i++){
			if(i>0){
				idCollect.append(",");	
			}
			idCollect.append(data.get(i).getId());
		}
		idCollect.append(")");
		
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.PRESERVE_NO);
		return db.update(DBHelper.TABLE_NAME_NEWS, cv, DBHelper.COLUMN_SERVER_ID + " in " + idCollect.toString().trim(), null) > 0;
	}
	
	/**
	 * 查询新闻是否已收藏
	 * @param id 新闻在服务器的id
	 * @return true:已收藏 false:未收藏
	 */
	public boolean isPreserve(int id){
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return false;
		}
		
		Cursor cursor = db.query(DBHelper.TABLE_NAME_NEWS, 
				new String[]{DBHelper.COLUMN_ID}, 
				DBHelper.COLUMN_SERVER_ID + "=" + id + " and " + DBHelper.COLUMN_IS_PRESERVE + "='" + DBHelper.PRESERVE_YES + "'", null, null, null, null);
		boolean res = cursor.getCount()>0;
		cursor.close();
//		db.close();
		
		return res;
	}
	
	
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
	
	/**
	 * @deprecated 在数据库dao停止使用
	 */
	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 获取今日快讯栏目ID,这里默认为其非今日快讯的返回值
	 * @return
	 */
	public int getBrandId(){
		return DEFAULT_NOT_TODAY_BRAND;
	}
	
	/**
	 * 设置今日快讯栏目ID
	 * @param id
	 */
	public void setBrandId(int id){}
	
	
}
