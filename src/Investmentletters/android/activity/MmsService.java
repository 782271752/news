package Investmentletters.android.activity;

import Investmentletters.android.utils.Constants;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.liang.MediaPlayer;
import com.liang.MediaPlayerListener;

/**
 * Mms���ŷ����� 
 * @author liang
 */
public class MmsService extends Service {

	/**�������*/
	public static final int PLAY = 0;
	/**���ֹͣ*/
	public static final int STOP = 1;
	/**Intent����������*/
	public static final String INTENT_COMMAND = "COMMAND";
	/**mms������,���ձ�Ӧ��fragment�ص㣬��Ҫ������Ϊ����ģʽ*/
	private MediaPlayer player = null;
	
	/**��ǰ����*/
	private int command = -1;
	
	/**�绰״̬�����������ڻ�ȡ��ǰ�绰״̬������������¼�*/
	private StateListner stateListner = null;
	
	/**mms���ż�����*/
	private MediaPlayerListener mediaPlayerListener = null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = MediaPlayer.getInstance();
		player.setDataSources(Constants.MMS_URL);
		
		mediaPlayerListener = new MediaPlayerListener() {
			
			@Override
			public void onPrePared(MediaPlayer paramMediaPlayer) {
				// TODO Auto-generated method stub
				paramMediaPlayer.play();
			}
			
			@Override
			public void onPlay(byte[] paramArrayOfByte) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int paramInt) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDestroy() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBuffer(int paramInt) {
				// TODO Auto-generated method stub
				
			}
		};
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		command = intent.getExtras().getInt(INTENT_COMMAND, -1);
		switch(command){
			case PLAY://��ʼ�㲥
				if(player.getMediaPlayerListener() == null){
					player.setListener(mediaPlayerListener);
				}
				player.prapare();
				break;
				
			case STOP://ֹͣ�ز�
				player.stop();
				break;
			
			default:
				break;
		}
		
		if(stateListner == null){
			stateListner = new StateListner();
			stateListner.execute();
		}
		
		return START_REDELIVER_INTENT;
	}
	
	
	/**
	 * �绰״̬�����������ڻ�ȡ��ǰ�绰״̬������������¼�
	 * @author liang
	 */
	private class StateListner extends AsyncTask<Void, Void, Void>{

		private MediaPlayer player = null;
		private TelephonyManager tm = null;
		
		public StateListner(){
			tm = (TelephonyManager)MmsService.this.getSystemService(Service.TELEPHONY_SERVICE);
			player = MediaPlayer.getInstance();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			while(command == PLAY){
				switch(tm.getCallState()){
					case TelephonyManager.CALL_STATE_IDLE://������
						if(player.getStatus() == MediaPlayer.STATUS_STOP){
							if(player.getMediaPlayerListener() == null){
								player.setListener(mediaPlayerListener);
							}
							player.prapare();							
						}
						break;
						
					case TelephonyManager.CALL_STATE_OFFHOOK://ͨ����
						player.stop();
						break;
						
					case TelephonyManager.CALL_STATE_RINGING: //������
						player.stop();
						break;
					
					default:
						break;
				}
				
				try{
					Thread.sleep(300);
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			player.stop();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stateListner = null;
		}
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		command = STOP;
		player.stop();
	}

}
