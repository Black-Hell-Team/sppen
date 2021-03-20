package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class LoaderManager {
	public static interface LoaderCallbacks<D> {
		public Loader<D> onCreateLoader(int r1i, Bundle r2_Bundle);

		public void onLoadFinished(Loader<D> r1_Loader_D, D r2_D);

		public void onLoaderReset(Loader<D> r1_Loader_D);
	}


	public LoaderManager() {
		super();
	}

	public static void enableDebugLogging(boolean enabled) {
		LoaderManagerImpl.DEBUG = enabled;
	}

	public abstract void destroyLoader(int r1i);

	public abstract void dump(String r1_String, FileDescriptor r2_FileDescriptor, PrintWriter r3_PrintWriter, String[] r4_String_A);

	public abstract <D> Loader<D> getLoader(int r1i);

	public boolean hasRunningLoaders() {
		return false;
	}

	public abstract <D> Loader<D> initLoader(int r1i, Bundle r2_Bundle, LoaderCallbacks<D> r3_LoaderCallbacks_D);

	public abstract <D> Loader<D> restartLoader(int r1i, Bundle r2_Bundle, LoaderCallbacks<D> r3_LoaderCallbacks_D);
}
