package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Parcelable;
import android.view.View;
import java.util.Collections;
import java.util.List;

public class AccessibilityRecordCompat {
	private static final AccessibilityRecordImpl IMPL;
	private final Object mRecord;

	static interface AccessibilityRecordImpl {
		public int getAddedCount(Object r1_Object);

		public CharSequence getBeforeText(Object r1_Object);

		public CharSequence getClassName(Object r1_Object);

		public CharSequence getContentDescription(Object r1_Object);

		public int getCurrentItemIndex(Object r1_Object);

		public int getFromIndex(Object r1_Object);

		public int getItemCount(Object r1_Object);

		public int getMaxScrollX(Object r1_Object);

		public int getMaxScrollY(Object r1_Object);

		public Parcelable getParcelableData(Object r1_Object);

		public int getRemovedCount(Object r1_Object);

		public int getScrollX(Object r1_Object);

		public int getScrollY(Object r1_Object);

		public AccessibilityNodeInfoCompat getSource(Object r1_Object);

		public List<CharSequence> getText(Object r1_Object);

		public int getToIndex(Object r1_Object);

		public int getWindowId(Object r1_Object);

		public boolean isChecked(Object r1_Object);

		public boolean isEnabled(Object r1_Object);

		public boolean isFullScreen(Object r1_Object);

		public boolean isPassword(Object r1_Object);

		public boolean isScrollable(Object r1_Object);

		public Object obtain();

		public Object obtain(Object r1_Object);

		public void recycle(Object r1_Object);

		public void setAddedCount(Object r1_Object, int r2i);

		public void setBeforeText(Object r1_Object, CharSequence r2_CharSequence);

		public void setChecked(Object r1_Object, boolean r2z);

		public void setClassName(Object r1_Object, CharSequence r2_CharSequence);

		public void setContentDescription(Object r1_Object, CharSequence r2_CharSequence);

		public void setCurrentItemIndex(Object r1_Object, int r2i);

		public void setEnabled(Object r1_Object, boolean r2z);

		public void setFromIndex(Object r1_Object, int r2i);

		public void setFullScreen(Object r1_Object, boolean r2z);

		public void setItemCount(Object r1_Object, int r2i);

		public void setMaxScrollX(Object r1_Object, int r2i);

		public void setMaxScrollY(Object r1_Object, int r2i);

		public void setParcelableData(Object r1_Object, Parcelable r2_Parcelable);

		public void setPassword(Object r1_Object, boolean r2z);

		public void setRemovedCount(Object r1_Object, int r2i);

		public void setScrollX(Object r1_Object, int r2i);

		public void setScrollY(Object r1_Object, int r2i);

		public void setScrollable(Object r1_Object, boolean r2z);

		public void setSource(Object r1_Object, View r2_View);

		public void setSource(Object r1_Object, View r2_View, int r3i);

		public void setToIndex(Object r1_Object, int r2i);
	}

	static class AccessibilityRecordStubImpl implements AccessibilityRecordCompat.AccessibilityRecordImpl {
		AccessibilityRecordStubImpl() {
			super();
		}

		public int getAddedCount(Object record) {
			return 0;
		}

		public CharSequence getBeforeText(Object record) {
			return null;
		}

		public CharSequence getClassName(Object record) {
			return null;
		}

		public CharSequence getContentDescription(Object record) {
			return null;
		}

		public int getCurrentItemIndex(Object record) {
			return 0;
		}

		public int getFromIndex(Object record) {
			return 0;
		}

		public int getItemCount(Object record) {
			return 0;
		}

		public int getMaxScrollX(Object record) {
			return 0;
		}

		public int getMaxScrollY(Object record) {
			return 0;
		}

		public Parcelable getParcelableData(Object record) {
			return null;
		}

		public int getRemovedCount(Object record) {
			return 0;
		}

		public int getScrollX(Object record) {
			return 0;
		}

		public int getScrollY(Object record) {
			return 0;
		}

		public AccessibilityNodeInfoCompat getSource(Object record) {
			return null;
		}

		public List<CharSequence> getText(Object record) {
			return Collections.emptyList();
		}

		public int getToIndex(Object record) {
			return 0;
		}

		public int getWindowId(Object record) {
			return 0;
		}

		public boolean isChecked(Object record) {
			return false;
		}

		public boolean isEnabled(Object record) {
			return false;
		}

		public boolean isFullScreen(Object record) {
			return false;
		}

		public boolean isPassword(Object record) {
			return false;
		}

		public boolean isScrollable(Object record) {
			return false;
		}

		public Object obtain() {
			return null;
		}

		public Object obtain(Object record) {
			return null;
		}

		public void recycle(Object record) {
		}

