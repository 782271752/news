package Investmentletters.android.utils;

/**
 * 腾讯微博api相关接口
 * @author liang
 */
public interface TenXunWeiBoListener {

	/**初始化失败*/
	public static final int ERR_NO_INIT = 0;
	/**授权失败*/
	public static final int ERR_NO_PERMISSION = 1;
	/**分享失败*/
	public static final int ERR_NO_SHARE = 2;
	/**分享内容过长或都为空*/
	public static final int ERR_NO_CONTENT_LEN = 3;
	/**发表太快，被频率限制，请控制发表频率*/
	public static final int ERR_NO_TOO_RAST = 4;
	/**重复发表*/
	public static final int ERR_NO_REPEAT = 5;
	
	
	/**
	 * 出错
	 * @param code ,详情见,TenXunWeiBoListener.ERR_NO_XXX
	 * @param msg
	 */
	public void onError(int code,String msg);
	
	/**分享成功*/
	public void onComplete();
	
}
