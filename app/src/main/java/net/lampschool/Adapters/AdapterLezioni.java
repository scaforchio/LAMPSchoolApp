package net.lampschool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.lampschool.R;
import net.lampschool.Utils.Lezione;
import net.lampschool.Utils.SezioneLezioni;
import net.lampschool.ViewHolders.SectionViewHolder;
import net.lampschool.ViewHolders.ViewHolderLezione;

import java.util.ArrayList;
import java.util.List;

public class AdapterLezioni extends ExpandableRecyclerAdapter<SectionViewHolder,ViewHolderLezione>
{
    private LayoutInflater inflater;
    private ArrayList<SezioneLezioni> sezioni;

    public AdapterLezioni(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        sezioni = (ArrayList<SezioneLezioni>)parentItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SectionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = inflater.inflate(R.layout.list_section,parentViewGroup,false);
        return new SectionViewHolder(v);
    }

    @Override
    public ViewHolderLezione onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = inflater.inflate(R.layout.list_lezioni_item,childViewGroup,false);
        return new ViewHolderLezione(v);
    }

    @Override
    public void onBindParentViewHolder(SectionViewHolder parentViewHolder, int position, ParentListItem parentListItem)
    {
        SezioneLezioni sezione = (SezioneLezioni) parentListItem;
        parentViewHolder.bind(sezione.getTitolo());
    }

    @Override
    public void onBindChildViewHolder(ViewHolderLezione childViewHolder, int position, Object childListItem)
    {
        Lezione lezione = (Lezione) childListItem;
        childViewHolder.bind(lezione);
    }

    public int getAllItemCount()
    {
        int num = 0;

        for(SezioneLezioni sezione : sezioni)
        {
            num += sezione.getLezioni().size();
        }

        return num;
    }
}
