package android.support.v4.view;

import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.View;

class KeyEventCompatEclair {
	KeyEventCompatEclair() {
		super();
	}

	public static boolean dispatch(KeyEvent event, Callback receiver, Object state, Object target) {
		return event.dispatch(receiver, (DispatcherState) state, target);
	}

	public static Object getKeyDispatcherState(View view) {
		return view.getKeyDispatcherState();
	}

	public static boolean isTracking(KeyEvent event) {
		return event.isTracking();
	}

	public static void startTracking(KeyEvent event) {
		event.startTracking();
	}
}
