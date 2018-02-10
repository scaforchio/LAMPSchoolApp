package net.lampschool.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.lampschool.R;
import net.lampschool.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SearchVotiDialog extends DialogFragment implements View.OnClickListener {
    private Spinner materia, metodo_ricerca;
    private Button inizio, fine;
    private String data_inizio, data_fine, dati;
    private Resources risorse;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.search_voti_dialog, null);
        inizio = (Button) v.findViewById(R.id.button);
        fine = (Button) v.findViewById(R.id.button2);
        materia = (Spinner) v.findViewById(R.id.spinner);
        metodo_ricerca = (Spinner) v.findViewById(R.id.spinner2);

        String[] metodi = {"Materia e Data", "Data", "Materia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, metodi);
        metodo_ricerca.setAdapter(adapter);
        metodo_ricerca.setSelection(1);

        risorse = getResources();
        dati = getArguments().getString("dati");
        String[] materie = Utils.getMaterieFromJson(dati);

        adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, materie);
        materia.setAdapter(adapter);

        data_inizio = getOneWeekAgoDate();
        data_fine = getTodayDate();

        inizio.setText(data_inizio);
        fine.setText(data_fine);

        inizio.setOnClickListener(this);
        fine.setOnClickListener(this);

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v).setMessage(risorse.getString(R.string.search_message))
                .setPositiveButton(risorse.getString(R.string.search_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();

                        switch (metodo_ricerca.getSelectedItemPosition()) {
                            case 0:
                                intent.putExtra("metodo", 0);
                                intent.putExtra("data_inizio", data_inizio);
                                intent.putExtra("data_fine", data_fine);
                                intent.putExtra("materia", (String) materia.getSelectedItem());
                                break;
                            case 1:
                                intent.putExtra("metodo", 1);
                                intent.putExtra("data_inizio", data_inizio);
                                intent.putExtra("data_fine", data_fine);
                                break;
                            case 2:
                                intent.putExtra("metodo", 2);
                                intent.putExtra("materia", (String) materia.getSelectedItem());
                        }

                        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                    }
                })
                .setNegativeButton(risorse.getString(R.string.search_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
            }
        });

        builder.getWindow().getAttributes().windowAnimations = R.style.DialogAnim;

        return builder;
    }

    private String getTodayDate() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        return sdf.format(calendar.getTime());
    }

    private String getOneWeekAgoDate() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return sdf.format(calendar.getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    data_inizio = bundle.getString("data");
                    inizio.setText(data_inizio);
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    data_fine = bundle.getString("data");
                    fine.setText(data_fine);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        DateDialog picker = new DateDialog();

        if (b.equals(inizio)) {
            Bundle args = new Bundle();
            args.putString("data", data_inizio);
            picker.setArguments(args);
            picker.setTargetFragment(this, 1);
            picker.show(getFragmentManager(), "date_picker");
        } else {
            Bundle args = new Bundle();
            args.putString("data", data_fine);
            picker.setArguments(args);
            picker.setTargetFragment(this, 2);
            picker.show(getFragmentManager(), "date_picker");
        }
    }
}
