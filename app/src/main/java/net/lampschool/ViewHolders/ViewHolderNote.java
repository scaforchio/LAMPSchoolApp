package net.lampschool.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import net.lampschool.R;
import net.lampschool.Utils.Nota;

public class ViewHolderNote extends ChildViewHolder
{
    private TextView cognome_doc;
    private TextView nome_doc;
    private TextView data_nota;
    private TextView nota;

    public ViewHolderNote(View v) {
        super(v);
        cognome_doc = (TextView) v.findViewById(R.id.cognome_doc);
        nome_doc = (TextView) v.findViewById(R.id.nome_doc);
        data_nota = (TextView) v.findViewById(R.id.data_nota);
        nota = (TextView) v.findViewById(R.id.descrizione_nota);
    }

    public void bind(Nota n)
    {
        cognome_doc.setText(n.getCognome());
        nome_doc.setText(n.getNome());
        data_nota.setText(n.getData());
        nota.setText("Nota: " + n.getNota());
    }
}
