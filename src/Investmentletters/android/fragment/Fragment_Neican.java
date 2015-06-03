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

	/** �汾�л����ְ�ť����˾�״� */
	private RelativeLayout radarLayoutButton = null;
	/** �汾�л����ְ�ť�����ݽ��� */
	private RelativeLayout decoderLayoutButton = null;
	/** �汾�л����ְ�ť�����̽�� */
	private RelativeLayout traderExplainLayoutButton = null;

	/** ��˾�״��б� ListView */
	private RefreshListView radarListView = null;
	/** ���ݽ����б� ListView */
	private RefreshListView decoderListView = null;
	/** ���̽���б� ListView */
	private RefreshListView traderExplainListView = null;

	/** ��˾�״����� */
	private List<News> radarData = null;
	/** ���ݽ������� */
	private List<News> decoderData = null;
	/** ���̽������ */
	private List<News> traderExplainData = null;

	/** ��˾�״�Adapter */
	private CommonNewsAdapter radarAdapter = null;
	/** ���ݽ���Adapter */
	private CommonNewsAdapter decoderAdapter = null;
	/** ���̽��Adapter */
	private CommonNewsAdapter traderExplainAdapter = null;

	/** ��˾�״�dao�� */
	private Radar radar = null;
	/** ��˾�״�DB dao�� */
	private DBRadar dbRadar = null;

	/** ���ݽ���dao�� */
	private Decoder decoder = null;
	/** ���ݽ���DB dao�� */
	private DBDecode dbDecoder = null;

	/** ���̽��dao�� */
	private TraderExplain traderExplain = null;
	/** ���̽��DB dao�� */
	private DBTraderExplain dbTraderExplain = null;

	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;

	/** ��˾�״� */
	private final int WHAT_GET_RADAR_DATA = 0;
	/** ���ݽ��� */
	private final int WHAT_GET_DECODER_DATA = 1;
	/** ���̽�� */
	private final int WHAT_GET_TRADER_EXPLAIN_DATA = 2;

	/** ��˾�״� ����/����ˢ�� */
	private final int WHAT_GET_RADAR_REFRESH_DATA = 3;
	/** ���ݽ��� ����/����ˢ�� */
	private final int WHAT_GET_DECODER_REFRESH_DATA = 4;
	/** ���̽�� ����/����ˢ�� */
	private final int WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA = 5;

	/** ���ظ�����:��˾�״� */
	private boolean isLoadRadar = false;
	/** ���ظ�����:���ݽ��� */
	private boolean isLoadDecode = false;
	/** ���ظ�����:���̽�� */
	private boolean isLoadTraderExp = false;
	
	/** �Ƿ��Ѽ���Ĭ��:��˾�״� */
	private boolean isLoadRadarDefault = false;
	/** �Ƿ��Ѽ���Ĭ��:���ݽ��� */
	private boolean isLoadDecodeDefault = false;
	/** �Ƿ��Ѽ���Ĭ��:���̽�� */
	private boolean isLoadTraderExpDefault = false;

	/** ����������ͼ�����ã��ڽ����л������л����������� */
	private View vContentView = null;
	/** ��ȡϵͳʱ�� */
	private SimpleDateFormat dateFormat = null;

	/** ��ȡ����б�һ��ˢ��ʱ�� */
	private DBLastTime dbLastTime = null;
	
	private KuaiBaoComponent component = null;

	public Fragment_Neican() {
		dataVisitors = new DataVisitors(); // ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ
		radar = new Radar(); // ��˾�״�dao��
		decoder = new Decoder(); // ���ݽ���dao��
		traderExplain = new TraderExplain(); // ���̽��dao��
		radarData = new ArrayList<News>(); // ��˾�״�����
		decoderData = new ArrayList<News>(); // ���ݽ�������
		traderExplainData = new ArrayList<News>(); // ���̽������

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

		radarAdapter = new CommonNewsAdapter(context, radarData); // ��˾�״�Adapter
		decoderAdapter = new CommonNewsAdapter(context, decoderData); // ���ݽ���adapter
		traderExplainAdapter = new CommonNewsAdapter(context, traderExplainData); // ���̽��

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
		; // ��ʾ�ұ߿�ť

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
		radarListView.refreshVisible();// ˢ������

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/* ����ˢ�� */
	@Override
	public void onRefresh() {

		int lastId = -1;

		if (isViewVisible(radarListView) && !isLoadRadar) { // ��˾�״�
			isLoadRadar = true;
			lastId = radarAdapter.getLatestId();// ��ȡ��˾�״��б����һ������Id

			if (Utils.isNetWork(getActivity())) {// ����
				
				component.netLoadFreshPloy(lastId, dbRadar, radar, 
						dataVisitors, this, WHAT_GET_RADAR_DATA, WHAT_GET_RADAR_REFRESH_DATA, 
						isLoadRadarDefault, radarData, null, null, 
						null, radarAdapter, null, 
						dbLastTime, DBHelper.TYPE_LEIDA);
				
				isLoadRadarDefault = true;
				
			} else {// ����
				if (lastId < 0) { // ��ʾû�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbRadar, this,
							WHAT_GET_RADAR_DATA);
				} else { // ����ˢ��
					dataVisitors.getFresh(dbRadar, lastId, this,
							WHAT_GET_RADAR_REFRESH_DATA);
				}
			}

		} else if (isViewVisible(decoderListView) && !isLoadDecode) {// ���ݽ���
			isLoadDecode = true;
			lastId = decoderAdapter.getLatestId();// ���ݽ����б����һ������Id

			if (Utils.isNetWork(getActivity())) {// ����
				
				component.netLoadFreshPloy(lastId, dbDecoder, decoder, 
						dataVisitors, this, WHAT_GET_DECODER_DATA, WHAT_GET_DECODER_REFRESH_DATA, 
						isLoadDecodeDefault, decoderData, null, null, 
						null, decoderAdapter, null, 
						dbLastTime, DBHelper.TYPE_JIEMA);
				
				isLoadDecodeDefault = true;
				
			} else {// ����
				if (lastId < 0) { // ��ʾû�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbDecoder, this,
							WHAT_GET_DECODER_DATA);
				} else { // ����ˢ��
					dataVisitors.getFresh(dbDecoder, lastId, this,
							WHAT_GET_DECODER_REFRESH_DATA);
				}
			}
		} else if (isViewVisible(traderExplainListView) && !isLoadTraderExp) {// ���̽��
			isLoadTraderExp = true;
			lastId = traderExplainAdapter.getLatestId(); // ��ȡ���̽���б����һ������Id

			if (Utils.isNetWork(getActivity())) {// ����
				
				component.netLoadFreshPloy(lastId, dbTraderExplain, traderExplain, 
						dataVisitors, this, WHAT_GET_TRADER_EXPLAIN_DATA, WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA, 
						isLoadTraderExpDefault, traderExplainData, null, null, 
						null, traderExplainAdapter, null, 
						dbLastTime, DBHelper.TYPE_CAOPAN);
				
				isLoadTraderExpDefault = true;
				
			} else {// ����
				if (lastId < 0) {// û�����ݣ�Ϊ���μ���
					dataVisitors.getListDefault(dbTraderExplain, this,
							WHAT_GET_TRADER_EXPLAIN_DATA);
				} else { // ����ˢ��
					dataVisitors.getFresh(dbTraderExplain, lastId, this,
							WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA);
				}
			}
		}
	}

	@Override
	public void onLoadMore() {
		int minId = -1;

		if (isViewVisible(radarListView) && !isLoadRadar) { // ��˾�״�
			isLoadRadar = true;
			minId = radarAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// ����
				dataVisitors.getMore(radar, minId, this,
						WHAT_GET_RADAR_REFRESH_DATA);
			} else {// ����
				dataVisitors.getMore(dbRadar, minId, this,
						WHAT_GET_RADAR_REFRESH_DATA);
			}
		} else if (isViewVisible(decoderListView) && !isLoadDecode) {// ���ݽ���
			isLoadDecode = true;
			minId = decoderAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// ����
				dataVisitors.getMore(decoder, minId, this,
						WHAT_GET_DECODER_REFRESH_DATA);
			} else {// ����
				dataVisitors.getMore(dbDecoder, minId, this,
						WHAT_GET_DECODER_REFRESH_DATA);
			}
		} else if (isViewVisible(traderExplainListView) && !isLoadTraderExp) {// ���̽��
			isLoadTraderExp = true;
			minId = traderExplainAdapter.getMinId();

			if (Utils.isNetWork(getActivity())) {// ����
				dataVisitors.getMore(traderExplain, minId, this,
						WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA);
			} else {// ����
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
		case WHAT_GET_RADAR_DATA: // ��˾�״� ��ʼ����20����
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case WHAT_GET_RADAR_REFRESH_DATA: // ��˾�״� ����/����ˢ��
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case WHAT_GET_DECODER_DATA: // ���ݽ��� ��ʼ����20����
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case WHAT_GET_DECODER_REFRESH_DATA: // ���ݽ��� ����/����ˢ��
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case WHAT_GET_TRADER_EXPLAIN_DATA: // ���̽����ʼ����20����
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case WHAT_GET_TRADER_EXPLAIN_REFRESH_DATA:// ���̽������/����ˢ��
			if (data == null) {
				if (context != null) {
					Toast.makeText(context, "�������ٲ�����ѽ", Toast.LENGTH_SHORT).show();
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

		case R.id.showRight: // ��ʾ�ұ߿�
			((InterfaceActivity) getActivity()).showRight();
			break;

		case R.id.id_lei_da_layout_btn: // �л�����˾�״�
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

		case R.id.id_jie_ma_layout_btn:// �л������ݽ���

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

		case R.id.id_can_pan_layout_btn:// �л������̽��
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

		if (position == 0) {// ����ͷ������
			return;
		}
		setPosition(position);
		position--;
		intent.putExtra("INDEX", position);
		if (isViewVisible(radarListView)) {// ��˾�״�
			setDetailDatas(radarData);
			app.setShowDetailDatas(radarData);
			
		} else if (isViewVisible(decoderListView)) {// ���ݽ���
			setDetailDatas(decoderData);
			app.setShowDetailDatas(decoderData);
		
		} else if (isViewVisible(traderExplainListView)) {// ���̽��
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
	 * view�Ƿ�ɼ�
	 * 
	 * @param view
	 * @return true:�ɼ� false:���ɼ�
	 */
	private boolean isViewVisible(View view) {
		return view.getVisibility() == View.VISIBLE;
	}

}
