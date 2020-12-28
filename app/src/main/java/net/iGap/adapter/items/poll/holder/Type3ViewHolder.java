package net.iGap.adapter.items.poll.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import net.iGap.R;
import net.iGap.adapter.items.poll.PollAdapter;
import net.iGap.adapter.items.poll.PollItem;

public class Type3ViewHolder extends BaseViewHolder {
    private ImageView img0, img1;
    private CardView card0, card1;
    private View tick0, tick1;

    public Type3ViewHolder(PollAdapter pollAdapter, @NonNull View itemView) {
        super(pollAdapter, itemView);
        img0 = itemView.findViewById(R.id.type3_img0);
        img1 = itemView.findViewById(R.id.type3_img1);
        card0 = itemView.findViewById(R.id.type3_card0);
        card1 = itemView.findViewById(R.id.type3_card1);

        tick0 = itemView.findViewById(R.id.type3_tick0);
        tick1 = itemView.findViewById(R.id.type3_tick1);

    }

    @Override
    public void bindView(PollItem item) {
        img0.setImageDrawable(null);
        img1.setImageDrawable(null);
        if (item.pollItemFields == null || item.pollItemFields.size() < 2)
            return;

        loadImage(img0, item.pollItemFields.get(0).imageUrl);
        loadImage(img1, item.pollItemFields.get(1).imageUrl);

        checkTickVisibility(item.pollItemFields.get(0), tick0);
        checkTickVisibility(item.pollItemFields.get(1), tick1);

        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePollFieldsClick(item.pollItemFields.get(0));
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePollFieldsClick(item.pollItemFields.get(1));
            }
        });

    }
}