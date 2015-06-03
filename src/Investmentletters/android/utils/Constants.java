package Investmentletters.android.utils;

import android.os.Environment;

/**
 * 常量类
 * 
 * @author liang
 */
public class Constants {

	/** URL:数据入口 */
	public static final String NEW_URL = "http://new.etz927.com/json/";
	/** 汇瀛入口 */
	public static final String HY_URL = "http://hy.etz927.com/json/";

	/** URL:时事聚焦新闻列表、更多新闻url格式 */
	public static final String URL_GET_NEWS = NEW_URL
			+ "NewJsonIOS.aspx?Type=%d&PageId=%d";
	/** URL:刷新更新新闻列表 */
	public static final String URL_FRESH_NEWS = NEW_URL
			+ "JudgeMarkIOS.aspx?TypeId=%d&MarkId=%d";
	/** URL:获取修改的新闻列表 */
	public static final String URL_GET_MODIFY_NEWS = NEW_URL
			+ "UpdateNew.aspx?UpdateTime=%s&UpdateType=%d";
	/** URL:获取今日快讯不分栏目的前20条新闻列表 */
	public static final String URL_GET_TODAY_NO_BRAND_DEFAULT = NEW_URL
			+ "IOSBroadcasting.aspx";
	/** URL:获取今日快讯不分栏目的更多新闻列表 */
	public static final String URL_GET_TODAY_NO_BRAND_MORE = URL_GET_TODAY_NO_BRAND_DEFAULT
			+ "?PageId=%d";
	/** URL:获取今日快讯不分栏目的刷新更新的新闻列表 */
	public static final String URL_GET_TODAY_NO_BRAND_FRESH = URL_GET_TODAY_NO_BRAND_DEFAULT
			+ "?NewPageId=%d";
	
	/** URL:获取今日快讯不分栏目的前20条新闻列表 */
	public static final String URL_GET_TODAY_BY_BRAND = NEW_URL + "IOSBroadcastingIOS.aspx";
	
	/** URL:今日快讯有栏目新闻列表、更多新闻url格式 */
	public static final String URL_GET_TODAY_WITH_BRAND_NEWS = URL_GET_TODAY_BY_BRAND
			+ "?MarkType=%d&PageId=%d";
	/** URL:今日快讯有栏目刷新更新的url格式 */
	public static final String URL_GET_TODAY_WITH_BRAND_FRESH_NEWS = URL_GET_TODAY_BY_BRAND
			+ "?MarkType=%d&NewPageId=%d";
	/** URL:获取今日快讯栏目 */
	public static final String URL_GET_TODAY_BRANDS = NEW_URL
			+ "MarkClassArray.aspx";
	
	/** URL:获取电台DJ列表 */
	public static final String URL_GET_RADIO_DJS = NEW_URL + "host/host.ashx";
	/** URL:获取电台节目列表 */
	public static final String URL_GET_RADIO_LIST = NEW_URL + "host/act.ashx";
	/** URL:用户管理 */
	public static final String URL_ACCOUNT = NEW_URL + "UpdateUser.aspx";
	/** URL:用户注册 */
	public static final String URL_REGISTER = NEW_URL + "addusers.aspx";
	/** URL:版本更新 */
	public static final String URL_UPDATE = NEW_URL + "up.htm";
	/** URL:用户登陆 */
	public static final String URL_LOGIN = HY_URL + "login.aspx";
	/** URL:互动诊股 */
	public static final String URL_DIAGNOSIS_SHARE = HY_URL + "Feedback.aspx";
	/** URL:用户反馈 */
	public static final String URL_FEED_BACK = NEW_URL + "Feedback.aspx";
	/** URL:获取美图在线详细 */
	public static final String URL_GET_IMAGE_ON_LINE_DETAIL = HY_URL
			+ "j6.aspx?NewID=%d";
	/** URL: 修改用户信息 */
	public static final String URL_MODIFY_USER = NEW_URL + "UpdateUser.aspx";
	/** URL:获取消息推送详细信息 */
	public static final String URL_GET_PUSH_DETAIL = NEW_URL
			+ "GetIdNewIOS.aspx?NewId=%d&type=%d";
	/** URL:热门诊股列表 */
	public static final String URL_GET_HOT_SHARE = NEW_URL + "test.aspx";
	/** URL:互动诊股详细信息 */
	public static final String URL_GET_SHARE_DETAIL = NEW_URL
			+ "test.aspx?stock=%s";
	/**URL:热门新闻搜索*/
	public static final String URL_HOTNEWS_SRARCH = 
			"http://59.38.125.114:8032/n_crawl.aspx?fuzzy=0";

	/** 广播地址 */
	public static final String MMS_URL = "mms://hygzo.vicp.net:2013";

	/** SharedPreferences文件名 */
	public static final String SHARED_PREFERENCES_FILE_NAME = "HYKZ_TZKX";
	/** SharedPreferences键：用户名 */
	public static final String SHARED_KEY_USER_NAME = "SP_KEY_USER_NAME";
	/** SharedPreferences键：密码 */
	public static final String SHARED_KEY_PASSWD = "SP_KEY_PASSWD";

	/** 微信AppId */
	public static final String WEIXIN_APP_ID = "wxdfd5bd39d258c090";

	/** 新浪：Appid */
	public static final String SINA_APP_KEY = "2982850267";
	/** 新浪：App回调 */
	// public static final String SINA_REDIRECT_URL = "http://www.927953.com";
	public static final String SINA_REDIRECT_URL = "http://www.baidu.com";

	/** 腾讯微博APPid */
	public static final String TENCENT_WEIBO_APPID = "801338237";
	/** 腾讯微博密钥 */
	public static final String TENCENT_WEIBO_SECRET = "f453706056e7c29b77f24c5305f17b07";

	/** 百度推送app id */
	public static final String BAIDU_PUSH_APP_ID = "WC9ve569q0b4yVghnKPsvSxe";

	/** 临时文件夹绝对路径 */
	public static final String TEMP_DIR_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/com.hykj.investmentletters";

	/** 提交评论 */
	public static final String URL_SUBMIT_COMMENT = "http://new.etz927.com/json/CommentData.aspx";

	/** 获取评论列表URL */
	public static final String URL_COMMENT = NEW_URL
			+ "CommentDatalist.aspx?NewId=%d&NextPageIndex=%d";
	
	/**报告错误*/
	public static final String URL_REPORT_ERROR = "http://new.etz927.com/json/PhoneErr.ashx";
}
