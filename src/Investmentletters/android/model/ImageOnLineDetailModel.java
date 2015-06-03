package Investmentletters.android.model;

import Investmentletters.android.dao.DBImageDetail;
import Investmentletters.android.dao.ImageOnLineDetail;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;
import Investmentletters.android.utils.Constants;

/**
 * 美图在线详细Model
 * @author liang
 */
public class ImageOnLineDetailModel {

	/**数据访问协调类*/
	private DataVisitors dataVisitors = null;
	
	
	public ImageOnLineDetailModel(){
		dataVisitors = new DataVisitors();
	}
	
	public ImageOnLineDetailModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * 获取美图在线详细信息
	 * @param id 美图在线列表id
	 * @param cb 回调 ,回调的res中 null:失败  
	 * @param what 回调标记
	 */
	public void getDetail(int id,CallBack cb,int what){
		ImageOnLineDetail image = new ImageOnLineDetail();
		image.setUrl(Constants.URL_GET_IMAGE_ON_LINE_DETAIL, id);
		dataVisitors.doGet(image, cb, what);
	}
	
	/**
	 * 从DB获取美图在线详细信息
	 * @param dbImgDetail
	 * @param id 美图在线列表id
	 * @param cb 回调 ,回调的res中 null:失败  
	 * @param what 回调标记
	 */
	public void getDetailFromDb(DBImageDetail dbImgDetail,int id,CallBack cb,int what){
		dataVisitors.query(dbImgDetail, id, cb, what);
	}
	
}
