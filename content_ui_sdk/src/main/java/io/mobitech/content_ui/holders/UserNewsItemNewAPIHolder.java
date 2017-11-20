package io.mobitech.content_ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.mobitech.content.model.mobitech.Document;
import io.mobitech.content_ui.R;
import io.mobitech.content_ui.interfaces.OnItemClickListener;
import io.mobitech.content_ui.utils.GlideApp;
import io.mobitech.content_ui.utils.PrettyTime;

/**
 * Created by Viacheslav Titov on 28.10.2016.
 */

public class UserNewsItemNewAPIHolder extends RecyclerView.ViewHolder {

    private OnItemClickListener<Document> mClickListener;

    private ImageView mNewsImageView;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mPublisherTextView;
    private TextView mPromotedTextView;

    public UserNewsItemNewAPIHolder(View itemView, OnItemClickListener<Document> clickListener) {
        super(itemView);
        mNewsImageView = (ImageView) itemView.findViewById(R.id.newsImageView);
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mDateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
        mPublisherTextView = (TextView) itemView.findViewById(R.id.publisherTextView);
        mPromotedTextView = (TextView) itemView.findViewById(R.id.promotedTextView);
        mClickListener = clickListener;
    }

    public void bind(final Document item) {
        if (item == null) return;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(item);
                }
            }
        });
        if (item.getThumbnails() != null && !item.getThumbnails().isEmpty()) {
//            Glide.get(itemView.getContext());
            GlideApp.with(itemView.getContext())
                    .load(item.getThumbnails().get(0).getUrl())
//                    .dontAnimate()
//                    .dontTransform()
//                    .centerCrop()
                    .into(mNewsImageView);
        }

        if (!item.isPromoted()) {
            mPromotedTextView.setVisibility(View.GONE);
        }

        mTitleTextView.setText(item.getTitle());
        mDateTextView.setText(PrettyTime.getTimeAgo(item.getPublishedTime(), itemView.getContext()));
        mPublisherTextView.setText(item.getPromotedText());
    }
}
