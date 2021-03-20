package hack.hackit.pankaj.keyboardlisten;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KeyEventData {
    private String AppDateTime;
    private String AppPackageName;
    private String ApplicationName;
    private long SrNo;
    private String TypedText;

    KeyEventData() {
    }

    KeyEventData(String ApplicationName, String TypedText) {
        this.ApplicationName = ApplicationName;
        this.TypedText = TypedText;
    }

    KeyEventData(long srNo, String ApplicationName, String AppDateTime, String TypedText) {
        this.ApplicationName = ApplicationName;
        this.TypedText = TypedText;
        this.SrNo = srNo;
        this.AppDateTime = AppDateTime;
    }

    KeyEventData(String ApplicationName, String AppDateTime, String TypedText) {
        this.ApplicationName = ApplicationName;
        this.TypedText = TypedText;
        this.AppDateTime = AppDateTime;
    }

    KeyEventData(String ApplicationName, String AppDateTime, String TypedText, String AppPackageName) {
        this.ApplicationName = ApplicationName;
        this.TypedText = TypedText;
        this.AppDateTime = AppDateTime;
        this.AppPackageName = AppPackageName;
    }

    public void set_ApplicationName(String ApplicationName) {
        this.ApplicationName = ApplicationName;
    }

    public void set_AppPackageName(String appPackageName) {
        this.AppPackageName = appPackageName;
    }

    public void set_TypedText(String TypedText) {
        this.TypedText = TypedText;
    }

    public void set_SrNo(long SrNo) {
        this.SrNo = SrNo;
    }

    public void set_AppDateTime(String AppDateTime) {
        this.AppDateTime = AppDateTime;
    }

    public long get_SrNo() {
        return this.SrNo;
    }

    public String get_ApplicationName() {
        return this.ApplicationName;
    }

    public String get_TypedText() {
        return this.TypedText;
    }

    public String get_AppDateTime() {
        return this.AppDateTime;
    }

    public String getDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public String getAppPackageName() {
        return this.AppPackageName;
    }
}
