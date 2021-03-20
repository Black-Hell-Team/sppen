package android.support.v4.content;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.support.v4.util.DebugUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Loader<D> {
	boolean mAbandoned;
	boolean mContentChanged;
	Context mContext;
	int mId;
	OnLoadCompleteListener<D> mListener;
	boolean mProcessingChange;
	boolean mReset;
	boolean mStarted;

	public final class ForceLoadContentObserver extends ContentObserver {
		final /* synthetic */ Loader this$0;

		public ForceLoadContentObserver(Loader r2_Loader) {
			super(new Handler());
			this$0 = r2_Loader;
		}

		public boolean deliverSelfNotifications() {
			return true;
		}

		public void onChange(boolean selfChange) {
			this$0.onContentChanged();
		}
	}

	public static interface OnLoadCompleteListener<D> {
		public void onLoadComplete(Loader<D> r1_Loader_D, D r2_D);
	}


	public Loader(Context context) {
		super();
		mStarted = false;
		mAbandoned = false;
		mReset = true;
		mContentChanged = false;
		mProcessingChange = false;
		mContext = context.getApplicationContext();
	}

	public void abandon() {
		mAbandoned = true;
		onAbandon();
	}

	public void commitContentChanged() {
		mProcessingChange = false;
	}

	public String dataToString(D data) {
		StringBuilder sb = new StringBuilder(64);
		DebugUtils.buildShortClassTag(data, sb);
		sb.append("}");
		return sb.toString();
	}

	public void deliverResult(D data) {
		if (mListener != null) {
			mListener.onLoadComplete(this, data);
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		writer.print(prefix);
		writer.print("mId=");
		writer.print(mId);
		writer.print(" mListener=");
		writer.println(mListener);
		if (mStarted || mContentChanged || mProcessingChange) {
			writer.print(prefix);
			writer.print("mStarted=");
			writer.print(mStarted);
			writer.print(" mContentChanged=");
			writer.print(mContentChanged);
			writer.print(" mProcessingChange=");
			writer.println(mProcessingChange);
		}
		if (mAbandoned || mReset) {
			writer.print(prefix);
			writer.print("mAbandoned=");
			writer.print(mAbandoned);
			writer.print(" mReset=");
			writer.println(mReset);
		}
	}

	public void forceLoad() {
		onForceLoad();
	}

	public Context getContext() {
		return mContext;
	}

	public int getId() {
		return mId;
	}

	public boolean isAbandoned() {
		return mAbandoned;
	}

	public boolean isReset() {
		return mReset;
	}

	public boolean isStarted() {
		return mStarted;
	}

	protected void onAbandon() {
	}

	public void onContentChanged() {
		if (mStarted) {
			forceLoad();
		} else {
			mContentChanged = true;
		}
	}

	protected void onForceLoad() {
	}

	protected void onReset() {
	}

	protected void onStartLoading() {
	}

	protected void onStopLoading() {
	}

	public void registerListener(int id, OnLoadCompleteListener<D> listener) {
		if (mListener != null) {
			throw new IllegalStateException("There is already a listener registered");
		} else {
			mListener = listener;
			mId = id;
		}
	}

	public void reset() {
		onReset();
		mReset = true;
		mStarted = false;
		mAbandoned = false;
		mContentChanged = false;
		mProcessingChange = false;
	}

	public void rollbackContentChanged() {
		if (mProcessingChange) {
			mContentChanged = true;
		}
	}

	public final void startLoading() {
		mStarted = true;
		mReset = false;
		mAbandoned = false;
		onStartLoading();
	}

	public void stopLoading() {
		mStarted = false;
		onStopLoading();
	}

	public boolean takeContentChanged() {
		boolean res = mContentChanged;
		mContentChanged = false;
		mProcessingChange |= res;
		return res;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		DebugUtils.buildShortClassTag(this, sb);
		sb.append(" id=");
		sb.append(mId);
		sb.append("}");
		return sb.toString();
	}

	public void unregisterListener(OnLoadCompleteListener<D> listener) {
		if (mListener == null) {
			throw new IllegalStateException("No listener register");
		} else if (mListener != listener) {
			throw new IllegalArgumentException("Attempting to unregister the wrong listener");
		} else {
			mListener = null;
		}
	}
}
