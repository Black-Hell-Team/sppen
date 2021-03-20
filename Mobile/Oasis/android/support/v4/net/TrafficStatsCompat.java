package android.support.v4.net;

import android.os.Build.VERSION;
import java.net.Socket;
import java.net.SocketException;

public class TrafficStatsCompat {
	private static final TrafficStatsCompatImpl IMPL;

	static /* synthetic */ class AnonymousClass_1 {
	}

	static interface TrafficStatsCompatImpl {
		public void clearThreadStatsTag();

		public int getThreadStatsTag();

		public void incrementOperationCount(int r1i);

		public void incrementOperationCount(int r1i, int r2i);

		public void setThreadStatsTag(int r1i);

		public void tagSocket(Socket r1_Socket) throws SocketException;

		public void untagSocket(Socket r1_Socket) throws SocketException;
	}

	static class BaseTrafficStatsCompatImpl implements TrafficStatsCompat.TrafficStatsCompatImpl {
		private ThreadLocal<SocketTags> mThreadSocketTags;

		class AnonymousClass_1 extends ThreadLocal<TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags> {
			final /* synthetic */ TrafficStatsCompat.BaseTrafficStatsCompatImpl this$0;

			AnonymousClass_1(TrafficStatsCompat.BaseTrafficStatsCompatImpl r1_TrafficStatsCompat_BaseTrafficStatsCompatImpl) {
				super();
				this$0 = r1_TrafficStatsCompat_BaseTrafficStatsCompatImpl;
			}

			protected TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags initialValue() {
				return new TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags(null);
			}
		}

		private static class SocketTags {
			public int statsTag;

			private SocketTags() {
				super();
				statsTag = -1;
			}

			/* synthetic */ SocketTags(TrafficStatsCompat.AnonymousClass_1 x0) {
				this();
			}
		}


		BaseTrafficStatsCompatImpl() {
			super();
			mThreadSocketTags = new AnonymousClass_1(this);
		}

		public void clearThreadStatsTag() {
			((SocketTags) mThreadSocketTags.get()).statsTag = -1;
		}

		public int getThreadStatsTag() {
			return ((SocketTags) mThreadSocketTags.get()).statsTag;
		}

		public void incrementOperationCount(int operationCount) {
		}

		public void incrementOperationCount(int tag, int operationCount) {
		}

		public void setThreadStatsTag(int tag) {
			((SocketTags) mThreadSocketTags.get()).statsTag = tag;
		}

		public void tagSocket(Socket socket) {
		}

		public void untagSocket(Socket socket) {
		}
	}

	static class IcsTrafficStatsCompatImpl implements TrafficStatsCompat.TrafficStatsCompatImpl {
		IcsTrafficStatsCompatImpl() {
			super();
		}

		public void clearThreadStatsTag() {
			TrafficStatsCompatIcs.clearThreadStatsTag();
		}

		public int getThreadStatsTag() {
			return TrafficStatsCompatIcs.getThreadStatsTag();
		}

		public void incrementOperationCount(int operationCount) {
			TrafficStatsCompatIcs.incrementOperationCount(operationCount);
		}

		public void incrementOperationCount(int tag, int operationCount) {
			TrafficStatsCompatIcs.incrementOperationCount(tag, operationCount);
		}

		public void setThreadStatsTag(int tag) {
			TrafficStatsCompatIcs.setThreadStatsTag(tag);
		}

		public void tagSocket(Socket socket) throws SocketException {
			TrafficStatsCompatIcs.tagSocket(socket);
		}

		public void untagSocket(Socket socket) throws SocketException {
			TrafficStatsCompatIcs.untagSocket(socket);
		}
	}


	static {
		if (VERSION.SDK_INT >= 14) {
			IMPL = new IcsTrafficStatsCompatImpl();
		} else {
			IMPL = new BaseTrafficStatsCompatImpl();
		}
	}

	public TrafficStatsCompat() {
		super();
	}

	public static void clearThreadStatsTag() {
		IMPL.clearThreadStatsTag();
	}

	public static int getThreadStatsTag() {
		return IMPL.getThreadStatsTag();
	}

	public static void incrementOperationCount(int operationCount) {
		IMPL.incrementOperationCount(operationCount);
	}

	public static void incrementOperationCount(int tag, int operationCount) {
		IMPL.incrementOperationCount(tag, operationCount);
	}

	public static void setThreadStatsTag(int tag) {
		IMPL.setThreadStatsTag(tag);
	}

	public static void tagSocket(Socket socket) throws SocketException {
		IMPL.tagSocket(socket);
	}

	public static void untagSocket(Socket socket) throws SocketException {
		IMPL.untagSocket(socket);
	}
}
