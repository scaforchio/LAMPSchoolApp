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

public class SortLezioniDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private String sort = "";
    private RadioButton data1, data2, quadrimestre, materia1, materia2;
    private Resources risorse;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.sort_lezioni_dialog, null);
        risorse = getResources();

        data1 = (RadioButton) v.findViewById(R.id.data_lez_cresc);
        data2 = (RadioButton) v.findViewById(R.id.data_lez_decr);
        quadrimestre = (RadioButton) v.findViewById(R.id.sort_lez_quadr);
        materia1 = (RadioButton) v.findViewById(R.id.materia_lez_cresc);
        materia2 = (RadioButton) v.findViewById(R.id.materia_lez_decr);

        data1.setOnCheckedChangeListener(this);
        data2.setOnCheckedChangeListener(this);
        quadrimestre.setOnCheckedChangeListener(this);
        materia1.setOnCheckedChangeListener(this);
        materia2.setOnCheckedChangeListener(this);

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
            case R.id.data_lez_cresc:
                if (checked)
                    sort = "data_cresc";
                break;
            case R.id.data_lez_decr:
                if (checked)
                    sort = "data_decr";
                break;

            case R.id.sort_lez_quadr:
                if (checked)
                    sort = "quadr";
                break;
            case R.id.materia_lez_cresc:
                if (checked)
                    sort = "materia_cresc";
                break;
            case R.id.materia_lez_decr:
                if (checked)
                    sort = "materia_decr";
                break;
        }
    }
}
