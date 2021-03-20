package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;

public class ShareCompat {
	public static final String EXTRA_CALLING_ACTIVITY = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
	public static final String EXTRA_CALLING_PACKAGE = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
	private static ShareCompatImpl IMPL;

	public static class IntentBuilder {
		private Activity mActivity;
		private ArrayList<String> mBccAddresses;
		private ArrayList<String> mCcAddresses;
		private CharSequence mChooserTitle;
		private Intent mIntent;
		private ArrayList<Uri> mStreams;
		private ArrayList<String> mToAddresses;

		private IntentBuilder(Activity launchingActivity) {
			super();
			mActivity = launchingActivity;
			mIntent = new Intent().setAction("android.intent.action.SEND");
			mIntent.putExtra(EXTRA_CALLING_PACKAGE, launchingActivity.getPackageName());
			mIntent.putExtra(EXTRA_CALLING_ACTIVITY, launchingActivity.getComponentName());
			mIntent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
		}

		private void combineArrayExtra(String extra, ArrayList<String> add) {
			int currentLength;
			Object currentAddresses = mIntent.getStringArrayExtra(extra);
			if (currentAddresses != null) {
				currentLength = currentAddresses.length;
			} else {
				currentLength = 0;
			}
			Object finalAddresses = new Object[(add.size() + currentLength)];
			add.toArray(finalAddresses);
			if (currentAddresses != null) {
				System.arraycopy(currentAddresses, 0, finalAddresses, add.size(), currentLength);
			}
			mIntent.putExtra(extra, finalAddresses);
		}

		private void combineArrayExtra(String extra, String[] add) {
			int oldLength;
			Intent intent = getIntent();
			Object old = intent.getStringArrayExtra(extra);
			if (old != null) {
				oldLength = old.length;
			} else {
				oldLength = 0;
			}
			Object result = new Object[(add.length + oldLength)];
			if (old != null) {
				System.arraycopy(old, 0, result, 0, oldLength);
			}
			System.arraycopy(add, 0, result, oldLength, add.length);
			intent.putExtra(extra, result);
		}

		public static ShareCompat.IntentBuilder from(Activity launchingActivity) {
			return new ShareCompat.IntentBuilder(launchingActivity);
		}

		public ShareCompat.IntentBuilder addEmailBcc(String address) {
			if (mBccAddresses == null) {
				mBccAddresses = new ArrayList();
			}
			mBccAddresses.add(address);
			return this;
		}

