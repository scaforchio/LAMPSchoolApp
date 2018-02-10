package net.lampschool.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Uscita;

import java.util.ArrayList;

public class AdapterUscite extends RecyclerView.Adapter<AdapterUscite.ViewHolder> {
    private ArrayList<Uscita> uscite;
    private Context context;
    private int ore_tot = 0;

    public AdapterUscite(Context context, ArrayList<Uscita> uscite) {
        this.uscite = uscite;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_uscite_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Uscita uscita = uscite.get(position);

        holder.data_usc.setText(uscita.getData());

        holder.ora_usc.setText(uscita.getOra_Usc());
    }

    @Override
    public int getItemCount() {
        return uscite.size();
    }

    public void setUscite(ArrayList<Uscita> uscite) {
        this.uscite = uscite;
        notifyDataSetChanged();
    }

    public int getOre_tot() {
        return ore_tot;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView data_usc;
        TextView ora_usc;

        public ViewHolder(View v) {
            super(v);
            data_usc = (TextView) v.findViewById(R.id.data_usc);
            ora_usc = (TextView) v.findViewById(R.id.ora_usc);
        }
    }
}
