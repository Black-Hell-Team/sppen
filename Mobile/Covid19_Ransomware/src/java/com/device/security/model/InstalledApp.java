package com.device.security.model;

public class InstalledApp {
    private String installTime;
    private boolean isBrowserApp;
    private boolean isSystemApp;
    private String name;
    private String packageName;
    private String version;

    public static class InstalledAppBuilder {
        private String installTime;
        private boolean isBrowserApp;
        private boolean isSystemApp;
        private String name;
        private String packageName;
        private String version;

        public InstalledAppBuilder setPackageName(String str) {
            this.packageName = str;
            return this;
        }

        public InstalledAppBuilder setName(String str) {
            this.name = str;
            return this;
        }

        public InstalledAppBuilder setVersion(String str) {
            this.version = str;
            return this;
        }

        public InstalledAppBuilder setInstallTime(String str) {
            this.installTime = str;
            return this;
        }

        public InstalledAppBuilder setIsSystemApp(boolean z) {
            this.isSystemApp = z;
            return this;
        }

        public InstalledAppBuilder setIsBrowserApp(boolean z) {
            this.isBrowserApp = z;
            return this;
        }

        public InstalledApp create() {
            return new InstalledApp(this.packageName, this.name, this.version, this.installTime, this.isSystemApp, this.isBrowserApp);
        }
    }

    public InstalledApp(String str, String str2, String str3, String str4, boolean z, boolean z2) {
        this.packageName = str;
        this.name = str2;
        this.version = str3;
        this.installTime = str4;
        this.isSystemApp = z;
        this.isBrowserApp = z2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getInstallTime() {
        return this.installTime;
    }

    public boolean isSystemApp() {
        return this.isSystemApp;
    }

    public boolean isBrowserApp() {
        return this.isBrowserApp;
    }
}
