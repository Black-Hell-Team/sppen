package android.support.v4.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.v4.widget.CursorFilter.CursorFilterClient;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;

public abstract class CursorAdapter extends BaseAdapter implements Filterable, CursorFilterClient {
	@Deprecated
	public static final int FLAG_AUTO_REQUERY = 1;
	public static final int FLAG_REGISTER_CONTENT_OBSERVER = 2;
	protected boolean mAutoRequery;
	protected ChangeObserver mChangeObserver;
	protected Context mContext;
	protected Cursor mCursor;
	protected CursorFilter mCursorFilter;
	protected DataSetObserver mDataSetObserver;
	protected boolean mDataValid;
	protected FilterQueryProvider mFilterQueryProvider;
	protected int mRowIDColumn;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private class ChangeObserver extends ContentObserver {
		final /* synthetic */ CursorAdapter this$0;

		public ChangeObserver(CursorAdapter r2_CursorAdapter) {
			super(new Handler());
			this$0 = r2_CursorAdapter;
		}

		public boolean deliverSelfNotifications() {
			return true;
		}

		public void onChange(boolean selfChange) {
			this$0.onContentChanged();
		}
	}

	private class MyDataSetObserver extends DataSetObserver {
		final /* synthetic */ CursorAdapter this$0;

		private MyDataSetObserver(CursorAdapter r1_CursorAdapter) {
			super();
			this$0 = r1_CursorAdapter;
		}

		/* synthetic */ MyDataSetObserver(CursorAdapter x0, CursorAdapter.AnonymousClass_1 x1) {
			this(x0);
		}

		public void onChanged() {
			this$0.mDataValid = true;
			this$0.notifyDataSetChanged();
		}

		public void onInvalidated() {
			this$0.mDataValid = false;
			this$0.notifyDataSetInvalidated();
		}
	}


	@Deprecated
	public CursorAdapter(Context context, Cursor c) {
		super();
		init(context, c, (int)FLAG_AUTO_REQUERY);
	}

	public CursorAdapter(Context context, Cursor c, int flags) {
		super();
		init(context, c, flags);
	}

	public CursorAdapter(Context context, Cursor c, boolean autoRequery) {
		int r0i;
		super();
		if (autoRequery) {
			r0i = FLAG_AUTO_REQUERY;
		} else {
			r0i = FLAG_REGISTER_CONTENT_OBSERVER;
		}
		init(context, c, r0i);
	}

	public abstract void bindView(View r1_View, Context r2_Context, Cursor r3_Cursor);

	public void changeCursor(Cursor cursor) {
		Cursor old = swapCursor(cursor);
		if (old != null) {
			old.close();
		}
	}

	public CharSequence convertToString(Cursor cursor) {
		if (cursor == null) {
			return "";
		} else {
			return cursor.toString();
		}
	}

	public int getCount() {
		if (!mDataValid || mCursor == null) {
			return 0;
		} else {
			return mCursor.getCount();
		}
	}

	public Cursor getCursor() {
		return mCursor;
	}

	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (mDataValid) {
			View v;
			mCursor.moveToPosition(position);
			if (convertView == null) {
				v = newDropDownView(mContext, mCursor, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, mCursor);
			return v;
		} else {
			return null;
		}
	}

	public Filter getFilter() {
		if (mCursorFilter == null) {
			mCursorFilter = new CursorFilter(this);
		}
		return mCursorFilter;
	}

	public FilterQueryProvider getFilterQueryProvider() {
		return mFilterQueryProvider;
	}

	public Object getItem(int position) {
		if (!mDataValid || mCursor == null) {
			return null;
		} else {
			mCursor.moveToPosition(position);
			return mCursor;
		}
	}

