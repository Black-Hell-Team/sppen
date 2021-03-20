package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

public abstract class AsyncTaskLoader<D> extends Loader<D> {
	static final boolean DEBUG = false;
	static final String TAG = "AsyncTaskLoader";
	volatile AsyncTaskLoader<D> mCancellingTask;
	Handler mHandler;
	long mLastLoadCompleteTime;
	volatile AsyncTaskLoader<D> mTask;
	long mUpdateThrottle;

	final class LoadTask extends ModernAsyncTask<Void, Void, D> implements Runnable {
		private CountDownLatch done;
		D result;
		final /* synthetic */ AsyncTaskLoader this$0;
		boolean waiting;

		LoadTask(AsyncTaskLoader r3_AsyncTaskLoader) {
			super();
			this$0 = r3_AsyncTaskLoader;
			done = new CountDownLatch(1);
		}

		protected D doInBackground(Void ... params) {
			result = this$0.onLoadInBackground();
			return result;
		}

		protected void onCancelled() {
			this$0.dispatchOnCancelled(this, result);
			done.countDown();
		}

		protected void onPostExecute(D data) {
			this$0.dispatchOnLoadComplete(this, data);
			done.countDown();
		}

		public void run() {
			waiting = false;
			this$0.executePendingTask();
		}
	}


	public AsyncTaskLoader(Context context) {
		super(context);
		mLastLoadCompleteTime = -10000;
	}

	public boolean cancelLoad() {
		boolean r0z = DEBUG;
		if (mTask != null) {
			if (mCancellingTask != null) {
				if (mTask.waiting) {
					mTask.waiting = false;
					mHandler.removeCallbacks(mTask);
				}
				mTask = null;
				return DEBUG;
			} else if (mTask.waiting) {
				mTask.waiting = false;
				mHandler.removeCallbacks(mTask);
				mTask = null;
				return DEBUG;
			} else {
				boolean cancelled = mTask.cancel(r0z);
				if (cancelled) {
					mCancellingTask = mTask;
				}
				mTask = null;
				return cancelled;
			}
		} else {
			return DEBUG;
		}
	}

	void dispatchOnCancelled(AsyncTaskLoader<D> task, D data) {
		onCanceled(data);
		if (mCancellingTask == task) {
			rollbackContentChanged();
			mLastLoadCompleteTime = SystemClock.uptimeMillis();
			mCancellingTask = null;
			executePendingTask();
		}
	}

	void dispatchOnLoadComplete(AsyncTaskLoader<D> task, D data) {
		if (mTask != task) {
			dispatchOnCancelled(task, data);
		} else if (isAbandoned()) {
			onCanceled(data);
		} else {
			commitContentChanged();
			mLastLoadCompleteTime = SystemClock.uptimeMillis();
			mTask = null;
			deliverResult(data);
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		super.dump(prefix, fd, writer, args);
		if (mTask != null) {
			writer.print(prefix);
			writer.print("mTask=");
			writer.print(mTask);
			writer.print(" waiting=");
			writer.println(mTask.waiting);
		}
		if (mCancellingTask != null) {
			writer.print(prefix);
			writer.print("mCancellingTask=");
			writer.print(mCancellingTask);
			writer.print(" waiting=");
			writer.println(mCancellingTask.waiting);
		}
		if (mUpdateThrottle != 0) {
			writer.print(prefix);
			writer.print("mUpdateThrottle=");
			TimeUtils.formatDuration(mUpdateThrottle, writer);
			writer.print(" mLastLoadCompleteTime=");
			TimeUtils.formatDuration(mLastLoadCompleteTime, SystemClock.uptimeMillis(), writer);
			writer.println();
		}
	}

	void executePendingTask() {
		if (mCancellingTask != null || mTask == null) {
		} else {
			if (mTask.waiting) {
				mTask.waiting = false;
				mHandler.removeCallbacks(mTask);
			}
			if (mUpdateThrottle <= 0 || SystemClock.uptimeMillis() >= mLastLoadCompleteTime + mUpdateThrottle) {
				mTask.executeOnExecutor(ModernAsyncTask.THREAD_POOL_EXECUTOR, (Void[]) false);
			} else {
				mTask.waiting = true;
				mHandler.postAtTime(mTask, mLastLoadCompleteTime + mUpdateThrottle);
			}
		}
	}

	public abstract D loadInBackground();

	public void onCanceled(D data) {
	}

	protected void onForceLoad() {
		super.onForceLoad();
		cancelLoad();
		mTask = new LoadTask(this);
		executePendingTask();
	}

	protected D onLoadInBackground() {
		return loadInBackground();
	}

	public void setUpdateThrottle(long delayMS) {
		mUpdateThrottle = delayMS;
		if (delayMS != 0) {
			mHandler = new Handler();
		}
	}

	public void waitForLoader() {
		AsyncTaskLoader<D> task = mTask;
		if (task != null) {
			try {
				task.done.await();
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
