package com.example.mayman.finalchat.genericviewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MahmoudAyman on 8/18/2017.
 */

public abstract class GenericViewHolder extends RecyclerView.ViewHolder {
    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(int position);
}
