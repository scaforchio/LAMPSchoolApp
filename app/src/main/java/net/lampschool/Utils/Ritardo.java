package net.lampschool.Utils;

public class Ritardo {
    private String data;
    private boolean giustificato;
    private String ora_ent;

    public Ritardo(String data, boolean giustificato, String ora_ent) {
        this.data = data;
        this.giustificato = giustificato;
        this.ora_ent = ora_ent;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isGiustificato() {
        return giustificato;
    }

    public void setGiustificato(boolean giustificato) {
        this.giustificato = giustificato;
    }

    public String getOra_ent() {
        return ora_ent;
    }

    public void setOra_ent(String ora_ent) {
        this.ora_ent = ora_ent;
    }
}
