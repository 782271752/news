package Investmentletters.android.dao.base;

import java.util.List;

/**
 * 新闻列表DAO抽像基类
 * 
 * @author liang
 * 
 * @param <T>
 */
public abstract class NewsListBase<T> {

	/** 新闻接口类型:投资快讯 */
	public static final int TYPE_INVESTMENTLETTERS = 0x01;
	/** 新闻接口类型:没有栏目的今日快讯 */
	public static final int TYPE_TODAY_NO_BRAND = 0x02;
	/** 新闻接口类型：公司雷达 */
	public static final int TYPE_FADAR = 0x03;
	/** 新闻接口类型：数据解码 */
	public static final int TYPE_DECODER = 0x04;
	/** 新闻接口类型:美图在线 */
	public static final int TYPE_IMAGES_ON_LINE = 0x05;
	/** 新闻接口类型：操盘解读 */
	public static final int TYPE_TRADER_EXPLAIN = 0x06;
	/** 新闻接口类型：时事聚焦 */
	public static final int TYPE_FOCUS = 0x07;


	/**
	 * 获取黙认列表
	 * 
	 * @return null:失败
	 */
	public abstract List<T> getListDefault();

	/**
	 * 获取更多
	 * 
	 * @param minId
	 *            最小的id
	 * @return null:失败
	 */
	public abstract List<T> getMore(int minId);

	/**
	 * 刷新
	 * 
	 * @param maxId
	 *            最大的id
	 * @return null:失败
	 */
	public abstract List<T> getFresh(int maxId);

	/**
	 * 获取数据更新
	 * 
	 * @param lastTime
	 *            上次更新时间
	 * @return null:失败
	 */
	public abstract List<T> getUpdate(String lastTime);

	/** 获取新闻接口类型 */
	public abstract int getType();

	/**
	 * 设置新闻类型
	 * 
	 * @param type
	 *            详细参见：NewsListBase.TYPE_xxx 或今日快讯栏目名，本方法为扩展今日快讯有栏目栏找新闻而设
	 */
	public void setType(int type) {

	}
}
