package Investmentletters.android.utils;

import org.json.JSONObject;

import Investmentletters.android.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;

/**
 * 腾讯微博api
 * 
 * @author liang
 */
public class TenXunWeiBo {

	/** 认证客户端 */
	private OAuthClient oauthClient = null;
	/** OAuth相关model */
	private OAuth oauth = null;
	/** 授权对话框 */
	private Dialog verifyDialog = null;
	/** 授权webview */
	private WebView webView = null;

	private Handler handler = null;
	/** What:显示授权网页 */
	private final int WHAT_SHOW_PERMISSION_DIALOG = 0;
	/**WHAT:错误*/
	private final int WHAT_ERROR = 1;
	/**WHAT:完成分享*/
	private final int WHAT_COMPLETE = 2;

	/** 腾讯微博api相关接口,内部使用 */
	private TenXunWeiBoListener myListener = null;
	/** 腾讯微博api相关接口,调用者使用 */
	private TenXunWeiBoListener listener = null;

	/** 是否正在发送 */
	private boolean isSending = false;
	/** 分享内容 */
	private String shareContent = null;
	/**是否已授权*/
	private boolean isPermission = false;
	

	/**
	 * 腾讯微博api构造
	 * 
	 * @param context
	 * @param appKey
	 *            appKey
	 * @param appSecret
	 *            app密钥
	 */
	@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
	public TenXunWeiBo(Context context, String appKey, String appSecret) {

		oauthClient = new OAuthClient();
		
		oauth = new OAuth();
		oauth.setOauth_consumer_key(appKey);
		oauth.setOauth_consumer_secret(appSecret);

		myListener = new TenXunWeiBoListener() {

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				isSending = false;
				Message message = handler.obtainMessage();
				ListenModel lm = new ListenModel();
				lm.errno = code;
				lm.msg = msg;
				message.obj = lm;
				message.what = WHAT_ERROR;
				handler.sendMessage(message);
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				isSending = false;
				handler.sendEmptyMessage(WHAT_COMPLETE);
			}

		};

		webView = new WebView(context);

		verifyDialog = new Dialog(context, R.style.verify_dialog);

		verifyDialog.setContentView(webView);
		
		verifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						isSending = false;
					}
				});

		WebSettings setting = webView.getSettings();
		setting.setSupportZoom(true);
		setting.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {// 加载完成
					String url = view.getUrl();
					System.out.println("加载完成后地址:" + url);
					if (url.contains("&checkType=verifycode")) {// 授权成功
						Uri uri = Uri.parse(url);
						String value = uri.getQueryParameter("v"); // 授权码
						String orValue = uri.getQueryParameter("vcode");

						int verifycode = 0;
						int orVerifycode = 0;
						if (value != null) {
							verifycode = Integer.parseInt(value);
						}

						if (orValue != null) {
							orVerifycode = Integer.parseInt(orValue);
						}

						if (verifycode > 0) {
							oauth.setOauth_verifier(value);
						}

						if (orVerifycode > 0) {
							oauth.setOauth_verifier(orValue);
						}

						new Thread(new Runnable() {
							public void run() {
								System.out.println("开始使用授权后的Request Token换取Access Token");
								try {
									oauth = oauthClient.accessToken(oauth);// 使用授权后的RequestToken换取Access Token
									isPermission = true;
									share();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									myListener.onError(TenXunWeiBoListener.ERR_NO_PERMISSION,"permission deny!");
								}
								verifyDialog.dismiss();
							}
						}).start();

					} else if (url.contains("&checkType=error")) {// 拒绝授权
						System.out.println("拒绝授权");
						myListener.onError(TenXunWeiBoListener.ERR_NO_PERMISSION,"permission deny!");
						verifyDialog.dismiss();
					}
				}
				super.onProgressChanged(view, newProgress);
			}
		});

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				ListenModel lm = null;
				
				switch (msg.what) {
					case WHAT_SHOW_PERMISSION_DIALOG:
						verifyDialog.show();// 显示授权网页
						break;
						
					case WHAT_ERROR: //错误
						if(listener != null){
							lm = (ListenModel)msg.obj;
							listener.onError(lm.errno, lm.msg);
						}
						break;
						
					case WHAT_COMPLETE: //WHAT:完成分享
						if(listener != null){
							listener.onComplete();
						}
						break;
	
					default:
						break;
				}
			}
		};

	}

	/**
	 * 发布博客
	 * @param msg
	 */
	public void add(String msg) {
		if (isSending) {
			myListener.onError(TenXunWeiBoListener.ERR_NO_TOO_RAST,"发表太快，被频率限制，请控制发表频率");
			return;
		}
		
		shareContent = msg;
		isSending = true;
		
		if(isPermission){//授过权，现去分享
			share();
		}else{ //未授过权，现去授权
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						oauth = oauthClient.requestToken(oauth); // 获取未授权的Request
																	// Token
						webView.loadUrl("http://open.t.qq.com/cgi-bin/authorize?oauth_token="
								+ oauth.getOauth_token());
						handler.sendEmptyMessage(WHAT_SHOW_PERMISSION_DIALOG);// 显示授权网页
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("出错");
						myListener.onError(TenXunWeiBoListener.ERR_NO_INIT,"init error");// 初始化失败
					}
				}
			}).start();
		}
		
	}
	
	/**分享*/
	private void share(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				T_API api = new T_API();
				try {
					System.out.println("开始发送...");
					String result = api.add(oauth, "json", shareContent, "211.66.184.33");
					System.out.println("发送结果：" + result);
					
					JSONObject json = new JSONObject(result);
					int ret = json.getInt("ret");
					int errcode = json.getInt("errcode");
					
					System.out.println("发送结果状态：ret:"+ret+"   errcode:"+errcode);
					
					//这里只作部份需要错误处理，其他的的只作为分享失败处理
					if(ret == 1){
						if(errcode == 2){
							myListener.onError(TenXunWeiBoListener.ERR_NO_CONTENT_LEN,"微博内容超出长度限制或为空，建议缩减要发表内容");
						}else{
							myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
						}
					}else if(ret == 4){
						if(errcode == 10){ //发表太快，被频率限制，请控制发表频率
							myListener.onError(TenXunWeiBoListener.ERR_NO_TOO_RAST,"发表太快，被频率限制，请控制发表频率");
						}else if(errcode == 13){// 重复发表，请不要连续发表重复内容
							myListener.onError(TenXunWeiBoListener.ERR_NO_REPEAT,"重复发表，请不要连续发表重复内容");
						}else{
							myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
						}
					}else if(ret == 5){
						myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
					}else{ //分享成功
						myListener.onComplete();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("分享失败...");
					myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
				}
			}
		}).start();
	}

	/** 腾讯微博api相关接口,调用者使用 */
	public void setListener(TenXunWeiBoListener listener) {
		this.listener = listener;
	}
	
	/**Listener相关model*/
	private class ListenModel{
		/**错误代码*/
		private int errno = 0;
		/**错误信息*/
		private String msg = null;
	}


}
