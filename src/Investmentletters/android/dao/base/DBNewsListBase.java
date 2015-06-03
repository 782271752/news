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
 * �����б����ݿ�DAo����
 * @author liang
 */
public abstract class DBNewsListBase extends NewsListBase<News> {
	
	/**��������:Ͷ�ʿ�Ѷ*/
	public static final int TYPE_INVESTMENTLETTERS = DBHelper.TYPE_TOUZI;
	/**��������:���տ�Ѷ*/
	public static final int TYPE_TODAY = DBHelper.TYPE_JINRI;
	/**�������ͣ���˾�״�*/
	public static final int TYPE_FADAR = DBHelper.TYPE_LEIDA;
	/**�������ͣ����ݽ���*/
	public static final int TYPE_DECODER = DBHelper.TYPE_JIEMA;
	/**��������:��ͼ����*/
	public static final int TYPE_IMAGES_ON_LINE = DBHelper.TYPE_MEITU;
	/**�������ͣ����̽��*/
	public static final int TYPE_TRADER_EXPLAIN = DBHelper.TYPE_CAOPAN;
	/**�������ͣ�ʱ�¾۽�*/
	public static final int TYPE_FOCUS = DBHelper.TYPE_FOCUS;
	/**�������ͣ���Ϣ����*/
	public static final int TYPE_PUSH_MSG = DBHelper.TYPE_PUSH_MSG;
	
	/**Sqlite���ݿ������*/
	private DBHelper dbHelper = null;
	
	/**Ĭ��ÿҳ��������*/
	private final int PAGE_ZISE = 20;
	/**�ǽ��տ�Ѷ��ĿĬ��ֵ*/
	private final int DEFAULT_NOT_TODAY_BRAND = -1;
	
	public DBNewsListBase(Context context){
		dbHelper = DBHelper.getInstance(context);
	}
	
