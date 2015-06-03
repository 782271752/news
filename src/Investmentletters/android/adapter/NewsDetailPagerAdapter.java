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
 * 详细新闻ViewPager容器adapter<br/>
 * <strong>现只支持单视频播放,且限定格式。具体见文档</strong>
 * @author liang
 */
public class NewsDetailPagerAdapter extends PagerAdapter implements View.OnClickListener{

	/**新闻列表*/
	private List<News> data = null;
	/**xml解析类*/
	private LayoutInflater inflater = null;
	
	/**标题 字体大小*/
	private float titleTextSize = 25;
	/**内容 字体大小*/
	private float contentTextSize = 19;
	
	private Context context = null;
	
	/**相关新闻点击事件用到*/
	private WebViewClient weiViewClient = null;
	
	/**
	 * 构造
	 * @param context
	 * @param data 新闻列表
	 */
	public NewsDetailPagerAdapter(Context context , List<News> data){
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		weiViewClient = new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				System.out.println("加载："+url);
				try{
					//打开相关新闻
					
					int position = (Integer)view.getTag();//哪个下标的webview
					int type = NewsDetailPagerAdapter.this.data.get(position).getType();//获取新闻类型
					Intent intent = new Intent(NewsDetailPagerAdapter.this.context, PushMessageDetail.class);
					
					if(type == NewsListBase.TYPE_TODAY_NO_BRAND){
						type = 1;
					}else{
						type = 0;
					}
					
					intent.putExtra("TYPE", type);
					intent.putExtra("ID", Integer.valueOf(url.trim()));
					intent.putExtra("FROM_RELATE", true);//标记从相关新闻中打开
					NewsDetailPagerAdapter.this.context.startActivity(intent);
					
					return true;
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//点击了相关新闻
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
		
		View vVideo = view.findViewById(R.id.video);//视频
		vVideo.setOnClickListener(this);
		
		TextView mTitleTextView = (TextView) view.findViewById(R.id.id_news_show_title); //标题
		TextView mTimeTextView = (TextView) view.findViewById(R.id.id_source_time);//时时
		WebView wvContent = (WebView) view.findViewById(R.id.id_content);//内容
		wvContent.setFocusableInTouchMode(false);
		wvContent.setFocusable(false);
		wvContent.setWebViewClient(weiViewClient);
		wvContent.setTag(position);
		
		/**设置标题*/
		mTitleTextView.setText(param.getTitle());
		mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
		
		/**设置内容*/
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
		if(contentTextSize < 19){ //小号字体
			settings.setTextSize(TextSize.SMALLER);	
		}else if(contentTextSize > 19){//大号字体
			settings.setTextSize(TextSize.LARGER);
		}else{//中号字体
			settings.setTextSize(TextSize.NORMAL);
		}
		
		Map<String,String> videoResult = getVideo(sb.toString()); 
		
		System.out.println("视频："+videoResult);
		
		/**设置时间*/
		mTimeTextView.setText("时间:" + param.getTime());
		
		if(videoResult == null){//没有视频
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
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;//每次刷新界面
	}
	
	/**
	 * 设置字体大小
	 * 
	 * titleTextSize 标题字体大小
	 * contentTextSize 内容字体大小
	 * timeTextSize 时间字体大小
	 */
	public void setTextSize(int titleTextSize, int contentTextSize, int timeTextSize){
		this.titleTextSize = titleTextSize;
		this.contentTextSize = contentTextSize;
		notifyDataSetChanged();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		//防止界面重叠
		ViewPager viewPager = (ViewPager)container;
		viewPager.removeView((View)object);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String url = (String)v.getTag();
		url = url.trim();
		if(url.equals("")){
			System.out.println("没有视频...");
			Toast.makeText(context, "视频源异常", Toast.LENGTH_SHORT).show();
		}else{
			System.out.println("视频地址："+url);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(url), "video/*");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		
	}
	
}
