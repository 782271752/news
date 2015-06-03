package Investmentletters.android.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Sqlite���ݿ������
 * 
 * @author liang
 */
public class DBHelper extends SQLiteOpenHelper {

	/** ���� */
	private static DBHelper dbHelper = null;

	/** ���ݿ����� */
	private static final String DATABASE_NAME = "db_investment.db";
	/** ���ݿ�汾�� */
	private static final int DATABASE_VERSION = 3;

	/** ���ݱ����ƣ����� */
	public static final String TABLE_NAME_NEWS = "t_news";
	/** ���ݱ����ƣ���ͼ������ϸ��Ϣ */
	public static final String TABLE_NAME_IMAGE_NEWS_DETAIL = "t_images_news_preserve";
	/** ���ݱ�����:��Ʊ */
	public static final String TABLE_NAME_SHARE = "t_share";

	/** ���ݱ��ֶ�:����id */
	public static final String COLUMN_ID = "_ID";
	/** ���ݱ��ֶ�:�����ڷ��������ݿ��id */
	public static final String COLUMN_SERVER_ID = "serverId";
	/** ���ݱ��ֶ�:���� */
	public static final String COLUMN_TITLE = "title";
	/** ���ݿ��ֶΣ�������Ҫ */
	public static final String COLUMN_SUMMARIZE = "summarize";
	/** ���ݱ��ֶ�:ʱ�� */
	public static final String COLUMN_TIME = "publicTime";
	/** ���ݱ��ֶ�:����ͼ */
	public static final String COLUMN_MIN_IMAGE = "minImage";
	/** ���ݱ��ֶ�:��ͼ */
	public static final String COLUMN_BIG_IMAGE = "bigImage";
	/** ���ݱ��ֶ�:���� */
	public static final String COLUMN_CONTENT = "content";
	/** ���ݱ��ֶΣ��ö� */
	public static final String COLUMN_TOP = "top";
	/** ���ݱ��ֶ�:�������� ��ϸ��MySQLite.TYPE_xxx */
	public static final String COLUMN_CONTENT_TYPE = "cType";
	/** ���ݱ��ֶ�:���տ�Ѷ��ĿID */
	public static final String COLUMN_BRAND_ID = "brandId";
	/** ���ݱ��ֶ�:�Ƿ�Ϊ�ղ� ��ϸ��MySQLite.PRESERVE_xxx */
	public static final String COLUMN_IS_PRESERVE = "isPreserve";
	/** ���ݱ��ֶ�:�ղ��� */
	public static final String COLUMN_USER = "user";

	/** ���ݱ��ֶΣ��Ƿ�Ϊ�ղ� :�� */
	public static final String VALUE_IS_PRESERVE_YES = "Y";
	/** ���ݱ��ֶΣ��Ƿ�Ϊ�ղ� :�� */
	public static final String VALUE_IS_PRESERVE_NO = "N";

	/** ��������:Ͷ�ʿ�Ѷ */
	public static final int TYPE_TOUZI = 1;
	/** ��������:���տ�Ѷ */
	public static final int TYPE_JINRI = 2;
	/** ��������:��ͼ���� */
	public static final int TYPE_MEITU = 3;
	/** ��������:��˾�״� */
	public static final int TYPE_LEIDA = 4;
	/** ��������:���ݽ��� */
	public static final int TYPE_JIEMA = 5;
	/** ��������:���̽�� */
	public static final int TYPE_CAOPAN = 6;
	/** ��������:ʱ�¾۽� */
	public static final int TYPE_FOCUS = 7;
	/** ��������:��Ϣ���� */
	public static final int TYPE_PUSH_MSG = 8;

	/** �Ƿ�Ϊ�ղ�:��Y */
	public static final String PRESERVE_YES = "Y";
	/** �Ƿ�Ϊ�ղ�:��N */
	public static final String PRESERVE_NO = "N";

	/** ���ֶΣ���Ʊ�� */
	public static final String COLUMN_NUMBER = "number";
	/** ���ֶΣ���Ʊ���� */
	public static final String COLUMN_NAME = "name";
	
