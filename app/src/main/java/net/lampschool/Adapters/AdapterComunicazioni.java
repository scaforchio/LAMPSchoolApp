package net.lampschool.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Comunicazione;

import java.util.ArrayList;

public class AdapterComunicazioni extends RecyclerView.Adapter<AdapterComunicazioni.ViewHolder> {
    private ArrayList<Comunicazione> comunicazioni;
    private Context context;

    public AdapterComunicazioni(Context context, ArrayList<Comunicazione> comunicazioni) {
        this.comunicazioni = comunicazioni;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comunicazioni_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comunicazione comunicazione = comunicazioni.get(position);

        holder.oggetto_com.setText(comunicazione.getOggetto());
        holder.data_com.setText("Data: " + comunicazione.getData());
    }

    public Comunicazione getItemAtPosition(int position) {
        return comunicazioni.get(position);
    }

    @Override
    public int getItemCount() {
        return comunicazioni.size();
    }

    public void setComunicazioni(ArrayList<Comunicazione> comunicazioni) {
        this.comunicazioni = comunicazioni;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView oggetto_com;
        TextView data_com;

        public ViewHolder(View v) {
            super(v);
            oggetto_com = (TextView) v.findViewById(R.id.oggetto_com);
            data_com = (TextView) v.findViewById(R.id.data_com);
        }
    }
}
