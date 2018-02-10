package net.lampschool.Listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollListener extends RecyclerView.OnScrollListener {
    public static final float HIDE_THRESHOLD = 100;
    public static final float SHOW_THRESHOLD = 50;

    int scrollDist = 0;
    private boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();


        if (firstVisibleItem == 0) {
            hide();
            scrollDist = 0;
            isVisible = false;
        } else {

            if (!isVisible && scrollDist < SHOW_THRESHOLD) {
                show();
                scrollDist = 0;
                isVisible = true;
            }

            if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
                scrollDist += dy;
            }
        }
    }

    public abstract void show();

    public abstract void hide();
}
