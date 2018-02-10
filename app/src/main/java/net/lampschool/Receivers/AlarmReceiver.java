package net.lampschool.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import net.lampschool.Services.UpdateService;
import net.lampschool.Utils.Utils;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar curr_cal = Calendar.getInstance();
        curr_cal.setTimeInMillis(System.currentTimeMillis());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int intervallo = preferences.getInt("intervallo", 60);


        if (intervallo == 60 && (curr_cal.get(Calendar.HOUR_OF_DAY) >= 8 && curr_cal.get(Calendar.HOUR_OF_DAY) < 14)) {
            Utils.stopAlarmManager(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("intervallo", 10);
            editor.commit();

            Log.d("INTERVALLO", "10");

            Utils.startAlarmManager(context);
        } else if (intervallo == 10 && (curr_cal.get(Calendar.HOUR_OF_DAY) < 8 || curr_cal.get(Calendar.HOUR_OF_DAY) >= 14)) {
            Utils.stopAlarmManager(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("intervallo", 60);
            editor.commit();

            Log.d("INTERVALLO", "60");

            Utils.startAlarmManager(context);
        } else {
            Intent i = new Intent(context, UpdateService.class);
            context.startService(i);
        }
    }
}
