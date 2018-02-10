package net.lampschool.Utils;

public class Lezione {
    private String materia;
    private String argomento;
    private String attivita;
    private String data;

    public Lezione() {
    }

    public Lezione(String materia, String argomento, String attivita, String data) {
        this.materia = materia;
        this.argomento = argomento;
        this.attivita = attivita;
        this.data = data;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAttivita() {
        return attivita;
    }

    public void setAttivita(String attivita) {
        this.attivita = attivita;
    }
}