	/**��Դ������*/
	private AssetManager assetManager = null;

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		assetManager = context.getAssets();
	}

	/**
	 * ��ȡʵ��,����ģʽ
	 * 
	 * @param context
	 * @return
	 */
	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}

		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		StringBuffer str = new StringBuffer();

		// ���������б����ݱ�
		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_NEWS);
		str.append("]([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// �����ڷ��������ݿ��id
		str.append(COLUMN_SERVER_ID);
		str.append("] INTEGER NOT NULL,[");

		// ����
		str.append(COLUMN_TITLE);
		str.append("] VARCHAR(255),[");

		// ������Ҫ
		str.append(COLUMN_SUMMARIZE);
		str.append("] VARCHAR(255),[");

		// ʱ��
		str.append(COLUMN_TIME);
		str.append("] DATE,[");

		// ����ͼ
		str.append(COLUMN_MIN_IMAGE);
		str.append("] VARCHAR(255),[");

		// ��ͼ
		str.append(COLUMN_BIG_IMAGE);
		str.append("] VARCHAR(255),[");

		// ����
		str.append(COLUMN_CONTENT);
		str.append("] TEXT,[");

		// �ö�
		str.append(COLUMN_TOP);
		str.append("] INTEGER NOT NULL DEFAULT -1,[");

		// ��������
		str.append(COLUMN_CONTENT_TYPE);
		str.append("] INTEGER NOT NULL,[");

		// ���տ�Ѷ����ID
		str.append(COLUMN_BRAND_ID);
		str.append("] INTEGER NOT NULL DEFAULT -1,[");

		// �ղ�
		str.append(COLUMN_IS_PRESERVE);
		str.append("] CHAR NOT NULL DEFAULT " + PRESERVE_NO + ",[");

		// �ղ���
		str.append(COLUMN_USER);
		str.append("] VARCHAR(11))");

		db.execSQL(str.toString());

		// ==================================================================
		// ------------------------------------------------------------------
		// ==================================================================

		// ������ͼ������ϸ��Ϣ���ݱ�
		str.delete(0, str.length());

		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_IMAGE_NEWS_DETAIL);
		str.append("] ([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// ������������ t_news��serverId ��Ϊ��ͼ���߼��ɵ� t_news����
		str.append(COLUMN_SERVER_ID);
		str.append("] INTEGER NOT NULL,[");

		// ����
		str.append(COLUMN_TITLE);
		str.append("] VARCHAR(255),[");

		// ʱ��
		str.append(COLUMN_TIME);
		str.append("] DATE,[");

		// ����ͼ
		str.append(COLUMN_MIN_IMAGE);
		str.append("] VARCHAR(255),[");

		// ����
		str.append(COLUMN_CONTENT);
		str.append("] TEXT)");

		db.execSQL(str.toString());

		// ==================================================================
		// ------------------------------------------------------------------
		// ==================================================================

		// ������Ʊ���ݱ�
		str.delete(0, str.length());

		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_SHARE);
		str.append("] ([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// ��Ʊ��
		str.append(COLUMN_NUMBER);
		str.append("] VARCHAR(255),[");

		// ��Ʊ����
		str.append(COLUMN_NAME);
		str.append("] VARCHAR(255))");
		db.execSQL(str.toString());
		
		//------------------------������Ʊ����-------------------
		InputStream is = null;
		
		try{
			is = assetManager.open("share_data");//�򿪹�Ʊ���ݵ�����Դ
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(is == null){
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tempData = null;//��������
		try {
			while((tempData = br.readLine()) != null){//��������
				db.execSQL(tempData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NEWS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMAGE_NEWS_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SHARE);
		
		onCreate(db);
	}

	/**
	 * �����棬������ղص�����
	 * 
	 * @return true:�ɹ� false:ʧ��
	 */
	public boolean cleanCache() {
		SQLiteDatabase db = null;

		try {
			db = getWritableDatabase();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (db == null) {
			return false;
		}

		List<Integer> delList = new ArrayList<Integer>();// ɾ���������б�ķ�����id
		String selection = COLUMN_IS_PRESERVE + "='" + PRESERVE_NO + "'";
		Cursor cursor = db.query(TABLE_NAME_NEWS, new String[] {
				COLUMN_SERVER_ID, COLUMN_CONTENT_TYPE }, selection, null, null,
				null, null);
		int index_id = cursor.getColumnIndex(COLUMN_SERVER_ID);
		int index_type = cursor.getColumnIndex(COLUMN_CONTENT_TYPE);
		while (cursor.moveToNext()) {
			if (cursor.getInt(index_type) == TYPE_MEITU) {
				delList.add(cursor.getInt(index_id));
			}
		}

		// ɾ��
		db.execSQL("delete from " + TABLE_NAME_NEWS + " where "
				+ COLUMN_IS_PRESERVE + "='" + PRESERVE_NO + "'");

		// ɾ��ͼƬ��ϸ
		StringBuffer sb = new StringBuffer();
		sb.append("delete from " + TABLE_NAME_IMAGE_NEWS_DETAIL + " where "
				+ COLUMN_SERVER_ID + " in (");

		int size = delList.size();
		for (int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(delList.get(i));
		}

		sb.append(")");
		db.execSQL(sb.toString().trim());

		cursor.close();
		// db.close();

		return true;
	}

}
