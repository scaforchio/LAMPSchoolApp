package net.lampschool.Dialogs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.lampschool.Activities.MainActivity;
import net.lampschool.Activities.SettingsActivity;
import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountManagerDialog extends DialogFragment implements View.OnClickListener {
    private Resources risorse;
    private MainActivity parentActivity;
    private ListView accounts;
    private Button b;
    private ArrayList<Account> alunni;
    private Gson gson;
    private static final int REQUEST_INTERNET = 1;
    private static final int REQUEST_NETWORK = 2;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Utils.stopAlarmManager(getActivity());
        risorse = getResources();
        gson = new Gson();
        LayoutInflater inflater = getActivity().getLayoutInflater();

        parentActivity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.account_manager_dialog, null);
        accounts = (ListView) v.findViewById(R.id.listAccount);
        b = (Button) v.findViewById(R.id.button5);
        b.setOnClickListener(this);

        loadLista();

        final AlertDialog builder = new AlertDialog.Builder(getActivity()).setView(v)
                .setMessage(risorse.getString(R.string.manager_message))
                .setPositiveButton(risorse.getString(R.string.login_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                })
                .setNegativeButton(risorse.getString(R.string.login_negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExitDialog dialog1 = new ExitDialog().newInstance(true);
                        dialog1.show(getFragmentManager(), "exit_dialog");
                    }
                })
                .setNeutralButton(risorse.getString(R.string.login_neutral), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).create();

        builder.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(final DialogInterface dialog)
            {
                Button b = builder.getButton(DialogInterface.BUTTON_NEUTRAL);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parentActivity, SettingsActivity.class);
                        intent.putExtra("dialog", true);
                        startActivityForResult(intent, 2);
                    }
                });

                Button b1 = builder.getButton(DialogInterface.BUTTON_POSITIVE);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Utils.isConnectedToInternet()) {

                            if (alunni.size() > 0) {
                                int i = accounts.getCheckedItemPosition();
                                final Account account1 = alunni.get(i);

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage(risorse.getString(R.string.progress_message));
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                if (checkInternetPermission() && checkNetworkPermission()) {
                                    connect(progressDialog, dialog, account1);
                                }
                            } else {
                                parentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(parentActivity, risorse.getString(R.string.user_add), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(parentActivity, risorse.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(risorse.getColor(R.color.colorPrimary));
                builder.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(risorse.getColor(R.color.colorPrimary));
            }
        });

        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        dialog.dismiss();
                        ExitDialog dialog1 = new ExitDialog().newInstance(true);
                        dialog1.show(getFragmentManager(), "exit_dialog");
                    }
                }

                return true;
            }
        });

        builder.setCanceledOnTouchOutside(false);
        builder.setCancelable(false);
        return builder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==2)
        {
            if(resultCode==1)
            {
                AccountManagerDialog dialog = new AccountManagerDialog();
                dialog.show(getFragmentManager(), "account_manager");
            }
        }
        else
        {
            loadLista();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("RUNTIME_PERMISSION", "PERMESSO INTERNET GARANTITO");
                }
                break;

            case REQUEST_NETWORK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("notifiche", true);
                    editor.commit();

                    Log.d("RUNTIME_PERMISSION", "PERMESSO NETWORK GARANTITO");
                }
                break;
        }
    }

    private void connect(final ProgressDialog progressDialog, final DialogInterface dialog, final Account account) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final String s = Utils.startLoginTask(parentActivity, account);

                if (s.equals("errore connessione") || s.equals("")) {
                    parentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(parentActivity, risorse.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressDialog.cancel();
                } else if (s.equals("tempo_esaurito")) {
                    parentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(parentActivity, parentActivity.getResources().getString(R.string.tempo_connessione), Toast.LENGTH_SHORT).show();
                        }
                    });
                    progressDialog.cancel();
                } else {
                    try {
                        JSONObject object = new JSONObject(s);
                        if (object.getInt("alunno") == 0) {
                            parentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(parentActivity, risorse.getString(R.string.errore_registro), Toast.LENGTH_SHORT).show();
                                }
                            });
                            progressDialog.cancel();
                        } else {
                            parentActivity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run() {
                                    String jsonAccount = gson.toJson(account);
                                    final Intent intent = new Intent();
                                    intent.putExtra("dati", s);
                                    intent.putExtra("account", jsonAccount);
                                    parentActivity.setDati(intent);
                                    progressDialog.cancel();
                                }
                            });
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        parentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(parentActivity, risorse.getString(R.string.errore_registro), Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressDialog.cancel();
                    }
                }
            }
        });
        t.start();
    }

    @TargetApi(23)
    private boolean checkInternetPermission() {
        int internet = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.INTERNET);

        if (internet == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.INTERNET)) {
                Toast.makeText(getActivity(), risorse.getString(R.string.internet_permission_required), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.INTERNET},
                        REQUEST_INTERNET);
            }
        }

        return false;
    }

    @TargetApi(23)
    private boolean checkNetworkPermission() {
        int internet = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_NETWORK_STATE);

        if (internet == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_NETWORK_STATE)) {
                Toast.makeText(getActivity(), risorse.getString(R.string.network_permission_required), Toast.LENGTH_SHORT).show();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("notifiche", false);
                editor.commit();

                return false;
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        REQUEST_NETWORK);
            }
        }

        return false;
    }

    @Override
    public void onClick(View v)
    {
        AddUserDialog dialog = new AddUserDialog();
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "add_dialog");
    }

    private void loadLista() {
        alunni = Utils.readListaAccount(getActivity());

        if (alunni.size() > 0) {
            String[] nominativi = new String[alunni.size()];

            for (int i = 0; i < alunni.size(); i++) {
                Account account1 = alunni.get(i);
                nominativi[i] = account1.getNome() + " " + account1.getCognome();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_single_choice, nominativi);
            accounts.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            accounts.setAdapter(adapter);
            accounts.setItemChecked(0, true);
        } else {
            String[] nominativi = new String[1];
            nominativi[0] = risorse.getString(R.string.no_users);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, nominativi);
            accounts.setAdapter(adapter);
        }
    }
}
