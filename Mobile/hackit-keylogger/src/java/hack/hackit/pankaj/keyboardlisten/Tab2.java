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

public class Tab2 extends Fragment {
    static ArrayList<String> NAMES = new ArrayList();
    public static ArrayList<KeyEventData> allEvenData = new ArrayList();
    private static MobileArrayAdapterTab2 myAdapter;
    private static View v;
    int index;
    private ListView lv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.index = 1;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab2_layout, container, false);
        showListView();
        return v;
    }

    public void showListView() {
        Driver.mdh = new MyDatabaseHelper(MainActivity.context);
        allEvenData = Driver.mdh.readAllEventData("All");
        System.out.println("Size read " + allEvenData.size());
        if (allEvenData.size() != 0) {
            myAdapter = new MobileArrayAdapterTab2(MainActivity.context, allEvenData, "Tab2");
            this.lv = (ListView) v.findViewById(R.id.listView);
            this.lv.setChoiceMode(3);
            this.lv.setAdapter(myAdapter);
            this.lv.setMultiChoiceModeListener(new MultiChoiceModeListener() {
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    mode.setTitle(Tab2.this.lv.getCheckedItemCount() + " Selected");
                    Tab2.myAdapter.toggleSelection(position);
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    SparseBooleanArray selected = Tab2.myAdapter.getSelectedIds();
                    switch (item.getItemId()) {
                        case R.id.move /*2131361903*/:
                            Tab2.this.AskOption(selected, mode, "Save", "Records will be saved in Saved Data");
                            return true;
                        case R.id.delete /*2131361904*/:
                            Tab2.this.AskOption(selected, mode, "Delete", "Selected Records will be deleted");
                            return true;
                        default:
                            return false;
                    }
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.listselection_menu, menu);
                    return true;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    Tab2.myAdapter.removeSelection("All");
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }
            });
            this.lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String typedText = ((KeyEventData) Tab2.allEvenData.get(position)).get_TypedText();
                    Tab2.this.showDialogScreen(((KeyEventData) Tab2.allEvenData.get(position)).get_ApplicationName(), typedText, ((KeyEventData) Tab2.allEvenData.get(position)).get_AppDateTime());
                }
            });
            if (myAdapter != null) {
                System.out.println("listview Created");
            }
        }
        Driver.mdh = null;
    }

    public static void updateList(String val) {
        NAMES.add(val);
        if (myAdapter == null) {
            System.out.println("null");
        } else {
            myAdapter.notifyDataSetChanged();
        }
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
                myAdapter.remove(myAdapter.getItemAtPosTab2(selected.keyAt(i)), "All");
            }
        }
        mode.finish();
        Toast.makeText(HKApplication.getAppContext(), "Records deleted Successfully.", 0).show();
    }

    public void onMoveCommand(SparseBooleanArray selected, ActionMode mode) {
        for (int i = selected.size() - 1; i >= 0; i--) {
            if (selected.valueAt(i)) {
                myAdapter.moveToSave(myAdapter.getItemAtPosTab2(selected.keyAt(i)));
            }
        }
        mode.finish();
        Toast.makeText(HKApplication.getAppContext(), "Records Saved Successfully", 0).show();
    }

    private void AskOption(final SparseBooleanArray selected, final ActionMode mode, String title, String desc) {
        Builder myQuittingDialogBox = new Builder(MainActivity.context, R.style.MyAlertDialogStyle);
        myQuittingDialogBox.setTitle(title);
        myQuittingDialogBox.setMessage(desc);
        if (title.equals("Delete")) {
            myQuittingDialogBox.setPositiveButton("Delete", new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    Tab2.this.onDeleteCommand(selected, mode);
                }
            });
        } else {
            myQuittingDialogBox.setPositiveButton("Save", new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    Tab2.this.onMoveCommand(selected, mode);
                }
            });
        }
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
