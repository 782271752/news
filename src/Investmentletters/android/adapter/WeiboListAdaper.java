package Investmentletters.android.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Investmentletters.android.R;
import Investmentletters.android.activity.ZoomActivity;
import Investmentletters.android.activity.ZoomImageActivity;
import Investmentletters.android.dao.base.WeiBoBase;
import Investmentletters.android.utils.ImageLoadThread;
import Investmentletters.android.utils.NewsListImageLoadHandler;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新闻列表Adapter
 * 
 * @author Administrator
 */
public class WeiboListAdaper extends BaseAdapter implements OnClickListener {
	List<WeiBoBase> data;
	Context context;
	private LayoutInflater mInflater;
	private NewsListImageLoadHandler handler = null;
	ProgressDialog Dialog = null;

	public WeiboListAdaper(Context context, List<WeiBoBase> data) {
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.data = data;
		handler = new NewsListImageLoadHandler();
		Dialog = new ProgressDialog(context);
		if (Dialog != null) {
			Dialog.setMessage("请稍候");
		}
		Dialog.show();
		Dialog.setCanceledOnTouchOutside(false);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View_Holper viewHolper = null;

		WeiBoBase item = data.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.weibo_text, null);
			viewHolper = new View_Holper();
			viewHolper.text = (TextView) convertView
					.findViewById(R.id.id_weibo_text);
			viewHolper.time = (TextView) convertView
					.findViewById(R.id.id_text_time);
			viewHolper.retweet_text = (TextView) convertView
					.findViewById(R.id.id_reposts_text);

			viewHolper.retweeted = (TextView) convertView
					.findViewById(R.id.id_retweeted);
			viewHolper.retweeted.setOnClickListener(this);

			viewHolper.repost = (TextView) convertView
					.findViewById(R.id.id_reposts);
			viewHolper.weiboimage = (ImageView) convertView
					.findViewById(R.id.id_weiboimage);
			viewHolper.weiboimage.setOnClickListener(this);
			viewHolper.repost.setOnClickListener(this);
			convertView.setTag(viewHolper);
		} else {
			viewHolper = (View_Holper) convertView.getTag();
		}
		String time = setdaytime(item.Time);
		viewHolper.time.setText(time);
		viewHolper.retweeted.setText(item.Comments + "");
		viewHolper.repost.setText(item.Reposts + "");
		viewHolper.retweet_text.setTag(position);
		viewHolper.text.setTag(position);
		if (item.retweet_text == null) {
			viewHolper.retweet_text.setVisibility(View.GONE);
			viewHolper.text.setText(item.text);
		} else {
			viewHolper.retweet_text.setVisibility(View.VISIBLE);
			viewHolper.retweet_text.setText(item.text);

			viewHolper.text.setText(item.retweet_text);
		}

		viewHolper.weiboimage.setTag(position);

		if (item.ImageUrl != null) {
			if (item.ImageUrl.length > 0) {
				new ImageLoadThread(context, item.ImageUrl[0],
						viewHolper.weiboimage, position, handler,
						NewsListImageLoadHandler.HANDLER_LOAD_IMG).start();
				viewHolper.weiboimage.setVisibility(View.VISIBLE);
			} else {
				viewHolper.weiboimage.setVisibility(View.GONE);
			}
		} else {
			viewHolper.weiboimage.setVisibility(View.GONE);
		}
		Dialog.dismiss();
		return convertView;
	}

	@SuppressLint("SimpleDateFormat")
	private String setdaytime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		Date res = null;
		try {
			res = sdf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sdf.applyLocalizedPattern("MM月dd日 HH时mm分");
		return sdf.format(res);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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

	/** 缓存类 */
	private class View_Holper {
		TextView text = null;
		TextView time = null;
		TextView retweeted = null;
		TextView repost = null;
		TextView retweet_text = null;
		ImageView weiboimage = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_weiboimage:
			int position = (Integer) v.getTag();

			// data.get(position).original_pic;
			String big_imgurl = data.get(position).original_pic;
			Intent intent = new Intent(context, ZoomActivity.class);
			intent.putExtra("url", big_imgurl);
			context.startActivity(intent);
			break;
		// case R.id.id_reposts:
		// new AlertDialog.Builder(context).setTitle("请输入").setIcon(
		// android.R.drawable.ic_dialog_info).setView(
		// new EditText(context)).setPositiveButton("转发并评论",new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// System.out.println("11111111111fegnqrjg qeighefbiu11111111111111111111111");
		//
		// }
		// }).show();
		// break;
		// case R.id.id_retweeted:
		// new AlertDialog.Builder(context).setTitle("请输入").setIcon(
		// android.R.drawable.ic_dialog_info).setView(
		// new EditText(context)).setPositiveButton("发表",new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// System.out.println("11111111111fegnqrjg qeighefbiu11111111111111111111111");
		//
		// }
		// }).show();
		// break;
		default:
			break;
		}
	}
}
