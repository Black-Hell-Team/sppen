package android.support.v4.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.xmlpull.v1.XmlPullParserException;

public class FileProvider extends ContentProvider {
	private static final String ATTR_NAME = "name";
	private static final String ATTR_PATH = "path";
	private static final String[] COLUMNS;
	private static final File DEVICE_ROOT;
	private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
	private static final String TAG_CACHE_PATH = "cache-path";
	private static final String TAG_EXTERNAL = "external-path";
	private static final String TAG_FILES_PATH = "files-path";
	private static final String TAG_ROOT_PATH = "root-path";
	private static HashMap<String, PathStrategy> sCache;
	private PathStrategy mStrategy;

	static interface PathStrategy {
		public File getFileForUri(Uri r1_Uri);

		public Uri getUriForFile(File r1_File);
	}

	static class SimplePathStrategy implements FileProvider.PathStrategy {
		private final String mAuthority;
		private final HashMap<String, File> mRoots;

		public SimplePathStrategy(String authority) {
			super();
			mRoots = new HashMap();
			mAuthority = authority;
		}

		public void addRoot(String name, File root) {
			if (TextUtils.isEmpty(name)) {
				throw new IllegalArgumentException("Name must not be empty");
			} else {
				try {
					mRoots.put(name, root.getCanonicalFile());
				} catch (IOException e) {
					throw new IllegalArgumentException("Failed to resolve canonical path for " + root, e);
				}
			}
		}

		public File getFileForUri(Uri uri) {
			String path = uri.getEncodedPath();
			int splitIndex = path.indexOf(47, 1);
			path = Uri.decode(path.substring(splitIndex + 1));
			File root = (File) mRoots.get(Uri.decode(path.substring(1, splitIndex)));
			if (root == null) {
				throw new IllegalArgumentException("Unable to find configured root for " + uri);
			} else {
				File file = new File(root, path);
				try {
					file = file.getCanonicalFile();
					if (!file.getPath().startsWith(root.getPath())) {
						throw new SecurityException("Resolved path jumped beyond configured root");
					} else {
						return file;
					}
				} catch (IOException e) {
					throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
				}
			}
		}

		public Uri getUriForFile(File file) {
			String path;
			Entry<String, File> mostSpecific;
			Iterator i$;
			String rootPath;
			try {
				path = file.getCanonicalPath();
				mostSpecific = null;
				i$ = mRoots.entrySet().iterator();
				while (i$.hasNext()) {
					Entry<String, File> root = (Entry) i$.next();
					rootPath = ((File) root.getValue()).getPath();
					if (path.startsWith(rootPath)) {
						if (mostSpecific == null || rootPath.length() > ((File) mostSpecific.getValue()).getPath().length()) {
							mostSpecific = root;
						}
					}
				}
				if (mostSpecific == null) {
					throw new IllegalArgumentException("Failed to find configured root that contains " + path);
				} else {
					rootPath = ((File) mostSpecific.getValue()).getPath();
					if (rootPath.endsWith("/")) {
						path = path.substring(rootPath.length());
					} else {
						path = path.substring(rootPath.length() + 1);
					}
					return new Builder().scheme("content").authority(mAuthority).encodedPath(Uri.encode((String) mostSpecific.getKey()) + '/' + Uri.encode(path, "/")).build();
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
			}
		}
	}


	static {
		String[] r0_String_A = new String[2];
		r0_String_A[0] = "_display_name";
		r0_String_A[1] = "_size";
		COLUMNS = r0_String_A;
		DEVICE_ROOT = new File("/");
		sCache = new HashMap();
	}

	public FileProvider() {
		super();
	}

	private static File buildPath(File base, String ... segments) {
		String[] arr$ = segments;
		int i$ = 0;
		File cur = base;
		while (i$ < arr$.length) {
			File cur_2;
			String segment = arr$[i$];
			if (segment != null) {
				cur_2 = new File(cur, segment);
			} else {
				cur_2 = cur;
			}
			i$++;
			cur = cur_2;
		}
		return cur;
	}

	private static Object[] copyOf(Object[] original, int newLength) {
		Object result = new Object[newLength];
		System.arraycopy(original, 0, result, 0, newLength);
		return result;
	}

	private static String[] copyOf(String[] original, int newLength) {
		Object result = new Object[newLength];
		System.arraycopy(original, 0, result, 0, newLength);
		return result;
	}

