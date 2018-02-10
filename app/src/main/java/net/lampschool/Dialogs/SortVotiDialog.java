package net.lampschool.Dialogs;

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
import android.widget.CompoundButton;
import android.widget.RadioButton;

import net.lampschool.R;

public class SortVotiDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private String sort = "";
    private RadioButton materia1, materia2, voto1, voto2, tipo1, tipo2, data1, data2, quadrimestre;
    private Resources risorse;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.sort_voti_dialog, null);
        risorse = getResources();

        materia1 = (RadioButton) v.findViewById(R.id.materia_cresc);
        materia2 = (RadioButton) v.findViewById(R.id.materia_decr);
        voto1 = (RadioButton) v.findViewById(R.id.voto_cresc);
        voto2 = (RadioButton) v.findViewById(R.id.voto_decr);
        tipo1 = (RadioButton) v.findViewById(R.id.tipo_cresc);
        tipo2 = (RadioButton) v.findViewById(R.id.tipo_decr);
        data1 = (RadioButton) v.findViewById(R.id.data_cresc);
        data2 = (RadioButton) v.findViewById(R.id.data_decr);
        quadrimestre = (RadioButton) v.findViewById(R.id.sort_quadr);

        materia1.setOnCheckedChangeListener(this);
        materia2.setOnCheckedChangeListener(this);
        voto1.setOnCheckedChangeListener(this);
        voto2.setOnCheckedChangeListener(this);
        tipo1.setOnCheckedChangeListener(this);
        tipo2.setOnCheckedChangeListener(this);
        data1.setOnCheckedChangeListener(this);
        data2.setOnCheckedChangeListener(this);
        quadrimestre.setOnCheckedChangeListener(this);

        final AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v).setMessage(risorse.getString(R.string.sort_message))
                .setPositiveButton(risorse.getString(R.string.sort_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra("ordine", sort);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), 1, intent);
                    }
                })
                .setNegativeButton(risorse.getString(R.string.sort_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
        switch (buttonView.getId()) {
            case R.id.materia_cresc:
                if (checked)
                    sort = "materia_cresc";
                break;
            case R.id.materia_decr:
                if (checked)
                    sort = "materia_decr";
                break;
            case R.id.voto_cresc:
                if (checked)
                    sort = "voto_cresc";
                break;
            case R.id.voto_decr:
                if (checked)
                    sort = "voto_decr";
                break;
            case R.id.tipo_cresc:
                if (checked)
                    sort = "tipo_cresc";
                break;
            case R.id.tipo_decr:
                if (checked)
                    sort = "tipo_decr";
                break;
            case R.id.data_cresc:
                if (checked)
                    sort = "data_cresc";
                break;
            case R.id.data_decr:
                if (checked)
                    sort = "data_decr";
                break;

            case R.id.sort_quadr:
                if (checked)
                    sort = "quadr";
                break;
        }
    }
}