	/**
	 * ��������<br/>
	 * <strong>ע�⣺����ǽ��տ�Ѷ����Ŀ�ģ���Ҫ��ִ�У�setBrandId(int id)</strong>
	 * @param data
	 * @return true:�ɹ� false:ʧ��
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
			
//			System.out.println("����:"+item.getId()+"  "+db.isOpen());
			
			cursor = db.query(DBHelper.TABLE_NAME_NEWS, new String[]{}, DBHelper.COLUMN_SERVER_ID+"="+item.getId()+" and "+DBHelper.COLUMN_CONTENT_TYPE+"="+getType(), null, null, null, null);
			if(cursor.getCount()>0){//�������ݣ����ظ�����
				cursor.close();
				continue;
			}
			
			cursor.close();
			
			cv.put(DBHelper.COLUMN_SERVER_ID, item.getId());//�����ڷ��������ݿ��id
			cv.put(DBHelper.COLUMN_TITLE, item.getTitle());//����
			cv.put(DBHelper.COLUMN_SUMMARIZE, item.getSummary());//������Ҫ
			cv.put(DBHelper.COLUMN_TIME, item.getTime());//ʱ��
			cv.put(DBHelper.COLUMN_MIN_IMAGE, item.getMinImg());//����ͼ
			cv.put(DBHelper.COLUMN_BIG_IMAGE, item.getBigImg());//��ͼ
			cv.put(DBHelper.COLUMN_CONTENT, item.getContent());//����
			cv.put(DBHelper.COLUMN_TOP, item.getTop());//�ö�
			cv.put(DBHelper.COLUMN_CONTENT_TYPE, getType());//��������,����Ҫע��
			cv.put(DBHelper.COLUMN_BRAND_ID, getBrandId());//���տ�Ѷ����ID�������տ�Ѷ��Ŀ��������Ҫ��дgetBrandId()
			cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.VALUE_IS_PRESERVE_NO);//�Ƿ�Ϊ�ղ�
			cv.put(DBHelper.COLUMN_USER, "");//�ղ���
			
//			System.out.println("����:"+item.getId());
			res = db.insert(DBHelper.TABLE_NAME_NEWS, null, cv) >= 0;
		}
		
//		db.close();
		
		
		return res;
	}
	
	/**
	 * ���������б����ݿ�
	 * @param data Ҫ���µ������б�
	 * @return true:�ɹ� false:ʧ��
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
			if(item.getTitle().trim().equals("")){//ɾ��
				db.delete(DBHelper.TABLE_NAME_NEWS, DBHelper.COLUMN_SERVER_ID+"="+id, null);
				db.delete(DBHelper.TABLE_NAME_IMAGE_NEWS_DETAIL, DBHelper.COLUMN_SERVER_ID+"="+id, null);//ɾ����ͼ�������
			}else{//�޸�
				cv.put(DBHelper.COLUMN_SERVER_ID, item.getId());//�����ڷ��������ݿ��id
				cv.put(DBHelper.COLUMN_TITLE, item.getTitle());//����
				cv.put(DBHelper.COLUMN_SUMMARIZE, item.getSummary());//������Ҫ
				cv.put(DBHelper.COLUMN_TIME, item.getTime());//ʱ��
				cv.put(DBHelper.COLUMN_MIN_IMAGE, item.getMinImg());//����ͼ
				cv.put(DBHelper.COLUMN_BIG_IMAGE, item.getBigImg());//��ͼ
				cv.put(DBHelper.COLUMN_CONTENT, item.getContent());//����
				cv.put(DBHelper.COLUMN_TOP, item.getTop());//�ö�
				cv.put(DBHelper.COLUMN_CONTENT_TYPE, getType());//��������,����Ҫע��
				cv.put(DBHelper.COLUMN_BRAND_ID, getBrandId());//���տ�Ѷ����ID�������տ�Ѷ��Ŀ��������Ҫ��дgetBrandId()
//				cv.put(DBHelper.COLUMN_IS_PRESERVE, DBHelper.VALUE_IS_PRESERVE_NO);//�Ƿ�Ϊ�ղ�
//				cv.put(DBHelper.COLUMN_USER, "");//�ղ���
				
				db.update(DBHelper.TABLE_NAME_NEWS,cv, DBHelper.COLUMN_SERVER_ID+"="+id, null);	
			}
			
		}
		
//		db.close();
		
		return true;
	}
	
	/**
	 * ��ȡ�a���б�<br/>
	 * <strong>ע�⣺����ǽ��տ�Ѷ����Ŀ�ģ���Ҫ��ִ�У�setBrandId(int id)</strong>
	 * @return null:ʧ�� 
	 */
	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		return query(-1,-1);
	}
	
	/**
	 * ��ȡ����<br/>
	 * <strong>ע�⣺����ǽ��տ�Ѷ����Ŀ�ģ���Ҫ��ִ�У�setBrandId(int id)</strong>
	 * @param minId ��С��id
	 * @return null:ʧ�� 
	 */
	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return query(minId,-1);
	}
	
	/**
	 * ˢ��<br/>
	 * <strong>ע�⣺����ǽ��տ�Ѷ����Ŀ�ģ���Ҫ��ִ�У�setBrandId(int id)</strong>
	 * @param maxId ����id
	 * @return null:ʧ�� 
	 */
	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		return query(-1,maxId);
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
		
		int type = -1;//�����ں�̨�����ͣ������������
		switch(getType()){
			case TYPE_INVESTMENTLETTERS://Ͷ�ʿ�Ѷ
				type = NewsListBase.TYPE_INVESTMENTLETTERS;
				break;
				
			case TYPE_TODAY://���տ�Ѷ
				type = NewsListBase.TYPE_TODAY_NO_BRAND;
				break;
				
			case TYPE_FADAR://��˾�״�
				type = NewsListBase.TYPE_FADAR;
				break;
				
			case TYPE_DECODER://���ݽ���
				type = NewsListBase.TYPE_DECODER;
				break;
				
			case TYPE_TRADER_EXPLAIN: //���̽��
				type = NewsListBase.TYPE_TRADER_EXPLAIN;
				break;
				
			case TYPE_FOCUS: //ʱ�¾۽�
				type = NewsListBase.TYPE_FOCUS;
				break;
		}
		
		while(cursor.moveToNext()){
			item = new News();
			
			item.setType(type);//������������
			
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
	 * �ղ�����
	 * @param id ����id
	 * @return true:�ɹ� false:ʧ��
	 */
	public boolean preserve(int id){
		return doPreserve(id,true);
	}

	/**
	 * ȡ���ղ�����
	 * @param id ����id
	 * @return true:�ɹ� false:ʧ��
	 */
	public boolean cancelPreserve(int id){
		return doPreserve(id,false);
	}
	
	/**
	 * �ղػ�ȡ���ղ�
	 * @param id 
	 * @param preserve true:�ղ�  false:ȡ���ղ�
	 * @return true:�ɹ�  false:ʧ��
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
	 * ȡ���ղ�
	 * @param data �����б�
	 * @return true:�ɹ� false:ʧ��
	 */
	public boolean cancelPreserveList(List<News> data){
		SQLiteDatabase db = getWritableDatabase();
		if(db == null || data == null){
			return false;
		}
		
		StringBuffer idCollect = new StringBuffer("(");//id��
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
	 * ��ѯ�����Ƿ����ղ�
	 * @param id �����ڷ�������id
	 * @return true:���ղ� false:δ�ղ�
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
	
	/**
	 * @deprecated �����ݿ�daoֹͣʹ��
	 */
	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * ��ȡ���տ�Ѷ��ĿID,����Ĭ��Ϊ��ǽ��տ�Ѷ�ķ���ֵ
	 * @return
	 */
	public int getBrandId(){
		return DEFAULT_NOT_TODAY_BRAND;
	}
	
	/**
	 * ���ý��տ�Ѷ��ĿID
	 * @param id
	 */
	public void setBrandId(int id){}
	
	
}
