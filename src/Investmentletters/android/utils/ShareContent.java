package Investmentletters.android.utils;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * ���ݷ�����
 * 
 * @author liang
 */
public class ShareContent implements WeiboAuthListener{

	private Activity activity = null;
	
	/**���˷�������*/
	private String sinaShareContent = null;
	/**���˷���ص�*/
	private RequestListener sinaListester = null;
	/**��Ѷ΢��������*/
	private TenXunWeiBo tenXunWeiBo = null;
	/**��Ѷ΢���ص�*/
	private TenXunWeiBoListener tenXunWeiBoListener = null;

	public ShareContent(Activity activity) {
		this.activity = activity;
		sinaListester = new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ�io�쳣"+e.toString());
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��쳣"+e.toString());
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			@Override
			public void onComplete(String str) {
				// TODO Auto-generated method stub
				System.out.println("���ˣ��ɹ�"+str);
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "����ɹ�", Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
		
		tenXunWeiBo = new TenXunWeiBo(activity, Constants.TENCENT_WEIBO_APPID, Constants.TENCENT_WEIBO_SECRET);
		tenXunWeiBoListener = new TenXunWeiBoListener() {
			
			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				switch(code){
				
					case TenXunWeiBoListener.ERR_NO_REPEAT:
						Toast.makeText(ShareContent.this.activity, "�ظ�����", Toast.LENGTH_SHORT).show();
						break;
						
					case TenXunWeiBoListener.ERR_NO_TOO_RAST:
						Toast.makeText(ShareContent.this.activity, "�㷢��Ƶ��̫���ˣ���Ϣһ��!", Toast.LENGTH_SHORT).show();
						break;
						
					default:
						Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
						break;
				}
			}
			
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "����ɹ�", Toast.LENGTH_SHORT).show();
			}
		};
		tenXunWeiBo.setListener(tenXunWeiBoListener);//���ûص�
	}

	/**
	 * ����΢��
	 * @param str Ҫ�������������
	 */
	public void shareToWeiXin(String str) {
		str = str.replaceAll("###", "\r\n");
		if (str.length() > 140) {
			str = str.substring(1, 140);
		}
		IWXAPI api = WXAPIFactory.createWXAPI(activity,
				Constants.WEIXIN_APP_ID,true); // IWXAPI ������app��΢��ͨѶ��openapi�ӿ�
		api.registerApp(Constants.WEIXIN_APP_ID);// ��Ӧ�õ�appIdע�ᵽ΢��
		
		if (api.isWXAppInstalled()) {
			WXTextObject wxTextObject = new WXTextObject();
			/* "wxTextObject.text ="��ΪҪ��������� */
			wxTextObject.text = str;

			// ��WXTextObject�����ʼ��һ��WXMediaMessage����
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = wxTextObject;
			msg.description = wxTextObject.text;

			// ����һ��Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis()); // transaction�ֶ�����Ψһ��ʾһ������
			req.message = msg;

//			final int supportFriendsVersion = 0x21020001;//֧�ַ�������Ȧ�İ汾
//			System.out.println("api.getWXAppSupportAPI()==="+api.getWXAppSupportAPI());
//			if(api.getWXAppSupportAPI() < supportFriendsVersion){//���͸�����
//				req.scene = SendMessageToWX.Req.WXSceneSession;
//			}else{ //���͵�����Ȧ
//				req.scene = SendMessageToWX.Req.WXSceneTimeline;
//			}
			
			System.out.println("���ţ�"+api.sendReq(req));;
		} else {//�Բ�������δ��װ΢�ſͻ���
			Toast.makeText(activity, "�Բ�������δ��װ΢�ſͻ���", Toast.LENGTH_LONG).show();
		}
		api.unregisterApp();
	}

	/**
	 * ��������Ȧ
	 * @param str Ҫ�������������
	 */
	public void shareToWeiXinFriends(String str) {
		str = str.replaceAll("###", "\r\n");
		if (str.length() > 140) {
			str = str.substring(1, 140);
		}
		IWXAPI api = WXAPIFactory.createWXAPI(activity,
				Constants.WEIXIN_APP_ID,true); // IWXAPI ������app��΢��ͨѶ��openapi�ӿ�
		api.registerApp(Constants.WEIXIN_APP_ID);// ��Ӧ�õ�appIdע�ᵽ΢��
		
		if (api.isWXAppInstalled()) {
			WXTextObject wxTextObject = new WXTextObject();
			/* "wxTextObject.text ="��ΪҪ��������� */
			wxTextObject.text = str;

			// ��WXTextObject�����ʼ��һ��WXMediaMessage����
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = wxTextObject;
			msg.description = wxTextObject.text;

			// ����һ��Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis()); // transaction�ֶ�����Ψһ��ʾһ������
			req.message = msg;

			final int supportFriendsVersion = 0x21020001;//֧�ַ�������Ȧ�İ汾
			System.out.println("api.getWXAppSupportAPI()==="+api.getWXAppSupportAPI());
			if(api.getWXAppSupportAPI() < supportFriendsVersion){//���͸�����
				Toast.makeText(activity, "�Բ�������΢�Ű汾̫�ͣ�������4.2�����ϰ汾", Toast.LENGTH_LONG).show();
			}else{ //���͵�����Ȧ
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			}
			System.out.println("���ţ�"+api.sendReq(req));;
		} else {//�Բ�������δ��װ΢�ſͻ���
			Toast.makeText(activity, "�Բ�������δ��װ΢�ſͻ���", Toast.LENGTH_LONG).show();
		}
		api.unregisterApp();
	}

	
	/**
	 * ��������΢��,������140����
	 * @param str Ҫ�������������
	 */
	public void shareToSinaWeibo(String str){
		Weibo weibo = Weibo.getInstance(Constants.SINA_APP_KEY, Constants.SINA_REDIRECT_URL);	
		sinaShareContent = str;
		weibo.authorize(activity, this);//��֤
	}
	
	/**
	 * ������Ѷ΢��
	 * @param str Ҫ���������
	 */
	public void shareToTencentWeiBo(String str){
		if(str != null){
			str.trim();
			if(str.length() > 100){
				str = str.substring(0, 100);
			}
			tenXunWeiBo.add(str);
		}
	}
	
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		System.out.println("���ˣ�ȡ����֤");
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onComplete(Bundle data) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ���֤�ɹ�"+data);
		Oauth2AccessToken token = new Oauth2AccessToken(data.getString("access_token"), data.getString("expires_in"));
		
		if(token.isSessionValid()){
			System.out.println("������֤�ɹ�...");
			StatusesAPI weiboApi = new StatusesAPI(token);
			StringBuffer sb = new StringBuffer();
			if(sinaShareContent.length()>100){
				sb.append(sinaShareContent.substring(0, 100));
			}else{
				sb.append(sinaShareContent);
			}
			sb.append("[����Ͷ�ʿ챨�ֻ��ͻ���]");
			System.out.println("���˿�ʼ����...");
			weiboApi.update(sb.toString(), "90.00", "90.00", sinaListester); //����
			
		}else{
			System.out.println("������֤ʧ��...");
		}
	}

	@Override
	public void onError(WeiboDialogError err) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ�����"+err.getMessage());
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		System.out.println("���ˣ��쳣");
		e.printStackTrace();
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "����ʧ��", Toast.LENGTH_SHORT).show();
			}
		});
	}
	

}
