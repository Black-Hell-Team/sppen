package com.orhanobut.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogStrategy implements LogStrategy {
    private final Handler handler;

    public DiskLogStrategy(Handler handler2) {
        this.handler = (Handler) Utils.checkNotNull(handler2);
    }

    @Override // com.orhanobut.logger.LogStrategy
    public void log(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        Handler handler2 = this.handler;
        handler2.sendMessage(handler2.obtainMessage(i, str2));
    }

    static class WriteHandler extends Handler {
        private final String folder;
        private final int maxFileSize;

        WriteHandler(Looper looper, String str, int i) {
            super((Looper) Utils.checkNotNull(looper));
            this.folder = (String) Utils.checkNotNull(str);
            this.maxFileSize = i;
        }

        public void handleMessage(Message message) {
            String str = (String) message.obj;
            FileWriter fileWriter = null;
            try {
                FileWriter fileWriter2 = new FileWriter(getLogFile(this.folder, "logs"), true);
                try {
                    writeLog(fileWriter2, str);
                    fileWriter2.flush();
                    fileWriter2.close();
                } catch (IOException unused) {
                    fileWriter = fileWriter2;
                }
            } catch (IOException unused2) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException unused3) {
                    }
                }
            }
        }

        private void writeLog(FileWriter fileWriter, String str) throws IOException {
            Utils.checkNotNull(fileWriter);
            Utils.checkNotNull(str);
            fileWriter.append((CharSequence) str);
        }

        private File getLogFile(String str, String str2) {
            Utils.checkNotNull(str);
            Utils.checkNotNull(str2);
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = null;
            File file3 = new File(file, String.format("%s_%s.csv", str2, 0));
            int i = 0;
            while (true) {
                file2 = file3;
                if (!file2.exists()) {
                    break;
                }
                i++;
                file3 = new File(file, String.format("%s_%s.csv", str2, Integer.valueOf(i)));
            }
            return (file2 == null || file2.length() >= ((long) this.maxFileSize)) ? file2 : file2;
        }
    }
}
