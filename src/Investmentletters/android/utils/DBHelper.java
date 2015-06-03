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
 * Sqlite数据库帮助类
 * 
 * @author liang
 */
public class DBHelper extends SQLiteOpenHelper {

	/** 单例 */
	private static DBHelper dbHelper = null;

	/** 数据库名称 */
	private static final String DATABASE_NAME = "db_investment.db";
	/** 数据库版本号 */
	private static final int DATABASE_VERSION = 3;

	/** 数据表名称：新闻 */
	public static final String TABLE_NAME_NEWS = "t_news";
	/** 数据表名称：美图在线详细信息 */
	public static final String TABLE_NAME_IMAGE_NEWS_DETAIL = "t_images_news_preserve";
	/** 数据表名称:股票 */
	public static final String TABLE_NAME_SHARE = "t_share";

	/** 数据表字段:本地id */
	public static final String COLUMN_ID = "_ID";
	/** 数据表字段:新闻在服务器数据库的id */
	public static final String COLUMN_SERVER_ID = "serverId";
	/** 数据表字段:标题 */
	public static final String COLUMN_TITLE = "title";
	/** 数据库字段：内容提要 */
	public static final String COLUMN_SUMMARIZE = "summarize";
	/** 数据表字段:时间 */
	public static final String COLUMN_TIME = "publicTime";
	/** 数据表字段:缩略图 */
	public static final String COLUMN_MIN_IMAGE = "minImage";
	/** 数据表字段:大图 */
	public static final String COLUMN_BIG_IMAGE = "bigImage";
	/** 数据表字段:内容 */
	public static final String COLUMN_CONTENT = "content";
	/** 数据表字段：置顶 */
	public static final String COLUMN_TOP = "top";
	/** 数据表字段:新闻类型 详细见MySQLite.TYPE_xxx */
	public static final String COLUMN_CONTENT_TYPE = "cType";
	/** 数据表字段:今日快讯栏目ID */
	public static final String COLUMN_BRAND_ID = "brandId";
	/** 数据表字段:是否为收藏 详细见MySQLite.PRESERVE_xxx */
	public static final String COLUMN_IS_PRESERVE = "isPreserve";
	/** 数据表字段:收藏者 */
	public static final String COLUMN_USER = "user";

	/** 数据表字段，是否为收藏 :是 */
	public static final String VALUE_IS_PRESERVE_YES = "Y";
	/** 数据表字段，是否为收藏 :否 */
	public static final String VALUE_IS_PRESERVE_NO = "N";

	/** 新闻类型:投资快讯 */
	public static final int TYPE_TOUZI = 1;
	/** 新闻类型:今日快讯 */
	public static final int TYPE_JINRI = 2;
	/** 新闻类型:美图在线 */
	public static final int TYPE_MEITU = 3;
	/** 新闻类型:公司雷达 */
	public static final int TYPE_LEIDA = 4;
	/** 新闻类型:数据解码 */
	public static final int TYPE_JIEMA = 5;
	/** 新闻类型:操盘解读 */
	public static final int TYPE_CAOPAN = 6;
	/** 新闻类型:时事聚焦 */
	public static final int TYPE_FOCUS = 7;
	/** 新闻类型:消息推送 */
	public static final int TYPE_PUSH_MSG = 8;

	/** 是否为收藏:是Y */
	public static final String PRESERVE_YES = "Y";
	/** 是否为收藏:否N */
	public static final String PRESERVE_NO = "N";

	/** 表字段：股票号 */
	public static final String COLUMN_NUMBER = "number";
	/** 表字段：股票名称 */
	public static final String COLUMN_NAME = "name";
	
	/**资源管理器*/
	private AssetManager assetManager = null;

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		assetManager = context.getAssets();
	}

	/**
	 * 获取实例,单例模式
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

		// 创建新闻列表数据表
		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_NEWS);
		str.append("]([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// 新闻在服务器数据库的id
		str.append(COLUMN_SERVER_ID);
		str.append("] INTEGER NOT NULL,[");

		// 标题
		str.append(COLUMN_TITLE);
		str.append("] VARCHAR(255),[");

		// 内容提要
		str.append(COLUMN_SUMMARIZE);
		str.append("] VARCHAR(255),[");

		// 时间
		str.append(COLUMN_TIME);
		str.append("] DATE,[");

		// 缩略图
		str.append(COLUMN_MIN_IMAGE);
		str.append("] VARCHAR(255),[");

		// 大图
		str.append(COLUMN_BIG_IMAGE);
		str.append("] VARCHAR(255),[");

		// 内容
		str.append(COLUMN_CONTENT);
		str.append("] TEXT,[");

		// 置顶
		str.append(COLUMN_TOP);
		str.append("] INTEGER NOT NULL DEFAULT -1,[");

		// 新闻类型
		str.append(COLUMN_CONTENT_TYPE);
		str.append("] INTEGER NOT NULL,[");

		// 今日快讯栏日ID
		str.append(COLUMN_BRAND_ID);
		str.append("] INTEGER NOT NULL DEFAULT -1,[");

		// 收藏
		str.append(COLUMN_IS_PRESERVE);
		str.append("] CHAR NOT NULL DEFAULT " + PRESERVE_NO + ",[");

		// 收藏者
		str.append(COLUMN_USER);
		str.append("] VARCHAR(11))");

		db.execSQL(str.toString());

		// ==================================================================
		// ------------------------------------------------------------------
		// ==================================================================

		// 创建美图在线详细信息数据表
		str.delete(0, str.length());

		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_IMAGE_NEWS_DETAIL);
		str.append("] ([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// 所属的新闻在 t_news的serverId 因为美图在线集成到 t_news中了
		str.append(COLUMN_SERVER_ID);
		str.append("] INTEGER NOT NULL,[");

		// 标题
		str.append(COLUMN_TITLE);
		str.append("] VARCHAR(255),[");

		// 时间
		str.append(COLUMN_TIME);
		str.append("] DATE,[");

		// 缩略图
		str.append(COLUMN_MIN_IMAGE);
		str.append("] VARCHAR(255),[");

		// 内容
		str.append(COLUMN_CONTENT);
		str.append("] TEXT)");

		db.execSQL(str.toString());

		// ==================================================================
		// ------------------------------------------------------------------
		// ==================================================================

		// 创建股票数据表
		str.delete(0, str.length());

		str.append("CREATE TABLE [");
		str.append(TABLE_NAME_SHARE);
		str.append("] ([");

		// id
		str.append(COLUMN_ID);
		str.append("] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,[");

		// 股票号
		str.append(COLUMN_NUMBER);
		str.append("] VARCHAR(255),[");

		// 股票名称
		str.append(COLUMN_NAME);
		str.append("] VARCHAR(255))");
		db.execSQL(str.toString());
		
		//------------------------插数股票数据-------------------
		InputStream is = null;
		
		try{
			is = assetManager.open("share_data");//打开股票数据导入资源
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if(is == null){
			return;
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String tempData = null;//插入数据
		try {
			while((tempData = br.readLine()) != null){//插入数据
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
	 * 清理缓存，不清除收藏的数据
	 * 
	 * @return true:成功 false:失败
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

		List<Integer> delList = new ArrayList<Integer>();// 删除的新闻列表的服务器id
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

		// 删除
		db.execSQL("delete from " + TABLE_NAME_NEWS + " where "
				+ COLUMN_IS_PRESERVE + "='" + PRESERVE_NO + "'");

		// 删除图片详细
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
