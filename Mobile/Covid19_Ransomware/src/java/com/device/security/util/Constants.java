package com.device.security.util;

import com.device.security.model.InstalledApp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String DEFAULT_BROWSER_PACKAGE = "com.google.android.googlequicksearchbox";
    public static List<InstalledApp> installedAppList = new ArrayList();
    public static List<String> whiteListApps = Arrays.asList(new String[]{"com.android.chrome", "com.google.android.inputmethod.latin"});
}
