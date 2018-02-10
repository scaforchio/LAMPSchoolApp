package net.lampschool.Utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class SezioneNote implements ParentListItem
{
    private String titolo;
    private ArrayList<Nota> note;
    private int index;

    public SezioneNote(String titolo, ArrayList<Nota> note, int index) {
        this.titolo = titolo;
        this.note = note;
        this.index = index;
    }

    public SezioneNote(){}

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public ArrayList<Nota> getNote() {
        return note;
    }

    public void setNote(ArrayList<Nota> note) {
        this.note = note;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public List<?> getChildItemList() {
        return note;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
