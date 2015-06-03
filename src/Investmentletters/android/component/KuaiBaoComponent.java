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
 * ���տ챨Fragment_Kuaibao�����������ٵ����ļ������������Ӵ���ɶ���
 * @author liang
 */
public class KuaiBaoComponent {
	
	
	/**
	 * ���ô�ͼ�±�
	 * @param views ��ͼ�±�view����
	 * @param selected ѡ��Ŀ����±ꡣ<strong>ע�⣺</strong>���selected���鳬�磬����ʾ�������ͼ��
	 * @param newsList ��Ӧ����������
	 */
	public void setBigNewsIndexIconSelected(List<View> views , int selected , List<?> newsList){
		
		if(views == null || newsList == null){
			return;
		}
		
		int newsCount = newsList.size();
		int size = views.size();
		
		View view = null;
		
		if(selected <0 || selected >= size){//����ʾ
			for(int i=0 ; i<size ; i++){
				view = views.get(i);
				view.setVisibility(View.GONE);
			}
			
			return;
		}
		
		//����ʾ����������±�
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(newsCount > i){
				view.setVisibility(View.VISIBLE);
			}else{
				view.setVisibility(View.GONE);
			}
		}
		
		//��ʾѡ���±�
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(i != selected){
				view.setBackgroundResource(R.drawable.index_not_select);	
				continue;
			}

			//ѡ��
			view.setBackgroundResource(R.drawable.index_select);
		}
	}
	
	/**
	 * �������������л���ѡ��״̬
	 * @param views ���������л�������
	 * @param selected ������Ϊѡ�е�������
	 */
	public void setSwitchBrandSelected(List<View> views,View selected){
		if(views == null || selected == null){
			return;
		}
		
		int size = views.size();
		View view = null;
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(view != selected){//δѡ��
				view.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			}
		}
		
		selected.setBackgroundResource(R.drawable.bg_radiobtn_press);
	}
	
	/**
	 * �������������л���ѡ��״̬
	 * @param views ���������л�������
	 * @param selected ������Ϊѡ�е�������
	 */
	public void setSwitchBrandSelected(List<View> views,int selected){
		if(views == null){
			return;
		}
		
		int size = views.size();
		View view = null;
		for(int i=0 ; i<size ; i++){
			view = views.get(i);
			if(i != selected){//δѡ��
				view.setBackgroundResource(R.drawable.bg_radiobtn_normal);
			}else{
				view.setBackgroundResource(R.drawable.bg_radiobtn_press);
			}
		}
	}
	
	/**
	 * ��dest���ظ�����src��
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
	 * ���������������ݲ���
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
						setBigNewsIndexIconSelected(indexArr, 0, data);// ����ѡ���±�
						tvbigTitle.setText(data.get(0).getTitle());	
					}
					
					pagerAdapter.notifyDataSetChanged();
				}
				
				adapter.notifyDataSetChanged();
				
				if(isLoadDefault){
					if (lastId < 0) {//��ʾû�����ݣ�Ϊ���μ���
						dataVisitors.getListDefault(netModel, callback, WHAT_DEFAULT);
					} else {//����ˢ�� 
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
