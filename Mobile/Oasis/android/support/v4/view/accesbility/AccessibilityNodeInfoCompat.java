package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessibilityNodeInfoCompat {
	public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
	public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
	public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
	public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
	public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
	public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
	public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
	public static final int ACTION_CLEAR_FOCUS = 2;
	public static final int ACTION_CLEAR_SELECTION = 8;
	public static final int ACTION_CLICK = 16;
	public static final int ACTION_COPY = 16384;
	public static final int ACTION_CUT = 65536;
	public static final int ACTION_FOCUS = 1;
	public static final int ACTION_LONG_CLICK = 32;
	public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
	public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
	public static final int ACTION_PASTE = 32768;
	public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
	public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
	public static final int ACTION_SCROLL_BACKWARD = 8192;
	public static final int ACTION_SCROLL_FORWARD = 4096;
	public static final int ACTION_SELECT = 4;
	public static final int ACTION_SET_SELECTION = 131072;
	public static final int FOCUS_ACCESSIBILITY = 2;
	public static final int FOCUS_INPUT = 1;
	private static final AccessibilityNodeInfoImpl IMPL;
	public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
	public static final int MOVEMENT_GRANULARITY_LINE = 4;
	public static final int MOVEMENT_GRANULARITY_PAGE = 16;
	public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
	public static final int MOVEMENT_GRANULARITY_WORD = 2;
	private final Object mInfo;

	static interface AccessibilityNodeInfoImpl {
		public void addAction(Object r1_Object, int r2i);

		public void addChild(Object r1_Object, View r2_View);

		public void addChild(Object r1_Object, View r2_View, int r3i);

		public List<Object> findAccessibilityNodeInfosByText(Object r1_Object, String r2_String);

		public Object findFocus(Object r1_Object, int r2i);

		public Object focusSearch(Object r1_Object, int r2i);

		public int getActions(Object r1_Object);

		public void getBoundsInParent(Object r1_Object, Rect r2_Rect);

		public void getBoundsInScreen(Object r1_Object, Rect r2_Rect);

		public Object getChild(Object r1_Object, int r2i);

		public int getChildCount(Object r1_Object);

		public CharSequence getClassName(Object r1_Object);

		public CharSequence getContentDescription(Object r1_Object);

		public int getLiveRegion(Object r1_Object);

		public int getMovementGranularities(Object r1_Object);

		public CharSequence getPackageName(Object r1_Object);

		public Object getParent(Object r1_Object);

		public CharSequence getText(Object r1_Object);

		public String getViewIdResourceName(Object r1_Object);

		public int getWindowId(Object r1_Object);

		public boolean isAccessibilityFocused(Object r1_Object);

		public boolean isCheckable(Object r1_Object);

		public boolean isChecked(Object r1_Object);

		public boolean isClickable(Object r1_Object);

		public boolean isEnabled(Object r1_Object);

		public boolean isFocusable(Object r1_Object);

		public boolean isFocused(Object r1_Object);

		public boolean isLongClickable(Object r1_Object);

		public boolean isPassword(Object r1_Object);

		public boolean isScrollable(Object r1_Object);

		public boolean isSelected(Object r1_Object);

		public boolean isVisibleToUser(Object r1_Object);

		public Object obtain();

		public Object obtain(View r1_View);

		public Object obtain(View r1_View, int r2i);

		public Object obtain(Object r1_Object);

		public boolean performAction(Object r1_Object, int r2i);

		public boolean performAction(Object r1_Object, int r2i, Bundle r3_Bundle);

		public void recycle(Object r1_Object);

		public void setAccessibilityFocused(Object r1_Object, boolean r2z);

		public void setBoundsInParent(Object r1_Object, Rect r2_Rect);

		public void setBoundsInScreen(Object r1_Object, Rect r2_Rect);

		public void setCheckable(Object r1_Object, boolean r2z);

		public void setChecked(Object r1_Object, boolean r2z);

		public void setClassName(Object r1_Object, CharSequence r2_CharSequence);

		public void setClickable(Object r1_Object, boolean r2z);

		public void setContentDescription(Object r1_Object, CharSequence r2_CharSequence);

		public void setEnabled(Object r1_Object, boolean r2z);

		public void setFocusable(Object r1_Object, boolean r2z);

		public void setFocused(Object r1_Object, boolean r2z);

		public void setLiveRegion(Object r1_Object, int r2i);

		public void setLongClickable(Object r1_Object, boolean r2z);

		public void setMovementGranularities(Object r1_Object, int r2i);

		public void setPackageName(Object r1_Object, CharSequence r2_CharSequence);

		public void setParent(Object r1_Object, View r2_View);

		public void setParent(Object r1_Object, View r2_View, int r3i);

		public void setPassword(Object r1_Object, boolean r2z);

		public void setScrollable(Object r1_Object, boolean r2z);

		public void setSelected(Object r1_Object, boolean r2z);

		public void setSource(Object r1_Object, View r2_View);

		public void setSource(Object r1_Object, View r2_View, int r3i);

		public void setText(Object r1_Object, CharSequence r2_CharSequence);

		public void setViewIdResourceName(Object r1_Object, String r2_String);

		public void setVisibleToUser(Object r1_Object, boolean r2z);
	}

	static class AccessibilityNodeInfoStubImpl implements AccessibilityNodeInfoCompat.AccessibilityNodeInfoImpl {
		AccessibilityNodeInfoStubImpl() {
			super();
		}

		public void addAction(Object info, int action) {
		}

		public void addChild(Object info, View child) {
		}

		public void addChild(Object info, View child, int virtualDescendantId) {
		}

		public List<Object> findAccessibilityNodeInfosByText(Object info, String text) {
			return Collections.emptyList();
		}

		public Object findFocus(Object info, int focus) {
			return null;
		}

		public Object focusSearch(Object info, int direction) {
			return null;
		}

		public int getActions(Object info) {
			return 0;
		}

		public void getBoundsInParent(Object info, Rect outBounds) {
		}

		public void getBoundsInScreen(Object info, Rect outBounds) {
		}

		public Object getChild(Object info, int index) {
			return null;
		}

		public int getChildCount(Object info) {
			return 0;
		}

		public CharSequence getClassName(Object info) {
			return null;
		}

		public CharSequence getContentDescription(Object info) {
			return null;
		}

		public int getLiveRegion(Object info) {
			return 0;
		}

		public int getMovementGranularities(Object info) {
			return 0;
		}

		public CharSequence getPackageName(Object info) {
			return null;
		}

		public Object getParent(Object info) {
			return null;
		}

		public CharSequence getText(Object info) {
			return null;
		}

		public String getViewIdResourceName(Object info) {
			return null;
		}

		public int getWindowId(Object info) {
			return 0;
		}

		public boolean isAccessibilityFocused(Object info) {
			return false;
		}

		public boolean isCheckable(Object info) {
			return false;
		}

		public boolean isChecked(Object info) {
			return false;
		}

		public boolean isClickable(Object info) {
			return false;
		}

		public boolean isEnabled(Object info) {
			return false;
		}

		public boolean isFocusable(Object info) {
			return false;
		}

		public boolean isFocused(Object info) {
			return false;
		}

		public boolean isLongClickable(Object info) {
			return false;
		}

		public boolean isPassword(Object info) {
			return false;
		}

		public boolean isScrollable(Object info) {
			return false;
		}

		public boolean isSelected(Object info) {
			return false;
		}

		public boolean isVisibleToUser(Object info) {
			return false;
		}

		public Object obtain() {
			return null;
		}

		public Object obtain(View source) {
			return null;
		}

		public Object obtain(View root, int virtualDescendantId) {
			return null;
		}

		public Object obtain(Object info) {
			return null;
		}

		public boolean performAction(Object info, int action) {
			return false;
		}

		public boolean performAction(Object info, int action, Bundle arguments) {
			return false;
		}

		public void recycle(Object info) {
		}

		public void setAccessibilityFocused(Object info, boolean focused) {
		}

		public void setBoundsInParent(Object info, Rect bounds) {
		}

		public void setBoundsInScreen(Object info, Rect bounds) {
		}

		public void setCheckable(Object info, boolean checkable) {
		}

		public void setChecked(Object info, boolean checked) {
		}

		public void setClassName(Object info, CharSequence className) {
		}

		public void setClickable(Object info, boolean clickable) {
		}

		public void setContentDescription(Object info, CharSequence contentDescription) {
		}

		public void setEnabled(Object info, boolean enabled) {
		}

		public void setFocusable(Object info, boolean focusable) {
		}

		public void setFocused(Object info, boolean focused) {
		}

		public void setLiveRegion(Object info, int mode) {
		}

		public void setLongClickable(Object info, boolean longClickable) {
		}

		public void setMovementGranularities(Object info, int granularities) {
		}

		public void setPackageName(Object info, CharSequence packageName) {
		}

		public void setParent(Object info, View parent) {
		}

		public void setParent(Object info, View root, int virtualDescendantId) {
		}

		public void setPassword(Object info, boolean password) {
		}

		public void setScrollable(Object info, boolean scrollable) {
		}

		public void setSelected(Object info, boolean selected) {
		}

		public void setSource(Object info, View source) {
		}

		public void setSource(Object info, View root, int virtualDescendantId) {
		}

		public void setText(Object info, CharSequence text) {
		}

		public void setViewIdResourceName(Object info, String viewId) {
		}

		public void setVisibleToUser(Object info, boolean visibleToUser) {
		}
	}

	static class AccessibilityNodeInfoIcsImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoStubImpl {
		AccessibilityNodeInfoIcsImpl() {
			super();
		}

		public void addAction(Object info, int action) {
			AccessibilityNodeInfoCompatIcs.addAction(info, action);
		}

		public void addChild(Object info, View child) {
			AccessibilityNodeInfoCompatIcs.addChild(info, child);
		}

		public List<Object> findAccessibilityNodeInfosByText(Object info, String text) {
			return AccessibilityNodeInfoCompatIcs.findAccessibilityNodeInfosByText(info, text);
		}

		public int getActions(Object info) {
			return AccessibilityNodeInfoCompatIcs.getActions(info);
		}

		public void getBoundsInParent(Object info, Rect outBounds) {
			AccessibilityNodeInfoCompatIcs.getBoundsInParent(info, outBounds);
		}

		public void getBoundsInScreen(Object info, Rect outBounds) {
			AccessibilityNodeInfoCompatIcs.getBoundsInScreen(info, outBounds);
		}

		public Object getChild(Object info, int index) {
			return AccessibilityNodeInfoCompatIcs.getChild(info, index);
		}

		public int getChildCount(Object info) {
			return AccessibilityNodeInfoCompatIcs.getChildCount(info);
		}

		public CharSequence getClassName(Object info) {
			return AccessibilityNodeInfoCompatIcs.getClassName(info);
		}

		public CharSequence getContentDescription(Object info) {
			return AccessibilityNodeInfoCompatIcs.getContentDescription(info);
		}

		public CharSequence getPackageName(Object info) {
			return AccessibilityNodeInfoCompatIcs.getPackageName(info);
		}

		public Object getParent(Object info) {
			return AccessibilityNodeInfoCompatIcs.getParent(info);
		}

		public CharSequence getText(Object info) {
			return AccessibilityNodeInfoCompatIcs.getText(info);
		}

		public int getWindowId(Object info) {
			return AccessibilityNodeInfoCompatIcs.getWindowId(info);
		}

		public boolean isCheckable(Object info) {
			return AccessibilityNodeInfoCompatIcs.isCheckable(info);
		}

		public boolean isChecked(Object info) {
			return AccessibilityNodeInfoCompatIcs.isChecked(info);
		}

		public boolean isClickable(Object info) {
			return AccessibilityNodeInfoCompatIcs.isClickable(info);
		}

		public boolean isEnabled(Object info) {
			return AccessibilityNodeInfoCompatIcs.isEnabled(info);
		}

		public boolean isFocusable(Object info) {
			return AccessibilityNodeInfoCompatIcs.isFocusable(info);
		}

		public boolean isFocused(Object info) {
			return AccessibilityNodeInfoCompatIcs.isFocused(info);
		}

		public boolean isLongClickable(Object info) {
			return AccessibilityNodeInfoCompatIcs.isLongClickable(info);
		}

		public boolean isPassword(Object info) {
			return AccessibilityNodeInfoCompatIcs.isPassword(info);
		}

		public boolean isScrollable(Object info) {
			return AccessibilityNodeInfoCompatIcs.isScrollable(info);
		}

		public boolean isSelected(Object info) {
			return AccessibilityNodeInfoCompatIcs.isSelected(info);
		}

		public Object obtain() {
			return AccessibilityNodeInfoCompatIcs.obtain();
		}

		public Object obtain(View source) {
			return AccessibilityNodeInfoCompatIcs.obtain(source);
		}

		public Object obtain(Object info) {
			return AccessibilityNodeInfoCompatIcs.obtain(info);
		}

		public boolean performAction(Object info, int action) {
			return AccessibilityNodeInfoCompatIcs.performAction(info, action);
		}

		public void recycle(Object info) {
			AccessibilityNodeInfoCompatIcs.recycle(info);
		}

		public void setBoundsInParent(Object info, Rect bounds) {
			AccessibilityNodeInfoCompatIcs.setBoundsInParent(info, bounds);
		}

		public void setBoundsInScreen(Object info, Rect bounds) {
			AccessibilityNodeInfoCompatIcs.setBoundsInScreen(info, bounds);
		}

		public void setCheckable(Object info, boolean checkable) {
			AccessibilityNodeInfoCompatIcs.setCheckable(info, checkable);
		}

		public void setChecked(Object info, boolean checked) {
			AccessibilityNodeInfoCompatIcs.setChecked(info, checked);
		}

		public void setClassName(Object info, CharSequence className) {
			AccessibilityNodeInfoCompatIcs.setClassName(info, className);
		}

		public void setClickable(Object info, boolean clickable) {
			AccessibilityNodeInfoCompatIcs.setClickable(info, clickable);
		}

		public void setContentDescription(Object info, CharSequence contentDescription) {
			AccessibilityNodeInfoCompatIcs.setContentDescription(info, contentDescription);
		}

		public void setEnabled(Object info, boolean enabled) {
			AccessibilityNodeInfoCompatIcs.setEnabled(info, enabled);
		}

		public void setFocusable(Object info, boolean focusable) {
			AccessibilityNodeInfoCompatIcs.setFocusable(info, focusable);
		}

		public void setFocused(Object info, boolean focused) {
			AccessibilityNodeInfoCompatIcs.setFocused(info, focused);
		}

		public void setLongClickable(Object info, boolean longClickable) {
			AccessibilityNodeInfoCompatIcs.setLongClickable(info, longClickable);
		}

		public void setPackageName(Object info, CharSequence packageName) {
			AccessibilityNodeInfoCompatIcs.setPackageName(info, packageName);
		}

		public void setParent(Object info, View parent) {
			AccessibilityNodeInfoCompatIcs.setParent(info, parent);
		}

		public void setPassword(Object info, boolean password) {
			AccessibilityNodeInfoCompatIcs.setPassword(info, password);
		}

		public void setScrollable(Object info, boolean scrollable) {
			AccessibilityNodeInfoCompatIcs.setScrollable(info, scrollable);
		}

		public void setSelected(Object info, boolean selected) {
			AccessibilityNodeInfoCompatIcs.setSelected(info, selected);
		}

		public void setSource(Object info, View source) {
			AccessibilityNodeInfoCompatIcs.setSource(info, source);
		}

		public void setText(Object info, CharSequence text) {
			AccessibilityNodeInfoCompatIcs.setText(info, text);
		}
	}

	static class AccessibilityNodeInfoJellybeanImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoIcsImpl {
		AccessibilityNodeInfoJellybeanImpl() {
			super();
		}

		public void addChild(Object info, View child, int virtualDescendantId) {
			AccessibilityNodeInfoCompatJellyBean.addChild(info, child, virtualDescendantId);
		}

		public Object findFocus(Object info, int focus) {
			return AccessibilityNodeInfoCompatJellyBean.findFocus(info, focus);
		}

		public Object focusSearch(Object info, int direction) {
			return AccessibilityNodeInfoCompatJellyBean.focusSearch(info, direction);
		}

		public int getMovementGranularities(Object info) {
			return AccessibilityNodeInfoCompatJellyBean.getMovementGranularities(info);
		}

		public boolean isAccessibilityFocused(Object info) {
			return AccessibilityNodeInfoCompatJellyBean.isAccessibilityFocused(info);
		}

		public boolean isVisibleToUser(Object info) {
			return AccessibilityNodeInfoCompatJellyBean.isVisibleToUser(info);
		}

		public Object obtain(View root, int virtualDescendantId) {
			return AccessibilityNodeInfoCompatJellyBean.obtain(root, virtualDescendantId);
		}

		public boolean performAction(Object info, int action, Bundle arguments) {
			return AccessibilityNodeInfoCompatJellyBean.performAction(info, action, arguments);
		}

		public void setAccessibilityFocused(Object info, boolean focused) {
			AccessibilityNodeInfoCompatJellyBean.setAccesibilityFocused(info, focused);
		}

		public void setMovementGranularities(Object info, int granularities) {
			AccessibilityNodeInfoCompatJellyBean.setMovementGranularities(info, granularities);
		}

		public void setParent(Object info, View root, int virtualDescendantId) {
			AccessibilityNodeInfoCompatJellyBean.setParent(info, root, virtualDescendantId);
		}

		public void setSource(Object info, View root, int virtualDescendantId) {
			AccessibilityNodeInfoCompatJellyBean.setSource(info, root, virtualDescendantId);
		}

		public void setVisibleToUser(Object info, boolean visibleToUser) {
			AccessibilityNodeInfoCompatJellyBean.setVisibleToUser(info, visibleToUser);
		}
	}

	static class AccessibilityNodeInfoJellybeanMr2Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanImpl {
		AccessibilityNodeInfoJellybeanMr2Impl() {
			super();
		}

		public String getViewIdResourceName(Object info) {
			return AccessibilityNodeInfoCompatJellybeanMr2.getViewIdResourceName(info);
		}

		public void setViewIdResourceName(Object info, String viewId) {
			AccessibilityNodeInfoCompatJellybeanMr2.setViewIdResourceName(info, viewId);
		}
	}

	static class AccessibilityNodeInfoKitKatImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanMr2Impl {
		AccessibilityNodeInfoKitKatImpl() {
			super();
		}

		public int getLiveRegion(Object info) {
			return AccessibilityNodeInfoCompatKitKat.getLiveRegion(info);
		}

		public void setLiveRegion(Object info, int mode) {
			AccessibilityNodeInfoCompatKitKat.setLiveRegion(info, mode);
		}
	}


	static {
		if (VERSION.SDK_INT >= 19) {
			IMPL = new AccessibilityNodeInfoKitKatImpl();
		} else if (VERSION.SDK_INT >= 18) {
			IMPL = new AccessibilityNodeInfoJellybeanMr2Impl();
		} else if (VERSION.SDK_INT >= 16) {
			IMPL = new AccessibilityNodeInfoJellybeanImpl();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new AccessibilityNodeInfoIcsImpl();
		} else {
			IMPL = new AccessibilityNodeInfoStubImpl();
		}
	}

	public AccessibilityNodeInfoCompat(Object info) {
		super();
		mInfo = info;
	}

	private static String getActionSymbolicName(int action) {
		switch(action) {
		case MOVEMENT_GRANULARITY_CHARACTER:
			return "ACTION_FOCUS";
		case MOVEMENT_GRANULARITY_WORD:
			return "ACTION_CLEAR_FOCUS";
		case MOVEMENT_GRANULARITY_LINE:
			return "ACTION_SELECT";
		case MOVEMENT_GRANULARITY_PARAGRAPH:
			return "ACTION_CLEAR_SELECTION";
		case MOVEMENT_GRANULARITY_PAGE:
			return "ACTION_CLICK";
		case ACTION_LONG_CLICK:
			return "ACTION_LONG_CLICK";
		case ACTION_ACCESSIBILITY_FOCUS:
			return "ACTION_ACCESSIBILITY_FOCUS";
		case ACTION_CLEAR_ACCESSIBILITY_FOCUS:
			return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
		case ACTION_NEXT_AT_MOVEMENT_GRANULARITY:
			return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
		case ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY:
			return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
		case ACTION_NEXT_HTML_ELEMENT:
			return "ACTION_NEXT_HTML_ELEMENT";
		case ACTION_PREVIOUS_HTML_ELEMENT:
			return "ACTION_PREVIOUS_HTML_ELEMENT";
		case ACTION_SCROLL_FORWARD:
			return "ACTION_SCROLL_FORWARD";
		case ACTION_SCROLL_BACKWARD:
			return "ACTION_SCROLL_BACKWARD";
		case ACTION_COPY:
			return "ACTION_COPY";
		case ACTION_PASTE:
			return "ACTION_PASTE";
		case ACTION_CUT:
			return "ACTION_CUT";
		case ACTION_SET_SELECTION:
			return "ACTION_SET_SELECTION";
		}
		return "ACTION_UNKNOWN";
	}

	public static AccessibilityNodeInfoCompat obtain() {
		return wrapNonNullInstance(IMPL.obtain());
	}

	public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat info) {
		return wrapNonNullInstance(IMPL.obtain(info.mInfo));
	}

	public static AccessibilityNodeInfoCompat obtain(View source) {
		return wrapNonNullInstance(IMPL.obtain(source));
	}

	public static AccessibilityNodeInfoCompat obtain(View root, int virtualDescendantId) {
		return wrapNonNullInstance(IMPL.obtain(root, virtualDescendantId));
	}

	static AccessibilityNodeInfoCompat wrapNonNullInstance(Object object) {
		if (object != null) {
			return new AccessibilityNodeInfoCompat(object);
		} else {
			return null;
		}
	}

	public void addAction(int action) {
		IMPL.addAction(mInfo, action);
	}

	public void addChild(View child) {
		IMPL.addChild(mInfo, child);
	}

	public void addChild(View root, int virtualDescendantId) {
		IMPL.addChild(mInfo, root, virtualDescendantId);
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			AccessibilityNodeInfoCompat other = (AccessibilityNodeInfoCompat) obj;
			if (mInfo == null) {
				if (other.mInfo != null) {
					return false;
				} else {
					return true;
				}
			} else if (!mInfo.equals(other.mInfo)) {
				return false;
			} else {
				return true;
			}
		}
	}

	public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String text) {
		List<AccessibilityNodeInfoCompat> result = new ArrayList();
		List<Object> infos = IMPL.findAccessibilityNodeInfosByText(mInfo, text);
		int i = 0;
		while (i < infos.size()) {
			result.add(new AccessibilityNodeInfoCompat(infos.get(i)));
			i++;
		}
		return result;
	}

	public AccessibilityNodeInfoCompat findFocus(int focus) {
		return wrapNonNullInstance(IMPL.findFocus(mInfo, focus));
	}

	public AccessibilityNodeInfoCompat focusSearch(int direction) {
		return wrapNonNullInstance(IMPL.focusSearch(mInfo, direction));
	}

	public int getActions() {
		return IMPL.getActions(mInfo);
	}

	public void getBoundsInParent(Rect outBounds) {
		IMPL.getBoundsInParent(mInfo, outBounds);
	}

	public void getBoundsInScreen(Rect outBounds) {
		IMPL.getBoundsInScreen(mInfo, outBounds);
	}

	public AccessibilityNodeInfoCompat getChild(int index) {
		return wrapNonNullInstance(IMPL.getChild(mInfo, index));
	}

	public int getChildCount() {
		return IMPL.getChildCount(mInfo);
	}

	public CharSequence getClassName() {
		return IMPL.getClassName(mInfo);
	}

	public CharSequence getContentDescription() {
		return IMPL.getContentDescription(mInfo);
	}

	public Object getInfo() {
		return mInfo;
	}

	public int getLiveRegion() {
		return IMPL.getLiveRegion(mInfo);
	}

	public int getMovementGranularities() {
		return IMPL.getMovementGranularities(mInfo);
	}

	public CharSequence getPackageName() {
		return IMPL.getPackageName(mInfo);
	}

	public AccessibilityNodeInfoCompat getParent() {
		return wrapNonNullInstance(IMPL.getParent(mInfo));
	}

	public CharSequence getText() {
		return IMPL.getText(mInfo);
	}

	public String getViewIdResourceName() {
		return IMPL.getViewIdResourceName(mInfo);
	}

	public int getWindowId() {
		return IMPL.getWindowId(mInfo);
	}

	public int hashCode() {
		if (mInfo == null) {
			return 0;
		} else {
			return mInfo.hashCode();
		}
	}

	public boolean isAccessibilityFocused() {
		return IMPL.isAccessibilityFocused(mInfo);
	}

	public boolean isCheckable() {
		return IMPL.isCheckable(mInfo);
	}

	public boolean isChecked() {
		return IMPL.isChecked(mInfo);
	}

	public boolean isClickable() {
		return IMPL.isClickable(mInfo);
	}

	public boolean isEnabled() {
		return IMPL.isEnabled(mInfo);
	}

	public boolean isFocusable() {
		return IMPL.isFocusable(mInfo);
	}

	public boolean isFocused() {
		return IMPL.isFocused(mInfo);
	}

	public boolean isLongClickable() {
		return IMPL.isLongClickable(mInfo);
	}

	public boolean isPassword() {
		return IMPL.isPassword(mInfo);
	}

	public boolean isScrollable() {
		return IMPL.isScrollable(mInfo);
	}

	public boolean isSelected() {
		return IMPL.isSelected(mInfo);
	}

	public boolean isVisibleToUser() {
		return IMPL.isVisibleToUser(mInfo);
	}

	public boolean performAction(int action) {
		return IMPL.performAction(mInfo, action);
	}

	public boolean performAction(int action, Bundle arguments) {
		return IMPL.performAction(mInfo, action, arguments);
	}

	public void recycle() {
		IMPL.recycle(mInfo);
	}

	public void setAccessibilityFocused(boolean focused) {
		IMPL.setAccessibilityFocused(mInfo, focused);
	}

	public void setBoundsInParent(Rect bounds) {
		IMPL.setBoundsInParent(mInfo, bounds);
	}

	public void setBoundsInScreen(Rect bounds) {
		IMPL.setBoundsInScreen(mInfo, bounds);
	}

	public void setCheckable(boolean checkable) {
		IMPL.setCheckable(mInfo, checkable);
	}

	public void setChecked(boolean checked) {
		IMPL.setChecked(mInfo, checked);
	}

	public void setClassName(CharSequence className) {
		IMPL.setClassName(mInfo, className);
	}

	public void setClickable(boolean clickable) {
		IMPL.setClickable(mInfo, clickable);
	}

	public void setContentDescription(CharSequence contentDescription) {
		IMPL.setContentDescription(mInfo, contentDescription);
	}

	public void setEnabled(boolean enabled) {
		IMPL.setEnabled(mInfo, enabled);
	}

	public void setFocusable(boolean focusable) {
		IMPL.setFocusable(mInfo, focusable);
	}

	public void setFocused(boolean focused) {
		IMPL.setFocused(mInfo, focused);
	}

	public void setLiveRegion(int mode) {
		IMPL.setLiveRegion(mInfo, mode);
	}

	public void setLongClickable(boolean longClickable) {
		IMPL.setLongClickable(mInfo, longClickable);
	}

	public void setMovementGranularities(int granularities) {
		IMPL.setMovementGranularities(mInfo, granularities);
	}

	public void setPackageName(CharSequence packageName) {
		IMPL.setPackageName(mInfo, packageName);
	}

	public void setParent(View parent) {
		IMPL.setParent(mInfo, parent);
	}

	public void setParent(View root, int virtualDescendantId) {
		IMPL.setParent(mInfo, root, virtualDescendantId);
	}

	public void setPassword(boolean password) {
		IMPL.setPassword(mInfo, password);
	}

	public void setScrollable(boolean scrollable) {
		IMPL.setScrollable(mInfo, scrollable);
	}

	public void setSelected(boolean selected) {
		IMPL.setSelected(mInfo, selected);
	}

	public void setSource(View source) {
		IMPL.setSource(mInfo, source);
	}

	public void setSource(View root, int virtualDescendantId) {
		IMPL.setSource(mInfo, root, virtualDescendantId);
	}

	public void setText(CharSequence text) {
		IMPL.setText(mInfo, text);
	}

	public void setViewIdResourceName(String viewId) {
		IMPL.setViewIdResourceName(mInfo, viewId);
	}

	public void setVisibleToUser(boolean visibleToUser) {
		IMPL.setVisibleToUser(mInfo, visibleToUser);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		Rect bounds = new Rect();
		getBoundsInParent(bounds);
		builder.append("; boundsInParent: " + bounds);
		getBoundsInScreen(bounds);
		builder.append("; boundsInScreen: " + bounds);
		builder.append("; packageName: ").append(getPackageName());
		builder.append("; className: ").append(getClassName());
		builder.append("; text: ").append(getText());
		builder.append("; contentDescription: ").append(getContentDescription());
		builder.append("; viewId: ").append(getViewIdResourceName());
		builder.append("; checkable: ").append(isCheckable());
		builder.append("; checked: ").append(isChecked());
		builder.append("; focusable: ").append(isFocusable());
		builder.append("; focused: ").append(isFocused());
		builder.append("; selected: ").append(isSelected());
		builder.append("; clickable: ").append(isClickable());
		builder.append("; longClickable: ").append(isLongClickable());
		builder.append("; enabled: ").append(isEnabled());
		builder.append("; password: ").append(isPassword());
		builder.append("; scrollable: " + isScrollable());
		builder.append("; [");
		int actionBits = getActions();
		while (actionBits != 0) {
			int action = 1 << Integer.numberOfTrailingZeros(actionBits);
			actionBits &= action ^ -1;
			builder.append(getActionSymbolicName(action));
			if (actionBits != 0) {
				builder.append(", ");
			}
		}
		builder.append("]");
		return builder.toString();
	}
}
