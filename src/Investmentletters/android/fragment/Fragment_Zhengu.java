package Investmentletters.android.fragment;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.ShareDetail;
import Investmentletters.android.adapter.HotnewsAdapter;
import Investmentletters.android.adapter.ShareHotAdapter;
import Investmentletters.android.dao.DBShare;
import Investmentletters.android.dao.HotNews;
import Investmentletters.android.dao.HotShare;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.Hot_News;
import Investmentletters.android.entity.Share;
import Investmentletters.android.view.ScrollText;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * �������
 * 
 * @author liang
 */
public class Fragment_Zhengu extends Fragment implements View.OnClickListener,
		DataVisitors.CallBack, OnItemClickListener {

	/** ��Ʊ�б�DB dao */
	private DBShare dbShare = null;
	/** ��Ʊԭʼ���� */
	private List<Share> originData = null;
	/** ���Ź�Ʊ���� */
	private List<Share> hotData = null;
	/** ������������ */
	private List<Hot_News> hotnewsData = null;
	/** ������ */
	private EditText etSearch = null;

	/** ���ݷ���Э���� */
	private DataVisitors dataVisitors = null;

	/** what:��ȡ���ݿ��Ʊ�б� */
	private final int WHAT_DB_GET_LIST = 1;
	/** what:��ȡ���Ź�Ʊ�����б� */
	private final int WHAT_GET_HOT_LIST = 2;
	/** what:��ȡ�������������б� */
	private final int WHAT_GET_HOTNEWS_LIST = 3;

	/** �ؼ��ʲ�ѯ����б� */
	private ListView lvSearch = null;
	/** �ؼ��ʲ�ѯ���adapter */
	private ArrayAdapter<String> searchAdapter = null;
	/** �ؼ��ʲ�ѯ���adapter���� */
	private List<String> searchData = null;

	/** ���Ź�Ʊ�б� */
	private ListView lvHot = null;

	/** ���Ź�Ʊ�б�adapter */
	private ShareHotAdapter hotAdapter = null;
	/** ��������б� */
	private HotShare hotShare = null;

	/** ���������б�adapter */
	private HotnewsAdapter hotnewsAdapter = null;
	/** ���������б� */
	private HotNews hotnews;

	/** ������ʾ�� */
	private View vLoading = null;

	/** ������ͼ */
	private View contentView = null;

	public Fragment_Zhengu() {
		dbShare = new DBShare(getActivity());
		originData = new ArrayList<Share>();

		dataVisitors = new DataVisitors();
		searchData = new ArrayList<String>();
		hotData = new ArrayList<Share>();
		hotShare = new HotShare();

		hotnewsData = new ArrayList<Hot_News>();
		hotnews = new HotNews();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (contentView != null) {
//			ViewGroup vg = (ViewGroup) contentView.getParent();
//			vg.removeView(contentView);
//			if (hotData.size() < 1 && vLoading.getVisibility() != View.VISIBLE) {// û�����ݣ������ȡ
//				dataVisitors.queryHotShare(hotShare, Fragment_Zhengu.this,
//						WHAT_GET_HOT_LIST);
//				vLoading.setVisibility(View.VISIBLE);
//			}
//			return contentView;
//		}

		Context context = getActivity();
		View view = inflater.inflate(R.layout.list_hudongzhengu, container,
				false);
		contentView = view;
		view.findViewById(R.id.showRight).setOnClickListener(this);// ��ʾ�ұ�

		lvSearch = (ListView) view.findViewById(R.id.search_share_list);
		searchAdapter = new ArrayAdapter<String>(context,
				R.layout.share_search_item, R.id.content, searchData);
		lvSearch.setAdapter(searchAdapter);
		lvSearch.setOnItemClickListener(this);

		lvHot = (ListView) view.findViewById(R.id.hot_share_list);

		// ����ͷ����ͼ
		View hotHeadView = inflater.inflate(R.layout.share_hot_head, null);
		// ScrollText scrollText =
		// (ScrollText)hotHeadView.findViewById(R.id.response);//���������������
		// scrollText.setText("��������Խ������ṩ����Ʒ���ڲ��Խ׶Σ����ݽ����ο�����ϸ����������������");
		// scrollText.setTextSize(14.0F);
		lvHot.addHeaderView(hotHeadView);

		vLoading = hotHeadView.findViewById(R.id.loading);

		// hotAdapter = new ShareHotAdapter(context, hotData);
		hotnewsAdapter = new HotnewsAdapter(context, hotnewsData);
		lvHot.setAdapter(hotAdapter);
		lvHot.setOnItemClickListener(this);

		etSearch = (EditText) view.findViewById(R.id.search);
		etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					System.out.println("��ȡ���ؼ�����");
					lvHot.setVisibility(View.VISIBLE);
					lvSearch.setVisibility(View.GONE);
					if (hotnewsData.size() < 1
							&& vLoading.getVisibility() != View.VISIBLE) {// û�����ݣ������ȡ
						dataVisitors.doGet(hotnews, Fragment_Zhengu.this, WHAT_GET_HOTNEWS_LIST);
//						dataVisitors.queryNews(hotnews,
//								 Fragment_Zhengu.this, WHAT_GET_HOTNEWS_LIST);
						System.out.println("search ��ȡjson");
						vLoading.setVisibility(View.VISIBLE);
					}

				}
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String reg = s.toString().trim();
				// if (reg.equals("")) {// Ϊ�գ���ʾĬ���б�
				// lvHot.setVisibility(View.VISIBLE);
				// lvSearch.setVisibility(View.GONE);
				//
				// if (hotData.size() < 1
				// && vLoading.getVisibility() != View.VISIBLE) {// û�����ݣ������ȡ
				// dataVisitors.queryHotShare(hotShare,
				// Fragment_Zhengu.this, WHAT_GET_HOT_LIST);
				// vLoading.setVisibility(View.VISIBLE);
				// }
				// } else {// ��Ϊ�գ���ѯ
				// lvHot.setVisibility(View.GONE);
				// lvSearch.setVisibility(View.VISIBLE);
				// dataVisitors.queryShare(dbShare, s.toString().trim(),
				// Fragment_Zhengu.this, WHAT_DB_GET_LIST);
				// }
			}
		});

		// dataVisitors.queryHotShare(hotShare, this, WHAT_GET_HOT_LIST);
		// vLoading.setVisibility(View.VISIBLE);

		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.showRight:
			((InterfaceActivity) getActivity()).showRight();
			break;

		}
	}

	@Override
	public synchronized void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		switch (what) {
		case WHAT_DB_GET_LIST: // ��ȡ���ݿ��Ʊ�б�
			if (res == null) {
				return;
			}

			List<?> tempData = (List<?>) res;
			Share sItem = null;
			originData.clear();
			searchData.clear();
			for (Object obj : tempData) {
				sItem = (Share) obj;
				originData.add(sItem);

				searchData.add(sItem.getNumber() + "  " + sItem.getName());
			}

			searchAdapter.notifyDataSetChanged();

			break;

		case WHAT_GET_HOT_LIST: // ��ȡ���Ź�Ʊ�����б�
			vLoading.setVisibility(View.GONE);
			if (res == null) {
				return;
			}

			List<?> tempHotData = (List<?>) res;
			hotData.clear();
			for (Object obj : tempHotData) {
				hotData.add((Share) obj);
			}

			hotAdapter.notifyDataSetChanged();

			break;
		case WHAT_GET_HOTNEWS_LIST:
			System.out.println("search res = " + res);
			System.out.println("search �ص��ɹ�");
			 vLoading.setVisibility(View.GONE);
			 if (res == null) {
			 return;
			 }
			
			 List<?> tempHotNewsData = (List<?>) res;
			 int size = tempHotNewsData.size();
			 System.out.println("search:"+size);
			 hotnewsData.clear();
			 for (int i= 0;i<size;i++) {
				 hotnewsData.add((Hot_News) tempHotNewsData.get(i));
			 }
			
			 hotnewsAdapter.notifyDataSetChanged();
			break;

		default:

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), ShareDetail.class);
		if (lvHot.getVisibility() == View.VISIBLE) {// �������
			if (position < 1) {// position=0Ϊͷ��
				return;
			}

			intent.putExtra("NUMBER", hotData.get(position - 1).getNumber());
			startActivity(intent);
		} else {// ��������б�
			intent.putExtra("NUMBER", originData.get(position).getNumber());
			startActivity(intent);
		}

	}

}
