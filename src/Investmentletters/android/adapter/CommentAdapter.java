package Investmentletters.android.adapter;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.entity.Comment;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 评论adapter
 * */
public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private List<Comment> mData = null;

	public CommentAdapter(Context context, List<Comment> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 获取列表最小一条新闻id */
	public int getMinId() {
		int id = -1;
		int tempId = 0;
		for (Comment item : mData) {
			tempId = item.getId();
			if (id == -1) {
				id = tempId;
				continue;
			}

			if (id > tempId) {
				id = tempId;
			}
		}
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolper viewHolper = null;

		if (view == null) {
			view = View.inflate(mContext, R.layout.listitem_comment_list, null);

			viewHolper = new ViewHolper();
			viewHolper.userName = (TextView) view
					.findViewById(R.id.id_comment_user_name); // 用户名
			viewHolper.content = (TextView) view
					.findViewById(R.id.id_comment_content); // 内容
			viewHolper.time = (TextView) view
					.findViewById(R.id.id_comment_time); // 时间

			viewHolper.llReply = (LinearLayout) view
					.findViewById(R.id.reply_container);// 被回复框

			view.setTag(viewHolper);
		} else {
			viewHolper = (ViewHolper) view.getTag();
		}

		Comment comment = mData.get(position); // 评论列表Item内容
		viewHolper.userName.setText(comment.getUserName()); // 设置用户名

		String content = comment.getContent();
		List<Comment> res = getReply(content);

		viewHolper.llReply.removeAllViews();

		if (res == null) {// 没有回复，直接显示
			viewHolper.content.setText(content); // 设置（评论）内容
		} else {
			viewHolper.content.setText(res.get(0).getContent()); // 设置（评论）内容

			int size = res.size();
			Comment item = null;
			View vReply = null;
			for (int i = 1; i < size; i++) {
				item = res.get(i);
				vReply = View.inflate(mContext, R.layout.discuss_reply, null);
				vReply.setBackgroundColor(Color.RED);
				((TextView) vReply.findViewById(R.id.reply_id)).setText(item
						.getUserName());
				((TextView) vReply.findViewById(R.id.reply_content))
						.setText(item.getContent());
				viewHolper.llReply.addView(vReply,
						viewHolper.llReply.getChildCount());
			}

		}

		viewHolper.time.setText(comment.getTime()); // 设置时间

		return view;
	}

	private class ViewHolper {
		private TextView userName;
		private TextView content;
		private TextView time;

		LinearLayout llReply = null;// 被回复框reply_container
	}

	/**
	 * 获取回复内容
	 * 
	 * @param src
	 *            源
	 * @return null:没有回复，直接显示, 其他：第一只有内容，本次回复的内容，其他的为被回复的对像
	 */
	private List<Comment> getReply(String src) {

		if (src == null) {
			return null;
		}

		src = src.trim();

		if (src.length() == 0) {
			return null;
		}

		/*
		 * Q ###游客&&&asdf
		 * 
		 * ###游客&&&zzzzzzzzz
		 * 
		 * ###游客&&&pppppppppllllllllllmmmmmmm ###游客&&&pppppppppllllllllll
		 * ###1862009****&&&25
		 */

		String[] temp = src.split("###");

		if (temp.length < 2) {
			return null;
		}

		List<Comment> result = new ArrayList<Comment>();
		Comment item = new Comment();
		item.setContent(temp[0]);
		result.add(item);

		String[] tempSlip = null;
		for (int i = 1; i < temp.length; i++) {
			item = new Comment();
			tempSlip = temp[i].split("&&&");

			if (tempSlip.length < 2) {
				item.setUserName("");
				item.setContent(tempSlip[0]);
			} else {
				item.setUserName(tempSlip[0]);
				item.setContent(tempSlip[1]);
			}
			result.add(item);
		}

		return result;
	}

}
