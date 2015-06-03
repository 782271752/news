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
 * ���տ챨
 * @author Administrator
 */
public class Fragment_Kuaibao extends Fragment implements IXListViewListener,View.OnClickListener, AdapterView.OnItemClickListener,DataVisitors.CallBack {
	
	/**���������л�������*/
	private List<View> typeSwitcherList = null;
	
	/** Ͷ�ʿ�Ѷ�б� */
	private RefreshListView investmentNewsListView = null;
	/** ���տ�Ѷ�б� */
	private RefreshListView todayNewsListView = null;
	/** ʱ�¾۽��б� */
	private RefreshListView focusListView = null;
	/** ��ͼ�б� */
	private RefreshListView meiTuNewsListView = null;

	/** Ͷ�ʿ�ѶAdapter */
	private InvestmentNewsAdapter investmentAdapter = null;
	/** ���տ챨Adapter */
	private CommonNewsAdapter todayAdapter = null;
	/** ʱ�¾۽�Adapter */
	private InvestmentNewsAdapter focusAdapter = null;
	/** ��ͼAdapter */
	private MeituAdapter meituAdapter = null;

	/**Ͷ�ʿ�Ѷͷ����ͼViewPager*/
	private IndexInvestmentViewPager viewPager  = null;
	/** Ͷ�ʿ�Ѷ�б�ͷ����ͼ���� */
	private InvestementBigNewsAdapter investmentBigNewsAdapter = null;
	/**Ͷ�ʿ�Ѷ��ͷ����ͼ����*/
	private TextView tvInvBitTitle = null;
	/**Ͷ�ʿ�Ѷ�б�ͷ����ͼ�±�ͼ��*/
	private List<View> invIndexArr = null;
	

	/** Ͷ�ʿ�Ѷ���� */
	private List<News> investmentData = null;
	/** ���տ챨���� */
	private List<News> todayData = null;
	/** ʱ�¾۽����� */
	private List<News> focusData = null;
	/** ��ͼ���� */
	private List<News> meiTuData = null;

	/** Ͷ�ʿ�Ѷdao�� */
	private Investmentletters investmentletters = null;
	/**Ͷ�ʿ�ѶDB dao*/
	private DBInvestmentletters dbInvestmentletters = null;
	
	/** ���տ�Ѷ��û����Ŀ��dao�� */
	private TodayNoBrand todayNoBrand = null;
	/** ���տ�Ѷ��û����Ŀ��DBdao�� */
	private DBToday dbTodayNoBrand = null;
	
	/**���տ�Ѷ������Ŀ��dao*/
	private TodayByBrand todayByBrand = null;
	/**���տ�Ѷ������Ŀ��DB dao*/
	private DBTodayByBrand dbTodayByBrand = null;
	
	/**ʱ�¾۽�dao*/
	private FocusNews focusNews = null;
	/**ʱ�¾۽�DB dao*/
	private DBFocusNews dbFocusNews = null;
	
	/** ��ͼdao�� */
	private ImageOnLine imageOnLine = null;
	/** ��ͼDB dao�� */
	private DBImageOnLine dbImageOnLine = null;

	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;

	/** Ͷ�ʿ�Ѷ */
	private final int WHAT_GET_INV_DATA = 0;
	/** ���տ챨 */
	private final int WHAT_GET_TODAY_DATA = 1;
	/**ʱ�¾۽�*/
	private final int WHAT_GET_FOCUS_DATA = 2;
	/** ��ͼ����*/
	private final int WHAT_GET_MEITU_DATA = 3;

	/** Ͷ�ʿ�Ѷ  ����ˢ��/�������ظ���*/
	private final int WHAT_GET_INV_REFRESH_DATA = 4;
	/** ���տ챨 ����ˢ��/�������ظ��� */
	private final int WHAT_GET_TODAY_REFRESH_DATA = 5;
	/**ʱ�¾۽� ����ˢ��/�������ظ���*/
	private final int WHAT_GET_FOCUS_REFRESH_DATA = 6;
	/** ��ͼ���� ����ˢ��/�������ظ���*/
	private final int WHAT_GET_MEITU_REFRESH_DATA = 7;
	
	/** ���տ�Ѷɸѡ���͵�ID,-1:����ɸѡ */
	private int todayBrandId = -1;
	
