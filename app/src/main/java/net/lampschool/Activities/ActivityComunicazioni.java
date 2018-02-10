package net.lampschool.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import net.lampschool.R;

public class ActivityComunicazioni extends AppCompatActivity {
    private Toolbar toolbar;
    private Resources risorse;
    private WebView testo_com;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicazioni);
        risorse = getResources();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(risorse.getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String html = getIntent().getStringExtra("testo_com");

        testo_com = (WebView) findViewById(R.id.testo_com);
        testo_com.loadData(html, "text/html", "UTF-8");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
