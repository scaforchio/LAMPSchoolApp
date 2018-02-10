package net.lampschool.Utils;

import java.io.Serializable;

public class Account implements Serializable {
    String nome;
    String cognome;
    String username;
    String password;
    String encrypted;
    String suffisso;
    String link;
    int num_voti;
    int num_assenze;
    int num_ritardi;
    int num_uscite;
    int num_note;
    int num_comunicazioni;
    int num_lezioni;

    public Account(String nome, String cognome, String username, String password, String suffisso, String link) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.encrypted = Utils.hashMD5(password);
        this.suffisso = suffisso;
        this.link = link;
        num_voti = 0;
        num_assenze = 0;
        num_ritardi = 0;
        num_uscite = 0;
        num_note = 0;
        num_comunicazioni = 0;
        num_lezioni = 0;
    }

    public Account() {
        encrypted = "";
        num_voti = 0;
        num_assenze = 0;
        num_ritardi = 0;
        num_uscite = 0;
        num_note = 0;
        num_comunicazioni = 0;
        num_lezioni = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.encrypted = Utils.hashMD5(password);
    }

    public String getEncryptedPassword() {
        return encrypted;
    }

    public String getSuffisso() {
        return suffisso;
    }

    public void setSuffisso(String suffisso) {
        this.suffisso = suffisso;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getNumAssenze() {
        return num_assenze;
    }

    public void setNumAssenze(int num) {
        this.num_assenze = num;
    }

    public int getNumVoti() {
        return num_voti;
    }

    public void setNumVoti(int num) {
        this.num_voti = num;
    }

    public int getNumRitardi() {
        return num_ritardi;
    }

    public void setNumRitardi(int num) {
        this.num_ritardi = num;
    }

    public int getNumUscite() {
        return num_uscite;
    }

    public void setNumUscite(int num) {
        this.num_uscite = num;
    }

    public int getNumNote() {
        return num_note;
    }

    public void setNumNote(int num) {
        this.num_note = num;
    }

    public int getNumComunicazioni() {
        return num_comunicazioni;
    }

    public void setNumComunicazioni(int num) {
        this.num_comunicazioni = num;
    }

    @Override
    public boolean equals(Object o) {
        Account account = (Account) o;

        if (account.getUsername().equals(username)) {
            if (account.getEncryptedPassword().equals(encrypted)) {
                if (account.getLink().equals(link)) {
                    if (account.getSuffisso().equals(suffisso)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("nome:").append(this.nome).append(" ");
        buffer.append("cognome:").append(this.cognome).append(" ");
        buffer.append("username:").append(this.username).append(" ");
        buffer.append("password:").append(this.password).append(" ");
        buffer.append("encrypted:").append(this.encrypted).append(" ");
        buffer.append("link:").append(this.link).append(" ");
        buffer.append("suffisso:").append(this.suffisso).append(" ");
        buffer.append("num_voti:").append(this.num_voti).append(" ");
        buffer.append("num_assenze:").append(this.num_assenze).append(" ");
        buffer.append("num_ritardi:").append(this.num_ritardi).append(" ");
        buffer.append("num_uscite:").append(this.num_uscite).append(" ");
        buffer.append("num_note:").append(this.num_note).append(" ");
        buffer.append("num_comunicazioni:").append(this.num_comunicazioni).append(" ");
        return buffer.toString();
    }
}
