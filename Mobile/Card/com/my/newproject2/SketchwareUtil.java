package com.my.newproject2;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class SketchwareUtil {
	public SketchwareUtil() {
		super();
	}

	public static void getAllKeysFromMap(Map<String, Object> r2_Map_StringObject, ArrayList<String> r3_ArrayList_String) {
		if (r3_ArrayList_String == null) {
		} else {
			r3_ArrayList_String.clear();
			if (r2_Map_StringObject == null || r2_Map_StringObject.size() <= 0) {
			} else {
				Iterator r1_Iterator = r2_Map_StringObject.entrySet().iterator();
				while (r1_Iterator.hasNext()) {
					r3_ArrayList_String.add((String) ((Entry) r1_Iterator.next()).getKey());
				}
			}
		}
	}

	public static ArrayList<Double> getCheckedItemPositionsToArray(ListView r6_ListView) {
		ArrayList<Double> r1_ArrayList_Double = new ArrayList();
		SparseBooleanArray r2_SparseBooleanArray = r6_ListView.getCheckedItemPositions();
		int r0i = 0;
		while (r0i < r2_SparseBooleanArray.size()) {
			if (r2_SparseBooleanArray.valueAt(r0i)) {
				r1_ArrayList_Double.add(Double.valueOf((double) r2_SparseBooleanArray.keyAt(r0i)));
			}
			r0i++;
		}
		return r1_ArrayList_Double;
	}

	public static float getDip(Context r3_Context, int r4i) {
		return TypedValue.applyDimension(1, (float) r4i, r3_Context.getResources().getDisplayMetrics());
	}

	public static int getDisplayHeightPixels(Context r1_Context) {
		return r1_Context.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getDisplayWidthPixels(Context r1_Context) {
		return r1_Context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getLocationX(View r2_View) {
		int[] r0_int_A = new int[2];
		r2_View.getLocationInWindow(r0_int_A);
		return r0_int_A[0];
	}

	public static int getLocationY(View r2_View) {
		int[] r0_int_A = new int[2];
		r2_View.getLocationInWindow(r0_int_A);
		return r0_int_A[1];
	}

	public static int getRandom(int r2i, int r3i) {
		return new Random().nextInt((r3i - r2i) + 1) + r2i;
	}

	public static void showMessage(Context r1_Context, String r2_String) {
		Toast.makeText(r1_Context, r2_String, 0).show();
	}
}
