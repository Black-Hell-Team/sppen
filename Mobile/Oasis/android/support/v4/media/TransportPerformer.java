package android.support.v4.media;

import android.os.SystemClock;
import android.view.KeyEvent;

public abstract class TransportPerformer {
	static final int AUDIOFOCUS_GAIN = 1;
	static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
	static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
	static final int AUDIOFOCUS_LOSS = -1;
	static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
	static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

	public TransportPerformer() {
		super();
	}

	public void onAudioFocusChange(int focusChange) {
		int keyCode = 0;
		switch(focusChange) {
		case AUDIOFOCUS_LOSS:
			keyCode = TransportMediator.KEYCODE_MEDIA_PAUSE;
			break;
		}
		if (keyCode != 0) {
			long now = SystemClock.uptimeMillis();
			onMediaButtonDown(keyCode, new KeyEvent(now, now, 0, keyCode, 0));
			onMediaButtonUp(keyCode, new KeyEvent(now, now, 1, keyCode, 0));
		}
	}

	public int onGetBufferPercentage() {
		return 100;
	}

	public abstract long onGetCurrentPosition();

	public abstract long onGetDuration();

	public int onGetTransportControlFlags() {
		return 60;
	}

	public abstract boolean onIsPlaying();

	public boolean onMediaButtonDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case 79:
		case 85:
			if (onIsPlaying()) {
				onPause();
				return true;
			} else {
				onStart();
				return true;
			}
		case 86:
			onStop();
			return true;
		case TransportMediator.KEYCODE_MEDIA_PLAY:
			onStart();
			return true;
		case TransportMediator.KEYCODE_MEDIA_PAUSE:
			onPause();
			return true;
		}
		return true;
	}

	public boolean onMediaButtonUp(int keyCode, KeyEvent event) {
		return true;
	}

	public abstract void onPause();

	public abstract void onSeekTo(long r1j);

	public abstract void onStart();

	public abstract void onStop();
}
