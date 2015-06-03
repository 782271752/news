package Investmentletters.android.utils;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.tencent.weibo.api.User_API;
import com.tencent.weibo.beans.Account;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.beans.QParameter;
import com.tencent.weibo.utils.Base64Encoder;
import com.tencent.weibo.utils.QHttpClient;
import com.tencent.weibo.utils.QStr;


/**
 * OAuth��֤��Ȩ�Լ�ǩ�����
 * 
 * @author <a href="http://blog.javawind.net">Xuefang Xu</a> 
 * @Data 2010-01-21
 * @Version 1.0.0
 */

public class OAuthClient {
	private static final String hashAlgorithmName = "HmacSHA1";
	private static Log log = LogFactory.getLog(OAuthClient.class);
	/**
	 * ��ȡδ��Ȩ��Request Token
	 * 
	 * @param oauth
	 * @return
	 * @throws Exception
	 */
	public OAuth requestToken(OAuth oauth) throws Exception {
		String url = "http://open.t.qq.com/cgi-bin/request_token";
		QHttpClient http = new QHttpClient();

		String queryString = getOauthParams(url, "GET",
				oauth.getOauth_consumer_secret(), "", oauth.getParams());
		 
		log.info("requestToken queryString = "+queryString);
		System.out.println("queryString:"+queryString);
		String responseData = http.httpGet(url, queryString);
		log.info("requestToken responseData = "+responseData);
		System.out.println("responseData:"+responseData);
		if (!parseToken(responseData, oauth)) {// Request Token ��Ȩ��ͨ��
			oauth.setStatus(1);
			log.info( "requestToken past !");
		}
		
		
		return oauth;
	}

	/**
	 * ʹ����Ȩ���Request Token��ȡAccess Token
	 * 
	 * @param oauth
	 * @return
	 * @throws Exception
	 */
	public OAuth accessToken(OAuth oauth) throws Exception {
		 
		log.info( "accessToken oauth.getOauth_token() = "+oauth.getOauth_token()+"oauth.getOauth_verifier() = "+oauth.getOauth_verifier());
		
		String url = "http://open.t.qq.com/cgi-bin/access_token";
		QHttpClient http = new QHttpClient();

		String queryString = getOauthParams(url, "GET",	oauth.getOauth_consumer_secret(),
				oauth.getOauth_token_secret(), oauth.getAccessParams());
		
		log.info("accessToken queryString = "+queryString);
		log.info("accessToken url = "+url);
		
		String responseData = http.httpGet(url, queryString);
		
		log.info("accessToken responseData = "+responseData);
		
		if (!parseToken(responseData, oauth)) {// Access Token ��Ȩ��ͨ��
			oauth.setStatus(2);
		}
		return oauth;
	}

	/**
	 * ��ȡ�˻�����,����ת����Accountʵ��
	 * 
	 * @param oauth
	 * @return
	 * @throws Exception
	 */
	public Account getAccount(OAuth oauth) throws Exception {
		Account account = new Account();

		String myInfo = (new User_API()).info(oauth, "xml");

		SAXReader saxReader = new SAXReader();
		Document xml = saxReader.read(new ByteArrayInputStream(myInfo.getBytes("UTF-8")));

		int ret = Integer.parseInt(xml.selectSingleNode("root/ret").getText());
		if (ret != 0) {
			return account;
		}

		String name = xml.selectSingleNode("root/data/name").getText();
		String nick = xml.selectSingleNode("root/data/nick").getText();
		String head = xml.selectSingleNode("root/data/head").getText();
		String isvip = xml.selectSingleNode("root/data/isvip").getText();
		String sex = xml.selectSingleNode("root/data/sex").getText();
		String fansnum = xml.selectSingleNode("root/data/fansnum").getText();
		String idolnum = xml.selectSingleNode("root/data/idolnum").getText();
		String tweetnum = xml.selectSingleNode("root/data/tweetnum").getText();

		account.setName(name);
		account.setNick(nick);
		account.setHead(head);
		account.setIsvip(isvip);
		account.setSex(sex);
		account.setFansnum(fansnum);
		account.setIdolnum(idolnum);
		account.setTweetnum(tweetnum);

		return account;
	}

