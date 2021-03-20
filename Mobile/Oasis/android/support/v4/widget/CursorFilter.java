package android.support.v4.widget;

import android.database.Cursor;
import android.widget.Filter;
import android.widget.Filter.FilterResults;

class CursorFilter extends Filter {
	CursorFilterClient mClient;

	static interface CursorFilterClient {
		public void changeCursor(Cursor r1_Cursor);

		public CharSequence convertToString(Cursor r1_Cursor);

		public Cursor getCursor();

		public Cursor runQueryOnBackgroundThread(CharSequence r1_CharSequence);
	}


	CursorFilter(CursorFilterClient client) {
		super();
		mClient = client;
	}

	public CharSequence convertResultToString(Object resultValue) {
		return mClient.convertToString((Cursor) resultValue);
	}

	protected FilterResults performFiltering(CharSequence constraint) {
		Cursor cursor = mClient.runQueryOnBackgroundThread(constraint);
		FilterResults results = new FilterResults();
		if (cursor != null) {
			results.count = cursor.getCount();
			results.values = cursor;
			return results;
		} else {
			results.count = 0;
			results.values = null;
			return results;
		}
	}

	protected void publishResults(CharSequence constraint, FilterResults results) {
		Cursor oldCursor = mClient.getCursor();
		if (results.values == null || results.values == oldCursor) {
		} else {
			mClient.changeCursor((Cursor) results.values);
		}
	}
}
