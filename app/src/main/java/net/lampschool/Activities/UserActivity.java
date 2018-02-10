package net.lampschool.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import net.lampschool.R;
import net.lampschool.Utils.Account;
import net.lampschool.Utils.Utils;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {
    private static int EDIT_MODE = 0;
    private EditText utente, password, link, suffisso, nome, cognome;
    private Toolbar toolbar;
    private Account account;
    private FloatingActionButton accept, cancel;
    private Resources risorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        risorse = getResources();

        Gson gson = new Gson();
        Intent intent = getIntent();
        account = gson.fromJson(intent.getStringExtra("account"), Account.class);

        toolbar.setTitle(account.getNome() + " " + account.getCognome());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        accept = (FloatingActionButton) findViewById(R.id.accept);
        cancel = (FloatingActionButton) findViewById(R.id.cancel);
        utente = (EditText) findViewById(R.id.utente1);
        password = (EditText) findViewById(R.id.password1);
        link = (EditText) findViewById(R.id.link1);
        suffisso = (EditText) findViewById(R.id.suffisso1);
        nome = (EditText) findViewById(R.id.nome_alunno1);
        cognome = (EditText) findViewById(R.id.cognome_alunno1);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        accept.hide();
        cancel.hide();

        utente.setText(account.getUsername());
        password.setText(account.getPassword());
        link.setText(account.getLink());
        suffisso.setText(account.getSuffisso());
        nome.setText(account.getNome());
        cognome.setText(account.getCognome());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                if (EDIT_MODE == 0) {
                    editMode(true);
                    accept.show();
                    cancel.show();
                    return true;
                }
                return false;

            case R.id.delete:
                Utils.eliminaAccount(this, account);
                onBackPressed();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (EDIT_MODE == 1) {
            editMode(false);
            accept.hide();
            cancel.hide();
        } else super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        FloatingActionButton fab = (FloatingActionButton) v;
        if (fab.equals(accept))
        {
            Account account1 = new Account();

            String nome1 = nome.getText().toString().trim();
            String cognome1 = cognome.getText().toString().trim();

            account1.setUsername(utente.getText().toString().replaceAll(" ", "").trim());
            account1.setPassword(password.getText().toString().replaceAll(" ", "").trim());
            account1.setSuffisso(suffisso.getText().toString().replaceAll(" ", "").trim());
            account1.setLink(link.getText().toString().replaceAll(" ", "").trim());
            account1.setCognome(cognome1);
            account1.setNome(nome1);

            ArrayList<Account> accounts = Utils.readListaAccount(this);
            accounts.remove(account);
            accounts.add(account1);
            Utils.salvaListaAccount(this, accounts);
            Toast.makeText(this, risorse.getString(R.string.edit_ok), Toast.LENGTH_SHORT).show();
            getSupportActionBar().setTitle(nome1 + " " + cognome1);
        }

        editMode(false);
        cancel.hide();
        accept.hide();
    }

    public void editMode(boolean stato) {
        EDIT_MODE = stato ? 1 : 0;
        utente.setEnabled(stato);
        password.setEnabled(stato);
        link.setEnabled(stato);
        suffisso.setEnabled(stato);
        nome.setEnabled(stato);
        cognome.setEnabled(stato);
    }
}