	/**
	 * ����������� �� ����ǩ��
	 * 
	 * @param url
	 * @param httpMethod
	 * @param consumerSecret
	 * @param tokenSecrect
	 * @param parameters
	 * @return
	 */
	public String getOauthParams(String url, String httpMethod,
			String consumerSecret, String tokenSecrect,
			List<QParameter> parameters) {
		Collections.sort(parameters);

		String urlWithParameter = url;

		String parameterString = encodeParams(parameters);
		if (parameterString != null && !parameterString.equals("")) {
			urlWithParameter += "?" + parameterString;
		}

		URL aUrl = null;
		try {
			aUrl = new URL(urlWithParameter);
		} catch (MalformedURLException e) {
			System.err.println("URL parse error:" + e.getLocalizedMessage());
		}

		String signature = this.generateSignature(aUrl, consumerSecret,
				tokenSecrect, httpMethod, parameters);

		parameterString += "&oauth_signature=";
		parameterString += QStr.encode(signature);

		return parameterString;
	}

	/**
	 * ��֤Token���ؽ��
	 * 
	 * @param response
	 * @param oauth
	 * @return
	 * @throws Exception
	 */
	public boolean parseToken(String response, OAuth oauth) throws Exception {
		if (response == null || response.equals("")) {
			return false;
		}

		oauth.setMsg(response);
		String[] tokenArray = response.split("&");
		if (tokenArray.length < 2) {
			return false;
		}

		String strTokenKey = tokenArray[0];
		String strTokenSecrect = tokenArray[1];

		String[] token = strTokenKey.split("=");
		if (token.length < 2) {
			return false;
		}
		oauth.setOauth_token(token[1]);

		String[] tokenSecrect = strTokenSecrect.split("=");
		if (tokenSecrect.length < 2) {
			return false;
		}
		oauth.setOauth_token_secret(tokenSecrect[1]);
		 
		log.info( "parseToken response=>> tokenArray.length = "+tokenArray.length);
		if (tokenArray.length == 3) {
			String[] params = tokenArray[2].split("=");
			if ("name".equals(params[0]) && params.length == 2) {
//				oauth.setAccount(this.getAccount(oauth));// ��ȡ��ǰ��¼�û����˻���Ϣ
			}
		}

		return true;
	}

	/**
	 * ����ǩ��ֵ
	 * 
	 * @param url
	 * @param consumerSecret
	 * @param accessTokenSecret
	 * @param httpMethod
	 * @param parameters
	 * @return
	 */
	private String generateSignature(URL url, String consumerSecret,
			String accessTokenSecret, String httpMethod,
			List<QParameter> parameters) {
		String base = this.generateSignatureBase(url, httpMethod, parameters);
		return this.generateSignature(base, consumerSecret, accessTokenSecret);
	}

	/**
	 * ����ǩ��ֵ
	 * 
	 * @param base
	 * @param consumerSecret
	 * @param accessTokenSecret
	 * @return
	 */
	private String generateSignature(String base, String consumerSecret,
			String accessTokenSecret) {
		try {
			Mac mac = Mac.getInstance(hashAlgorithmName);
			String oauthSignature = QStr.encode(consumerSecret)
					+ "&"
					+ ((accessTokenSecret == null) ? "" : QStr
							.encode(accessTokenSecret));
			SecretKeySpec spec = new SecretKeySpec(oauthSignature.getBytes(),
					hashAlgorithmName);
			mac.init(spec);
			byte[] bytes = mac.doFinal(base.getBytes());
			return new String(Base64Encoder.encode(bytes));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * ����ǩ������Ͳ���
	 * 
	 * @param url
	 * @param httpMethod
	 * @param parameters
	 * @return
	 */
	private String generateSignatureBase(URL url, String httpMethod,
			List<QParameter> parameters) {

		StringBuilder base = new StringBuilder();
		base.append(httpMethod.toUpperCase());
		base.append("&");
		base.append(QStr.encode(getNormalizedUrl(url)));
		base.append("&");
		base.append(QStr.encode(encodeParams(parameters)));

		return base.toString();
	}

	/**
	 * ��������URL
	 * 
	 * @param url
	 * @return
	 */
	private static String getNormalizedUrl(URL url) {
		try {
			StringBuilder buf = new StringBuilder();
			buf.append(url.getProtocol());
			buf.append("://");
			buf.append(url.getHost());
			if ((url.getProtocol().equals("http") || url.getProtocol().equals(
					"https"))
					&& url.getPort() != -1) {
				buf.append(":");
				buf.append(url.getPort());
			}
			buf.append(url.getPath());
			return buf.toString();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * �����������
	 * 
	 * @param params
	 * @return
	 */
	private static String encodeParams(List<QParameter> params) {
		StringBuilder result = new StringBuilder();
		for (QParameter param : params) {
			if (result.length() != 0) {
				result.append("&");
			}
			result.append(QStr.encode(param.getName()));
			result.append("=");
			result.append(QStr.encode(param.getValue()));
		}
		return result.toString();
	}

}
