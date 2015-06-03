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
 * ��Ѷ΢��api
 * 
 * @author liang
 */
public class TenXunWeiBo {

	/** ��֤�ͻ��� */
	private OAuthClient oauthClient = null;
	/** OAuth���model */
	private OAuth oauth = null;
	/** ��Ȩ�Ի��� */
	private Dialog verifyDialog = null;
	/** ��Ȩwebview */
	private WebView webView = null;

	private Handler handler = null;
	/** What:��ʾ��Ȩ��ҳ */
	private final int WHAT_SHOW_PERMISSION_DIALOG = 0;
	/**WHAT:����*/
	private final int WHAT_ERROR = 1;
	/**WHAT:��ɷ���*/
	private final int WHAT_COMPLETE = 2;

	/** ��Ѷ΢��api��ؽӿ�,�ڲ�ʹ�� */
	private TenXunWeiBoListener myListener = null;
	/** ��Ѷ΢��api��ؽӿ�,������ʹ�� */
	private TenXunWeiBoListener listener = null;

	/** �Ƿ����ڷ��� */
	private boolean isSending = false;
	/** �������� */
	private String shareContent = null;
	/**�Ƿ�����Ȩ*/
	private boolean isPermission = false;
	

	/**
	 * ��Ѷ΢��api����
	 * 
	 * @param context
	 * @param appKey
	 *            appKey
	 * @param appSecret
	 *            app��Կ
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
				if (newProgress == 100) {// �������
					String url = view.getUrl();
					System.out.println("������ɺ��ַ:" + url);
					if (url.contains("&checkType=verifycode")) {// ��Ȩ�ɹ�
						Uri uri = Uri.parse(url);
						String value = uri.getQueryParameter("v"); // ��Ȩ��
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
								System.out.println("��ʼʹ����Ȩ���Request Token��ȡAccess Token");
								try {
									oauth = oauthClient.accessToken(oauth);// ʹ����Ȩ���RequestToken��ȡAccess Token
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

					} else if (url.contains("&checkType=error")) {// �ܾ���Ȩ
						System.out.println("�ܾ���Ȩ");
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
						verifyDialog.show();// ��ʾ��Ȩ��ҳ
						break;
						
					case WHAT_ERROR: //����
						if(listener != null){
							lm = (ListenModel)msg.obj;
							listener.onError(lm.errno, lm.msg);
						}
						break;
						
					case WHAT_COMPLETE: //WHAT:��ɷ���
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
	 * ��������
	 * @param msg
	 */
	public void add(String msg) {
		if (isSending) {
			myListener.onError(TenXunWeiBoListener.ERR_NO_TOO_RAST,"����̫�죬��Ƶ�����ƣ�����Ʒ���Ƶ��");
			return;
		}
		
		shareContent = msg;
		isSending = true;
		
		if(isPermission){//�ڹ�Ȩ����ȥ����
			share();
		}else{ //δ�ڹ�Ȩ����ȥ��Ȩ
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						oauth = oauthClient.requestToken(oauth); // ��ȡδ��Ȩ��Request
																	// Token
						webView.loadUrl("http://open.t.qq.com/cgi-bin/authorize?oauth_token="
								+ oauth.getOauth_token());
						handler.sendEmptyMessage(WHAT_SHOW_PERMISSION_DIALOG);// ��ʾ��Ȩ��ҳ
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("����");
						myListener.onError(TenXunWeiBoListener.ERR_NO_INIT,"init error");// ��ʼ��ʧ��
					}
				}
			}).start();
		}
		
	}
	
	/**����*/
	private void share(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				T_API api = new T_API();
				try {
					System.out.println("��ʼ����...");
					String result = api.add(oauth, "json", shareContent, "211.66.184.33");
					System.out.println("���ͽ����" + result);
					
					JSONObject json = new JSONObject(result);
					int ret = json.getInt("ret");
					int errcode = json.getInt("errcode");
					
					System.out.println("���ͽ��״̬��ret:"+ret+"   errcode:"+errcode);
					
					//����ֻ��������Ҫ�����������ĵ�ֻ��Ϊ����ʧ�ܴ���
					if(ret == 1){
						if(errcode == 2){
							myListener.onError(TenXunWeiBoListener.ERR_NO_CONTENT_LEN,"΢�����ݳ����������ƻ�Ϊ�գ���������Ҫ��������");
						}else{
							myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
						}
					}else if(ret == 4){
						if(errcode == 10){ //����̫�죬��Ƶ�����ƣ�����Ʒ���Ƶ��
							myListener.onError(TenXunWeiBoListener.ERR_NO_TOO_RAST,"����̫�죬��Ƶ�����ƣ�����Ʒ���Ƶ��");
						}else if(errcode == 13){// �ظ������벻Ҫ���������ظ�����
							myListener.onError(TenXunWeiBoListener.ERR_NO_REPEAT,"�ظ������벻Ҫ���������ظ�����");
						}else{
							myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
						}
					}else if(ret == 5){
						myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
					}else{ //����ɹ�
						myListener.onComplete();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("����ʧ��...");
					myListener.onError(TenXunWeiBoListener.ERR_NO_SHARE,"share failure!");
				}
			}
		}).start();
	}

	/** ��Ѷ΢��api��ؽӿ�,������ʹ�� */
	public void setListener(TenXunWeiBoListener listener) {
		this.listener = listener;
	}
	
	/**Listener���model*/
	private class ListenModel{
		/**�������*/
		private int errno = 0;
		/**������Ϣ*/
		private String msg = null;
	}


}
