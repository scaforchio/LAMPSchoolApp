package net.lampschool.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import net.lampschool.R;
import net.lampschool.Receivers.AlarmReceiver;
import net.lampschool.Tasks.CheckConnectionTask;
import net.lampschool.Tasks.LoginTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Utils {
    public static String[] getInformazioniAlunno(String json) {
        String[] info = new String[4];
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            JSONObject dati = new JSONObject(json);

            info[0] = dati.getString("nome");
            info[0] = info[0].toLowerCase();
            info[0] = info[0].substring(0, 1).toUpperCase() + info[0].substring(1);
            info[1] = dati.getString("cognome");
            info[1] = info[1].toLowerCase();
            info[1] = info[1].substring(0, 1).toUpperCase() + info[1].substring(1);
            info[2] = dati.getString("datanascita");
            info[3] = dati.getString("classe");
            Date date = sdf1.parse(info[2]);
            info[2] = sdf.format(date);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return info;
    }

    public static ArrayList<Lezione> getLezioniFromJson(String json) {
        ArrayList<Lezione> lezioni = new ArrayList<>();

        JSONArray materie, argomenti, date, attivita;

        try {
            JSONObject dati = new JSONObject(json);
            materie = dati.getJSONArray("matelez");
            argomenti = dati.getJSONArray("argolez");
            attivita = dati.getJSONArray("attilez");
            date = dati.getJSONArray("datelez");

            for (int i = 0; i < materie.length(); i++) {
                Lezione lezione = new Lezione();
                lezione.setArgomento(argomenti.getString(i));
                lezione.setData(date.getString(i));
                lezione.setMateria(materie.getString(i));
                lezione.setAttivita(attivita.getString(i));
                lezioni.add(lezione);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lezioni;
    }

    public static ArrayList<Media> getMedieFromJson(Context context, String json) {
        String[] materie = getMaterieFromJson(json);
        ArrayList<Voto> voti = getVotiFromJson(context, json);
        ArrayList<Media> medie = new ArrayList<>();
        Date fine = getFineQuadrimestreFromJson(json);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        for (int i = 0; i < materie.length; i++) {
            Media media = new Media();
            media.setMateria(materie[i]);
            media.setQuadrimestre(1);

            Media media1 = new Media();
            media1.setMateria(materie[i]);
            media1.setQuadrimestre(2);

            float somma = 0;
            float somma1 = 0;
            int n = 0;
            int n1 = 0;

            for (int j = 0; j < voti.size(); j++) {
                Voto v = voti.get(j);

                try {
                    if (v.getMateria().equals(materie[i])) {
                        Date data = df.parse(v.getData());

                        if (data.compareTo(fine) <= 0) {
                            if (v.getVoto() != 99) {
                                somma += v.getVoto();
                                n++;
                            }
                        } else {
                            if (v.getVoto() != 99) {
                                somma1 += v.getVoto();
                                n1++;
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (n > 0) media.setMedia(somma / n);
            else media.setMedia(0);

            if (n1 > 0) media1.setMedia(somma1 / n1);
            else media1.setMedia(0);

            medie.add(media);
            medie.add(media1);
        }

        return medie;
    }

    public static ArrayList<Voto> getVotiFromJson(Context context, String json) {
        ArrayList<Voto> voti = new ArrayList<>();
        Resources risorse = context.getResources();
        JSONArray tipo, data, voto, giudizio, denominazione;

        try {
            JSONObject dati = new JSONObject(json);
            tipo = dati.getJSONArray("tipo");
            data = dati.getJSONArray("date");
            voto = dati.getJSONArray("voto");
            giudizio = dati.getJSONArray("giudizio");
            denominazione = dati.getJSONArray("denominazione");

            String[] tipi = risorse.getStringArray(R.array.tipi_voto);

            for (int i = 0; i < voto.length(); i++) {
                String t = tipo.getString(i);

                switch (t) {
                    case "O":
                        t = tipi[0];
                        break;
                    case "P":
                        t = tipi[1];
                        break;
                    case "S":
                        t = tipi[2];
                        break;
                }

                float voto1 = Float.valueOf(voto.getString(i));

                Voto v = new Voto(t, data.getString(i),
                        voto1, giudizio.getString(i), denominazione.getString(i));
                voti.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return voti;
    }

    public static ArrayList<Assenza> getAssenzeFromJson(String json) {
        ArrayList<Assenza> assenze = new ArrayList<>();

        JSONArray date, giustifiche;

        try {
            JSONObject dati = new JSONObject(json);
            date = dati.getJSONArray("dateass");
            giustifiche = dati.getJSONArray("giustass");

            boolean giustificata;
            for (int i = 0; i < date.length(); i++) {
                giustificata = !giustifiche.getString(i).equals("0");

                Assenza assenza = new Assenza(date.getString(i), giustificata);
                assenze.add(assenza);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assenze;
    }

    public static ArrayList<Ritardo> getRitardiFromJson(String json) {
        ArrayList<Ritardo> ritardi = new ArrayList<>();

        JSONArray date, giustifiche, n_ore, ora_ent;

        try {
            JSONObject dati = new JSONObject(json);
            date = dati.getJSONArray("daterit");
            giustifiche = dati.getJSONArray("giustr");
            n_ore = dati.getJSONArray("numore");
            ora_ent = dati.getJSONArray("oraent");

            boolean giustificata;
            for (int i = 0; i < date.length(); i++) {
                giustificata = !giustifiche.getString(i).equals("0");

                Ritardo ritardo = new Ritardo(date.getString(i), giustificata, ora_ent.getString(i));
                ritardi.add(ritardo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ritardi;
    }

    public static ArrayList<Uscita> getUsciteFromJson(String json) {
        ArrayList<Uscita> uscite = new ArrayList<>();

        JSONArray date, ora_usc;

        try {
            JSONObject dati = new JSONObject(json);
            date = dati.getJSONArray("dateusc");
            ora_usc = dati.getJSONArray("oraus");

            for (int i = 0; i < date.length(); i++) {
                Uscita uscita = new Uscita(date.getString(i), ora_usc.getString(i));
                uscite.add(uscita);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return uscite;
    }

    public static ArrayList<Nota> getNoteFromJson(Context context, String json) {
        ArrayList<Nota> note = new ArrayList<>();
        Resources risorse = context.getResources();
        JSONArray date, cognomi, nomi, descrizioni;
        String[] tipi = risorse.getStringArray(R.array.tipi_note);
        String tipo = tipi[0];

        try {
            JSONObject dati = new JSONObject(json);
            date = dati.getJSONArray("data");
            cognomi = dati.getJSONArray("cognomedoc");
            nomi = dati.getJSONArray("nomedoc");
            descrizioni = dati.getJSONArray("notealunno");

            for (int i = 0; i < descrizioni.length(); i++) {
                Nota nota = new Nota(cognomi.getString(i), nomi.getString(i), descrizioni.getString(i), date.getString(i), tipo);
                note.add(nota);
            }

            date = dati.getJSONArray("datac");
            cognomi = dati.getJSONArray("cognomedc");
            nomi = dati.getJSONArray("nomedc");
            descrizioni = dati.getJSONArray("noteclasse");
            tipo = tipi[1];

            for (int i = 0; i < descrizioni.length(); i++) {
                Nota nota = new Nota(cognomi.getString(i), nomi.getString(i), descrizioni.getString(i), date.getString(i), tipo);
                note.add(nota);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return note;
    }

    public static String[] getMaterieLezioniFromJson(String json) {
        String[] mat = new String[0];
        try {
            JSONObject dati = new JSONObject(json);
            JSONArray materie = dati.getJSONArray("matelez");
            String a = materie.toString().replace("[", "").replace("]", "");
            mat = a.split("\",\"");

            for (int i = 0; i < mat.length; i++) {
                mat[i] = mat[i].replace("\"", "");
            }

            mat = new HashSet<>(Arrays.asList(mat)).toArray(new String[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mat;
    }

    public static String[] getMaterieFromJson(String json) {
        String[] mat = new String[0];
        try {
            JSONObject dati = new JSONObject(json);
            JSONArray materie = dati.getJSONArray("denominazione");
            String a = materie.toString().replace("[", "").replace("]", "");
            mat = a.split("\",\"");

            for (int i = 0; i < mat.length; i++) {
                mat[i] = mat[i].replace("\"", "");
            }

            mat = new HashSet<>(Arrays.asList(mat)).toArray(new String[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mat;
    }

    public static ArrayList<Comunicazione> getComunicazioniFromJson(String json) {
        ArrayList<Comunicazione> comunicazioni = new ArrayList<>();

        JSONArray oggetti, testi, date;

        try {
            JSONObject dati = new JSONObject(json);
            oggetti = dati.getJSONArray("oggcom");
            testi = dati.getJSONArray("testicom");
            date = dati.getJSONArray("datecom");

            for (int i = 0; i < oggetti.length(); i++) {
                Comunicazione com = new Comunicazione(oggetti.getString(i), testi.getString(i), date.getString(i));
                comunicazioni.add(com);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return comunicazioni;
    }

    public static int getNumVotiFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numerovoti");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

    public static int getNumAssenzeFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numeroassenze");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

    public static int getNumRitardiFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numeroritardi");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

    public static int getNumUsciteFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numerouscite");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

    public static int getNumNoteFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numeronote");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

    public static int getNumComunicazioniFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            num = dati.getInt("numerocomunicazioni");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    }

 /*   public static int getNumLezioniFromJson(String json) {
        int num = 0;
        try {
            JSONObject dati = new JSONObject(json);
            JSONArray array = dati.getJSONArray("datelez");
            num = array.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return num;
    } */

    public static ArrayList<SezioneNote> sezionaNote(Context context, ArrayList<Nota> note) {
        ArrayList<SezioneNote> sezioni = new ArrayList<>();
        Resources risorse = context.getResources();
        int index = 0;
        String[] tipi = risorse.getStringArray(R.array.tipi);


        for (String tipo : tipi) {
            int num_note = 0;
            ArrayList<Nota> note1 = new ArrayList<>();
            SezioneNote sezioneNote = new SezioneNote();
            sezioneNote.setIndex(index);
            sezioneNote.setTitolo(tipo);

            for (int j = 0; j < note.size(); j++) {
                Nota nota = note.get(j);

                if (nota.getTipo().equals(tipo)) {
                    note1.add(nota);
                    num_note++;
                    index++;
                }
            }

            if (num_note == 0) {
                note1.add(new Nota("", "", risorse.getString(R.string.no_note), "", ""));
                index++;
            }

            sezioneNote.setNote(note1);
            sezioni.add(sezioneNote);
        }

        return sezioni;
    }

    public static ArrayList<SezioneMedie> sezionaMedie(Context context, ArrayList<Media> medie) {
        Resources risorse = context.getResources();
        String[] quadrimestri = risorse.getStringArray(R.array.quadrimestri);

        ArrayList<SezioneMedie> sezioni = new ArrayList<>();
        int index = 0;

        for (int i = 0; i < quadrimestri.length; i++) {
            SezioneMedie sezioneMedie = new SezioneMedie();
            sezioneMedie.setTitolo(quadrimestri[i]);
            sezioneMedie.setIndex(index);
            ArrayList<Media> med = new ArrayList<>();
            for (int j = 0; j < medie.size(); j++) {
                Media m = medie.get(j);

                if (m.getQuadrimestre() == (i + 1)) {
                    med.add(m);
                    index++;
                }
            }

            sezioneMedie.setMedie(med);
            sezioni.add(sezioneMedie);
        }

        return sezioni;
    }

    public static String votoToString(float voto) {
        if (voto == 99.00) {
            return "";
        } else {
            int a = (int) voto;
            double decimal = (10 * voto - 10 * a) / 10;

            if (decimal == 0.25) {
                return a + "+";
            } else if (decimal == 0.50) {
                return a + "\u00BD";
            } else if (decimal == 0.75) {
                return (a + 1) + "-";
            }

            return "" + a;
        }
    }

    public static ArrayList<SezioneVoti> filtraVoti(ArrayList<Voto> voti, int metodo, String inizio, String fine, String materia,
                                                    String[] materie) {
        ArrayList<SezioneVoti> sezioni = new ArrayList<>();
        ArrayList<Voto> vot;
        SezioneVoti sezione;

        switch (metodo) {
            case 0:
                vot = new ArrayList<>();
                sezione = new SezioneVoti();
                sezione.setTitolo(materia);
                sezione.setIndex(0);

                for (int i = 0; i < voti.size(); i++) {
                    Voto v = voti.get(i);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                    try {
                        Date data_inizio = sdf.parse(inizio);
                        Date data_fine = sdf.parse(fine);
                        Date data = sdf.parse(v.getData());

                        if ((data.compareTo(data_inizio) >= 0 && data.compareTo(data_fine) <= 0)) {
                            if (materia.toLowerCase().contains(v.getMateria().toLowerCase())) {
                                vot.add(v);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                sezione.setVoti(vot);
                sezioni.add(sezione);
                return sezioni;

            case 1:
                int index = 0;

                for (int j = 0; j < materie.length; j++) {
                    vot = new ArrayList<>();
                    String mat = materie[j];
                    sezione = new SezioneVoti();
                    sezione.setTitolo(mat);
                    sezione.setIndex(index);

                    for (int i = 0; i < voti.size(); i++) {
                        Voto v = voti.get(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                        try {
                            Date data_inizio = sdf.parse(inizio);
                            Date data_fine = sdf.parse(fine);
                            Date data = sdf.parse(v.getData());

                            if ((data.compareTo(data_inizio) >= 0 && data.compareTo(data_fine) <= 0)) {
                                if (mat.toLowerCase().contains(v.getMateria().toLowerCase())) {
                                    vot.add(v);
                                    index++;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    sezione.setVoti(vot);
                    sezioni.add(sezione);
                }
                return sezioni;
            case 2:
                vot = new ArrayList<>();
                sezione = new SezioneVoti();
                sezione.setTitolo(materia);
                sezione.setIndex(0);

                for (int i = 0; i < voti.size(); i++) {
                    Voto v = voti.get(i);
                    if (materia.toLowerCase().contains(v.getMateria().toLowerCase())) {
                        vot.add(v);
                    }
                }
                sezione.setVoti(vot);
                sezioni.add(sezione);
                return sezioni;
        }

        return sezioni;
    }

    public static ArrayList<SezioneLezioni> filtraLezioni(ArrayList<Lezione> lezioni, int metodo, String inizio, String fine, String materia,
                                                          String[] materie) {
        ArrayList<SezioneLezioni> sezioni = new ArrayList<>();
        ArrayList<Lezione> lez;
        SezioneLezioni sezione;

        switch (metodo) {
            case 0:
                lez = new ArrayList<>();
                sezione = new SezioneLezioni();
                sezione.setTitolo(materia);
                sezione.setIndex(0);

                for (int i = 0; i < lezioni.size(); i++) {
                    Lezione l = lezioni.get(i);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                    try {
                        Date data_inizio = sdf.parse(inizio);
                        Date data_fine = sdf.parse(fine);
                        Date data = sdf.parse(l.getData());

                        if ((data.compareTo(data_inizio) >= 0 && data.compareTo(data_fine) <= 0)) {
                            if (materia.toLowerCase().contains(l.getMateria().toLowerCase())) {
                                lez.add(l);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                sezione.setLezioni(lez);
                sezioni.add(sezione);
                return sezioni;

            case 1:
                int index = 0;

                for (int j = 0; j < materie.length; j++) {
                    lez = new ArrayList<>();
                    String mat = materie[j];
                    sezione = new SezioneLezioni();
                    sezione.setTitolo(mat);
                    sezione.setIndex(index);

                    for (int i = 0; i < lezioni.size(); i++) {
                        Lezione l = lezioni.get(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
                        try {
                            Date data_inizio = sdf.parse(inizio);
                            Date data_fine = sdf.parse(fine);
                            Date data = sdf.parse(l.getData());

                            if ((data.compareTo(data_inizio) >= 0 && data.compareTo(data_fine) <= 0)) {
                                if (mat.toLowerCase().contains(l.getMateria().toLowerCase())) {
                                    lez.add(l);
                                    index++;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    sezione.setLezioni(lez);
                    sezioni.add(sezione);
                }
                return sezioni;
            case 2:
                lez = new ArrayList<>();
                sezione = new SezioneLezioni();
                sezione.setTitolo(materia);
                sezione.setIndex(0);

                for (int i = 0; i < lezioni.size(); i++) {
                    Lezione l = lezioni.get(i);
                    if (materia.toLowerCase().contains(l.getMateria().toLowerCase())) {
                        lez.add(l);
                    }
                }
                sezione.setLezioni(lez);
                sezioni.add(sezione);
                return sezioni;
        }

        return sezioni;
    }

    public static String startLoginTask(Context context, Account account) {
        String s = "";
        LoginTask task = new LoginTask();
        try {
            s = task.execute(account).get(15, TimeUnit.SECONDS);
            Log.d("RESULT: ", s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            return "tempo_esaurito";
        }

        return s;
    }

    public static ArrayList<Media> ordinaListaMedie(ArrayList<Media> medie, String ordine) {
        boolean scambio = true;
        while (scambio) {
            scambio = false;

            for (int i = 0; i < medie.size() - 1; i++) {
                Media m = medie.get(i);
                Media m1 = medie.get(i + 1);

                switch (ordine) {
                    case "materia_cresc":
                        if (m.getMateria().toLowerCase().compareTo(m1.getMateria().toLowerCase()) > 0) {
                            medie.set(i, m1);
                            medie.set(i + 1, m);
                            scambio = true;
                        }
                        break;

                    case "materia_decr":
                        if (m.getMateria().toLowerCase().compareTo(m1.getMateria().toLowerCase()) < 0) {
                            medie.set(i, m1);
                            medie.set(i + 1, m);
                            scambio = true;
                        }
                        break;

                    case "media_cresc":
                        if (m.getMedia() > m1.getMedia()) {
                            medie.set(i, m1);
                            medie.set(i + 1, m);
                            scambio = true;
                        }
                        break;

                    case "media_decr":
                        if (m.getMedia() < m.getMedia()) {
                            medie.set(i, m1);
                            medie.set(i + 1, m);
                            scambio = true;
                        }
                        break;
                }
            }
        }

        return medie;
    }

    private static ArrayList<Voto> ordinaVoti(ArrayList<Voto> voti, String ordine) {
        boolean scambio = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        try {
            Date data, data1;

            while (scambio) {
                scambio = false;
                for (int i = 0; i < voti.size() - 1; i++) {
                    Voto v = voti.get(i);
                    Voto v1 = voti.get(i + 1);

                    data = sdf.parse(v.getData());
                    data1 = sdf.parse(v1.getData());

                    switch (ordine) {
                        case "voto_cresc":
                            if (v.getVoto() > v1.getVoto()) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;
                        case "voto_decr":
                            if (v.getVoto() < v1.getVoto()) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;

                        case "tipo_cresc":
                            if (v.getTipo().toLowerCase().compareTo(v1.getTipo().toLowerCase()) > 0) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;
                        case "tipo_decr":
                            if (v.getTipo().toLowerCase().compareTo(v1.getTipo().toLowerCase()) < 0) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;

                        case "data_cresc":
                            if (data.compareTo(data1) > 0) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;
                        case "data_decr":
                            if (data.compareTo(data1) < 0) {
                                voti.set(i, v1);
                                voti.set(i + 1, v);
                                scambio = true;
                            }
                            break;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return voti;
    }

    private static ArrayList<Lezione> ordinaLezioni(ArrayList<Lezione> lezioni, String ordine) {
        boolean scambio = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        while (scambio) {
            scambio = false;
            for (int i = 0; i < lezioni.size() - 1; i++) {
                Lezione l = lezioni.get(i);
                Lezione l1 = lezioni.get(i + 1);

                try {
                    Date data = sdf.parse(l.getData());
                    Date data1 = sdf.parse(l1.getData());

                    switch (ordine) {
                        case "data_cresc":
                            if (data.compareTo(data1) > 0) {
                                lezioni.set(i, l1);
                                lezioni.set(i + 1, l);
                                scambio = true;
                            }
                            break;
                        case "data_decr":
                            if (data.compareTo(data1) < 0) {
                                lezioni.set(i, l1);
                                lezioni.set(i + 1, l);
                                scambio = true;
                            }
                            break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return lezioni;
    }

    public static Date getFineQuadrimestreFromJson(String dati) {
        try {
            JSONObject jsonObject = new JSONObject(dati);
            String data = jsonObject.getString("fineprimo");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
            Date startDate;
            startDate = df.parse(data);
            String newDateString = df1.format(startDate);
            return df1.parse(newDateString);
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static ArrayList<SezioneVoti> ordinaLista(Context context, ArrayList<Voto> voti, String ordine, String[] materie, String dati) {
        Resources risorse = context.getResources();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        String[] quadrimestri = risorse.getStringArray(R.array.quadrimestri);

        if (ordine.equals("quadr")) {
            ArrayList<SezioneVoti> sezioni = new ArrayList<>();
            int index = 0;

            Map<String, ArrayList<Voto>> quadrimestre1 = new HashMap<String, ArrayList<Voto>>();
            Map<String, ArrayList<Voto>> quadrimestre2 = new HashMap<String, ArrayList<Voto>>();

            for (int i = 0; i < materie.length; i++) {
                quadrimestre1.put(materie[i], new ArrayList<Voto>());
                quadrimestre2.put(materie[i], new ArrayList<Voto>());
            }

            for (int i = 0; i < voti.size(); i++) {
                Voto v = voti.get(i);
                String materia = v.getMateria();

                Date data_fine = getFineQuadrimestreFromJson(dati);
                Date data = null;
                try {
                    data = sdf.parse(v.getData());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ArrayList<Voto> app;

                if (data.compareTo(data_fine) <= 0) {
                    if ((app = quadrimestre1.get(materia)) == null) {
                        app = new ArrayList<>();
                    }
                    app.add(v);
                    quadrimestre1.put(materia, app);
                } else if (data.compareTo(data_fine) > 0) {
                    if ((app = quadrimestre2.get(materia)) == null) {
                        app = new ArrayList<>();
                    }
                    app.add(v);
                    quadrimestre2.put(materia, app);
                }
            }

            SezioneVoti sezioneVoti = new SezioneVoti();
            sezioneVoti.setTitolo(quadrimestri[0]);
            sezioneVoti.setIndex(index);
            sezioneVoti.setVoti(new ArrayList<Voto>());
            sezioni.add(sezioneVoti);

            for (String key : quadrimestre1.keySet()) {
                ArrayList<Voto> app = quadrimestre1.get(key);
                if (app.size() > 0) {
                    sezioneVoti = new SezioneVoti();
                    sezioneVoti.setTitolo(key);
                    sezioneVoti.setIndex(index);
                    sezioneVoti.setVoti(app);
                    sezioni.add(sezioneVoti);
                    index += app.size();
                }
            }

            sezioneVoti = new SezioneVoti();
            sezioneVoti.setTitolo(quadrimestri[1]);
            sezioneVoti.setIndex(index);
            sezioneVoti.setVoti(new ArrayList<Voto>());
            sezioni.add(sezioneVoti);

            for (String key : quadrimestre2.keySet()) {
                ArrayList<Voto> app = quadrimestre2.get(key);
                if (app.size() > 0) {
                    sezioneVoti = new SezioneVoti();
                    sezioneVoti.setTitolo(key);
                    sezioneVoti.setIndex(index);
                    sezioneVoti.setVoti(app);
                    sezioni.add(sezioneVoti);
                    index += app.size();
                }
            }

            return sezioni;
        } else {
            ArrayList<SezioneVoti> sezioni = new ArrayList<>();
            Map<String, ArrayList<Voto>> map = new HashMap<String, ArrayList<Voto>>();

            for (int i = 0; i < materie.length; i++) {
                map.put(materie[i], new ArrayList<Voto>());
            }

            for (int i = 0; i < voti.size(); i++) {
                Voto v = voti.get(i);
                String materia = v.getMateria();

                ArrayList<Voto> app;
                if ((app = map.get(materia)) == null) {
                    app = new ArrayList<>();
                }
                app.add(v);
                map.put(materia, app);
            }

            int index = 0;
            ArrayList<String> ordinate;
            SezioneVoti sezione;

            switch (ordine) {
                case "materia_cresc":
                    ordinate = bubblesort(map.keySet(), 1);
                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneVoti();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Voto> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setVoti(app);
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;

                case "materia_decr":
                    ordinate = bubblesort(map.keySet(), 0);

                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneVoti();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Voto> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setVoti(app);
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;

                case "voto_cresc":
                case "voto_decr":
                case "tipo_cresc":
                case "tipo_decr":
                case "data_cresc":
                case "data_decr":
                    ordinate = bubblesort(map.keySet(), 1);
                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneVoti();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Voto> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setVoti(ordinaVoti(app, ordine));
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;
            }
        }

        return new ArrayList<>();
    }

    private static ArrayList<String> bubblesort(Set<String> keys, int ordine) {
        //ordine: 0=decrescente, 1=crescente
        ArrayList<String> keys1 = new ArrayList<>();
        keys1.addAll(keys);
        boolean scambio = true;

        while (scambio) {
            scambio = false;
            for (int i = 0; i < keys1.size() - 1; i++) {
                String key = keys1.get(i);
                String key1 = keys1.get(i + 1);

                if (ordine == 1) {
                    if (key.compareTo(key1) > 0) {
                        String app = keys1.get(i + 1);
                        keys1.set(i + 1, keys1.get(i));
                        keys1.set(i, app);
                        scambio = true;
                    }
                } else {
                    if (key.compareTo(key1) < 0) {
                        String app = keys1.get(i + 1);
                        keys1.set(i + 1, keys1.get(i));
                        keys1.set(i, app);
                        scambio = true;
                    }
                }
            }
        }

        return keys1;
    }

    public static ArrayList<SezioneLezioni> ordinaListaLezioni(Context context, ArrayList<Lezione> lezioni, String ordine, String[] materie, String dati) {
        Resources risorse = context.getResources();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        String[] quadrimestri = risorse.getStringArray(R.array.quadrimestri);

        if (ordine.equals("quadr")) {
            ArrayList<SezioneLezioni> sezioni = new ArrayList<>();
            int index = 0;

            Map<String, ArrayList<Lezione>> quadrimestre1 = new HashMap<String, ArrayList<Lezione>>();
            Map<String, ArrayList<Lezione>> quadrimestre2 = new HashMap<String, ArrayList<Lezione>>();

            for (int i = 0; i < materie.length; i++) {
                quadrimestre1.put(materie[i], new ArrayList<Lezione>());
                quadrimestre2.put(materie[i], new ArrayList<Lezione>());
            }

            for (int i = 0; i < lezioni.size(); i++) {
                Lezione l = lezioni.get(i);
                String materia = l.getMateria();

                Date data_fine = getFineQuadrimestreFromJson(dati);
                Date data = null;
                try {
                    data = sdf.parse(l.getData());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (data.compareTo(data_fine) <= 0) {
                    ArrayList<Lezione> app = quadrimestre1.get(materia);
                    app.add(l);
                    quadrimestre1.put(materia, app);
                } else if (data.compareTo(data_fine) > 0) {
                    ArrayList<Lezione> app = quadrimestre2.get(materia);
                    app.add(l);
                    quadrimestre2.put(materia, app);
                }
            }

            SezioneLezioni sezioneLezioni = new SezioneLezioni();
            sezioneLezioni.setTitolo(quadrimestri[0]);
            sezioneLezioni.setIndex(index);
            sezioneLezioni.setLezioni(new ArrayList<Lezione>());
            sezioni.add(sezioneLezioni);

            for (String key : quadrimestre1.keySet()) {
                ArrayList<Lezione> app = quadrimestre1.get(key);
                if (app.size() > 0) {
                    sezioneLezioni = new SezioneLezioni();
                    sezioneLezioni.setTitolo(key);
                    sezioneLezioni.setIndex(index);
                    sezioneLezioni.setLezioni(app);
                    sezioni.add(sezioneLezioni);
                    index += app.size();
                }
            }

            sezioneLezioni = new SezioneLezioni();
            sezioneLezioni.setTitolo(quadrimestri[1]);
            sezioneLezioni.setIndex(index);
            sezioneLezioni.setLezioni(new ArrayList<Lezione>());
            sezioni.add(sezioneLezioni);

            for (String key : quadrimestre2.keySet()) {
                ArrayList<Lezione> app = quadrimestre2.get(key);
                if (app.size() > 0) {
                    sezioneLezioni = new SezioneLezioni();
                    sezioneLezioni.setTitolo(key);
                    sezioneLezioni.setIndex(index);
                    sezioneLezioni.setLezioni(app);
                    sezioni.add(sezioneLezioni);
                    index += app.size();
                }
            }

            return sezioni;
        } else {
            ArrayList<SezioneLezioni> sezioni = new ArrayList<>();
            Map<String, ArrayList<Lezione>> map = new HashMap<String, ArrayList<Lezione>>();

            for (int i = 0; i < materie.length; i++) {
                map.put(materie[i], new ArrayList<Lezione>());
            }

            for (int i = 0; i < lezioni.size(); i++) {
                Lezione l = lezioni.get(i);
                String materia = l.getMateria();

                ArrayList<Lezione> app;
                if ((app = map.get(materia)) == null) {
                    app = new ArrayList<>();
                }
                app.add(l);
                map.put(materia, app);
            }

            int index = 0;
            ArrayList<String> ordinate;
            SezioneLezioni sezione;

            switch (ordine) {
                case "materia_cresc":
                    ordinate = bubblesort(map.keySet(), 1);
                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneLezioni();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Lezione> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setLezioni(app);
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;

                case "materia_decr":
                    ordinate = bubblesort(map.keySet(), 0);

                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneLezioni();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Lezione> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setLezioni(app);
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;

                case "voto_cresc":
                case "voto_decr":
                case "tipo_cresc":
                case "tipo_decr":
                case "data_cresc":
                case "data_decr":
                    ordinate = bubblesort(map.keySet(), 1);
                    for (int i = 0; i < ordinate.size(); i++) {
                        String materia = ordinate.get(i);
                        sezione = new SezioneLezioni();
                        sezione.setTitolo(materia);
                        sezione.setIndex(index);
                        ArrayList<Lezione> app = map.get(materia);
                        if (app.size() > 0) {
                            sezione.setLezioni(ordinaLezioni(app, ordine));
                            sezioni.add(sezione);
                            index += app.size();
                        }
                    }

                    return sezioni;
            }
        }

        return new ArrayList<>();
    }

    public static boolean importFileFromUri(Context context, Uri uri) {
        ArrayList<Account> accounts = readListaAccount(context);

        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            ObjectInputStream ois = new ObjectInputStream(is);

            Account account;

            while ((account = (Account) ois.readObject()) != null) {
                accounts.add(account);
            }

            ois.close();
            is.close();

            salvaListaAccount(context, accounts);
            return true;
        } catch (FileNotFoundException e) {
            Log.d("INFO", "Nessun file account presente");
            return false;
        } catch (EOFException e) {
            return true;
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return false;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportAccountsToUri(Context context) {
        ArrayList<Account> accounts = readListaAccount(context);
        String path = Environment.getExternalStorageDirectory() + "/accounts.lamp";
        File file = new File(path);

        if (file.exists()) file.delete();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (int i = 0; i < accounts.size(); i++) {
                oos.writeObject(accounts.get(i));
            }

            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isConnectedToInternet() {
        boolean connected;
        CheckConnectionTask task = new CheckConnectionTask();
        try {
            connected = task.execute().get(10, TimeUnit.SECONDS);
            return connected;
        } catch (Exception e) {
            return false;
        }
    }

    public static String hashMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte element : array) {
                sb.append(Integer.toHexString((element & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void salvaAccount(Context context, Account account) {
        ArrayList<Account> accounts = readListaAccount(context);
        accounts.add(account);
        context.deleteFile("account.obj");

        try {
            FileOutputStream fos = context.openFileOutput("account.obj", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (int i = 0; i < accounts.size(); i++) {
                oos.writeObject(accounts.get(i));
            }

            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Account> readListaAccount(Context context) {
        ArrayList<Account> accounts = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput("account.obj");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Account account;

            while ((account = (Account) ois.readObject()) != null) {
                accounts.add(account);
            }

            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.d("INFO", "Nessun file account presente");
            return accounts;
        } catch (OptionalDataException e) {
            e.printStackTrace();
            return accounts;
        } catch (EOFException e) {
            Log.d("INFO", "RAGGIUNTA FINE FILE");
            return accounts;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return accounts;
        } catch (IOException e) {
            e.printStackTrace();
            return accounts;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public static void eliminaAccount(Context context, Account account) {
        ArrayList<Account> accounts = readListaAccount(context);
        accounts.remove(account);
        salvaListaAccount(context, accounts);
    }

    public static void salvaListaAccount(Context context, ArrayList<Account> accounts) {
        context.deleteFile("account.obj");
        try {
            FileOutputStream fos = context.openFileOutput("account.obj", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            for (int i = 0; i < accounts.size(); i++) {
                oos.writeObject(accounts.get(i));
            }

            oos.close();
            fos.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startAlarmManager(Context context) {
        final Context context1 = context;
        Thread t = new Thread() {
            @Override
            public void run() {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context1);

                if (Utils.isConnectedToInternet()) {
                    //int intervallo = preferences.getInt("intervallo", 60);
                    int intervallo=15;
                    Intent intent1 = new Intent(context1, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context1, AlarmReceiver.REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarm = (AlarmManager) context1.getSystemService(Context.ALARM_SERVICE);
                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, intervallo * 60 * 1000, pendingIntent);
                    Log.d("DEBUG: ", "Servizio in ascolto");
                    return;
                }
                stopAlarmManager(context1);
            }
        };
        t.start();
    }

    public static void stopAlarmManager(Context context) {
        Intent intent1 = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Log.d("DEBUG:", "Servizio stoppato");
    }
}