	/**���ظ�����:Ͷ�ʿ�Ѷ*/
	private boolean isLoadInv = false;
	/**���ظ�����:���տ�Ѷ*/
	private boolean isLoadToday = false;
	/**���ظ�����:ʱ�¾۽�*/
	private boolean isLoadFocus = false;
	/**���ظ�����:��ͼ����*/
	private boolean isLoadImg = false;
	
	/**�Ƿ��Ѽ���Ĭ��:Ͷ�ʿ�Ѷ*/
	private boolean isLoadInvDefault = false;
	/**�Ƿ��Ѽ���Ĭ��:���տ�Ѷ*/
	private boolean isLoadTodayDefault = false;
	/**�Ƿ��Ѽ���Ĭ��:ʱ�¾۽�*/
	private boolean isLoadFocusDefault = false;
	/**�Ƿ��Ѽ���Ĭ��:��ͼ����*/
	private boolean isLoadImgDefault = false;
	
	
	
	/**����������ͼ�����ã��ڽ����л������л�����������*/
	private View vContentView = null;
	
	private SimpleDateFormat dateFormat = null;
	
	/**ȡ�����տ�Ѷ��Ŀ������*/
	private OnCancelTodayBrandLisener onCancelTodayBrandListener = null;
	/**��ȡ����б�һ��ˢ��ʱ��*/
	private DBLastTime dbLastTime = null;
	
	/**ʱ�¾۽�ͷ����ͼViewPager*/
	private IndexInvestmentViewPager focusViewPager  = null;
	/** ʱ�¾۽��б�ͷ����ͼ���� */
	private InvestementBigNewsAdapter focusBigNewsAdapter = null;
	/**ʱ�¾۽���ͷ����ͼ����*/
	private TextView tvFocusBigTitle = null;
	/**ʱ�¾۽��б�ͷ����ͼ�±�ͼ��*/
	private List<View> focusIndexArr = null;
	
	/**���տ챨Fragment_Kuaibao�����������ٵ����ļ������������Ӵ���ɶ���*/
	private KuaiBaoComponent component = null;
	
	public Fragment_Kuaibao(){
		dataVisitors = new DataVisitors(); // ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ

		investmentData = new ArrayList<News>(); //Ͷ�ʿ�Ѷ����
		todayData = new ArrayList<News>();// ���տ챨����
		focusData = new ArrayList<News>();
		meiTuData = new ArrayList<News>();// ��ͼ����
		
		investmentletters = new Investmentletters();// Ͷ�ʿ�ѶModel��
		todayNoBrand = new TodayNoBrand();// ���տ챨Model��
		todayByBrand = new TodayByBrand();//���տ�Ѷ������Ŀ��dao��
		focusNews = new FocusNews();
		imageOnLine = new ImageOnLine();// ��ͼdao
		
		dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
		
		focusIndexArr = new ArrayList<View>();//ʱ�¾۽��б�ͷ����ͼ�±�ͼ��
		component = new KuaiBaoComponent();// ���տ챨Fragment_Kuaibao������
		invIndexArr = new ArrayList<View>();//Ͷ�ʿ�Ѷ�б�ͷ����ͼ�±�ͼ��
		
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

		investmentAdapter = new InvestmentNewsAdapter(context, investmentData); // Ͷ�ʿ�ѶAdapter
		todayAdapter = new CommonNewsAdapter(context, todayData); // ���տ챨Adapter
		meituAdapter = new MeituAdapter(context, meiTuData); // ��ͼadapter
		investmentBigNewsAdapter = new InvestementBigNewsAdapter(context,investmentData);// Ͷ�ʿ�Ѷ�б�ͷ����ͼ����
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

		mView.findViewById(R.id.showLeft).setOnClickListener(this);// ��ʾ��߿�ť
		mView.findViewById(R.id.showRight).setOnClickListener(this); // ��ʾ�ұ߿�ť

		investmentNewsListView = (RefreshListView) mView.findViewById(R.id.id_investment_news_listview);// Ͷ�ʿ�Ѷ�б�
		todayNewsListView = (RefreshListView) mView.findViewById(R.id.id_today_news_listview);// ���տ�Ѷ�б�
		focusListView = (RefreshListView) mView.findViewById(R.id.ssjj_news_listview);// ʱ�¾۽��б�
		meiTuNewsListView = (RefreshListView) mView.findViewById(R.id.id_meitu_news_listview);// ��ͼ�б�

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

		//Ͷ�ʿ�Ѷ��ͼ����
		View vBigInv = inflater.inflate(R.layout.inv_list_head_big, null);
		
		tvInvBitTitle = (TextView)vBigInv.findViewById(R.id.title);//Ͷ�ʿ�Ѷ��ͷ����ͼ����
		
		invIndexArr.clear();
		invIndexArr.add(vBigInv.findViewById(R.id.index_one));//Ͷ�ʿ�Ѷ��ͷ��ͼ�����±꣺1
		invIndexArr.add(vBigInv.findViewById(R.id.index_two));//Ͷ�ʿ�Ѷ��ͷ��ͼ�����±꣺2
		invIndexArr.add(vBigInv.findViewById(R.id.index_three));//Ͷ�ʿ�Ѷ��ͷ��ͼ�����±꣺3
		
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
				component.setBigNewsIndexIconSelected(invIndexArr, position, investmentData);// ����ѡ���±�
				if(investmentData.size()>position){//���ñ���
					tvInvBitTitle.setText(investmentData.get(position).getTitle());
				}
			}
			 
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		
		//ʱ�¾۽���ͼ����
       View vBigFocus = inflater.inflate(R.layout.inv_list_head_big, null);
       tvFocusBigTitle = (TextView)vBigFocus.findViewById(R.id.title);//��ͼ����
		
