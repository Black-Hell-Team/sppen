package android.support.v4.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel.Stub;

public abstract class NotificationCompatSideChannelService extends Service {
	private static final int BUILD_VERSION_CODE_KITKAT_WATCH = 20;

	static /* synthetic */ class AnonymousClass_1 {
	}

	private class NotificationSideChannelStub extends Stub {
		final /* synthetic */ NotificationCompatSideChannelService this$0;

		private NotificationSideChannelStub(NotificationCompatSideChannelService r1_NotificationCompatSideChannelService) {
			super();
			this$0 = r1_NotificationCompatSideChannelService;
		}

		/* synthetic */ NotificationSideChannelStub(NotificationCompatSideChannelService x0, NotificationCompatSideChannelService.AnonymousClass_1 x1) {
			this(x0);
		}

		public void cancel(String packageName, int id, String tag) throws RemoteException {
			this$0.checkPermission(getCallingUid(), packageName);
			long idToken = clearCallingIdentity();
			this$0.cancel(packageName, id, tag);
			restoreCallingIdentity(idToken);
		}

		public void cancelAll(String packageName) {
			this$0.checkPermission(getCallingUid(), packageName);
			long idToken = clearCallingIdentity();
			this$0.cancelAll(packageName);
			restoreCallingIdentity(idToken);
		}

		public void notify(String packageName, int id, String tag, Notification notification) throws RemoteException {
			this$0.checkPermission(getCallingUid(), packageName);
			long idToken = clearCallingIdentity();
			this$0.notify(packageName, id, tag, notification);
			restoreCallingIdentity(idToken);
		}
	}


	public NotificationCompatSideChannelService() {
		super();
	}

	private void checkPermission(int callingUid, String packageName) {
		String[] arr$ = getPackageManager().getPackagesForUid(callingUid);
		int i$ = 0;
		while (i$ < arr$.length) {
			if (arr$[i$].equals(packageName)) {
				return;
			} else {
				i$++;
			}
		}
		throw new SecurityException("NotificationSideChannelService: Uid " + callingUid + " is not authorized for package " + packageName);
	}

	public abstract void cancel(String r1_String, int r2i, String r3_String);

	public abstract void cancelAll(String r1_String);

	public abstract void notify(String r1_String, int r2i, String r3_String, Notification r4_Notification);

	public IBinder onBind(Intent intent) {
		Object r0_Object = null;
		if (intent.getAction().equals(NotificationManagerCompat.ACTION_BIND_SIDE_CHANNEL)) {
			if (VERSION.SDK_INT >= 20) {
				return null;
			} else {
				return new NotificationSideChannelStub(this, r0_Object);
			}
		} else {
			return null;
		}
	}
}
