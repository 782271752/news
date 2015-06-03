package Investmentletters.android.fragment;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.ImageNewsDetail;
import Investmentletters.android.activity.MyApplication;
import Investmentletters.android.activity.NewsShowActivity;
import Investmentletters.android.adapter.PreserveMeituAdapter;
import Investmentletters.android.adapter.PreserveNewsAdapter;
import Investmentletters.android.dao.DBImagePreserveList;
import Investmentletters.android.dao.DBPreserveList;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Utils;
import Investmentletters.android.view.RefreshListView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 显示收藏
 * @author Administrator
 */
public class Fragment_Collect extends Fragment implements View.OnClickListener,
					DataVisitors.CallBack,RefreshListView.IXListViewListener,OnItemClickListener{
	
	/**切换栏：文章collect_article*/
	private View vArticle = null;
	/**切换栏：图片collect_picture*/
	private View vImage = null;
	
	/**文章列表：article_list*/
	private RefreshListView rlvArticle = null;
	/**图片列表:image_list*/
	private RefreshListView rlvImage = null;
	
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	/**新闻收藏列表DAO*/
	private DBPreserveList dbArticle = null;
	/**图片新闻收藏列表DAO*/
	private DBImagePreserveList dbImage = null;
	
	/**文章adapter*/
	private PreserveNewsAdapter articleAdapter = null;
	/**文章数据*/
	private List<News> articleList = null;
	
	/**美图在线Adapter*/
	private PreserveMeituAdapter imgAdapter = null;
	/**美图数据*/
	private List<News> imgList = null;
	
	/**what:文章默认列表*/
	private final int WHAT_ARTICLE_DEFAULT = 0;
	/**what:文章加载更新*/
	private final int WHAT_ARTICLE_FRESH = 1;
	/**what:图片默认列表*/
	private final int WHAT_IMG_DEFAULT = 2;
	/**what:图片加载更新*/
	private final int WHAT_IMG_FRESH = 3;
	/**what:删除收藏*/
	private final int WHAT_DEL_PRESERVE = 4;
	
	/**防重复加载*/
	private boolean isLoadArticle = false;
	/**防重复加载*/
	private boolean isLoadImg = false;
	
	/**是否打开时显示文章列表,这里主要解决从菜单=》个人收藏=》图片，直接进入图片问题*/
	private boolean isInitShowArticle = true;
	
	/**是否为编辑状态*/
	private boolean isEditMode = false;
	
	/**按钮控制栏*/
	private View vColtrolBar = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Context context = getActivity();
		dataVisitors = new DataVisitors();
		dbArticle = new DBPreserveList(context);
		dbImage = new DBImagePreserveList(context);
		
		articleList = new ArrayList<News>();
		articleAdapter = new PreserveNewsAdapter(context, articleList);
		
		imgList = new ArrayList<News>();
		imgAdapter = new PreserveMeituAdapter(context, imgList);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mView = inflater.inflate(R.layout.page_collect, container, false);
		
		mView.findViewById(R.id.edit_or_finish).setOnClickListener(this);//编辑或完成按钮
		mView.findViewById(R.id.del_select).setOnClickListener(this);//删除所选
		mView.findViewById(R.id.del_all).setOnClickListener(this);//删除全部
		
		vArticle = mView.findViewById(R.id.collect_article);
		vArticle.setOnClickListener(this);
		
		vImage = mView.findViewById(R.id.collect_picture);
		vImage.setOnClickListener(this);
		
		vColtrolBar = mView.findViewById(R.id.control_bar);
		
		rlvArticle = (RefreshListView)mView.findViewById(R.id.article_list);
		rlvArticle.setAdapter(articleAdapter);
		rlvArticle.setPullLoadEnable(true);
		rlvArticle.setPullRefreshEnable(true);
		rlvArticle.setXListViewListener(this);
		rlvArticle.setOnItemClickListener(this);
		
		rlvImage = (RefreshListView)mView.findViewById(R.id.image_list);
		rlvImage.setAdapter(imgAdapter);
		rlvImage.setPullLoadEnable(true);
		rlvImage.setPullRefreshEnable(true);
		rlvImage.setXListViewListener(this);
		rlvImage.setOnItemClickListener(this);
		
		if(isInitShowArticle){//默认打开文章列表
			rlvArticle.setVisibility(View.VISIBLE);
			rlvImage.setVisibility(View.GONE);
			
			vArticle.setBackgroundResource(R.drawable.bg_radiobtn_press);
			vImage.setBackgroundColor(Color.argb(0, 0, 0, 0));
			
			rlvArticle.refreshVisible();	
		}else{//默认打开美图收藏列表
			rlvArticle.setVisibility(View.GONE);
			rlvImage.setVisibility(View.VISIBLE);
			
			vArticle.setBackgroundColor(Color.argb(0, 0, 0, 0));
			vImage.setBackgroundResource(R.drawable.bg_radiobtn_press);
			
			rlvImage.refreshVisible();	
		}
		return mView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.edit_or_finish://编辑完成按钮
				isEditMode = !isEditMode;
				articleAdapter.setEditableMode(isEditMode);
				imgAdapter.setEditableMode(isEditMode);
				if(isEditMode){
					vColtrolBar.setVisibility(View.VISIBLE);
					((ImageView)v).setImageResource(R.drawable.btn_finish);
				}else{
					vColtrolBar.setVisibility(View.GONE);
					((ImageView)v).setImageResource(R.drawable.btn_edit);
					
					int size = articleList.size();
					for(int i=0 ; i<size ; i++){
						articleList.get(i).setSelect(false);
					}
					
					size = imgList.size();
					for(int i=0 ; i<size ; i++){
						imgList.get(i).setSelect(false);
					}
				}
				break;
		
			case R.id.collect_article: //文章
				vArticle.setBackgroundResource(R.drawable.bg_radiobtn_press);
				vImage.setBackgroundColor(Color.argb(0, 0, 0, 0));
				rlvArticle.setVisibility(View.VISIBLE);
				rlvImage.setVisibility(View.GONE);
				
				if(articleList.size() < 1){
					rlvArticle.refreshVisible();
				}
				
				break;
				
			case R.id.collect_picture: //图片
				vArticle.setBackgroundColor(Color.argb(0, 0, 0, 0));
				vImage.setBackgroundResource(R.drawable.bg_radiobtn_press);
				rlvArticle.setVisibility(View.GONE);
				rlvImage.setVisibility(View.VISIBLE);
				
				if(imgList.size() < 1){
					rlvImage.refreshVisible();
				}
				
				break;
				
			case R.id.del_select://删除所选
				List<News> delList = new ArrayList<News>();
				for(News item:articleList){
					if(item.isSelect()){
						delList.add(item);
					}
				}
				
				for(News item:imgList){
					if(item.isSelect()){
						delList.add(item);
					}
				}
				
				for(News item:delList){
					articleList.remove(item);
					imgList.remove(item);
				}
				
				dataVisitors.cancelPreserveList(dbImage, delList, this, WHAT_DEL_PRESERVE);
				articleAdapter.notifyDataSetChanged();
				imgAdapter.notifyDataSetChanged();
				
				break;
				
			case R.id.del_all://删除全部
				
				List<News> delAllList = new ArrayList<News>();
				for(News item:articleList){
					delAllList.add(item);
				}
				
				for(News item:imgList){
					delAllList.add(item);
				}
				
				articleList.clear();
				imgList.clear();
				
				dataVisitors.cancelPreserveList(dbImage, delAllList, this, WHAT_DEL_PRESERVE);
				articleAdapter.notifyDataSetChanged();
				imgAdapter.notifyDataSetChanged();
				
				
				break;
	
			default:
				break;
		}
		
	}

	@Override
	public synchronized void onResult(int what, Object res) {
		Context context = getActivity();
		List<?> data = null;
		
		// TODO Auto-generated method stub
		switch(what){
			case WHAT_ARTICLE_DEFAULT: //what:文章默认列表
				isLoadArticle = false;
				data = (List<?>) res;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
					}
				} else {
					articleList.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						articleList.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(articleList);
				
				articleAdapter.notifyDataSetChanged();
				rlvArticle.stopRefresh();
				rlvArticle.stopLoadMore();
				rlvArticle.setRefreshTime("刚刚");
				
				break;
				
			case WHAT_ARTICLE_FRESH://what:文章加载更新
				isLoadArticle = false;
				data = (List<?>) res;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
					}
				} else {
					int size = data.size();
					for (int i = 0; i < size; i++) {
						articleList.add((News) data.get(i));
					}
				}
				
				Utils.SortTopMaxToMin(articleList);
				
				articleAdapter.notifyDataSetChanged();
				rlvArticle.stopRefresh();
				rlvArticle.stopLoadMore();
				rlvArticle.setRefreshTime("刚刚");
				break;
				
			case WHAT_IMG_DEFAULT: //图片默认列表
				isLoadImg = false;
				data = (List<?>) res;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
					}
				} else {
					
					imgList.clear();
					int size = data.size();
					for (int i = 0; i < size; i++) {
						imgList.add((News) data.get(i));
					}
				}
				imgAdapter.notifyDataSetChanged();
				rlvImage.stopRefresh();
				rlvImage.stopLoadMore();
				rlvImage.setRefreshTime("刚刚");
				break;
				
			case WHAT_IMG_FRESH: //图片加载更新
				isLoadImg = false;
				data = (List<?>) res;
				if (data == null) {
					if(context != null){
						Toast.makeText(context, "您的网速不给力呀", Toast.LENGTH_SHORT).show();
					}
				} else {
					int size = data.size();
					for (int i = 0; i < size; i++) {
						imgList.add((News) data.get(i));
					}
				}
				imgAdapter.notifyDataSetChanged();
				rlvImage.stopRefresh();
				rlvImage.stopLoadMore();
				rlvImage.setRefreshTime("刚刚");
				break;
				
			case WHAT_DEL_PRESERVE:
				if(context != null){
					if((Boolean)res){
						Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();						
					}else{
						Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();						
					}
				}
				
				break;
			
			default:
				break;
		}
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		int id = -1;
		
		if(isViewVisible(rlvArticle)){
			if(isLoadArticle){//防重复加载
				return;
			}

			id = articleAdapter.getLatestId();
			isLoadArticle= true;
			if(id<0){//空表
				dataVisitors.getDBListDefault(dbArticle, this, WHAT_ARTICLE_DEFAULT);
			}else{
				dataVisitors.getDBFresh(dbArticle, id, this, WHAT_ARTICLE_FRESH);
			}
		}else{
			if(isLoadImg){//防重复加载
				return;
			}

			id = imgAdapter.getLatestId();
			isLoadImg= true;
			if(id<0){//空表
				dataVisitors.getDBListDefault(dbImage, this, WHAT_IMG_DEFAULT);
			}else{
				dataVisitors.getDBFresh(dbImage, id, this, WHAT_IMG_FRESH);
			}
		}
	}


	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		int id = -1;
		if(isViewVisible(rlvArticle)){
			if(isLoadArticle){//防重复加载
				return;
			}

			id = articleAdapter.getMinId();
			isLoadArticle= true;
			if(id<0){//空表
				dataVisitors.getDBListDefault(dbArticle, this, WHAT_ARTICLE_DEFAULT);
			}else{
				dataVisitors.getMore(dbArticle, id, this, WHAT_ARTICLE_FRESH);
			}
		}else{
			if(isLoadImg){//防重复加载
				return;
			}

			id = imgAdapter.getMinId();
			isLoadImg= true;
			if(id<0){//空表
				dataVisitors.getDBListDefault(dbImage, this, WHAT_IMG_DEFAULT);
			}else{
				dataVisitors.getMore(dbImage, id, this, WHAT_IMG_FRESH);
			}
		}
	}
	
	/**
	 * 视图是否可见
	 * @param view
	 * @return true:可见 false:不可见
	 */
	private boolean isViewVisible(View view){
		return view.getVisibility() == View.VISIBLE;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Activity activity = getActivity();

		MyApplication app = (MyApplication) activity.getApplication();

		System.out.println("点击："+position);
		
		if (position == 0) {// 点中头部更多
			return;
		}
		
		if(isViewVisible(rlvArticle)){// 文章
			position--;
			
			if(isEditMode){//编辑模式
				News item = articleList.get(position); 
				item.setSelect(!item.isSelect());
				articleAdapter.notifyDataSetChanged();
				return;
			}
			
			Intent intent = new Intent(activity, NewsShowActivity.class);
			intent.putExtra("INDEX", position);
			intent.putExtra("IS_PRESERVE", true);
			app.setShowDetailDatas(articleList);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}else{ // 美图在线
			position--;
			
			if(isEditMode){//编辑模式
				News item = imgList.get(position); 
				item.setSelect(!item.isSelect());
				imgAdapter.notifyDataSetChanged();
				return;
			}
			
			Intent imgIntent = new Intent(activity, ImageNewsDetail.class);
			imgIntent.putExtra("IS_PRESERVE", true);
			imgIntent.putExtra("INDEX", imgList.get(position).getId());
			startActivity(imgIntent);
			getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
		}
		
	}
	
	/**是否打开时显示文章列表,这里主要解决从菜单=》个人收藏=》图片，直接进入图片问题,需要在FragmentManagement操作前执行*/
	public void setIsInitShowArticle(boolean show){
		isInitShowArticle = show;
	}
	

}
