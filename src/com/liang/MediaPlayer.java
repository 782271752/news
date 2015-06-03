package com.liang;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

/**
 * mms播放器,按照本应用fragment特点，需要播放器为单例模式
 * @author liang
 */
public class MediaPlayer {

	private int context = 0;
	/**mms地址*/
	private String url = null;
	/**mms播放器监听器*/
	private MediaPlayerListener listener = null;
	/**mms播放器监听器*/
	private MediaPlayerListener callerListener = null;
	/**是否可以播放*/
	private boolean isEnable = false;
	/**原始音频流播放类*/
	private AudioTrack audioTrack = null;
	/**状态：停止*/
	public static final int STATUS_STOP = 0;
	/**状态：播放*/
	public static final int STATUS_PLAY = 1;
	/**状态：准备就绪*/
	public static final int STATUS_PREPARE = 2;
	/**状态：缓冲中*/
	public static final int STATUS_CACHE = 3;
	/**当前状态*/
	private int status = 0;
	/**播放请求版本号*/
	private int requireVersion = 0;
	private Handler handler = null;

	/**what:准备就绪*/
	private final int WHAT_PREPARED = 0;
	/**what:播放*/
	private final int WHAT_PLAY = 1;
	/**what:错误*/
	private final int WHAT_ERROR = 2;
	/**what:消毁*/
	private final int WHAT_DESTROY = 3;
	/**what:缓冲*/
	private final int WHAT_CACHE = 4;

	/**缓冲的数据*/
	private List<byte[]> playData = null;

	/**线程数*/
	private int threads = 0;

	/**最大缓冲数*/
	private final int MAX_CACHE_LEN = 20;

	/**单例*/
	private static MediaPlayer instance = null;
	
	static {
		System.loadLibrary("ffmpeg");
		System.loadLibrary("mediaplayer");
	}

