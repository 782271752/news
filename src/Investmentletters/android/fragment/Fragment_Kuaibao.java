package Investmentletters.android.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Investmentletters.android.R;
import Investmentletters.android.activity.ImageNewsDetail;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.NewsShowActivity;
import Investmentletters.android.adapter.CommonNewsAdapter;
import Investmentletters.android.adapter.InvestementBigNewsAdapter;
import Investmentletters.android.adapter.InvestmentNewsAdapter;
import Investmentletters.android.adapter.MeituAdapter;
import Investmentletters.android.component.KuaiBaoComponent;
import Investmentletters.android.dao.DBFocusNews;
import Investmentletters.android.dao.DBImageOnLine;
import Investmentletters.android.dao.DBInvestmentletters;
import Investmentletters.android.dao.DBLastTime;
import Investmentletters.android.dao.DBToday;
import Investmentletters.android.dao.DBTodayByBrand;
import Investmentletters.android.dao.FocusNews;
import Investmentletters.android.dao.ImageOnLine;
import Investmentletters.android.dao.Investmentletters;
import Investmentletters.android.dao.TodayByBrand;
import Investmentletters.android.dao.TodayNoBrand;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.DBHelper;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.IndexInvestmentViewPager;
import Investmentletters.android.view.RefreshListView;
import Investmentletters.android.view.RefreshListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 今日快报
 * @author Administrator
 */
public class Fragment_Kuaibao extends Fragment implements IXListViewListener,View.OnClickListener, AdapterView.OnItemClickListener,DataVisitors.CallBack {
	
	/**新闻类型切换栏数组*/
	private List<View> typeSwitcherList = null;
	
	/** 投资快讯列表 */
	private RefreshListView investmentNewsListView = null;
	/** 今日快讯列表 */
	private RefreshListView todayNewsListView = null;
	/** 时事聚焦列表 */
	private RefreshListView focusListView = null;
	/** 美图列表 */
	private RefreshListView meiTuNewsListView = null;

	/** 投资快讯Adapter */
	private InvestmentNewsAdapter investmentAdapter = null;
	/** 今日快报Adapter */
	private CommonNewsAdapter todayAdapter = null;
	/** 时事聚焦Adapter */
	private InvestmentNewsAdapter focusAdapter = null;
	/** 美图Adapter */
	private MeituAdapter meituAdapter = null;

	/**投资快讯头部大图ViewPager*/
	private IndexInvestmentViewPager viewPager  = null;
	/** 投资快讯列表头部大图新闻 */
	private InvestementBigNewsAdapter investmentBigNewsAdapter = null;
	/**投资快讯表头部大图标题*/
	private TextView tvInvBitTitle = null;
	/**投资快讯列表头部大图下标图组*/
	private List<View> invIndexArr = null;
	

	/** 投资快讯数据 */
	private List<News> investmentData = null;
	/** 今日快报数据 */
	private List<News> todayData = null;
	/** 时事聚焦数据 */
	private List<News> focusData = null;
	/** 美图数据 */
	private List<News> meiTuData = null;

	/** 投资快讯dao类 */
	private Investmentletters investmentletters = null;
	/**投资快讯DB dao*/
	private DBInvestmentletters dbInvestmentletters = null;
	
	/** 今日快讯，没有栏目的dao类 */
	private TodayNoBrand todayNoBrand = null;
	/** 今日快讯，没有栏目的DBdao类 */
	private DBToday dbTodayNoBrand = null;
	
	/**今日快讯，含栏目的dao*/
	private TodayByBrand todayByBrand = null;
	/**今日快讯，含栏目的DB dao*/
	private DBTodayByBrand dbTodayByBrand = null;
	
	/**时事聚焦dao*/
	private FocusNews focusNews = null;
	/**时事聚焦DB dao*/
	private DBFocusNews dbFocusNews = null;
	
	/** 美图dao类 */
	private ImageOnLine imageOnLine = null;
	/** 美图DB dao类 */
	private DBImageOnLine dbImageOnLine = null;

	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;

