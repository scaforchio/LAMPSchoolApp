package net.lampschool.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import net.lampschool.Adapters.AdapterUtenti;
import net.lampschool.Decorations.RecyclerViewDividerItemDecoration;
import net.lampschool.Dialogs.AddUserDialog;
import net.lampschool.Listeners.RecyclerItemClickListener;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Utils;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recycler;
    private SearchView searchView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterUtenti adapter;
    private ArrayList<Account> accounts;
    private Resources risorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        risorse = getResources();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(risorse.getString(R.string.title_activity_utenti));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recycler = (RecyclerView) findViewById(R.id.recycler_account);

        recycler.setHasFixedSize(true);

        accounts = Utils.readListaAccount(this);
        adapter = new AdapterUtenti(this, accounts);
        recycler.setAdapter(adapter);
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        recycler.addItemDecoration(new RecyclerViewDividerItemDecoration(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_user).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Gson gson = new Gson();
        Account account = accounts.get(position);
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("account", gson.toJson(account));
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onClick(View v) {
        AddUserDialog dialog = new AddUserDialog();
        dialog.show(getFragmentManager(), "add_dialog_account");
    }

    public void refresh() {
        accounts = Utils.readListaAccount(this);
        adapter.setAccounts(accounts);
        fab.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.findUtenti(newText);
        return true;
    }
}
