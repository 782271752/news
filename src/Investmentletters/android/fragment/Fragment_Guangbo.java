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

	/** ��ʾ�ұ� */
	private ImageButton showRight;
	/** ������ͷ�� */
	private ImageView ivHead;;
	/** ����΢�����ϰ�ť */
	private Button Detailed;
	/** �㲥�б����� */
	private List<BaseEntity> listData = null;
	/** �㲥�б� */
	private ListView brandListView = null;
	/** �㲥��Ŀ�б�adapter */
	private RadioListAdapter radioAdapter = null;
	/** �Ƿ����ڼ��ع㲥��Ŀ */
	private boolean isLoadList = false;
	/** �Ƿ����ڼ���dj�б� */
	private boolean isLoadDj = false;

	/** ��ȡ���㲥��Ŀ�б����� */
	private final int GET_DATA = 0;
	/** ��ȡ���㲥ͷ�� */
	private final int GET_IMAGE = 1;

	/** ���ݷ����ߣ���Ҫ����Э�������ߡ��߳��Լ����跽���Ĺ�ϵ */
	private DataVisitors dataVisitors = null;
	/** ��̨��Ŀ�б�dao */
	private RadioList radiolist = null;
	/** ��̨DJ DAO */
	private RadioDJ djDao = null;
	private ImageLoadHandler handler = null;
	/** ��̨DJʵ�� */
	private List<DJ> djList = null;

	/** ������ */
	private MediaPlayer player = null;
	/** ������ʾ */
	private TextView tvTips = null;

	/** ���ſ��ư�ť */
	private ImageView ivPlayeControl = null;

	/** ����Intent */
	private Intent playIntent = null;
	/** ����������id */
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
		System.out.println("��oncrate...");
		new AccoutContent(getActivity());
		player.setListener(new MediaPlayerListener() {

			@Override
			public void onPrePared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("");
				if (tvTips != null) {
					tvTips.setText("׼������");
				}

				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_stop);
				}

				mp.play();
			}

			@Override
			public void onPlay(byte[] data) {
				// TODO Auto-generated method stub
				System.out.println("�����У�" + data.length);
				if (tvTips != null) {
					tvTips.setText("������...");
				}

				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_stop);
				}
			}

			@Override
			public void onError(int errNo) {
				// TODO Auto-generated method stub
				System.out.println("����:" + errNo);
				if (tvTips != null) {
					tvTips.setText("����ʧ��");
				}
				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_play);
				}
			}

			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub
				if (tvTips != null) {
					tvTips.setText("ֹͣ");
				}
				System.out.println("ֹͣ");
				if (ivPlayeControl != null) {
					ivPlayeControl.setImageResource(R.drawable.radio_play);
				}
			}

			@Override
			public void onBuffer(int percent) {
				// TODO Auto-generated method stub
				if (tvTips != null) {
					tvTips.setText("���ڻ���" + percent + "%...");
				}
				System.out.println("�����У�" + percent);
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
		ivPlayeControl.setOnClickListener(this);// �㲥���ſ���
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
	 * ת��jsonΪlist
	 * 
	 * @param jsonStr
	 *            json��
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
			System.out.println("���ͷ��");
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

		case R.id.play: // �㲥���ſ���
			if (player.getStatus() != MediaPlayer.STATUS_STOP) {// ֹͣ
				System.out.println("���ֹͣ...");
				tvTips.setText("����ֹͣ");
				playIntent.putExtra(MmsService.INTENT_COMMAND, MmsService.STOP);
			} else {// ����
				System.out.println("�������...");
				tvTips.setText("����׼��");
				playIntent.putExtra(MmsService.INTENT_COMMAND, MmsService.PLAY);
			}

			getActivity().startService(playIntent);

			break;

		case R.id.detailed:// ��ϸ����
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
		case GET_DATA: // ��ȡ�����տ�Ѷ��Ŀ�б�����
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
				if (djList.size() > 0) {// ���ص�һ��djͷ��
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
