package net.lampschool.Utils;

public class Comunicazione {
    private String oggetto;
    private String testo;
    private String data;

    public Comunicazione(String oggetto, String testo, String data) {
        this.oggetto = oggetto;
        this.testo = testo;
        this.data = data;
    }

    public Comunicazione() {
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
