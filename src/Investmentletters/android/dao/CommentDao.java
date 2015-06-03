package Investmentletters.android.dao;

import Investmentletters.android.dao.base.CommentNewBase;
import Investmentletters.android.utils.Constants;

/**
 * ∆¿¬€ dao ¿‡
 * 
 */
public class CommentDao extends CommentNewBase {

	@Override
	public String getMoreUrlFormat() {
		return Constants.URL_COMMENT;
	}

	@Override
	public String getFreshUrlFormat() {
		return Constants.URL_COMMENT;
	}
}
