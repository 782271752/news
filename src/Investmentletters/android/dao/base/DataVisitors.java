package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import Investmentletters.android.dao.DBImageDetail;
import Investmentletters.android.dao.DBLastTime;
import Investmentletters.android.dao.DBShare;
import Investmentletters.android.dao.HotNews;
import Investmentletters.android.dao.HotShare;
import Investmentletters.android.entity.Image;
import Investmentletters.android.entity.News;
import android.os.AsyncTask;

/**
 * 数据访问协调类
 * 
 * @author liang
 */
public class DataVisitors {

	/**
	 * 获取数据默认列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param cb
	 *            回调
	 * @param what
	 *            回调的标记
	 */
	public void getListDefault(NewsListBase<?> model, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {

			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[1];
				what = (Integer) params[2];
				return ((NewsListBase<?>) params[0]).getListDefault();
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, cb, what);
	}

	/**
	 * 获取数据库数据默认列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param cb
	 *            回调
	 * @param what
	 *            回调的标记
	 */
	public void getDBListDefault(DBNewsListBase model, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {

			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[1];
				what = (Integer) params[2];
				return ((DBNewsListBase) params[0]).getListDefault();
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, cb, what);
	}

	/**
	 * 评论 获取下拉刷机列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param newId
	 *            当前新闻的Id
	 * @param cb
	 *            回调
	 * @param what
	 *            回调的标记
	 */
	public void getFreshComment(CommentNewListBase<?> model, int newId,
			CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {

			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((CommentNewListBase<?>) params[0])
						.getFresh((Integer) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, newId, cb, what);
	}

	/**
	 * 获取更多数据列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param minId
	 *            列表中最小的id
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void getMore(NewsListBase<?> model, int minId, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((NewsListBase<?>) params[0])
						.getMore((Integer) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, minId, cb, what);
	}

	public void getDBMore(DBNewsListBase model, int minId, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((DBNewsListBase) params[0])
						.getMore((Integer) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, minId, cb, what);
	}

	/**
	 * 获取更多数据列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param newId
	 *            当前新闻的Id
	 * @param minId
	 *            列表中最小的id
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void getCommentMore(CommentNewListBase<?> model, int newId,
			int minId, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[3];
				what = (Integer) params[4];
				return ((CommentNewListBase<?>) params[0]).getMore(
						(Integer) params[1], (Integer) params[2]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, newId, minId, cb, what);
	}

	/**
	 * 获取刷新数据列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param minId
	 *            列表中最大的id
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void getFresh(NewsListBase<?> model, int maxId, CallBack cb, int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((NewsListBase<?>) params[0])
						.getFresh((Integer) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, maxId, cb, what);
	}

	/**
	 * 获取数据库刷新数据列表
	 * 
	 * @param model
	 *            要访问的model类
	 * @param minId
	 *            列表中最小的id
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void getDBFresh(DBNewsListBase model, int maxId, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((DBNewsListBase) params[0])
						.getFresh((Integer) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, maxId, cb, what);
	}

	/**
	 * 获取修改的数据列表
	 * 
	 * @param model
	 *            model类
	 * @param lastTime
	 *            最后修改时间
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void getUpdate(NewsListBase<?> model, String lastTime, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, List<?>>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected List<?> doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				return ((NewsListBase<?>) params[0])
						.getUpdate((String) params[1]);
			}

			protected void onPostExecute(List<?> result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, lastTime, cb, what);
	}

	/**
	 * 执行Post
	 * 
	 * @param model
	 *            model类
	 * @param params
	 *            post 参数
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void doPost(AbsOtherBase<?> model, List<NameValuePair> params,
			CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];
				List<NameValuePair> postParam = null;
				if (params[1] != null) {
					postParam = new ArrayList<NameValuePair>();
					for (Object obj : (List<?>) params[1]) {
						postParam.add((NameValuePair) obj);
					}
				}
				return ((AbsOtherBase<?>) params[0]).doPost(postParam);
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, params, cb, what);
	}

	/**
	 * 执行get
	 * 
	 * @param model
	 *            model类
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void doGet(AbsOtherBase<?> model, CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[1];
				what = (Integer) params[2];
				return ((AbsOtherBase<?>) params[0]).doGet();
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, cb, what);

	}

	/**
	 * 增加数据到数据库,现只是插入数据到美图在线详细
	 * 
	 * @param model
	 *            现只是美图在线dao实例
	 * @param data
	 *            数据
	 * @param id
	 *            美图在线id
	 * @param cb
	 *            回调 ，其中的res: false:失败 true:成功
	 * @param what
	 *            回调标记
	 */
	public void dbAdd(AbsDBBase<?> model, List<?> data, int id, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[3];
				what = (Integer) params[4];

				List<?> data = (List<?>) params[1];
				List<Image> tempAddData = new ArrayList<Image>();
				for (Object item : data) {
					tempAddData.add((Image) item);
				}

				return ((DBImageDetail) params[0]).add(tempAddData,
						(Integer) params[2]);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, data, id, cb, what);
	}

