package net.lampschool.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Account;

import java.util.ArrayList;

public class AdapterUtenti extends RecyclerView.Adapter<AdapterUtenti.ViewHolder> {
    private ArrayList<Account> accounts;
    private ArrayList<Account> accounts_old;
    private Context context;
    private Resources risorse;

    public AdapterUtenti(Context context, ArrayList<Account> accounts) {
        this.accounts = accounts;
        accounts_old = (ArrayList<Account>) accounts.clone();
        this.context = context;
        risorse = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_account_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account account = accounts.get(position);
        holder.den_utente.setText(account.getNome() + " " + account.getCognome());
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
        accounts_old = (ArrayList<Account>) accounts.clone();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void findUtenti(String query) {
        if (query.equals("")) {
            accounts = (ArrayList<Account>) accounts_old.clone();
        } else {
            accounts.clear();
            for (int i = 0; i < accounts_old.size(); i++) {
                Account account = accounts_old.get(i);
                String den = account.getCognome() + " " + account.getNome();

                if (den.toLowerCase().contains(query.toLowerCase())) {
                    accounts.add(account);
                }
            }
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView den_utente;

        public ViewHolder(View v) {
            super(v);
            den_utente = (TextView) v.findViewById(R.id.den_utente);
        }
    }
}
