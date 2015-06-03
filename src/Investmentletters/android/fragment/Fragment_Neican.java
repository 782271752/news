package Investmentletters.android.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.NewsShowActivity;
import Investmentletters.android.adapter.CommonNewsAdapter;
import Investmentletters.android.component.KuaiBaoComponent;
import Investmentletters.android.dao.DBDecode;
import Investmentletters.android.dao.DBLastTime;
import Investmentletters.android.dao.DBRadar;
import Investmentletters.android.dao.DBTraderExplain;
import Investmentletters.android.dao.Decoder;
import Investmentletters.android.dao.Radar;
import Investmentletters.android.dao.TraderExplain;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.DBHelper;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.RefreshListView;
import Investmentletters.android.view.RefreshListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_Neican extends Fragment implements IXListViewListener,
		View.OnClickListener, AdapterView.OnItemClickListener,
		DataVisitors.CallBack {

	/** 版本切换布局按钮：公司雷达 */
	private RelativeLayout radarLayoutButton = null;
	/** 版本切换布局按钮：数据解码 */
	private RelativeLayout decoderLayoutButton = null;
	/** 版本切换布局按钮：操盘解读 */
	private RelativeLayout traderExplainLayoutButton = null;

	/** 公司雷达列表 ListView */
	private RefreshListView radarListView = null;
	/** 数据解码列表 ListView */
	private RefreshListView decoderListView = null;
	/** 操盘解读列表 ListView */
	private RefreshListView traderExplainListView = null;

	/** 公司雷达数据 */
	private List<News> radarData = null;
	/** 数据解码数据 */
	private List<News> decoderData = null;
	/** 操盘解读数据 */
	private List<News> traderExplainData = null;

	/** 公司雷达Adapter */
	private CommonNewsAdapter radarAdapter = null;
	/** 数据解码Adapter */
	private CommonNewsAdapter decoderAdapter = null;
	/** 操盘解读Adapter */
	private CommonNewsAdapter traderExplainAdapter = null;

	/** 公司雷达dao类 */
	private Radar radar = null;
	/** 公司雷达DB dao类 */
	private DBRadar dbRadar = null;

	/** 数据解码dao类 */
	private Decoder decoder = null;
	/** 数据解码DB dao类 */
	private DBDecode dbDecoder = null;

	/** 操盘解读dao类 */
	private TraderExplain traderExplain = null;
	/** 操盘解读DB dao类 */
	private DBTraderExplain dbTraderExplain = null;

	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;

	/** 公司雷达 */
	private final int WHAT_GET_RADAR_DATA = 0;
	/** 数据解码 */
	private final int WHAT_GET_DECODER_DATA = 1;
	/** 操盘解读 */
	private final int WHAT_GET_TRADER_EXPLAIN_DATA = 2;

	/** 公司雷达 下拉/下拉刷新 */
	private final int WHAT_GET_RADAR_REFRESH_DATA = 3;
	/** 数据解码 下拉/上拉刷新 */
	private final int WHAT_GET_DECODER_REFRESH_DATA = 4;
	/** 操盘解读 下拉/上拉刷新 */
	private final int WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA = 5;

	/** 防重复加载:公司雷达 */
	private boolean isLoadRadar = false;
	/** 防重复加载:数据解码 */
	private boolean isLoadDecode = false;
	/** 防重复加载:操盘解读 */
	private boolean isLoadTraderExp = false;
	
	/** 是否已加载默认:公司雷达 */
	private boolean isLoadRadarDefault = false;
	/** 是否已加载默认:数据解码 */
	private boolean isLoadDecodeDefault = false;
	/** 是否已加载默认:操盘解读 */
	private boolean isLoadTraderExpDefault = false;

	/** 整个界面视图。作用：在界面切换后再切换回来，不变 */
	private View vContentView = null;
	/** 获取系统时间 */
	private SimpleDateFormat dateFormat = null;

	/** 获取最后列表一次刷新时间 */
	private DBLastTime dbLastTime = null;
	
	private KuaiBaoComponent component = null;

	public Fragment_Neican() {
		dataVisitors = new DataVisitors(); // 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系
		radar = new Radar(); // 公司雷达dao类
		decoder = new Decoder(); // 数据解码dao类
		traderExplain = new TraderExplain(); // 操盘解读dao类
		radarData = new ArrayList<News>(); // 公司雷达数据
		decoderData = new ArrayList<News>(); // 数据解码数据
		traderExplainData = new ArrayList<News>(); // 操盘解读数据

		dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.UK);
		
		component = new KuaiBaoComponent();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getActivity();

		dbRadar = new DBRadar(context);
		dbDecoder = new DBDecode(context);
		dbTraderExplain = new DBTraderExplain(context);

		radarAdapter = new CommonNewsAdapter(context, radarData); // 公司雷达Adapter
		decoderAdapter = new CommonNewsAdapter(context, decoderData); // 数据解码adapter
		traderExplainAdapter = new CommonNewsAdapter(context, traderExplainData); // 操盘解读

		dbLastTime = new DBLastTime(context);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (vContentView != null) {
			ViewGroup vg = (ViewGroup) vContentView.getParent();
			vg.removeView(vContentView);
			return vContentView;
		}

		View view = inflater.inflate(R.layout.list_touzineican, null);
		vContentView = view;

		view.findViewById(R.id.showRight).setOnClickListener(this);
		; // 显示右边框按钮

		radarLayoutButton = (RelativeLayout) view
				.findViewById(R.id.id_lei_da_layout_btn);
		radarLayoutButton.setBackgroundResource(R.drawable.bg_radiobtn_press);
		decoderLayoutButton = (RelativeLayout) view
				.findViewById(R.id.id_jie_ma_layout_btn);
		traderExplainLayoutButton = (RelativeLayout) view
				.findViewById(R.id.id_can_pan_layout_btn);

		radarLayoutButton.setOnClickListener(this);
		decoderLayoutButton.setOnClickListener(this);
		traderExplainLayoutButton.setOnClickListener(this);

		radarListView = (RefreshListView) view
				.findViewById(R.id.id_lei_da_listview);
		decoderListView = (RefreshListView) view
				.findViewById(R.id.id_jie_ma_listview);
		traderExplainListView = (RefreshListView) view
				.findViewById(R.id.id_can_pan_listview);

		radarListView.setPullLoadEnable(true);
		radarListView.setXListViewListener(this);
		radarListView.setPullRefreshEnable(true);

		decoderListView.setPullLoadEnable(true);
		decoderListView.setXListViewListener(this);
		decoderListView.setPullRefreshEnable(true);

		traderExplainListView.setPullLoadEnable(true);
		traderExplainListView.setXListViewListener(this);
		traderExplainListView.setPullRefreshEnable(true);

		radarListView.setAdapter(radarAdapter);
		decoderListView.setAdapter(decoderAdapter);
		traderExplainListView.setAdapter(traderExplainAdapter);

		radarListView.setOnItemClickListener(this);
		decoderListView.setOnItemClickListener(this);
		traderExplainListView.setOnItemClickListener(this);

		radarListView.setVisibility(View.VISIBLE);
		radarListView.refreshVisible();// 刷新数据

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/* 下拉刷新 */
	@Override
	public void onRefresh() {

		int lastId = -1;

		if (isViewVisible(radarListView) && !isLoadRadar) { // 公司雷达
			isLoadRadar = true;
			lastId = radarAdapter.getLatestId();// 获取公司雷达列表最后一条数据Id

			if (Utils.isNetWork(getActivity())) {// 有网
				
				component.netLoadFreshPloy(lastId, dbRadar, radar, 
						dataVisitors, this, WHAT_GET_RADAR_DATA, WHAT_GET_RADAR_REFRESH_DATA, 
						isLoadRadarDefault, radarData, null, null, 
						null, radarAdapter, null, 
						dbLastTime, DBHelper.TYPE_LEIDA);
				
				isLoadRadarDefault = true;
				
			} else {// 无网
				if (lastId < 0) { // 表示没有数据，为初次加载
					dataVisitors.getListDefault(dbRadar, this,
							WHAT_GET_RADAR_DATA);
				} else { // 下拉刷新
					dataVisitors.getFresh(dbRadar, lastId, this,
							WHAT_GET_RADAR_REFRESH_DATA);
				}
			}

		} else if (isViewVisible(decoderListView) && !isLoadDecode) {// 数据解码
			isLoadDecode = true;
			lastId = decoderAdapter.getLatestId();// 数据解码列表最后一条数据Id

			if (Utils.isNetWork(getActivity())) {// 有网
				
				component.netLoadFreshPloy(lastId, dbDecoder, decoder, 
						dataVisitors, this, WHAT_GET_DECODER_DATA, WHAT_GET_DECODER_REFRESH_DATA, 
						isLoadDecodeDefault, decoderData, null, null, 
						null, decoderAdapter, null, 
						dbLastTime, DBHelper.TYPE_JIEMA);
				
				isLoadDecodeDefault = true;
				
			} else {// 无网
				if (lastId < 0) { // 表示没有数据，为初次加载
					dataVisitors.getListDefault(dbDecoder, this,
							WHAT_GET_DECODER_DATA);
				} else { // 下拉刷新
					dataVisitors.getFresh(dbDecoder, lastId, this,
							WHAT_GET_DECODER_REFRESH_DATA);
				}
			}
		} else if (isViewVisible(traderExplainListView) && !isLoadTraderExp) {// 操盘解读
			isLoadTraderExp = true;
			lastId = traderExplainAdapter.getLatestId(); // 获取操盘解读列表最后一条数据Id

			if (Utils.isNetWork(getActivity())) {// 有网
				
				component.netLoadFreshPloy(lastId, dbTraderExplain, traderExplain, 
						dataVisitors, this, WHAT_GET_TRADER_EXPLAIN_DATA, WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA, 
						isLoadTraderExpDefault, traderExplainData, null, null, 
						null, traderExplainAdapter, null, 
						dbLastTime, DBHelper.TYPE_CAOPAN);
				
				isLoadTraderExpDefault = true;
				
			} else {// 无网
				if (lastId < 0) {// 没有数据，为初次加载
					dataVisitors.getListDefault(dbTraderExplain, this,
							WHAT_GET_TRADER_EXPLAIN_DATA);
				} else { // 下拉刷新
					dataVisitors.getFresh(dbTraderExplain, lastId, this,
							WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA);
				}
			}
		}
	}

	@Override
	public void onLoadMore() {
		int minId = -1;

		if (isViewVisible(radarListView) && !isLoadRadar) { // 公司雷达
			isLoadRadar = true;
			minId = radarAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// 有网
				dataVisitors.getMore(radar, minId, this,
						WHAT_GET_RADAR_REFRESH_DATA);
			} else {// 无网
				dataVisitors.getMore(dbRadar, minId, this,
						WHAT_GET_RADAR_REFRESH_DATA);
			}
		} else if (isViewVisible(decoderListView) && !isLoadDecode) {// 数据解码
			isLoadDecode = true;
			minId = decoderAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// 有网
				dataVisitors.getMore(decoder, minId, this,
						WHAT_GET_DECODER_REFRESH_DATA);
			} else {// 无网
				dataVisitors.getMore(dbDecoder, minId, this,
						WHAT_GET_DECODER_REFRESH_DATA);
			}
		} else if (isViewVisible(traderExplainListView) && !isLoadTraderExp) {// 操盘解读
			isLoadTraderExp = true;
			minId = traderExplainAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// 有网
				dataVisitors.getMore(traderExplain, minId, this,
						WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA);
			} else {// 无网
				dataVisitors.getMore(dbTraderExplain, minId, this,
						WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA);
			}
		}
	}

	@Override
	public void onResult(int what, Object res) {

		Context context = getActivity();

		List<?> data = (List<?>) res;

		switch (what) {
		case WHAT_GET_RADAR_DATA: // 公司雷达 初始加载20条的
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbRadar, data, null, 0);

				radarData.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					radarData.add((News) data.get(i));
				}
			}
			radarAdapter.notifyDataSetChanged();
			radarListView.stopRefresh();
			radarListView.stopLoadMore();
			radarListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));
			isLoadRadar = false;
			break;

		case WHAT_GET_RADAR_REFRESH_DATA: // 公司雷达 下拉/上拉刷新
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbRadar, data, null, 0);

				int size = data.size();
				for (int i = 0; i < size; i++) {
					radarData.add((News) data.get(i));
				}
			}
			radarAdapter.notifyDataSetChanged();
			radarListView.stopRefresh();
			radarListView.stopLoadMore();
			radarListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));
			isLoadRadar = false;
			break;

		case WHAT_GET_DECODER_DATA: // 数据解码 初始加载20条的
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbDecoder, data, null, 0);

				decoderData.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					decoderData.add((News) data.get(i));
				}
			}
			decoderAdapter.notifyDataSetChanged();
			decoderListView.stopRefresh();
			decoderListView.stopLoadMore();
			decoderListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));
			isLoadDecode = false;
			break;

		case WHAT_GET_DECODER_REFRESH_DATA: // 数据解码 下拉/上拉刷新
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbDecoder, data, null, 0);

				int size = data.size();
				for (int i = 0; i < size; i++) {
					decoderData.add((News) data.get(i));
				}
			}
			decoderAdapter.notifyDataSetChanged();
			decoderListView.stopRefresh();
			decoderListView.stopLoadMore();
			decoderListView.setRefreshTime(dateFormat.format(new Date(System
					.currentTimeMillis())));
			isLoadDecode = false;
			break;

		case WHAT_GET_TRADER_EXPLAIN_DATA: // 操盘解读初始加载20条的
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbTraderExplain, data, null, 0);

				traderExplainData.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					traderExplainData.add((News) data.get(i));
				}
			}
			traderExplainAdapter.notifyDataSetChanged();
			traderExplainListView.stopRefresh();
			traderExplainListView.stopLoadMore();
			traderExplainListView.setRefreshTime(dateFormat.format(new Date(
					System.currentTimeMillis())));
			isLoadTraderExp = false;
			break;

		case WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA:// 操盘解读下拉/上拉刷新
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
				}
			} else {

				dataVisitors.dbAdd(dbTraderExplain, data, null, 0);

				int size = data.size();
				for (int i = 0; i < size; i++) {
					traderExplainData.add((News) data.get(i));
				}
			}
			traderExplainAdapter.notifyDataSetChanged();
			traderExplainListView.stopRefresh();
			traderExplainListView.stopLoadMore();
			traderExplainListView.setRefreshTime(dateFormat.format(new Date(
					System.currentTimeMillis())));
			isLoadTraderExp = false;
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.showRight: // 显示右边框
			((InterfaceActivity) getActivity()).showRight();
			break;

		case R.id.id_lei_da_layout_btn: // 切换到公司雷达
			radarLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_press);
			decoderLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			traderExplainLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);

			radarListView.setVisibility(View.VISIBLE);
			decoderListView.setVisibility(View.GONE);
			traderExplainListView.setVisibility(View.GONE);

			if (radarAdapter.getCount() < 1) {
				radarListView.refreshVisible();
			}

			break;

		case R.id.id_jie_ma_layout_btn:// 切换到数据解码

			radarLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			decoderLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_press);
			traderExplainLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);

			radarListView.setVisibility(View.GONE);
			decoderListView.setVisibility(View.VISIBLE);
			traderExplainListView.setVisibility(View.GONE);

			if (decoderAdapter.getCount() < 1) {
				decoderListView.refreshVisible();
			}

			break;

		case R.id.id_can_pan_layout_btn:// 切换到操盘解读
			radarLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			decoderLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			traderExplainLayoutButton
					.setBackgroundResource(R.drawable.bg_radiobtn_press);

			radarListView.setVisibility(View.GONE);
			decoderListView.setVisibility(View.GONE);
			traderExplainListView.setVisibility(View.VISIBLE);

			if (traderExplainAdapter.getCount() < 1) {
				traderExplainListView.refreshVisible();
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Position_Data = position;
		Activity activity = getActivity();
		Intent intent = new Intent(activity, NewsShowActivity.class);
		MyApplication app = (MyApplication) activity.getApplication();

		if (position == 0) {// 点中头部更多
			return;
		}
		setPosition(position);
		position--;
		intent.putExtra("INDEX", position);
		if (isViewVisible(radarListView)) {// 公司雷达
			setDetailDatas(radarData);
			app.setShowDetailDatas(radarData);
			
		} else if (isViewVisible(decoderListView)) {// 数据解码
			setDetailDatas(decoderData);
			app.setShowDetailDatas(decoderData);
		
		} else if (isViewVisible(traderExplainListView)) {// 操盘解读
			setDetailDatas(traderExplainData);
			app.setShowDetailDatas(traderExplainData);
		}
		if (Utils.isLogin() == false) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment_Login Login = new Fragment_Login();
			ft.replace(R.id.id_framelayout, Login);
			ft.commit();
			return;
		}
		startActivity(intent);

	}

	private static List<News> DetailData = null;

	public void setDetailDatas(List<News> data) {
		DetailData = data;
	}

	public static List<News> getDetailDatas() {
		return DetailData;
	}

	private static int Position_Data;

	public void setPosition(int Position) {
		Position_Data = Position;
	}

	public static int getPosition() {
		return Position_Data;
	}

	/**
	 * view是否可见
	 * 
	 * @param view
	 * @return true:可见 false:不可见
	 */
	private boolean isViewVisible(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

}
