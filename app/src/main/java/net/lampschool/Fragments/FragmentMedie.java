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
import android.widget.Toast;

import com.google.gson.Gson;

import net.lampschool.Activities.SettingsActivity;
import net.lampschool.Adapters.AdapterMedie;
import net.lampschool.Decorations.RecyclerViewDividerItemDecoration;
import net.lampschool.Dialogs.AccountManagerDialog;
import net.lampschool.Dialogs.InfoDialog;
import net.lampschool.Dialogs.SortMedieDialog;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Media;
import net.lampschool.Utils.SezioneMedie;
import net.lampschool.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMedie extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<Media> medie;
    private Account account;
    private String dati;
    private RecyclerView recycler;
    private AdapterMedie adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresh;
    private Resources risorse;
    private Gson gson;

    public FragmentMedie newInstance(String dati, Account account) {
        gson = new Gson();
        FragmentMedie fragment = new FragmentMedie();
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
        dati = b.getString("dati");
        account = gson.fromJson(b.getString("account"), Account.class);
        medie = Utils.getMedieFromJson(getActivity(), dati);

        risorse = getResources();
        View v = inflater.inflate(R.layout.tab_medie, container, false);

        recycler = (RecyclerView) v.findViewById(R.id.recycler_medie);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        if (medie.size() > 0) {
            loadContent("materia_cresc");
        }

        recycler.addItemDecoration(new RecyclerViewDividerItemDecoration(getContext()));

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.layout_refresh7);
        refresh.setColorSchemeResources(R.color.rosso, R.color.verde, R.color.blu);

        if (preferences.getBoolean("refresh", true)) {
            refresh.setEnabled(true);
            refresh.setOnRefreshListener(this);
        } else {
            refresh.setEnabled(false);
        }

        setHasOptionsMenu(true);
        return v;
    }

    public void loadContent(String ordine) {
        ArrayList<SezioneMedie> sezioni;

        medie = Utils.ordinaListaMedie(medie,ordine);
        sezioni = Utils.sezionaMedie(getActivity(), medie);

        adapter = new AdapterMedie(getContext(),LayoutInflater.from(getActivity()),sezioni);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu4, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reload:
                refresh.setRefreshing(true);
                refresh();
                break;

            case R.id.sort:
                SortMedieDialog sort = new SortMedieDialog();
                sort.setTargetFragment(this, 2);
                sort.show(getActivity().getSupportFragmentManager(), "sort");
                break;

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

            case R.id.logout:
                AccountManagerDialog manager = new AccountManagerDialog();
                manager.show(getActivity().getFragmentManager(), "account_manager_dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            String ordine = data.getStringExtra("ordine");
            loadContent(ordine);
        }
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

    @Override
    public void onRefresh() {
        refresh();
    }

    public void refresh() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean message = false;

                if (Utils.isConnectedToInternet()) {
                    String s = Utils.startLoginTask(getContext(), account);
                    if (!isEmpty(s)) {
                        medie = Utils.getMedieFromJson(getActivity(), s);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadContent("materia_cresc");
                            }
                        });
                    } else message = true;
                } else
                    message = true;

                final boolean finalMessage = message;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalMessage) {
                            Toast.makeText(getContext(), risorse.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                        refresh.setRefreshing(false);
                    }
                });
            }
        });
        t.start();
    }
}
