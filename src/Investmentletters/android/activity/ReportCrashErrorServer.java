package Investmentletters.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import Investmentletters.android.utils.Constants;
import Investmentletters.android.utils.Http;
import Investmentletters.android.utils.Utils;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

public class ReportCrashErrorServer extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Bundle data = intent.getExtras();
		
		if(data != null){
			String brand = data.getString("brand");
			String model = data.getString("model");
			String release = data.getString("release");
			String errorMsg = data.getString("error_msg");
			String apkVersion = Utils.getVersionName(this);
			
			new AsyncTask<String, Void, Void>(){

				@Override
				protected Void doInBackground(String... params) {
					// TODO Auto-generated method stub
					
					Http http = new Http();
					final int MAX_FAILURE = 3;
					
					List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
					httpParams.add(new BasicNameValuePair("brand", params[0]));
					httpParams.add(new BasicNameValuePair("Model", params[1]));
					httpParams.add(new BasicNameValuePair("release", params[2]));
					httpParams.add(new BasicNameValuePair("error_msg", params[3]));
					httpParams.add(new BasicNameValuePair("software_version", params[4]));
					
					for(int i=0 ; i<MAX_FAILURE ; i++){
						String res = http.doPost(Constants.URL_REPORT_ERROR, httpParams);
						if(res == null){
							continue;
						}
						
						if(res.trim().equals("1")){
							System.out.println("提交成功");
							break;
						}else {
							System.out.println("提交失败..."+i+"  "+res);
						}
						
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					return null;
				}
				
				protected void onPostExecute(Void result) {
					stopSelf();
				};
				
			}.execute(brand , model , release , errorMsg , apkVersion);
			
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

}
