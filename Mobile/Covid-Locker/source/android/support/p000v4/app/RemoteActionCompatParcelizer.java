package android.support.p000v4.app;

import androidx.core.app.RemoteActionCompat;
import androidx.versionedparcelable.VersionedParcel;

/* renamed from: android.support.v4.app.RemoteActionCompatParcelizer */
public final class RemoteActionCompatParcelizer extends androidx.core.app.RemoteActionCompatParcelizer {
    public static RemoteActionCompat read(VersionedParcel versionedParcel) {
        return androidx.core.app.RemoteActionCompatParcelizer.read(versionedParcel);
    }

    public static void write(RemoteActionCompat remoteActionCompat, VersionedParcel versionedParcel) {
        androidx.core.app.RemoteActionCompatParcelizer.write(remoteActionCompat, versionedParcel);
    }
}
