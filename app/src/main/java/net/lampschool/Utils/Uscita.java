package net.lampschool.Utils;

public class Uscita {
    private String data;
    private String ora;

    public Uscita(String data, String ora) {
        this.data = data;
        this.ora = ora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra_Usc() {
        return ora;
    }

    public void setOra_Usc(String ora) {
        this.ora = ora;
    }
}
