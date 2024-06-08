package com.vikram.xlauncher;

import android.graphics.Rect;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import androidx.recyclerview.widget.RecyclerView;

public class CenterCardItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalMargin;

    public CenterCardItemDecoration(int horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        int halfWidth = parent.getWidth() / 2;
        int childWidth = view.getWidth();

        int offset = halfWidth - childWidth / 2 - horizontalMargin;

        if (position == 0) {
            outRect.left = offset;
            outRect.right = horizontalMargin / 2;
        } else if (position == itemCount - 1) {
            outRect.left = horizontalMargin / 2;
            outRect.right = offset;
        } else {
            outRect.left = horizontalMargin / 2;
            outRect.right = horizontalMargin / 2;
        }
    }
}