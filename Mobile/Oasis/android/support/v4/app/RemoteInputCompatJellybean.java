package android.support.v4.app;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory;

class RemoteInputCompatJellybean {
	public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
	private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
	private static final String KEY_CHOICES = "choices";
	private static final String KEY_EXTRAS = "extras";
	private static final String KEY_LABEL = "label";
	private static final String KEY_RESULT_KEY = "resultKey";
	public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";

	RemoteInputCompatJellybean() {
		super();
	}

	static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
		Bundle resultsBundle = new Bundle();
		RemoteInput[] arr$ = remoteInputs;
		int i$ = 0;
		while (i$ < arr$.length) {
			RemoteInput remoteInput = arr$[i$];
			Object result = results.get(remoteInput.getResultKey());
			if (result instanceof CharSequence) {
				resultsBundle.putCharSequence(remoteInput.getResultKey(), (CharSequence) result);
			}
			i$++;
		}
		Intent clipIntent = new Intent();
		clipIntent.putExtra(EXTRA_RESULTS_DATA, resultsBundle);
		intent.setClipData(ClipData.newIntent(RESULTS_CLIP_LABEL, clipIntent));
	}

	static RemoteInput fromBundle(Bundle data, Factory factory) {
		return factory.build(data.getString(KEY_RESULT_KEY), data.getCharSequence(KEY_LABEL), data.getCharSequenceArray(KEY_CHOICES), data.getBoolean(KEY_ALLOW_FREE_FORM_INPUT), data.getBundle(KEY_EXTRAS));
	}

	static RemoteInput[] fromBundleArray(Bundle[] bundles, Factory factory) {
		if (bundles == null) {
			return null;
		} else {
			RemoteInput[] remoteInputs = factory.newArray(bundles.length);
			int i = 0;
			while (i < bundles.length) {
				remoteInputs[i] = fromBundle(bundles[i], factory);
				i++;
			}
			return remoteInputs;
		}
	}

	static Bundle getResultsFromIntent(Intent intent) {
		ClipData clipData = intent.getClipData();
		if (clipData == null) {
			return null;
		} else {
			ClipDescription clipDescription = clipData.getDescription();
			if (clipDescription.hasMimeType("text/vnd.android.intent")) {
				if (clipDescription.getLabel().equals(RESULTS_CLIP_LABEL)) {
					return (Bundle) clipData.getItemAt(0).getIntent().getExtras().getParcelable(EXTRA_RESULTS_DATA);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	static Bundle toBundle(RemoteInput remoteInput) {
		Bundle data = new Bundle();
		data.putString(KEY_RESULT_KEY, remoteInput.getResultKey());
		data.putCharSequence(KEY_LABEL, remoteInput.getLabel());
		data.putCharSequenceArray(KEY_CHOICES, remoteInput.getChoices());
		data.putBoolean(KEY_ALLOW_FREE_FORM_INPUT, remoteInput.getAllowFreeFormInput());
		data.putBundle(KEY_EXTRAS, remoteInput.getExtras());
		return data;
	}

	static Bundle[] toBundleArray(RemoteInput[] remoteInputs) {
		if (remoteInputs == null) {
			return null;
		} else {
			Bundle[] bundles = new Bundle[remoteInputs.length];
			int i = 0;
			while (i < remoteInputs.length) {
				bundles[i] = toBundle(remoteInputs[i]);
				i++;
			}
			return bundles;
		}
	}
}
