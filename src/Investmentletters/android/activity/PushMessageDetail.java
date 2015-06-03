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
 * ��ѯ������ϸ����
 * @author liang
 */
public class PushMessageDetail extends Activity implements DataVisitors.CallBack,
			OnClickListener,
			NewsTextSizeDialog.OnTextSizeChangeListener{
	
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	/**��Ϣ���͵�dao��*/
	private PushMsg pushMsg = null;
	
	/**what:��ȡ��Ϣ*/
	private final int WHAT_GET_DATA = 0;
	/** what:�ղ� */
	private final int WHAT_PRESERVE = 1;
	/** what:ȡ���ղ� */
	private final int WHAT_CANCEL_PRESERVE = 2;
	
	private ProgressDialog progressDialog = null;
	
	/**�Ƿ����������д�*/
	private boolean isFromRelate = false;
	
	/** ���ذ�ť */
	private View mBackButton;
	/** �ղذ�ť */
	private ImageView mCollectButton;
	/** ����ť */
	private View mShareButton;
	/** ���۰�ť */
	private View mCommentButton;
	/** ���������С��ť */
	private View mChangeTextSizeButton;
	
	/**����*/
	private TextView tvTitle = null;
	/**ʱ��*/
	private TextView tvTime = null;
	/**��������*/
	private WebView wvContent = null;
	
	/** ����Ի��� */
	private Dialog shareDialog = null;
	
	/** ���ݷ����� */
	private ShareContent shareContent = null;

	/** �����С */
	private int textSizeFifteen = 15;

	/** �����С */
	private int textSizeNineteen = 19;

	/** �����С */
	private int textSizeTwentyTwo = 22;
	/** �����С */
	private int textSizeTwentyFive = 25;

	/**��Ϣ���������б����ݿ�DAo*/
	private DBPushMsg dbPushMsg = null; 
	
	/**Ҫ��ʾ������*/
	private News news = null;
	
	/**��Ƶ��*/
	private View vVideo = null;
	
	/**������ϸ��������ѡ���*/
	private NewsTextSizeDialog textSizeDialog = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.push_message_detail);
		
		int type = -1;//��������
		int id = -1;//����id
		try{
			Bundle data = getIntent().getExtras();
			type = data.getInt("TYPE", -1);
			id = data.getInt("ID", -1);
			isFromRelate = data.getBoolean("FROM_RELATE", false);
			System.out.println("�������ݣ�"+data);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			type = -1;
			id = -1;
		}
		
		if(type < 0 || id < 0){
			Toast.makeText(this, "�����쳣", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		pushMsg = new PushMsg();
		dataVisitors = new DataVisitors();
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("���ڻ�ȡ����");
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
		
		mBackButton = findViewById(R.id.id_news_show_back_btn);// ���ذ�ť
		mCollectButton = (ImageView)findViewById(R.id.id_collect_btn);// �ղ�
		mCollectButton.setTag(false);
		mShareButton = findViewById(R.id.id_share_btn);// ����ť
		mCommentButton = findViewById(R.id.id_comment_btn); // ���۰�ť
		mChangeTextSizeButton = findViewById(R.id.id_change_textsize_btn); // ���������С��ť
		
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
		
		if(what == WHAT_GET_DATA){ //��ȡ��Ϣ
			if(res == null){
				Toast.makeText(this, "�����쳣", Toast.LENGTH_SHORT).show();
				finish();
				progressDialog.dismiss();
				return;
			}
			
			List<?> data = (List<?>)res;
			if(data.size() < 0){
				Toast.makeText(this, "�����쳣", Toast.LENGTH_SHORT).show();
				finish();
				progressDialog.dismiss();
			}
			
			news = (News)data.get(0);
			if(!isFromRelate){
				dataVisitors.dbAdd(dbPushMsg, data, this, -1);//��ӽ����ݿ�				
			}
			
			tvTitle.setText(news.getTitle());
			tvTime.setText("ʱ��:"+news.getTime());
			
			/**��������*/
			String content = news.getContent().replaceAll("&&&","'");
			content = content.replaceAll("doublequotation","\"");
			StringBuffer sb = new StringBuffer();
			sb.append("<html><head><style type='text/css'>*{margin:0px;padding:0px;}</style></head><body style='background-color:#F7F3F7;width:100%;height:100%;'>");
//			sb.append("<video id=\"video\" src=\"http://new.etz927.com/json/b.mp4\" autoplay=\"autoplay\"   controls=\"controls\" width=\"300\" height=\"200\" onError=\"alert('error');\"/>");
			sb.append(content);
			sb.append("</body></html>");
			
			Map<String,String> videoResult = getVideo(sb.toString()); 
			
			System.out.println("��Ƶ��"+videoResult);
			
			
			if(videoResult == null){//û����Ƶ
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
		}else if (what == WHAT_PRESERVE) {//�ղ�
			if (res == null) {
				Toast.makeText(this, "�ղ��쳣", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, "�ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		}else if(what == WHAT_CANCEL_PRESERVE){//ȡ���ղ�
			if (res == null) {
				Toast.makeText(this, "ȡ���ղ��쳣", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, "ȡ���ղ�ʧ��", Toast.LENGTH_SHORT).show();
				}
			}
		}
		
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch (id) {
			case R.id.id_news_show_back_btn: // ����
				finish();
				break;
				
			case R.id.id_comment_btn://����
				
				break;
				
			case R.id.id_change_textsize_btn://��ʾ���������С
				textSizeDialog.show();
				break;
				
			case R.id.id_collect_btn: // �ղ�
				if(news != null){
					if((Boolean)mCollectButton.getTag()){//ȡ���ղ�
						dataVisitors.cancelPreserve(dbPushMsg,news.getId() , this, WHAT_CANCEL_PRESERVE);						
					}else{//�ղ�
						dataVisitors.preserve(dbPushMsg,news.getId() , this, WHAT_PRESERVE);
					}
				}
				break;
	
			case R.id.id_share_btn:// ��������dislog
				showShareDialog();
				break;
				
			case R.id.id_weixin_share_btn:// ����΢��
				if (news != null) {
					StringBuffer sb = new StringBuffer();
	
					sb.append("���⣺");
					sb.append(news.getTitle());
					sb.append("\n\n���ݣ�");
					sb.append(news.getContent());
					sb.append("\n\nʱ�䣺");
					sb.append(news.getTime());
					String toshare = sb.toString().substring(0, 30);
					shareContent.shareToWeiXin(toshare);
				}
				shareDialog.dismiss();
				break;
				
			case R.id.id_sina_weibo_share_btn:// ��������΢��
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
				
			case R.id.id_tencent_weibo_share_btn:// ������Ѷ΢��
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
				
			case R.id.video://�����Ƶ
				String url = (String)v.getTag();
				url = url.trim();
				if(url.equals("")){
					System.out.println("û����Ƶ...");
					Toast.makeText(this, "��ƵԴ�쳣", Toast.LENGTH_SHORT).show();
				}else{
					System.out.println("��Ƶ��ַ��"+url);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "video/*");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				break;
		}
	}
	
	/** ��ʾ����dislog */
	private void showShareDialog() {
		if (shareDialog == null) {
			shareDialog = new Dialog(this, R.style.dialog);

			LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View dialogView = inflater.inflate(R.layout.layout_share, null);
			shareDialog.setContentView(dialogView);
			shareDialog.setCanceledOnTouchOutside(true);

			View mWXShareButton = dialogView
					.findViewById(R.id.id_weixin_share_btn);// ΢��
			View mSinaWeiBoShareButton = dialogView
					.findViewById(R.id.id_sina_weibo_share_btn); // ����΢��
			View mTencentWeiboShareButton = (Button) dialogView
					.findViewById(R.id.id_tencent_weibo_share_btn);// ��Ѷ΢��

			mWXShareButton.setOnClickListener(this);
			mSinaWeiBoShareButton.setOnClickListener(this);
			mTencentWeiboShareButton.setOnClickListener(this);
		}
		shareDialog.show();
	}
	
	/**
	 * ��ȡ��Ƶ�Լ�����
	 * @param content html����
	 * @return ��Ƶ���ݣ�key:video��Ƶ����  content:��������html����
	 */
	public Map<String,String> getVideo(String src){

		if(src == null){
			System.out.println("ԴΪ��");
			return null;
		}
		
		src = src.trim();
		
		System.out.println("ԴΪ��"+src);
		
		int pos = src.indexOf("<video");
		
		if(pos<0){
			System.out.println("�Ҳ���<video");
			return null;
		}
		
		int endPos = src.indexOf("/>", pos);
		
		System.out.println("��ǿ�ʼλ�ã�"+pos);
		
		if(endPos<0){
			System.out.println("�Ҳ�����ǽ���λ��...");
			return null;
		}
		
		System.out.println("��ǽ���λ�ã�"+endPos);
		
		String videoStr = src.substring(pos, endPos+2);//��Ƶ�����
		
		System.out.println("��Ƶ���룺"+videoStr);
		
		String content = src.substring(0, pos+2)+""+src.substring(endPos);//��ȥ��Ƶ��Ĵ���
		System.out.println("��ȥ��Ƶ��Ĵ���:"+content);
		
		pos = videoStr.indexOf("src=\"");
		endPos = videoStr.indexOf("\"", pos+5);
		String videoUrl = videoStr.substring(pos+5 , endPos);//��Ƶ��ַ
		
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
