package Investmentletters.android.dao.base;

import java.util.List;

import Investmentletters.android.entity.Comment;

/**
 * 评论列表DAO抽像基类
 * 
 * @param <T>
 */
public abstract class CommentNewListBase<T> {

	/**
	 * 根据新闻id获取评论列表
	 * 
	 * @param newId
	 *            当前新闻id
	 * @return
	 */
	public abstract List<T> getFresh(int newId);

	/**
	 * 根据新闻id，评论的最小id获取更多评论列表
	 * 
	 * @param newId
	 *            当前新闻id
	 * @param minId
	 *            当前新闻评论列表最小Id
	 * @return
	 */
	public abstract List<T> getMore(int newId, int minId);
}
