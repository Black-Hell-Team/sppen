package com.device.security.util;

import com.device.security.model.InstalledApp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstalledAppManager {
    private static volatile InstalledAppManager instance;
    private List<InstalledApp> installedAppList = Collections.synchronizedList(new ArrayList());

    private InstalledAppManager() {
    }

    public static InstalledAppManager getInstance() {
        if (instance == null) {
            synchronized (InstalledAppManager.class) {
                if (instance == null) {
                    instance = new InstalledAppManager();
                }
            }
        }
        return instance;
    }

    public synchronized void addInstalledAppsList(List<InstalledApp> list) {
        this.installedAppList.clear();
        this.installedAppList.addAll(list);
    }

    public synchronized List<InstalledApp> getInstalledAppList() {
        return this.installedAppList;
    }
}
