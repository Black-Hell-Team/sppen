package android.support.v4.util;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
	private final File mBackupName;
	private final File mBaseName;

	public AtomicFile(File baseName) {
		super();
		mBaseName = baseName;
		mBackupName = new File(baseName.getPath() + ".bak");
	}

	static boolean sync(FileOutputStream stream) {
		if (stream != null) {
			try {
				stream.getFD().sync();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public void delete() {
		mBaseName.delete();
		mBackupName.delete();
	}

	public void failWrite(FileOutputStream str) {
		if (str != null) {
			sync(str);
			try {
				str.close();
				mBaseName.delete();
				mBackupName.renameTo(mBaseName);
			} catch (IOException e) {
				Log.w("AtomicFile", "failWrite: Got exception:", e);
				return;
			}
		}
	}

	public void finishWrite(FileOutputStream str) {
		if (str != null) {
			sync(str);
			try {
				str.close();
				mBackupName.delete();
			} catch (IOException e) {
				Log.w("AtomicFile", "finishWrite: Got exception:", e);
				return;
			}
		}
	}

	public File getBaseFile() {
		return mBaseName;
	}

	public FileInputStream openRead() throws FileNotFoundException {
		if (mBackupName.exists()) {
			mBaseName.delete();
			mBackupName.renameTo(mBaseName);
		}
		return new FileInputStream(mBaseName);
	}

	public byte[] readFully() throws IOException {
		FileInputStream stream = openRead();
		int pos = 0;
		Object data = new Object[stream.available()];
		while (true) {
			int amt = stream.read(data, pos, data.length - pos);
			if (amt <= 0) {
				stream.close();
				return data;
			} else {
				pos += amt;
				int avail = stream.available();
				if (avail > data.length - pos) {
					Object newData = new Object[(pos + avail)];
					System.arraycopy(data, 0, newData, 0, pos);
					data = newData;
				}
			}
		}
	}

	public FileOutputStream startWrite() throws IOException {
		if (mBaseName.exists()) {
			if (!mBackupName.exists()) {
				if (!mBaseName.renameTo(mBackupName)) {
					Log.w("AtomicFile", "Couldn't rename file " + mBaseName + " to backup file " + mBackupName);
				}
			} else {
				mBaseName.delete();
			}
		}
		try {
			return new FileOutputStream(mBaseName);
		} catch (FileNotFoundException e) {
			if (!mBaseName.getParentFile().mkdir()) {
				throw new IOException("Couldn't create directory " + mBaseName);
			} else {
				return new FileOutputStream(mBaseName);
			}
		}
	}
}