		public ShareCompat.IntentBuilder addEmailBcc(String[] addresses) {
			combineArrayExtra("android.intent.extra.BCC", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder addEmailCc(String address) {
			if (mCcAddresses == null) {
				mCcAddresses = new ArrayList();
			}
			mCcAddresses.add(address);
			return this;
		}

		public ShareCompat.IntentBuilder addEmailCc(String[] addresses) {
			combineArrayExtra("android.intent.extra.CC", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder addEmailTo(String address) {
			if (mToAddresses == null) {
				mToAddresses = new ArrayList();
			}
			mToAddresses.add(address);
			return this;
		}

		public ShareCompat.IntentBuilder addEmailTo(String[] addresses) {
			combineArrayExtra("android.intent.extra.EMAIL", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder addStream(Uri streamUri) {
			Uri currentStream = (Uri) mIntent.getParcelableExtra("android.intent.extra.STREAM");
			if (currentStream == null) {
				return setStream(streamUri);
			} else {
				if (mStreams == null) {
					mStreams = new ArrayList();
				}
				if (currentStream != null) {
					mIntent.removeExtra("android.intent.extra.STREAM");
					mStreams.add(currentStream);
				}
				mStreams.add(streamUri);
				return this;
			}
		}

		public Intent createChooserIntent() {
			return Intent.createChooser(getIntent(), mChooserTitle);
		}

		Activity getActivity() {
			return mActivity;
		}

		/* JADX WARNING: inconsistent code */
		/*
		public android.content.Intent getIntent() {
			r7_this = this;
			r1 = 1;
			r2 = 0;
			r6 = 0;
			r3 = r7.mToAddresses;
			if (r3 == 0) goto L_0x0010;
		L_0x0007:
			r3 = "android.intent.extra.EMAIL";
			r4 = r7.mToAddresses;
			r7.combineArrayExtra(r3, r4);
			r7.mToAddresses = r6;
		L_0x0010:
			r3 = r7.mCcAddresses;
			if (r3 == 0) goto L_0x001d;
		L_0x0014:
			r3 = "android.intent.extra.CC";
			r4 = r7.mCcAddresses;
			r7.combineArrayExtra(r3, r4);
			r7.mCcAddresses = r6;
		L_0x001d:
			r3 = r7.mBccAddresses;
			if (r3 == 0) goto L_0x002a;
		L_0x0021:
			r3 = "android.intent.extra.BCC";
			r4 = r7.mBccAddresses;
			r7.combineArrayExtra(r3, r4);
			r7.mBccAddresses = r6;
		L_0x002a:
			r3 = r7.mStreams;
			if (r3 == 0) goto L_0x008d;
		L_0x002e:
			r3 = r7.mStreams;
			r3 = r3.size();
			if (r3 <= r1) goto L_0x008d;
		L_0x0036:
			r3 = r7.mIntent;
			r3 = r3.getAction();
			r4 = "android.intent.action.SEND_MULTIPLE";
			r0 = r3.equals(r4);
			if (r1_needsSendMultiple != 0) goto L_0x006a;
		L_0x0044:
			if (r0_isSendMultiple == 0) goto L_0x006a;
		L_0x0046:
			r3 = r7.mIntent;
			r4 = "android.intent.action.SEND";
			r3.setAction(r4);
			r3 = r7.mStreams;
			if (r3 == 0) goto L_0x008f;
		L_0x0051:
			r3 = r7.mStreams;
			r3 = r3.isEmpty();
			if (r3 != 0) goto L_0x008f;
		L_0x0059:
			r3 = r7.mIntent;
			r4 = "android.intent.extra.STREAM";
			r5 = r7.mStreams;
			r2 = r5.get(r2);
			r2 = (android.os.Parcelable) r2;
			r3.putExtra(r4, r2);
		L_0x0068:
			r7.mStreams = r6;
		L_0x006a:
			if (r1_needsSendMultiple == 0) goto L_0x008a;
		L_0x006c:
			if (r0_isSendMultiple != 0) goto L_0x008a;
		L_0x006e:
			r2 = r7.mIntent;
			r3 = "android.intent.action.SEND_MULTIPLE";
			r2.setAction(r3);
			r2 = r7.mStreams;
			if (r2 == 0) goto L_0x0097;
		L_0x0079:
			r2 = r7.mStreams;
			r2 = r2.isEmpty();
			if (r2 != 0) goto L_0x0097;
		L_0x0081:
			r2 = r7.mIntent;
			r3 = "android.intent.extra.STREAM";
			r4 = r7.mStreams;
			r2.putParcelableArrayListExtra(r3, r4);
		L_0x008a:
			r2 = r7.mIntent;
			return r2;
		L_0x008d:
			r1_needsSendMultiple = r2;
			goto L_0x0036;
		L_0x008f:
			r2 = r7.mIntent;
			r3 = "android.intent.extra.STREAM";
			r2.removeExtra(r3);
			goto L_0x0068;
		L_0x0097:
			r2 = r7.mIntent;
			r3 = "android.intent.extra.STREAM";
			r2.removeExtra(r3);
			goto L_0x008a;
		}
		*/
		public Intent getIntent() {
			boolean needsSendMultiple = true;
			int r2i = 0;
			if (mToAddresses != null) {
				combineArrayExtra("android.intent.extra.EMAIL", mToAddresses);
				mToAddresses = null;
			}
			if (mCcAddresses != null) {
				combineArrayExtra("android.intent.extra.CC", mCcAddresses);
				mCcAddresses = null;
			}
			if (mBccAddresses != null) {
				combineArrayExtra("android.intent.extra.BCC", mBccAddresses);
				mBccAddresses = null;
			}
			if (mStreams == null || mStreams.size() <= 1) {
				needsSendMultiple = false;
			} else {
				isSendMultiple = mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
			}
			isSendMultiple = mIntent.getAction().equals("android.intent.action.SEND_MULTIPLE");
			if (needsSendMultiple || !isSendMultiple) {
				return mIntent;
			} else {
				mIntent.setAction("android.intent.action.SEND");
				if (mStreams == null || mStreams.isEmpty()) {
					mIntent.removeExtra("android.intent.extra.STREAM");
				} else {
					mIntent.putExtra("android.intent.extra.STREAM", (Parcelable) mStreams.get(r2i));
				}
				mStreams = null;
				return mIntent;
			}
		}

		public ShareCompat.IntentBuilder setChooserTitle(int resId) {
			return setChooserTitle(mActivity.getText(resId));
		}

		public ShareCompat.IntentBuilder setChooserTitle(CharSequence title) {
			mChooserTitle = title;
			return this;
		}

		public ShareCompat.IntentBuilder setEmailBcc(String[] addresses) {
			mIntent.putExtra("android.intent.extra.BCC", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder setEmailCc(String[] addresses) {
			mIntent.putExtra("android.intent.extra.CC", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder setEmailTo(String[] addresses) {
			if (mToAddresses != null) {
				mToAddresses = null;
			}
			mIntent.putExtra("android.intent.extra.EMAIL", addresses);
			return this;
		}

		public ShareCompat.IntentBuilder setHtmlText(String htmlText) {
			mIntent.putExtra(IntentCompat.EXTRA_HTML_TEXT, htmlText);
			if (!mIntent.hasExtra("android.intent.extra.TEXT")) {
				setText(Html.fromHtml(htmlText));
			}
			return this;
		}

		public ShareCompat.IntentBuilder setStream(Uri streamUri) {
			if (!mIntent.getAction().equals("android.intent.action.SEND")) {
				mIntent.setAction("android.intent.action.SEND");
			}
			mStreams = null;
			mIntent.putExtra("android.intent.extra.STREAM", streamUri);
			return this;
		}

		public ShareCompat.IntentBuilder setSubject(String subject) {
			mIntent.putExtra("android.intent.extra.SUBJECT", subject);
			return this;
		}

		public ShareCompat.IntentBuilder setText(CharSequence text) {
			mIntent.putExtra("android.intent.extra.TEXT", text);
			return this;
		}

		public ShareCompat.IntentBuilder setType(String mimeType) {
			mIntent.setType(mimeType);
			return this;
		}

		public void startChooser() {
			mActivity.startActivity(createChooserIntent());
		}
	}

	public static class IntentReader {
		private static final String TAG = "IntentReader";
		private Activity mActivity;
		private ComponentName mCallingActivity;
		private String mCallingPackage;
		private Intent mIntent;
		private ArrayList<Uri> mStreams;

		private IntentReader(Activity activity) {
			super();
			mActivity = activity;
			mIntent = activity.getIntent();
			mCallingPackage = ShareCompat.getCallingPackage(activity);
			mCallingActivity = ShareCompat.getCallingActivity(activity);
		}

		public static ShareCompat.IntentReader from(Activity activity) {
			return new ShareCompat.IntentReader(activity);
		}

		public ComponentName getCallingActivity() {
			return mCallingActivity;
		}

		public Drawable getCallingActivityIcon() {
			if (mCallingActivity == null) {
				return null;
			} else {
				PackageManager pm = mActivity.getPackageManager();
				try {
					return pm.getActivityIcon(mCallingActivity);
				} catch (NameNotFoundException e) {
					Log.e(TAG, "Could not retrieve icon for calling activity", e);
					return null;
				}
			}
		}

		public Drawable getCallingApplicationIcon() {
			if (mCallingPackage == null) {
				return null;
			} else {
				PackageManager pm = mActivity.getPackageManager();
				try {
					return pm.getApplicationIcon(mCallingPackage);
				} catch (NameNotFoundException e) {
					Log.e(TAG, "Could not retrieve icon for calling application", e);
					return null;
				}
			}
		}

		public CharSequence getCallingApplicationLabel() {
			if (mCallingPackage == null) {
				return null;
			} else {
				PackageManager pm = mActivity.getPackageManager();
				try {
					return pm.getApplicationLabel(pm.getApplicationInfo(mCallingPackage, 0));
				} catch (NameNotFoundException e) {
					Log.e(TAG, "Could not retrieve label for calling application", e);
					return null;
				}
			}
		}

		public String getCallingPackage() {
			return mCallingPackage;
		}

		public String[] getEmailBcc() {
			return mIntent.getStringArrayExtra("android.intent.extra.BCC");
		}

		public String[] getEmailCc() {
			return mIntent.getStringArrayExtra("android.intent.extra.CC");
		}

		public String[] getEmailTo() {
			return mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
		}

		public String getHtmlText() {
			String result = mIntent.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
			if (result == null) {
				CharSequence text = getText();
				if (text instanceof Spanned) {
					return Html.toHtml((Spanned) text);
				} else if (text != null) {
					return IMPL.escapeHtml(text);
				} else {
					return result;
				}
			} else {
				return result;
			}
		}

		public Uri getStream() {
			return (Uri) mIntent.getParcelableExtra("android.intent.extra.STREAM");
		}

		public Uri getStream(int index) {
			if (mStreams != null || !isMultipleShare()) {
				if (mStreams == null) {
					return (Uri) mStreams.get(index);
				} else if (index != 0) {
					return (Uri) mIntent.getParcelableExtra("android.intent.extra.STREAM");
				} else {
					throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + index);
				}
			} else {
				mStreams = mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
				if (mStreams == null) {
					if (index != 0) {
						throw new IndexOutOfBoundsException("Stream items available: " + getStreamCount() + " index requested: " + index);
					} else {
						return (Uri) mIntent.getParcelableExtra("android.intent.extra.STREAM");
					}
				} else {
					return (Uri) mStreams.get(index);
				}
			}
		}

		public int getStreamCount() {
			if (mStreams != null || !isMultipleShare()) {
				if (mStreams == null) {
					return mStreams.size();
				} else if (!mIntent.hasExtra("android.intent.extra.STREAM")) {
					return 1;
				} else {
					return 0;
				}
			} else {
				mStreams = mIntent.getParcelableArrayListExtra("android.intent.extra.STREAM");
				if (mStreams == null) {
					if (!mIntent.hasExtra("android.intent.extra.STREAM")) {
						return 0;
					} else {
						return 1;
					}
				} else {
					return mStreams.size();
				}
			}
		}

		public String getSubject() {
			return mIntent.getStringExtra("android.intent.extra.SUBJECT");
		}

		public CharSequence getText() {
			return mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
		}

		public String getType() {
			return mIntent.getType();
		}

		public boolean isMultipleShare() {
			return "android.intent.action.SEND_MULTIPLE".equals(mIntent.getAction());
		}

		public boolean isShareIntent() {
			String action = mIntent.getAction();
			if ("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isSingleShare() {
			return "android.intent.action.SEND".equals(mIntent.getAction());
		}
	}

	static interface ShareCompatImpl {
		public void configureMenuItem(MenuItem r1_MenuItem, ShareCompat.IntentBuilder r2_ShareCompat_IntentBuilder);

		public String escapeHtml(CharSequence r1_CharSequence);
	}

	static class ShareCompatImplBase implements ShareCompat.ShareCompatImpl {
		ShareCompatImplBase() {
			super();
		}

		private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
			int i = start;
			while (i < end) {
				char c = text.charAt(i);
				if (c == '<') {
					out.append("&lt;");
				} else if (c == '>') {
					out.append("&gt;");
				} else if (c == '&') {
					out.append("&amp;");
				} else if (c > '~' || c < ' ') {
					out.append("&#" + c + ";");
				} else if (c == ' ') {
					while (i + 1 < end && text.charAt(i + 1) == ' ') {
						out.append("&nbsp;");
						i++;
					}
					out.append(' ');
				} else {
					out.append(c);
				}
				i++;
			}
		}

		public void configureMenuItem(MenuItem item, ShareCompat.IntentBuilder shareIntent) {
			item.setIntent(shareIntent.createChooserIntent());
		}

		public String escapeHtml(CharSequence text) {
			StringBuilder out = new StringBuilder();
			withinStyle(out, text, 0, text.length());
			return out.toString();
		}
	}

	static class ShareCompatImplICS extends ShareCompat.ShareCompatImplBase {
		ShareCompatImplICS() {
			super();
		}

		public void configureMenuItem(MenuItem item, ShareCompat.IntentBuilder shareIntent) {
			ShareCompatICS.configureMenuItem(item, shareIntent.getActivity(), shareIntent.getIntent());
			if (shouldAddChooserIntent(item)) {
				item.setIntent(shareIntent.createChooserIntent());
			}
		}

		boolean shouldAddChooserIntent(MenuItem item) {
			if (!item.hasSubMenu()) {
				return true;
			} else {
				return false;
			}
		}
	}

	static class ShareCompatImplJB extends ShareCompat.ShareCompatImplICS {
		ShareCompatImplJB() {
			super();
		}

		public String escapeHtml(CharSequence html) {
			return ShareCompatJB.escapeHtml(html);
		}

		boolean shouldAddChooserIntent(MenuItem item) {
			return false;
		}
	}


	static {
		if (VERSION.SDK_INT >= 16) {
			IMPL = new ShareCompatImplJB();
		} else if (VERSION.SDK_INT >= 14) {
			IMPL = new ShareCompatImplICS();
		} else {
			IMPL = new ShareCompatImplBase();
		}
	}

	public ShareCompat() {
		super();
	}

	public static void configureMenuItem(Menu menu, int menuItemId, IntentBuilder shareIntent) {
		MenuItem item = menu.findItem(menuItemId);
		if (item == null) {
			throw new IllegalArgumentException("Could not find menu item with id " + menuItemId + " in the supplied menu");
		} else {
			configureMenuItem(item, shareIntent);
		}
	}

	public static void configureMenuItem(MenuItem item, IntentBuilder shareIntent) {
		IMPL.configureMenuItem(item, shareIntent);
	}

	public static ComponentName getCallingActivity(Activity calledActivity) {
		ComponentName result = calledActivity.getCallingActivity();
		if (result == null) {
			result = calledActivity.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY);
		}
		return result;
	}

	public static String getCallingPackage(Activity calledActivity) {
		String result = calledActivity.getCallingPackage();
		if (result == null) {
			result = calledActivity.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE);
		}
		return result;
	}
}
