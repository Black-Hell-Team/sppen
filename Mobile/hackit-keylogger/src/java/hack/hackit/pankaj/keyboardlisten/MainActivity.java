package hack.hackit.pankaj.keyboardlisten;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;

public class MainActivity extends ActionBarActivity implements TabListener {
    protected static Context context;
    private ActionBar actionBar;
    private List<String> items;
    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.actionBar = getSupportActionBar();
        this.mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        this.viewPager.setAdapter(this.mAdapter);
        this.actionBar.setHomeButtonEnabled(false);
        this.actionBar.setNavigationMode(2);
        this.actionBar.addTab(this.actionBar.newTab().setText("Saved Data").setTabListener(this));
        this.actionBar.addTab(this.actionBar.newTab().setText("Typed Data").setTabListener(this));
        this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int position) {
                MainActivity.this.actionBar.setSelectedNavigationItem(position);
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void showMessage() {
    }

    public void openFilter() {
    }

    public void refreshList() {
        finish();
        startActivity(getIntent());
    }

    private void openSetting() {
        startActivity(new Intent(this, Settings.class));
    }

    private void openHelp() {
        startActivity(new Intent(this, Help.class));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ((SearchView) menu.findItem(R.id.action_search1).getActionView()).setSearchableInfo(((SearchManager) getSystemService("search")).getSearchableInfo(getComponentName()));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh /*2131361906*/:
                refreshList();
                return true;
            case R.id.action_setting /*2131361907*/:
                openSetting();
                return true;
            case R.id.action_help /*2131361908*/:
                openHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onBackPressed() {
        AskOption().show();
    }

    private AlertDialog AskOption() {
        return new Builder(this, R.style.MyAlertDialogStyle).setTitle("Exit").setMessage("Do you want to Exit").setPositiveButton("Yes", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                MainActivity.this.finish();
                System.exit(0);
            }
        }).setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
    }
}
