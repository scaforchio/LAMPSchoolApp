package net.lampschool.ViewHolders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import net.lampschool.R;
import net.lampschool.Utils.Utils;
import net.lampschool.Utils.Voto;

public class ViewHolderVoti extends ChildViewHolder
{
    private TextView tipo;
    private TextView voto;
    private TextView data;
    private TextView giudizio;

    public ViewHolderVoti(View v) {
        super(v);
        tipo = (TextView) v.findViewById(R.id.tipo);
        voto = (TextView) v.findViewById(R.id.voto);
        data = (TextView) v.findViewById(R.id.data);
        giudizio = (TextView) v.findViewById(R.id.giudizio);
    }

    public void bind(Context context, Voto v)
    {
        tipo.setText(v.getTipo());
        String s = Utils.votoToString(v.getVoto());
        voto.setText(s);

        int colore = getColorForVoto(context,v.getVoto());
        if (colore != -1) {
            voto.setTextColor(colore);
        } else voto.setTextColor(tipo.getCurrentTextColor());

        data.setText(v.getData());
        String s1 = v.getGiudizio();

        if (!giudizio.equals("")) {
            giudizio.setText("Note: " + s1);
        } else giudizio.setText("");
    }

    private int getColorForVoto(Context context,float voto) {
        Resources risorse = context.getResources();
        if (voto >= 6.0) {
            return risorse.getColor(R.color.verde);
        } else if (voto < 6.0) {
            return risorse.getColor(R.color.rosso);
        }

        return -1;
    }
}
