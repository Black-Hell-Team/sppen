package android.support.v4.app;

import android.app.Notification;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INotificationSideChannel extends IInterface {
	public static abstract class Stub extends Binder implements INotificationSideChannel {
		private static final String DESCRIPTOR = "android.support.v4.app.INotificationSideChannel";
		static final int TRANSACTION_cancel = 2;
		static final int TRANSACTION_cancelAll = 3;
		static final int TRANSACTION_notify = 1;

		private static class Proxy implements INotificationSideChannel {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				super();
				mRemote = remote;
			}

			public IBinder asBinder() {
				return mRemote;
			}

			public void cancel(String packageName, int id, String tag) throws RemoteException {
				Parcel _data = Parcel.obtain();
				_data.writeInterfaceToken(DESCRIPTOR);
				_data.writeString(packageName);
				_data.writeInt(id);
				_data.writeString(tag);
				mRemote.transact(TRANSACTION_cancel, _data, null, TRANSACTION_notify);
				_data.recycle();
			}

			public void cancelAll(String packageName) throws RemoteException {
				Parcel _data = Parcel.obtain();
				_data.writeInterfaceToken(DESCRIPTOR);
				_data.writeString(packageName);
				mRemote.transact(TRANSACTION_cancelAll, _data, null, TRANSACTION_notify);
				_data.recycle();
			}

			public String getInterfaceDescriptor() {
				return DESCRIPTOR;
			}

			public void notify(String packageName, int id, String tag, Notification notification) throws RemoteException {
				Parcel _data = Parcel.obtain();
				_data.writeInterfaceToken(DESCRIPTOR);
				_data.writeString(packageName);
				_data.writeInt(id);
				_data.writeString(tag);
				if (notification != null) {
					_data.writeInt(TRANSACTION_notify);
					notification.writeToParcel(_data, 0);
				} else {
					_data.writeInt(0);
				}
				mRemote.transact(TRANSACTION_notify, _data, null, TRANSACTION_notify);
				_data.recycle();
			}
		}


		public Stub() {
			super();
			attachInterface(this, DESCRIPTOR);
		}

		public static INotificationSideChannel asInterface(IBinder obj) {
			if (obj == null) {
				return null;
			} else {
				IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
				if (iin == null || !(iin instanceof INotificationSideChannel)) {
					return new Proxy(obj);
				} else {
					return (INotificationSideChannel) iin;
				}
			}
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
			switch(code) {
			case TRANSACTION_notify:
				Notification _arg3;
				data.enforceInterface(DESCRIPTOR);
				String _arg0 = data.readString();
				int _arg1 = data.readInt();
				String _arg2 = data.readString();
				if (data.readInt() != 0) {
					_arg3 = (Notification) Notification.CREATOR.createFromParcel(data);
				} else {
					_arg3 = null;
				}
				notify(_arg0, _arg1, _arg2, _arg3);
				return true;
			case TRANSACTION_cancel:
				data.enforceInterface(DESCRIPTOR);
				cancel(data.readString(), data.readInt(), data.readString());
				return true;
			case TRANSACTION_cancelAll:
				data.enforceInterface(DESCRIPTOR);
				cancelAll(data.readString());
				return true;
			case 1598968902:
				reply.writeString(DESCRIPTOR);
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}
	}


	public void cancel(String r1_String, int r2i, String r3_String) throws RemoteException;

	public void cancelAll(String r1_String) throws RemoteException;

	public void notify(String r1_String, int r2i, String r3_String, Notification r4_Notification) throws RemoteException;
}
