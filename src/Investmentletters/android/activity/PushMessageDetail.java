package Investmentletters.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import Investmentletters.android.R;
import Investmentletters.android.dao.DBPushMsg;
import Investmentletters.android.dao.PushMsg;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.ShareContent;
import Investmentletters.android.view.NewsTextSizeDialog;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 查询推送详细数据
 * @author liang
 */
public class PushMessageDetail extends Activity implements DataVisitors.CallBack,
			OnClickListener,
			NewsTextSizeDialog.OnTextSizeChangeListener{
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	/**消息推送的dao类*/
	private PushMsg pushMsg = null;
	
	/**what:获取信息*/
	private final int WHAT_GET_DATA = 0;
	/** what:收藏 */
	private final int WHAT_PRESERVE = 1;
	/** what:取消收藏 */
	private final int WHAT_CANCEL_PRESERVE = 2;
	
	private ProgressDialog progressDialog = null;
	
	/**是否多相关新闻中打开*/
	private boolean isFromRelate = false;
	
	/** 返回按钮 */
	private View mBackButton;
	/** 收藏按钮 */
	private ImageView mCollectButton;
	/** 分享按钮 */
	private View mShareButton;
	/** 评论按钮 */
	private View mCommentButton;
	/** 调整字体大小按钮 */
	private View mChangeTextSizeButton;
	
	/**标题*/
	private TextView tvTitle = null;
	/**时间*/
	private TextView tvTime = null;
	/**新闻内容*/
	private WebView wvContent = null;
	
	/** 分享对话框 */
	private Dialog shareDialog = null;
	
	/** 内容分享类 */
	private ShareContent shareContent = null;

	/** 字体大小 */
	private int textSizeFifteen = 15;

	/** 字体大小 */
	private int textSizeNineteen = 19;

	/** 字体大小 */
	private int textSizeTwentyTwo = 22;
	/** 字体大小 */
	private int textSizeTwentyFive = 25;

	/**消息推送新闻列表数据库DAo*/
	private DBPushMsg dbPushMsg = null; 
	
	/**要显示的新闻*/
	private News news = null;
	
	/**视频框*/
	private View vVideo = null;
	
	/**新闻详细界面字体选择框*/
	private NewsTextSizeDialog textSizeDialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_message_detail);
		
		int type = -1;//新闻类型
		int id = -1;//新闻id
		try{
			Bundle data = getIntent().getExtras();
			type = data.getInt("TYPE", -1);
			id = data.getInt("ID", -1);
			isFromRelate = data.getBoolean("FROM_RELATE", false);
			System.out.println("请求数据："+data);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			type = -1;
			id = -1;
		}
		
		if(type < 0 || id < 0){
			Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		pushMsg = new PushMsg();
		dataVisitors = new DataVisitors();
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在获取新闻");
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == KeyEvent.KEYCODE_BACK){
					dialog.dismiss();
					finish();
					return true;
				}
				return false;
			}
		});

		dbPushMsg = new DBPushMsg(this);
		
		mBackButton = findViewById(R.id.id_news_show_back_btn);// 返回按钮
		mCollectButton = (ImageView)findViewById(R.id.id_collect_btn);// 收藏
		mCollectButton.setTag(false);
		mShareButton = findViewById(R.id.id_share_btn);// 分享按钮
		mCommentButton = findViewById(R.id.id_comment_btn); // 评论按钮
		mChangeTextSizeButton = findViewById(R.id.id_change_textsize_btn); // 调整字体大小按钮
		
		vVideo = findViewById(R.id.video);
		vVideo.setOnClickListener(this);
		
		tvTitle = (TextView)findViewById(R.id.title);
		tvTime = (TextView)findViewById(R.id.time);
		wvContent = (WebView)findViewById(R.id.content);
		wvContent.setBackgroundColor(Color.rgb( 240,240 ,240 ));//F0F0F0
		WebSettings settings = wvContent.getSettings();
		settings.setTextSize(TextSize.NORMAL);
		
		mBackButton.setOnClickListener(this);
		mCollectButton.setOnClickListener(this);
		mShareButton.setOnClickListener(this);
		mCommentButton.setOnClickListener(this);
		mChangeTextSizeButton.setOnClickListener(this);
		
		shareContent = new ShareContent(this);
		
		progressDialog.show();
		pushMsg.setType(type);
		dataVisitors.getFresh(pushMsg, id, this, WHAT_GET_DATA);
		
		textSizeDialog = new NewsTextSizeDialog(this);
		textSizeDialog.setListener(this);
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		
		if(what == WHAT_GET_DATA){ //获取信息
			if(res == null){
				Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
				finish();
				progressDialog.dismiss();
				return;
			}
			
			List<?> data = (List<?>)res;
			if(data.size() < 0){
				Toast.makeText(this, "数据异常", Toast.LENGTH_SHORT).show();
				finish();
				progressDialog.dismiss();
			}
			
			news = (News)data.get(0);
			if(!isFromRelate){
				dataVisitors.dbAdd(dbPushMsg, data, this, -1);//添加进数据库				
			}
			
			tvTitle.setText(news.getTitle());
			tvTime.setText("时间:"+news.getTime());
			
			/**设置内容*/
			String content = news.getContent().replaceAll("&&&","'");
			content = content.replaceAll("doublequotation","\"");
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><style type='text/css'>*{margin:0px;padding:0px;}</style></head><body style='background-color:#F7F3F7;width:100%;height:100%;'>");
//			sb.append("<video id=\"video\" src=\"http://new.etz927.com/json/b.mp4\" autoplay=\"autoplay\"   controls=\"controls\" width=\"300\" height=\"200\" onError=\"alert('error');\"/>");
			sb.append(content);
			sb.append("</body></html>");
			
			Map<String,String> videoResult = getVideo(sb.toString()); 
			
			System.out.println("视频："+videoResult);
			
			
			if(videoResult == null){//没有视频
				vVideo.setTag("");
				vVideo.setVisibility(View.GONE);
				wvContent.loadDataWithBaseURL("", sb.toString(), "text/html", HTTP.UTF_8, "");
			}else{
				vVideo.setTag(videoResult.get("video").trim());
				vVideo.setVisibility(View.VISIBLE);
				wvContent.loadDataWithBaseURL("", videoResult.get("content"), "text/html", HTTP.UTF_8, "");
			}
			
			progressDialog.dismiss();
			
			return;
		}else if (what == WHAT_PRESERVE) {//收藏
			if (res == null) {
				Toast.makeText(this, "收藏异常", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this, R.layout.toast_preserve_add, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setImageResource(R.drawable.btn_collect_normal);
					mCollectButton.setTag(true);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
		}else if(what == WHAT_CANCEL_PRESERVE){//取消收藏
			if (res == null) {
				Toast.makeText(this, "取消收藏异常", Toast.LENGTH_SHORT).show();
			} else {
				if ((Boolean) res) {
					Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
					View view = View.inflate(this, R.layout.toast_preserve_cancel, null);
					toast.setView(view);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					mCollectButton.setImageResource(R.drawable.btn_collect);
					mCollectButton.setTag(false);
				} else {
					Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch (id) {
			case R.id.id_news_show_back_btn: // 返回
				finish();
				break;
				
			case R.id.id_comment_btn://评论
				
				break;
				
			case R.id.id_change_textsize_btn://显示设置字体大小
				textSizeDialog.show();
				break;
				
			case R.id.id_collect_btn: // 收藏
				if(news != null){
					if((Boolean)mCollectButton.getTag()){//取消收藏
						dataVisitors.cancelPreserve(dbPushMsg,news.getId() , this, WHAT_CANCEL_PRESERVE);						
					}else{//收藏
						dataVisitors.preserve(dbPushMsg,news.getId() , this, WHAT_PRESERVE);
					}
				}
				break;
	
			case R.id.id_share_btn:// 弹出分享dislog
				showShareDialog();
				break;
				
			case R.id.id_weixin_share_btn:// 分享到微信
				if (news != null) {
					StringBuffer sb = new StringBuffer();
	
					sb.append("标题：");
					sb.append(news.getTitle());
					sb.append("\n\n内容：");
					sb.append(news.getContent());
					sb.append("\n\n时间：");
					sb.append(news.getTime());
					String toshare = sb.toString().substring(0, 30);
					shareContent.shareToWeiXin(toshare);
				}
				shareDialog.dismiss();
				break;
				
			case R.id.id_sina_weibo_share_btn:// 分享到新浪微博
				if (news != null) {
					StringBuffer sb = new StringBuffer();
	
					sb.append(news.getTitle());
					sb.append("\n");
					sb.append(news.getContent());
					sb.append("\n");
					sb.append(news.getTime());
	
					shareContent.shareToSinaWeibo(sb.toString());
				}
				shareDialog.dismiss();
				break;
				
			case R.id.id_tencent_weibo_share_btn:// 分享到腾讯微博
				if (news != null) {
					StringBuffer sb = new StringBuffer();
	
					sb.append(news.getTitle());
					sb.append("\n");
					sb.append(news.getContent());
					sb.append("\n");
					sb.append(news.getTime());
	
					shareContent.shareToTencentWeiBo(sb.toString());
				}
				shareDialog.dismiss();
				break;
				
			case R.id.video://点击视频
				String url = (String)v.getTag();
				url = url.trim();
				if(url.equals("")){
					System.out.println("没有视频...");
					Toast.makeText(this, "视频源异常", Toast.LENGTH_SHORT).show();
				}else{
					System.out.println("视频地址："+url);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "video/*");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				break;
		}
	}
	
	/** 显示分享dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			View mWXShareButton = dialogView
					.findViewById(R.id.id_weixin_share_btn);// 微信
			View mSinaWeiBoShareButton = dialogView
					.findViewById(R.id.id_sina_weibo_share_btn); // 新浪微博
			View mTencentWeiboShareButton = (Button) dialogView
					.findViewById(R.id.id_tencent_weibo_share_btn);// 腾讯微博

			mWXShareButton.setOnClickListener(this);
			mSinaWeiBoShareButton.setOnClickListener(this);
			mTencentWeiboShareButton.setOnClickListener(this);
		}
		shareDialog.show();
	}
	
	/**
	 * 获取视频以及内容
	 * @param content html内容
	 * @return 视频内容：key:video社频名称  content:处理过后的html内容
	 */
	public Map<String,String> getVideo(String src){

		if(src == null){
			System.out.println("源为空");
			return null;
		}
		
		src = src.trim();
		
		System.out.println("源为："+src);
		
		int pos = src.indexOf("<video");
		
		if(pos<0){
			System.out.println("找不到<video");
			return null;
		}
		
		int endPos = src.indexOf("/>", pos);
		
		System.out.println("标记开始位置："+pos);
		
		if(endPos<0){
			System.out.println("找不到标记结束位置...");
			return null;
		}
		
		System.out.println("标记结束位置："+endPos);
		
		String videoStr = src.substring(pos, endPos+2);//视频代码段
		
		System.out.println("视频代码："+videoStr);
		
		String content = src.substring(0, pos+2)+""+src.substring(endPos);//减去视频后的代码
		System.out.println("减去视频后的代码:"+content);
		
		pos = videoStr.indexOf("src=\"");
		endPos = videoStr.indexOf("\"", pos+5);
		String videoUrl = videoStr.substring(pos+5 , endPos);//视频地址
		
		Map<String,String> result = new HashMap<String, String>();
		result.put("video", videoUrl);
		result.put("content", content);
		
		return result;
	}

	@Override
	public void onTextSizeChanged(Investmentletters.android.view.NewsTextSizeDialog.TextSize size) {
		// TODO Auto-generated method stub
		WebSettings settings = wvContent.getSettings();
		switch (size) {
			case TEXT_SIZE_SMALL:
				tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeNineteen);
				settings.setTextSize(TextSize.SMALLER);
				tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeFifteen);
				break;
	
			case TEXT_SIZE_NORMAL:
				tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeTwentyTwo);
				settings.setTextSize(TextSize.NORMAL);
				tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeNineteen);
				break;
	
			case TEXT_SIZE_LARGE:
				tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeTwentyFive);
				settings.setTextSize(TextSize.LARGER);
				tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeTwentyTwo);
				break;
		}
		
	}
	
	
}
