package hack.hackit.pankaj.keyboardlisten;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Tab1 extends Fragment {
    public static ArrayList<KeyEventData> saved_AllEvenData = new ArrayList();
    static ArrayList<String> saved_NAMES = new ArrayList();
    private static MobileArrayAdapterTab1 saved_myAdapter;
    private static View saved_v;
    int index;
    private ListView saved_lv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.index = 0;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        saved_v = inflater.inflate(R.layout.tab1_layout, container, false);
        showListView();
        return saved_v;
    }

    public void showListView() {
        Driver.mdh = new MyDatabaseHelper(MainActivity.context);
        saved_AllEvenData = Driver.mdh.readAllEventData("Saved");
        System.out.println("Size read " + saved_AllEvenData.size());
        if (saved_AllEvenData.size() != 0) {
            saved_myAdapter = new MobileArrayAdapterTab1(MainActivity.context, saved_AllEvenData, "Tab1");
            this.saved_lv = (ListView) saved_v.findViewById(R.id.tab1listView);
            this.saved_lv.setChoiceMode(3);
            this.saved_lv.setAdapter(saved_myAdapter);
            this.saved_lv.setMultiChoiceModeListener(new MultiChoiceModeListener() {
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    mode.setTitle(Tab1.this.saved_lv.getCheckedItemCount() + " Selected");
                    Tab1.saved_myAdapter.toggleSelection(position);
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    SparseBooleanArray selected = Tab1.saved_myAdapter.getSelectedIds();
                    switch (item.getItemId()) {
                        case R.id.delete /*2131361904*/:
                            Tab1.this.AskOption(selected, mode, "Delete", "Selected Records will be deleted");
                            return true;
                        default:
                            return false;
                    }
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.saved_listselection_menu, menu);
                    return true;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    Tab1.saved_myAdapter.removeSelection("Saved");
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
            this.saved_lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String typedText = ((KeyEventData) Tab1.saved_AllEvenData.get(position)).get_TypedText();
                    Tab1.this.showDialogScreen(((KeyEventData) Tab1.saved_AllEvenData.get(position)).get_ApplicationName(), typedText, ((KeyEventData) Tab1.saved_AllEvenData.get(position)).get_AppDateTime());
                }
            });
            if (saved_myAdapter != null) {
                System.out.println("listview Created");
            }
        }
        Driver.mdh = null;
    }

    public void showDialogScreen(String appName, String typedText, String dateTime) {
        Intent i = new Intent(HKApplication.getAppContext(), ShowTextDialog.class);
        i.putExtra("appName", appName);
        i.putExtra("typedText", typedText);
        i.putExtra("dateTime", dateTime);
        startActivity(i);
    }

    public void onDeleteCommand(SparseBooleanArray selected, ActionMode mode) {
        for (int i = selected.size() - 1; i >= 0; i--) {
            if (selected.valueAt(i)) {
                saved_myAdapter.remove(saved_myAdapter.getItemAtPosTab1(selected.keyAt(i)), "Saved");
            }
        }
        mode.finish();
        Toast.makeText(HKApplication.getAppContext(), "Records deleted Successfully.", 0).show();
    }

    private void AskOption(final SparseBooleanArray selected, final ActionMode mode, String title, String desc) {
        Builder myQuittingDialogBox = new Builder(MainActivity.context, R.style.MyAlertDialogStyle);
        myQuittingDialogBox.setTitle(title);
        myQuittingDialogBox.setMessage(desc);
        myQuittingDialogBox.setPositiveButton("Delete", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                Tab1.this.onDeleteCommand(selected, mode);
            }
        });
        myQuittingDialogBox.setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = myQuittingDialogBox.create();
        dialog.show();
        Button neutral_button = dialog.getButton(-3);
        Button positive_button = dialog.getButton(-1);
        if (neutral_button != null) {
            neutral_button.setTextColor(MainActivity.context.getResources().getColor(R.color.green));
        }
        if (positive_button != null) {
            positive_button.setTextColor(MainActivity.context.getResources().getColor(R.color.green));
        }
    }
}
