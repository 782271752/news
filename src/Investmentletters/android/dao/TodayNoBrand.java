package Investmentletters.android.dao;

import java.util.List;

import Investmentletters.android.dao.base.NewsBase;
import Investmentletters.android.entity.News;
import Investmentletters.android.utils.Constants;

/**
 * 今日快讯，没有栏目的dao类
 * @author liang
 */
public class TodayNoBrand extends NewsBase {
	
	@Override
	public List<News> getListDefault() {
		// TODO Auto-generated method stub
		return json2List(getHttp().doGet(getUrl(getDefaultUrlFormat())));
	}

	@Override
	public List<News> getMore(int minId) {
		// TODO Auto-generated method stub
		return json2List(getHttp().doGet(getUrl(getMoreUrlFormat() , minId)));
	}

	@Override
	public List<News> getFresh(int maxId) {
		// TODO Auto-generated method stub
		return json2List(getHttp().doGet(getUrl(getFreshUrlFormat() ,maxId)));
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return TYPE_TODAY_NO_BRAND;
	}

	@Override
	public String getDefaultUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_NO_BRAND_DEFAULT;
	}

	@Override
	public String getMoreUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_NO_BRAND_MORE;
	}

	@Override
	public String getFreshUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_TODAY_NO_BRAND_FRESH;
	}

	@Override
	public String getUpdateUrlFormat() {
		// TODO Auto-generated method stub
		return Constants.URL_GET_MODIFY_NEWS;
	}
	
}
