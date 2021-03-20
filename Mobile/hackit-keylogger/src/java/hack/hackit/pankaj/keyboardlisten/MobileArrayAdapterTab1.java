package hack.hackit.pankaj.keyboardlisten;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MobileArrayAdapterTab1 extends ArrayAdapter<String> {
    private final Context context;
    String currentTab = "Tab1";
    private SparseBooleanArray mSelectedItemsIds;

    public MobileArrayAdapterTab1(Context context, ArrayList<KeyEventData> temp, String currentTab) {
        super(context, R.layout.activity_main, getApplicationNamesList(temp));
        this.context = context;
        this.mSelectedItemsIds = new SparseBooleanArray();
        this.currentTab = currentTab;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.single_row, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.Textview1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.Textview2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconimage);
        String appName = ((KeyEventData) Tab1.saved_AllEvenData.get(position)).get_ApplicationName();
        String appDT = ((KeyEventData) Tab1.saved_AllEvenData.get(position)).get_AppDateTime();
        Drawable icon = null;
        try {
            icon = this.context.getPackageManager().getApplicationIcon(((KeyEventData) Tab1.saved_AllEvenData.get(position)).getAppPackageName());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (icon == null) {
            icon = this.context.getResources().getDrawable(R.drawable.ic_launcher);
        }
        textView1.setText(appName);
        textView2.setText(appDT);
        imageView.setImageDrawable(icon);
        return rowView;
    }

    public static ArrayList<String> getApplicationNamesList(ArrayList<KeyEventData> temp) {
        ArrayList<String> appNames = new ArrayList();
        for (int i = 0; i < temp.size(); i++) {
            appNames.add(((KeyEventData) temp.get(i)).get_ApplicationName());
        }
        return appNames;
    }

    public KeyEventData getItemAtPosTab1(int position) {
        KeyEventData kvd = new KeyEventData();
        return (KeyEventData) Tab1.saved_AllEvenData.get(position);
    }

    public void toggleSelection(int position) {
        selectView(position, !this.mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value) {
            this.mSelectedItemsIds.put(position, value);
        } else {
            this.mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public void remove(KeyEventData object, String table_name) {
        MyDatabaseHelper dh = new MyDatabaseHelper(MainActivity.context);
        dh.deleteRecord(object, table_name);
        dh.close();
        Tab1.saved_AllEvenData.remove(object);
        notifyDataSetChanged();
    }

    public void removeSelection(String table_name) {
        Log.w("From RemoveSelection", "yes");
        this.mSelectedItemsIds = new SparseBooleanArray();
        clear();
        addAll(getApplicationNamesList(Tab1.saved_AllEvenData));
        notifyDataSetChanged();
    }

    public void clearSelectionData() {
        this.mSelectedItemsIds.clear();
        notifyDataSetChanged();
    }

    public static void addObject(KeyEventData object) {
        Tab1.saved_AllEvenData.add(object);
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return this.mSelectedItemsIds;
    }
}
