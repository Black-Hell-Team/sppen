package hack.hackit.pankaj.keyboardlisten;

public class AppPackageInfo {
    private String app_name;
    private String app_package_name;

    AppPackageInfo(String app_name, String app_package_name) {
        this.app_name = app_name;
        this.app_package_name = app_package_name;
    }

    public String getApp_name() {
        return this.app_name;
    }

    public String getApp_package_name() {
        return this.app_package_name;
    }
}
