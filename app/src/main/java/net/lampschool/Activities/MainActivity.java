package net.lampschool.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import net.lampschool.Adapters.ViewPagerAdapter;
import net.lampschool.Dialogs.AccountManagerDialog;
import net.lampschool.R;
import net.lampschool.SlidingTabs.SlidingTabLayout;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Utils;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private String[] titoli;
    private int numTabs;
    private String dati = "";
    private int lastTab = 0;
    private Resources risorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_name);
        setSupportActionBar(toolbar);

        risorse = getResources();
        dati = "";
        showDialog();

        numTabs = risorse.getInteger(R.integer.numTabs);
        titoli = risorse.getStringArray(R.array.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(numTabs);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);
        tabs.setOnPageChangeListener(this);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return risorse.getColor(R.color.tabsScrollColor);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notifiche = preferences.getBoolean("notifiche", true);

        if (notifiche) {
            Utils.stopAlarmManager(this);
            Utils.startAlarmManager(this);
        }
        else Utils.stopAlarmManager(this);
    }

    private void showDialog() {
        Utils.stopAlarmManager(this);
        AccountManagerDialog dialog = new AccountManagerDialog();
        dialog.show(getFragmentManager(), "account_manager");
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setDati(Intent intent) {
        Gson gson = new Gson();
        String jsonAccount = intent.getStringExtra("account");
        Account account = gson.fromJson(jsonAccount, Account.class);
        dati = intent.getStringExtra("dati");
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titoli, numTabs, dati, account);
        pager.setAdapter(adapter);
        pager.setCurrentItem(lastTab);
        tabs.setViewPager(pager);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        lastTab = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
