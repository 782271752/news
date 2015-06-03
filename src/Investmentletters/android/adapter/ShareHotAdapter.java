package Investmentletters.android.adapter;

import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.Share;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 互动诊股热门股票列表
 * @author liang
 */
public class ShareHotAdapter extends BaseAdapter {
	
	private LayoutInflater inflater = null;
	private List<Share> data = null;
	
	public ShareHotAdapter(Context context , List<Share> data){
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	private class ViewHolder{
		TextView tvName = null;
		TextView tvDesc = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.share_hot_adapter, null);
			holder.tvName = (TextView)convertView.findViewById(R.id.content);
			holder.tvDesc = (TextView)convertView.findViewById(R.id.desc);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Share item = data.get(position);
		holder.tvName.setText(item.getName()+"("+item.getNumber()+")");
		holder.tvDesc.setText("动态PE:"+item.getPe()+"  上年每股收益:"+item.getMoney());
		
		return convertView;
	}

}
