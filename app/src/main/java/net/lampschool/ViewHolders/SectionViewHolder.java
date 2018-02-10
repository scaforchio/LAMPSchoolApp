package net.lampschool.ViewHolders;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import net.lampschool.R;

public class SectionViewHolder extends ParentViewHolder
{
    private static final float ROTAZIONE_INIZIALE = 0.0f;
    private static final float ROTAZIONE_FINALE = 180.0f;
    private TextView titolo;
    private ImageView arrow;
    private RelativeLayout layout;

    public SectionViewHolder(View v)
    {
        super(v);
        titolo = (TextView) v.findViewById(R.id.section_title);
        arrow = (ImageView) v.findViewById(R.id.arrow);
        layout = (RelativeLayout) v.findViewById(R.id.rel_sezione);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpanded())
                {
                    collapseView();
                }
                else
                {
                    expandView();
                }
            }
        });
    }

    @Override
    public boolean shouldItemViewClickToggleExpansion() {
        return false;
    }

    public void bind(String s)
    {
        titolo.setText(s);
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if(expanded)
        {
            arrow.setRotation(ROTAZIONE_FINALE);
        }
        else arrow.setRotation(ROTAZIONE_INIZIALE);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation animation;

        if(expanded)
        {
            animation = new RotateAnimation(ROTAZIONE_FINALE,
                    ROTAZIONE_INIZIALE,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                    RotateAnimation.RELATIVE_TO_SELF,0.5f);
        }
        else
        {
            animation = new RotateAnimation(-1 * ROTAZIONE_FINALE,
                    ROTAZIONE_INIZIALE,RotateAnimation.RELATIVE_TO_SELF,0.5f,
                    RotateAnimation.RELATIVE_TO_SELF,0.5f);
        }

        animation.setDuration(200);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);
    }
}
