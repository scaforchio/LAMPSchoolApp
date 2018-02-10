package net.lampschool.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import net.lampschool.R;
import net.lampschool.Utils.Utils;

public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceClickListener {
    public static int RESULT_CODE = 0;
    boolean dialog;
    private Toolbar toolbar;
    private Preference utenti, refresh, export, import_pref, info;
    private Resources risorse;
    private static final int REQUEST_READ_STORAGE = 1;
    private static final int REQUEST_WRITE_STORAGE = 2;
    private static final int FILE_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        risorse = getResources();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(risorse.getString(R.string.title_activity_impostazioni));
        setSupportActionBar(toolbar);
        dialog = getIntent().getBooleanExtra("dialog", false);

        RESULT_CODE = dialog ? 1 : 2;

        addPreferencesFromResource(R.xml.preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        utenti = findPreference("utenti");
        refresh = findPreference("refresh");
        info = findPreference("informazioni");
        export = findPreference("export");
        import_pref = findPreference("import");
        utenti.setOnPreferenceClickListener(this);
        refresh.setOnPreferenceClickListener(this);
        info.setOnPreferenceClickListener(this);
        export.setOnPreferenceClickListener(this);
        import_pref.setOnPreferenceClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CODE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case FILE_REQUEST_CODE:
                    String name = data.getData().getLastPathSegment();

                    int index = name.lastIndexOf(".");
                    String extension = name.substring(index + 1);

                    if (!extension.equals("lamp")) {
                        Toast.makeText(this, risorse.getString(R.string.fileformat_error), Toast.LENGTH_SHORT).show();
                    } else {
                        Uri uri = data.getData();
                        boolean result = Utils.importFileFromUri(this, uri);

                        if (result) {
                            Toast.makeText(this, risorse.getString(R.string.import_ok), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, risorse.getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals("refresh"))
            Toast.makeText(this, risorse.getString(R.string.riavvio_richiesto), Toast.LENGTH_SHORT).show();
        else if (key.equals("utenti")) {
            Intent intent = new Intent(this, UsersActivity.class);
            startActivity(intent);
        } else if (key.equals("informazioni")) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        } else if (key.equals("export")) {
            if (checkWritePermission()) {
                esportaAccount();
            }
        } else if (key.equals("import")) {
            if (checkReadPermission()) {
                startFileChooser();
            }
        }

        return true;
    }

    private void startFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    private void esportaAccount() {
        boolean success = Utils.exportAccountsToUri(this);

        if (success)
            Toast.makeText(this, risorse.getString(R.string.export_ok), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, risorse.getString(R.string.export_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startFileChooser();
                }
                break;

            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    esportaAccount();
                }
                break;
        }
    }

    @TargetApi(23)
    private boolean checkWritePermission() {
        int writeStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (writeStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, risorse.getString(R.string.write_permission_required), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        return false;
    }

    @TargetApi(23)
    private boolean checkReadPermission() {
        int readStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (readStorage == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, risorse.getString(R.string.read_permission_required), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_STORAGE);
            }
        }

        return false;
    }
}
