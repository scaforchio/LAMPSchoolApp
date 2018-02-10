package net.lampschool.Tasks;

import android.os.AsyncTask;

import java.net.URL;
import java.net.URLConnection;

public class CheckConnectionTask extends AsyncTask<Void, Void, Boolean> {
    @Override
    protected Boolean doInBackground(Void... params) {
        String link = "http://www.google.com";
        int timeout = 10000;
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
    }
}
