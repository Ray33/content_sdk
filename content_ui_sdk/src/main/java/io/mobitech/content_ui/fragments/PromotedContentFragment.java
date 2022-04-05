package io.mobitech.content_ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import io.mobitech.content.model.mobitech.Document;
import io.mobitech.content.services.api.callbacks.ContentCallback;
import io.mobitech.content_ui.ContentUIApplication;
import io.mobitech.content_ui.R;
import io.mobitech.content_ui.adapters.UserNewsRecyclerViewNewAPIAdapter;
import io.mobitech.content_ui.interfaces.EndlessScrollListener;
import io.mobitech.content_ui.interfaces.OnItemClickListener;
import io.mobitech.content_ui.interfaces.OnLoadCompleteListener;
import io.mobitech.content_ui.interfaces.OnNewsClickListener;

/**
 * Created by Viacheslav Titov on 14.09.2016.
 *
 * Renders only promoted content
 */

public class PromotedContentFragment extends Fragment implements OnLoadCompleteListener {

    public static final String TAG = PromotedContentFragment.class.getName();

    private RecyclerView mRecyclerView;
    private ContentLoadingProgressBar mProgressBar;
    private OnNewsClickListener mOnNewsClickListener;
    private UserNewsRecyclerViewNewAPIAdapter mAdapter;

    public static PromotedContentFragment newInstance() {
        return new PromotedContentFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (activity == null) return;
        if (activity instanceof OnNewsClickListener) {
            mOnNewsClickListener = (OnNewsClickListener) activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_news, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                updateView(page);
            }

            @Override
            public void onScroll(RecyclerView view, int dx, int dy) {

            }
        });
        mProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.progressBar);
        mAdapter = new UserNewsRecyclerViewNewAPIAdapter(null, mClickListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateView(1);
    }

    private void updateView(final int page) {
        mProgressBar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(ContentUIApplication.getInstance().getUserID())) {
            ContentUIApplication.getInstance().getRecommendationService()
                    .getPromotedContent(null, 5, new ContentCallback<List<Document>>() {
                @Override
                public void processResult(List<Document> contentResult, Context context) {
                    if (contentResult == null) {
                        contentResult = new ArrayList<>();
                    }

                    mAdapter.addData(contentResult);
                    //mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            ContentUIApplication.getInstance().init(this);
        }
    }

    private OnItemClickListener<Document> mClickListener = new OnItemClickListener<Document>() {
        @Override
        public void onItemClick(Document item) {
            if (item == null || mOnNewsClickListener == null) return;
            mOnNewsClickListener.onNewsClick(item.getClickUrl());
        }
    };

    @Override
    public void onLoadComplete() {
        updateView(1);
    }
}
