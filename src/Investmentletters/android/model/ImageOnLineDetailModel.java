package Investmentletters.android.model;

import Investmentletters.android.dao.DBImageDetail;
import Investmentletters.android.dao.ImageOnLineDetail;
import Investmentletters.android.dao.base.DataVisitors;
import Investmentletters.android.dao.base.DataVisitors.CallBack;
import Investmentletters.android.utils.Constants;

/**
 * ��ͼ������ϸModel
 * @author liang
 */
public class ImageOnLineDetailModel {

	/**���ݷ���Э����*/
	private DataVisitors dataVisitors = null;
	
	
	public ImageOnLineDetailModel(){
		dataVisitors = new DataVisitors();
	}
	
	public ImageOnLineDetailModel(DataVisitors dataVisitors){
		this.dataVisitors = dataVisitors;	
	}
	
	/**
	 * ��ȡ��ͼ������ϸ��Ϣ
	 * @param id ��ͼ�����б�id
	 * @param cb �ص� ,�ص���res�� null:ʧ��  
	 * @param what �ص����
	 */
	public void getDetail(int id,CallBack cb,int what){
		ImageOnLineDetail image = new ImageOnLineDetail();
		image.setUrl(Constants.URL_GET_IMAGE_ON_LINE_DETAIL, id);
		dataVisitors.doGet(image, cb, what);
	}
	
	/**
	 * ��DB��ȡ��ͼ������ϸ��Ϣ
	 * @param dbImgDetail
	 * @param id ��ͼ�����б�id
	 * @param cb �ص� ,�ص���res�� null:ʧ��  
	 * @param what �ص����
	 */
	public void getDetailFromDb(DBImageDetail dbImgDetail,int id,CallBack cb,int what){
		dataVisitors.query(dbImgDetail, id, cb, what);
	}
	
}
