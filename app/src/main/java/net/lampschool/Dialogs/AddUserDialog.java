package net.lampschool.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.lampschool.Activities.UsersActivity;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Utils;

import java.util.ArrayList;

public class AddUserDialog extends DialogFragment {
    private EditText utente, password, link, suffisso, nome, cognome;
    private Resources risorse;
    private String tag;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        tag = getTag();
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_dialog, null);

        utente = (EditText) v.findViewById(R.id.utente);
        password = (EditText) v.findViewById(R.id.password);
        link = (EditText) v.findViewById(R.id.link);
        suffisso = (EditText) v.findViewById(R.id.suffisso);
        nome = (EditText) v.findViewById(R.id.nome_alunno);
        cognome = (EditText) v.findViewById(R.id.cognome_alunno);
        risorse = getResources();

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v).setMessage(risorse.getString(R.string.user_message))
                .setPositiveButton(risorse.getString(R.string.user_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton(risorse.getString(R.string.user_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ut, pass, suff, link1, nom, cogn;
                        ut = utente.getText().toString().replaceAll(" ", "").trim();
                        pass = password.getText().toString().replaceAll(" ", "").trim();
                        link1 = link.getText().toString().replaceAll(" ", "").trim();
                        suff = suffisso.getText().toString().replaceAll(" ", "").trim();
                        nom = nome.getText().toString().trim();
                        cogn = cognome.getText().toString().trim();
                        if (!ut.equals("") && !pass.equals("") && !link1.equals("") &&
                                !nom.equals("") && !cogn.equals("")) {
                            Account account = new Account();
                            account.setUsername(ut);
                            account.setPassword(pass);
                            account.setLink(link1);
                            account.setSuffisso(suff);
                            account.setNome(nom);
                            account.setCognome(cogn);

                            ArrayList<Account> accounts = Utils.readListaAccount(getActivity());
                            if (!accounts.contains(account)) {
                                Utils.salvaAccount(getActivity(), account);

                                if (tag.equals("add_dialog")) {
                                    getTargetFragment().onActivityResult(1, 1, null);
                                } else ((UsersActivity) getActivity()).refresh();

                                dialog.dismiss();
                            } else
                                Toast.makeText(getActivity(), risorse.getString(R.string.user_exist), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), risorse.getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
            }
        });

        return builder;
    }
}
