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
 * �����
 * @author Administrator
 */
public class LeftFragment extends Fragment implements OnItemClickListener,DataVisitors.CallBack,OnCancelTodayBrandLisener{
	public static int smallpic_id;
	/**���տ�Ѷ��Ŀ����*/
	private List<BaseEntity> brandData = null;
	/** ���տ�Ѷ��Ŀ�б� */
	private ListView brandListView = null;
	/** ���տ�Ѷ��Ŀ�б�adapter */
	private TodayBrandListAdapter todayAdapter = null;
	/**�Ƿ����ڼ��ؽ��տ�Ѷ��Ŀ*/
	private boolean isLoadTodayBrand = false;

	/** ��ȡ�����տ�Ѷ��Ŀ�б����� */
	private final int GET_BRAND_DATA = 0;

	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;
	/** ���տ�Ѷ��Ŀdao */
	private TodayBrand todayBrand = null;
	/** ���տ챨Fragment */
	private Fragment_Kuaibao Kuaibao = null;
	
	/**
	 * ���ý��տ챨Fragment
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

		// ��ȡ��Ŀ��
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
			case GET_BRAND_DATA: // ��ȡ�����տ�Ѷ��Ŀ�б�����
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
		
		todayAdapter.setSelected(position);//����ѡ�е��±�

		// �л������տ챨��
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
	public void onCancel() {//ȡ�����տ�Ѷ��Ŀѡ��
		// TODO Auto-generated method stub
		todayAdapter.setSelected(-1);
	}

}
