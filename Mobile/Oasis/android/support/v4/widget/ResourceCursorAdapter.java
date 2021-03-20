package android.support.v4.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ResourceCursorAdapter extends CursorAdapter {
	private int mDropDownLayout;
	private LayoutInflater mInflater;
	private int mLayout;

	@Deprecated
	public ResourceCursorAdapter(Context context, int layout, Cursor c) {
		super(context, c);
		mDropDownLayout = layout;
		mLayout = layout;
		mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
	}

	public ResourceCursorAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, c, flags);
		mDropDownLayout = layout;
		mLayout = layout;
		mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
	}

	public ResourceCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		mDropDownLayout = layout;
		mLayout = layout;
		mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
	}

	public View newDropDownView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(mDropDownLayout, parent, false);
	}

	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(mLayout, parent, false);
	}

	public void setDropDownViewResource(int dropDownLayout) {
		mDropDownLayout = dropDownLayout;
	}

	public void setViewResource(int layout) {
		mLayout = layout;
	}
}
