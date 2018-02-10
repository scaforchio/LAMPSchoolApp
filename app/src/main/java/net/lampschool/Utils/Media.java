package net.lampschool.Utils;

public class Media {
    private String materia;
    private int quadrimestre;
    private float media;

    public Media() {
        materia = "";
        media = 0;
        quadrimestre = 1;
    }

    public Media(String materia, float media, int quadrimestre) {
        this.materia = materia;
        this.media = media;
        this.quadrimestre = quadrimestre;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getMateria() {
        return materia;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public float getMedia() {
        return media;
    }

    public void setQuadrimestre(int quadrimestre) {
        this.quadrimestre = quadrimestre;
    }

    public int getQuadrimestre() {
        return quadrimestre;
    }
}
