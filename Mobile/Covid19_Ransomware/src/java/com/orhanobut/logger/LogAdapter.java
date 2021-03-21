package com.orhanobut.logger;

public interface LogAdapter {
    boolean isLoggable(int i, String str);

    void log(int i, String str, String str2);
}