		public void setAddedCount(Object record, int addedCount) {
		}

		public void setBeforeText(Object record, CharSequence beforeText) {
		}

		public void setChecked(Object record, boolean isChecked) {
		}

		public void setClassName(Object record, CharSequence className) {
		}

		public void setContentDescription(Object record, CharSequence contentDescription) {
		}

		public void setCurrentItemIndex(Object record, int currentItemIndex) {
		}

		public void setEnabled(Object record, boolean isEnabled) {
		}

		public void setFromIndex(Object record, int fromIndex) {
		}

		public void setFullScreen(Object record, boolean isFullScreen) {
		}

		public void setItemCount(Object record, int itemCount) {
		}

		public void setMaxScrollX(Object record, int maxScrollX) {
		}

		public void setMaxScrollY(Object record, int maxScrollY) {
		}

		public void setParcelableData(Object record, Parcelable parcelableData) {
		}

		public void setPassword(Object record, boolean isPassword) {
		}

		public void setRemovedCount(Object record, int removedCount) {
		}

		public void setScrollX(Object record, int scrollX) {
		}

		public void setScrollY(Object record, int scrollY) {
		}

		public void setScrollable(Object record, boolean scrollable) {
		}

		public void setSource(Object record, View source) {
		}

		public void setSource(Object record, View root, int virtualDescendantId) {
		}

		public void setToIndex(Object record, int toIndex) {
		}
	}

	static class AccessibilityRecordIcsImpl extends AccessibilityRecordCompat.AccessibilityRecordStubImpl {
		AccessibilityRecordIcsImpl() {
			super();
		}

		public int getAddedCount(Object record) {
			return AccessibilityRecordCompatIcs.getAddedCount(record);
		}

		public CharSequence getBeforeText(Object record) {
			return AccessibilityRecordCompatIcs.getBeforeText(record);
		}

		public CharSequence getClassName(Object record) {
			return AccessibilityRecordCompatIcs.getClassName(record);
		}

		public CharSequence getContentDescription(Object record) {
			return AccessibilityRecordCompatIcs.getContentDescription(record);
		}

		public int getCurrentItemIndex(Object record) {
			return AccessibilityRecordCompatIcs.getCurrentItemIndex(record);
		}

		public int getFromIndex(Object record) {
			return AccessibilityRecordCompatIcs.getFromIndex(record);
		}

		public int getItemCount(Object record) {
			return AccessibilityRecordCompatIcs.getItemCount(record);
		}

		public Parcelable getParcelableData(Object record) {
			return AccessibilityRecordCompatIcs.getParcelableData(record);
		}

		public int getRemovedCount(Object record) {
			return AccessibilityRecordCompatIcs.getRemovedCount(record);
		}

		public int getScrollX(Object record) {
			return AccessibilityRecordCompatIcs.getScrollX(record);
		}

		public int getScrollY(Object record) {
			return AccessibilityRecordCompatIcs.getScrollY(record);
		}

		public AccessibilityNodeInfoCompat getSource(Object record) {
			return AccessibilityNodeInfoCompat.wrapNonNullInstance(AccessibilityRecordCompatIcs.getSource(record));
		}

		public List<CharSequence> getText(Object record) {
			return AccessibilityRecordCompatIcs.getText(record);
		}

		public int getToIndex(Object record) {
			return AccessibilityRecordCompatIcs.getToIndex(record);
		}

		public int getWindowId(Object record) {
			return AccessibilityRecordCompatIcs.getWindowId(record);
		}

		public boolean isChecked(Object record) {
			return AccessibilityRecordCompatIcs.isChecked(record);
		}

		public boolean isEnabled(Object record) {
			return AccessibilityRecordCompatIcs.isEnabled(record);
		}

		public boolean isFullScreen(Object record) {
			return AccessibilityRecordCompatIcs.isFullScreen(record);
		}

		public boolean isPassword(Object record) {
			return AccessibilityRecordCompatIcs.isPassword(record);
		}

		public boolean isScrollable(Object record) {
			return AccessibilityRecordCompatIcs.isScrollable(record);
		}

		public Object obtain() {
			return AccessibilityRecordCompatIcs.obtain();
		}

		public Object obtain(Object record) {
			return AccessibilityRecordCompatIcs.obtain(record);
		}

		public void recycle(Object record) {
			AccessibilityRecordCompatIcs.recycle(record);
		}

		public void setAddedCount(Object record, int addedCount) {
			AccessibilityRecordCompatIcs.setAddedCount(record, addedCount);
		}

		public void setBeforeText(Object record, CharSequence beforeText) {
			AccessibilityRecordCompatIcs.setBeforeText(record, beforeText);
		}

