package net.lampschool.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Assenza;

import java.util.ArrayList;

public class AdapterAssenze extends RecyclerView.Adapter<AdapterAssenze.ViewHolder> {
    private ArrayList<Assenza> assenze;
    private Context context;
    private Resources risorse;

    public AdapterAssenze(Context context, ArrayList<Assenza> assenze) {
        this.assenze = assenze;
        this.context = context;
        risorse = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_assenze_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Assenza assenza = assenze.get(position);

        holder.data_ass.setText(assenza.getData());

        if (assenza.isGiustificata()) {
            holder.giustifica.setText(risorse.getString(R.string.giustifica_ass));
            holder.giustifica.setTextColor(holder.data_ass.getCurrentTextColor());
        } else {
            holder.giustifica.setTextColor(risorse.getColor(R.color.rosso));
            holder.giustifica.setText(risorse.getString(R.string.no_giustifica_ass));
        }
    }

    @Override
    public int getItemCount() {
        return assenze.size();
    }

    public void setAssenze(ArrayList<Assenza> assenze) {
        this.assenze = assenze;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView data_ass;
        TextView giustifica;

        public ViewHolder(View v) {
            super(v);
            data_ass = (TextView) v.findViewById(R.id.data_ass);
            giustifica = (TextView) v.findViewById(R.id.giustifica_ass);
        }
    }
}
