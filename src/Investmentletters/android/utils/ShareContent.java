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
 * 内容分享类
 * 
 * @author liang
 */
public class ShareContent implements WeiboAuthListener{

	private Activity activity = null;
	
	/**新浪分享内容*/
	private String sinaShareContent = null;
	/**新浪分享回调*/
	private RequestListener sinaListester = null;
	/**腾讯微博分享类*/
	private TenXunWeiBo tenXunWeiBo = null;
	/**腾讯微博回调*/
	private TenXunWeiBoListener tenXunWeiBoListener = null;

	public ShareContent(Activity activity) {
		this.activity = activity;
		sinaListester = new RequestListener() {
			
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：io异常"+e.toString());
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				System.out.println("新浪：异常"+e.toString());
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			@Override
			public void onComplete(String str) {
				// TODO Auto-generated method stub
				System.out.println("新浪：成功"+str);
				ShareContent.this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ShareContent.this.activity, "分享成功", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(ShareContent.this.activity, "重复分享", Toast.LENGTH_SHORT).show();
						break;
						
					case TenXunWeiBoListener.ERR_NO_TOO_RAST:
						Toast.makeText(ShareContent.this.activity, "你发表频率太快了，休息一下!", Toast.LENGTH_SHORT).show();
						break;
						
					default:
						Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
						break;
				}
			}
			
			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "分享成功", Toast.LENGTH_SHORT).show();
			}
		};
		tenXunWeiBo.setListener(tenXunWeiBoListener);//设置回调
	}

	/**
	 * 分享到微信
	 * @param str 要分享的文字内容
	 */
	public void shareToWeiXin(String str) {
		str = str.replaceAll("###", "\r\n");
		if (str.length() > 140) {
			str = str.substring(1, 140);
		}
		IWXAPI api = WXAPIFactory.createWXAPI(activity,
				Constants.WEIXIN_APP_ID,true); // IWXAPI 第三方app和微信通讯的openapi接口
		api.registerApp(Constants.WEIXIN_APP_ID);// 将应用的appId注册到微信
		
		if (api.isWXAppInstalled()) {
			WXTextObject wxTextObject = new WXTextObject();
			/* "wxTextObject.text ="后为要分享的内容 */
			wxTextObject.text = str;

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = wxTextObject;
			msg.description = wxTextObject.text;

			// 构造一个Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标示一个请求
			req.message = msg;

//			final int supportFriendsVersion = 0x21020001;//支持分享到朋友圈的版本
//			System.out.println("api.getWXAppSupportAPI()==="+api.getWXAppSupportAPI());
//			if(api.getWXAppSupportAPI() < supportFriendsVersion){//分送给好友
//				req.scene = SendMessageToWX.Req.WXSceneSession;
//			}else{ //发送到朋友圈
//				req.scene = SendMessageToWX.Req.WXSceneTimeline;
//			}
			
			System.out.println("威信："+api.sendReq(req));;
		} else {//对不起，您尚未安装微信客户端
			Toast.makeText(activity, "对不起，您尚未安装微信客户端", Toast.LENGTH_LONG).show();
		}
		api.unregisterApp();
	}

	/**
	 * 分享到朋友圈
	 * @param str 要分享的文字内容
	 */
	public void shareToWeiXinFriends(String str) {
		str = str.replaceAll("###", "\r\n");
		if (str.length() > 140) {
			str = str.substring(1, 140);
		}
		IWXAPI api = WXAPIFactory.createWXAPI(activity,
				Constants.WEIXIN_APP_ID,true); // IWXAPI 第三方app和微信通讯的openapi接口
		api.registerApp(Constants.WEIXIN_APP_ID);// 将应用的appId注册到微信
		
		if (api.isWXAppInstalled()) {
			WXTextObject wxTextObject = new WXTextObject();
			/* "wxTextObject.text ="后为要分享的内容 */
			wxTextObject.text = str;

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = wxTextObject;
			msg.description = wxTextObject.text;

			// 构造一个Req
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标示一个请求
			req.message = msg;

			final int supportFriendsVersion = 0x21020001;//支持分享到朋友圈的版本
			System.out.println("api.getWXAppSupportAPI()==="+api.getWXAppSupportAPI());
			if(api.getWXAppSupportAPI() < supportFriendsVersion){//分送给好友
				Toast.makeText(activity, "对不起，您的微信版本太低，请升级4.2或以上版本", Toast.LENGTH_LONG).show();
			}else{ //发送到朋友圈
				req.scene = SendMessageToWX.Req.WXSceneTimeline;
			}
			System.out.println("威信："+api.sendReq(req));;
		} else {//对不起，您尚未安装微信客户端
			Toast.makeText(activity, "对不起，您尚未安装微信客户端", Toast.LENGTH_LONG).show();
		}
		api.unregisterApp();
	}

	
	/**
	 * 分享到新浪微博,不超过140个字
	 * @param str 要分享的文字内容
	 */
	public void shareToSinaWeibo(String str){
		Weibo weibo = Weibo.getInstance(Constants.SINA_APP_KEY, Constants.SINA_REDIRECT_URL);	
		sinaShareContent = str;
		weibo.authorize(activity, this);//认证
	}
	
	/**
	 * 分享到腾讯微博
	 * @param str 要分享的内容
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
		System.out.println("新浪：取消认证");
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onComplete(Bundle data) {
		// TODO Auto-generated method stub
		System.out.println("新浪：认证成功"+data);
		Oauth2AccessToken token = new Oauth2AccessToken(data.getString("access_token"), data.getString("expires_in"));
		
		if(token.isSessionValid()){
			System.out.println("新浪认证成功...");
			StatusesAPI weiboApi = new StatusesAPI(token);
			StringBuffer sb = new StringBuffer();
			if(sinaShareContent.length()>100){
				sb.append(sinaShareContent.substring(0, 100));
			}else{
				sb.append(sinaShareContent);
			}
			sb.append("[来自投资快报手机客户端]");
			System.out.println("新浪开始分享...");
			weiboApi.update(sb.toString(), "90.00", "90.00", sinaListester); //分享
			
		}else{
			System.out.println("新浪认证失败...");
		}
	}

	@Override
	public void onError(WeiboDialogError err) {
		// TODO Auto-generated method stub
		System.out.println("新浪：出错"+err.getMessage());
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		System.out.println("新浪：异常");
		e.printStackTrace();
		ShareContent.this.activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ShareContent.this.activity, "分享失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	

}
