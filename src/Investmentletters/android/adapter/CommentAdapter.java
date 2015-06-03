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
 * ����adapter
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

	/** ��ȡ�б���Сһ������id */
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
					.findViewById(R.id.id_comment_user_name); // �û���
			viewHolper.content = (TextView) view
					.findViewById(R.id.id_comment_content); // ����
			viewHolper.time = (TextView) view
					.findViewById(R.id.id_comment_time); // ʱ��

			viewHolper.llReply = (LinearLayout) view
					.findViewById(R.id.reply_container);// ���ظ���

			view.setTag(viewHolper);
		} else {
			viewHolper = (ViewHolper) view.getTag();
		}

		Comment comment = mData.get(position); // �����б�Item����
		viewHolper.userName.setText(comment.getUserName()); // �����û���

		String content = comment.getContent();
		List<Comment> res = getReply(content);

		viewHolper.llReply.removeAllViews();

		if (res == null) {// û�лظ���ֱ����ʾ
			viewHolper.content.setText(content); // ���ã����ۣ�����
		} else {
			viewHolper.content.setText(res.get(0).getContent()); // ���ã����ۣ�����

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

		viewHolper.time.setText(comment.getTime()); // ����ʱ��

		return view;
	}

	private class ViewHolper {
		private TextView userName;
		private TextView content;
		private TextView time;

		LinearLayout llReply = null;// ���ظ���reply_container
	}

	/**
	 * ��ȡ�ظ�����
	 * 
	 * @param src
	 *            Դ
	 * @return null:û�лظ���ֱ����ʾ, ��������һֻ�����ݣ����λظ������ݣ�������Ϊ���ظ��Ķ���
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
		 * Q ###�ο�&&&asdf
		 * 
		 * ###�ο�&&&zzzzzzzzz
		 * 
		 * ###�ο�&&&pppppppppllllllllllmmmmmmm ###�ο�&&&pppppppppllllllllll
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
