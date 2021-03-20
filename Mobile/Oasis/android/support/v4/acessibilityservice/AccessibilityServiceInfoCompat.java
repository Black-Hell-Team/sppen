package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;

public class AccessibilityServiceInfoCompat {
	public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
	public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
	public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
	public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
	public static final int DEFAULT = 1;
	public static final int FEEDBACK_ALL_MASK = -1;
	public static final int FEEDBACK_BRAILLE = 32;
	public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
	public static final int FLAG_REPORT_VIEW_IDS = 16;
	public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
	public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
	public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
	private static final AccessibilityServiceInfoVersionImpl IMPL;

	static interface AccessibilityServiceInfoVersionImpl {
		public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo r1_AccessibilityServiceInfo);

		public int getCapabilities(AccessibilityServiceInfo r1_AccessibilityServiceInfo);

		public String getDescription(AccessibilityServiceInfo r1_AccessibilityServiceInfo);

		public String getId(AccessibilityServiceInfo r1_AccessibilityServiceInfo);

		public ResolveInfo getResolveInfo(AccessibilityServiceInfo r1_AccessibilityServiceInfo);

		public String getSettingsActivityName(AccessibilityServiceInfo r1_AccessibilityServiceInfo);
	}

	static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoCompat.AccessibilityServiceInfoVersionImpl {
		AccessibilityServiceInfoStubImpl() {
			super();
		}

		public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
			return false;
		}

		public int getCapabilities(AccessibilityServiceInfo info) {
			return 0;
		}

		public String getDescription(AccessibilityServiceInfo info) {
			return null;
		}

		public String getId(AccessibilityServiceInfo info) {
			return null;
		}

		public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
			return null;
		}

		public String getSettingsActivityName(AccessibilityServiceInfo info) {
			return null;
		}
	}

	static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoCompat.AccessibilityServiceInfoStubImpl {
		AccessibilityServiceInfoIcsImpl() {
			super();
		}

		public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(info);
		}

		public int getCapabilities(AccessibilityServiceInfo info) {
			if (getCanRetrieveWindowContent(info)) {
				return DEFAULT;
			} else {
				return 0;
			}
		}

		public String getDescription(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatIcs.getDescription(info);
		}

		public String getId(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatIcs.getId(info);
		}

		public ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatIcs.getResolveInfo(info);
		}

		public String getSettingsActivityName(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(info);
		}
	}

	static class AccessibilityServiceInfoJellyBeanMr2 extends AccessibilityServiceInfoCompat.AccessibilityServiceInfoIcsImpl {
		AccessibilityServiceInfoJellyBeanMr2() {
			super();
		}

		public int getCapabilities(AccessibilityServiceInfo info) {
			return AccessibilityServiceInfoCompatJellyBeanMr2.getCapabilities(info);
		}
	}


	static {
		if (VERSION.SDK_INT >= 18) {
			IMPL = new AccessibilityServiceInfoJellyBeanMr2();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new AccessibilityServiceInfoIcsImpl();
		} else {
			IMPL = new AccessibilityServiceInfoStubImpl();
		}
	}

	private AccessibilityServiceInfoCompat() {
		super();
	}

	public static String capabilityToString(int capability) {
		switch(capability) {
		case DEFAULT:
			return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
		case FLAG_INCLUDE_NOT_IMPORTANT_VIEWS:
			return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
		case FLAG_REQUEST_TOUCH_EXPLORATION_MODE:
			return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
		case FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY:
			return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
		}
		return "UNKNOWN";
	}

	public static String feedbackTypeToString(int feedbackType) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		while (feedbackType > 0) {
			int feedbackTypeFlag = 1 << Integer.numberOfTrailingZeros(feedbackType);
			feedbackType &= feedbackTypeFlag ^ -1;
			if (builder.length() > 1) {
				builder.append(", ");
			}
			switch(feedbackTypeFlag) {
			case DEFAULT:
				builder.append("FEEDBACK_SPOKEN");
				break;
			case FLAG_INCLUDE_NOT_IMPORTANT_VIEWS:
				builder.append("FEEDBACK_HAPTIC");
				break;
			case FLAG_REQUEST_TOUCH_EXPLORATION_MODE:
				builder.append("FEEDBACK_AUDIBLE");
				break;
			case FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY:
				builder.append("FEEDBACK_VISUAL");
				break;
			case FLAG_REPORT_VIEW_IDS:
				builder.append("FEEDBACK_GENERIC");
				break;
			}
		}
		builder.append("]");
		return builder.toString();
	}

	public static String flagToString(int flag) {
		switch(flag) {
		case DEFAULT:
			return "DEFAULT";
		case FLAG_INCLUDE_NOT_IMPORTANT_VIEWS:
			return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
		case FLAG_REQUEST_TOUCH_EXPLORATION_MODE:
			return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
		case FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY:
			return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
		case FLAG_REPORT_VIEW_IDS:
			return "FLAG_REPORT_VIEW_IDS";
		case FLAG_REQUEST_FILTER_KEY_EVENTS:
			return "FLAG_REQUEST_FILTER_KEY_EVENTS";
		}
		return null;
	}

	public static boolean getCanRetrieveWindowContent(AccessibilityServiceInfo info) {
		return IMPL.getCanRetrieveWindowContent(info);
	}

	public static int getCapabilities(AccessibilityServiceInfo info) {
		return IMPL.getCapabilities(info);
	}

	public static String getDescription(AccessibilityServiceInfo info) {
		return IMPL.getDescription(info);
	}

	public static String getId(AccessibilityServiceInfo info) {
		return IMPL.getId(info);
	}

	public static ResolveInfo getResolveInfo(AccessibilityServiceInfo info) {
		return IMPL.getResolveInfo(info);
	}

	public static String getSettingsActivityName(AccessibilityServiceInfo info) {
		return IMPL.getSettingsActivityName(info);
	}
}
