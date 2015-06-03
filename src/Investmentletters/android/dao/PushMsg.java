package Investmentletters.android.dao;

import java.util.List;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Constants;

/**
 * 消息推送的dao类<br/>
 * @author liang
 */
public class PushMsg extends NewsBase {
	
	/**推送类型*/
	private int type = 0;
	
	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		return json2List(getHttp().doGet(getUrl(getFreshUrlFormat(),maxId ,getType())));
	}

	@Override
	public List<News> getUpdate(String lastTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		super.setType(type);
		this.type = type;
	}

	@Override
	public String getDefaultUrlFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMoreUrlFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFreshUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_PUSH_DETAIL;
	}

	@Override
	public String getUpdateUrlFormat() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