	public long getItemId(int position) {
		long r0j = 0;
		if (!mDataValid || mCursor == null || !mCursor.moveToPosition(position)) {
			return r0j;
		} else {
			r0j = mCursor.getLong(mRowIDColumn);
			return r0j;
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (!mDataValid) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		} else if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position " + position);
		} else {
			View v;
			if (convertView == null) {
				v = newView(mContext, mCursor, parent);
			} else {
				v = convertView;
			}
			bindView(v, mContext, mCursor);
			return v;
		}
	}

	public boolean hasStableIds() {
		return true;
	}

	void init(Context context, Cursor c, int flags) {
		boolean cursorPresent = true;
		if ((flags & 1) == 1) {
			flags |= 2;
			mAutoRequery = true;
		} else {
			mAutoRequery = false;
		}
		int r1i;
		if (c != null) {
			mCursor = c;
			mDataValid = cursorPresent;
			mContext = context;
			if (!cursorPresent) {
				r1i = c.getColumnIndexOrThrow("_id");
			} else {
				r1i = -1;
			}
			mRowIDColumn = r1i;
			if ((flags & 2) != 2) {
				mChangeObserver = new ChangeObserver(this);
				mDataSetObserver = new MyDataSetObserver(this, null);
			} else {
				mChangeObserver = null;
				mDataSetObserver = null;
			}
			if (!cursorPresent) {
				if (mChangeObserver == null) {
					c.registerContentObserver(mChangeObserver);
				}
				if (mDataSetObserver != null) {
					c.registerDataSetObserver(mDataSetObserver);
				}
			}
		} else {
			cursorPresent = false;
			mCursor = c;
			mDataValid = cursorPresent;
			mContext = context;
			if (!cursorPresent) {
				r1i = -1;
			} else {
				r1i = c.getColumnIndexOrThrow("_id");
			}
			mRowIDColumn = r1i;
			if ((flags & 2) != 2) {
				mChangeObserver = null;
				mDataSetObserver = null;
			} else {
				mChangeObserver = new ChangeObserver(this);
				mDataSetObserver = new MyDataSetObserver(this, null);
			}
			if (!cursorPresent) {
			} else if (mChangeObserver == null) {
				if (mDataSetObserver != null) {
				} else {
					c.registerDataSetObserver(mDataSetObserver);
				}
			} else {
				c.registerContentObserver(mChangeObserver);
				if (mDataSetObserver != null) {
					c.registerDataSetObserver(mDataSetObserver);
				}
			}
		}
	}

	@Deprecated
	protected void init(Context context, Cursor c, boolean autoRequery) {
		int r0i;
		if (autoRequery) {
			r0i = FLAG_AUTO_REQUERY;
		} else {
			r0i = FLAG_REGISTER_CONTENT_OBSERVER;
		}
		init(context, c, r0i);
	}

	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		return newView(context, cursor, parent);
	}

	public abstract View newView(Context r1_Context, Cursor r2_Cursor, ViewGroup r3_ViewGroup);

	protected void onContentChanged() {
		if (!mAutoRequery || mCursor == null || mCursor.isClosed()) {
		} else {
			mDataValid = mCursor.requery();
		}
	}

	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (mFilterQueryProvider != null) {
			return mFilterQueryProvider.runQuery(constraint);
		} else {
			return mCursor;
		}
	}

	public void setFilterQueryProvider(FilterQueryProvider filterQueryProvider) {
		mFilterQueryProvider = filterQueryProvider;
	}

	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		} else {
			Cursor oldCursor = mCursor;
			if (oldCursor != null) {
				if (mChangeObserver != null) {
					oldCursor.unregisterContentObserver(mChangeObserver);
				}
				if (mDataSetObserver != null) {
					oldCursor.unregisterDataSetObserver(mDataSetObserver);
				}
			}
			mCursor = newCursor;
			if (newCursor != null) {
				if (mChangeObserver != null) {
					newCursor.registerContentObserver(mChangeObserver);
				}
				if (mDataSetObserver != null) {
					newCursor.registerDataSetObserver(mDataSetObserver);
				}
				mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
				mDataValid = true;
				notifyDataSetChanged();
				return oldCursor;
			} else {
				mRowIDColumn = -1;
				mDataValid = false;
				notifyDataSetInvalidated();
				return oldCursor;
			}
		}
	}
}
