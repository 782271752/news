package Investmentletters.android.utils;

/**
 * ��Ѷ΢��api��ؽӿ�
 * @author liang
 */
public interface TenXunWeiBoListener {

	/**��ʼ��ʧ��*/
	public static final int ERR_NO_INIT = 0;
	/**��Ȩʧ��*/
	public static final int ERR_NO_PERMISSION = 1;
	/**����ʧ��*/
	public static final int ERR_NO_SHARE = 2;
	/**�������ݹ�����Ϊ��*/
	public static final int ERR_NO_CONTENT_LEN = 3;
	/**����̫�죬��Ƶ�����ƣ�����Ʒ���Ƶ��*/
	public static final int ERR_NO_TOO_RAST = 4;
	/**�ظ�����*/
	public static final int ERR_NO_REPEAT = 5;
	
	
	/**
	 * ����
	 * @param code ,�����,TenXunWeiBoListener.ERR_NO_XXX
	 * @param msg
	 */
	public void onError(int code,String msg);
	
	/**����ɹ�*/
	public void onComplete();
	
}