	/** 投资快讯 */
	private final int WHAT_GET_INV_DATA = 0;
	/** 今日快报 */
	private final int WHAT_GET_TODAY_DATA = 1;
	/**时事聚焦*/
	private final int WHAT_GET_FOCUS_DATA = 2;
	/** 美图在线*/
	private final int WHAT_GET_MEITU_DATA = 3;

	/** 投资快讯  下拉刷新/上拉加载更多*/
	private final int WHAT_GET_INV_REFRESH_DATA = 4;
	/** 今日快报 下拉刷新/上拉加载更多 */
	private final int WHAT_GET_TODAY_REFRESH_DATA = 5;
	/**时事聚焦 下拉刷新/上拉加载更多*/
	private final int WHAT_GET_FOCUS_REFRESH_DATA = 6;
	/** 美图在线 下拉刷新/上拉加载更多*/
	private final int WHAT_GET_MEITU_REFRESH_DATA = 7;
	
	/** 今日快讯筛选类型的ID,-1:不作筛选 */
	private int todayBrandId = -1;
	
	/**防重复加载:投资快讯*/
	private boolean isLoadInv = false;
	/**防重复加载:今日快讯*/
	private boolean isLoadToday = false;
	/**防重复加载:时事聚焦*/
	private boolean isLoadFocus = false;
	/**防重复加载:美图在线*/
	private boolean isLoadImg = false;
	
	/**是否已加载默认:投资快讯*/
	private boolean isLoadInvDefault = false;
	/**是否已加载默认:今日快讯*/
	private boolean isLoadTodayDefault = false;
	/**是否已加载默认:时事聚焦*/
	private boolean isLoadFocusDefault = false;
	/**是否已加载默认:美图在线*/
	private boolean isLoadImgDefault = false;
	
	
	
	/**整个界面视图。作用：在界面切换后再切换回来，不变*/
	private View vContentView = null;
	
	private SimpleDateFormat dateFormat = null;
	
	/**取消今日快讯栏目监听器*/
	private OnCancelTodayBrandLisener onCancelTodayBrandListener = null;
	/**获取最后列表一次刷新时间*/
	private DBLastTime dbLastTime = null;
	
	/**时事聚焦头部大图ViewPager*/
	private IndexInvestmentViewPager focusViewPager  = null;
	/** 时事聚焦列表头部大图新闻 */
	private InvestementBigNewsAdapter focusBigNewsAdapter = null;
	/**时事聚焦表头部大图标题*/
	private TextView tvFocusBigTitle = null;
	/**时事聚焦列表头部大图下标图组*/
	private List<View> focusIndexArr = null;
	
	/**今日快报Fragment_Kuaibao相关组件，减少单个文件代码量，增加代码可读性*/
	private KuaiBaoComponent component = null;
	
