package Investmentletters.android.dao;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.dao.base.AbsDBBase;
import Investmentletters.android.entity.Hot_News;
import Investmentletters.android.utils.DBHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 股票列表DB dao
 * @author liang
 */
public class DBShare extends AbsDBBase<List<Hot_News>> {

	public DBShare(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 查询所有股票
	 * @param pattern 关键词
	 */
	public List<Hot_News> query(String pattern) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getReadableDatabase();
		if(db == null){
			return null;
		}
		
		String[] columns = new String[]{
			DBHelper.COLUMN_NUMBER,
			DBHelper.COLUMN_NAME
		};
		
		//生成查询条件
		StringBuffer selection = new StringBuffer();
		String[] regs = pattern.split(" ");//以空格分开关键词
		
		selection.append(DBHelper.COLUMN_NUMBER+ " like '%"+pattern+"%' or ");
		selection.append(DBHelper.COLUMN_NAME+ " like '%"+pattern+"%'");
		
		for(int i=0 ; i<regs.length ; i++){
			selection.append(" or "+DBHelper.COLUMN_NUMBER+ " like '%"+regs[i]+"%' or ");
			selection.append(DBHelper.COLUMN_NAME+ " like '%"+regs[i]+"%'");			
		}
		
		
		Cursor cursor = db.query(DBHelper.TABLE_NAME_SHARE,
								columns, 
								selection.toString(), null, null, null, DBHelper.COLUMN_ID+" ASC");
		
		List<Hot_News> result = new ArrayList<Hot_News>();
		Hot_News item = null;
		while(cursor.moveToNext()){
			item = new Hot_News();
			item.settitle(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NUMBER)));
			item.seturl(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
			
			result.add(item);
		}
		cursor.close();
		
		return result;
	}

	@Override
	public boolean add(List<Hot_News> data, int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(List<Hot_News> data, int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Hot_News> query(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
