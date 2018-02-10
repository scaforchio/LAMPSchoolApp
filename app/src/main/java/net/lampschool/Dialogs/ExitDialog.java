package net.lampschool.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import net.lampschool.R;


public class ExitDialog extends DialogFragment {
    private Resources risorse;

    public ExitDialog newInstance() {
        ExitDialog dialog = new ExitDialog();
        Bundle b = new Bundle();
        b.putBoolean("flag", false);
        dialog.setArguments(b);
        return dialog;
    }

    public ExitDialog newInstance(boolean flag) {
        ExitDialog dialog = new ExitDialog();
        Bundle b = new Bundle();
        b.putBoolean("flag", flag);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        risorse = getResources();
        Bundle b = getArguments();
        final boolean flag = b.getBoolean("flag");

        final AlertDialog builder = new AlertDialog.Builder(getActivity()).setMessage(risorse.getString(R.string.exit_message))
                .setPositiveButton(risorse.getString(R.string.exit_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton(risorse.getString(R.string.exit_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (flag) {
                            AccountManagerDialog manager = new AccountManagerDialog();
                            manager.show(getFragmentManager(), "account_manager_dialog");
                        }
                    }
                }).create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
            }
        });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        dialog.dismiss();
                        if (flag) {
                            AccountManagerDialog manager = new AccountManagerDialog();
                            manager.show(getFragmentManager(), "account_manager_dialog");
                        }
                    }
                }

                return true;
            }
        });

        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        return builder;
    }
}