	public Fragment_Kuaibao(){
		dataVisitors = new DataVisitors(); // 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系

		investmentData = new ArrayList<News>(); //投资快讯数据
		todayData = new ArrayList<News>();// 今日快报数据
		focusData = new ArrayList<News>();
		meiTuData = new ArrayList<News>();// 美图数据
		
		investmentletters = new Investmentletters();// 投资快讯Model类
		todayNoBrand = new TodayNoBrand();// 今日快报Model类
		todayByBrand = new TodayByBrand();//今日快讯，含栏目的dao类
		focusNews = new FocusNews();
		imageOnLine = new ImageOnLine();// 美图dao
		
		dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
		
		focusIndexArr = new ArrayList<View>();//时事聚焦列表头部大图下标图组
		component = new KuaiBaoComponent();// 今日快报Fragment_Kuaibao相关组件
		invIndexArr = new ArrayList<View>();//投资快讯列表头部大图下标图组
		
		typeSwitcherList = new ArrayList<View>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = getActivity();
		
		dbInvestmentletters = new DBInvestmentletters(context);
		dbTodayNoBrand = new DBToday(context);
		dbTodayByBrand = new DBTodayByBrand(context);
		dbFocusNews = new DBFocusNews(context);
		dbImageOnLine = new DBImageOnLine(context);

		investmentAdapter = new InvestmentNewsAdapter(context, investmentData); // 投资快讯Adapter
		todayAdapter = new CommonNewsAdapter(context, todayData); // 今日快报Adapter
		meituAdapter = new MeituAdapter(context, meiTuData); // 美图adapter
		investmentBigNewsAdapter = new InvestementBigNewsAdapter(context,investmentData);// 投资快讯列表头部大图新闻
		focusAdapter = new InvestmentNewsAdapter(context, focusData);
		focusBigNewsAdapter = new InvestementBigNewsAdapter(context, focusData);
		dbLastTime = new DBLastTime(context);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		if(vContentView != null){
			ViewGroup vg = (ViewGroup)vContentView.getParent();
			vg.removeView(vContentView);
			return vContentView;
		}
		
		View mView = inflater.inflate(R.layout.list_jinrikuaibao, null);
		vContentView = mView;

		mView.findViewById(R.id.showLeft).setOnClickListener(this);// 显示左边框按钮
		mView.findViewById(R.id.showRight).setOnClickListener(this); // 显示右边框按钮

		investmentNewsListView = (RefreshListView) mView.findViewById(R.id.id_investment_news_listview);// 投资快讯列表
		todayNewsListView = (RefreshListView) mView.findViewById(R.id.id_today_news_listview);// 今日快讯列表
		focusListView = (RefreshListView) mView.findViewById(R.id.ssjj_news_listview);// 时事聚焦列表
		meiTuNewsListView = (RefreshListView) mView.findViewById(R.id.id_meitu_news_listview);// 美图列表

		investmentNewsListView.setPullLoadEnable(true);
		investmentNewsListView.setPullRefreshEnable(true);
		investmentNewsListView.setXListViewListener(this);

		todayNewsListView.setPullLoadEnable(true);
		todayNewsListView.setXListViewListener(this);
		todayNewsListView.setPullRefreshEnable(true);
		
		focusListView.setPullLoadEnable(true);
		focusListView.setXListViewListener(this);
		focusListView.setPullRefreshEnable(true);

		meiTuNewsListView.setPullLoadEnable(false);
		meiTuNewsListView.setXListViewListener(this);
		meiTuNewsListView.setPullRefreshEnable(true);

		//投资快讯大图部份
		View vBigInv = inflater.inflate(R.layout.inv_list_head_big, null);
		
		tvInvBitTitle = (TextView)vBigInv.findViewById(R.id.title);//投资快讯表头部大图标题
		
		invIndexArr.clear();
		invIndexArr.add(vBigInv.findViewById(R.id.index_one));//投资快讯表头大图部份下标：1
		invIndexArr.add(vBigInv.findViewById(R.id.index_two));//投资快讯表头大图部份下标：2
		invIndexArr.add(vBigInv.findViewById(R.id.index_three));//投资快讯表头大图部份下标：3
		
		Context context = getActivity();
		viewPager = (IndexInvestmentViewPager)vBigInv.findViewById(R.id.viewpager);
		final int screenWidth = ((MyApplication)context.getApplicationContext()).getScreenWidth();
		int height = screenWidth / 2;
		if(height<1){
			height = 170;
		}
		vBigInv.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, height));
		viewPager.setAdapter(investmentBigNewsAdapter);
		investmentNewsListView.addHeaderView(vBigInv);
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				component.setBigNewsIndexIconSelected(invIndexArr, position, investmentData);// 设置选中下标
				if(investmentData.size()>position){//设置标题
					tvInvBitTitle.setText(investmentData.get(position).getTitle());
				}
			}
			 
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		
		//时事聚焦大图部份
       View vBigFocus = inflater.inflate(R.layout.inv_list_head_big, null);
       tvFocusBigTitle = (TextView)vBigFocus.findViewById(R.id.title);//大图标题
		
       focusIndexArr.clear();
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_one));//大图部份下标：1
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_two));//大图部份下标：2
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_three));//大图部份下标：3
		
       vBigFocus.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, height));
       
       focusViewPager = (IndexInvestmentViewPager)vBigFocus.findViewById(R.id.viewpager);
       focusViewPager.setAdapter(focusBigNewsAdapter);
       focusListView.addHeaderView(vBigFocus);
		
       focusViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				component.setBigNewsIndexIconSelected(focusIndexArr, position, focusData);// 设置选中下标
				if(focusData.size()>position){//设置标题
					tvFocusBigTitle.setText(focusData.get(position).getTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		

		// 设置列表adapter
		investmentNewsListView.setAdapter(investmentAdapter);
		todayNewsListView.setAdapter(todayAdapter);
		focusListView.setAdapter(focusAdapter);
		meiTuNewsListView.setAdapter(meituAdapter);

		// 设置列表点击事件
		investmentNewsListView.setOnItemClickListener(this);
		todayNewsListView.setOnItemClickListener(this);
		focusListView.setOnItemClickListener(this);
		meiTuNewsListView.setOnItemClickListener(this);

		// 相关切换按钮
		View mInvestmentNewsLayout = (RelativeLayout) mView.findViewById(R.id.id_investment_news);
		View mTodayNewsLayout = (RelativeLayout) mView.findViewById(R.id.id_today_news);
		
		View vNewsFocus = mView.findViewById(R.id.ssjj_news);
		vNewsFocus.setBackgroundResource(R.drawable.bg_radiobtn_press);
		
		View mMeituLayout = (RelativeLayout) mView.findViewById(R.id.id_meitu_news);
		
		typeSwitcherList.clear();
		typeSwitcherList.add(vNewsFocus);
		typeSwitcherList.add(mInvestmentNewsLayout);
		typeSwitcherList.add(mTodayNewsLayout);
		typeSwitcherList.add(mMeituLayout);
		
		
		//设置切换宽度，主要为了美图在线栏在第二页
		int switchWidth = screenWidth/3;//切换栏宽度
		System.out.println("切换栏宽度:"+switchWidth);
		LayoutParams layoutParams = mInvestmentNewsLayout.getLayoutParams();
		layoutParams.width = switchWidth;
		mInvestmentNewsLayout.setLayoutParams(layoutParams);
		
		layoutParams = mTodayNewsLayout.getLayoutParams();
		layoutParams.width = switchWidth;
		mTodayNewsLayout.setLayoutParams(layoutParams);
		
		layoutParams = vNewsFocus.getLayoutParams();
		layoutParams.width = switchWidth;
		vNewsFocus.setLayoutParams(layoutParams);
		
		layoutParams = mMeituLayout.getLayoutParams();
		layoutParams.width = switchWidth;
		mMeituLayout.setLayoutParams(layoutParams);

		mInvestmentNewsLayout.setOnClickListener(this);
		mTodayNewsLayout.setOnClickListener(this);
		vNewsFocus.setOnClickListener(this);
		mMeituLayout.setOnClickListener(this);

		investmentNewsListView.setVisibility(View.GONE); 
		todayNewsListView.setVisibility(View.GONE);
		focusListView.setVisibility(View.VISIBLE);
		meiTuNewsListView.setVisibility(View.GONE);
		
		focusListView.refreshVisible();// 刷新数据
		
		return mView;
	}

	/* 下拉刷新 */
	@Override
	public void onRefresh() {
		
		int lastId = -1;

		System.out.println("下拉.....刷新");
		
		if(isViewVisible(investmentNewsListView) && !isLoadInv){// 投资快讯 下拉刷新
			isLoadInv = true;
			lastId = investmentAdapter.getLatestId(); //获取投资快讯列表最后一条数据Id
			
			if(Utils.isNetWork(getActivity())){//有网
				
				component.netLoadFreshPloy(lastId, dbInvestmentletters, investmentletters, 
										dataVisitors, this, WHAT_GET_INV_DATA, WHAT_GET_INV_REFRESH_DATA, 
										isLoadInvDefault, investmentData, viewPager, invIndexArr, 
										tvInvBitTitle, investmentAdapter, investmentBigNewsAdapter, 
										dbLastTime, DBHelper.TYPE_TOUZI);
				isLoadInvDefault = true;
				
			}else{//无网，加载离线
				if (lastId < 0) {//表示没有数据，为初次加载
					dataVisitors.getListDefault(dbInvestmentletters, this, WHAT_GET_INV_DATA);
				} else {//下拉刷新 
					dataVisitors.getFresh(dbInvestmentletters, lastId, this,WHAT_GET_INV_REFRESH_DATA);
				}				
			}
			
		}else if(isViewVisible(todayNewsListView) && !isLoadToday){ // 今日快讯 下拉刷新
			isLoadToday = true;
			lastId = todayAdapter.getLatestId();//获取今日快讯 列表最后一条数据Id 
			
			if(Utils.isNetWork(getActivity())){//有网
				if(todayBrandId < 0){
					component.netLoadFreshPloy(lastId, dbTodayNoBrand, todayNoBrand, 
							dataVisitors, this, WHAT_GET_TODAY_DATA, WHAT_GET_TODAY_REFRESH_DATA, 
							isLoadTodayDefault, todayData, null, null, 
							null, todayAdapter, null, 
							dbLastTime, DBHelper.TYPE_JINRI);
					
					isLoadTodayDefault = true;
							
				}else{
					todayByBrand.setType(todayBrandId);
					
					component.netLoadFreshPloy(lastId, dbTodayByBrand, todayByBrand, 
							dataVisitors, this, WHAT_GET_TODAY_DATA, WHAT_GET_TODAY_REFRESH_DATA, 
							isLoadTodayDefault, todayData, null, null, 
							null, todayAdapter, null, 
							dbLastTime, DBHelper.TYPE_JINRI);
					
					isLoadTodayDefault = true;
				}
				
			}else{//无网，加载离线
				if(todayBrandId < 0){
					if (lastId < 0) {//表示没有数据，为初次加载
						dataVisitors.getListDefault(dbTodayNoBrand, this, WHAT_GET_TODAY_DATA);
					} else {//下拉刷新 
						dataVisitors.getFresh(dbTodayNoBrand, lastId, this,WHAT_GET_TODAY_REFRESH_DATA);
					}
				}else{
					dbTodayByBrand.setType(todayBrandId);
					if (lastId < 0) {//表示没有数据，为初次加载
						dataVisitors.getListDefault(dbTodayByBrand, this, WHAT_GET_TODAY_REFRESH_DATA);
					} else {//下拉刷新 
						dataVisitors.getFresh(dbTodayByBrand, lastId, this,WHAT_GET_TODAY_REFRESH_DATA);
					}
				}
			}
			
		}else if(isViewVisible(meiTuNewsListView) && !isLoadImg){ // 美图在线 下拉刷新
			isLoadImg = true;
			lastId = meituAdapter.getLatestId();//获取美图在线 列表最后一条数据Id
			
			if(Utils.isNetWork(getActivity())){//有网
				
				component.netLoadFreshPloy(lastId, dbImageOnLine, imageOnLine, 
						dataVisitors, this, WHAT_GET_MEITU_DATA, WHAT_GET_MEITU_REFRESH_DATA, 
						isLoadImgDefault, meiTuData, null, null, 
						null, meituAdapter, null, 
						dbLastTime, DBHelper.TYPE_MEITU);
				
				isLoadImgDefault = true;
				
			}else{//无网，加载离线
				if (lastId < 0) { //表示没有数据，为初次加载
					dataVisitors.getListDefault(dbImageOnLine, this, WHAT_GET_MEITU_DATA);
				} else {//下拉刷新
					dataVisitors.getFresh(dbImageOnLine, lastId, this,WHAT_GET_MEITU_REFRESH_DATA);
				}
			}
		}else if(isViewVisible(focusListView) && !isLoadFocus){//时事聚焦下拉刷新
			isLoadFocus = true;
			lastId = focusAdapter.getLatestId();
			if(Utils.isNetWork(getActivity())){//有网
				
				component.netLoadFreshPloy(lastId, dbFocusNews, focusNews, 
						dataVisitors, this, WHAT_GET_FOCUS_DATA, WHAT_GET_FOCUS_REFRESH_DATA, 
						isLoadFocusDefault, focusData, focusViewPager, focusIndexArr, 
						tvFocusBigTitle, focusAdapter, focusBigNewsAdapter, 
						dbLastTime, DBHelper.TYPE_FOCUS);
				isLoadFocusDefault= true;
				
			}else{//无网，加载离线
				if (lastId < 0) { //表示没有数据，为初次加载
					dataVisitors.getListDefault(dbFocusNews, this, WHAT_GET_FOCUS_DATA);
				} else {//下拉刷新
					dataVisitors.getFresh(dbFocusNews, lastId, this,WHAT_GET_FOCUS_REFRESH_DATA);
				}
			}
		}
		
		
	}

	@Override
	public void onLoadMore() {
		int minId = -1;
		System.out.println("下拉.....加载");
		if(isViewVisible(investmentNewsListView)){// 投资快讯 上拉加载更多
			isLoadInv = true;
			minId = investmentAdapter.getMinId();//获取投资快讯新闻列表最小的新闻Id
			
			if(Utils.isNetWork(getActivity())){//有网
				dataVisitors.getMore(investmentletters, minId, this, WHAT_GET_INV_REFRESH_DATA);
				dataVisitors.updateList(investmentletters, dbInvestmentletters, dbLastTime, DBHelper.TYPE_TOUZI, null, 0);
			}else{//无网，加载离线
				dataVisitors.getMore(dbInvestmentletters, minId, this, WHAT_GET_INV_REFRESH_DATA);
			}
			
		}else if(isViewVisible(todayNewsListView)){ //// 今日快讯 上拉加载更多
			isLoadToday = true;
			minId = todayAdapter.getMinId();//获取今日快讯新闻列表最小的新闻Id
			
			if(Utils.isNetWork(getActivity())){//有网
				if(todayBrandId < 0){
					dataVisitors.getMore(todayNoBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
					dataVisitors.updateList(todayNoBrand, dbTodayByBrand, dbLastTime, DBHelper.TYPE_JINRI, null, 0);
				}else{
					dataVisitors.getMore(todayByBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
					dataVisitors.updateList(todayByBrand, dbTodayNoBrand, dbLastTime, DBHelper.TYPE_JINRI, null, 0);
				}
				
			}else{//无网，加载离线
				dataVisitors.getMore(dbTodayNoBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
			}
			
		}else if(isViewVisible(focusListView) && !isLoadFocus){//时事聚焦 上拉加载更多
			isLoadFocus = true;
			minId = focusAdapter.getMinId();//获取时事聚焦新闻列表最小的新闻Id
			
			if(Utils.isNetWork(getActivity())){//有网
				dataVisitors.getMore(focusNews, minId, this, WHAT_GET_FOCUS_REFRESH_DATA);
				dataVisitors.updateList(focusNews, dbFocusNews, dbLastTime, DBHelper.TYPE_FOCUS, null, 0);
			}else{//无网，加载离线
				dataVisitors.getMore(dbFocusNews, minId, this, WHAT_GET_FOCUS_REFRESH_DATA);
			}
		}
	}

	/**
	 * 筛选显示今日快报
	 * @param brandId 栏目id
	 */
	public void select(int id) {
		if(isLoadToday){//防重复加载:今日快讯
			return;
		}
		
		if(todayBrandId != id){
			todayData.clear();
			todayAdapter.notifyDataSetChanged();
		}
		
		todayBrandId = id;
		
		component.setSwitchBrandSelected(typeSwitcherList, 2);

		investmentNewsListView.setVisibility(View.GONE);
		todayNewsListView.setVisibility(View.VISIBLE);
		focusListView.setVisibility(View.GONE);
		meiTuNewsListView.setVisibility(View.GONE);

		todayNewsListView.refreshVisible();

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.showLeft: // 显示左边框
				((InterfaceActivity) getActivity()).showLeft();
				break;
	
			case R.id.showRight: // 显示右边框
				((InterfaceActivity) getActivity()).showRight();
				break;
	
			case R.id.id_investment_news: // 切换到投资快讯
				
				component.setSwitchBrandSelected(typeSwitcherList, v);
	
				investmentNewsListView.setVisibility(View.VISIBLE);
				todayNewsListView.setVisibility(View.GONE);
				focusListView.setVisibility(View.GONE);
				meiTuNewsListView.setVisibility(View.GONE);
				
				if(investmentAdapter.getCount() < 1){
					investmentNewsListView.refreshVisible();
				}
				break;
	
			case R.id.id_today_news:// 切换到今日快讯
				
				if(todayBrandId > 0){//之前是筛选过的，现在清除筛选
					todayData.clear();
					todayAdapter.notifyDataSetChanged();
					if(onCancelTodayBrandListener != null){//取消今日快讯栏目监听器,用于左边栏
						onCancelTodayBrandListener.onCancel();
					}
				}
				
				todayBrandId = -1;
				
				component.setSwitchBrandSelected(typeSwitcherList, v);
	
				investmentNewsListView.setVisibility(View.GONE);
				todayNewsListView.setVisibility(View.VISIBLE);
				focusListView.setVisibility(View.GONE);
				meiTuNewsListView.setVisibility(View.GONE);
				
				if(todayAdapter.getCount()<1){
					todayNewsListView.refreshVisible();
				}
	
				break;
				
			case R.id.ssjj_news://切换到时事聚焦
				component.setSwitchBrandSelected(typeSwitcherList, v);
				
				investmentNewsListView.setVisibility(View.GONE);
				todayNewsListView.setVisibility(View.GONE);
				focusListView.setVisibility(View.VISIBLE);
				meiTuNewsListView.setVisibility(View.GONE);
				
				if(focusAdapter.getCount() < 1){
					focusListView.refreshVisible();
				}
				break;
	
			case R.id.id_meitu_news:// 切换到美图在线
				
				component.setSwitchBrandSelected(typeSwitcherList, v);
	
				investmentNewsListView.setVisibility(View.GONE);
				todayNewsListView.setVisibility(View.GONE);
				focusListView.setVisibility(View.GONE);
				meiTuNewsListView.setVisibility(View.VISIBLE);
	
				if(meituAdapter.getCount()<1){
					meiTuNewsListView.refreshVisible();
				}
				break;
	
			default:
				break;
		}
	}

	@Override
	public void onResult(int what, Object res) {
		
		Context context = getActivity();
		System.out.println("数据返回："+context);

		List<?> data = (List<?>) res;

		switch (what) {
			case WHAT_GET_INV_DATA: // 获取投资快讯初始加载20条
				isLoadInv = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//添加到数据库
					investmentData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						investmentData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(investmentData);//这个比较特别，因有大图以及新闻列表共用，因此在这排序
				
				if(investmentData.size()>0){
					viewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(invIndexArr, 0, investmentData);// 设置选中下标
					tvInvBitTitle.setText(investmentData.get(0).getTitle());	
				}
				
				System.out.println("投资快报刷新");
				investmentBigNewsAdapter.notifyDataSetChanged();
				investmentAdapter.notifyDataSetChanged();
				investmentNewsListView.stopRefresh();
				investmentNewsListView.stopLoadMore();
				investmentNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_INV_REFRESH_DATA: //投资快讯 下拉刷新/上拉加载更多
				isLoadInv = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//添加到数据库
					int size = data.size();
					for (int i = 0; i < size; i++) {
						investmentData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(investmentData);//这个比较特别，因有大图以及新闻列表共用，因此在这排序
				
				if(investmentData.size()>0){
					viewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(invIndexArr, 0, investmentData);// 设置选中下标
					tvInvBitTitle.setText(investmentData.get(0).getTitle());	
				}
				
				investmentBigNewsAdapter.notifyDataSetChanged();
				investmentAdapter.notifyDataSetChanged();
				investmentNewsListView.stopRefresh();
				investmentNewsListView.stopLoadMore();
				investmentNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_TODAY_DATA: // 获取今日快据前20条数据
				isLoadToday = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					
					if(todayBrandId < 0){
						dataVisitors.dbAdd(dbTodayNoBrand, data, null, 0);
					}else{
						dataVisitors.dbAdd(dbTodayByBrand, data, null, 0);
					}
					
					todayData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						todayData.add((News) data.get(i));
					}
				}
				todayAdapter.notifyDataSetChanged();
				todayNewsListView.stopRefresh();
				todayNewsListView.stopLoadMore();
				todayNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_TODAY_REFRESH_DATA: //今日快报 下拉刷新/上拉加载更多
				isLoadToday = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					
					if(todayBrandId < 0){
						dataVisitors.dbAdd(dbTodayNoBrand, data, null, 0);
					}else{
						dataVisitors.dbAdd(dbTodayByBrand, data, null, 0);
					}
					
					int size = data.size();
					for (int i = 0; i < size; i++) {
						todayData.add((News) data.get(i));
					}
				}
				todayAdapter.notifyDataSetChanged();
				todayNewsListView.stopRefresh();
				todayNewsListView.stopLoadMore();
				todayNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_MEITU_DATA: // 获取美图数据
				isLoadImg = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					
					dataVisitors.dbAdd(dbImageOnLine, data, null, 0);
					
					meiTuData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						meiTuData.add((News) data.get(i));
					}
				}
				meituAdapter.notifyDataSetChanged();
				meiTuNewsListView.stopRefresh();
				meiTuNewsListView.stopLoadMore();
				meiTuNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_MEITU_REFRESH_DATA: // 美图在线 下拉刷新/上拉加载更多
				isLoadImg = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					
					dataVisitors.dbAdd(dbImageOnLine, data, null, 0);
					
					int size = data.size();
					for (int i = 0; i < size; i++) {
						meiTuData.add((News) data.get(i));
					}
				}
				meituAdapter.notifyDataSetChanged();
				meiTuNewsListView.stopRefresh();
				meiTuNewsListView.stopLoadMore();
				meiTuNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_FOCUS_DATA:// 获取时事聚焦前20条数据
				System.out.println("FOCUS111111111111111111111");
				isLoadFocus = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbFocusNews, data, null, 0);//添加到数据库
					focusData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						focusData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(focusData);//这个比较特别，因有大图以及新闻列表共用，因此在这排序
				
				if(focusData.size()>0){
					focusViewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(focusIndexArr, 0, focusData);// 设置选中下标
					tvFocusBigTitle.setText(focusData.get(0).getTitle());	
				}
				
				focusAdapter.notifyDataSetChanged();
				focusBigNewsAdapter.notifyDataSetChanged();
				focusListView.stopRefresh();
				focusListView.stopLoadMore();
				focusListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				
				break;
				
			case WHAT_GET_FOCUS_REFRESH_DATA://时事聚焦 下拉刷新/上拉加载更多
				isLoadFocus = false;
				System.out.println("FOCUS2222222222222222222");
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbFocusNews, data, null, 0);//添加到数据库
					int size = data.size();
					for (int i = 0; i < size; i++) {
						focusData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(focusData);//这个比较特别，因有大图以及新闻列表共用，因此在这排序
				
				if(focusData.size()>0){
					focusViewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(focusIndexArr, 0, focusData);// 设置选中下标
					tvFocusBigTitle.setText(focusData.get(0).getTitle());	
				}
				
				focusBigNewsAdapter.notifyDataSetChanged();
				focusAdapter.notifyDataSetChanged();
				focusListView.stopRefresh();
				focusListView.stopLoadMore();
				focusListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			default:
				break;
		}

	}

	/** 点击列表事件,显示详细新闻 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Activity activity = getActivity();
		Intent intent = new Intent(activity, NewsShowActivity.class);
		Intent imgIntent = new Intent(activity, ImageNewsDetail.class);

		MyApplication app = (MyApplication) activity.getApplication();

		
		if (position == 0) {// 点中头部更多
			return;
		}
		
		if(isViewVisible(investmentNewsListView)){// 投资快讯
			position++;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(investmentData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			
		}else if(isViewVisible(todayNewsListView)){ //// 今日快讯 
			
			position--;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(todayData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else if(isViewVisible(focusListView)){//时事聚焦
			position++;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(focusData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else if(isViewVisible(meiTuNewsListView)){ // 美图在线
			position--;
			imgIntent.putExtra("INDEX", meiTuData.get(position).getId());
			imgIntent.putExtra("TITLE", meiTuData.get(position).getTitle());
			app.setShowDetailDatas(meiTuData);
			startActivity(imgIntent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}
	}
	
	/**
	 * view是否可见
	 * @param view
	 * @return true:可见 false:不可见
	 */
	private boolean isViewVisible(View view){
		return view.getVisibility() == View.VISIBLE;
	}
	
	
	public void setOnCancelTodayBrandListener(OnCancelTodayBrandLisener onCancelTodayBrandListener){
		this.onCancelTodayBrandListener = onCancelTodayBrandListener;
	}
	
	/**取消今日快讯栏目监听器*/
	public interface OnCancelTodayBrandLisener{
		/**取消今日快讯栏目选中*/
		public void onCancel();
	}

}
