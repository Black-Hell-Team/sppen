package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompatBase.Action.Factory;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationCompat {
	public static final int DEFAULT_ALL = -1;
	public static final int DEFAULT_LIGHTS = 4;
	public static final int DEFAULT_SOUND = 1;
	public static final int DEFAULT_VIBRATE = 2;
	public static final String EXTRA_INFO_TEXT = "android.infoText";
	public static final String EXTRA_LARGE_ICON = "android.largeIcon";
	public static final String EXTRA_LARGE_ICON_BIG = "android.largeIcon.big";
	public static final String EXTRA_PEOPLE = "android.people";
	public static final String EXTRA_PICTURE = "android.picture";
	public static final String EXTRA_PROGRESS = "android.progress";
	public static final String EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate";
	public static final String EXTRA_PROGRESS_MAX = "android.progressMax";
	public static final String EXTRA_SHOW_CHRONOMETER = "android.showChronometer";
	public static final String EXTRA_SMALL_ICON = "android.icon";
	public static final String EXTRA_SUB_TEXT = "android.subText";
	public static final String EXTRA_SUMMARY_TEXT = "android.summaryText";
	public static final String EXTRA_TEXT = "android.text";
	public static final String EXTRA_TEXT_LINES = "android.textLines";
	public static final String EXTRA_TITLE = "android.title";
	public static final String EXTRA_TITLE_BIG = "android.title.big";
	public static final int FLAG_AUTO_CANCEL = 16;
	public static final int FLAG_FOREGROUND_SERVICE = 64;
	public static final int FLAG_GROUP_SUMMARY = 512;
	public static final int FLAG_HIGH_PRIORITY = 128;
	public static final int FLAG_INSISTENT = 4;
	public static final int FLAG_LOCAL_ONLY = 256;
	public static final int FLAG_NO_CLEAR = 32;
	public static final int FLAG_ONGOING_EVENT = 2;
	public static final int FLAG_ONLY_ALERT_ONCE = 8;
	public static final int FLAG_SHOW_LIGHTS = 1;
	private static final NotificationCompatImpl IMPL;
	public static final int PRIORITY_DEFAULT = 0;
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_LOW = -1;
	public static final int PRIORITY_MAX = 2;
	public static final int PRIORITY_MIN = -2;
	public static final int STREAM_DEFAULT = -1;

	static /* synthetic */ class AnonymousClass_1 {
	}

	public static class Builder {
		ArrayList<NotificationCompat.Action> mActions;
		CharSequence mContentInfo;
		PendingIntent mContentIntent;
		CharSequence mContentText;
		CharSequence mContentTitle;
		Context mContext;
		Bundle mExtras;
		PendingIntent mFullScreenIntent;
		String mGroupKey;
		boolean mGroupSummary;
		Bitmap mLargeIcon;
		boolean mLocalOnly;
		Notification mNotification;
		int mNumber;
		int mPriority;
		int mProgress;
		boolean mProgressIndeterminate;
		int mProgressMax;
		String mSortKey;
		NotificationCompat.Style mStyle;
		CharSequence mSubText;
		RemoteViews mTickerView;
		boolean mUseChronometer;

		public Builder(Context context) {
			super();
			mActions = new ArrayList();
			mLocalOnly = false;
			mNotification = new Notification();
			mContext = context;
			mNotification.when = System.currentTimeMillis();
			mNotification.audioStreamType = -1;
			mPriority = 0;
		}

		private void setFlag(int mask, boolean value) {
			if (value) {
				r0_Notification = mNotification;
				r0_Notification.flags |= mask;
			} else {
				r0_Notification = mNotification;
				r0_Notification.flags &= mask ^ -1;
			}
		}

		public NotificationCompat.Builder addAction(int icon, CharSequence title, PendingIntent intent) {
			mActions.add(new NotificationCompat.Action(icon, title, intent));
			return this;
		}

		public NotificationCompat.Builder addAction(NotificationCompat.Action action) {
			mActions.add(action);
			return this;
		}

		public NotificationCompat.Builder addExtras(Bundle extras) {
			if (extras != null) {
				if (mExtras == null) {
					mExtras = new Bundle(extras);
					return this;
				} else {
					mExtras.putAll(extras);
					return this;
				}
			} else {
				return this;
			}
		}

		public Notification build() {
			return IMPL.build(this);
		}

		public NotificationCompat.Builder extend(NotificationCompat.Extender extender) {
			extender.extend(this);
			return this;
		}

		public Bundle getExtras() {
			if (mExtras == null) {
				mExtras = new Bundle();
			}
			return mExtras;
		}

		@Deprecated
		public Notification getNotification() {
			return IMPL.build(this);
		}

		public NotificationCompat.Builder setAutoCancel(boolean autoCancel) {
			setFlag(FLAG_AUTO_CANCEL, autoCancel);
			return this;
		}

		public NotificationCompat.Builder setContent(RemoteViews views) {
			mNotification.contentView = views;
			return this;
		}

		public NotificationCompat.Builder setContentInfo(CharSequence info) {
			mContentInfo = info;
			return this;
		}

		public NotificationCompat.Builder setContentIntent(PendingIntent intent) {
			mContentIntent = intent;
			return this;
		}

		public NotificationCompat.Builder setContentText(CharSequence text) {
			mContentText = text;
			return this;
		}

		public NotificationCompat.Builder setContentTitle(CharSequence title) {
			mContentTitle = title;
			return this;
		}

		public NotificationCompat.Builder setDefaults(int defaults) {
			mNotification.defaults = defaults;
			if ((defaults & 4) != 0) {
				Notification r0_Notification = mNotification;
				r0_Notification.flags |= 1;
			}
			return this;
		}

		public NotificationCompat.Builder setDeleteIntent(PendingIntent intent) {
			mNotification.deleteIntent = intent;
			return this;
		}

		public NotificationCompat.Builder setExtras(Bundle extras) {
			mExtras = extras;
			return this;
		}

		public NotificationCompat.Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
			mFullScreenIntent = intent;
			setFlag(FLAG_HIGH_PRIORITY, highPriority);
			return this;
		}

		public NotificationCompat.Builder setGroup(String groupKey) {
			mGroupKey = groupKey;
			return this;
		}

		public NotificationCompat.Builder setGroupSummary(boolean isGroupSummary) {
			mGroupSummary = isGroupSummary;
			return this;
		}

		public NotificationCompat.Builder setLargeIcon(Bitmap icon) {
			mLargeIcon = icon;
			return this;
		}

		public NotificationCompat.Builder setLights(int argb, int onMs, int offMs) {
			boolean showLights;
			int r1i = PRIORITY_HIGH;
			mNotification.ledARGB = argb;
			mNotification.ledOnMS = onMs;
			mNotification.ledOffMS = offMs;
			if (mNotification.ledOnMS == 0 || mNotification.ledOffMS == 0) {
				showLights = false;
			} else {
				showLights = true;
			}
			Notification r3_Notification = mNotification;
			int r4i = mNotification.flags & -2;
			if (showLights) {
				r3_Notification.flags = r1i | r4i;
				return this;
			} else {
				r1i = 0;
				r3_Notification.flags = r1i | r4i;
				return this;
			}
		}

		public NotificationCompat.Builder setLocalOnly(boolean b) {
			mLocalOnly = b;
			return this;
		}

		public NotificationCompat.Builder setNumber(int number) {
			mNumber = number;
			return this;
		}

		public NotificationCompat.Builder setOngoing(boolean ongoing) {
			setFlag(PRIORITY_MAX, ongoing);
			return this;
		}

		public NotificationCompat.Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
			setFlag(FLAG_ONLY_ALERT_ONCE, onlyAlertOnce);
			return this;
		}

		public NotificationCompat.Builder setPriority(int pri) {
			mPriority = pri;
			return this;
		}

		public NotificationCompat.Builder setProgress(int max, int progress, boolean indeterminate) {
			mProgressMax = max;
			mProgress = progress;
			mProgressIndeterminate = indeterminate;
			return this;
		}

		public NotificationCompat.Builder setSmallIcon(int icon) {
			mNotification.icon = icon;
			return this;
		}

		public NotificationCompat.Builder setSmallIcon(int icon, int level) {
			mNotification.icon = icon;
			mNotification.iconLevel = level;
			return this;
		}

		public NotificationCompat.Builder setSortKey(String sortKey) {
			mSortKey = sortKey;
			return this;
		}

		public NotificationCompat.Builder setSound(Uri sound) {
			mNotification.sound = sound;
			mNotification.audioStreamType = -1;
			return this;
		}

		public NotificationCompat.Builder setSound(Uri sound, int streamType) {
			mNotification.sound = sound;
			mNotification.audioStreamType = streamType;
			return this;
		}

		public NotificationCompat.Builder setStyle(NotificationCompat.Style style) {
			if (mStyle != style) {
				mStyle = style;
				if (mStyle != null) {
					mStyle.setBuilder(this);
				}
			}
			return this;
		}

		public NotificationCompat.Builder setSubText(CharSequence text) {
			mSubText = text;
			return this;
		}

		public NotificationCompat.Builder setTicker(CharSequence tickerText) {
			mNotification.tickerText = tickerText;
			return this;
		}

		public NotificationCompat.Builder setTicker(CharSequence tickerText, RemoteViews views) {
			mNotification.tickerText = tickerText;
			mTickerView = views;
			return this;
		}

		public NotificationCompat.Builder setUsesChronometer(boolean b) {
			mUseChronometer = b;
			return this;
		}

		public NotificationCompat.Builder setVibrate(long[] pattern) {
			mNotification.vibrate = pattern;
			return this;
		}

		public NotificationCompat.Builder setWhen(long when) {
			mNotification.when = when;
			return this;
		}
	}

	public static interface Extender {
		public NotificationCompat.Builder extend(NotificationCompat.Builder r1_NotificationCompat_Builder);
	}

	static interface NotificationCompatImpl {
		public Notification build(NotificationCompat.Builder r1_NotificationCompat_Builder);

		public NotificationCompat.Action getAction(Notification r1_Notification, int r2i);

		public int getActionCount(Notification r1_Notification);

		public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> r1_ArrayList_Parcelable);

		public Bundle getExtras(Notification r1_Notification);

		public String getGroup(Notification r1_Notification);

		public boolean getLocalOnly(Notification r1_Notification);

		public ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompat.Action[] r1_NotificationCompat_Action_A);

		public String getSortKey(Notification r1_Notification);

		public boolean isGroupSummary(Notification r1_Notification);
	}

	public static abstract class Style {
		CharSequence mBigContentTitle;
		NotificationCompat.Builder mBuilder;
		CharSequence mSummaryText;
		boolean mSummaryTextSet;

		public Style() {
			super();
			mSummaryTextSet = false;
		}

		public Notification build() {
			Notification notification = null;
			if (mBuilder != null) {
				notification = mBuilder.build();
			}
			return notification;
		}

		public void setBuilder(NotificationCompat.Builder builder) {
			if (mBuilder != builder) {
				mBuilder = builder;
				if (mBuilder != null) {
					mBuilder.setStyle(this);
				}
			}
		}
	}

	public static class Action extends NotificationCompatBase.Action {
		public static final Factory FACTORY;
		public PendingIntent actionIntent;
		public int icon;
		private final Bundle mExtras;
		private final RemoteInput[] mRemoteInputs;
		public CharSequence title;

		public static final class Builder {
			private final Bundle mExtras;
			private final int mIcon;
			private final PendingIntent mIntent;
			private ArrayList<RemoteInput> mRemoteInputs;
			private final CharSequence mTitle;

			public Builder(int icon, CharSequence title, PendingIntent intent) {
				this(icon, title, intent, new Bundle());
			}

			private Builder(int icon, CharSequence title, PendingIntent intent, Bundle extras) {
				super();
				mIcon = icon;
				mTitle = title;
				mIntent = intent;
				mExtras = extras;
			}

			public Builder(NotificationCompat.Action action) {
				this(action.icon, action.title, action.actionIntent, new Bundle(action.mExtras));
			}

			public NotificationCompat.Action.Builder addExtras(Bundle extras) {
				if (extras != null) {
					mExtras.putAll(extras);
				}
				return this;
			}

			public NotificationCompat.Action.Builder addRemoteInput(RemoteInput remoteInput) {
				if (mRemoteInputs == null) {
					mRemoteInputs = new ArrayList();
				}
				mRemoteInputs.add(remoteInput);
				return this;
			}

			public NotificationCompat.Action build() {
				RemoteInput[] remoteInputs;
				if (mRemoteInputs != null) {
					remoteInputs = (RemoteInput[]) mRemoteInputs.toArray(new RemoteInput[mRemoteInputs.size()]);
				} else {
					remoteInputs = null;
				}
				return new NotificationCompat.Action(mIcon, mTitle, mIntent, mExtras, remoteInputs, null);
			}

			public NotificationCompat.Action.Builder extend(NotificationCompat.Action.Extender extender) {
				extender.extend(this);
				return this;
			}

			public Bundle getExtras() {
				return mExtras;
			}
		}

		public static interface Extender {
			public NotificationCompat.Action.Builder extend(NotificationCompat.Action.Builder r1_NotificationCompat_Action_Builder);
		}

		static class AnonymousClass_1 implements Factory {
			AnonymousClass_1() {
				super();
			}

			public NotificationCompat.Action build(int icon, CharSequence title, PendingIntent actionIntent, Bundle extras, RemoteInput[] remoteInputs) {
				return new NotificationCompat.Action(icon, title, actionIntent, extras, (RemoteInput[]) ((RemoteInput[]) remoteInputs), null);
			}

			public NotificationCompat.Action[] newArray(int length) {
				return new NotificationCompat.Action[length];
			}
		}

		public static final class WearableExtender implements NotificationCompat.Action.Extender {
			private static final int DEFAULT_FLAGS = 1;
			private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
			private static final int FLAG_AVAILABLE_OFFLINE = 1;
			private static final String KEY_FLAGS = "flags";
			private int mFlags;

			public WearableExtender() {
				super();
				mFlags = 1;
			}

			public WearableExtender(NotificationCompat.Action action) {
				super();
				mFlags = 1;
				Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
				if (wearableBundle != null) {
					mFlags = wearableBundle.getInt(KEY_FLAGS, FLAG_AVAILABLE_OFFLINE);
				}
			}

			private void setFlag(int mask, boolean value) {
				if (value) {
					mFlags |= mask;
				} else {
					mFlags &= mask ^ -1;
				}
			}

			public NotificationCompat.Action.WearableExtender clone() throws CloneNotSupportedException {
				NotificationCompat.Action.WearableExtender that = new NotificationCompat.Action.WearableExtender();
				that.mFlags = mFlags;
				return that;
			}

			public NotificationCompat.Action.Builder extend(NotificationCompat.Action.Builder builder) {
				Bundle wearableBundle = new Bundle();
				if (mFlags != 1) {
					wearableBundle.putInt(KEY_FLAGS, mFlags);
				}
				builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
				return builder;
			}

			public boolean isAvailableOffline() {
				if ((mFlags & 1) != 0) {
					return true;
				} else {
					return false;
				}
			}

			public NotificationCompat.Action.WearableExtender setAvailableOffline(boolean availableOffline) {
				setFlag(FLAG_AVAILABLE_OFFLINE, availableOffline);
				return this;
			}
		}


		static {
			FACTORY = new AnonymousClass_1();
		}

		public Action(int icon, CharSequence title, PendingIntent intent) {
			this(icon, title, intent, new Bundle(), null);
		}

		private Action(int icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs) {
			super();
			this.icon = icon;
			this.title = title;
			actionIntent = intent;
			if (extras != null) {
				mExtras = extras;
				mRemoteInputs = remoteInputs;
			} else {
				extras = new Bundle();
				mExtras = extras;
				mRemoteInputs = remoteInputs;
			}
		}

		/* synthetic */ Action(int x0, CharSequence x1, PendingIntent x2, Bundle x3, RemoteInput[] x4, NotificationCompat.AnonymousClass_1 x5) {
			this(x0, x1, x2, x3, x4);
		}

		protected PendingIntent getActionIntent() {
			return actionIntent;
		}

		public Bundle getExtras() {
			return mExtras;
		}

		protected int getIcon() {
			return icon;
		}

		public RemoteInput[] getRemoteInputs() {
			return mRemoteInputs;
		}

		protected CharSequence getTitle() {
			return title;
		}
	}

	public static class BigPictureStyle extends NotificationCompat.Style {
		Bitmap mBigLargeIcon;
		boolean mBigLargeIconSet;
		Bitmap mPicture;

		public BigPictureStyle() {
			super();
		}

		public BigPictureStyle(NotificationCompat.Builder builder) {
			super();
			setBuilder(builder);
		}

		public NotificationCompat.BigPictureStyle bigLargeIcon(Bitmap b) {
			mBigLargeIcon = b;
			mBigLargeIconSet = true;
			return this;
		}

		public NotificationCompat.BigPictureStyle bigPicture(Bitmap b) {
			mPicture = b;
			return this;
		}

		public NotificationCompat.BigPictureStyle setBigContentTitle(CharSequence title) {
			mBigContentTitle = title;
			return this;
		}

		public NotificationCompat.BigPictureStyle setSummaryText(CharSequence cs) {
			mSummaryText = cs;
			mSummaryTextSet = true;
			return this;
		}
	}

	public static class BigTextStyle extends NotificationCompat.Style {
		CharSequence mBigText;

		public BigTextStyle() {
			super();
		}

		public BigTextStyle(NotificationCompat.Builder builder) {
			super();
			setBuilder(builder);
		}

		public NotificationCompat.BigTextStyle bigText(CharSequence cs) {
			mBigText = cs;
			return this;
		}

		public NotificationCompat.BigTextStyle setBigContentTitle(CharSequence title) {
			mBigContentTitle = title;
			return this;
		}

		public NotificationCompat.BigTextStyle setSummaryText(CharSequence cs) {
			mSummaryText = cs;
			mSummaryTextSet = true;
			return this;
		}
	}

	public static class InboxStyle extends NotificationCompat.Style {
		ArrayList<CharSequence> mTexts;

		public InboxStyle() {
			super();
			mTexts = new ArrayList();
		}

		public InboxStyle(NotificationCompat.Builder builder) {
			super();
			mTexts = new ArrayList();
			setBuilder(builder);
		}

		public NotificationCompat.InboxStyle addLine(CharSequence cs) {
			mTexts.add(cs);
			return this;
		}

		public NotificationCompat.InboxStyle setBigContentTitle(CharSequence title) {
			mBigContentTitle = title;
			return this;
		}

		public NotificationCompat.InboxStyle setSummaryText(CharSequence cs) {
			mSummaryText = cs;
			mSummaryTextSet = true;
			return this;
		}
	}

	static class NotificationCompatImplBase implements NotificationCompat.NotificationCompatImpl {
		NotificationCompatImplBase() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			Notification result = b.mNotification;
			result.setLatestEventInfo(b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent);
			if (b.mPriority > 0) {
				result.flags |= 128;
			}
			return result;
		}

		public NotificationCompat.Action getAction(Notification n, int actionIndex) {
			return null;
		}

		public int getActionCount(Notification n) {
			return PRIORITY_DEFAULT;
		}

		public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
			return null;
		}

		public Bundle getExtras(Notification n) {
			return null;
		}

		public String getGroup(Notification n) {
			return null;
		}

		public boolean getLocalOnly(Notification n) {
			return false;
		}

		public ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompat.Action[] actions) {
			return null;
		}

		public String getSortKey(Notification n) {
			return null;
		}

		public boolean isGroupSummary(Notification n) {
			return false;
		}
	}

	public static final class WearableExtender implements NotificationCompat.Extender {
		private static final int DEFAULT_CONTENT_ICON_GRAVITY = 8388613;
		private static final int DEFAULT_FLAGS = 1;
		private static final int DEFAULT_GRAVITY = 80;
		private static final String EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS";
		private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE = 1;
		private static final int FLAG_HINT_HIDE_ICON = 2;
		private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY = 4;
		private static final int FLAG_START_SCROLL_BOTTOM = 8;
		private static final String KEY_ACTIONS = "actions";
		private static final String KEY_BACKGROUND = "background";
		private static final String KEY_CONTENT_ACTION_INDEX = "contentActionIndex";
		private static final String KEY_CONTENT_ICON = "contentIcon";
		private static final String KEY_CONTENT_ICON_GRAVITY = "contentIconGravity";
		private static final String KEY_CUSTOM_CONTENT_HEIGHT = "customContentHeight";
		private static final String KEY_CUSTOM_SIZE_PRESET = "customSizePreset";
		private static final String KEY_DISPLAY_INTENT = "displayIntent";
		private static final String KEY_FLAGS = "flags";
		private static final String KEY_GRAVITY = "gravity";
		private static final String KEY_PAGES = "pages";
		public static final int SIZE_DEFAULT = 0;
		public static final int SIZE_FULL_SCREEN = 5;
		public static final int SIZE_LARGE = 4;
		public static final int SIZE_MEDIUM = 3;
		public static final int SIZE_SMALL = 2;
		public static final int SIZE_XSMALL = 1;
		public static final int UNSET_ACTION_INDEX = -1;
		private ArrayList<NotificationCompat.Action> mActions;
		private Bitmap mBackground;
		private int mContentActionIndex;
		private int mContentIcon;
		private int mContentIconGravity;
		private int mCustomContentHeight;
		private int mCustomSizePreset;
		private PendingIntent mDisplayIntent;
		private int mFlags;
		private int mGravity;
		private ArrayList<Notification> mPages;

		public WearableExtender() {
			super();
			mActions = new ArrayList();
			mFlags = 1;
			mPages = new ArrayList();
			mContentIconGravity = 8388613;
			mContentActionIndex = -1;
			mCustomSizePreset = 0;
			mGravity = 80;
		}

		public WearableExtender(Notification notif) {
			Bundle wearableBundle;
			super();
			mActions = new ArrayList();
			mFlags = 1;
			mPages = new ArrayList();
			mContentIconGravity = 8388613;
			mContentActionIndex = -1;
			mCustomSizePreset = 0;
			mGravity = 80;
			Bundle extras = NotificationCompat.getExtras(notif);
			if (extras != null) {
				wearableBundle = extras.getBundle(EXTRA_WEARABLE_EXTENSIONS);
			} else {
				wearableBundle = null;
			}
			if (wearableBundle != null) {
				NotificationCompat.Action[] actions = IMPL.getActionsFromParcelableArrayList(wearableBundle.getParcelableArrayList(KEY_ACTIONS));
				if (actions != null) {
					Collections.addAll(mActions, actions);
				}
				mFlags = wearableBundle.getInt(KEY_FLAGS, SIZE_XSMALL);
				mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
				Notification[] pages = NotificationCompat.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
				if (pages != null) {
					Collections.addAll(mPages, pages);
				}
				mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
				mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
				mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, DEFAULT_CONTENT_ICON_GRAVITY);
				mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, UNSET_ACTION_INDEX);
				mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, SIZE_DEFAULT);
				mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
				mGravity = wearableBundle.getInt(KEY_GRAVITY, DEFAULT_GRAVITY);
			}
		}

		private void setFlag(int mask, boolean value) {
			if (value) {
				mFlags |= mask;
			} else {
				mFlags &= mask ^ -1;
			}
		}

		public NotificationCompat.WearableExtender addAction(NotificationCompat.Action action) {
			mActions.add(action);
			return this;
		}

		public NotificationCompat.WearableExtender addActions(List<NotificationCompat.Action> actions) {
			mActions.addAll(actions);
			return this;
		}

		public NotificationCompat.WearableExtender addPage(Notification page) {
			mPages.add(page);
			return this;
		}

		public NotificationCompat.WearableExtender addPages(List<Notification> pages) {
			mPages.addAll(pages);
			return this;
		}

		public NotificationCompat.WearableExtender clearActions() {
			mActions.clear();
			return this;
		}

		public NotificationCompat.WearableExtender clearPages() {
			mPages.clear();
			return this;
		}

		public NotificationCompat.WearableExtender clone() throws CloneNotSupportedException {
			NotificationCompat.WearableExtender that = new NotificationCompat.WearableExtender();
			that.mActions = new ArrayList(mActions);
			that.mFlags = mFlags;
			that.mDisplayIntent = mDisplayIntent;
			that.mPages = new ArrayList(mPages);
			that.mBackground = mBackground;
			that.mContentIcon = mContentIcon;
			that.mContentIconGravity = mContentIconGravity;
			that.mContentActionIndex = mContentActionIndex;
			that.mCustomSizePreset = mCustomSizePreset;
			that.mCustomContentHeight = mCustomContentHeight;
			that.mGravity = mGravity;
			return that;
		}

		public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
			Bundle wearableBundle = new Bundle();
			if (!mActions.isEmpty()) {
				wearableBundle.putParcelableArrayList(KEY_ACTIONS, IMPL.getParcelableArrayListForActions((NotificationCompat.Action[]) mActions.toArray(new NotificationCompat.Action[mActions.size()])));
			}
			if (mFlags != 1) {
				wearableBundle.putInt(KEY_FLAGS, mFlags);
			}
			if (mDisplayIntent != null) {
				wearableBundle.putParcelable(KEY_DISPLAY_INTENT, mDisplayIntent);
			}
			if (!mPages.isEmpty()) {
				wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) mPages.toArray(new Notification[mPages.size()]));
			}
			if (mBackground != null) {
				wearableBundle.putParcelable(KEY_BACKGROUND, mBackground);
			}
			if (mContentIcon != 0) {
				wearableBundle.putInt(KEY_CONTENT_ICON, mContentIcon);
			}
			if (mContentIconGravity != 8388613) {
				wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, mContentIconGravity);
			}
			if (mContentActionIndex != -1) {
				wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, mContentActionIndex);
			}
			if (mCustomSizePreset != 0) {
				wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, mCustomSizePreset);
			}
			if (mCustomContentHeight != 0) {
				wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, mCustomContentHeight);
			}
			if (mGravity != 80) {
				wearableBundle.putInt(KEY_GRAVITY, mGravity);
			}
			builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
			return builder;
		}

		public List<NotificationCompat.Action> getActions() {
			return mActions;
		}

		public Bitmap getBackground() {
			return mBackground;
		}

		public int getContentAction() {
			return mContentActionIndex;
		}

		public int getContentIcon() {
			return mContentIcon;
		}

		public int getContentIconGravity() {
			return mContentIconGravity;
		}

		public boolean getContentIntentAvailableOffline() {
			if ((mFlags & 1) != 0) {
				return true;
			} else {
				return false;
			}
		}

		public int getCustomContentHeight() {
			return mCustomContentHeight;
		}

		public int getCustomSizePreset() {
			return mCustomSizePreset;
		}

		public PendingIntent getDisplayIntent() {
			return mDisplayIntent;
		}

		public int getGravity() {
			return mGravity;
		}

		public boolean getHintHideIcon() {
			if ((mFlags & 2) != 0) {
				return true;
			} else {
				return false;
			}
		}

		public boolean getHintShowBackgroundOnly() {
			if ((mFlags & 4) != 0) {
				return true;
			} else {
				return false;
			}
		}

		public List<Notification> getPages() {
			return mPages;
		}

		public boolean getStartScrollBottom() {
			if ((mFlags & 8) != 0) {
				return true;
			} else {
				return false;
			}
		}

		public NotificationCompat.WearableExtender setBackground(Bitmap background) {
			mBackground = background;
			return this;
		}

		public NotificationCompat.WearableExtender setContentAction(int actionIndex) {
			mContentActionIndex = actionIndex;
			return this;
		}

		public NotificationCompat.WearableExtender setContentIcon(int icon) {
			mContentIcon = icon;
			return this;
		}

		public NotificationCompat.WearableExtender setContentIconGravity(int contentIconGravity) {
			mContentIconGravity = contentIconGravity;
			return this;
		}

		public NotificationCompat.WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
			setFlag(SIZE_XSMALL, contentIntentAvailableOffline);
			return this;
		}

		public NotificationCompat.WearableExtender setCustomContentHeight(int height) {
			mCustomContentHeight = height;
			return this;
		}

		public NotificationCompat.WearableExtender setCustomSizePreset(int sizePreset) {
			mCustomSizePreset = sizePreset;
			return this;
		}

		public NotificationCompat.WearableExtender setDisplayIntent(PendingIntent intent) {
			mDisplayIntent = intent;
			return this;
		}

		public NotificationCompat.WearableExtender setGravity(int gravity) {
			mGravity = gravity;
			return this;
		}

		public NotificationCompat.WearableExtender setHintHideIcon(boolean hintHideIcon) {
			setFlag(SIZE_SMALL, hintHideIcon);
			return this;
		}

		public NotificationCompat.WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
			setFlag(SIZE_LARGE, hintShowBackgroundOnly);
			return this;
		}

		public NotificationCompat.WearableExtender setStartScrollBottom(boolean startScrollBottom) {
			setFlag(FLAG_START_SCROLL_BOTTOM, startScrollBottom);
			return this;
		}
	}

	static class NotificationCompatImplGingerbread extends NotificationCompat.NotificationCompatImplBase {
		NotificationCompatImplGingerbread() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			Notification result = b.mNotification;
			result.setLatestEventInfo(b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent);
			result = NotificationCompatGingerbread.add(result, b.mContext, b.mContentTitle, b.mContentText, b.mContentIntent, b.mFullScreenIntent);
			if (b.mPriority > 0) {
				result.flags |= 128;
			}
			return result;
		}
	}

	static class NotificationCompatImplHoneycomb extends NotificationCompat.NotificationCompatImplBase {
		NotificationCompatImplHoneycomb() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			return NotificationCompatHoneycomb.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon);
		}
	}

	static class NotificationCompatImplIceCreamSandwich extends NotificationCompat.NotificationCompatImplBase {
		NotificationCompatImplIceCreamSandwich() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			return NotificationCompatIceCreamSandwich.add(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate);
		}
	}

	static class NotificationCompatImplJellybean extends NotificationCompat.NotificationCompatImplBase {
		NotificationCompatImplJellybean() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			NotificationCompatJellybean.Builder builder = new NotificationCompatJellybean.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
			NotificationCompat.addActionsToBuilder(builder, b.mActions);
			NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
			return builder.build();
		}

		public NotificationCompat.Action getAction(Notification n, int actionIndex) {
			return (NotificationCompat.Action) NotificationCompatJellybean.getAction(n, actionIndex, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
		}

		public int getActionCount(Notification n) {
			return NotificationCompatJellybean.getActionCount(n);
		}

		public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
			return (NotificationCompat.Action[]) NotificationCompatJellybean.getActionsFromParcelableArrayList(parcelables, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
		}

		public Bundle getExtras(Notification n) {
			return NotificationCompatJellybean.getExtras(n);
		}

		public String getGroup(Notification n) {
			return NotificationCompatJellybean.getGroup(n);
		}

		public boolean getLocalOnly(Notification n) {
			return NotificationCompatJellybean.getLocalOnly(n);
		}

		public ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompat.Action[] actions) {
			return NotificationCompatJellybean.getParcelableArrayListForActions(actions);
		}

		public String getSortKey(Notification n) {
			return NotificationCompatJellybean.getSortKey(n);
		}

		public boolean isGroupSummary(Notification n) {
			return NotificationCompatJellybean.isGroupSummary(n);
		}
	}

	static class NotificationCompatImplKitKat extends NotificationCompat.NotificationCompatImplJellybean {
		NotificationCompatImplKitKat() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			NotificationCompatKitKat.Builder builder = new NotificationCompatKitKat.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
			NotificationCompat.addActionsToBuilder(builder, b.mActions);
			NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
			return builder.build();
		}

		public NotificationCompat.Action getAction(Notification n, int actionIndex) {
			return (NotificationCompat.Action) NotificationCompatKitKat.getAction(n, actionIndex, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
		}

		public int getActionCount(Notification n) {
			return NotificationCompatKitKat.getActionCount(n);
		}

		public Bundle getExtras(Notification n) {
			return NotificationCompatKitKat.getExtras(n);
		}

		public String getGroup(Notification n) {
			return NotificationCompatKitKat.getGroup(n);
		}

		public boolean getLocalOnly(Notification n) {
			return NotificationCompatKitKat.getLocalOnly(n);
		}

		public String getSortKey(Notification n) {
			return NotificationCompatKitKat.getSortKey(n);
		}

		public boolean isGroupSummary(Notification n) {
			return NotificationCompatKitKat.isGroupSummary(n);
		}
	}

	static class NotificationCompatImplApi20 extends NotificationCompat.NotificationCompatImplKitKat {
		NotificationCompatImplApi20() {
			super();
		}

		public Notification build(NotificationCompat.Builder b) {
			NotificationCompatApi20.Builder builder = new NotificationCompatApi20.Builder(b.mContext, b.mNotification, b.mContentTitle, b.mContentText, b.mContentInfo, b.mTickerView, b.mNumber, b.mContentIntent, b.mFullScreenIntent, b.mLargeIcon, b.mProgressMax, b.mProgress, b.mProgressIndeterminate, b.mUseChronometer, b.mPriority, b.mSubText, b.mLocalOnly, b.mExtras, b.mGroupKey, b.mGroupSummary, b.mSortKey);
			NotificationCompat.addActionsToBuilder(builder, b.mActions);
			NotificationCompat.addStyleToBuilderJellybean(builder, b.mStyle);
			return builder.build();
		}

		public NotificationCompat.Action getAction(Notification n, int actionIndex) {
			return (NotificationCompat.Action) NotificationCompatApi20.getAction(n, actionIndex, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
		}

		public NotificationCompat.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> parcelables) {
			return (NotificationCompat.Action[]) NotificationCompatApi20.getActionsFromParcelableArrayList(parcelables, NotificationCompat.Action.FACTORY, RemoteInput.FACTORY);
		}

		public String getGroup(Notification n) {
			return NotificationCompatApi20.getGroup(n);
		}

		public boolean getLocalOnly(Notification n) {
			return NotificationCompatApi20.getLocalOnly(n);
		}

		public ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompat.Action[] actions) {
			return NotificationCompatApi20.getParcelableArrayListForActions(actions);
		}

		public String getSortKey(Notification n) {
			return NotificationCompatApi20.getSortKey(n);
		}

		public boolean isGroupSummary(Notification n) {
			return NotificationCompatApi20.isGroupSummary(n);
		}
	}


	static {
		if (VERSION.SDK_INT >= 20) {
			IMPL = new NotificationCompatImplApi20();
		} else if (VERSION.SDK_INT >= 19) {
			IMPL = new NotificationCompatImplKitKat();
		} else if (VERSION.SDK_INT >= 16) {
			IMPL = new NotificationCompatImplJellybean();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new NotificationCompatImplIceCreamSandwich();
		} else if (VERSION.SDK_INT >= 11) {
			IMPL = new NotificationCompatImplHoneycomb();
		} else if (VERSION.SDK_INT >= 9) {
			IMPL = new NotificationCompatImplGingerbread();
		} else {
			IMPL = new NotificationCompatImplBase();
		}
	}

	public NotificationCompat() {
		super();
	}

	private static void addActionsToBuilder(NotificationBuilderWithActions builder, ArrayList<Action> actions) {
		Iterator i$ = actions.iterator();
		while (i$.hasNext()) {
			builder.addAction((Action) i$.next());
		}
	}

	private static void addStyleToBuilderJellybean(NotificationBuilderWithBuilderAccessor builder, Style style) {
		if (style != null) {
			if (style instanceof BigTextStyle) {
				BigTextStyle bigTextStyle = (BigTextStyle) style;
				NotificationCompatJellybean.addBigTextStyle(builder, bigTextStyle.mBigContentTitle, bigTextStyle.mSummaryTextSet, bigTextStyle.mSummaryText, bigTextStyle.mBigText);
			} else if (style instanceof InboxStyle) {
				InboxStyle inboxStyle = (InboxStyle) style;
				NotificationCompatJellybean.addInboxStyle(builder, inboxStyle.mBigContentTitle, inboxStyle.mSummaryTextSet, inboxStyle.mSummaryText, inboxStyle.mTexts);
			} else if (style instanceof BigPictureStyle) {
				BigPictureStyle bigPictureStyle = (BigPictureStyle) style;
				NotificationCompatJellybean.addBigPictureStyle(builder, bigPictureStyle.mBigContentTitle, bigPictureStyle.mSummaryTextSet, bigPictureStyle.mSummaryText, bigPictureStyle.mPicture, bigPictureStyle.mBigLargeIcon, bigPictureStyle.mBigLargeIconSet);
			}
		}
	}

	public static Action getAction(Notification notif, int actionIndex) {
		return IMPL.getAction(notif, actionIndex);
	}

	public static int getActionCount(Notification notif) {
		return IMPL.getActionCount(notif);
	}

	public static Bundle getExtras(Notification notif) {
		return IMPL.getExtras(notif);
	}

	public static String getGroup(Notification notif) {
		return IMPL.getGroup(notif);
	}

	public static boolean getLocalOnly(Notification notif) {
		return IMPL.getLocalOnly(notif);
	}

	private static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
		Parcelable[] array = bundle.getParcelableArray(key);
		if (array instanceof Notification[] || array == null) {
			return (Notification[]) ((Notification[]) array);
		} else {
			Notification[] typedArray = new Notification[array.length];
			int i = PRIORITY_DEFAULT;
			while (i < array.length) {
				typedArray[i] = (Notification) array[i];
				i++;
			}
			bundle.putParcelableArray(key, typedArray);
			return typedArray;
		}
	}

	public static String getSortKey(Notification notif) {
		return IMPL.getSortKey(notif);
	}

	public static boolean isGroupSummary(Notification notif) {
		return IMPL.isGroupSummary(notif);
	}
}
