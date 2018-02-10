package net.lampschool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import net.lampschool.R;
import net.lampschool.Utils.Nota;
import net.lampschool.Utils.SezioneNote;
import net.lampschool.ViewHolders.SectionViewHolder;
import net.lampschool.ViewHolders.ViewHolderNote;

import java.util.ArrayList;
import java.util.List;

public class AdapterNote extends ExpandableRecyclerAdapter<SectionViewHolder,ViewHolderNote>
{
    private LayoutInflater inflater;
    private ArrayList<SezioneNote> note;
    private Context context;

    public AdapterNote(Context context,@NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        this.context = context;
        note = (ArrayList<SezioneNote>) parentItemList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SectionViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View v = inflater.inflate(R.layout.list_section,parentViewGroup,false);
        return new SectionViewHolder(v);
    }

    @Override
    public ViewHolderNote onCreateChildViewHolder(ViewGroup childViewGroup) {
        View v = inflater.inflate(R.layout.list_note_item,childViewGroup,false);
        return new ViewHolderNote(v);
    }

    @Override
    public void onBindParentViewHolder(SectionViewHolder parentViewHolder, int position, ParentListItem parentListItem)
    {
        SezioneNote sezione = (SezioneNote) parentListItem;
        parentViewHolder.bind(sezione.getTitolo());
    }

    @Override
    public void onBindChildViewHolder(ViewHolderNote childViewHolder, int position, Object childListItem)
    {
        Nota nota = (Nota) childListItem;
        childViewHolder.bind(nota);
    }
}
