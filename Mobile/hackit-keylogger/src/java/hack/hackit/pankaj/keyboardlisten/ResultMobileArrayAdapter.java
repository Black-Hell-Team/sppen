package hack.hackit.pankaj.keyboardlisten;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ResultMobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;

    public ResultMobileArrayAdapter(Context context, ArrayList<KeyEventData> temp) {
        super(context, R.layout.activity_result, getApplicationNamesList(temp));
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.single_row, parent, false);
        TextView textView1 = (TextView) rowView.findViewById(R.id.Textview1);
        TextView textView2 = (TextView) rowView.findViewById(R.id.Textview2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconimage);
        String appName = ((KeyEventData) SearchableActivity.filterData.get(position)).get_ApplicationName();
        String appDT = ((KeyEventData) SearchableActivity.filterData.get(position)).get_AppDateTime();
        Drawable icon = null;
        try {
            icon = this.context.getPackageManager().getApplicationIcon(((KeyEventData) SearchableActivity.filterData.get(position)).getAppPackageName());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
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
}
