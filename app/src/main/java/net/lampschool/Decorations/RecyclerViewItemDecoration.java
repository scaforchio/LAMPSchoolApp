package net.lampschool.Decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int spaceHeight;

    public RecyclerViewItemDecoration(int spaceHeight) {
        this.spaceHeight = spaceHeight;
    }

    @Override
    public void getItemOffsets(Rect rect, View v, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(v) != parent.getAdapter().getItemCount() - 1) {
            rect.bottom = spaceHeight;
        }
    }
}
