package Investmentletters.android.component;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.dao.DBLastTime;
import Investmentletters.android.dao.base.DBNewsListBase;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.NewsListBase;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Utils;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 今日快报Fragment_Kuaibao相关组件，减少单个文件代码量，增加代码可读性
 * @author liang
 */
public class KuaiBaoComponent {
	
	
	/**
	 * 设置大图下标
	 * @param views 大图下标view数组
	 * @param selected 选中目标的下标。<strong>注意：</strong>如果selected数组超界，则不显示数组里的图标
	 * @param newsList 对应的新闻数组
	 */
	public void setBigNewsIndexIconSelected(List<View> views , int selected , List<?> newsList){
		
		if(views == null || newsList == null){
			return;
		}
		
		int newsCount = newsList.size();
		int size = views.size();
		
		View view = null;
		
		if(selected <0 || selected >= size){//不显示
			for(int i=0 ; i<size ; i++){
				view = views.get(i);
				view.setVisibility(View.GONE);
			}
			
			return;
		}
		
		//不显示数量过多的下标
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(newsCount > i){
				view.setVisibility(View.VISIBLE);
			}else{
				view.setVisibility(View.GONE);
			}
		}
		
		//显示选中下标
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(i != selected){
				view.setBackgroundResource(R.drawable.index_not_select);	
				continue;
			}

			//选中
			view.setBackgroundResource(R.drawable.index_select);
		}
	}
	
	/**
	 * 设置新闻类型切换栏选中状态
	 * @param views 新闻类型切换栏数组
	 * @param selected 被设置为选中的类型栏
	 */
	public void setSwitchBrandSelected(List<View> views,View selected){
		if(views == null || selected == null){
			return;
		}
		
		int size = views.size();
		View view = null;
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(view != selected){//未选中
				view.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			}
		}
		
		selected.setBackgroundResource(R.drawable.bg_radiobtn_press);
	}
	
	/**
	 * 设置新闻类型切换栏选中状态
	 * @param views 新闻类型切换栏数组
	 * @param selected 被设置为选中的类型栏
	 */
	public void setSwitchBrandSelected(List<View> views,int selected){
		if(views == null){
			return;
		}
		
		int size = views.size();
		View view = null;
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(i != selected){//未选中
				view.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			}else{
				view.setBackgroundResource(R.drawable.bg_radiobtn_press);
			}
		}
	}
	
	/**
	 * 把dest非重复加入src中
	 * @param src
	 * @param dest
	 */
	public void addToShowNoRepeat(List<News> src,List<News> dest){
		
		if(src == null || dest == null){
			return;
		}
		
		int destLen = dest.size();
		
		int id = 0;
		boolean isSame = false;
		for(int i=0 ; i<destLen ; i++){
			id = dest.get(i).getId();
			isSame = false;
			for(int k=0 ; k<src.size() ; k++){
				if(src.get(k).getId() == id){
					isSame = true;
					break;
				}
			}
			
			if(isSame){
				continue;
			}
			
			src.add(dest.get(i));
		}
		
	}
	
	/**
	 * 网络下拉加载数据策略
	 * @param lastId 
	 * @param dbModel 
	 * @param netModel
	 * @param dataVisitors
	 * @param callback
	 * @param WHAT_DEFAULT
	 * @param WHAT_REFLASH
	 * @param isLoadDefault
	 * @param data
	 * @param viewPager
	 * @param indexArr
	 * @param tvbigTitle
	 * @param adapter
	 * @param pagerAdapter
	 * @param dbLastTime
	 * @param DB_UPDATE_TYPE
	 */
	public void netLoadFreshPloy(final int lastId,
							final DBNewsListBase dbModel , final NewsListBase<?> netModel , 
							 final DataVisitors dataVisitors,final DataVisitors.CallBack callback,
							 final int WHAT_DEFAULT , final int WHAT_REFLASH ,
							 final boolean isLoadDefault,final List<News> data,
							 final ViewPager viewPager , final List<View> indexArr,
							 final TextView tvbigTitle, final BaseAdapter adapter ,
							 final PagerAdapter pagerAdapter,final DBLastTime dbLastTime,
							 final int DB_UPDATE_TYPE){
		
		new AsyncTask<Void, Void, List<News>>(){

			@Override
			protected List<News> doInBackground(Void... params) {
				// TODO Auto-generated method stub
				
				if (lastId > 0) {
					return null;
				}
				
				return dbModel.getListDefault();
			}
			
			protected void onPostExecute(java.util.List<News> result) {
				
				addToShowNoRepeat(data , result);
				
				if(viewPager != null){
					Utils.SortTopMaxToMin(data);
					
					if(data.size()>0){
						viewPager.setCurrentItem(0, true);
						setBigNewsIndexIconSelected(indexArr, 0, data);// 设置选中下标
						tvbigTitle.setText(data.get(0).getTitle());	
					}
					
					pagerAdapter.notifyDataSetChanged();
				}
				
				adapter.notifyDataSetChanged();
				
				if(isLoadDefault){
					if (lastId < 0) {//表示没有数据，为初次加载
						dataVisitors.getListDefault(netModel, callback, WHAT_DEFAULT);
					} else {//下拉刷新 
						dataVisitors.getFresh(netModel, lastId, callback,WHAT_REFLASH);
					}
				}else{
					dataVisitors.getListDefault(netModel, callback, WHAT_DEFAULT);
				}
				
				dataVisitors.updateList(netModel, dbModel, dbLastTime, DB_UPDATE_TYPE	, null, 0);
			};
			
		}.execute();
	}
	
	
}
