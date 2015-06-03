package Investmentletters.android.utils;

import java.util.ArrayList;
import java.util.List;

import Investmentletters.android.dao.DBDecode;
import Investmentletters.android.dao.DBImageDetail;
import Investmentletters.android.dao.DBImageOnLine;
import Investmentletters.android.dao.DBInvestmentletters;
import Investmentletters.android.dao.DBRadar;
import Investmentletters.android.dao.DBToday;
import Investmentletters.android.dao.DBTraderExplain;
import Investmentletters.android.dao.Decoder;
import Investmentletters.android.dao.ImageOnLine;
import Investmentletters.android.dao.Investmentletters;
import Investmentletters.android.dao.Radar;
import Investmentletters.android.dao.TodayNoBrand;
import Investmentletters.android.dao.TraderExplain;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;
import Investmentletters.android.entity.Image;
import Investmentletters.android.entity.News;
import Investmentletters.android.model.ImageOnLineDetailModel;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * ���߼����߳���
 * @author Administrator
 *
 */
public class OffLineUtils implements CallBack{

	/**��Ϊ�����˵ط���ڣ����ظ�����*/
	private static boolean isLoading = false;
	
	private ProgressDialog progressDialog = null;
	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	/** Ͷ�ʿ�Ѷdao�� */
	private Investmentletters investmentletters = null;
	/**Ͷ�ʿ�ѶDB dao*/
	private DBInvestmentletters dbInvestmentletters = null;
	
	/** ���տ�Ѷ��û����Ŀ��dao�� */
	private TodayNoBrand todayNoBrand = null;
	/** ���տ�Ѷ��û����Ŀ��DBdao�� */
	private DBToday dbTodayNoBrand = null;
	
	/** ��˾�״�dao�� */
	private Radar radar = null;
	/** ��˾�״�DB dao�� */
	private DBRadar dbRadar = null;
	
	/** ���ݽ���dao�� */
	private Decoder decoder = null;
	/** ���ݽ���DB dao�� */
	private DBDecode dbDecoder = null;
	
	/** ���̽��dao�� */
	private TraderExplain traderExplain = null;
	/** ���̽��DB dao�� */
	private DBTraderExplain dbTraderExplain = null;
	
	/** ��ͼdao�� */
	private ImageOnLine imageOnLine = null;
	/** ��ͼDB dao�� */
	private DBImageOnLine dbImageOnLine = null;
	
	/**��ͼ������ϸ�������ݿ�DAo*/
	private DBImageDetail gbImgDetail = null;
	
	/** ��ͼ����model */
	private ImageOnLineDetailModel imageOnLineDetailModel = null;
	
	/**what:����Ͷ�ʿ�Ѷ*/
	private final int WHAT_INV = 1;
	/**what:���ؽ��տ�Ѷ*/
	private final int WHAT_TODAY = 2;
	/**what:���ع�˾�״�*/
	private final int WHAT_LADAR = 3;
	/**what:�������ݽ���*/
	private final int WHAT_DECODE = 4;
	/**what:���ز��̽�Ѷ*/
	private final int WHAT_EXPLAIN = 5;
	/**what:������ͼ�����б�*/
	private final int WHAT_IMG_LIST = 6;
	
	
	/**���ڽ��е�������*/
	private int taskNum = 0;
	
	private Context context = null;
	
	/**��ͼ������ϸcallback*/
	private CallBack imgDetailCallBack = null;
	
	public OffLineUtils(Context context){
		
		this.context = context;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("���Ժ�");
		dataVisitors = new DataVisitors();
		
		
		investmentletters = new Investmentletters();// Ͷ�ʿ�ѶModel��
		dbInvestmentletters = new DBInvestmentletters(context);
		
		todayNoBrand = new TodayNoBrand();// ���տ챨Model��
		dbTodayNoBrand = new DBToday(context);
		
		imageOnLine = new ImageOnLine();// ��ͼdao
		dbImageOnLine = new DBImageOnLine(context);
		
		radar = new Radar(); // ��˾�״�dao��
		dbRadar = new DBRadar(context);
		
		decoder = new Decoder(); // ���ݽ���dao��
		dbDecoder = new DBDecode(context);
		
		traderExplain = new TraderExplain(); // ���̽��dao��
		dbTraderExplain = new DBTraderExplain(context);
		
		imageOnLineDetailModel = new ImageOnLineDetailModel(dataVisitors);
		gbImgDetail = new DBImageDetail(context);
		
		imgDetailCallBack = new CallBack() {
			@Override
			public void onResult(int what, Object res) {
				// TODO Auto-generated method stub
				if(res == null){
					checkOffLineState(false,false);
					return;
				}
				List<?> tempData = (List<?>)res;
				List<Image> imgList = new ArrayList<Image>();
				for(Object item:tempData){
					imgList.add((Image)item);
				}
				dataVisitors.dbAdd(gbImgDetail, imgList, what, null, 0);
				checkOffLineState(false,false);
			}
		};
	}
	
	/**���ؼ�����������*/
	public void start(){
		
		if(isLoading){//�����ط�����
			Toast.makeText(context, "��ʼ����", Toast.LENGTH_SHORT).show();
		}
		
		if(checkOffLineState(true,true)>0){//���������ں�̨
			progressDialog.show();
			return;
		}
		
		isLoading = true;
		progressDialog.show();
		dataVisitors.getListDefault(investmentletters, this, WHAT_INV);
		dataVisitors.getListDefault(todayNoBrand, this, WHAT_TODAY);
		dataVisitors.getListDefault(radar, this, WHAT_LADAR);
		dataVisitors.getListDefault(decoder, this, WHAT_DECODE);
		dataVisitors.getListDefault(traderExplain, this, WHAT_EXPLAIN);
		dataVisitors.getListDefault(imageOnLine, this, WHAT_IMG_LIST);
		taskNum = 6;
	}
	
	
	/**
	 * ������������Ƿ����
	 * @param add true:����һ������  false:��һ������
	 * @param readOnly ֻ��
	 * 
	 * return ��ǰ������
	 */
	public synchronized int checkOffLineState(boolean add,boolean readOnly){
		
		if(readOnly){
			return taskNum;
		}
		
		if(add){
			taskNum++;
		}else{
			taskNum--;
		}
		
		if(taskNum < 1){
			progressDialog.dismiss();
			Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
			isLoading = false;
		}
		
		return taskNum;
		
	}
	
	@Override
	public synchronized void onResult(int what, Object res) {
		// TODO Auto-generated method stub
		if(res == null){
			checkOffLineState(false,false);
			return;
		}
		
		List<?> tempData = (List<?>)res;
		List<News> data = null;
		
		switch(what){
			case WHAT_INV: //����Ͷ�ʿ�Ѷ
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//��ӵ����ݿ�
				checkOffLineState(false,false);
				break;
			
			case WHAT_TODAY: //���ؽ��տ�Ѷ
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbTodayNoBrand, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_LADAR: //��˾�״�
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbRadar, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_DECODE: //���ݽ���
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbDecoder, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_EXPLAIN: //���̽�Ѷ
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbTraderExplain, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_IMG_LIST: //��ͼ�����б�
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbImageOnLine, data, null, 0);
				
				for(News item:data){
					checkOffLineState(true,false);
					int id = item.getId();
					imageOnLineDetailModel.getDetail(id, imgDetailCallBack, id);
				}
				
				checkOffLineState(false,false);
				
				break;
		
			default:
				break;
		}
	}
	
	
}
