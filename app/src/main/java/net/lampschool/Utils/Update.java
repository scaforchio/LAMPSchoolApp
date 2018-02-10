package net.lampschool.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import net.lampschool.Activities.MainActivity;
import net.lampschool.R;

import java.util.ArrayList;

public class Update {
    public static boolean checkAssenze(Context context, Account account, String dati) {
        int num_assenze = Utils.getNumAssenzeFromJson(dati);
        int num = account.getNumAssenze();
        if (num_assenze > num) {
            salvaAssenze(context, account, num_assenze);
            return true;
        } else return false;
    }

    public static void salvaAssenze(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumAssenze(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static boolean checkRitardi(Context context, Account account, String dati) {
        int num_ritardi = Utils.getNumRitardiFromJson(dati);
        int num = account.getNumRitardi();
        if (num_ritardi > num) {
            salvaRitardi(context, account, num_ritardi);
            return true;
        } else return false;
    }

    public static void salvaRitardi(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumRitardi(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static boolean checkUscite(Context context, Account account, String dati) {
        int num_uscite = Utils.getNumUsciteFromJson(dati);
        int num = account.getNumUscite();
        if (num_uscite > num) {
            salvaUscite(context, account, num_uscite);
            return true;
        } else return false;
    }

    public static void salvaUscite(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumUscite(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static boolean checkNote(Context context, Account account, String dati) {
        int num_note = Utils.getNumNoteFromJson(dati);
        int num = account.getNumNote();
        if (num_note > num) {
            salvaNote(context, account, num_note);
            return true;
        } else return false;
    }

    public static void salvaNote(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumNote(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static boolean checkVoti(Context context, Account account, String dati) {
        int num_voti = Utils.getNumVotiFromJson(dati);
        int num = account.getNumVoti();
        if (num_voti > num) {
            salvaVoti(context, account, num_voti);
            return true;
        } else return false;
    }

    public static void salvaVoti(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumVoti(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static boolean checkComunicazioni(Context context, Account account, String dati) {
        int num_comunicazioni = Utils.getNumComunicazioniFromJson(dati);
        int num = account.getNumComunicazioni();
        if (num_comunicazioni > num) {
            salvaComunicazioni(context, account, num_comunicazioni);
            return true;
        } else return false;
    }

    public static void salvaComunicazioni(Context context, Account account, int num) {
        if (num >= 0) {
            ArrayList<Account> accounts = Utils.readListaAccount(context);
            if (accounts.size() > 0) {
                int index = accounts.indexOf(account);
                accounts.get(index).setNumComunicazioni(num);
                Utils.salvaListaAccount(context, accounts);
            }
        }
    }

    public static void showNotification(Context context, ArrayList<boolean[]> stati, ArrayList<String> alunni) {
        Resources risorse = context.getResources();
        String[] messaggi = risorse.getStringArray(R.array.notifiche);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int num_messaggi = 0;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(risorse.getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(Color.BLACK)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(risorse.getColor(R.color.colorPrimary), 250, 4000);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        int lastPos = 0;
        String lastAlunno = "";

        for (int i = 0; i < stati.size(); i++) {
            boolean[] stat = stati.get(i);
            String alunno = alunni.get(i);

            for (int j = 0; j < stat.length; j++) {
                if (stat[j]) {
                    style.addLine(messaggi[j] + " " + alunno);
                    lastPos = j;
                    lastAlunno = alunno;
                    num_messaggi++;
                }
            }
        }

        if (num_messaggi == 1) {
            builder.setContentText(messaggi[lastPos] + " " + lastAlunno);
        } else if (num_messaggi == 0) {
            return;
        } else {
            builder.setContentText(num_messaggi + " " + risorse.getString(R.string.nuove_notifiche));
            builder.setStyle(style);
            builder.setNumber(num_messaggi);
        }
        manager.notify(0, builder.build());
    }
}