	/**
	 * 增加新闻列表到数据库
	 * 
	 * @param model
	 * @param data
	 * @param cb
	 *            回调 ，其中的res: false:失败 true:成功
	 * @param what
	 */
	public void dbAdd(DBNewsListBase model, List<?> data, CallBack cb, int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				List<?> data = (List<?>) params[1];
				List<News> tempIData = new ArrayList<News>();
				for (Object item : data) {
					tempIData.add((News) item);
				}
				return ((DBNewsListBase) params[0]).add(tempIData);

			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, data, cb, what);
	}

	/**
	 * 删除美图在线详细数据
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 *            cb 回调 ，其中的res: false:失败 true:成功
	 * @param what
	 *            回调标记
	 */
	public void dbDelete(AbsDBBase<?> model, int id, CallBack cb, int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				List<?> data = (List<?>) params[1];
				List<News> tempIData = new ArrayList<News>();
				for (Object item : data) {
					tempIData.add((News) item);
				}
				return ((AbsDBBase<?>) params[0]).delete((Integer) params[1]);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, id, cb, what);
	}

	/**
	 * 更新美图在线详细信息
	 * 
	 * @param model
	 * @param data
	 * @param id
	 * @param cb
	 *            回调 ，其中的res: false:失败 true:成功
	 * @param what
	 */
	public void dbUpdate(AbsDBBase<?> model, List<?> data, int id, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[3];
				what = (Integer) params[4];

				List<?> data = (List<?>) params[1];
				List<Image> tempUpdateData = new ArrayList<Image>();
				for (Object item : data) {
					tempUpdateData.add((Image) item);
				}
				return ((DBImageDetail) params[0]).update(tempUpdateData,
						(Integer) params[2]);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, data, id, cb, what);
	}

	/**
	 * 更新数据库新闻列表
	 * 
	 * @param model
	 * @param data
	 * @param id
	 * @param cb
	 *            回调 ，其中的res: false:失败 true:成功
	 * @param what
	 */
	public void dbUpdate(DBNewsListBase model, List<?> data, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				List<?> data = (List<?>) params[1];
				List<News> tempUData = new ArrayList<News>();
				for (Object item : data) {
					tempUData.add((News) item);
				}
				return ((DBNewsListBase) params[0]).update(tempUData);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, data, cb, what);
	}

	/**
	 * 查讲美图在线详细数据
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 *            回调 ，其中的res: null:失败 List&lt;Image&gt;成功
	 * @param what
	 *            回调标记
	 */
	public void query(AbsDBBase<?> model, int id, CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				return ((AbsDBBase<?>) params[0]).query((Integer) params[1]);
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, id, cb, what);
	}

	/**
	 * 收藏新闻
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 * @param what
	 */
	public void preserve(DBNewsListBase model, int id, CallBack cb, int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				return ((DBNewsListBase) params[0])
						.preserve((Integer) params[1]);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, id, cb, what);
	}

	/**
	 * 取消收藏新闻
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 * @param what
	 */
	public void cancelPreserve(DBNewsListBase model, int id, CallBack cb,
			int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				return ((DBNewsListBase) params[0])
						.cancelPreserve((Integer) params[1]);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, id, cb, what);
	}

	/**
	 * 更新新闻列表
	 * 
	 * @param model
	 *            http新闻列表dao
	 * @param dbModel
	 *            本地新闻列表dao
	 * @param dbLastTime
	 *            最后时间dao
	 * @param type
	 *            新闻类型，具体参见DBHelper.TYPE_xxx
	 * @param cb
	 *            回调
	 * @param what
	 *            回调标记
	 */
	public void updateList(NewsListBase<?> model, DBNewsListBase dbModel,
			DBLastTime dbLastTime, int type, CallBack cb, int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[4];
				what = (Integer) params[5];

				NewsListBase<?> model = (NewsListBase<?>) params[0];
				DBNewsListBase dbModel = (DBNewsListBase) params[1];
				DBLastTime dbLastTime = (DBLastTime) params[2];
				int type = (Integer) params[3];// 类型

				String lastTime = dbLastTime.getLastTime(type);
				List<?> data = model.getUpdate(lastTime);
				System.out.println("更新：" + lastTime + "  " + data);
				if (data == null) {
					return false;
				}

				List<News> newsData = new ArrayList<News>();
				for (Object item : data) {
					newsData.add((News) item);
				}
				System.out.println("更新：" + newsData.size());
				return dbModel.update(newsData);
			}

			protected void onPostExecute(Boolean result) {
				System.out.println("更新结果：" + result);
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, dbModel, dbLastTime, type, cb, what);
	}
	/**
	 * 查询新闻
	 * 
	 * @param model
	 * @param pattern
	 *            关键词
	 * @param cb
	 *            回调 ，其中的res: null:失败 List&lt;Share&gt;成功
	 * @param what
	 *            回调标记
	 */
	public void queryNews(DBShare model, String pattern, CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				return ((DBShare) params[0]).query((String) params[1]);
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, pattern, cb, what);
	}
	/**
	 * 查询股票
	 * 
	 * @param model
	 * @param pattern
	 *            关键词
	 * @param cb
	 *            回调 ，其中的res: null:失败 List&lt;Share&gt;成功
	 * @param what
	 *            回调标记
	 */
	public void queryShare(DBShare model, String pattern, CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				return ((DBShare) params[0]).query((String) params[1]);
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, pattern, cb, what);
	}
	

	/**
	 * 查询热门股票
	 * 
	 * @param model
	 * @param cb
	 *            回调 ，其中的res: null:失败 List&lt;Share&gt;成功
	 * @param what
	 *            回调标记
	 */
	public void queryHotShare(HotShare model, CallBack cb, int what) {
		new AsyncTask<Object, Void, Object>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[1];
				what = (Integer) params[2];

				return ((HotShare) params[0]).doGet();
			}

			protected void onPostExecute(Object result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, cb, what);
	}

	/**
	 * 取消收藏列表
	 * 
	 * @param model
	 * @param data
	 * @param cb
	 *            回调。其中，res:false:为失败 ,true:为成功
	 * @param what
	 *            回调标记
	 */
	public void cancelPreserveList(DBNewsListBase model, List<News> data,
			CallBack cb, int what) {
		new AsyncTask<Object, Void, Boolean>() {
			private CallBack cb = null;
			private int what = -1;

			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				cb = (CallBack) params[2];
				what = (Integer) params[3];

				List<?> tempData = (List<?>) params[1];
				List<News> data = new ArrayList<News>();
				for (Object obj : tempData) {
					data.add((News) obj);
				}

				return ((DBNewsListBase) params[0]).cancelPreserveList(data);
			}

			protected void onPostExecute(Boolean result) {
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, data, cb, what);
	}

	/** 回调 */
	public interface CallBack {
		/**
		 * 数据返回
		 * 
		 * @param what
		 *            返回类型
		 * @param res
		 *            返回数据。null:获取失败。<br/>
		 * <br/>
		 *            <strong> 使用前用户需知res确定类型，用于转换自已需要的数据。具体类型在model相关方法中。<br/>
		 *            如：Investmentletters.android.dao.base.
		 *            NewsBase中的getListDefault
		 *            (),getMore(),getFresh(),getUpdate() </strong>
		 */
		public void onResult(int what, Object res);
	}

}
