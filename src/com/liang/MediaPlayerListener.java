package com.liang;

/**
 * mms²¥·ÅÆ÷¼àÌýÆ÷
 * 
 * @author liang
 */
public abstract interface MediaPlayerListener {
	public static final int ERRNO_OPEN = 1;
	public static final int ERRNO_URL_EMPTY = 2;
	public static final int ERRNO_NOT_OPEN = 3;
	public static final int ERRNO_INIT = 4;

	public abstract void onPrePared(MediaPlayer paramMediaPlayer);

	public abstract void onPlay(byte[] paramArrayOfByte);

	public abstract void onBuffer(int paramInt);

	public abstract void onError(int paramInt);

	public abstract void onDestroy();
}