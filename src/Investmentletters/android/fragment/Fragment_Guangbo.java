package Investmentletters.android.fragment;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.R;
import Investmentletters.android.activity.Dj_WeiboActivity;
import Investmentletters.android.activity.InterfaceActivity;
import Investmentletters.android.activity.MmsService;
import Investmentletters.android.adapter.RadioListAdapter;
import Investmentletters.android.dao.RadioDJ;
import Investmentletters.android.dao.RadioList;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.entity.BaseEntity;
import Investmentletters.android.entity.DJ;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.AccoutContent;
import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.ImageLoadHandler;
import Investmentletters.android.utils.ImageLoadThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liang.MediaPlayer;
import com.liang.MediaPlayerListener;

public class Fragment_Guangbo extends Fragment implements View.OnClickListener,
		DataVisitors.CallBack {

	/** 显示右边 */
	private ImageButton showRight;
	/** 主持人头像 */
	private ImageView ivHead;;
	/** 新浪微博资料按钮 */
	private Button Detailed;
	/** 广播列表数据 */
	private List<BaseEntity> listData = null;
	/** 广播列表 */
	private ListView brandListView = null;
	/** 广播栏目列表adapter */
	private RadioListAdapter radioAdapter = null;
	/** 是否正在加载广播栏目 */
	private boolean isLoadList = false;
	/** 是否正在加载dj列表 */
	private boolean isLoadDj = false;

	/** 获取到广播栏目列表数据 */
	private final int GET_DATA = 0;
	/** 获取到广播头像 */
	private final int GET_IMAGE = 1;

	/** 数据访问者，主要用于协调调用者、线程以及塞阻方法的关系 */
	private DataVisitors dataVisitors = null;
	/** 电台节目列表dao */
	private RadioList radiolist = null;
	/** 电台DJ DAO */
	private RadioDJ djDao = null;
	private ImageLoadHandler handler = null;
	/** 电台DJ实体 */
	private List<DJ> djList = null;

	/** 播放器 */
	private MediaPlayer player = null;
	/** 播放提示 */
	private TextView tvTips = null;

	/** 播放控制按钮 */
	private ImageView ivPlayeControl = null;

	/** 播放Intent */
	private Intent playIntent = null;
	/** 主持人新浪id */
	long dj_weiboid;
	TextView Djname;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		listData = new ArrayList<BaseEntity>();
		radioAdapter = new RadioListAdapter(getActivity(), listData);

		dataVisitors = new DataVisitors();
		radiolist = new RadioList();
		djDao = new RadioDJ();
		djList = new ArrayList<DJ>();

		player = MediaPlayer.getInstance();
		player.setDataSources(Constants.MMS_URL);
		System.out.println("在oncrate...");
		new AccoutContent(getActivity());
		player.setListener(new MediaPlayerListener() {

			@Override
			public void onPrePared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("");
				if (tvTips != null) {
					tvTips.setText("准备就绪");
				}

				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_stop);
				}

				mp.play();
			}

			@Override
			public void onPlay(byte[] data) {
				// TODO Auto-generated method stub
				System.out.println("播放中：" + data.length);
				if (tvTips != null) {
					tvTips.setText("播放中...");
				}

				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_stop);
				}
			}

			@Override
			public void onError(int errNo) {
				// TODO Auto-generated method stub
				System.out.println("出错:" + errNo);
				if (tvTips != null) {
					tvTips.setText("播放失败");
				}
				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_play);
				}
			}

			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub
				if (tvTips != null) {
					tvTips.setText("停止");
				}
				System.out.println("停止");
				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_play);
				}
			}

			@Override
			public void onBuffer(int percent) {
				// TODO Auto-generated method stub
				if (tvTips != null) {
					tvTips.setText("正在缓冲" + percent + "%...");
				}
				System.out.println("缓冲中：" + percent);
				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_stop);
				}
			}
		});

		playIntent = new Intent(getActivity(), MmsService.class);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_gushiguangbo, null);

		showRight = (ImageButton) view.findViewById(R.id.showRight);
		showRight.setOnClickListener(this);

		ivHead = (ImageView) view.findViewById(R.id.head1);
		ivHead.setOnClickListener(this);
		ivPlayeControl = (ImageView) view.findViewById(R.id.play);
		ivPlayeControl.setOnClickListener(this);// 广播播放控制
		Djname = (TextView) view.findViewById(R.id.dj_name);

		Detailed = (Button) view.findViewById(R.id.detailed);
		Detailed.setOnClickListener(this);
		tvTips = (TextView) view.findViewById(R.id.tips);

		listData = new ArrayList<BaseEntity>();
		radioAdapter = new RadioListAdapter(getActivity(), listData);
		brandListView = (ListView) view.findViewById(R.id.radio_list);
		brandListView.setAdapter(radioAdapter);
		handler = new ImageLoadHandler();

		return view;
	}

	/**
	 * 转换json为list
	 * 
	 * @param jsonStr
	 *            json串
	 * 
	 * @return
	 */

	public List<News> json2List(String jsonStr) {
		List<News> result = null;
		try {

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Object tag = ivHead.getTag();
		switch (v.getId()) {
		case R.id.showRight:
			((InterfaceActivity) getActivity()).showRight();
			break;
		case R.id.head1:
			System.out.println("点击头像：");
			if (tag != null) {
				int size = djList.size() - 1;
				int index = (Integer) tag;
				index++;

				if (index > size) {
					index = 0;
				}

				new ImageLoadThread(getActivity(), djList.get(index).getImg(),
						ivHead, index, handler,
						ImageLoadHandler.HANDLER_LOAD_IMG).start();
				String name = djList.get(index).getName();
				Djname.setText(name);
			}

			break;

		case R.id.play: // 广播播放控制
			if (player.getStatus() != MediaPlayer.STATUS_STOP) {// 停止
				System.out.println("点击停止...");
				tvTips.setText("正在停止");
				playIntent.putExtra(MmsService.INTENT_COMMAND, MmsService.STOP);
			} else {// 播放
				System.out.println("点击播放...");
				tvTips.setText("正在准备");
				playIntent.putExtra(MmsService.INTENT_COMMAND, MmsService.PLAY);
			}

			getActivity().startService(playIntent);

			break;

		case R.id.detailed:// 详细资料
			if (tag == null) {
				return;
			}
			int index = (Integer) tag;
			long id = Long.parseLong(djList.get(index).getWeiboId());
			// Content.LoginToSinaWeibo(id);
			dj_weiboid = id;

			Activity activity = getActivity();
			Intent intent = new Intent(activity, Dj_WeiboActivity.class);
			intent.putExtra("INDEX", dj_weiboid);
			intent.putExtra("NAME", "msdfsdf");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);

			break;

		default:
			break;
		}

	}

	@Override
	public void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		if (res == null) {
			return;
		}
		List<?> data = (List<?>) res;

		switch (what) {
		case GET_DATA: // 获取到今日快讯栏目列表数据
			if (data != null) {
				listData.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					listData.add((BaseEntity) data.get(i));
				}
				radioAdapter.notifyDataSetChanged();
			}
			isLoadList = false;
			break;
		case GET_IMAGE:
			if (data != null) {
				djList.clear();
				int size = data.size();
				for (int i = 0; i < size; i++) {
					djList.add((DJ) data.get(i));
				}
				if (djList.size() > 0) {// 加载第一个dj头像
					new ImageLoadThread(getActivity(), djList.get(0).getImg(),
							ivHead, 0, handler,
							ImageLoadHandler.HANDLER_LOAD_IMG).start();
				}
			}
			isLoadDj = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (listData.size() < 1 && !isLoadList) {
			isLoadList = true;
			dataVisitors.doGet(radiolist, this, GET_DATA);
		}

		if (djList.size() < 1 && !isLoadDj) {
			isLoadDj = true;
			dataVisitors.doGet(djDao, this, GET_IMAGE);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.setListener(null);
	}
}
