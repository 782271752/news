package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.entity.Comment;
import Investmentletters.android.utils.Http;

/**
 * 评论基类
 */
public abstract class CommentNewBase extends CommentNewListBase<Comment> {

	/** 网络类 */
	private Http http = null;

	public CommentNewBase() {
		http = new Http();
	}

	/**
	 * 下拉刷新 newId 新闻Id(标识）
	 * 
	 */
	@Override
	public List<Comment> getFresh(int newId) {
		return jsonList(getHttp().doGet(getUrl(getFreshUrlFormat(), newId, 0)));
	}

	/**
	 * 上拉获取更多
	 */
	@Override
	public List<Comment> getMore(int newId, int minId) {
		return jsonList(getHttp().doGet(
				getUrl(getMoreUrlFormat(), newId, minId)));
	}

	public List<Comment> jsonList(String jsonStr) {

		System.out.println("结果：" + jsonStr);

		List<Comment> result = null;

		try {
			JSONArray ja = new JSONArray(jsonStr);
			int len = ja.length();
			Comment comment = null;
			JSONObject jo = null;
			result = new ArrayList<Comment>();

			for (int i = 0; i < len; i++) {
				jo = ja.getJSONObject(i);
				comment = new Comment();
				comment.setUserName(jo.getString("回复人").trim());
				comment.setContent(jo.getString("内容").trim());
				comment.setId(jo.getInt("回复标识"));
				comment.setTime(jo.getString("时间").trim());

				result.add(comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (result != null) {
				result.clear();
			}
		}

		return result;
	}

	/**
	 * 获取接口地址
	 * 
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return
	 */
	public String getUrl(String format, Object... args) {
		return String.format(format, args);
	}

	/** 获取接口格式：更多列表 */
	public abstract String getMoreUrlFormat();

	/** 获取接口格式：刷新列表 */
	public abstract String getFreshUrlFormat();

	/** 获取 Http 工具类实例 */
	public Http getHttp() {
		return http;
	}
}
