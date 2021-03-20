package android.support.v4.content;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader.ForceLoadContentObserver;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

public class CursorLoader extends AsyncTaskLoader<Cursor> {
	Cursor mCursor;
	final Loader<Cursor> mObserver;
	String[] mProjection;
	String mSelection;
	String[] mSelectionArgs;
	String mSortOrder;
	Uri mUri;

	public CursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver(this);
	}

	public CursorLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		super(context);
		mObserver = new ForceLoadContentObserver(this);
		mUri = uri;
		mProjection = projection;
		mSelection = selection;
		mSelectionArgs = selectionArgs;
		mSortOrder = sortOrder;
	}

	public void deliverResult(Cursor cursor) {
		if (isReset()) {
			if (cursor != null) {
				cursor.close();
			}
		} else {
			Cursor oldCursor = mCursor;
			mCursor = cursor;
			if (isStarted()) {
				super.deliverResult(cursor);
			}
			if (oldCursor == null || oldCursor == cursor || oldCursor.isClosed()) {
			} else {
				oldCursor.close();
			}
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix);
		writer.print("mUri=");
		writer.println(mUri);
		writer.print(prefix);
		writer.print("mProjection=");
		writer.println(Arrays.toString(mProjection));
		writer.print(prefix);
		writer.print("mSelection=");
		writer.println(mSelection);
		writer.print(prefix);
		writer.print("mSelectionArgs=");
		writer.println(Arrays.toString(mSelectionArgs));
		writer.print(prefix);
		writer.print("mSortOrder=");
		writer.println(mSortOrder);
		writer.print(prefix);
		writer.print("mCursor=");
		writer.println(mCursor);
		writer.print(prefix);
		writer.print("mContentChanged=");
		writer.println(mContentChanged);
	}

	public String[] getProjection() {
		return mProjection;
	}

	public String getSelection() {
		return mSelection;
	}

	public String[] getSelectionArgs() {
		return mSelectionArgs;
	}

	public String getSortOrder() {
		return mSortOrder;
	}

	public Uri getUri() {
		return mUri;
	}

	public Cursor loadInBackground() {
		Cursor cursor = getContext().getContentResolver().query(mUri, mProjection, mSelection, mSelectionArgs, mSortOrder);
		if (cursor != null) {
			cursor.getCount();
			cursor.registerContentObserver(mObserver);
		}
		return cursor;
	}

	public void onCanceled(Cursor cursor) {
		if (cursor == null || cursor.isClosed()) {
		} else {
			cursor.close();
		}
	}

	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (mCursor == null || mCursor.isClosed()) {
			mCursor = null;
		} else {
			mCursor.close();
			mCursor = null;
		}
	}

	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	protected void onStopLoading() {
		cancelLoad();
	}

	public void setProjection(String[] projection) {
		mProjection = projection;
	}

	public void setSelection(String selection) {
		mSelection = selection;
	}

	public void setSelectionArgs(String[] selectionArgs) {
		mSelectionArgs = selectionArgs;
	}

	public void setSortOrder(String sortOrder) {
		mSortOrder = sortOrder;
	}

	public void setUri(Uri uri) {
		mUri = uri;
	}
}
