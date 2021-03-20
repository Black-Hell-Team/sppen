package android.support.v4.app;

import android.app.RemoteInput.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput;
import android.support.v4.app.RemoteInputCompatBase.RemoteInput.Factory;

class RemoteInputCompatApi20 {
	RemoteInputCompatApi20() {
		super();
	}

	static void addResultsToIntent(RemoteInput[] remoteInputs, Intent intent, Bundle results) {
		android.app.RemoteInput.addResultsToIntent(fromCompat(remoteInputs), intent, results);
	}

	static android.app.RemoteInput[] fromCompat(RemoteInput[] srcArray) {
		if (srcArray == null) {
			return null;
		} else {
			android.app.RemoteInput[] result = new android.app.RemoteInput[srcArray.length];
			int i = 0;
			while (i < srcArray.length) {
				RemoteInput src = srcArray[i];
				result[i] = new Builder(src.getResultKey()).setLabel(src.getLabel()).setChoices(src.getChoices()).setAllowFreeFormInput(src.getAllowFreeFormInput()).addExtras(src.getExtras()).build();
				i++;
			}
			return result;
		}
	}

	static Bundle getResultsFromIntent(Intent intent) {
		return android.app.RemoteInput.getResultsFromIntent(intent);
	}

	static RemoteInput[] toCompat(android.app.RemoteInput[] srcArray, Factory factory) {
		if (srcArray == null) {
			return null;
		} else {
			RemoteInput[] result = factory.newArray(srcArray.length);
			int i = 0;
			while (i < srcArray.length) {
				android.app.RemoteInput src = srcArray[i];
				result[i] = factory.build(src.getResultKey(), src.getLabel(), src.getChoices(), src.getAllowFreeFormInput(), src.getExtras());
				i++;
			}
			return result;
		}
	}
}
