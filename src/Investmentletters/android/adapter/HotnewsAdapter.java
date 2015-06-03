package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.Hot_News;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotnewsAdapter extends BaseAdapter{
	private LayoutInflater inflater = null;
	private List<Hot_News> data = null;
	public HotnewsAdapter(Context context , List<Hot_News> data){
		inflater = LayoutInflater.from(context);
		this.data = data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(data == null){
			return 0;
		}
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	private class ViewHolder{
		TextView tvTitle = null;
		TextView tvUrl = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.share_hot_adapter, null);
			holder.tvTitle = (TextView)convertView.findViewById(R.id.content);
			holder.tvUrl = (TextView)convertView.findViewById(R.id.desc);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Hot_News item = data.get(position);
		holder.tvTitle.setText(item.gettitle());
		holder.tvUrl.setText(item.geturl());
		
		return convertView;
	}

}
