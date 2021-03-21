package com.orhanobut.logger;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogStrategy implements LogStrategy {
    private final Handler handler;

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
            fileWriter.append(str);
        }

        private File getLogFile(String str, String str2) {
            Utils.checkNotNull(str);
            Utils.checkNotNull(str2);
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = null;
            String str3 = "%s_%s.csv";
            File file3 = new File(file, String.format(str3, new Object[]{str2, Integer.valueOf(0)}));
            int i = 0;
            while (true) {
                File file4 = file3;
                file3 = file2;
                file2 = file4;
                if (!file2.exists()) {
                    break;
                }
                i++;
                file3 = new File(file, String.format(str3, new Object[]{str2, Integer.valueOf(i)}));
            }
            return (file3 == null || file3.length() >= ((long) this.maxFileSize)) ? file2 : file3;
        }
    }

    public DiskLogStrategy(Handler handler) {
        this.handler = (Handler) Utils.checkNotNull(handler);
    }

    public void log(int i, String str, String str2) {
        Utils.checkNotNull(str2);
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(i, str2));
    }
}
