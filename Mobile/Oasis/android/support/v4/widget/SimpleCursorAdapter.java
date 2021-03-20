package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleCursorAdapter extends ResourceCursorAdapter {
	private CursorToStringConverter mCursorToStringConverter;
	protected int[] mFrom;
	String[] mOriginalFrom;
	private int mStringConversionColumn;
	protected int[] mTo;
	private ViewBinder mViewBinder;

	public static interface CursorToStringConverter {
		public CharSequence convertToString(Cursor r1_Cursor);
	}

	public static interface ViewBinder {
		public boolean setViewValue(View r1_View, Cursor r2_Cursor, int r3i);
	}


	@Deprecated
	public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
		super(context, layout, c);
		mStringConversionColumn = -1;
		mTo = to;
		mOriginalFrom = from;
		findColumns(from);
	}

	public SimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, flags);
		mStringConversionColumn = -1;
		mTo = to;
		mOriginalFrom = from;
		findColumns(from);
	}

	private void findColumns(String[] from) {
		if (mCursor != null) {
			int i;
			int count = from.length;
			if (mFrom == null || mFrom.length != count) {
				mFrom = new int[count];
			} else {
				i = 0;
			}
			i = 0;
			while (i < count) {
				mFrom[i] = mCursor.getColumnIndexOrThrow(from[i]);
				i++;
			}
		} else {
			mFrom = null;
		}
	}

	public void bindView(View view, Context context, Cursor cursor) {
		ViewBinder binder = mViewBinder;
		int[] from = mFrom;
		int[] to = mTo;
		int i = 0;
		while (i < mTo.length) {
			View v = view.findViewById(to[i]);
			if (v != null) {
				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, cursor, from[i]);
				}
				if (!bound) {
					String text = cursor.getString(from[i]);
					if (text == null) {
						text = "";
					}
					if (v instanceof TextView) {
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						setViewImage((ImageView) v, text);
					} else {
						throw new IllegalStateException(v.getClass().getName() + " is not a " + " view that can be bounds by this SimpleCursorAdapter");
					}
				}
			}
			i++;
		}
	}

	public void changeCursorAndColumns(Cursor c, String[] from, int[] to) {
		mOriginalFrom = from;
		mTo = to;
		super.changeCursor(c);
		findColumns(mOriginalFrom);
	}

	public CharSequence convertToString(Cursor cursor) {
		if (mCursorToStringConverter != null) {
			return mCursorToStringConverter.convertToString(cursor);
		} else if (mStringConversionColumn > -1) {
			return cursor.getString(mStringConversionColumn);
		} else {
			return super.convertToString(cursor);
		}
	}

	public CursorToStringConverter getCursorToStringConverter() {
		return mCursorToStringConverter;
	}

	public int getStringConversionColumn() {
		return mStringConversionColumn;
	}

	public ViewBinder getViewBinder() {
		return mViewBinder;
	}

	public void setCursorToStringConverter(CursorToStringConverter cursorToStringConverter) {
		mCursorToStringConverter = cursorToStringConverter;
	}

	public void setStringConversionColumn(int stringConversionColumn) {
		mStringConversionColumn = stringConversionColumn;
	}

	public void setViewBinder(ViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			v.setImageURI(Uri.parse(value));
			return;
		}
	}

	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	public Cursor swapCursor(Cursor c) {
		findColumns(mOriginalFrom);
		return super.swapCursor(c);
	}
}
