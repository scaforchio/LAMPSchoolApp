package net.lampschool.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Ritardo;

import java.util.ArrayList;

public class AdapterRitardi extends RecyclerView.Adapter<AdapterRitardi.ViewHolder> {
    private ArrayList<Ritardo> ritardi;
    private Context context;
    private Resources risorse;
    private int ore_tot = 0;

    public AdapterRitardi(Context context, ArrayList<Ritardo> ritardi) {
        this.ritardi = ritardi;
        this.context = context;
        risorse = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ritardi_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ritardo ritardo = ritardi.get(position);

        holder.data_rit.setText(ritardo.getData());

        boolean giustificato = ritardo.isGiustificato();

        if (giustificato) {
            holder.giustifica.setText(risorse.getString(R.string.giustifica_rit));
            holder.giustifica.setTextColor(holder.data_rit.getCurrentTextColor());
        } else {
            holder.giustifica.setTextColor(context.getResources().getColor(R.color.rosso));
            holder.giustifica.setText(risorse.getString(R.string.no_giustifica_rit));
        }

        holder.ora_ent.setText(ritardo.getOra_ent());
    }

    @Override
    public int getItemCount() {
        return ritardi.size();
    }

    public void setRitardi(ArrayList<Ritardo> ritardi) {
        this.ritardi = ritardi;
        notifyDataSetChanged();
    }

    public int getOre_tot() {
        return ore_tot;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView data_rit;
        TextView giustifica;
        TextView ora_ent;

        public ViewHolder(View v) {
            super(v);
            data_rit = (TextView) v.findViewById(R.id.data_rit);
            giustifica = (TextView) v.findViewById(R.id.giustifica_rit);
            ora_ent = (TextView) v.findViewById(R.id.ora_ent);
        }
    }
}
