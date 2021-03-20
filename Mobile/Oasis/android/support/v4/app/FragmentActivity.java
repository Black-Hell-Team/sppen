package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FragmentActivity extends Activity {
	static final String FRAGMENTS_TAG = "android:support:fragments";
	private static final int HONEYCOMB = 11;
	static final int MSG_REALLY_STOPPED = 1;
	static final int MSG_RESUME_PENDING = 2;
	private static final String TAG = "FragmentActivity";
	SimpleArrayMap<String, LoaderManagerImpl> mAllLoaderManagers;
	boolean mCheckedForLoaderManager;
	final FragmentContainer mContainer;
	boolean mCreated;
	final FragmentManagerImpl mFragments;
	final Handler mHandler;
	LoaderManagerImpl mLoaderManager;
	boolean mLoadersStarted;
	boolean mOptionsMenuInvalidated;
	boolean mReallyStopped;
	boolean mResumed;
	boolean mRetaining;
	boolean mStopped;

	class AnonymousClass_1 extends Handler {
		final /* synthetic */ FragmentActivity this$0;

		AnonymousClass_1(FragmentActivity r1_FragmentActivity) {
			super();
			this$0 = r1_FragmentActivity;
		}

		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_REALLY_STOPPED:
				if (this$0.mStopped) {
					this$0.doReallyStop(false);
				}
			case MSG_RESUME_PENDING:
				this$0.onResumeFragments();
				this$0.mFragments.execPendingActions();
			}
			super.handleMessage(msg);
		}
	}

	static class FragmentTag {
		public static final int[] Fragment;
		public static final int Fragment_id = 1;
		public static final int Fragment_name = 0;
		public static final int Fragment_tag = 2;

		static {
			Fragment = new int[]{16842755, 16842960, 16842961};
		}

		FragmentTag() {
			super();
		}
	}

	static final class NonConfigurationInstances {
		Object activity;
		SimpleArrayMap<String, Object> children;
		Object custom;
		ArrayList<Fragment> fragments;
		SimpleArrayMap<String, LoaderManagerImpl> loaders;

		NonConfigurationInstances() {
			super();
		}
	}

	class AnonymousClass_2 implements FragmentContainer {
		final /* synthetic */ FragmentActivity this$0;

		AnonymousClass_2(FragmentActivity r1_FragmentActivity) {
			super();
			this$0 = r1_FragmentActivity;
		}

		public View findViewById(int id) {
			return this$0.findViewById(id);
		}
	}


	public FragmentActivity() {
		super();
		mHandler = new AnonymousClass_1(this);
		mFragments = new FragmentManagerImpl();
		mContainer = new AnonymousClass_2(this);
	}

	private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
		writer.print(prefix);
		if (view == null) {
			writer.println("null");
		} else {
			writer.println(viewToString(view));
			if (view instanceof ViewGroup) {
				ViewGroup grp = (ViewGroup) view;
				int N = grp.getChildCount();
				if (N > 0) {
					prefix = prefix + "  ";
					int i = 0;
					while (i < N) {
						dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
						i++;
					}
				}
			}
		}
	}

	private static String viewToString(View view) {
		char r6c;
		int id;
		Resources r;
		String pkgname;
		char r7c = 'F';
		char r8c = '.';
		StringBuilder out = new StringBuilder(128);
		out.append(view.getClass().getName());
		out.append('{');
		out.append(Integer.toHexString(System.identityHashCode(view)));
		out.append(' ');
		switch(view.getVisibility()) {
		case WearableExtender.SIZE_DEFAULT:
			out.append('V');
			if (!view.isFocusable()) {
				r6c = 'F';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isEnabled()) {
				r6c = 'E';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.willNotDraw()) {
				r6c = '.';
			} else {
				r6c = 'D';
			}
			out.append(r6c);
			if (!view.isHorizontalScrollBarEnabled()) {
				r6c = 'H';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isVerticalScrollBarEnabled()) {
				r6c = 'V';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isClickable()) {
				r6c = 'C';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isLongClickable()) {
				r6c = 'L';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			out.append(' ');
			if (!view.isFocused()) {
				out.append(r7c);
				if (!view.isSelected()) {
					r6c = 'S';
				} else {
					r6c = '.';
				}
				out.append(r6c);
				if (!view.isPressed()) {
					r8c = 'P';
				}
				out.append(r8c);
				out.append(' ');
				out.append(view.getLeft());
				out.append(',');
				out.append(view.getTop());
				out.append('-');
				out.append(view.getRight());
				out.append(',');
				out.append(view.getBottom());
				id = view.getId();
				if (id != -1) {
					out.append(" #");
					out.append(Integer.toHexString(id));
					r = view.getResources();
					if (id == 0 || r == null) {
						out.append("}");
						return out.toString();
					} else {
						switch((-16777216 & id)) {
						case ViewCompat.MEASURED_STATE_TOO_SMALL:
							pkgname = "android";
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							break;
						case 2130706432:
							pkgname = "app";
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							break;
						}
						pkgname = r.getResourcePackageName(id);
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
					}
				}
				out.append("}");
				return out.toString();
			} else {
				r7c = '.';
				out.append(r7c);
				if (!view.isSelected()) {
					r6c = '.';
				} else {
					r6c = 'S';
				}
				out.append(r6c);
				if (!view.isPressed()) {
					out.append(r8c);
					out.append(' ');
					out.append(view.getLeft());
					out.append(',');
					out.append(view.getTop());
					out.append('-');
					out.append(view.getRight());
					out.append(',');
					out.append(view.getBottom());
					id = view.getId();
					if (id != -1) {
						out.append("}");
						return out.toString();
					} else {
						out.append(" #");
						out.append(Integer.toHexString(id));
						r = view.getResources();
						if (id == 0 || r == null) {
							out.append("}");
							return out.toString();
						} else {
							switch((-16777216 & id)) {
							case ViewCompat.MEASURED_STATE_TOO_SMALL:
								pkgname = "android";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								out.append("}");
								return out.toString();
							case 2130706432:
								pkgname = "app";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								out.append("}");
								return out.toString();
							}
							pkgname = r.getResourcePackageName(id);
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							out.append("}");
							return out.toString();
						}
					}
				} else {
					r8c = 'P';
					out.append(r8c);
					out.append(' ');
					out.append(view.getLeft());
					out.append(',');
					out.append(view.getTop());
					out.append('-');
					out.append(view.getRight());
					out.append(',');
					out.append(view.getBottom());
					id = view.getId();
					if (id != -1) {
						out.append(" #");
						out.append(Integer.toHexString(id));
						r = view.getResources();
						if (id == 0 || r == null) {
							out.append("}");
							return out.toString();
						} else {
							switch((-16777216 & id)) {
							case ViewCompat.MEASURED_STATE_TOO_SMALL:
								pkgname = "android";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								break;
							case 2130706432:
								pkgname = "app";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								break;
							}
							pkgname = r.getResourcePackageName(id);
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
						}
					}
					out.append("}");
					return out.toString();
				}
			}
		case TransportMediator.FLAG_KEY_MEDIA_PLAY:
			out.append('I');
			if (!view.isFocusable()) {
				r6c = '.';
			} else {
				r6c = 'F';
			}
			out.append(r6c);
			if (!view.isEnabled()) {
				r6c = '.';
			} else {
				r6c = 'E';
			}
			out.append(r6c);
			if (!view.willNotDraw()) {
				r6c = 'D';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isHorizontalScrollBarEnabled()) {
				r6c = '.';
			} else {
				r6c = 'H';
			}
			out.append(r6c);
			if (!view.isVerticalScrollBarEnabled()) {
				r6c = '.';
			} else {
				r6c = 'V';
			}
			out.append(r6c);
			if (!view.isClickable()) {
				r6c = '.';
			} else {
				r6c = 'C';
			}
			out.append(r6c);
			if (!view.isLongClickable()) {
				r6c = '.';
			} else {
				r6c = 'L';
			}
			out.append(r6c);
			out.append(' ');
			if (!view.isFocused()) {
				r7c = '.';
			}
			out.append(r7c);
			if (!view.isSelected()) {
				r6c = 'S';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isPressed()) {
				r8c = 'P';
			}
			out.append(r8c);
			out.append(' ');
			out.append(view.getLeft());
			out.append(',');
			out.append(view.getTop());
			out.append('-');
			out.append(view.getRight());
			out.append(',');
			out.append(view.getBottom());
			id = view.getId();
			if (id != -1) {
				out.append("}");
				return out.toString();
			} else {
				out.append(" #");
				out.append(Integer.toHexString(id));
				r = view.getResources();
				if (id == 0 || r == null) {
					out.append("}");
					return out.toString();
				} else {
					switch((-16777216 & id)) {
					case ViewCompat.MEASURED_STATE_TOO_SMALL:
						pkgname = "android";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						out.append("}");
						return out.toString();
					case 2130706432:
						pkgname = "app";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						out.append("}");
						return out.toString();
					}
					pkgname = r.getResourcePackageName(id);
					out.append(" ");
					out.append(pkgname);
					out.append(":");
					out.append(r.getResourceTypeName(id));
					out.append("/");
					out.append(r.getResourceEntryName(id));
					out.append("}");
					return out.toString();
				}
			}
		case TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE:
			out.append('G');
			if (!view.isFocusable()) {
				r6c = 'F';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isEnabled()) {
				r6c = 'E';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.willNotDraw()) {
				r6c = '.';
			} else {
				r6c = 'D';
			}
			out.append(r6c);
			if (!view.isHorizontalScrollBarEnabled()) {
				r6c = 'H';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isVerticalScrollBarEnabled()) {
				r6c = 'V';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isClickable()) {
				r6c = 'C';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			if (!view.isLongClickable()) {
				r6c = 'L';
			} else {
				r6c = '.';
			}
			out.append(r6c);
			out.append(' ');
			if (!view.isFocused()) {
				out.append(r7c);
				if (!view.isSelected()) {
					r6c = '.';
				} else {
					r6c = 'S';
				}
				out.append(r6c);
				if (!view.isPressed()) {
					out.append(r8c);
					out.append(' ');
					out.append(view.getLeft());
					out.append(',');
					out.append(view.getTop());
					out.append('-');
					out.append(view.getRight());
					out.append(',');
					out.append(view.getBottom());
					id = view.getId();
					if (id != -1) {
						out.append(" #");
						out.append(Integer.toHexString(id));
						r = view.getResources();
						if (id == 0 || r == null) {
							out.append("}");
							return out.toString();
						} else {
							switch((-16777216 & id)) {
							case ViewCompat.MEASURED_STATE_TOO_SMALL:
								pkgname = "android";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								break;
							case 2130706432:
								pkgname = "app";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								break;
							}
							pkgname = r.getResourcePackageName(id);
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
						}
					}
					out.append("}");
					return out.toString();
				} else {
					r8c = 'P';
					out.append(r8c);
					out.append(' ');
					out.append(view.getLeft());
					out.append(',');
					out.append(view.getTop());
					out.append('-');
					out.append(view.getRight());
					out.append(',');
					out.append(view.getBottom());
					id = view.getId();
					if (id != -1) {
						out.append("}");
						return out.toString();
					} else {
						out.append(" #");
						out.append(Integer.toHexString(id));
						r = view.getResources();
						if (id == 0 || r == null) {
							out.append("}");
							return out.toString();
						} else {
							switch((-16777216 & id)) {
							case ViewCompat.MEASURED_STATE_TOO_SMALL:
								pkgname = "android";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								out.append("}");
								return out.toString();
							case 2130706432:
								pkgname = "app";
								out.append(" ");
								out.append(pkgname);
								out.append(":");
								out.append(r.getResourceTypeName(id));
								out.append("/");
								out.append(r.getResourceEntryName(id));
								out.append("}");
								return out.toString();
							}
							pkgname = r.getResourcePackageName(id);
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							out.append("}");
							return out.toString();
						}
					}
				}
			} else {
				r7c = '.';
				out.append(r7c);
				if (!view.isSelected()) {
					r6c = 'S';
				} else {
					r6c = '.';
				}
				out.append(r6c);
				if (!view.isPressed()) {
					r8c = 'P';
				}
				out.append(r8c);
				out.append(' ');
				out.append(view.getLeft());
				out.append(',');
				out.append(view.getTop());
				out.append('-');
				out.append(view.getRight());
				out.append(',');
				out.append(view.getBottom());
				id = view.getId();
				if (id != -1) {
					out.append(" #");
					out.append(Integer.toHexString(id));
					r = view.getResources();
					if (id == 0 || r == null) {
						out.append("}");
						return out.toString();
					} else {
						switch((-16777216 & id)) {
						case ViewCompat.MEASURED_STATE_TOO_SMALL:
							pkgname = "android";
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							break;
						case 2130706432:
							pkgname = "app";
							out.append(" ");
							out.append(pkgname);
							out.append(":");
							out.append(r.getResourceTypeName(id));
							out.append("/");
							out.append(r.getResourceEntryName(id));
							break;
						}
						pkgname = r.getResourcePackageName(id);
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
					}
				}
				out.append("}");
				return out.toString();
			}
		}
		out.append('.');
		if (!view.isFocusable()) {
			r6c = '.';
		} else {
			r6c = 'F';
		}
		out.append(r6c);
		if (!view.isEnabled()) {
			r6c = '.';
		} else {
			r6c = 'E';
		}
		out.append(r6c);
		if (!view.willNotDraw()) {
			r6c = 'D';
		} else {
			r6c = '.';
		}
		out.append(r6c);
		if (!view.isHorizontalScrollBarEnabled()) {
			r6c = '.';
		} else {
			r6c = 'H';
		}
		out.append(r6c);
		if (!view.isVerticalScrollBarEnabled()) {
			r6c = '.';
		} else {
			r6c = 'V';
		}
		out.append(r6c);
		if (!view.isClickable()) {
			r6c = '.';
		} else {
			r6c = 'C';
		}
		out.append(r6c);
		if (!view.isLongClickable()) {
			r6c = '.';
		} else {
			r6c = 'L';
		}
		out.append(r6c);
		out.append(' ');
		if (!view.isFocused()) {
			r7c = '.';
		}
		out.append(r7c);
		if (!view.isSelected()) {
			r6c = '.';
		} else {
			r6c = 'S';
		}
		out.append(r6c);
		if (!view.isPressed()) {
			out.append(r8c);
			out.append(' ');
			out.append(view.getLeft());
			out.append(',');
			out.append(view.getTop());
			out.append('-');
			out.append(view.getRight());
			out.append(',');
			out.append(view.getBottom());
			id = view.getId();
			if (id != -1) {
				out.append("}");
				return out.toString();
			} else {
				out.append(" #");
				out.append(Integer.toHexString(id));
				r = view.getResources();
				if (id == 0 || r == null) {
					out.append("}");
					return out.toString();
				} else {
					switch((-16777216 & id)) {
					case ViewCompat.MEASURED_STATE_TOO_SMALL:
						pkgname = "android";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						out.append("}");
						return out.toString();
					case 2130706432:
						pkgname = "app";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						out.append("}");
						return out.toString();
					}
					pkgname = r.getResourcePackageName(id);
					out.append(" ");
					out.append(pkgname);
					out.append(":");
					out.append(r.getResourceTypeName(id));
					out.append("/");
					out.append(r.getResourceEntryName(id));
					out.append("}");
					return out.toString();
				}
			}
		} else {
			r8c = 'P';
			out.append(r8c);
			out.append(' ');
			out.append(view.getLeft());
			out.append(',');
			out.append(view.getTop());
			out.append('-');
			out.append(view.getRight());
			out.append(',');
			out.append(view.getBottom());
			id = view.getId();
			if (id != -1) {
				out.append(" #");
				out.append(Integer.toHexString(id));
				r = view.getResources();
				if (id == 0 || r == null) {
					out.append("}");
					return out.toString();
				} else {
					switch((-16777216 & id)) {
					case ViewCompat.MEASURED_STATE_TOO_SMALL:
						pkgname = "android";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						break;
					case 2130706432:
						pkgname = "app";
						out.append(" ");
						out.append(pkgname);
						out.append(":");
						out.append(r.getResourceTypeName(id));
						out.append("/");
						out.append(r.getResourceEntryName(id));
						break;
					}
					pkgname = r.getResourcePackageName(id);
					out.append(" ");
					out.append(pkgname);
					out.append(":");
					out.append(r.getResourceTypeName(id));
					out.append("/");
					out.append(r.getResourceEntryName(id));
				}
			}
			out.append("}");
			return out.toString();
		}
	}

	void doReallyStop(boolean retaining) {
		if (!mReallyStopped) {
			mReallyStopped = true;
			mRetaining = retaining;
			mHandler.removeMessages(MSG_REALLY_STOPPED);
			onReallyStop();
		}
	}

	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		String innerPrefix;
		if (VERSION.SDK_INT >= 11) {
			writer.print(prefix);
			writer.print("Local FragmentActivity ");
			writer.print(Integer.toHexString(System.identityHashCode(this)));
			writer.println(" State:");
			innerPrefix = prefix + "  ";
			writer.print(innerPrefix);
			writer.print("mCreated=");
			writer.print(mCreated);
			writer.print("mResumed=");
			writer.print(mResumed);
			writer.print(" mStopped=");
			writer.print(mStopped);
			writer.print(" mReallyStopped=");
			writer.println(mReallyStopped);
			writer.print(innerPrefix);
			writer.print("mLoadersStarted=");
			writer.println(mLoadersStarted);
			if (mLoaderManager == null) {
				writer.print(prefix);
				writer.print("Loader Manager ");
				writer.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
				writer.println(":");
				mLoaderManager.dump(prefix + "  ", fd, writer, args);
			}
			mFragments.dump(prefix, fd, writer, args);
			writer.print(prefix);
			writer.println("View Hierarchy:");
			dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
		} else {
			writer.print(prefix);
			writer.print("Local FragmentActivity ");
			writer.print(Integer.toHexString(System.identityHashCode(this)));
			writer.println(" State:");
			innerPrefix = prefix + "  ";
			writer.print(innerPrefix);
			writer.print("mCreated=");
			writer.print(mCreated);
			writer.print("mResumed=");
			writer.print(mResumed);
			writer.print(" mStopped=");
			writer.print(mStopped);
			writer.print(" mReallyStopped=");
			writer.println(mReallyStopped);
			writer.print(innerPrefix);
			writer.print("mLoadersStarted=");
			writer.println(mLoadersStarted);
			if (mLoaderManager == null) {
				mFragments.dump(prefix, fd, writer, args);
				writer.print(prefix);
				writer.println("View Hierarchy:");
				dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
			} else {
				writer.print(prefix);
				writer.print("Loader Manager ");
				writer.print(Integer.toHexString(System.identityHashCode(mLoaderManager)));
				writer.println(":");
				mLoaderManager.dump(prefix + "  ", fd, writer, args);
				mFragments.dump(prefix, fd, writer, args);
				writer.print(prefix);
				writer.println("View Hierarchy:");
				dumpViewHierarchy(prefix + "  ", writer, getWindow().getDecorView());
			}
		}
	}

	public Object getLastCustomNonConfigurationInstance() {
		NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
		if (nc != null) {
			return nc.custom;
		} else {
			return null;
		}
	}

	LoaderManagerImpl getLoaderManager(String who, boolean started, boolean create) {
		if (mAllLoaderManagers == null) {
			mAllLoaderManagers = new SimpleArrayMap();
		}
		LoaderManagerImpl lm = (LoaderManagerImpl) mAllLoaderManagers.get(who);
		if (lm == null) {
			if (create) {
				lm = new LoaderManagerImpl(who, this, started);
				mAllLoaderManagers.put(who, lm);
				return lm;
			} else {
				return lm;
			}
		} else {
			lm.updateActivity(this);
			return lm;
		}
	}

	public FragmentManager getSupportFragmentManager() {
		return mFragments;
	}

	public LoaderManager getSupportLoaderManager() {
		if (mLoaderManager != null) {
			return mLoaderManager;
		} else {
			mCheckedForLoaderManager = true;
			mLoaderManager = getLoaderManager("(root)", mLoadersStarted, true);
			return mLoaderManager;
		}
	}

	void invalidateSupportFragment(String who) {
		if (mAllLoaderManagers != null) {
			LoaderManagerImpl lm = (LoaderManagerImpl) mAllLoaderManagers.get(who);
			if (lm == null || lm.mRetaining) {
			} else {
				lm.doDestroy();
				mAllLoaderManagers.remove(who);
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFragments.noteStateNotSaved();
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (mFragments.mActive == null || index < 0 || index >= mFragments.mActive.size()) {
				Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
			} else {
				Fragment frag = (Fragment) mFragments.mActive.get(index);
				if (frag == null) {
					Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
				} else {
					frag.onActivityResult(65535 & requestCode, resultCode, data);
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onAttachFragment(Fragment fragment) {
	}

	public void onBackPressed() {
		if (!mFragments.popBackStackImmediate()) {
			finish();
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mFragments.dispatchConfigurationChanged(newConfig);
	}

	protected void onCreate(Bundle savedInstanceState) {
		ArrayList r2_ArrayList = null;
		mFragments.attachActivity(this, mContainer, null);
		if (getLayoutInflater().getFactory() == null) {
			getLayoutInflater().setFactory(this);
		}
		super.onCreate(savedInstanceState);
		NonConfigurationInstances nc = (NonConfigurationInstances) getLastNonConfigurationInstance();
		if (nc != null) {
			mAllLoaderManagers = nc.loaders;
		}
		if (savedInstanceState != null) {
			Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
			FragmentManagerImpl r3_FragmentManagerImpl = mFragments;
			if (nc != null) {
				r2_ArrayList = nc.fragments;
			}
			r3_FragmentManagerImpl.restoreAllState(p, r2_ArrayList);
		}
		mFragments.dispatchCreate();
	}

	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		if (featureId == 0) {
			boolean show = super.onCreatePanelMenu(featureId, menu) | mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
			if (VERSION.SDK_INT >= HONEYCOMB) {
				return show;
			} else {
				return true;
			}
		} else {
			return super.onCreatePanelMenu(featureId, menu);
		}
	}

	/* JADX WARNING: inconsistent code */
	/*
	public android.view.View onCreateView(java.lang.String r12_name, @android.support.annotation.NonNull android.content.Context r13_context, @android.support.annotation.NonNull android.util.AttributeSet r14_attrs) {
		r11_this = this;
		r3 = 0;
		r1 = 0;
		r10 = 1;
		r8 = -1;
		r7 = "fragment";
		r7 = r7.equals(r12_name);
		if (r7 != 0) goto L_0x0011;
	L_0x000c:
		r7 = super.onCreateView(r12_name, r13_context, r14_attrs);
	L_0x0010:
		return r7;
	L_0x0011:
		r7 = "class";
		r2 = r14_attrs.getAttributeValue(r3, r7);
		r7 = android.support.v4.app.FragmentActivity.FragmentTag.Fragment;
		r0 = r13_context.obtainStyledAttributes(r14_attrs, r7);
		if (r2_fname != 0) goto L_0x0023;
	L_0x001f:
		r2_fname = r0_a.getString(r1);
	L_0x0023:
		r4 = r0_a.getResourceId(r10, r8);
		r7 = 2;
		r6 = r0_a.getString(r7);
		r0_a.recycle();
		r7 = android.support.v4.app.Fragment.isSupportFragmentClass(r11, r2_fname);
		if (r7 != 0) goto L_0x003a;
	L_0x0035:
		r7 = super.onCreateView(r12_name, r13_context, r14_attrs);
		goto L_0x0010;
	L_0x003a:
		r5 = 0;
		if (r5_parent == 0) goto L_0x0041;
	L_0x003d:
		r1 = r5_parent.getId();
	L_0x0041:
		if (r1_containerId != r8) goto L_0x0068;
	L_0x0043:
		if (r4_id != r8) goto L_0x0068;
	L_0x0045:
		if (r6_tag != 0) goto L_0x0068;
	L_0x0047:
		r7 = new java.lang.IllegalArgumentException;
		r8 = new java.lang.StringBuilder;
		r8.<init>();
		r9 = r14_attrs.getPositionDescription();
		r8 = r8.append(r9);
		r9 = ": Must specify unique android:id, android:tag, or have a parent with an id for ";
		r8 = r8.append(r9);
		r8 = r8.append(r2_fname);
		r8 = r8.toString();
		r7.<init>(r8);
		throw r7;
	L_0x0068:
		if (r4_id == r8) goto L_0x0070;
	L_0x006a:
		r7 = r11.mFragments;
		r3 = r7.findFragmentById(r4_id);
	L_0x0070:
		if (r3_fragment != 0) goto L_0x007a;
	L_0x0072:
		if (r6_tag == 0) goto L_0x007a;
	L_0x0074:
		r7 = r11.mFragments;
		r3_fragment = r7.findFragmentByTag(r6_tag);
	L_0x007a:
		if (r3_fragment != 0) goto L_0x0084;
	L_0x007c:
		if (r1_containerId == r8) goto L_0x0084;
	L_0x007e:
		r7 = r11.mFragments;
		r3_fragment = r7.findFragmentById(r1_containerId);
	L_0x0084:
		r7 = android.support.v4.app.FragmentManagerImpl.DEBUG;
		if (r7 == 0) goto L_0x00b8;
	L_0x0088:
		r7 = "FragmentActivity";
		r8 = new java.lang.StringBuilder;
		r8.<init>();
		r9 = "onCreateView: id=0x";
		r8 = r8.append(r9);
		r9 = java.lang.Integer.toHexString(r4_id);
		r8 = r8.append(r9);
		r9 = " fname=";
		r8 = r8.append(r9);
		r8 = r8.append(r2_fname);
		r9 = " existing=";
		r8 = r8.append(r9);
		r8 = r8.append(r3_fragment);
		r8 = r8.toString();
		android.util.Log.v(r7, r8);
	L_0x00b8:
		if (r3_fragment != 0) goto L_0x00fe;
	L_0x00ba:
		r3_fragment = android.support.v4.app.Fragment.instantiate(r11, r2_fname);
		r3_fragment.mFromLayout = r10;
		if (r4_id == 0) goto L_0x00fc;
	L_0x00c2:
		r7 = r4_id;
	L_0x00c3:
		r3_fragment.mFragmentId = r7;
		r3_fragment.mContainerId = r1_containerId;
		r3_fragment.mTag = r6_tag;
		r3_fragment.mInLayout = r10;
		r7 = r11.mFragments;
		r3_fragment.mFragmentManager = r7;
		r7 = r3_fragment.mSavedFragmentState;
		r3_fragment.onInflate(r11, r14_attrs, r7);
		r7 = r11.mFragments;
		r7.addFragment(r3_fragment, r10);
	L_0x00d9:
		r7 = r3_fragment.mView;
		if (r7 != 0) goto L_0x015a;
	L_0x00dd:
		r7 = new java.lang.IllegalStateException;
		r8 = new java.lang.StringBuilder;
		r8.<init>();
		r9 = "Fragment ";
		r8 = r8.append(r9);
		r8 = r8.append(r2_fname);
		r9 = " did not create a view.";
		r8 = r8.append(r9);
		r8 = r8.toString();
		r7.<init>(r8);
		throw r7;
	L_0x00fc:
		r7 = r1_containerId;
		goto L_0x00c3;
	L_0x00fe:
		r7 = r3_fragment.mInLayout;
		if (r7 == 0) goto L_0x0149;
	L_0x0102:
		r7 = new java.lang.IllegalArgumentException;
		r8 = new java.lang.StringBuilder;
		r8.<init>();
		r9 = r14_attrs.getPositionDescription();
		r8 = r8.append(r9);
		r9 = ": Duplicate id 0x";
		r8 = r8.append(r9);
		r9 = java.lang.Integer.toHexString(r4_id);
		r8 = r8.append(r9);
		r9 = ", tag ";
		r8 = r8.append(r9);
		r8 = r8.append(r6_tag);
		r9 = ", or parent id 0x";
		r8 = r8.append(r9);
		r9 = java.lang.Integer.toHexString(r1_containerId);
		r8 = r8.append(r9);
		r9 = " with another fragment for ";
		r8 = r8.append(r9);
		r8 = r8.append(r2_fname);
		r8 = r8.toString();
		r7.<init>(r8);
		throw r7;
	L_0x0149:
		r3_fragment.mInLayout = r10;
		r7 = r3_fragment.mRetaining;
		if (r7 != 0) goto L_0x0154;
	L_0x014f:
		r7 = r3_fragment.mSavedFragmentState;
		r3_fragment.onInflate(r11, r14_attrs, r7);
	L_0x0154:
		r7 = r11.mFragments;
		r7.moveToState(r3_fragment);
		goto L_0x00d9;
	L_0x015a:
		if (r4_id == 0) goto L_0x0161;
	L_0x015c:
		r7 = r3_fragment.mView;
		r7.setId(r4_id);
	L_0x0161:
		r7 = r3_fragment.mView;
		r7 = r7.getTag();
		if (r7 != 0) goto L_0x016e;
	L_0x0169:
		r7 = r3_fragment.mView;
		r7.setTag(r6_tag);
	L_0x016e:
		r7 = r3_fragment.mView;
		goto L_0x0010;
	}
	*/
	public View onCreateView(String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		Fragment fragment = null;
		int containerId = 0;
		if (!"fragment".equals(name)) {
			return super.onCreateView(name, context, attrs);
		} else {
			String fname = attrs.getAttributeValue(null, "class");
			TypedArray a = context.obtainStyledAttributes(attrs, FragmentTag.Fragment);
			if (fname == null) {
				fname = a.getString(0);
			}
			int id = a.getResourceId(MSG_REALLY_STOPPED, -1);
			String tag = a.getString(MSG_RESUME_PENDING);
			a.recycle();
			if (!Fragment.isSupportFragmentClass(this, fname)) {
				return super.onCreateView(name, context, attrs);
			} else {
				if (false) {
					containerId = null.getId();
				}
				if (containerId != -1 || id != -1 || tag != null) {
					int r7i;
					if (fragment != null || tag == null) {
						if (!FragmentManagerImpl.DEBUG) {
							Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment);
						}
						if (fragment == null) {
							fragment = Fragment.instantiate(this, fname);
							fragment.mFromLayout = true;
							if (id != 0) {
								r7i = id;
							} else {
								r7i = containerId;
							}
							fragment.mFragmentId = r7i;
							fragment.mContainerId = containerId;
							fragment.mTag = tag;
							fragment.mInLayout = true;
							fragment.mFragmentManager = mFragments;
							fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
							mFragments.addFragment(fragment, true);
						} else if (fragment.mInLayout) {
							throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
						} else {
							fragment.mInLayout = true;
							if (!fragment.mRetaining) {
								fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
							}
							mFragments.moveToState(fragment);
						}
						if (fragment.mView == null) {
							throw new IllegalStateException("Fragment " + fname + " did not create a view.");
						} else {
							if (id != 0) {
								fragment.mView.setId(id);
							}
							if (fragment.mView.getTag() != null) {
								fragment.mView.setTag(tag);
							}
							return fragment.mView;
						}
					} else {
						fragment = mFragments.findFragmentByTag(tag);
						if (!FragmentManagerImpl.DEBUG) {
							if (fragment == null) {
								if (fragment.mInLayout) {
									fragment.mInLayout = true;
									if (!fragment.mRetaining) {
										mFragments.moveToState(fragment);
									} else {
										fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
										mFragments.moveToState(fragment);
									}
								} else {
									throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
								}
							} else {
								fragment = Fragment.instantiate(this, fname);
								fragment.mFromLayout = true;
								if (id != 0) {
									r7i = containerId;
								} else {
									r7i = id;
								}
								fragment.mFragmentId = r7i;
								fragment.mContainerId = containerId;
								fragment.mTag = tag;
								fragment.mInLayout = true;
								fragment.mFragmentManager = mFragments;
								fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
								mFragments.addFragment(fragment, true);
							}
							if (fragment.mView == null) {
								if (id != 0) {
									if (fragment.mView.getTag() != null) {
										return fragment.mView;
									} else {
										fragment.mView.setTag(tag);
										return fragment.mView;
									}
								} else {
									fragment.mView.setId(id);
									if (fragment.mView.getTag() != null) {
										fragment.mView.setTag(tag);
									}
									return fragment.mView;
								}
							} else {
								throw new IllegalStateException("Fragment " + fname + " did not create a view.");
							}
						} else {
							Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment);
							if (fragment == null) {
								fragment = Fragment.instantiate(this, fname);
								fragment.mFromLayout = true;
								if (id != 0) {
									r7i = id;
								} else {
									r7i = containerId;
								}
								fragment.mFragmentId = r7i;
								fragment.mContainerId = containerId;
								fragment.mTag = tag;
								fragment.mInLayout = true;
								fragment.mFragmentManager = mFragments;
								fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
								mFragments.addFragment(fragment, true);
							} else if (fragment.mInLayout) {
								throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
							} else {
								fragment.mInLayout = true;
								if (!fragment.mRetaining) {
									fragment.onInflate(this, attrs, fragment.mSavedFragmentState);
								}
								mFragments.moveToState(fragment);
							}
							if (fragment.mView == null) {
								throw new IllegalStateException("Fragment " + fname + " did not create a view.");
							} else {
								if (id != 0) {
									fragment.mView.setId(id);
								}
								if (fragment.mView.getTag() != null) {
									return fragment.mView;
								} else {
									fragment.mView.setTag(tag);
									return fragment.mView;
								}
							}
						}
					}
				} else {
					throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname);
				}
			}
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		doReallyStop(false);
		mFragments.dispatchDestroy();
		if (mLoaderManager != null) {
			mLoaderManager.doDestroy();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (VERSION.SDK_INT >= 5 || keyCode != 4 || event.getRepeatCount() != 0) {
			return super.onKeyDown(keyCode, event);
		} else {
			onBackPressed();
			return true;
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		mFragments.dispatchLowMemory();
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (super.onMenuItemSelected(featureId, item)) {
			return true;
		} else {
			switch(featureId) {
			case WearableExtender.SIZE_DEFAULT:
				return mFragments.dispatchOptionsItemSelected(item);
			case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT:
				return mFragments.dispatchContextItemSelected(item);
			}
			return false;
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mFragments.noteStateNotSaved();
	}

	public void onPanelClosed(int featureId, Menu menu) {
		switch(featureId) {
		case WearableExtender.SIZE_DEFAULT:
			mFragments.dispatchOptionsMenuClosed(menu);
			break;
		}
		super.onPanelClosed(featureId, menu);
	}

	protected void onPause() {
		super.onPause();
		mResumed = false;
		if (mHandler.hasMessages(MSG_RESUME_PENDING)) {
			mHandler.removeMessages(MSG_RESUME_PENDING);
			onResumeFragments();
		}
		mFragments.dispatchPause();
	}

	protected void onPostResume() {
		super.onPostResume();
		mHandler.removeMessages(MSG_RESUME_PENDING);
		onResumeFragments();
		mFragments.execPendingActions();
	}

	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		return super.onPreparePanel(0, view, menu);
	}

	public boolean onPreparePanel(int featureId, View view, Menu menu) {
		if (featureId != 0 || menu == null) {
			return super.onPreparePanel(featureId, view, menu);
		} else {
			if (mOptionsMenuInvalidated) {
				mOptionsMenuInvalidated = false;
				menu.clear();
				onCreatePanelMenu(featureId, menu);
			}
			return onPrepareOptionsPanel(view, menu) | mFragments.dispatchPrepareOptionsMenu(menu);
		}
	}

	void onReallyStop() {
		if (mLoadersStarted) {
			mLoadersStarted = false;
			if (mLoaderManager != null) {
				if (!mRetaining) {
					mLoaderManager.doStop();
				} else {
					mLoaderManager.doRetain();
				}
			}
		}
		mFragments.dispatchReallyStop();
	}

	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(MSG_RESUME_PENDING);
		mResumed = true;
		mFragments.execPendingActions();
	}

	protected void onResumeFragments() {
		mFragments.dispatchResume();
	}

	public Object onRetainCustomNonConfigurationInstance() {
		return null;
	}

	public final Object onRetainNonConfigurationInstance() {
		if (mStopped) {
			doReallyStop(true);
		}
		Object custom = onRetainCustomNonConfigurationInstance();
		ArrayList<Fragment> fragments = mFragments.retainNonConfig();
		boolean retainLoaders = false;
		if (mAllLoaderManagers != null) {
			int N = mAllLoaderManagers.size();
			LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
			int i = N - 1;
			while (i >= 0) {
				loaders[i] = (LoaderManagerImpl) mAllLoaderManagers.valueAt(i);
				i--;
			}
			i = 0;
			while (i < N) {
				LoaderManagerImpl lm = loaders[i];
				if (lm.mRetaining) {
					retainLoaders = true;
				} else {
					lm.doDestroy();
					mAllLoaderManagers.remove(lm.mWho);
				}
				i++;
			}
		}
		if (fragments != null || retainLoaders || custom != null) {
			NonConfigurationInstances nci = new NonConfigurationInstances();
			nci.activity = null;
			nci.custom = custom;
			nci.children = null;
			nci.fragments = fragments;
			nci.loaders = mAllLoaderManagers;
			return nci;
		} else {
			return null;
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Parcelable p = mFragments.saveAllState();
		if (p != null) {
			outState.putParcelable(FRAGMENTS_TAG, p);
		}
	}

	protected void onStart() {
		super.onStart();
		mStopped = false;
		mReallyStopped = false;
		mHandler.removeMessages(MSG_REALLY_STOPPED);
		if (!mCreated) {
			mCreated = true;
			mFragments.dispatchActivityCreated();
		}
		mFragments.noteStateNotSaved();
		mFragments.execPendingActions();
		if (!mLoadersStarted) {
			mLoadersStarted = true;
			if (mLoaderManager != null) {
				mLoaderManager.doStart();
			} else if (!mCheckedForLoaderManager) {
				mLoaderManager = getLoaderManager("(root)", mLoadersStarted, false);
				if (mLoaderManager == null || mLoaderManager.mStarted) {
					mCheckedForLoaderManager = true;
				} else {
					mLoaderManager.doStart();
				}
			}
			mCheckedForLoaderManager = true;
		}
		mFragments.dispatchStart();
		if (mAllLoaderManagers != null) {
			int N = mAllLoaderManagers.size();
			LoaderManagerImpl[] loaders = new LoaderManagerImpl[N];
			int i = N - 1;
			while (i >= 0) {
				loaders[i] = (LoaderManagerImpl) mAllLoaderManagers.valueAt(i);
				i--;
			}
			i = 0;
			while (i < N) {
				LoaderManagerImpl lm = loaders[i];
				lm.finishRetain();
				lm.doReportStart();
				i++;
			}
		}
	}

	protected void onStop() {
		super.onStop();
		mStopped = true;
		mHandler.sendEmptyMessage(MSG_REALLY_STOPPED);
		mFragments.dispatchStop();
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		if (requestCode == -1 || (-65536 & requestCode) == 0) {
			super.startActivityForResult(intent, requestCode);
		} else {
			throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
		}
	}

	public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
		if (requestCode == -1) {
			super.startActivityForResult(intent, -1);
		} else if ((-65536 & requestCode) != 0) {
			throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
		} else {
			super.startActivityForResult(intent, ((fragment.mIndex + 1) << 16) + (65535 & requestCode));
		}
	}

	public void supportInvalidateOptionsMenu() {
		if (VERSION.SDK_INT >= 11) {
			ActivityCompatHoneycomb.invalidateOptionsMenu(this);
		} else {
			mOptionsMenuInvalidated = true;
		}
	}
}
