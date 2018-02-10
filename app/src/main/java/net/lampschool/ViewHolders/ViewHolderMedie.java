package net.lampschool.ViewHolders;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import net.lampschool.R;
import net.lampschool.Utils.Media;

import java.util.Locale;

public class ViewHolderMedie extends ChildViewHolder
{
    private TextView materia;
    private TextView media;

    public ViewHolderMedie(View v) {
        super(v);
        materia = (TextView) v.findViewById(R.id.materia_media);
        media = (TextView) v.findViewById(R.id.media);
    }

    public void bind(Context context,Media m)
    {
        Resources risorse = context.getResources();

        materia.setText(m.getMateria());
        String s = String.format(Locale.US, "%.1f", m.getMedia());

        if (m.getMedia() == 0.0) {
            media.setTextColor(risorse.getColor(android.R.color.black));
            media.setText("-");
        } else if (m.getMedia() >= 6.0) {
            media.setTextColor(risorse.getColor(R.color.verde));
            media.setText(s);
        } else {
            media.setTextColor(risorse.getColor(R.color.rosso));
            media.setText(s);
        }
    }
}
