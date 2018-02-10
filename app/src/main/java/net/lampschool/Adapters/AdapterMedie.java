package net.lampschool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.lampschool.R;
import net.lampschool.Utils.Media;
import net.lampschool.Utils.SezioneMedie;
import net.lampschool.ViewHolders.SectionViewHolder;
import net.lampschool.ViewHolders.ViewHolderMedie;

import java.util.ArrayList;
import java.util.List;

public class AdapterMedie extends ExpandableRecyclerAdapter<SectionViewHolder,ViewHolderMedie>
{
    Context context;
    LayoutInflater inflater;
    ArrayList<SezioneMedie> sezioni;

    public AdapterMedie(Context context,LayoutInflater inflater, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        this.context = context;
        sezioni = (ArrayList<SezioneMedie>) parentItemList;
        this.inflater = inflater;
    }

    @Override
    public SectionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = inflater.inflate(R.layout.list_section,parentViewGroup,false);
        return new SectionViewHolder(v);
    }

    @Override
    public ViewHolderMedie onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = inflater.inflate(R.layout.list_medie_item,childViewGroup,false);
        return new ViewHolderMedie(v);
    }

    @Override
    public void onBindParentViewHolder(SectionViewHolder parentViewHolder, int position, ParentListItem parentListItem)
    {
        SezioneMedie sezione = (SezioneMedie) parentListItem;
        parentViewHolder.bind(sezione.getTitolo());
    }

    @Override
    public void onBindChildViewHolder(ViewHolderMedie childViewHolder, int position, Object childListItem)
    {
        Media media = (Media) childListItem;
        childViewHolder.bind(context,media);
    }
}
