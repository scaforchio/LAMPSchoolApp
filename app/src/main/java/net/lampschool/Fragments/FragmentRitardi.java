package net.lampschool.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.lampschool.Activities.SettingsActivity;
import net.lampschool.Adapters.AdapterRitardi;
import net.lampschool.Decorations.RecyclerViewDividerItemDecoration;
import net.lampschool.Dialogs.AccountManagerDialog;
import net.lampschool.Dialogs.InfoDialog;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Ritardo;
import net.lampschool.Utils.Update;
import net.lampschool.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentRitardi extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Ritardo> ritardi;
    private RecyclerView recycler;
    private AdapterRitardi adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresh;
    private TextView tot_rit;
    private Resources risorse;
    private Gson gson;
    private Account account;
    private String dati;
    private boolean refresh_flag = false;

    public FragmentRitardi newInstance(String dati, Account account) {
        FragmentRitardi fragment = new FragmentRitardi();
        gson = new Gson();
        Bundle b = new Bundle();
        b.putString("dati", dati);
        b.putString("account", gson.toJson(account));
        fragment.setArguments(b);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Bundle b = getArguments();
        account = gson.fromJson(b.getString("account"), Account.class);
        dati = b.getString("dati");
        ritardi = Utils.getRitardiFromJson(dati);

        risorse = getResources();
        View v = inflater.inflate(R.layout.tab_ritardi, container, false);

        recycler = (RecyclerView) v.findViewById(R.id.recycler_ritardi);
        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        adapter = new AdapterRitardi(getContext(), ritardi);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new RecyclerViewDividerItemDecoration(getContext()));

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.layout_refresh2);
        refresh.setColorSchemeResources(R.color.rosso, R.color.verde, R.color.blu);

        if (preferences.getBoolean("refresh", true)) {
            refresh.setEnabled(true);
            refresh.setOnRefreshListener(this);
        } else {
            refresh.setEnabled(false);
        }

        tot_rit = (TextView) v.findViewById(R.id.tot_rit);
        tot_rit.setText(risorse.getString(R.string.num_ritardi) + " " + adapter.getItemCount());
        if (adapter.getItemCount() > 0) saveNumRitardi();

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                InfoDialog info = new InfoDialog();
                Bundle bundle = new Bundle();
                bundle.putString("dati", dati);
                info.setArguments(bundle);
                info.show(getActivity().getSupportFragmentManager(), "info");
                break;

            case R.id.settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivityForResult(intent, 1);
                break;

            case R.id.reload:
                if (!refresh_flag) {
                    refresh.setRefreshing(true);
                    refresh();
                }
                break;

            case R.id.logout:
                AccountManagerDialog manager = new AccountManagerDialog();
                manager.show(getActivity().getFragmentManager(), "account_manager_dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        if (!refresh_flag) {
            refresh();
        }
    }

    private void refresh() {
        refresh_flag = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean message = false;
                if (Utils.isConnectedToInternet()) {
                    String s = Utils.startLoginTask(getActivity(), account);
                    if (!isEmpty(s)) {
                        adapter.setRitardi(Utils.getRitardiFromJson(s));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tot_rit.setText(risorse.getString(R.string.num_ritardi) + " " + adapter.getItemCount());
                            }
                        });
                        saveNumRitardi();
                    } else message = true;
                } else message = true;

                final boolean finalMessage = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalMessage)
                            Toast.makeText(getContext(), risorse.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                        refresh_flag = false;
                    }
                });
            }
        });
        t.run();
    }

    public void saveNumRitardi() {
        Update.salvaRitardi(getActivity(), account, adapter.getItemCount());
    }

    private boolean isEmpty(String s) {
        if (s.equals("errore connessione") || s.equals("") || s.equals("tempo_esaurito")) {
            return true;
        } else {
            try {
                JSONObject object = new JSONObject(s);
                return object.getInt("alunno") == 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
