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

/**
 * Created by michele on 16/01/16.
 */
public class SortMedieDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    private String sort = "";
    private RadioButton materia1, materia2, media1, media2;
    private Resources risorse;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.sort_medie_dialog, null);
        risorse = getResources();

        materia1 = (RadioButton) v.findViewById(R.id.materia1_cresc);
        materia2 = (RadioButton) v.findViewById(R.id.materia1_decr);
        media1 = (RadioButton) v.findViewById(R.id.media_cresc);
        media2 = (RadioButton) v.findViewById(R.id.media_decr);

        materia1.setOnCheckedChangeListener(this);
        materia2.setOnCheckedChangeListener(this);
        media1.setOnCheckedChangeListener(this);
        media2.setOnCheckedChangeListener(this);

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
            case R.id.materia1_cresc:
                if (checked)
                    sort = "materia_cresc";
                break;
            case R.id.materia1_decr:
                if (checked)
                    sort = "materia_decr";
                break;
            case R.id.media_cresc:
                if (checked)
                    sort = "media_cresc";
                break;
            case R.id.media_decr:
                if (checked)
                    sort = "media_decr";
                break;
        }
    }
}
