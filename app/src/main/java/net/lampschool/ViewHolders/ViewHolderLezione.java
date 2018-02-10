package net.lampschool.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import net.lampschool.R;
import net.lampschool.Utils.Lezione;

public class ViewHolderLezione extends ChildViewHolder
{
    private TextView attivita_lez;
    private TextView argomento_lez;
    private TextView data_lez;

    public ViewHolderLezione(View v) {
        super(v);
        attivita_lez = (TextView) v.findViewById(R.id.attivita_lez);
        argomento_lez = (TextView) v.findViewById(R.id.argomento_lez);
        data_lez = (TextView) v.findViewById(R.id.data_lez);
    }

    public void bind(Lezione lezione)
    {
        attivita_lez.setText("Attivita: " + lezione.getAttivita());
        argomento_lez.setText("Argomento: " + lezione.getArgomento());
        data_lez.setText("Data: " + lezione.getData());
    }
}
