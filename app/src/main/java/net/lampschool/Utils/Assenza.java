package net.lampschool.Utils;

public class Assenza {
    private String data;
    private boolean giustificata;

    public Assenza(String data, boolean giustificata) {
        this.data = data;
        this.giustificata = giustificata;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isGiustificata() {
        return giustificata;
    }

    public void setGiustificata(boolean giustificata) {
        this.giustificata = giustificata;
    }
}
