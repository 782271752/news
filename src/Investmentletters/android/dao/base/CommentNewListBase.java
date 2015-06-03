package Investmentletters.android.dao.base;

import java.util.List;

import Investmentletters.android.entity.Comment;

/**
 * �����б�DAO�������
 * 
 * @param <T>
 */
public abstract class CommentNewListBase<T> {

	/**
	 * ��������id��ȡ�����б�
	 * 
	 * @param newId
	 *            ��ǰ����id
	 * @return
	 */
	public abstract List<T> getFresh(int newId);

	/**
	 * ��������id�����۵���Сid��ȡ���������б�
	 * 
	 * @param newId
	 *            ��ǰ����id
	 * @param minId
	 *            ��ǰ���������б���СId
	 * @return
	 */
	public abstract List<T> getMore(int newId, int minId);
}
