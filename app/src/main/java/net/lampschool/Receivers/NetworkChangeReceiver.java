package net.lampschool.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.lampschool.Utils.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notifiche = preferences.getBoolean("notifiche", true);

        if (notifiche) {
            Utils.stopAlarmManager(context);
            Utils.startAlarmManager(context);
        }
    }
}
