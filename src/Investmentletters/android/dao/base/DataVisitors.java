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
 * ���ݷ���Э����
 * 
 * @author liang
 */
public class DataVisitors {

	/**
	 * ��ȡ����Ĭ���б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص��ı��
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
	 * ��ȡ���ݿ�����Ĭ���б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص��ı��
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
	 * ���� ��ȡ����ˢ���б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param newId
	 *            ��ǰ���ŵ�Id
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص��ı��
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
	 * ��ȡ���������б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param minId
	 *            �б�����С��id
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ��ȡ���������б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param newId
	 *            ��ǰ���ŵ�Id
	 * @param minId
	 *            �б�����С��id
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ��ȡˢ�������б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param minId
	 *            �б�������id
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ��ȡ���ݿ�ˢ�������б�
	 * 
	 * @param model
	 *            Ҫ���ʵ�model��
	 * @param minId
	 *            �б�����С��id
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ��ȡ�޸ĵ������б�
	 * 
	 * @param model
	 *            model��
	 * @param lastTime
	 *            ����޸�ʱ��
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ִ��Post
	 * 
	 * @param model
	 *            model��
	 * @param params
	 *            post ����
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * ִ��get
	 * 
	 * @param model
	 *            model��
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
	 * �������ݵ����ݿ�,��ֻ�ǲ������ݵ���ͼ������ϸ
	 * 
	 * @param model
	 *            ��ֻ����ͼ����daoʵ��
	 * @param data
	 *            ����
	 * @param id
	 *            ��ͼ����id
	 * @param cb
	 *            �ص� �����е�res: false:ʧ�� true:�ɹ�
	 * @param what
	 *            �ص����
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
	 * ���������б����ݿ�
	 * 
	 * @param model
	 * @param data
	 * @param cb
	 *            �ص� �����е�res: false:ʧ�� true:�ɹ�
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
	 * ɾ����ͼ������ϸ����
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 *            cb �ص� �����е�res: false:ʧ�� true:�ɹ�
	 * @param what
	 *            �ص����
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
	 * ������ͼ������ϸ��Ϣ
	 * 
	 * @param model
	 * @param data
	 * @param id
	 * @param cb
	 *            �ص� �����е�res: false:ʧ�� true:�ɹ�
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
	 * �������ݿ������б�
	 * 
	 * @param model
	 * @param data
	 * @param id
	 * @param cb
	 *            �ص� �����е�res: false:ʧ�� true:�ɹ�
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
	 * �齲��ͼ������ϸ����
	 * 
	 * @param model
	 * @param id
	 * @param cb
	 *            �ص� �����е�res: null:ʧ�� List&lt;Image&gt;�ɹ�
	 * @param what
	 *            �ص����
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
	 * �ղ�����
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
	 * ȡ���ղ�����
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
	 * ���������б�
	 * 
	 * @param model
	 *            http�����б�dao
	 * @param dbModel
	 *            ���������б�dao
	 * @param dbLastTime
	 *            ���ʱ��dao
	 * @param type
	 *            �������ͣ�����μ�DBHelper.TYPE_xxx
	 * @param cb
	 *            �ص�
	 * @param what
	 *            �ص����
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
				int type = (Integer) params[3];// ����

				String lastTime = dbLastTime.getLastTime(type);
				List<?> data = model.getUpdate(lastTime);
				System.out.println("���£�" + lastTime + "  " + data);
				if (data == null) {
					return false;
				}

				List<News> newsData = new ArrayList<News>();
				for (Object item : data) {
					newsData.add((News) item);
				}
				System.out.println("���£�" + newsData.size());
				return dbModel.update(newsData);
			}

			protected void onPostExecute(Boolean result) {
				System.out.println("���½����" + result);
				if (cb != null) {
					cb.onResult(what, result);
				}
			};

		}.execute(model, dbModel, dbLastTime, type, cb, what);
	}
	/**
	 * ��ѯ����
	 * 
	 * @param model
	 * @param pattern
	 *            �ؼ���
	 * @param cb
	 *            �ص� �����е�res: null:ʧ�� List&lt;Share&gt;�ɹ�
	 * @param what
	 *            �ص����
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
	 * ��ѯ��Ʊ
	 * 
	 * @param model
	 * @param pattern
	 *            �ؼ���
	 * @param cb
	 *            �ص� �����е�res: null:ʧ�� List&lt;Share&gt;�ɹ�
	 * @param what
	 *            �ص����
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
	 * ��ѯ���Ź�Ʊ
	 * 
	 * @param model
	 * @param cb
	 *            �ص� �����е�res: null:ʧ�� List&lt;Share&gt;�ɹ�
	 * @param what
	 *            �ص����
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
	 * ȡ���ղ��б�
	 * 
	 * @param model
	 * @param data
	 * @param cb
	 *            �ص������У�res:false:Ϊʧ�� ,true:Ϊ�ɹ�
	 * @param what
	 *            �ص����
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

	/** �ص� */
	public interface CallBack {
		/**
		 * ���ݷ���
		 * 
		 * @param what
		 *            ��������
		 * @param res
		 *            �������ݡ�null:��ȡʧ�ܡ�<br/>
		 * <br/>
		 *            <strong> ʹ��ǰ�û���֪resȷ�����ͣ�����ת��������Ҫ�����ݡ�����������model��ط����С�<br/>
		 *            �磺Investmentletters.android.dao.base.
		 *            NewsBase�е�getListDefault
		 *            (),getMore(),getFresh(),getUpdate() </strong>
		 */
		public void onResult(int what, Object res);
	}

}