		public void setChecked(Object record, boolean isChecked) {
			AccessibilityRecordCompatIcs.setChecked(record, isChecked);
		}

		public void setClassName(Object record, CharSequence className) {
			AccessibilityRecordCompatIcs.setClassName(record, className);
		}

		public void setContentDescription(Object record, CharSequence contentDescription) {
			AccessibilityRecordCompatIcs.setContentDescription(record, contentDescription);
		}

		public void setCurrentItemIndex(Object record, int currentItemIndex) {
			AccessibilityRecordCompatIcs.setCurrentItemIndex(record, currentItemIndex);
		}

		public void setEnabled(Object record, boolean isEnabled) {
			AccessibilityRecordCompatIcs.setEnabled(record, isEnabled);
		}

		public void setFromIndex(Object record, int fromIndex) {
			AccessibilityRecordCompatIcs.setFromIndex(record, fromIndex);
		}

		public void setFullScreen(Object record, boolean isFullScreen) {
			AccessibilityRecordCompatIcs.setFullScreen(record, isFullScreen);
		}

		public void setItemCount(Object record, int itemCount) {
			AccessibilityRecordCompatIcs.setItemCount(record, itemCount);
		}

		public void setParcelableData(Object record, Parcelable parcelableData) {
			AccessibilityRecordCompatIcs.setParcelableData(record, parcelableData);
		}

		public void setPassword(Object record, boolean isPassword) {
			AccessibilityRecordCompatIcs.setPassword(record, isPassword);
		}

		public void setRemovedCount(Object record, int removedCount) {
			AccessibilityRecordCompatIcs.setRemovedCount(record, removedCount);
		}

		public void setScrollX(Object record, int scrollX) {
			AccessibilityRecordCompatIcs.setScrollX(record, scrollX);
		}

		public void setScrollY(Object record, int scrollY) {
			AccessibilityRecordCompatIcs.setScrollY(record, scrollY);
		}

		public void setScrollable(Object record, boolean scrollable) {
			AccessibilityRecordCompatIcs.setScrollable(record, scrollable);
		}

		public void setSource(Object record, View source) {
			AccessibilityRecordCompatIcs.setSource(record, source);
		}

		public void setToIndex(Object record, int toIndex) {
			AccessibilityRecordCompatIcs.setToIndex(record, toIndex);
		}
	}

	static class AccessibilityRecordIcsMr1Impl extends AccessibilityRecordCompat.AccessibilityRecordIcsImpl {
		AccessibilityRecordIcsMr1Impl() {
			super();
		}

		public int getMaxScrollX(Object record) {
			return AccessibilityRecordCompatIcsMr1.getMaxScrollX(record);
		}

		public int getMaxScrollY(Object record) {
			return AccessibilityRecordCompatIcsMr1.getMaxScrollY(record);
		}

		public void setMaxScrollX(Object record, int maxScrollX) {
			AccessibilityRecordCompatIcsMr1.setMaxScrollX(record, maxScrollX);
		}

		public void setMaxScrollY(Object record, int maxScrollY) {
			AccessibilityRecordCompatIcsMr1.setMaxScrollY(record, maxScrollY);
		}
	}

	static class AccessibilityRecordJellyBeanImpl extends AccessibilityRecordCompat.AccessibilityRecordIcsMr1Impl {
		AccessibilityRecordJellyBeanImpl() {
			super();
		}

		public void setSource(Object record, View root, int virtualDescendantId) {
			AccessibilityRecordCompatJellyBean.setSource(record, root, virtualDescendantId);
		}
	}


	static {
		if (VERSION.SDK_INT >= 16) {
			IMPL = new AccessibilityRecordJellyBeanImpl();
		} else if (VERSION.SDK_INT >= 15) {
			IMPL = new AccessibilityRecordIcsMr1Impl();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new AccessibilityRecordIcsImpl();
		} else {
			IMPL = new AccessibilityRecordStubImpl();
		}
	}

	public AccessibilityRecordCompat(Object record) {
		super();
		mRecord = record;
	}

	public static AccessibilityRecordCompat obtain() {
		return new AccessibilityRecordCompat(IMPL.obtain());
	}

