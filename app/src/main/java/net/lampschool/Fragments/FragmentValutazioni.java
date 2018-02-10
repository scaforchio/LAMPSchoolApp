package net.lampschool.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import net.lampschool.Adapters.AdapterVoti;
import net.lampschool.Decorations.RecyclerViewDividerItemDecoration;
import net.lampschool.Dialogs.AccountManagerDialog;
import net.lampschool.Dialogs.InfoDialog;
import net.lampschool.Dialogs.SearchVotiDialog;
import net.lampschool.Dialogs.SortVotiDialog;
import net.lampschool.Listeners.ScrollListener;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.SezioneVoti;
import net.lampschool.Utils.Update;
import net.lampschool.Utils.Utils;
import net.lampschool.Utils.Voto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentValutazioni extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private ArrayList<Voto> voti;
    private Account account;
    private String dati;
    private String[] materie;
    private RecyclerView recycler;
    private AdapterVoti adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton fab;
    private TextView num_voti;
    private Resources risorse;
    private Gson gson;
    private boolean refresh_flag = false;

    public FragmentValutazioni newInstance(String dati, Account account) {
        gson = new Gson();
        FragmentValutazioni fragment = new FragmentValutazioni();
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
        voti = Utils.getVotiFromJson(getActivity(), dati);
        materie = Utils.getMaterieFromJson(dati);

        risorse = getResources();
        View v = inflater.inflate(R.layout.tab_voti, container, false);

        recycler = (RecyclerView) v.findViewById(R.id.recycler_voti);
        num_voti = (TextView) v.findViewById(R.id.num_voti);
        fab = (FloatingActionButton) v.findViewById(R.id.scrollTop);
        fab.setOnClickListener(this);
        fab.setVisibility(View.GONE);

        recycler.addOnScrollListener(new ScrollListener() {
            @Override
            public void show() {
                fab.show();
            }

            @Override
            public void hide() {
                fab.hide();
            }
        });


        recycler.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        if (voti.size() > 0) {
            loadContent(null);
        }

        recycler.addItemDecoration(new RecyclerViewDividerItemDecoration(getContext()));

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.layout_refresh);
        refresh.setColorSchemeResources(R.color.rosso, R.color.verde, R.color.blu);

        if (preferences.getBoolean("refresh", true)) {
            refresh.setEnabled(true);
            refresh.setOnRefreshListener(this);
        } else {
            refresh.setEnabled(false);
        }

        num_voti.setText(risorse.getString(R.string.num_voti) + " " + adapter.getAllItemCount());

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            int metodo = data.getIntExtra("metodo", 3);
            String inizio = data.getStringExtra("data_inizio");
            String fine = data.getStringExtra("data_fine");
            String materia = data.getStringExtra("materia");

            if (metodo != 3) cambiaFiltroVoti(metodo, inizio, fine, materia);
        } else if (requestCode == 2) {
            String ordine = data.getStringExtra("ordine");
            cambiaOrdineVoti(ordine);
        }
    }

    public void loadContent(ArrayList<SezioneVoti> sezioni) {
        ArrayList<SezioneVoti> sezioni1;

        if (sezioni == null) {
            sezioni1 = Utils.ordinaLista(getActivity(), voti, "materia_cresc", materie, dati);
        } else {
            sezioni1 = sezioni;
        }

        adapter = new AdapterVoti(getActivity(),sezioni1);
        recycler.setAdapter(adapter);

        num_voti.setText(risorse.getString(R.string.num_voti) + " " + adapter.getAllItemCount());
        saveNumVoti();
    }

    private void cambiaFiltroVoti(int metodo, String inizio, String fine, String materia) {
        loadContent(Utils.filtraVoti(voti, metodo, inizio, fine, materia, materie));
    }

    private void cambiaOrdineVoti(String ordine)
    {
        loadContent(Utils.ordinaLista(getActivity(), voti, ordine, materie, dati));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu1, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchVotiDialog dialog = new SearchVotiDialog();
                Bundle b = new Bundle();
                b.putString("dati", dati);
                dialog.setArguments(b);
                dialog.setTargetFragment(this, 1);
                dialog.show(getActivity().getSupportFragmentManager(), "search");
                break;

            case R.id.reload:
                if (!refresh_flag) {
                    refresh.setRefreshing(true);
                    refresh();
                }
                break;

            case R.id.sort:
                SortVotiDialog sort = new SortVotiDialog();
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
    public void onRefresh() {
        if (!refresh_flag) {
            refresh();
        }
    }

    public void refresh() {
        refresh_flag = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean message = false;

                if (Utils.isConnectedToInternet()) {
                    String s = Utils.startLoginTask(getContext(), account);
                    if (!isEmpty(s)) {
                        voti = Utils.getVotiFromJson(getActivity(), s);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadContent(null);
                            }
                        });
                        saveNumVoti();
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
                        refresh_flag = false;
                    }
                });
            }
        });
        t.start();
    }

    public void saveNumVoti() {
        Update.salvaVoti(getActivity(), account, adapter.getAllItemCount());
    }

    @Override
    public void onClick(View v) {
        recycler.smoothScrollToPosition(0);
        fab.hide();
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
