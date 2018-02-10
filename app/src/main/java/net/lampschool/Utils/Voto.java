package net.lampschool.Utils;

public class Voto {
    private String tipo;
    private String data;
    private float voto;
    private String giudizio;
    private String materia;

    public Voto(String tipo, String data, float voto, String giudizio, String materia) {
        this.tipo = tipo;
        this.data = data;
        this.voto = voto;
        this.giudizio = giudizio;
        this.materia = materia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getGiudizio() {
        return giudizio;
    }

    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    public String getMateria() {
        return materia;
    }

    public void setDenominazione(String denominazione) {
        this.materia = denominazione;
    }

    public float getVoto() {
        return voto;
    }

    public void setVoto(float voto) {
        this.voto = voto;
    }
}
