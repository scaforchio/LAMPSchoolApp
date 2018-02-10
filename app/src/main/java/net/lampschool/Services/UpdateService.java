package net.lampschool.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import net.lampschool.Utils.Account;
import net.lampschool.Utils.Update;
import net.lampschool.Utils.Utils;

import java.util.ArrayList;

public class UpdateService extends IntentService {
    public UpdateService() {
        super("Update Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);

        Utils.stopAlarmManager(this);

        ArrayList<Account> accounts = Utils.readListaAccount(this);
        ArrayList<boolean[]> stato_messaggi = new ArrayList<>();
        ArrayList<String> alunni = new ArrayList<>();

        for (int i = 0; i < accounts.size(); i++) {
            boolean[] stat = {false, false, false, false, false, false};
            Account account = accounts.get(i);
            String s = Utils.startLoginTask(this, account);
            if (s.equals("") || s.equals("errore connessione") || s.equals("tempo_esaurito")) {
                return;
            } else {
                stat[0] = Update.checkVoti(this, account, s);
                stat[1] = Update.checkAssenze(this, account, s);
                stat[2] = Update.checkRitardi(this, account, s);
                stat[3] = Update.checkUscite(this, account, s);
                stat[4] = Update.checkNote(this, account, s);
                stat[5] = Update.checkComunicazioni(this, account, s);
                stato_messaggi.add(stat);
                alunni.add(account.getCognome() + " " + account.getNome());
            }
        }

        Update.showNotification(this, stato_messaggi, alunni);
        Utils.startAlarmManager(this);
    }
}
