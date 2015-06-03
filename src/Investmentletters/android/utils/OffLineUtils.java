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
 * 离线加载线程类
 * @author Administrator
 *
 */
public class OffLineUtils implements CallBack{

	/**因为有两人地方入口，防重复加载*/
	private static boolean isLoading = false;
	
	private ProgressDialog progressDialog = null;
	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	/** 投资快讯dao类 */
	private Investmentletters investmentletters = null;
	/**投资快讯DB dao*/
	private DBInvestmentletters dbInvestmentletters = null;
	
	/** 今日快讯，没有栏目的dao类 */
	private TodayNoBrand todayNoBrand = null;
	/** 今日快讯，没有栏目的DBdao类 */
	private DBToday dbTodayNoBrand = null;
	
	/** 公司雷达dao类 */
	private Radar radar = null;
	/** 公司雷达DB dao类 */
	private DBRadar dbRadar = null;
	
	/** 数据解码dao类 */
	private Decoder decoder = null;
	/** 数据解码DB dao类 */
	private DBDecode dbDecoder = null;
	
	/** 操盘解读dao类 */
	private TraderExplain traderExplain = null;
	/** 操盘解读DB dao类 */
	private DBTraderExplain dbTraderExplain = null;
	
	/** 美图dao类 */
	private ImageOnLine imageOnLine = null;
	/** 美图DB dao类 */
	private DBImageOnLine dbImageOnLine = null;
	
	/**美图在线详细新闻数据库DAo*/
	private DBImageDetail gbImgDetail = null;
	
	/** 美图详情model */
	private ImageOnLineDetailModel imageOnLineDetailModel = null;
	
	/**what:下载投资快讯*/
	private final int WHAT_INV = 1;
	/**what:下载今日快讯*/
	private final int WHAT_TODAY = 2;
	/**what:下载公司雷达*/
	private final int WHAT_LADAR = 3;
	/**what:下载数据解码*/
	private final int WHAT_DECODE = 4;
	/**what:下载操盘解讯*/
	private final int WHAT_EXPLAIN = 5;
	/**what:下载美图在线列表*/
	private final int WHAT_IMG_LIST = 6;
	
	
	/**正在进行的任务数*/
	private int taskNum = 0;
	
	private Context context = null;
	
	/**美图在线详细callback*/
	private CallBack imgDetailCallBack = null;
	
	public OffLineUtils(Context context){
		
		this.context = context;
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("请稍候");
		dataVisitors = new DataVisitors();
		
		
		investmentletters = new Investmentletters();// 投资快讯Model类
		dbInvestmentletters = new DBInvestmentletters(context);
		
		todayNoBrand = new TodayNoBrand();// 今日快报Model类
		dbTodayNoBrand = new DBToday(context);
		
		imageOnLine = new ImageOnLine();// 美图dao
		dbImageOnLine = new DBImageOnLine(context);
		
		radar = new Radar(); // 公司雷达dao类
		dbRadar = new DBRadar(context);
		
		decoder = new Decoder(); // 数据解码dao类
		dbDecoder = new DBDecode(context);
		
		traderExplain = new TraderExplain(); // 操盘解读dao类
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
	
	/**开载加载离线数据*/
	public void start(){
		
		if(isLoading){//其他地方还有
			Toast.makeText(context, "开始加载", Toast.LENGTH_SHORT).show();
		}
		
		if(checkOffLineState(true,true)>0){//还有任任在后台
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
	 * 检查离线下载是否完成
	 * @param add true:增加一个任务  false:减一个任务
	 * @param readOnly 只读
	 * 
	 * return 当前任务数
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
			Toast.makeText(context, "完成离线", Toast.LENGTH_SHORT).show();
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
			case WHAT_INV: //下载投资快讯
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbInvestmentletters, data, null, 0);//添加到数据库
				checkOffLineState(false,false);
				break;
			
			case WHAT_TODAY: //下载今日快讯
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbTodayNoBrand, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_LADAR: //公司雷达
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbRadar, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_DECODE: //数据解码
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbDecoder, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_EXPLAIN: //操盘解讯
				data = new ArrayList<News>();
				for(Object item:tempData){
					data.add((News)item);
				}
				dataVisitors.dbAdd(dbTraderExplain, data, null, 0);
				checkOffLineState(false,false);
				break;
				
			case WHAT_IMG_LIST: //美图在线列表
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
