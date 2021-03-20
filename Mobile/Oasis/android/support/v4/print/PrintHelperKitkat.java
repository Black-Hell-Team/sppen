package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PrintHelperKitkat {
	public static final int COLOR_MODE_COLOR = 2;
	public static final int COLOR_MODE_MONOCHROME = 1;
	private static final String LOG_TAG = "PrintHelperKitkat";
	private static final int MAX_PRINT_SIZE = 3500;
	public static final int ORIENTATION_LANDSCAPE = 1;
	public static final int ORIENTATION_PORTRAIT = 2;
	public static final int SCALE_MODE_FILL = 2;
	public static final int SCALE_MODE_FIT = 1;
	int mColorMode;
	final Context mContext;
	Options mDecodeOptions;
	private final Object mLock;
	int mOrientation;
	int mScaleMode;

	class AnonymousClass_1 extends PrintDocumentAdapter {
		private PrintAttributes mAttributes;
		final /* synthetic */ PrintHelperKitkat this$0;
		final /* synthetic */ Bitmap val$bitmap;
		final /* synthetic */ int val$fittingMode;
		final /* synthetic */ String val$jobName;

		AnonymousClass_1(PrintHelperKitkat r1_PrintHelperKitkat, String r2_String, Bitmap r3_Bitmap, int r4i) {
			super();
			this$0 = r1_PrintHelperKitkat;
			val$jobName = r2_String;
			val$bitmap = r3_Bitmap;
			val$fittingMode = r4i;
		}

		public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
			boolean changed = true;
			mAttributes = newPrintAttributes;
			PrintDocumentInfo info = new Builder(val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
			if (!newPrintAttributes.equals(oldPrintAttributes)) {
				layoutResultCallback.onLayoutFinished(info, changed);
			} else {
				changed = false;
				layoutResultCallback.onLayoutFinished(info, changed);
			}
		}

		public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
			Throwable r5_Throwable;
			PrintedPdfDocument pdfDocument = new PrintedPdfDocument(this$0.mContext, mAttributes);
			int r5i = SCALE_MODE_FIT;
			try {
				Page page = pdfDocument.startPage(r5i);
				page.getCanvas().drawBitmap(val$bitmap, this$0.getMatrix(val$bitmap.getWidth(), val$bitmap.getHeight(), new RectF(page.getInfo().getContentRect()), val$fittingMode), null);
				pdfDocument.finishPage(page);
				pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
				PageRange[] r5_PageRange_A = new PageRange[1];
				r5_PageRange_A[0] = PageRange.ALL_PAGES;
				writeResultCallback.onWriteFinished(r5_PageRange_A);
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (fileDescriptor != null) {
					try {
						fileDescriptor.close();
					} catch (IOException e) {
						return;
					}
				}
			} catch (Throwable th) {
				r5_Throwable = th;
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (fileDescriptor != null) {
					fileDescriptor.close();
					return r5_Throwable;
				} else {
					return r5_Throwable;
				}
			}
		}
	}

	class AnonymousClass_2 extends PrintDocumentAdapter {
		AsyncTask<Uri, Boolean, Bitmap> loadBitmap;
		private PrintAttributes mAttributes;
		Bitmap mBitmap;
		final /* synthetic */ PrintHelperKitkat this$0;
		final /* synthetic */ int val$fittingMode;
		final /* synthetic */ Uri val$imageFile;
		final /* synthetic */ String val$jobName;

		class AnonymousClass_1 extends AsyncTask<Uri, Boolean, Bitmap> {
			final /* synthetic */ PrintHelperKitkat.AnonymousClass_2 this$1;
			final /* synthetic */ CancellationSignal val$cancellationSignal;
			final /* synthetic */ LayoutResultCallback val$layoutResultCallback;
			final /* synthetic */ PrintAttributes val$newPrintAttributes;
			final /* synthetic */ PrintAttributes val$oldPrintAttributes;

			class AnonymousClass_1 implements OnCancelListener {
				final /* synthetic */ PrintHelperKitkat.AnonymousClass_2.AnonymousClass_1 this$2;

				AnonymousClass_1(PrintHelperKitkat.AnonymousClass_2.AnonymousClass_1 r1_PrintHelperKitkat_AnonymousClass_2_AnonymousClass_1) {
					super();
					this$2 = r1_PrintHelperKitkat_AnonymousClass_2_AnonymousClass_1;
				}

				public void onCancel() {
					this$2.this$1.cancelLoad();
					this$2.cancel(false);
				}
			}


			AnonymousClass_1(PrintHelperKitkat.AnonymousClass_2 r1_PrintHelperKitkat_AnonymousClass_2, CancellationSignal r2_CancellationSignal, PrintAttributes r3_PrintAttributes, PrintAttributes r4_PrintAttributes, LayoutResultCallback r5_LayoutResultCallback) {
				super();
				this$1 = r1_PrintHelperKitkat_AnonymousClass_2;
				val$cancellationSignal = r2_CancellationSignal;
				val$newPrintAttributes = r3_PrintAttributes;
				val$oldPrintAttributes = r4_PrintAttributes;
				val$layoutResultCallback = r5_LayoutResultCallback;
			}

			protected Bitmap doInBackground(Uri ... uris) {
				try {
					return this$1.this$0.loadConstrainedBitmap(this$1.val$imageFile, MAX_PRINT_SIZE);
				} catch (FileNotFoundException e) {
					return null;
				}
			}

			protected void onCancelled(Bitmap result) {
				val$layoutResultCallback.onLayoutCancelled();
			}

			protected void onPostExecute(Bitmap bitmap) {
				boolean changed = true;
				super.onPostExecute(bitmap);
				this$1.mBitmap = bitmap;
				if (bitmap != null) {
					PrintDocumentInfo info = new Builder(this$1.val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
					if (!val$newPrintAttributes.equals(val$oldPrintAttributes)) {
						val$layoutResultCallback.onLayoutFinished(info, changed);
					} else {
						changed = false;
						val$layoutResultCallback.onLayoutFinished(info, changed);
					}
				} else {
					val$layoutResultCallback.onLayoutFailed(null);
				}
			}

			protected void onPreExecute() {
				val$cancellationSignal.setOnCancelListener(new AnonymousClass_1(this));
			}
		}


		AnonymousClass_2(PrintHelperKitkat r2_PrintHelperKitkat, String r3_String, Uri r4_Uri, int r5i) {
			super();
			this$0 = r2_PrintHelperKitkat;
			val$jobName = r3_String;
			val$imageFile = r4_Uri;
			val$fittingMode = r5i;
			mBitmap = null;
		}

		private void cancelLoad() {
			synchronized(this$0.mLock) {
				if (this$0.mDecodeOptions != null) {
					this$0.mDecodeOptions.requestCancelDecode();
					this$0.mDecodeOptions = null;
				}
			}
		}

		public void onFinish() {
			super.onFinish();
			cancelLoad();
			loadBitmap.cancel(true);
		}

		public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
			boolean changed = true;
			if (cancellationSignal.isCanceled()) {
				layoutResultCallback.onLayoutCancelled();
				mAttributes = newPrintAttributes;
			} else if (mBitmap != null) {
				PrintDocumentInfo info = new Builder(val$jobName).setContentType(SCALE_MODE_FIT).setPageCount(SCALE_MODE_FIT).build();
				if (!newPrintAttributes.equals(oldPrintAttributes)) {
					layoutResultCallback.onLayoutFinished(info, changed);
				} else {
					changed = false;
					layoutResultCallback.onLayoutFinished(info, changed);
				}
			} else {
				loadBitmap = new AnonymousClass_1(this, cancellationSignal, newPrintAttributes, oldPrintAttributes, layoutResultCallback);
				loadBitmap.execute(new Uri[0]);
				mAttributes = newPrintAttributes;
			}
		}

		public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
			Throwable r5_Throwable;
			PrintedPdfDocument pdfDocument = new PrintedPdfDocument(this$0.mContext, mAttributes);
			int r5i = SCALE_MODE_FIT;
			try {
				Page page = pdfDocument.startPage(r5i);
				page.getCanvas().drawBitmap(mBitmap, this$0.getMatrix(mBitmap.getWidth(), mBitmap.getHeight(), new RectF(page.getInfo().getContentRect()), val$fittingMode), null);
				pdfDocument.finishPage(page);
				pdfDocument.writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
				PageRange[] r5_PageRange_A = new PageRange[1];
				r5_PageRange_A[0] = PageRange.ALL_PAGES;
				writeResultCallback.onWriteFinished(r5_PageRange_A);
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (fileDescriptor != null) {
					try {
						fileDescriptor.close();
					} catch (IOException e) {
						return;
					}
				}
			} catch (Throwable th) {
				r5_Throwable = th;
				if (pdfDocument != null) {
					pdfDocument.close();
				}
				if (fileDescriptor != null) {
					fileDescriptor.close();
					return r5_Throwable;
				} else {
					return r5_Throwable;
				}
			}
		}
	}


	PrintHelperKitkat(Context context) {
		super();
		mDecodeOptions = null;
		mLock = new Object();
		mScaleMode = 2;
		mColorMode = 2;
		mOrientation = 1;
		mContext = context;
	}

	private Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
		Matrix matrix = new Matrix();
		float scale = content.width() / ((float) imageWidth);
		if (fittingMode == SCALE_MODE_FILL) {
			scale = Math.max(scale, content.height() / ((float) imageHeight));
		} else {
			scale = Math.min(scale, content.height() / ((float) imageHeight));
		}
		matrix.postScale(scale, scale);
		matrix.postTranslate((content.width() - (((float) imageWidth) * scale)) / 2.0f, (content.height() - (((float) imageHeight) * scale)) / 2.0f);
		return matrix;
	}

	private Bitmap loadBitmap(Uri uri, Options o) throws FileNotFoundException {
		Throwable r2_Throwable;
		if (uri == null || mContext == null) {
			throw new IllegalArgumentException("bad argument to loadBitmap");
		} else {
			InputStream is;
			Bitmap r2_Bitmap;
			try {
				is = mContext.getContentResolver().openInputStream(uri);
				r2_Bitmap = BitmapFactory.decodeStream(is, null, o);
				if (is != null) {
					try {
						is.close();
						return r2_Bitmap;
					} catch (IOException e) {
						Log.w(LOG_TAG, "close fail ", e);
						return r2_Bitmap;
					}
				} else {
					return r2_Bitmap;
				}
			} catch (Throwable th) {
				r2_Throwable = th;
				if (false) {
					null.close();
					return r2_Throwable;
				} else {
					return r2_Throwable;
				}
			}
		}
	}

	private Bitmap loadConstrainedBitmap(Uri uri, int maxSideLength) throws FileNotFoundException {
		if (maxSideLength <= 0 || uri == null || mContext == null) {
			throw new IllegalArgumentException("bad argument to getScaledBitmap");
		} else {
			Options opt = new Options();
			opt.inJustDecodeBounds = true;
			loadBitmap(uri, opt);
			int w = opt.outWidth;
			int h = opt.outHeight;
			if (w > 0) {
				if (h <= 0) {
					return null;
				} else {
					int imageSide = Math.max(w, h);
					int sampleSize = SCALE_MODE_FIT;
					while (imageSide > maxSideLength) {
						imageSide >>>= 1;
						sampleSize <<= 1;
					}
					if (sampleSize > 0) {
						if (Math.min(w, h) / sampleSize > 0) {
							Options decodeOptions;
							synchronized(mLock) {
								try {
									mDecodeOptions = new Options();
									mDecodeOptions.inMutable = true;
									mDecodeOptions.inSampleSize = sampleSize;
									decodeOptions = mDecodeOptions;
								} catch (Throwable th) {
									while (true) {
										return th;
									}
								}
							}
							try {
								Bitmap r6_Bitmap = loadBitmap(uri, decodeOptions);
								synchronized(mLock) {
									try {
										mDecodeOptions = null;
									} catch (Throwable th_2) {
										return th_2;
									}
								}
								return r6_Bitmap;
							} catch (Throwable th_3) {
								synchronized(mLock) {
								}
								mDecodeOptions = null;
								return th_3;
							}
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			} else {
				return null;
			}
		}
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
		if (bitmap == null) {
		} else {
			int fittingMode = mScaleMode;
			PrintManager printManager = (PrintManager) mContext.getSystemService("print");
			MediaSize mediaSize = MediaSize.UNKNOWN_PORTRAIT;
			if (bitmap.getWidth() > bitmap.getHeight()) {
				mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
			}
			printManager.print(jobName, new AnonymousClass_1(this, jobName, bitmap, fittingMode), new Builder().setMediaSize(mediaSize).setColorMode(mColorMode).build());
		}
	}

	public void printBitmap(String jobName, Uri imageFile) throws FileNotFoundException {
		AnonymousClass_2 printDocumentAdapter = new AnonymousClass_2(this, jobName, imageFile, mScaleMode);
		PrintManager printManager = (PrintManager) mContext.getSystemService("print");
		Builder builder = new Builder();
		builder.setColorMode(mColorMode);
		if (mOrientation == SCALE_MODE_FIT) {
			builder.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
		} else if (mOrientation == 2) {
			builder.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
		}
		printManager.print(jobName, printDocumentAdapter, builder.build());
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
