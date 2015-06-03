package Investmentletters.android.utils;

import android.os.Environment;

/**
 * ������
 * 
 * @author liang
 */
public class Constants {

	/** URL:������� */
	public static final String NEW_URL = "http://new.etz927.com/json/";
	/** ������ */
	public static final String HY_URL = "http://hy.etz927.com/json/";

	/** URL:ʱ�¾۽������б���������url��ʽ */
	public static final String URL_GET_NEWS = NEW_URL
			+ "NewJsonIOS.aspx?Type=%d&PageId=%d";
	/** URL:ˢ�¸��������б� */
	public static final String URL_FRESH_NEWS = NEW_URL
			+ "JudgeMarkIOS.aspx?TypeId=%d&MarkId=%d";
	/** URL:��ȡ�޸ĵ������б� */
	public static final String URL_GET_MODIFY_NEWS = NEW_URL
			+ "UpdateNew.aspx?UpdateTime=%s&UpdateType=%d";
	/** URL:��ȡ���տ�Ѷ������Ŀ��ǰ20�������б� */
	public static final String URL_GET_TODAY_NO_BRAND_DEFAULT = NEW_URL
			+ "IOSBroadcasting.aspx";
	/** URL:��ȡ���տ�Ѷ������Ŀ�ĸ��������б� */
	public static final String URL_GET_TODAY_NO_BRAND_MORE = URL_GET_TODAY_NO_BRAND_DEFAULT
			+ "?PageId=%d";
	/** URL:��ȡ���տ�Ѷ������Ŀ��ˢ�¸��µ������б� */
	public static final String URL_GET_TODAY_NO_BRAND_FRESH = URL_GET_TODAY_NO_BRAND_DEFAULT
			+ "?NewPageId=%d";
	
	/** URL:��ȡ���տ�Ѷ������Ŀ��ǰ20�������б� */
	public static final String URL_GET_TODAY_BY_BRAND = NEW_URL + "IOSBroadcastingIOS.aspx";
	
	/** URL:���տ�Ѷ����Ŀ�����б���������url��ʽ */
	public static final String URL_GET_TODAY_WITH_BRAND_NEWS = URL_GET_TODAY_BY_BRAND
			+ "?MarkType=%d&PageId=%d";
	/** URL:���տ�Ѷ����Ŀˢ�¸��µ�url��ʽ */
	public static final String URL_GET_TODAY_WITH_BRAND_FRESH_NEWS = URL_GET_TODAY_BY_BRAND
			+ "?MarkType=%d&NewPageId=%d";
	/** URL:��ȡ���տ�Ѷ��Ŀ */
	public static final String URL_GET_TODAY_BRANDS = NEW_URL
			+ "MarkClassArray.aspx";
	
	/** URL:��ȡ��̨DJ�б� */
	public static final String URL_GET_RADIO_DJS = NEW_URL + "host/host.ashx";
	/** URL:��ȡ��̨��Ŀ�б� */
	public static final String URL_GET_RADIO_LIST = NEW_URL + "host/act.ashx";
	/** URL:�û����� */
	public static final String URL_ACCOUNT = NEW_URL + "UpdateUser.aspx";
	/** URL:�û�ע�� */
	public static final String URL_REGISTER = NEW_URL + "addusers.aspx";
	/** URL:�汾���� */
	public static final String URL_UPDATE = NEW_URL + "up.htm";
	/** URL:�û���½ */
	public static final String URL_LOGIN = HY_URL + "login.aspx";
	/** URL:������� */
	public static final String URL_DIAGNOSIS_SHARE = HY_URL + "Feedback.aspx";
	/** URL:�û����� */
	public static final String URL_FEED_BACK = NEW_URL + "Feedback.aspx";
	/** URL:��ȡ��ͼ������ϸ */
	public static final String URL_GET_IMAGE_ON_LINE_DETAIL = HY_URL
			+ "j6.aspx?NewID=%d";
	/** URL: �޸��û���Ϣ */
	public static final String URL_MODIFY_USER = NEW_URL + "UpdateUser.aspx";
	/** URL:��ȡ��Ϣ������ϸ��Ϣ */
	public static final String URL_GET_PUSH_DETAIL = NEW_URL
			+ "GetIdNewIOS.aspx?NewId=%d&type=%d";
	/** URL:��������б� */
	public static final String URL_GET_HOT_SHARE = NEW_URL + "test.aspx";
	/** URL:���������ϸ��Ϣ */
	public static final String URL_GET_SHARE_DETAIL = NEW_URL
			+ "test.aspx?stock=%s";
	/**URL:������������*/
	public static final String URL_HOTNEWS_SRARCH = 
			"http://59.38.125.114:8032/n_crawl.aspx?fuzzy=0";

	/** �㲥��ַ */
	public static final String MMS_URL = "mms://hygzo.vicp.net:2013";

	/** SharedPreferences�ļ��� */
	public static final String SHARED_PREFERENCES_FILE_NAME = "HYKZ_TZKX";
	/** SharedPreferences�����û��� */
	public static final String SHARED_KEY_USER_NAME = "SP_KEY_USER_NAME";
	/** SharedPreferences�������� */
	public static final String SHARED_KEY_PASSWD = "SP_KEY_PASSWD";

	/** ΢��AppId */
	public static final String WEIXIN_APP_ID = "wxdfd5bd39d258c090";

	/** ���ˣ�Appid */
	public static final String SINA_APP_KEY = "2982850267";
	/** ���ˣ�App�ص� */
	// public static final String SINA_REDIRECT_URL = "http://www.927953.com";
	public static final String SINA_REDIRECT_URL = "http://www.baidu.com";

	/** ��Ѷ΢��APPid */
	public static final String TENCENT_WEIBO_APPID = "801338237";
	/** ��Ѷ΢����Կ */
	public static final String TENCENT_WEIBO_SECRET = "f453706056e7c29b77f24c5305f17b07";

	/** �ٶ�����app id */
	public static final String BAIDU_PUSH_APP_ID = "WC9ve569q0b4yVghnKPsvSxe";

	/** ��ʱ�ļ��о���·�� */
	public static final String TEMP_DIR_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/com.hykj.investmentletters";

	/** �ύ���� */
	public static final String URL_SUBMIT_COMMENT = "http://new.etz927.com/json/CommentData.aspx";

	/** ��ȡ�����б�URL */
	public static final String URL_COMMENT = NEW_URL
			+ "CommentDatalist.aspx?NewId=%d&NextPageIndex=%d";
	
	/**�������*/
	public static final String URL_REPORT_ERROR = "http://new.etz927.com/json/PhoneErr.ashx";
}