	private static PathStrategy getPathStrategy(Context context, String authority) {
		PathStrategy strat;
		synchronized(sCache) {
			try {
				strat = (PathStrategy) sCache.get(authority);
				if (strat == null) {
					strat = parsePathStrategy(context, authority);
					sCache.put(authority, strat);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e);
			} catch (XmlPullParserException e_2) {
				throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", e_2);
			} catch (Throwable th) {
				return th;
			}
		}
		return strat;
	}

	public static Uri getUriForFile(Context context, String authority, File file) {
		return getPathStrategy(context, authority).getUriForFile(file);
	}

	private static int modeToMode(String mode) {
		if ("r".equals(mode)) {
			return 268435456;
		} else if ("w".equals(mode) || "wt".equals(mode)) {
			return 738197504;
		} else if ("wa".equals(mode)) {
			return 704643072;
		} else if ("rw".equals(mode)) {
			return 939524096;
		} else if ("rwt".equals(mode)) {
			return 1006632960;
		} else {
			throw new IllegalArgumentException("Invalid mode: " + mode);
		}
	}

	private static PathStrategy parsePathStrategy(Context context, String authority) throws IOException, XmlPullParserException {
		SimplePathStrategy strat = new SimplePathStrategy(authority);
		XmlResourceParser in = context.getPackageManager().resolveContentProvider(authority, TransportMediator.FLAG_KEY_MEDIA_NEXT).loadXmlMetaData(context.getPackageManager(), META_DATA_FILE_PROVIDER_PATHS);
		if (in == null) {
			throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
		}
		while (true) {
			int type = in.next();
			if (type != 1) {
				if (type == 2) {
					String tag = in.getName();
					String name = in.getAttributeValue(null, ATTR_NAME);
					String path = in.getAttributeValue(null, ATTR_PATH);
					File target = null;
					String[] r9_String_A;
					if (TAG_ROOT_PATH.equals(tag)) {
						r9_String_A = new String[1];
						r9_String_A[0] = path;
						target = buildPath(DEVICE_ROOT, r9_String_A);
					} else if (TAG_FILES_PATH.equals(tag)) {
						r9_String_A = new String[1];
						r9_String_A[0] = path;
						target = buildPath(context.getFilesDir(), r9_String_A);
					} else if (TAG_CACHE_PATH.equals(tag)) {
						r9_String_A = new String[1];
						r9_String_A[0] = path;
						target = buildPath(context.getCacheDir(), r9_String_A);
					} else if (TAG_EXTERNAL.equals(tag)) {
						r9_String_A = new String[1];
						r9_String_A[0] = path;
						target = buildPath(Environment.getExternalStorageDirectory(), r9_String_A);
					}
					if (target != null) {
						strat.addRoot(name, target);
					}
				}
			} else {
				return strat;
			}
		}
	}

	public void attachInfo(Context context, ProviderInfo info) {
		super.attachInfo(context, info);
		if (info.exported) {
			throw new SecurityException("Provider must not be exported");
		} else if (!info.grantUriPermissions) {
			throw new SecurityException("Provider must grant uri permissions");
		} else {
			mStrategy = getPathStrategy(context, info.authority);
		}
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (mStrategy.getFileForUri(uri).delete()) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getType(Uri uri) {
		File file = mStrategy.getFileForUri(uri);
		int lastDot = file.getName().lastIndexOf(46);
		if (lastDot >= 0) {
			String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.getName().substring(lastDot + 1));
			if (mime != null) {
				return mime;
			}
		}
		return "application/octet-stream";
	}

	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException("No external inserts");
	}

	public boolean onCreate() {
		return true;
	}

	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		return ParcelFileDescriptor.open(mStrategy.getFileForUri(uri), modeToMode(mode));
	}

	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		File file = mStrategy.getFileForUri(uri);
		if (projection == null) {
			projection = COLUMNS;
		}
		String[] cols = new String[projection.length];
		Object[] values = new Object[projection.length];
		String[] arr$ = projection;
		int i$ = 0;
		int i = 0;
		while (i$ < arr$.length) {
			int i_2;
			String col = arr$[i$];
			if ("_display_name".equals(col)) {
				cols[i] = "_display_name";
				i_2 = i + 1;
				values[i] = file.getName();
			} else if ("_size".equals(col)) {
				cols[i] = "_size";
				i_2 = i + 1;
				values[i] = Long.valueOf(file.length());
			} else {
				i_2 = i;
			}
			i$++;
			i = i_2;
		}
		MatrixCursor cursor = new MatrixCursor(copyOf(cols, i), 1);
		cursor.addRow(copyOf(values, i));
		return cursor;
	}

	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException("No external updates");
	}
}
