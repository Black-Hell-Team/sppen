package android.support.v4.content;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.WindowCompat;
import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class ModernAsyncTask<Params, Progress, Result> {
	private static final int CORE_POOL_SIZE = 5;
	private static final int KEEP_ALIVE = 1;
	private static final String LOG_TAG = "AsyncTask";
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int MESSAGE_POST_PROGRESS = 2;
	private static final int MESSAGE_POST_RESULT = 1;
	public static final Executor THREAD_POOL_EXECUTOR;
	private static volatile Executor sDefaultExecutor;
	private static final InternalHandler sHandler;
	private static final BlockingQueue<Runnable> sPoolWorkQueue;
	private static final ThreadFactory sThreadFactory;
	private final FutureTask<Result> mFuture;
	private volatile Status mStatus;
	private final AtomicBoolean mTaskInvoked;
	private final WorkerRunnable<Params, Result> mWorker;

	static class AnonymousClass_1 implements ThreadFactory {
		private final AtomicInteger mCount;

		AnonymousClass_1() {
			super();
			mCount = new AtomicInteger(1);
		}

		public Thread newThread(Runnable r) {
			return new Thread(r, "ModernAsyncTask #" + mCount.getAndIncrement());
		}
	}

	class AnonymousClass_3 extends FutureTask<Result> {
		final /* synthetic */ ModernAsyncTask this$0;

		AnonymousClass_3(ModernAsyncTask r1_ModernAsyncTask, Callable x0) {
			super(x0);
			this$0 = r1_ModernAsyncTask;
		}

		protected void done() {
			try {
				this$0.postResultIfNotInvoked(get());
			} catch (InterruptedException e) {
				Log.w(LOG_TAG, e);
				return;
			} catch (ExecutionException e_2) {
				throw new RuntimeException("An error occured while executing doInBackground()", e_2.getCause());
			} catch (CancellationException e_3) {
				this$0.postResultIfNotInvoked(null);
				return;
			}
		}
	}

	static /* synthetic */ class AnonymousClass_4 {
		static final /* synthetic */ int[] $SwitchMap$android$support$v4$content$ModernAsyncTask$Status;

		static {
			$SwitchMap$android$support$v4$content$ModernAsyncTask$Status = new int[ModernAsyncTask.Status.values().length];
			try {
				$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[ModernAsyncTask.Status.RUNNING.ordinal()] = 1;
			} catch (NoSuchFieldError e) {
			}
			$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[ModernAsyncTask.Status.FINISHED.ordinal()] = 2;
		}
	}

	private static class AsyncTaskResult<Data> {
		final Data[] mData;
		final ModernAsyncTask mTask;

		AsyncTaskResult(ModernAsyncTask task, Data ... data) {
			super();
			mTask = task;
			mData = data;
		}
	}

	private static class InternalHandler extends Handler {
		private InternalHandler() {
			super();
		}

		/* synthetic */ InternalHandler(ModernAsyncTask.AnonymousClass_1 x0) {
			this();
		}

		public void handleMessage(Message msg) {
			ModernAsyncTask.AsyncTaskResult result = (ModernAsyncTask.AsyncTaskResult) msg.obj;
			switch(msg.what) {
			case MESSAGE_POST_RESULT:
				result.mTask.finish(result.mData[0]);
			case MESSAGE_POST_PROGRESS:
				result.mTask.onProgressUpdate(result.mData);
			}
		}
	}

	public static enum Status {
		PENDING,
		RUNNING,
		FINISHED;

	}

	private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
		Params[] mParams;

		private WorkerRunnable() {
			super();
		}

		/* synthetic */ WorkerRunnable(ModernAsyncTask.AnonymousClass_1 x0) {
			this();
		}
	}

	class AnonymousClass_2 extends ModernAsyncTask.WorkerRunnable<Params, Result> {
		final /* synthetic */ ModernAsyncTask this$0;

		AnonymousClass_2(ModernAsyncTask r2_ModernAsyncTask) {
			super(null);
			this$0 = r2_ModernAsyncTask;
		}

		public Result call() throws Exception {
			this$0.mTaskInvoked.set(true);
			Process.setThreadPriority(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
			return this$0.postResult(this$0.doInBackground(mParams));
		}
	}


	static {
		sThreadFactory = new AnonymousClass_1();
		sPoolWorkQueue = new LinkedBlockingQueue(10);
		THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(5, 128, 1, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
		sHandler = new InternalHandler(null);
		sDefaultExecutor = THREAD_POOL_EXECUTOR;
	}

	public ModernAsyncTask() {
		super();
		mStatus = Status.PENDING;
		mTaskInvoked = new AtomicBoolean();
		mWorker = new AnonymousClass_2(this);
		mFuture = new AnonymousClass_3(this, mWorker);
	}

	public static void execute(Runnable runnable) {
		sDefaultExecutor.execute(runnable);
	}

	private void finish(Result result) {
		if (isCancelled()) {
			onCancelled(result);
		} else {
			onPostExecute(result);
		}
		mStatus = Status.FINISHED;
	}

	public static void init() {
		sHandler.getLooper();
	}

	private Result postResult(Result result) {
		Object[] r3_Object_A = new Object[1];
		r3_Object_A[0] = result;
		sHandler.obtainMessage(MESSAGE_POST_RESULT, new AsyncTaskResult(this, r3_Object_A)).sendToTarget();
		return result;
	}

	private void postResultIfNotInvoked(Result result) {
		if (!mTaskInvoked.get()) {
			postResult(result);
		}
	}

	public static void setDefaultExecutor(Executor exec) {
		sDefaultExecutor = exec;
	}

	public final boolean cancel(boolean mayInterruptIfRunning) {
		return mFuture.cancel(mayInterruptIfRunning);
	}

	protected abstract Result doInBackground(Params ... r1_Params_A);

	public final ModernAsyncTask<Params, Progress, Result> execute(Params ... params) {
		return executeOnExecutor(sDefaultExecutor, params);
	}

	public final ModernAsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params ... params) {
		if (mStatus != Status.PENDING) {
			switch(AnonymousClass_4.$SwitchMap$android$support$v4$content$ModernAsyncTask$Status[mStatus.ordinal()]) {
			case MESSAGE_POST_RESULT:
				throw new IllegalStateException("Cannot execute task: the task is already running.");
			case MESSAGE_POST_PROGRESS:
				throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
			}
		}
		mStatus = Status.RUNNING;
		onPreExecute();
		mWorker.mParams = params;
		exec.execute(mFuture);
		return this;
	}

	public final Result get() throws InterruptedException, ExecutionException {
		return mFuture.get();
	}

	public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return mFuture.get(timeout, unit);
	}

	public final Status getStatus() {
		return mStatus;
	}

	public final boolean isCancelled() {
		return mFuture.isCancelled();
	}

	protected void onCancelled() {
	}

	protected void onCancelled(Result result) {
		onCancelled();
	}

	protected void onPostExecute(Result result) {
	}

	protected void onPreExecute() {
	}

	protected void onProgressUpdate(Progress ... values) {
	}

	protected final void publishProgress(Progress ... values) {
		if (!isCancelled()) {
			sHandler.obtainMessage(MESSAGE_POST_PROGRESS, new AsyncTaskResult(this, values)).sendToTarget();
		}
	}
}
