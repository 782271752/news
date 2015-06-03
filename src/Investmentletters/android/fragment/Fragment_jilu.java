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
import Investmentletters.android.dao.DBPushMsg;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.view.RefreshListView;
import Investmentletters.android.view.RefreshListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * 消息记录列表
 * @author Administrator
 */
public class Fragment_jilu extends Fragment implements OnClickListener,IXListViewListener,DataVisitors.CallBack,OnItemClickListener{
	/**推送列表listview*/
	private RefreshListView listView = null;
	private CommonNewsAdapter adapter = null;
	/**新闻数据*/
	private List<News> data = null;
	/**日期格式*/
	private static SimpleDateFormat dateFormat = null;
	/**是否正在加载数据*/
	private boolean isLoading = false;
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	/**消息推送新闻列表数据库DAo*/
	private DBPushMsg dbPushMsg = null;
	
	/**获取默认数据*/
	private final int WHAT_GET_DEFAULT = 0;
	/**获取刷新、更多数据*/
	private final int WHAT_GET_FRESH = 1;
	
	public Fragment_jilu(){
		data = new ArrayList<News>();
		dateFormat = new SimpleDateFormat("MM-dd HH:mm",Locale.UK); 
		dataVisitors = new DataVisitors();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dbPushMsg = new DBPushMsg(getActivity());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.page_jilu, null);
		view.findViewById(R.id.showRight).setOnClickListener(this);
		
		listView = (RefreshListView)view.findViewById(R.id.listview);
		adapter = new CommonNewsAdapter(getActivity(), data);
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		listView.refreshVisible();
		
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.showRight://显示右边菜单项
				((InterfaceActivity) getActivity()).showRight();
				break;
				
			default:
				break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if(isLoading){
			return;
		}
		isLoading = true;
		
		int id = adapter.getLatestId();
		if(id < 0){//加载默认列表
			dataVisitors.getDBListDefault(dbPushMsg, this, WHAT_GET_DEFAULT);
		}else{//加载更新的推送新闻
			dataVisitors.getDBFresh(dbPushMsg, id, this, WHAT_GET_FRESH);
		}
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if(isLoading){
			return;
		}
		isLoading = true;
		
		int id = adapter.getMinId();
		if(id < 0){//加载默认列表
			dataVisitors.getDBListDefault(dbPushMsg, this, WHAT_GET_DEFAULT);
		}else{//加载更旧的推送新闻
			dataVisitors.getDBMore(dbPushMsg, id, this, WHAT_GET_FRESH);
		}
		
	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		isLoading = false;
		Context context = getActivity();
		if(context == null){//界面已不渲杂了，没必要再做
			return;
		}
		
		if(res == null){
			Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(what == WHAT_GET_DEFAULT){//默认列表
			data.clear();
		}
		
		List<?> resData = (List<?>)res;
		for(Object item : resData){
			data.add((News)item);
		}
		
		adapter.notifyDataSetChanged();
		listView.stopLoadMore();
		listView.stopRefresh();
		listView.setRefreshTime(dateFormat.format(new Date(System.currentTimeMillis())));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Activity activity = getActivity();
		Intent intent = new Intent(activity, NewsShowActivity.class);
		MyApplication app = (MyApplication) activity.getApplication();
		
		if (position == 0) {// 点中头部更多
			return;
		}
		
		position--;
		intent.putExtra("INDEX", position);
		app.setShowDetailDatas(data);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	}


}
