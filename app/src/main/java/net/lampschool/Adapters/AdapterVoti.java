package net.lampschool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.lampschool.R;
import net.lampschool.Utils.SezioneVoti;
import net.lampschool.Utils.Voto;
import net.lampschool.ViewHolders.SectionViewHolder;
import net.lampschool.ViewHolders.ViewHolderVoti;

import java.util.ArrayList;
import java.util.List;

public class AdapterVoti extends ExpandableRecyclerAdapter<SectionViewHolder,ViewHolderVoti>
{
    private LayoutInflater inflater;
    private ArrayList<SezioneVoti> sezioni;
    private Context context;

    public AdapterVoti(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        this.context = context;
        sezioni = (ArrayList<SezioneVoti>)parentItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SectionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = inflater.inflate(R.layout.list_section,parentViewGroup,false);
        return new SectionViewHolder(v);
    }

    @Override
    public ViewHolderVoti onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = inflater.inflate(R.layout.list_voti_item,childViewGroup,false);
        return new ViewHolderVoti(v);
    }

    @Override
    public void onBindParentViewHolder(SectionViewHolder parentViewHolder, int position, ParentListItem parentListItem)
    {
        SezioneVoti sezione = (SezioneVoti) parentListItem;
        parentViewHolder.bind(sezione.getTitolo());
    }

    @Override
    public void onBindChildViewHolder(ViewHolderVoti childViewHolder, int position, Object childListItem)
    {
        Voto voto = (Voto) childListItem;
        childViewHolder.bind(context,voto);
    }

    public int getAllItemCount()
    {
        int num = 0;

        for(SezioneVoti sezione : sezioni)
        {
            num += sezione.getVoti().size();
        }

        return num;
    }
}