	@SuppressLint({ "HandlerLeak" })
	private MediaPlayer() {
		this.handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case WHAT_PREPARED:
						if(callerListener != null){
							callerListener.onPrePared(MediaPlayer.this);							
						}
						break;
						
					case WHAT_PLAY:
						if(callerListener != null){
							callerListener.onPlay((byte[]) msg.obj);							
						}
						break;
						
					case WHAT_ERROR:
						if(callerListener != null){
							callerListener.onError(((Integer) msg.obj).intValue());							
						}
						break;
						
					case WHAT_DESTROY:
						if(callerListener != null){
							callerListener.onDestroy();
						}
	
						break;
					case WHAT_CACHE:
						if(callerListener != null){
							callerListener.onBuffer(((Integer) msg.obj).intValue());		
						}
						break;
				}
			}
		};
		this.listener = new MediaPlayerListener() {
			public void onPrePared(MediaPlayer mp) {
				status = STATUS_PREPARE;
				Message msg = handler.obtainMessage();
				msg.what = WHAT_PREPARED;
				handler.sendMessage(msg);
			}

			public void onPlay(byte[] data) {
				status = STATUS_PLAY;
				Message msg = handler.obtainMessage();
				msg.what = WHAT_PLAY;
				msg.obj = data;
				handler.sendMessage(msg);
			}

			public void onError(int errNo) {
				status = STATUS_STOP;
				Message msg = handler.obtainMessage();
				msg.what = WHAT_ERROR;
				msg.obj = Integer.valueOf(errNo);
				handler.sendMessage(msg);
			}

			public void onDestroy() {
				status = STATUS_STOP;
				Message msg = handler.obtainMessage();
				msg.what = WHAT_DESTROY;
				handler.sendMessage(msg);
			}

			public void onBuffer(int percent) {
				status = STATUS_CACHE;
				Message msg = handler.obtainMessage();
				msg.what = WHAT_CACHE;
				msg.obj = Integer.valueOf(percent);
				handler.sendMessage(msg);
			}
		};
		
		playData = new ArrayList<byte[]>();
	}
	
	/**
	 * 按照本应用fragment特点，需要播放器为单例模式
	 * @return
	 */
	public static MediaPlayer getInstance(){
		if(instance == null){
			instance = new MediaPlayer();
		}
		return instance;
	}

	public void setDataSources(String url) {
		this.url = url;
	}
	
	/**生成请求版本号*/
	private void generaVersion(){
		requireVersion += 1;
		requireVersion = (requireVersion > 65530 ? 0: requireVersion);
	}

	public void prapare() {
		if (this.url == null) {
			listener.onError(MediaPlayerListener.ERRNO_URL_EMPTY);
			return;
		}

		generaVersion();//生成请求版本号

		if (status == STATUS_PREPARE) {
			if (context > 0) {
				jni_close(context);
				context = 0;
			}
			status = STATUS_STOP;
		} else if ((status == STATUS_PLAY) || (status == STATUS_CACHE)) {
			stop();
		}

		new PrepareThread(requireVersion).start();
	}

	public void play() {
		if (this.status != STATUS_PREPARE) {
			return;
		}

		if (this.context < 0) {
			this.listener.onError(3);
			return;
		}

		int sampleRateInHz = jni_getSampleInSize(this.context);

		int chenals = jni_getChanels(this.context);

		int audioMinBuffSize = 0;

		if (chenals == 1){
			audioMinBuffSize = AudioTrack.getMinBufferSize(sampleRateInHz, 4, 2);	
		}else {
			audioMinBuffSize = AudioTrack.getMinBufferSize(sampleRateInHz, 12,2);
		}

		try {
			if (chenals == 1)
				this.audioTrack = new AudioTrack(3, sampleRateInHz, 4, 2,audioMinBuffSize, 1);
			else {
				this.audioTrack = new AudioTrack(3, sampleRateInHz, 12, 2,audioMinBuffSize, 1);
			}

			this.audioTrack.play();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			this.audioTrack = null;
		}

		if (this.audioTrack == null) {
			this.listener.onError(4);
			return;
		}

		this.threads = 2;

		new Thread(new Runnable() {
			public void run() {
				while (isEnable) {
					try {
						Thread.sleep(100L);
					} catch (Exception e) {
						e.printStackTrace();
					}
					byte[] data = MediaPlayer.this
							.jni_getAudioFrame(context);

					if (data != null) {
						doData(data, false);
					}
				}

				doData(null, true);

				jni_close(context);
				context = 0;
				checkAlive();
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				boolean isBuffer = true;

				while (isEnable) {
					try {
						Thread.sleep(10L);
					} catch (Exception e) {
						e.printStackTrace();
					}

					int preBuffIndex = -1;
					int currentBuffIndex = 0;

					while ((isBuffer) && (isEnable)) {
						currentBuffIndex = playData.size();
						if (preBuffIndex != currentBuffIndex) {
							preBuffIndex = currentBuffIndex;
							int pregress = 100 * preBuffIndex / 20;
							if (pregress > 100) {
								pregress = 100;
							}
							listener.onBuffer(pregress);
						}

						if (preBuffIndex > 20) {
							isBuffer = false;
							break;
						}
						try {
							Thread.sleep(500L);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					if (isEnable) {
						byte[] data = doData(null, false);
						isBuffer = playData.size() < 1;
						if (data != null) {
							audioTrack.write(data, 0,
									data.length);
							listener.onPlay(data);
						}
					}
				}
				audioTrack.stop();
				audioTrack.release();
				audioTrack = null;
				checkAlive();
			}
		}).start();
	}

	private synchronized byte[] doData(byte[] data, boolean isClean) {
		if (isClean) {
			this.playData.clear();
			return null;
		}

		if (data == null) {
			if (this.playData.size() > 0)
				return (byte[]) this.playData.remove(0);
		} else {
			this.playData.add(data);
		}

		return null;
	}

	private synchronized void checkAlive() {
		this.threads -= 1;
		if (this.threads < 1)
			this.listener.onDestroy();
	}

	public void stop() {
		generaVersion();//生成请求版本号
		this.isEnable = false;
	}
	
	/**准备线程*/
	private class PrepareThread extends Thread{
		
		private int version = 0;
		
		public PrepareThread(int version){
			this.version = version;
		}
		
		public void run() {
			System.out.println("准备："+status);
			while ((status != STATUS_STOP || isEnable) && (version == requireVersion)) {
				System.out.println("等："+version);
				try {
					Thread.sleep(200L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (version != requireVersion) {
				System.out.println("版本不同："+requireVersion+"  "+version);
				return;
			}

			isEnable = true;

			System.out.println("开始打开："+requireVersion+"  "+version);
			context = jni_open(url);
			if (context > 0) {
				listener.onPrePared(MediaPlayer.this);
			} else
				listener.onError(1);
		}
	}

	private native int jni_open(String paramString);

	private native int jni_getSampleInSize(int paramInt);

	private native int jni_getChanels(int paramInt);

	private native byte[] jni_getAudioFrame(int paramInt);

	private native void jni_close(int paramInt);

	public synchronized void setListener(MediaPlayerListener listener) {
		this.callerListener = listener;
	}
	
	public MediaPlayerListener getMediaPlayerListener(){
		return callerListener;
	}

	public int getStatus() {
		return status;
	}
}