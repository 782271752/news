package Investmentletters.android.dao.base;

import java.util.List;

/**
 * �����б�DAO�������
 * 
 * @author liang
 * 
 * @param <T>
 */
public abstract class NewsListBase<T> {

	/** ���Žӿ�����:Ͷ�ʿ�Ѷ */
	public static final int TYPE_INVESTMENTLETTERS = 0x01;
	/** ���Žӿ�����:û����Ŀ�Ľ��տ�Ѷ */
	public static final int TYPE_TODAY_NO_BRAND = 0x02;
	/** ���Žӿ����ͣ���˾�״� */
	public static final int TYPE_FADAR = 0x03;
	/** ���Žӿ����ͣ����ݽ��� */
	public static final int TYPE_DECODER = 0x04;
	/** ���Žӿ�����:��ͼ���� */
	public static final int TYPE_IMAGES_ON_LINE = 0x05;
	/** ���Žӿ����ͣ����̽�� */
	public static final int TYPE_TRADER_EXPLAIN = 0x06;
	/** ���Žӿ����ͣ�ʱ�¾۽� */
	public static final int TYPE_FOCUS = 0x07;


	/**
	 * ��ȡ�a���б�
	 * 
	 * @return null:ʧ��
	 */
	public abstract List<T> getListDefault();

	/**
	 * ��ȡ����
	 * 
	 * @param minId
	 *            ��С��id
	 * @return null:ʧ��
	 */
	public abstract List<T> getMore(int minId);

	/**
	 * ˢ��
	 * 
	 * @param maxId
	 *            ����id
	 * @return null:ʧ��
	 */
	public abstract List<T> getFresh(int maxId);

	/**
	 * ��ȡ���ݸ���
	 * 
	 * @param lastTime
	 *            �ϴθ���ʱ��
	 * @return null:ʧ��
	 */
	public abstract List<T> getUpdate(String lastTime);

	/** ��ȡ���Žӿ����� */
	public abstract int getType();

	/**
	 * ������������
	 * 
	 * @param type
	 *            ��ϸ�μ���NewsListBase.TYPE_xxx ����տ�Ѷ��Ŀ����������Ϊ��չ���տ�Ѷ����Ŀ�������Ŷ���
	 */
	public void setType(int type) {

	}
}
