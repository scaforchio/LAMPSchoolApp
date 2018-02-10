package net.lampschool.Utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class SezioneMedie implements ParentListItem
{
    private String titolo;
    private ArrayList<Media> medie;
    private int index;

    public SezioneMedie(String titolo, ArrayList<Media> medie, int index) {
        this.titolo = titolo;
        this.medie = medie;
        this.index = index;
    }

    public SezioneMedie(){}

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

    public ArrayList<Media> getMedie() {
        return medie;
    }

    public void setMedie(ArrayList<Media> medie) {
        this.medie = medie;
    }

    @Override
    public List<?> getChildItemList() {
        return medie;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
