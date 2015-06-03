package Investmentletters.android.fragment;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.adapter.TodayBrandListAdapter;
import Investmentletters.android.dao.TodayBrand;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.BaseEntity;
import Investmentletters.android.fragment.Fragment_Kuaibao.OnCancelTodayBrandLisener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 左则框
 * @author Administrator
 */
public class LeftFragment extends Fragment implements OnItemClickListener,DataVisitors.CallBack,OnCancelTodayBrandLisener{
	public static int smallpic_id;
	/**今日快讯栏目数据*/
	private List<BaseEntity> brandData = null;
	/** 今日快讯栏目列表 */
	private ListView brandListView = null;
	/** 今日快讯栏目列表adapter */
	private TodayBrandListAdapter todayAdapter = null;
	/**是否正在加载今日快讯栏目*/
	private boolean isLoadTodayBrand = false;

	/** 获取到今日快讯栏目列表数据 */
	private final int GET_BRAND_DATA = 0;

	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;
	/** 今日快讯栏目dao */
	private TodayBrand todayBrand = null;
	/** 今日快报Fragment */
	private Fragment_Kuaibao Kuaibao = null;
	
	/**
	 * 设置今日快报Fragment
	 * @param Kuaibao
	 */
	public void setKuaiBaoFragment(Fragment_Kuaibao Kuaibao){
		this.Kuaibao = Kuaibao;
		Kuaibao.setOnCancelTodayBrandListener(this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		brandData = new ArrayList<BaseEntity>();
		todayAdapter = new TodayBrandListAdapter(getActivity(), brandData);

		dataVisitors = new DataVisitors();
		todayBrand = new TodayBrand();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);
		
		brandListView = (ListView) view.findViewById(R.id.id_today_news_program_name_listview);
		brandListView.setAdapter(todayAdapter);

		// 获取栏目名
		brandListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onResult(int what, Object res) {
		if(res==null){
			return;
		}
		List<?> data = (List<?>) res;
		
		switch (what) {
			case GET_BRAND_DATA: // 获取到今日快讯栏目列表数据
				if (data != null) {
					brandData.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						brandData.add((BaseEntity) data.get(i));
					}
					todayAdapter.notifyDataSetChanged();
				}
				isLoadTodayBrand = false;
				break;
	
			default:
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		todayAdapter.setSelected(position);//设置选中的下标

		// 切换到今日快报框
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.id_framelayout, Kuaibao);
		ft.commit();
		BaseEntity item = brandData.get(position);
		Kuaibao.select(item.getId());
		((InterfaceActivity)getActivity()).showLeft();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(brandData.size()<1 && !isLoadTodayBrand){
			isLoadTodayBrand = true;
			dataVisitors.doGet(todayBrand, this, GET_BRAND_DATA);
		}
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCancel() {//取消今日快讯栏目选中
		// TODO Auto-generated method stub
		todayAdapter.setSelected(-1);
	}

}
