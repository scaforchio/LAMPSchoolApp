package net.lampschool.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import net.lampschool.Utils.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginTask extends AsyncTask<Object, Object, String> {

    @Override
    protected String doInBackground(Object... params) {
        Account account = (Account) params[0];
        String link = account.getLink();
        String suffisso = account.getSuffisso();
        String utente = account.getUsername();
        String password = account.getEncryptedPassword();

        String QueryLogin = "http://" + link + "/lsapp/jsonlogin.php?utente=" + utente +
                "&password=" + password + "&suffisso=" + suffisso+"&versione=16";

        return getRisultato(QueryLogin);
    }

    private String getRisultato(String query) {
        BufferedReader bufferedReader;
        try {
            URL url = new URL(query);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }


            return sb.toString().trim();
        } catch (MalformedURLException e) {
            Log.d("MALFORMED URL: ", e.toString());
        } catch (IOException e) {
            Log.d("IOEXCEPTION: ", e.toString());
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
