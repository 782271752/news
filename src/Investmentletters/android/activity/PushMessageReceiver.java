package Investmentletters.android.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushConstants;

/**
 * �����Ϣ֪ͨ������
 * @author liang
 */
public class PushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)){//���NofiCation
			System.out.println("���NofiCation���⣺"+intent.getExtras().getString(PushConstants.EXTRA_NOTIFICATION_TITLE));
			System.out.println("���ݣ�"+intent.getExtras().getString(PushConstants.EXTRA_NOTIFICATION_CONTENT));
			try{
				String content = intent.getExtras().getString(PushConstants.EXTRA_EXTRA);//�Զ�������
				System.out.println("�Զ������ݣ�"+content);	
				JSONObject json = new JSONObject(content);
				json = new JSONObject(json.getString("content").trim());
				JSONArray arr = json.getJSONArray("NewsId");
				int id = arr.getInt(0);//����id
				int type = arr.getInt(1);//��������
				
				Intent detailIntent = new Intent(context, PushMessageDetail.class);
				detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				detailIntent.putExtra("TYPE", type);
				detailIntent.putExtra("ID", id);
				
				context.startActivity(detailIntent);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				//ֻ�Ǵ����
				Intent openIntent = new Intent(context, InterfaceActivity.class);
				openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(openIntent);
			}
			
			//---------����start
//			Intent detailIntent = new Intent(context, PushMessageDetail.class);
//			detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			detailIntent.putExtra("TYPE", 0);
//			detailIntent.putExtra("ID", 0);
//			
//			context.startActivity(detailIntent);
			//---------����end
			
		}
	}

}