       focusIndexArr.clear();
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_one));//��ͼ�����±꣺1
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_two));//��ͼ�����±꣺2
       focusIndexArr.add(vBigFocus.findViewById(R.id.index_three));//��ͼ�����±꣺3
		
       vBigFocus.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.FILL_PARENT, height));
       
       focusViewPager = (IndexInvestmentViewPager)vBigFocus.findViewById(R.id.viewpager);
       focusViewPager.setAdapter(focusBigNewsAdapter);
       focusListView.addHeaderView(vBigFocus);
		
       focusViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				component.setBigNewsIndexIconSelected(focusIndexArr, position, focusData);// ����ѡ���±�
				if(focusData.size()>position){//���ñ���
					tvFocusBigTitle.setText(focusData.get(position).getTitle());
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
			@Override
			public void onPageScrollStateChanged(int state) {}
		});
		

		// �����б�adapter
		investmentNewsListView.setAdapter(investmentAdapter);
		todayNewsListView.setAdapter(todayAdapter);
		focusListView.setAdapter(focusAdapter);
		meiTuNewsListView.setAdapter(meituAdapter);

		// �����б����¼�
		investmentNewsListView.setOnItemClickListener(this);
		todayNewsListView.setOnItemClickListener(this);
		focusListView.setOnItemClickListener(this);
		meiTuNewsListView.setOnItemClickListener(this);

		// ����л���ť
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
		
		
		//�����л���ȣ���ҪΪ����ͼ�������ڵڶ�ҳ
		int switchWidth = screenWidth/3;//�л������
		System.out.println("�л������:"+switchWidth);
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
		
		focusListView.refreshVisible();// ˢ������
		
		return mView;
	}

	/* ����ˢ�� */
	@Override
	public void onRefresh() {
		
		int lastId = -1;

		System.out.println("����.....ˢ��");
		
		if(isViewVisible(investmentNewsListView) && !isLoadInv){// Ͷ�ʿ�Ѷ ����ˢ��
			isLoadInv = true;
			lastId = investmentAdapter.getLatestId(); //��ȡͶ�ʿ�Ѷ�б����һ������Id
			
			if(Utils.isNetWork(getActivity())){//����
				
				component.netLoadFreshPloy(lastId, dbInvestmentletters, investmentletters, 
										dataVisitors, this, WHAT_GET_INV_DATA, WHAT_GET_INV_REFRESH_DATA, 
										isLoadInvDefault, investmentData, viewPager, invIndexArr, 
										tvInvBitTitle, investmentAdapter, investmentBigNewsAdapter, 
										dbLastTime, DBHelper.TYPE_TOUZI);
				isLoadInvDefault = true;
				
			}else{//��������������
				if (lastId < 0) {//��ʾû�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbInvestmentletters, this, WHAT_GET_INV_DATA);
				} else {//����ˢ�� 
					dataVisitors.getFresh(dbInvestmentletters, lastId, this,WHAT_GET_INV_REFRESH_DATA);
				}				
			}
			
		}else if(isViewVisible(todayNewsListView) && !isLoadToday){ // ���տ�Ѷ ����ˢ��
			isLoadToday = true;
			lastId = todayAdapter.getLatestId();//��ȡ���տ�Ѷ �б����һ������Id 
			
			if(Utils.isNetWork(getActivity())){//����
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
				
			}else{//��������������
				if(todayBrandId < 0){
					if (lastId < 0) {//��ʾû�����ݣ�Ϊ���μ���
						dataVisitors.getListDefault(dbTodayNoBrand, this, WHAT_GET_TODAY_DATA);
					} else {//����ˢ�� 
						dataVisitors.getFresh(dbTodayNoBrand, lastId, this,WHAT_GET_TODAY_REFRESH_DATA);
					}
				}else{
					dbTodayByBrand.setType(todayBrandId);
					if (lastId < 0) {//��ʾû�����ݣ�Ϊ���μ���
						dataVisitors.getListDefault(dbTodayByBrand, this, WHAT_GET_TODAY_REFRESH_DATA);
					} else {//����ˢ�� 
						dataVisitors.getFresh(dbTodayByBrand, lastId, this,WHAT_GET_TODAY_REFRESH_DATA);
					}
				}
			}
			
		}else if(isViewVisible(meiTuNewsListView) && !isLoadImg){ // ��ͼ���� ����ˢ��
			isLoadImg = true;
			lastId = meituAdapter.getLatestId();//��ȡ��ͼ���� �б����һ������Id
			
			if(Utils.isNetWork(getActivity())){//����
				
				component.netLoadFreshPloy(lastId, dbImageOnLine, imageOnLine, 
						dataVisitors, this, WHAT_GET_MEITU_DATA, WHAT_GET_MEITU_REFRESH_DATA, 
						isLoadImgDefault, meiTuData, null, null, 
						null, meituAdapter, null, 
						dbLastTime, DBHelper.TYPE_MEITU);
				
				isLoadImgDefault = true;
				
			}else{//��������������
				if (lastId < 0) { //��ʾû�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbImageOnLine, this, WHAT_GET_MEITU_DATA);
				} else {//����ˢ��
					dataVisitors.getFresh(dbImageOnLine, lastId, this,WHAT_GET_MEITU_REFRESH_DATA);
				}
			}
		}else if(isViewVisible(focusListView) && !isLoadFocus){//ʱ�¾۽�����ˢ��
			isLoadFocus = true;
			lastId = focusAdapter.getLatestId();
			if(Utils.isNetWork(getActivity())){//����
				
				component.netLoadFreshPloy(lastId, dbFocusNews, focusNews, 
						dataVisitors, this, WHAT_GET_FOCUS_DATA, WHAT_GET_FOCUS_REFRESH_DATA, 
						isLoadFocusDefault, focusData, focusViewPager, focusIndexArr, 
						tvFocusBigTitle, focusAdapter, focusBigNewsAdapter, 
						dbLastTime, DBHelper.TYPE_FOCUS);
				isLoadFocusDefault= true;
				
			}else{//��������������
				if (lastId < 0) { //��ʾû�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbFocusNews, this, WHAT_GET_FOCUS_DATA);
				} else {//����ˢ��
					dataVisitors.getFresh(dbFocusNews, lastId, this,WHAT_GET_FOCUS_REFRESH_DATA);
				}
			}
		}
		
		
	}

	@Override
	public void onLoadMore() {
		int minId = -1;
		System.out.println("����.....����");
		if(isViewVisible(investmentNewsListView)){// Ͷ�ʿ�Ѷ �������ظ���
			isLoadInv = true;
			minId = investmentAdapter.getMinId();//��ȡͶ�ʿ�Ѷ�����б���С������Id
			
			if(Utils.isNetWork(getActivity())){//����
				dataVisitors.getMore(investmentletters, minId, this, WHAT_GET_INV_REFRESH_DATA);
				dataVisitors.updateList(investmentletters, dbInvestmentletters, dbLastTime, DBHelper.TYPE_TOUZI, null, 0);
			}else{//��������������
				dataVisitors.getMore(dbInvestmentletters, minId, this, WHAT_GET_INV_REFRESH_DATA);
			}
			
		}else if(isViewVisible(todayNewsListView)){ //// ���տ�Ѷ �������ظ���
			isLoadToday = true;
			minId = todayAdapter.getMinId();//��ȡ���տ�Ѷ�����б���С������Id
			
			if(Utils.isNetWork(getActivity())){//����
				if(todayBrandId < 0){
					dataVisitors.getMore(todayNoBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
					dataVisitors.updateList(todayNoBrand, dbTodayByBrand, dbLastTime, DBHelper.TYPE_JINRI, null, 0);
				}else{
					dataVisitors.getMore(todayByBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
					dataVisitors.updateList(todayByBrand, dbTodayNoBrand, dbLastTime, DBHelper.TYPE_JINRI, null, 0);
				}
				
			}else{//��������������
				dataVisitors.getMore(dbTodayNoBrand, minId, this, WHAT_GET_TODAY_REFRESH_DATA);
			}
			
		}else if(isViewVisible(focusListView) && !isLoadFocus){//ʱ�¾۽� �������ظ���
			isLoadFocus = true;
			minId = focusAdapter.getMinId();//��ȡʱ�¾۽������б���С������Id
			
			if(Utils.isNetWork(getActivity())){//����
				dataVisitors.getMore(focusNews, minId, this, WHAT_GET_FOCUS_REFRESH_DATA);
				dataVisitors.updateList(focusNews, dbFocusNews, dbLastTime, DBHelper.TYPE_FOCUS, null, 0);
			}else{//��������������
				dataVisitors.getMore(dbFocusNews, minId, this, WHAT_GET_FOCUS_REFRESH_DATA);
			}
		}
	}

	/**
	 * ɸѡ��ʾ���տ챨
	 * @param brandId ��Ŀid
	 */
	public void select(int id) {
		if(isLoadToday){//���ظ�����:���տ�Ѷ
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
			case R.id.showLeft: // ��ʾ��߿�
				((InterfaceActivity) getActivity()).showLeft();
				break;
	
			case R.id.showRight: // ��ʾ�ұ߿�
				((InterfaceActivity) getActivity()).showRight();
				break;
	
			case R.id.id_investment_news: // �л���Ͷ�ʿ�Ѷ
				
				component.setSwitchBrandSelected(typeSwitcherList, v);
	
				investmentNewsListView.setVisibility(View.VISIBLE);
				todayNewsListView.setVisibility(View.GONE);
				focusListView.setVisibility(View.GONE);
				meiTuNewsListView.setVisibility(View.GONE);
				
				if(investmentAdapter.getCount() < 1){
					investmentNewsListView.refreshVisible();
				}
				break;
	
			case R.id.id_today_news:// �л������տ�Ѷ
				
				if(todayBrandId > 0){//֮ǰ��ɸѡ���ģ��������ɸѡ
					todayData.clear();
					todayAdapter.notifyDataSetChanged();
					if(onCancelTodayBrandListener != null){//ȡ�����տ�Ѷ��Ŀ������,���������
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
				
			case R.id.ssjj_news://�л���ʱ�¾۽�
				component.setSwitchBrandSelected(typeSwitcherList, v);
				
				investmentNewsListView.setVisibility(View.GONE);
				todayNewsListView.setVisibility(View.GONE);
				focusListView.setVisibility(View.VISIBLE);
				meiTuNewsListView.setVisibility(View.GONE);
				
				if(focusAdapter.getCount() < 1){
					focusListView.refreshVisible();
				}
				break;
	
			case R.id.id_meitu_news:// �л�����ͼ����
				
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
		System.out.println("���ݷ��أ�"+context);

		List<?> data = (List<?>) res;

		switch (what) {
			case WHAT_GET_INV_DATA: // ��ȡͶ�ʿ�Ѷ��ʼ����20��
				isLoadInv = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//��ӵ����ݿ�
					investmentData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						investmentData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(investmentData);//����Ƚ��ر����д�ͼ�Լ������б��ã������������
				
				if(investmentData.size()>0){
					viewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(invIndexArr, 0, investmentData);// ����ѡ���±�
					tvInvBitTitle.setText(investmentData.get(0).getTitle());	
				}
				
				System.out.println("Ͷ�ʿ챨ˢ��");
				investmentBigNewsAdapter.notifyDataSetChanged();
				investmentAdapter.notifyDataSetChanged();
				investmentNewsListView.stopRefresh();
				investmentNewsListView.stopLoadMore();
				investmentNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_INV_REFRESH_DATA: //Ͷ�ʿ�Ѷ ����ˢ��/�������ظ���
				isLoadInv = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//��ӵ����ݿ�
					int size = data.size();
					for (int i = 0; i < size; i++) {
						investmentData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(investmentData);//����Ƚ��ر����д�ͼ�Լ������б��ã������������
				
				if(investmentData.size()>0){
					viewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(invIndexArr, 0, investmentData);// ����ѡ���±�
					tvInvBitTitle.setText(investmentData.get(0).getTitle());	
				}
				
				investmentBigNewsAdapter.notifyDataSetChanged();
				investmentAdapter.notifyDataSetChanged();
				investmentNewsListView.stopRefresh();
				investmentNewsListView.stopLoadMore();
				investmentNewsListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				break;
				
			case WHAT_GET_TODAY_DATA: // ��ȡ���տ��ǰ20������
				isLoadToday = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
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
				
			case WHAT_GET_TODAY_REFRESH_DATA: //���տ챨 ����ˢ��/�������ظ���
				isLoadToday = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
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
				
			case WHAT_GET_MEITU_DATA: // ��ȡ��ͼ����
				isLoadImg = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
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
				
			case WHAT_GET_MEITU_REFRESH_DATA: // ��ͼ���� ����ˢ��/�������ظ���
				isLoadImg = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
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
				
			case WHAT_GET_FOCUS_DATA:// ��ȡʱ�¾۽�ǰ20������
				System.out.println("FOCUS111111111111111111111");
				isLoadFocus = false;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbFocusNews, data, null, 0);//��ӵ����ݿ�
					focusData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						focusData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(focusData);//����Ƚ��ر����д�ͼ�Լ������б��ã������������
				
				if(focusData.size()>0){
					focusViewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(focusIndexArr, 0, focusData);// ����ѡ���±�
					tvFocusBigTitle.setText(focusData.get(0).getTitle());	
				}
				
				focusAdapter.notifyDataSetChanged();
				focusBigNewsAdapter.notifyDataSetChanged();
				focusListView.stopRefresh();
				focusListView.stopLoadMore();
				focusListView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
				
				break;
				
			case WHAT_GET_FOCUS_REFRESH_DATA://ʱ�¾۽� ����ˢ��/�������ظ���
				isLoadFocus = false;
				System.out.println("FOCUS2222222222222222222");
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();					
					}
				} else {
					dataVisitors.dbAdd(dbFocusNews, data, null, 0);//��ӵ����ݿ�
					int size = data.size();
					for (int i = 0; i < size; i++) {
						focusData.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(focusData);//����Ƚ��ر����д�ͼ�Լ������б��ã������������
				
				if(focusData.size()>0){
					focusViewPager.setCurrentItem(0, true);
					component.setBigNewsIndexIconSelected(focusIndexArr, 0, focusData);// ����ѡ���±�
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

	/** ����б��¼�,��ʾ��ϸ���� */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Activity activity = getActivity();
		Intent intent = new Intent(activity, NewsShowActivity.class);
		Intent imgIntent = new Intent(activity, ImageNewsDetail.class);

		MyApplication app = (MyApplication) activity.getApplication();

		
		if (position == 0) {// ����ͷ������
			return;
		}
		
		if(isViewVisible(investmentNewsListView)){// Ͷ�ʿ�Ѷ
			position++;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(investmentData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			
		}else if(isViewVisible(todayNewsListView)){ //// ���տ�Ѷ 
			
			position--;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(todayData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else if(isViewVisible(focusListView)){//ʱ�¾۽�
			position++;
			intent.putExtra("INDEX", position);
			app.setShowDetailDatas(focusData);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else if(isViewVisible(meiTuNewsListView)){ // ��ͼ����
			position--;
			imgIntent.putExtra("INDEX", meiTuData.get(position).getId());
			imgIntent.putExtra("TITLE", meiTuData.get(position).getTitle());
			app.setShowDetailDatas(meiTuData);
			startActivity(imgIntent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}
	}
	
	/**
	 * view�Ƿ�ɼ�
	 * @param view
	 * @return true:�ɼ� false:���ɼ�
	 */
	private boolean isViewVisible(View view){
		return view.getVisibility() == View.VISIBLE;
	}
	
	
	public void setOnCancelTodayBrandListener(OnCancelTodayBrandLisener onCancelTodayBrandListener){
		this.onCancelTodayBrandListener = onCancelTodayBrandListener;
	}
	
	/**ȡ�����տ�Ѷ��Ŀ������*/
	public interface OnCancelTodayBrandLisener{
		/**ȡ�����տ�Ѷ��Ŀѡ��*/
		public void onCancel();
	}

}
