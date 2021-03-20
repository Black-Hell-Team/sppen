package hack.hackit.pankaj.keyboardlisten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;

public class SearchableActivity extends ActionBarActivity {
    protected static ArrayList<KeyEventData> filterData = new ArrayList();
    private static ResultMobileArrayAdapter myAdapter;
    private String table_name = "All";

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        handleIntent(getIntent(), this.table_name);
    }

    private void handleIntent(Intent intent, String table_name) {
        if ("android.intent.action.SEARCH".equals(intent.getAction())) {
            String query = intent.getStringExtra("query");
            Log.w("Query", query);
            showListView(query, table_name);
        }
    }

    /* Access modifiers changed, original: protected */
    public void showListView(String filter_Key, String table_name) {
        Driver.mdh = new MyDatabaseHelper(this);
        filterData = Driver.mdh.filterData(filter_Key, table_name);
        System.out.println("Size read " + filterData.size());
        if (filterData.size() != 0) {
            myAdapter = new ResultMobileArrayAdapter(this, filterData);
            ListView lv = (ListView) findViewById(R.id.result_listView);
            lv.setAdapter(myAdapter);
            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String typedText = ((KeyEventData) SearchableActivity.filterData.get(position)).get_TypedText();
                    SearchableActivity.this.showDialogScreen(((KeyEventData) SearchableActivity.filterData.get(position)).get_ApplicationName(), typedText, ((KeyEventData) SearchableActivity.filterData.get(position)).get_AppDateTime());
                }
            });
            if (myAdapter != null) {
                System.out.println("listview Created");
            }
        }
        Driver.mdh = null;
    }

    /* Access modifiers changed, original: protected */
    public void showDialogScreen(String appName, String typedText, String dateTime) {
        Intent i = new Intent(this, ShowTextDialog.class);
        i.putExtra("appName", appName);
        i.putExtra("typedText", typedText);
        i.putExtra("dateTime", dateTime);
        startActivity(i);
    }
}
