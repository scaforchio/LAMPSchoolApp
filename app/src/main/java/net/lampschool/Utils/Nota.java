package net.lampschool.Utils;

public class Nota {
    private String cognome_doc;
    private String nome_doc;
    private String nota;
    private String data;
    private String tipo;

    public Nota(String cognome, String nome, String nota, String data, String tipo) {
        this.cognome_doc = cognome;
        this.nome_doc = nome;
        this.nota = nota;
        this.data = data;
        this.tipo = tipo;
    }

    public String getCognome() {
        return cognome_doc;
    }

    public void setCognome(String cognome) {
        this.cognome_doc = cognome;
    }

    public String getNome() {
        return nome_doc;
    }

    public void setNome(String nome) {
        this.nome_doc = nome;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
