package net.lampschool.Utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class SezioneVoti implements ParentListItem
{
    private String titolo;
    private ArrayList<Voto> voti;
    private int index;

    public SezioneVoti(String titolo, ArrayList<Voto> voti, int index) {
        this.titolo = titolo;
        this.voti = voti;
        this.index = index;
    }

    public SezioneVoti() {
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Voto> getVoti() {
        return voti;
    }

    public void setVoti(ArrayList<Voto> voti) {
        this.voti = voti;
    }

    @Override
    public List<?> getChildItemList() {
        return voti;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
