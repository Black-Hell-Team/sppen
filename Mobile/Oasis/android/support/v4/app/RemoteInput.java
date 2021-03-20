package android.support.v4.app;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory;
import android.util.Log;

public class RemoteInput extends RemoteInputCompatBase.RemoteInput {
	public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
	public static final Factory FACTORY;
	private static final Impl IMPL;
	public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
	private static final String TAG = "RemoteInput";
	private final boolean mAllowFreeFormInput;
	private final CharSequence[] mChoices;
	private final Bundle mExtras;
	private final CharSequence mLabel;
	private final String mResultKey;

	public static final class Builder {
		private boolean mAllowFreeFormInput;
		private CharSequence[] mChoices;
		private Bundle mExtras;
		private CharSequence mLabel;
		private final String mResultKey;

		public Builder(String resultKey) {
			super();
			mAllowFreeFormInput = true;
			mExtras = new Bundle();
			if (resultKey == null) {
				throw new IllegalArgumentException("Result key can't be null");
			} else {
				mResultKey = resultKey;
			}
		}

		public RemoteInput.Builder addExtras(Bundle extras) {
			if (extras != null) {
				mExtras.putAll(extras);
			}
			return this;
		}

		public RemoteInput build() {
			return new RemoteInput(mResultKey, mLabel, mChoices, mAllowFreeFormInput, mExtras);
		}

		public Bundle getExtras() {
			return mExtras;
		}

		public RemoteInput.Builder setAllowFreeFormInput(boolean allowFreeFormInput) {
			mAllowFreeFormInput = allowFreeFormInput;
			return this;
		}

		public RemoteInput.Builder setChoices(CharSequence[] choices) {
			mChoices = choices;
			return this;
		}

		public RemoteInput.Builder setLabel(CharSequence label) {
			mLabel = label;
			return this;
		}
	}

	static interface Impl {
		public void addResultsToIntent(RemoteInput[] r1_RemoteInput_A, Intent r2_Intent, Bundle r3_Bundle);

		public Bundle getResultsFromIntent(Intent r1_Intent);
	}

	static class AnonymousClass_1 implements Factory {
		AnonymousClass_1() {
			super();
		}

		public RemoteInput build(String resultKey, CharSequence label, CharSequence[] choices, boolean allowFreeFormInput, Bundle extras) {
			return new RemoteInput(resultKey, label, choices, allowFreeFormInput, extras);
		}

		public RemoteInput[] newArray(int size) {
			return new RemoteInput[size];
		}
	}

	static class ImplApi20 implements RemoteInput.Impl {
		ImplApi20() {
			super();
		}

		public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
			RemoteInputCompatApi20.addResultsToIntent(remoteInputs, intent, results);
		}

		public Bundle getResultsFromIntent(Intent intent) {
			return RemoteInputCompatApi20.getResultsFromIntent(intent);
		}
	}

	static class ImplBase implements RemoteInput.Impl {
		ImplBase() {
			super();
		}

		public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
			Log.w(TAG, "RemoteInput is only supported from API Level 16");
		}

		public Bundle getResultsFromIntent(Intent intent) {
			Log.w(TAG, "RemoteInput is only supported from API Level 16");
			return null;
		}
	}

	static class ImplJellybean implements RemoteInput.Impl {
		ImplJellybean() {
			super();
		}

		public void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
			RemoteInputCompatJellybean.addResultsToIntent(remoteInputs, intent, results);
		}

		public Bundle getResultsFromIntent(Intent intent) {
			return RemoteInputCompatJellybean.getResultsFromIntent(intent);
		}
	}


	static {
		if (VERSION.SDK_INT >= 20) {
			IMPL = new ImplApi20();
		} else if (VERSION.SDK_INT >= 16) {
			IMPL = new ImplJellybean();
		} else {
			IMPL = new ImplBase();
		}
		FACTORY = new AnonymousClass_1();
	}

	RemoteInput(String resultKey, CharSequence label, CharSequence[] choices, boolean allowFreeFormInput, Bundle extras) {
		super();
		mResultKey = resultKey;
		mLabel = label;
		mChoices = choices;
		mAllowFreeFormInput = allowFreeFormInput;
		mExtras = extras;
	}

	public static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
		IMPL.addResultsToIntent(remoteInputs, intent, results);
	}

	public static Bundle getResultsFromIntent(Intent intent) {
		return IMPL.getResultsFromIntent(intent);
	}

	public boolean getAllowFreeFormInput() {
		return mAllowFreeFormInput;
	}

	public CharSequence[] getChoices() {
		return mChoices;
	}

	public Bundle getExtras() {
		return mExtras;
	}

	public CharSequence getLabel() {
		return mLabel;
	}

	public String getResultKey() {
		return mResultKey;
	}
}
