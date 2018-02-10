package net.lampschool.Utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class SezioneLezioni implements ParentListItem
{
    private String titolo;
    private ArrayList<Lezione> lezioni;
    private int index;

    public SezioneLezioni(String titolo, ArrayList<Lezione> lezioni, int index) {
        this.titolo = titolo;
        this.lezioni = lezioni;
        this.index = index;
    }

    public SezioneLezioni() {}

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

    public ArrayList<Lezione> getLezioni() {
        return lezioni;
    }

    public void setLezioni(ArrayList<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    @Override
    public List<?> getChildItemList() {
        return lezioni;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