	public static AccessibilityRecordCompat obtain(AccessibilityRecordCompat record) {
		return new AccessibilityRecordCompat(IMPL.obtain(record.mRecord));
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			AccessibilityRecordCompat other = (AccessibilityRecordCompat) obj;
			if (mRecord == null) {
				if (other.mRecord != null) {
					return false;
				} else {
					return true;
				}
			} else if (!mRecord.equals(other.mRecord)) {
				return false;
			} else {
				return true;
			}
		}
	}

	public int getAddedCount() {
		return IMPL.getAddedCount(mRecord);
	}

	public CharSequence getBeforeText() {
		return IMPL.getBeforeText(mRecord);
	}

	public CharSequence getClassName() {
		return IMPL.getClassName(mRecord);
	}

	public CharSequence getContentDescription() {
		return IMPL.getContentDescription(mRecord);
	}

	public int getCurrentItemIndex() {
		return IMPL.getCurrentItemIndex(mRecord);
	}

	public int getFromIndex() {
		return IMPL.getFromIndex(mRecord);
	}

	public Object getImpl() {
		return mRecord;
	}

	public int getItemCount() {
		return IMPL.getItemCount(mRecord);
	}

	public int getMaxScrollX() {
		return IMPL.getMaxScrollX(mRecord);
	}

	public int getMaxScrollY() {
		return IMPL.getMaxScrollY(mRecord);
	}

	public Parcelable getParcelableData() {
		return IMPL.getParcelableData(mRecord);
	}

	public int getRemovedCount() {
		return IMPL.getRemovedCount(mRecord);
	}

	public int getScrollX() {
		return IMPL.getScrollX(mRecord);
	}

	public int getScrollY() {
		return IMPL.getScrollY(mRecord);
	}

	public AccessibilityNodeInfoCompat getSource() {
		return IMPL.getSource(mRecord);
	}

	public List<CharSequence> getText() {
		return IMPL.getText(mRecord);
	}

	public int getToIndex() {
		return IMPL.getToIndex(mRecord);
	}

	public int getWindowId() {
		return IMPL.getWindowId(mRecord);
	}

	public int hashCode() {
		if (mRecord == null) {
			return 0;
		} else {
			return mRecord.hashCode();
		}
	}

	public boolean isChecked() {
		return IMPL.isChecked(mRecord);
	}

	public boolean isEnabled() {
		return IMPL.isEnabled(mRecord);
	}

	public boolean isFullScreen() {
		return IMPL.isFullScreen(mRecord);
	}

	public boolean isPassword() {
		return IMPL.isPassword(mRecord);
	}

	public boolean isScrollable() {
		return IMPL.isScrollable(mRecord);
	}

	public void recycle() {
		IMPL.recycle(mRecord);
	}

	public void setAddedCount(int addedCount) {
		IMPL.setAddedCount(mRecord, addedCount);
	}

	public void setBeforeText(CharSequence beforeText) {
		IMPL.setBeforeText(mRecord, beforeText);
	}

	public void setChecked(boolean isChecked) {
		IMPL.setChecked(mRecord, isChecked);
	}

	public void setClassName(CharSequence className) {
		IMPL.setClassName(mRecord, className);
	}

	public void setContentDescription(CharSequence contentDescription) {
		IMPL.setContentDescription(mRecord, contentDescription);
	}

	public void setCurrentItemIndex(int currentItemIndex) {
		IMPL.setCurrentItemIndex(mRecord, currentItemIndex);
	}

	public void setEnabled(boolean isEnabled) {
		IMPL.setEnabled(mRecord, isEnabled);
	}

	public void setFromIndex(int fromIndex) {
		IMPL.setFromIndex(mRecord, fromIndex);
	}

	public void setFullScreen(boolean isFullScreen) {
		IMPL.setFullScreen(mRecord, isFullScreen);
	}

	public void setItemCount(int itemCount) {
		IMPL.setItemCount(mRecord, itemCount);
	}

	public void setMaxScrollX(int maxScrollX) {
		IMPL.setMaxScrollX(mRecord, maxScrollX);
	}

	public void setMaxScrollY(int maxScrollY) {
		IMPL.setMaxScrollY(mRecord, maxScrollY);
	}

	public void setParcelableData(Parcelable parcelableData) {
		IMPL.setParcelableData(mRecord, parcelableData);
	}

	public void setPassword(boolean isPassword) {
		IMPL.setPassword(mRecord, isPassword);
	}

	public void setRemovedCount(int removedCount) {
		IMPL.setRemovedCount(mRecord, removedCount);
	}

	public void setScrollX(int scrollX) {
		IMPL.setScrollX(mRecord, scrollX);
	}

	public void setScrollY(int scrollY) {
		IMPL.setScrollY(mRecord, scrollY);
	}

	public void setScrollable(boolean scrollable) {
		IMPL.setScrollable(mRecord, scrollable);
	}

	public void setSource(View source) {
		IMPL.setSource(mRecord, source);
	}

	public void setSource(View root, int virtualDescendantId) {
		IMPL.setSource(mRecord, root, virtualDescendantId);
	}

	public void setToIndex(int toIndex) {
		IMPL.setToIndex(mRecord, toIndex);
	}
}
