package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import java.util.ArrayList;

public class TransportMediator extends TransportController {
	public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
	public static final int FLAG_KEY_MEDIA_NEXT = 128;
	public static final int FLAG_KEY_MEDIA_PAUSE = 16;
	public static final int FLAG_KEY_MEDIA_PLAY = 4;
	public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
	public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
	public static final int FLAG_KEY_MEDIA_REWIND = 2;
	public static final int FLAG_KEY_MEDIA_STOP = 32;
	public static final int KEYCODE_MEDIA_PAUSE = 127;
	public static final int KEYCODE_MEDIA_PLAY = 126;
	public static final int KEYCODE_MEDIA_RECORD = 130;
	final AudioManager mAudioManager;
	final TransportPerformer mCallbacks;
	final Context mContext;
	final TransportMediatorJellybeanMR2 mController;
	final Object mDispatcherState;
	final Callback mKeyEventCallback;
	final ArrayList<TransportStateListener> mListeners;
	final TransportMediatorCallback mTransportKeyCallback;
	final View mView;

	class AnonymousClass_2 implements Callback {
		final /* synthetic */ TransportMediator this$0;

		AnonymousClass_2(TransportMediator r1_TransportMediator) {
			super();
			this$0 = r1_TransportMediator;
		}

		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (TransportMediator.isMediaKey(keyCode)) {
				return this$0.mCallbacks.onMediaButtonDown(keyCode, event);
			} else {
				return false;
			}
		}

		public boolean onKeyLongPress(int keyCode, KeyEvent event) {
			return false;
		}

		public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
			return false;
		}

		public boolean onKeyUp(int keyCode, KeyEvent event) {
			if (TransportMediator.isMediaKey(keyCode)) {
				return this$0.mCallbacks.onMediaButtonUp(keyCode, event);
			} else {
				return false;
			}
		}
	}

	class AnonymousClass_1 implements TransportMediatorCallback {
		final /* synthetic */ TransportMediator this$0;

		AnonymousClass_1(TransportMediator r1_TransportMediator) {
			super();
			this$0 = r1_TransportMediator;
		}

		public long getPlaybackPosition() {
			return this$0.mCallbacks.onGetCurrentPosition();
		}

		public void handleAudioFocusChange(int focusChange) {
			this$0.mCallbacks.onAudioFocusChange(focusChange);
		}

		public void handleKey(KeyEvent key) {
			key.dispatch(this$0.mKeyEventCallback);
		}

		public void playbackPositionUpdate(long newPositionMs) {
			this$0.mCallbacks.onSeekTo(newPositionMs);
		}
	}


	public TransportMediator(Activity activity, TransportPerformer callbacks) {
		this(activity, null, callbacks);
	}

	private TransportMediator(Activity activity, View view, TransportPerformer callbacks) {
		Context r0_Context;
		super();
		mListeners = new ArrayList();
		mTransportKeyCallback = new AnonymousClass_1(this);
		mKeyEventCallback = new AnonymousClass_2(this);
		if (activity != null) {
			r0_Context = activity;
		} else {
			r0_Context = view.getContext();
		}
		mContext = r0_Context;
		mCallbacks = callbacks;
		mAudioManager = (AudioManager) mContext.getSystemService("audio");
		if (activity != null) {
			view = activity.getWindow().getDecorView();
		}
		mView = view;
		mDispatcherState = KeyEventCompat.getKeyDispatcherState(mView);
		if (VERSION.SDK_INT >= 18) {
			mController = new TransportMediatorJellybeanMR2(mContext, mAudioManager, mView, mTransportKeyCallback);
		} else {
			mController = null;
		}
	}

	public TransportMediator(View view, TransportPerformer callbacks) {
		this(null, view, callbacks);
	}

	private TransportStateListener[] getListeners() {
		if (mListeners.size() <= 0) {
			return null;
		} else {
			TransportStateListener[] listeners = new TransportStateListener[mListeners.size()];
			mListeners.toArray(listeners);
			return listeners;
		}
	}

	static boolean isMediaKey(int keyCode) {
		switch(keyCode) {
		case 79:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89:
		case 90:
		case 91:
		case KEYCODE_MEDIA_PLAY:
		case KEYCODE_MEDIA_PAUSE:
		case KEYCODE_MEDIA_RECORD:
			return true;
		}
		return false;
	}

	private void pushControllerState() {
		if (mController != null) {
			mController.refreshState(mCallbacks.onIsPlaying(), mCallbacks.onGetCurrentPosition(), mCallbacks.onGetTransportControlFlags());
		}
	}

	private void reportPlayingChanged() {
		TransportStateListener[] listeners = getListeners();
		if (listeners != null) {
			TransportStateListener[] arr$ = listeners;
			int i$ = 0;
			while (i$ < arr$.length) {
				arr$[i$].onPlayingChanged(this);
				i$++;
			}
		}
	}

	private void reportTransportControlsChanged() {
		TransportStateListener[] listeners = getListeners();
		if (listeners != null) {
			TransportStateListener[] arr$ = listeners;
			int i$ = 0;
			while (i$ < arr$.length) {
				arr$[i$].onTransportControlsChanged(this);
				i$++;
			}
		}
	}

	public void destroy() {
		mController.destroy();
	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		return KeyEventCompat.dispatch(event, mKeyEventCallback, mDispatcherState, this);
	}

	public int getBufferPercentage() {
		return mCallbacks.onGetBufferPercentage();
	}

	public long getCurrentPosition() {
		return mCallbacks.onGetCurrentPosition();
	}

	public long getDuration() {
		return mCallbacks.onGetDuration();
	}

	public Object getRemoteControlClient() {
		if (mController != null) {
			return mController.getRemoteControlClient();
		} else {
			return null;
		}
	}

	public int getTransportControlFlags() {
		return mCallbacks.onGetTransportControlFlags();
	}

	public boolean isPlaying() {
		return mCallbacks.onIsPlaying();
	}

	public void pausePlaying() {
		if (mController != null) {
			mController.pausePlaying();
		}
		mCallbacks.onPause();
		pushControllerState();
		reportPlayingChanged();
	}

	public void refreshState() {
		pushControllerState();
		reportPlayingChanged();
		reportTransportControlsChanged();
	}

	public void registerStateListener(TransportStateListener listener) {
		mListeners.add(listener);
	}

	public void seekTo(long pos) {
		mCallbacks.onSeekTo(pos);
	}

	public void startPlaying() {
		if (mController != null) {
			mController.startPlaying();
		}
		mCallbacks.onStart();
		pushControllerState();
		reportPlayingChanged();
	}

	public void stopPlaying() {
		if (mController != null) {
			mController.stopPlaying();
		}
		mCallbacks.onStop();
		pushControllerState();
		reportPlayingChanged();
	}

	public void unregisterStateListener(TransportStateListener listener) {
		mListeners.remove(listener);
	}
}
