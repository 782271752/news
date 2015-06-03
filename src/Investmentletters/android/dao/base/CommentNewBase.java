package Investmentletters.android.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Investmentletters.android.entity.Comment;
import Investmentletters.android.utils.Http;

/**
 * ���ۻ���
 */
public abstract class CommentNewBase extends CommentNewListBase<Comment> {

	/** ������ */
	private Http http = null;

	public CommentNewBase() {
		http = new Http();
	}

	/**
	 * ����ˢ�� newId ����Id(��ʶ��
	 * 
	 */
	@Override
	public List<Comment> getFresh(int newId) {
		return jsonList(getHttp().doGet(getUrl(getFreshUrlFormat(), newId, 0)));
	}

	/**
	 * ������ȡ����
	 */
	@Override
	public List<Comment> getMore(int newId, int minId) {
		return jsonList(getHttp().doGet(
				getUrl(getMoreUrlFormat(), newId, minId)));
	}

	public List<Comment> jsonList(String jsonStr) {

		System.out.println("�����" + jsonStr);

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
				comment.setUserName(jo.getString("�ظ���").trim());
				comment.setContent(jo.getString("����").trim());
				comment.setId(jo.getInt("�ظ���ʶ"));
				comment.setTime(jo.getString("ʱ��").trim());

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
	 * ��ȡ�ӿڵ�ַ
	 * 
	 * @param format
	 *            ��ʽ
	 * @param args
	 *            ����
	 * @return
	 */
	public String getUrl(String format, Object... args) {
		return String.format(format, args);
	}

	/** ��ȡ�ӿڸ�ʽ�������б� */
	public abstract String getMoreUrlFormat();

	/** ��ȡ�ӿڸ�ʽ��ˢ���б� */
	public abstract String getFreshUrlFormat();

	/** ��ȡ Http ������ʵ�� */
	public Http getHttp() {
		return http;
	}
}
