package android.support.v4.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

public class TaskStackBuilder implements Iterable<Intent> {
	private static final TaskStackBuilderImpl IMPL;
	private static final String TAG = "TaskStackBuilder";
	private final ArrayList<Intent> mIntents;
	private final Context mSourceContext;

	public static interface SupportParentable {
		public Intent getSupportParentActivityIntent();
	}

	static interface TaskStackBuilderImpl {
		public PendingIntent getPendingIntent(Context r1_Context, Intent[] r2_Intent_A, int r3i, int r4i, Bundle r5_Bundle);
	}

	static class TaskStackBuilderImplBase implements TaskStackBuilder.TaskStackBuilderImpl {
		TaskStackBuilderImplBase() {
			super();
		}

		public PendingIntent getPendingIntent(Context context, Intent[] intents, int requestCode, int flags, Bundle options) {
			Intent topIntent = new Intent(intents[intents.length - 1]);
			topIntent.addFlags(268435456);
			return PendingIntent.getActivity(context, requestCode, topIntent, flags);
		}
	}

	static class TaskStackBuilderImplHoneycomb implements TaskStackBuilder.TaskStackBuilderImpl {
		TaskStackBuilderImplHoneycomb() {
			super();
		}

		public PendingIntent getPendingIntent(Context context, Intent[] intents, int requestCode, int flags, Bundle options) {
			intents[0] = new Intent(intents[0]).addFlags(268484608);
			return TaskStackBuilderHoneycomb.getActivitiesPendingIntent(context, requestCode, intents, flags);
		}
	}

	static class TaskStackBuilderImplJellybean implements TaskStackBuilder.TaskStackBuilderImpl {
		TaskStackBuilderImplJellybean() {
			super();
		}

		public PendingIntent getPendingIntent(Context context, Intent[] intents, int requestCode, int flags, Bundle options) {
			intents[0] = new Intent(intents[0]).addFlags(268484608);
			return TaskStackBuilderJellybean.getActivitiesPendingIntent(context, requestCode, intents, flags, options);
		}
	}


	static {
		if (VERSION.SDK_INT >= 11) {
			IMPL = new TaskStackBuilderImplHoneycomb();
		} else {
			IMPL = new TaskStackBuilderImplBase();
		}
	}

	private TaskStackBuilder(Context a) {
		super();
		mIntents = new ArrayList();
		mSourceContext = a;
	}

	public static TaskStackBuilder create(Context context) {
		return new TaskStackBuilder(context);
	}

	public static TaskStackBuilder from(Context context) {
		return create(context);
	}

	public TaskStackBuilder addNextIntent(Intent nextIntent) {
		mIntents.add(nextIntent);
		return this;
	}

	public TaskStackBuilder addNextIntentWithParentStack(Intent nextIntent) {
		ComponentName target = nextIntent.getComponent();
		if (target == null) {
			target = nextIntent.resolveActivity(mSourceContext.getPackageManager());
		}
		if (target != null) {
			addParentStack(target);
		}
		addNextIntent(nextIntent);
		return this;
	}

	public TaskStackBuilder addParentStack(Activity sourceActivity) {
		Intent parent = null;
		if (sourceActivity instanceof SupportParentable) {
			parent = ((SupportParentable) sourceActivity).getSupportParentActivityIntent();
		}
		if (parent == null) {
			parent = NavUtils.getParentActivityIntent(sourceActivity);
		}
		if (parent != null) {
			ComponentName target = parent.getComponent();
			if (target == null) {
				target = parent.resolveActivity(mSourceContext.getPackageManager());
			}
			addParentStack(target);
			addNextIntent(parent);
		}
		return this;
	}

	public TaskStackBuilder addParentStack(ComponentName sourceActivityName) {
		Intent parent;
		int insertAt = mIntents.size();
		try {
			parent = NavUtils.getParentActivityIntent(mSourceContext, sourceActivityName);
			while (parent != null) {
				mIntents.add(insertAt, parent);
				parent = NavUtils.getParentActivityIntent(mSourceContext, parent.getComponent());
			}
			return this;
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Bad ComponentName while traversing activity parent metadata");
			throw new IllegalArgumentException(e);
		}
	}

	public TaskStackBuilder addParentStack(Class<?> sourceActivityClass) {
		return addParentStack(new ComponentName(mSourceContext, sourceActivityClass));
	}

	public Intent editIntentAt(int index) {
		return (Intent) mIntents.get(index);
	}

	public Intent getIntent(int index) {
		return editIntentAt(index);
	}

	public int getIntentCount() {
		return mIntents.size();
	}

	public Intent[] getIntents() {
		Intent[] intents = new Intent[mIntents.size()];
		if (intents.length == 0) {
			return intents;
		} else {
			intents[0] = new Intent((Intent) mIntents.get(0)).addFlags(268484608);
			int i = 1;
			while (i < intents.length) {
				intents[i] = new Intent((Intent) mIntents.get(i));
				i++;
			}
			return intents;
		}
	}

	public PendingIntent getPendingIntent(int requestCode, int flags) {
		return getPendingIntent(requestCode, flags, null);
	}

	public PendingIntent getPendingIntent(int requestCode, int flags, Bundle options) {
		int r3i = 0;
		if (mIntents.isEmpty()) {
			throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
		} else {
			Intent[] intents = (Intent[]) mIntents.toArray(new Intent[mIntents.size()]);
			intents[r3i] = new Intent(intents[r3i]).addFlags(268484608);
			return IMPL.getPendingIntent(mSourceContext, intents, requestCode, flags, options);
		}
	}

	public Iterator<Intent> iterator() {
		return mIntents.iterator();
	}

	public void startActivities() {
		startActivities(null);
	}

	public void startActivities(Bundle options) {
		if (mIntents.isEmpty()) {
			throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
		} else {
			Intent[] intents = (Intent[]) mIntents.toArray(new Intent[mIntents.size()]);
			intents[0] = new Intent(intents[0]).addFlags(268484608);
			if (!ContextCompat.startActivities(mSourceContext, intents, options)) {
				Intent topIntent = new Intent(intents[intents.length - 1]);
				topIntent.addFlags(268435456);
				mSourceContext.startActivity(topIntent);
			}
		}
	}
}
