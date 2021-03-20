package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager {
	static boolean DEBUG = false;
	static final String TAG = "LoaderManager";
	FragmentActivity mActivity;
	boolean mCreatingLoader;
	final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
	final SparseArrayCompat<LoaderInfo> mLoaders;
	boolean mRetaining;
	boolean mRetainingStarted;
	boolean mStarted;
	final String mWho;

	final class LoaderInfo implements OnLoadCompleteListener<Object> {
		final Bundle mArgs;
		LoaderCallbacks<Object> mCallbacks;
		Object mData;
		boolean mDeliveredData;
		boolean mDestroyed;
		boolean mHaveData;
		final int mId;
		boolean mListenerRegistered;
		Loader<Object> mLoader;
		LoaderManagerImpl.LoaderInfo mPendingLoader;
		boolean mReportNextStart;
		boolean mRetaining;
		boolean mRetainingStarted;
		boolean mStarted;
		final /* synthetic */ LoaderManagerImpl this$0;

		public LoaderInfo(LoaderManagerImpl r1_LoaderManagerImpl, int id, Bundle args, LoaderCallbacks<Object> callbacks) {
			super();
			this$0 = r1_LoaderManagerImpl;
			mId = id;
			mArgs = args;
			mCallbacks = callbacks;
		}

		void callOnLoadFinished(Loader<Object> loader, Object data) {
			if (mCallbacks != null) {
				String lastBecause = null;
				if (this$0.mActivity != null) {
					lastBecause = this$0.mActivity.mFragments.mNoTransactionsBecause;
					this$0.mActivity.mFragments.mNoTransactionsBecause = "onLoadFinished";
				}
				try {
					if (DEBUG) {
						Log.v(TAG, "  onLoadFinished in " + loader + ": " + loader.dataToString(data));
					}
					mCallbacks.onLoadFinished(loader, data);
					if (this$0.mActivity != null) {
						this$0.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
					}
					mDeliveredData = true;
				} catch (Throwable th) {
					if (this$0.mActivity != null) {
						this$0.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
					}
				}
			}
		}

		void destroy() {
			if (DEBUG) {
				Log.v(TAG, "  Destroying: " + this);
			}
			mDestroyed = true;
			boolean needReset = mDeliveredData;
			mDeliveredData = false;
			if (mCallbacks == null || mLoader == null || !mHaveData || !needReset) {
				mCallbacks = null;
				mData = null;
				mHaveData = false;
				if (mLoader == null) {
					if (!mListenerRegistered) {
						mListenerRegistered = false;
						mLoader.unregisterListener(this);
					}
					mLoader.reset();
				}
				if (mPendingLoader == null) {
					mPendingLoader.destroy();
				}
			} else {
				if (DEBUG) {
					Log.v(TAG, "  Reseting: " + this);
				}
				String lastBecause = null;
				if (this$0.mActivity != null) {
					lastBecause = this$0.mActivity.mFragments.mNoTransactionsBecause;
					this$0.mActivity.mFragments.mNoTransactionsBecause = "onLoaderReset";
				}
				try {
					mCallbacks.onLoaderReset(mLoader);
					if (this$0.mActivity != null) {
						this$0.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
					}
				} catch (Throwable th) {
					if (this$0.mActivity != null) {
						this$0.mActivity.mFragments.mNoTransactionsBecause = lastBecause;
					}
				}
				mCallbacks = null;
				mData = null;
				mHaveData = false;
				if (mLoader == null) {
					if (mPendingLoader == null) {
					} else {
						mPendingLoader.destroy();
					}
				} else if (!mListenerRegistered) {
					mLoader.reset();
					if (mPendingLoader == null) {
						mPendingLoader.destroy();
					}
				} else {
					mListenerRegistered = false;
					mLoader.unregisterListener(this);
					mLoader.reset();
					if (mPendingLoader == null) {
					} else {
						mPendingLoader.destroy();
					}
				}
			}
		}

		public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
			writer.print(prefix);
			writer.print("mId=");
			writer.print(mId);
			writer.print(" mArgs=");
			writer.println(mArgs);
			writer.print(prefix);
			writer.print("mCallbacks=");
			writer.println(mCallbacks);
			writer.print(prefix);
			writer.print("mLoader=");
			writer.println(mLoader);
			if (mLoader != null) {
				mLoader.dump(prefix + "  ", fd, writer, args);
			}
			if (mHaveData || mDeliveredData) {
				writer.print(prefix);
				writer.print("mHaveData=");
				writer.print(mHaveData);
				writer.print("  mDeliveredData=");
				writer.println(mDeliveredData);
				writer.print(prefix);
				writer.print("mData=");
				writer.println(mData);
			} else {
				writer.print(prefix);
				writer.print("mStarted=");
				writer.print(mStarted);
				writer.print(" mReportNextStart=");
				writer.print(mReportNextStart);
				writer.print(" mDestroyed=");
				writer.println(mDestroyed);
				writer.print(prefix);
				writer.print("mRetaining=");
				writer.print(mRetaining);
				writer.print(" mRetainingStarted=");
				writer.print(mRetainingStarted);
				writer.print(" mListenerRegistered=");
				writer.println(mListenerRegistered);
			}
			writer.print(prefix);
			writer.print("mStarted=");
			writer.print(mStarted);
			writer.print(" mReportNextStart=");
			writer.print(mReportNextStart);
			writer.print(" mDestroyed=");
			writer.println(mDestroyed);
			writer.print(prefix);
			writer.print("mRetaining=");
			writer.print(mRetaining);
			writer.print(" mRetainingStarted=");
			writer.print(mRetainingStarted);
			writer.print(" mListenerRegistered=");
			writer.println(mListenerRegistered);
			if (mPendingLoader != null) {
				writer.print(prefix);
				writer.println("Pending Loader ");
				writer.print(mPendingLoader);
				writer.println(":");
				mPendingLoader.dump(prefix + "  ", fd, writer, args);
			}
		}

		void finishRetain() {
			if (mRetaining) {
				if (DEBUG) {
					Log.v(TAG, "  Finished Retaining: " + this);
				}
				mRetaining = false;
				if (mStarted == mRetainingStarted || mStarted) {
				} else {
					stop();
				}
			}
			if (!mStarted || !mHaveData || mReportNextStart) {
			} else {
				callOnLoadFinished(mLoader, mData);
			}
		}

		public void onLoadComplete(Loader<Object> loader, Object data) {
			if (DEBUG) {
				Log.v(TAG, "onLoadComplete: " + this);
			}
			if (mDestroyed) {
				if (DEBUG) {
					Log.v(TAG, "  Ignoring load complete -- destroyed");
				}
			} else if (this$0.mLoaders.get(mId) != this) {
				if (DEBUG) {
					Log.v(TAG, "  Ignoring load complete -- not active");
				}
			} else {
				LoaderManagerImpl.LoaderInfo pending = mPendingLoader;
				if (pending != null) {
					if (DEBUG) {
						Log.v(TAG, "  Switching to pending loader: " + pending);
					}
					mPendingLoader = null;
					this$0.mLoaders.put(mId, null);
					destroy();
					this$0.installLoader(pending);
				} else {
					LoaderManagerImpl.LoaderInfo info;
					if (mData != data || !mHaveData) {
						mData = data;
						mHaveData = true;
						if (mStarted) {
							callOnLoadFinished(loader, data);
						}
					} else {
						info = (LoaderManagerImpl.LoaderInfo) this$0.mInactiveLoaders.get(mId);
					}
					info = (LoaderManagerImpl.LoaderInfo) this$0.mInactiveLoaders.get(mId);
					if (info == null || info == this) {
						if (this$0.mActivity == null || this$0.hasRunningLoaders()) {
						} else {
							this$0.mActivity.mFragments.startPendingDeferredFragments();
						}
					} else {
						info.mDeliveredData = false;
						info.destroy();
						this$0.mInactiveLoaders.remove(mId);
						if (this$0.mActivity == null || this$0.hasRunningLoaders()) {
						} else {
							this$0.mActivity.mFragments.startPendingDeferredFragments();
						}
					}
				}
			}
		}

		void reportStart() {
			if (!mStarted || !mReportNextStart) {
			} else {
				mReportNextStart = false;
				if (mHaveData) {
					callOnLoadFinished(mLoader, mData);
				}
			}
		}

		void retain() {
			if (DEBUG) {
				Log.v(TAG, "  Retaining: " + this);
			}
			mRetaining = true;
			mRetainingStarted = mStarted;
			mStarted = false;
			mCallbacks = null;
		}

		void start() {
			if (!mRetaining || !mRetainingStarted) {
				if (!mStarted) {
					mStarted = true;
					if (DEBUG) {
						Log.v(TAG, "  Starting: " + this);
					}
					if (mLoader != null || mCallbacks == null) {
						if (mLoader == null) {
							if (!mLoader.getClass().isMemberClass() || Modifier.isStatic(mLoader.getClass().getModifiers())) {
								if (mListenerRegistered) {
									mLoader.registerListener(mId, this);
									mListenerRegistered = true;
								}
								mLoader.startLoading();
							} else {
								throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + mLoader);
							}
						}
					} else {
						mLoader = mCallbacks.onCreateLoader(mId, mArgs);
						if (mLoader == null) {
						} else if (!mLoader.getClass().isMemberClass() || Modifier.isStatic(mLoader.getClass().getModifiers())) {
							if (mListenerRegistered) {
								mLoader.startLoading();
							} else {
								mLoader.registerListener(mId, this);
								mListenerRegistered = true;
								mLoader.startLoading();
							}
						} else {
							throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + mLoader);
						}
					}
				}
			} else {
				mStarted = true;
			}
		}

		void stop() {
			if (DEBUG) {
				Log.v(TAG, "  Stopping: " + this);
			}
			mStarted = false;
			if (mRetaining || mLoader == null || !mListenerRegistered) {
			} else {
				mListenerRegistered = false;
				mLoader.unregisterListener(this);
				mLoader.stopLoading();
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(64);
			sb.append("LoaderInfo{");
			sb.append(Integer.toHexString(System.identityHashCode(this)));
			sb.append(" #");
			sb.append(mId);
			sb.append(" : ");
			DebugUtils.buildShortClassTag(mLoader, sb);
			sb.append("}}");
			return sb.toString();
		}
	}


	static {
		DEBUG = false;
	}

	LoaderManagerImpl(String who, FragmentActivity activity, boolean started) {
		super();
		mLoaders = new SparseArrayCompat();
		mInactiveLoaders = new SparseArrayCompat();
		mWho = who;
		mActivity = activity;
		mStarted = started;
	}

	private LoaderInfo createAndInstallLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
		mCreatingLoader = true;
		LoaderInfo info = createLoader(id, args, callback);
		installLoader(info);
		mCreatingLoader = false;
		return info;
	}

	private LoaderInfo createLoader(int id, Bundle args, LoaderCallbacks<Object> callback) {
		LoaderInfo info = new LoaderInfo(this, id, args, callback);
		info.mLoader = callback.onCreateLoader(id, args);
		return info;
	}

	public void destroyLoader(int id) {
		if (mCreatingLoader) {
			throw new IllegalStateException("Called while creating a loader");
		} else {
			if (DEBUG) {
				Log.v(TAG, "destroyLoader in " + this + " of " + id);
			}
			int idx = mLoaders.indexOfKey(id);
			if (idx >= 0) {
				mLoaders.removeAt(idx);
				((LoaderInfo) mLoaders.valueAt(idx)).destroy();
			}
			idx = mInactiveLoaders.indexOfKey(id);
			if (idx >= 0) {
				mInactiveLoaders.removeAt(idx);
				mInactiveLoaders.valueAt(idx).destroy();
			}
			if (mActivity == null || hasRunningLoaders()) {
			} else {
				mActivity.mFragments.startPendingDeferredFragments();
			}
		}
	}

	void doDestroy() {
		int i;
		if (!mRetaining) {
			if (DEBUG) {
				Log.v(TAG, "Destroying Active in " + this);
			}
			i = mLoaders.size() - 1;
			while (i >= 0) {
				((LoaderInfo) mLoaders.valueAt(i)).destroy();
				i--;
			}
			mLoaders.clear();
		}
		if (DEBUG) {
			Log.v(TAG, "Destroying Inactive in " + this);
		}
		i = mInactiveLoaders.size() - 1;
		while (i >= 0) {
			((LoaderInfo) mInactiveLoaders.valueAt(i)).destroy();
			i--;
		}
		mInactiveLoaders.clear();
	}

	void doReportNextStart() {
		int i = mLoaders.size() - 1;
		while (i >= 0) {
			((LoaderInfo) mLoaders.valueAt(i)).mReportNextStart = true;
			i--;
		}
	}

	void doReportStart() {
		int i = mLoaders.size() - 1;
		while (i >= 0) {
			((LoaderInfo) mLoaders.valueAt(i)).reportStart();
			i--;
		}
	}

	void doRetain() {
		if (DEBUG) {
			Log.v(TAG, "Retaining in " + this);
		}
		if (!mStarted) {
			Throwable e = new RuntimeException("here");
			e.fillInStackTrace();
			Log.w(TAG, "Called doRetain when not started: " + this, e);
		} else {
			mRetaining = true;
			mStarted = false;
			int i = mLoaders.size() - 1;
			while (i >= 0) {
				((LoaderInfo) mLoaders.valueAt(i)).retain();
				i--;
			}
		}
	}

	void doStart() {
		if (DEBUG) {
			Log.v(TAG, "Starting in " + this);
		}
		if (mStarted) {
			Throwable e = new RuntimeException("here");
			e.fillInStackTrace();
			Log.w(TAG, "Called doStart when already started: " + this, e);
		} else {
			mStarted = true;
			int i = mLoaders.size() - 1;
			while (i >= 0) {
				((LoaderInfo) mLoaders.valueAt(i)).start();
				i--;
			}
		}
	}

	void doStop() {
		if (DEBUG) {
			Log.v(TAG, "Stopping in " + this);
		}
		if (!mStarted) {
			Throwable e = new RuntimeException("here");
			e.fillInStackTrace();
			Log.w(TAG, "Called doStop when not started: " + this, e);
		} else {
			int i = mLoaders.size() - 1;
			while (i >= 0) {
				((LoaderInfo) mLoaders.valueAt(i)).stop();
				i--;
			}
			mStarted = false;
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		String innerPrefix;
		int i;
		LoaderInfo li;
		if (mLoaders.size() > 0) {
			writer.print(prefix);
			writer.println("Active Loaders:");
			innerPrefix = prefix + "    ";
			i = 0;
			while (i < mLoaders.size()) {
				li = (LoaderInfo) mLoaders.valueAt(i);
				writer.print(prefix);
				writer.print("  #");
				writer.print(mLoaders.keyAt(i));
				writer.print(": ");
				writer.println(li.toString());
				li.dump(innerPrefix, fd, writer, args);
				i++;
			}
		}
		if (mInactiveLoaders.size() > 0) {
			writer.print(prefix);
			writer.println("Inactive Loaders:");
			innerPrefix = prefix + "    ";
			i = 0;
			while (i < mInactiveLoaders.size()) {
				li = mInactiveLoaders.valueAt(i);
				writer.print(prefix);
				writer.print("  #");
				writer.print(mInactiveLoaders.keyAt(i));
				writer.print(": ");
				writer.println(li.toString());
				li.dump(innerPrefix, fd, writer, args);
				i++;
			}
		}
	}

	void finishRetain() {
		if (mRetaining) {
			if (DEBUG) {
				Log.v(TAG, "Finished Retaining in " + this);
			}
			mRetaining = false;
			int i = mLoaders.size() - 1;
			while (i >= 0) {
				((LoaderInfo) mLoaders.valueAt(i)).finishRetain();
				i--;
			}
		}
	}

	public <D> Loader<D> getLoader(int id) {
		if (mCreatingLoader) {
			throw new IllegalStateException("Called while creating a loader");
		} else {
			LoaderInfo loaderInfo = (LoaderInfo) mLoaders.get(id);
			if (loaderInfo != null) {
				if (loaderInfo.mPendingLoader != null) {
					return loaderInfo.mPendingLoader.mLoader;
				} else {
					return loaderInfo.mLoader;
				}
			} else {
				return null;
			}
		}
	}

	public boolean hasRunningLoaders() {
		boolean loadersRunning = false;
		int i = 0;
		while (i < mLoaders.size()) {
			int r4i;
			LoaderInfo li = (LoaderInfo) mLoaders.valueAt(i);
			if (!li.mStarted || li.mDeliveredData) {
				r4i = 0;
			} else {
				r4i = 1;
			}
			loadersRunning |= r4i;
			i++;
		}
		return loadersRunning;
	}

	public <D> Loader<D> initLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
		if (mCreatingLoader) {
			throw new IllegalStateException("Called while creating a loader");
		} else {
			LoaderInfo info = (LoaderInfo) mLoaders.get(id);
			if (DEBUG) {
				Log.v(TAG, "initLoader in " + this + ": args=" + args);
			}
			if (info == null) {
				info = createAndInstallLoader(id, args, callback);
				if (DEBUG) {
					Log.v(TAG, "  Created new loader " + info);
				}
			} else {
				if (DEBUG) {
					Log.v(TAG, "  Re-using existing loader " + info);
				}
				info.mCallbacks = callback;
			}
			if (!info.mHaveData || !mStarted) {
				return info.mLoader;
			} else {
				info.callOnLoadFinished(info.mLoader, info.mData);
				return info.mLoader;
			}
		}
	}

	void installLoader(LoaderInfo info) {
		mLoaders.put(info.mId, info);
		if (mStarted) {
			info.start();
		}
	}

	public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
		if (mCreatingLoader) {
			throw new IllegalStateException("Called while creating a loader");
		} else {
			LoaderInfo info = (LoaderInfo) mLoaders.get(id);
			if (DEBUG) {
				Log.v(TAG, "restartLoader in " + this + ": args=" + args);
			}
			if (info != null) {
				LoaderInfo inactive = (LoaderInfo) mInactiveLoaders.get(id);
				if (inactive != null) {
					if (info.mHaveData) {
						if (DEBUG) {
							Log.v(TAG, "  Removing last inactive loader: " + info);
						}
						inactive.mDeliveredData = false;
						inactive.destroy();
						info.mLoader.abandon();
						mInactiveLoaders.put(id, info);
					} else if (!info.mStarted) {
						if (DEBUG) {
							Log.v(TAG, "  Current loader is stopped; replacing");
						}
						mLoaders.put(id, null);
						info.destroy();
					} else {
						if (info.mPendingLoader != null) {
							if (DEBUG) {
								Log.v(TAG, "  Removing pending loader: " + info.mPendingLoader);
							}
							info.mPendingLoader.destroy();
							info.mPendingLoader = null;
						}
						if (DEBUG) {
							Log.v(TAG, "  Enqueuing as new pending loader");
						}
						info.mPendingLoader = createLoader(id, args, callback);
						return info.mPendingLoader.mLoader;
					}
				} else {
					if (DEBUG) {
						Log.v(TAG, "  Making last loader inactive: " + info);
					}
					info.mLoader.abandon();
					mInactiveLoaders.put(id, info);
				}
			}
			return createAndInstallLoader(id, args, callback).mLoader;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("LoaderManager{");
		sb.append(Integer.toHexString(System.identityHashCode(this)));
		sb.append(" in ");
		DebugUtils.buildShortClassTag(mActivity, sb);
		sb.append("}}");
		return sb.toString();
	}

	void updateActivity(FragmentActivity activity) {
		mActivity = activity;
	}
}
