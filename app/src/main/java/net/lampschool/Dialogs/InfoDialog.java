package net.lampschool.Dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.lampschool.R;
import net.lampschool.Utils.Utils;

public class InfoDialog extends DialogFragment {
    private Resources risorse;
    private String dati;
    private TextView nome, cognome, classe, data_nascita;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.info_dialog, null);

        Bundle b = getArguments();
        dati = b.getString("dati");
        risorse = getResources();

        String[] info = Utils.getInformazioniAlunno(dati);

        nome = (TextView) v.findViewById(R.id.info_nome);
        nome.setText("Nome: " + info[0]);
        cognome = (TextView) v.findViewById(R.id.info_cognome);
        cognome.setText("Cognome: " + info[1]);
        classe = (TextView) v.findViewById(R.id.info_classe);
        classe.setText("Classe: " + info[3]);
        data_nascita = (TextView) v.findViewById(R.id.info_data);
        data_nascita.setText("Data di nascita: " + info[2]);

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v).setMessage(risorse.getString(R.string.info_message))
                .create();

        return builder;
    }
}
