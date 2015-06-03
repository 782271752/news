package Investmentletters.android.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import Investmentletters.android.R;
import Investmentletters.android.activity.PushMessageDetail;
import Investmentletters.android.dao.base.NewsListBase;
import Investmentletters.android.entity.News;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��ϸ����ViewPager����adapter<br/>
 * <strong>��ֻ֧�ֵ���Ƶ����,���޶���ʽ��������ĵ�</strong>
 * @author liang
 */
public class NewsDetailPagerAdapter extends PagerAdapter implements View.OnClickListener{

	/**�����б�*/
	private List<News> data = null;
	/**xml������*/
	private LayoutInflater inflater = null;
	
	/**���� �����С*/
	private float titleTextSize = 25;
	/**���� �����С*/
	private float contentTextSize = 19;
	
	private Context context = null;
	
	/**������ŵ���¼��õ�*/
	private WebViewClient weiViewClient = null;
	
	/**
	 * ����
	 * @param context
	 * @param data �����б�
	 */
	public NewsDetailPagerAdapter(Context context , List<News> data){
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		weiViewClient = new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				System.out.println("���أ�"+url);
				try{
					//���������
					
					int position = (Integer)view.getTag();//�ĸ��±��webview
					int type = NewsDetailPagerAdapter.this.data.get(position).getType();//��ȡ��������
					Intent intent = new Intent(NewsDetailPagerAdapter.this.context, PushMessageDetail.class);
					
					if(type == NewsListBase.TYPE_TODAY_NO_BRAND){
						type = 1;
					}else{
						type = 0;
					}
					
					intent.putExtra("TYPE", type);
					intent.putExtra("ID", Integer.valueOf(url.trim()));
					intent.putExtra("FROM_RELATE", true);//��Ǵ���������д�
					NewsDetailPagerAdapter.this.context.startActivity(intent);
					
					return true;
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//������������
				return super.shouldOverrideUrlLoading(view, url);
			}
		};
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		// TODO Auto-generated method stub
		return view == (View)obj;
	}
	
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.viewpageitem_show_datail, null);
		News param = data.get(position);
		
		View vVideo = view.findViewById(R.id.video);//��Ƶ
		vVideo.setOnClickListener(this);
		
		TextView mTitleTextView = (TextView) view.findViewById(R.id.id_news_show_title); //����
		TextView mTimeTextView = (TextView) view.findViewById(R.id.id_source_time);//ʱʱ
		WebView wvContent = (WebView) view.findViewById(R.id.id_content);//����
		wvContent.setFocusableInTouchMode(false);
		wvContent.setFocusable(false);
		wvContent.setWebViewClient(weiViewClient);
		wvContent.setTag(position);
		
		/**���ñ���*/
		mTitleTextView.setText(param.getTitle());
		mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
		
		/**��������*/
		String content = param.getContent().replaceAll("&&&&","\"");
		content = content.replaceAll("&&&","\'");
		content = content.replaceAll("doublequotation","\"");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><style type='text/css'>*{margin:0px;padding:0px;}</style></head><body style='background-color:#F0F0F0;width:100%;height:100%;'>");
//		sb.append("<video id=\"video\" src=\"http://new.etz927.com/json/b.mp4\" autoplay=\"autoplay\"   controls=\"controls\" width=\"300\" height=\"200\" onError=\"alert('error');\"/>");
		sb.append(content);
		sb.append("</body></html>");
		
		wvContent.setBackgroundColor(Color.rgb( 240,240 ,240 ));//F0F0F0
		WebSettings settings = wvContent.getSettings();
		if(contentTextSize < 19){ //С������
			settings.setTextSize(TextSize.SMALLER);	
		}else if(contentTextSize > 19){//�������
			settings.setTextSize(TextSize.LARGER);
		}else{//�к�����
			settings.setTextSize(TextSize.NORMAL);
		}
		
		Map<String,String> videoResult = getVideo(sb.toString()); 
		
		System.out.println("��Ƶ��"+videoResult);
		
		/**����ʱ��*/
		mTimeTextView.setText("ʱ��:" + param.getTime());
		
		if(videoResult == null){//û����Ƶ
			vVideo.setTag("");
			vVideo.setVisibility(View.GONE);
			wvContent.loadDataWithBaseURL("", sb.toString(), "text/html", HTTP.UTF_8, "");
		}else{
			vVideo.setTag(videoResult.get("video").trim());
			vVideo.setVisibility(View.VISIBLE);
			wvContent.loadDataWithBaseURL("", videoResult.get("content"), "text/html", HTTP.UTF_8, "");
		}
		
		ViewPager pager = (ViewPager)container;
		pager.addView(view);
		
		return view;
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
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;//ÿ��ˢ�½���
	}
	
	/**
	 * ���������С
	 * 
	 * titleTextSize ���������С
	 * contentTextSize ���������С
	 * timeTextSize ʱ�������С
	 */
	public void setTextSize(int titleTextSize, int contentTextSize, int timeTextSize){
		this.titleTextSize = titleTextSize;
		this.contentTextSize = contentTextSize;
		notifyDataSetChanged();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//��ֹ�����ص�
		ViewPager viewPager = (ViewPager)container;
		viewPager.removeView((View)object);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String url = (String)v.getTag();
		url = url.trim();
		if(url.equals("")){
			System.out.println("û����Ƶ...");
			Toast.makeText(context, "��ƵԴ�쳣", Toast.LENGTH_SHORT).show();
		}else{
			System.out.println("��Ƶ��ַ��"+url);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url), "video/*");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		
	}
	
}
