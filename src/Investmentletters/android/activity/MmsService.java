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
 * Mms播放服务器 
 * @author liang
 */
public class MmsService extends Service {

	/**命令：播放*/
	public static final int PLAY = 0;
	/**命令：停止*/
	public static final int STOP = 1;
	/**Intent参数，命令*/
	public static final String INTENT_COMMAND = "COMMAND";
	/**mms播放器,按照本应用fragment特点，需要播放器为单例模式*/
	private MediaPlayer player = null;
	
	/**当前命令*/
	private int command = -1;
	
	/**电话状态监听器，用于获取当前电话状态，并处理相关事件*/
	private StateListner stateListner = null;
	
	/**mms播放监听器*/
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
			case PLAY://开始广播
				if(player.getMediaPlayerListener() == null){
					player.setListener(mediaPlayerListener);
				}
				player.prapare();
				break;
				
			case STOP://停止关播
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
	 * 电话状态监听器，用于获取当前电话状态，并处理相关事件
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
					case TelephonyManager.CALL_STATE_IDLE://待机中
						if(player.getStatus() == MediaPlayer.STATUS_STOP){
							if(player.getMediaPlayerListener() == null){
								player.setListener(mediaPlayerListener);
							}
							player.prapare();							
						}
						break;
						
					case TelephonyManager.CALL_STATE_OFFHOOK://通话中
						player.stop();
						break;
						
					case TelephonyManager.CALL_STATE_RINGING: //铃响中
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
