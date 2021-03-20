package android.support.v4.media;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.RemoteControlClient;
import android.media.RemoteControlClient.OnGetPlaybackPositionListener;
import android.media.RemoteControlClient.OnPlaybackPositionUpdateListener;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnWindowAttachListener;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;

class TransportMediatorJellybeanMR2 implements OnGetPlaybackPositionListener, OnPlaybackPositionUpdateListener {
	OnAudioFocusChangeListener mAudioFocusChangeListener;
	boolean mAudioFocused;
	final AudioManager mAudioManager;
	final Context mContext;
	boolean mFocused;
	final Intent mIntent;
	final BroadcastReceiver mMediaButtonReceiver;
	PendingIntent mPendingIntent;
	int mPlayState;
	final String mReceiverAction;
	final IntentFilter mReceiverFilter;
	RemoteControlClient mRemoteControl;
	final View mTargetView;
	final TransportMediatorCallback mTransportCallback;
	final OnWindowAttachListener mWindowAttachListener;
	final OnWindowFocusChangeListener mWindowFocusListener;

	class AnonymousClass_1 implements OnWindowAttachListener {
		final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

		AnonymousClass_1(TransportMediatorJellybeanMR2 r1_TransportMediatorJellybeanMR2) {
			super();
			this$0 = r1_TransportMediatorJellybeanMR2;
		}

		public void onWindowAttached() {
			this$0.windowAttached();
		}

		public void onWindowDetached() {
			this$0.windowDetached();
		}
	}

	class AnonymousClass_2 implements OnWindowFocusChangeListener {
		final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

		AnonymousClass_2(TransportMediatorJellybeanMR2 r1_TransportMediatorJellybeanMR2) {
			super();
			this$0 = r1_TransportMediatorJellybeanMR2;
		}

		public void onWindowFocusChanged(boolean hasFocus) {
			if (hasFocus) {
				this$0.gainFocus();
			} else {
				this$0.loseFocus();
			}
		}
	}

	class AnonymousClass_3 extends BroadcastReceiver {
		final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

		AnonymousClass_3(TransportMediatorJellybeanMR2 r1_TransportMediatorJellybeanMR2) {
			super();
			this$0 = r1_TransportMediatorJellybeanMR2;
		}

		public void onReceive(Context context, Intent intent) {
			try {
				this$0.mTransportCallback.handleKey((KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
			} catch (ClassCastException e) {
				Log.w("TransportController", e);
				return;
			}
		}
	}

	class AnonymousClass_4 implements OnAudioFocusChangeListener {
		final /* synthetic */ TransportMediatorJellybeanMR2 this$0;

		AnonymousClass_4(TransportMediatorJellybeanMR2 r1_TransportMediatorJellybeanMR2) {
			super();
			this$0 = r1_TransportMediatorJellybeanMR2;
		}

		public void onAudioFocusChange(int focusChange) {
			this$0.mTransportCallback.handleAudioFocusChange(focusChange);
		}
	}


	public TransportMediatorJellybeanMR2(Context context, AudioManager audioManager, View view, TransportMediatorCallback transportCallback) {
		super();
		mWindowAttachListener = new AnonymousClass_1(this);
		mWindowFocusListener = new AnonymousClass_2(this);
		mMediaButtonReceiver = new AnonymousClass_3(this);
		mAudioFocusChangeListener = new AnonymousClass_4(this);
		mPlayState = 0;
		mContext = context;
		mAudioManager = audioManager;
		mTargetView = view;
		mTransportCallback = transportCallback;
		mReceiverAction = context.getPackageName() + ":transport:" + System.identityHashCode(this);
		mIntent = new Intent(mReceiverAction);
		mIntent.setPackage(context.getPackageName());
		mReceiverFilter = new IntentFilter();
		mReceiverFilter.addAction(mReceiverAction);
		mTargetView.getViewTreeObserver().addOnWindowAttachListener(mWindowAttachListener);
		mTargetView.getViewTreeObserver().addOnWindowFocusChangeListener(mWindowFocusListener);
	}

	public void destroy() {
		windowDetached();
		mTargetView.getViewTreeObserver().removeOnWindowAttachListener(mWindowAttachListener);
		mTargetView.getViewTreeObserver().removeOnWindowFocusChangeListener(mWindowFocusListener);
	}

	void dropAudioFocus() {
		if (mAudioFocused) {
			mAudioFocused = false;
			mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
		}
	}

	void gainFocus() {
		if (!mFocused) {
			mFocused = true;
			mAudioManager.registerMediaButtonEventReceiver(mPendingIntent);
			mAudioManager.registerRemoteControlClient(mRemoteControl);
			if (mPlayState == WearableExtender.SIZE_MEDIUM) {
				takeAudioFocus();
			}
		}
	}

	public Object getRemoteControlClient() {
		return mRemoteControl;
	}

	void loseFocus() {
		dropAudioFocus();
		if (mFocused) {
			mFocused = false;
			mAudioManager.unregisterRemoteControlClient(mRemoteControl);
			mAudioManager.unregisterMediaButtonEventReceiver(mPendingIntent);
		}
	}

	public long onGetPlaybackPosition() {
		return mTransportCallback.getPlaybackPosition();
	}

	public void onPlaybackPositionUpdate(long newPositionMs) {
		mTransportCallback.playbackPositionUpdate(newPositionMs);
	}

	public void pausePlaying() {
		if (mPlayState == 3) {
			mPlayState = 2;
			mRemoteControl.setPlaybackState(CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		}
		dropAudioFocus();
	}

	public void refreshState(boolean playing, long position, int transportControls) {
		if (mRemoteControl != null) {
			int r1i;
			float r0f;
			RemoteControlClient r2_RemoteControlClient = mRemoteControl;
			if (playing) {
				r1i = 3;
			} else {
				r1i = 1;
			}
			if (playing) {
				r0f = 1.0f;
			} else {
				r0f = AutoScrollHelper.RELATIVE_UNSPECIFIED;
			}
			r2_RemoteControlClient.setPlaybackState(r1i, position, r0f);
			mRemoteControl.setTransportControlFlags(transportControls);
		}
	}

	public void startPlaying() {
		if (mPlayState != 3) {
			mPlayState = 3;
			mRemoteControl.setPlaybackState(WearableExtender.SIZE_MEDIUM);
		}
		if (mFocused) {
			takeAudioFocus();
		}
	}

	public void stopPlaying() {
		if (mPlayState != 1) {
			mPlayState = 1;
			mRemoteControl.setPlaybackState(1);
		}
		dropAudioFocus();
	}

	void takeAudioFocus() {
		if (!mAudioFocused) {
			mAudioFocused = true;
			mAudioManager.requestAudioFocus(mAudioFocusChangeListener, WearableExtender.SIZE_MEDIUM, 1);
		}
	}

	void windowAttached() {
		mContext.registerReceiver(mMediaButtonReceiver, mReceiverFilter);
		mPendingIntent = PendingIntent.getBroadcast(mContext, 0, mIntent, 268435456);
		mRemoteControl = new RemoteControlClient(mPendingIntent);
		mRemoteControl.setOnGetPlaybackPositionListener(this);
		mRemoteControl.setPlaybackPositionUpdateListener(this);
	}

	void windowDetached() {
		loseFocus();
		if (mPendingIntent != null) {
			mContext.unregisterReceiver(mMediaButtonReceiver);
			mPendingIntent.cancel();
			mPendingIntent = null;
			mRemoteControl = null;
		}
	}
}
