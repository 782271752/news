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
 * 互动诊股
 * 
 * @author liang
 */
public class Fragment_Zhengu extends Fragment implements View.OnClickListener,
		DataVisitors.CallBack, OnItemClickListener {

	/** 股票列表DB dao */
	private DBShare dbShare = null;
	/** 股票原始数据 */
	private List<Share> originData = null;
	/** 热门股票数据 */
	private List<Share> hotData = null;
	/** 热门新闻数据 */
	private List<Hot_News> hotnewsData = null;
	/** 搜索框 */
	private EditText etSearch = null;

	/** 数据访问协调类 */
	private DataVisitors dataVisitors = null;

	/** what:获取数据库股票列表 */
	private final int WHAT_DB_GET_LIST = 1;
	/** what:获取热门股票数据列表 */
	private final int WHAT_GET_HOT_LIST = 2;
	/** what:获取热门新闻数据列表 */
	private final int WHAT_GET_HOTNEWS_LIST = 3;

	/** 关键词查询结果列表 */
	private ListView lvSearch = null;
	/** 关键词查询结果adapter */
	private ArrayAdapter<String> searchAdapter = null;
	/** 关键词查询结果adapter数集 */
	private List<String> searchData = null;

	/** 热门股票列表 */
	private ListView lvHot = null;

	/** 热门股票列表adapter */
	private ShareHotAdapter hotAdapter = null;
	/** 热门诊股列表 */
	private HotShare hotShare = null;

	/** 热门新闻列表adapter */
	private HotnewsAdapter hotnewsAdapter = null;
	/** 热门新闻列表 */
	private HotNews hotnews;

	/** 加载提示框 */
	private View vLoading = null;

	/** 界面视图 */
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
//			if (hotData.size() < 1 && vLoading.getVisibility() != View.VISIBLE) {// 没有数据，网络获取
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
		view.findViewById(R.id.showRight).setOnClickListener(this);// 显示右边

		lvSearch = (ListView) view.findViewById(R.id.search_share_list);
		searchAdapter = new ArrayAdapter<String>(context,
				R.layout.share_search_item, R.id.content, searchData);
		lvSearch.setAdapter(searchAdapter);
		lvSearch.setOnItemClickListener(this);

		lvHot = (ListView) view.findViewById(R.id.hot_share_list);

		// 加入头部大图
		View hotHeadView = inflater.inflate(R.layout.share_hot_head, null);
		// ScrollText scrollText =
		// (ScrollText)hotHeadView.findViewById(R.id.response);//免责声明滚动组件
		// scrollText.setText("本功能由越声理财提供，产品尚在测试阶段，内容仅供参考，详细请留意免责声明！");
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
					System.out.println("获取到控件焦点");
					lvHot.setVisibility(View.VISIBLE);
					lvSearch.setVisibility(View.GONE);
					if (hotnewsData.size() < 1
							&& vLoading.getVisibility() != View.VISIBLE) {// 没有数据，网络获取
						dataVisitors.doGet(hotnews, Fragment_Zhengu.this, WHAT_GET_HOTNEWS_LIST);
//						dataVisitors.queryNews(hotnews,
//								 Fragment_Zhengu.this, WHAT_GET_HOTNEWS_LIST);
						System.out.println("search 读取json");
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
				// if (reg.equals("")) {// 为空，显示默认列表
				// lvHot.setVisibility(View.VISIBLE);
				// lvSearch.setVisibility(View.GONE);
				//
				// if (hotData.size() < 1
				// && vLoading.getVisibility() != View.VISIBLE) {// 没有数据，网络获取
				// dataVisitors.queryHotShare(hotShare,
				// Fragment_Zhengu.this, WHAT_GET_HOT_LIST);
				// vLoading.setVisibility(View.VISIBLE);
				// }
				// } else {// 不为空，查询
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
		case WHAT_DB_GET_LIST: // 获取数据库股票列表
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

		case WHAT_GET_HOT_LIST: // 获取热门股票数据列表
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
			System.out.println("search 回调成功");
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
		if (lvHot.getVisibility() == View.VISIBLE) {// 点击热门
			if (position < 1) {// position=0为头部
				return;
			}

			intent.putExtra("NUMBER", hotData.get(position - 1).getNumber());
			startActivity(intent);
		} else {// 点击搜索列表
			intent.putExtra("NUMBER", originData.get(position).getNumber());
			startActivity(intent);
		}

	}

}
