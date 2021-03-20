package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import java.io.FileNotFoundException;

public final class PrintHelper {
	public static final int COLOR_MODE_COLOR = 2;
	public static final int COLOR_MODE_MONOCHROME = 1;
	public static final int ORIENTATION_LANDSCAPE = 1;
	public static final int ORIENTATION_PORTRAIT = 2;
	public static final int SCALE_MODE_FILL = 2;
	public static final int SCALE_MODE_FIT = 1;
	PrintHelperVersionImpl mImpl;

	static /* synthetic */ class AnonymousClass_1 {
	}

	static interface PrintHelperVersionImpl {
		public int getColorMode();

		public int getOrientation();

		public int getScaleMode();

		public void printBitmap(String r1_String, Bitmap r2_Bitmap);

		public void printBitmap(String r1_String, Uri r2_Uri) throws FileNotFoundException;

		public void setColorMode(int r1i);

		public void setOrientation(int r1i);

		public void setScaleMode(int r1i);
	}

	private static final class PrintHelperKitkatImpl implements PrintHelper.PrintHelperVersionImpl {
		private final PrintHelperKitkat mPrintHelper;

		PrintHelperKitkatImpl(Context context) {
			super();
			mPrintHelper = new PrintHelperKitkat(context);
		}

		public int getColorMode() {
			return mPrintHelper.getColorMode();
		}

		public int getOrientation() {
			return mPrintHelper.getOrientation();
		}

		public int getScaleMode() {
			return mPrintHelper.getScaleMode();
		}

		public void printBitmap(String jobName, Bitmap bitmap) {
			mPrintHelper.printBitmap(jobName, bitmap);
		}

		public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
			mPrintHelper.printBitmap(jobName, imageFile);
		}

		public void setColorMode(int colorMode) {
			mPrintHelper.setColorMode(colorMode);
		}

		public void setOrientation(int orientation) {
			mPrintHelper.setOrientation(orientation);
		}

		public void setScaleMode(int scaleMode) {
			mPrintHelper.setScaleMode(scaleMode);
		}
	}

	private static final class PrintHelperStubImpl implements PrintHelper.PrintHelperVersionImpl {
		int mColorMode;
		int mOrientation;
		int mScaleMode;

		private PrintHelperStubImpl() {
			super();
			mScaleMode = 2;
			mColorMode = 2;
			mOrientation = 1;
		}

		/* synthetic */ PrintHelperStubImpl(PrintHelper.AnonymousClass_1 x0) {
			this();
		}

		public int getColorMode() {
			return mColorMode;
		}

		public int getOrientation() {
			return mOrientation;
		}

		public int getScaleMode() {
			return mScaleMode;
		}

		public void printBitmap(String jobName, Bitmap bitmap) {
		}

		public void printBitmap(String jobName, Uri imageFile) {
		}

		public void setColorMode(int colorMode) {
			mColorMode = colorMode;
		}

		public void setOrientation(int orientation) {
			mOrientation = orientation;
		}

		public void setScaleMode(int scaleMode) {
			mScaleMode = scaleMode;
		}
	}


	public PrintHelper(Context context) {
		super();
		if (systemSupportsPrint()) {
			mImpl = new PrintHelperKitkatImpl(context);
		} else {
			mImpl = new PrintHelperStubImpl(null);
		}
	}

	public static boolean systemSupportsPrint() {
		if (VERSION.SDK_INT >= 19) {
			return true;
		} else {
			return false;
		}
	}

	public int getColorMode() {
		return mImpl.getColorMode();
	}

	public int getOrientation() {
		return mImpl.getOrientation();
	}

	public int getScaleMode() {
		return mImpl.getScaleMode();
	}

	public void printBitmap(String jobName, Bitmap bitmap) {
		mImpl.printBitmap(jobName, bitmap);
	}

	public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
		mImpl.printBitmap(jobName, imageFile);
	}

	public void setColorMode(int colorMode) {
		mImpl.setColorMode(colorMode);
	}

	public void setOrientation(int orientation) {
		mImpl.setOrientation(orientation);
	}

	public void setScaleMode(int scaleMode) {
		mImpl.setScaleMode(scaleMode);
	}
}
