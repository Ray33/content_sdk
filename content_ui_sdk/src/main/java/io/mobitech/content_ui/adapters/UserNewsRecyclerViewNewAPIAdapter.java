package io.mobitech.content_ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import io.mobitech.content.model.mobitech.Document;
import io.mobitech.content_ui.R;
import io.mobitech.content_ui.holders.UserNewsItemNewAPIHolder;
import io.mobitech.content_ui.interfaces.OnItemClickListener;

/**
 * Created by Viacheslav Titov on 28.10.2016.
 */

public class UserNewsRecyclerViewNewAPIAdapter extends RecyclerView.Adapter<UserNewsItemNewAPIHolder> {

    private List<Document> mDocuments;
    private OnItemClickListener<Document> mClickListener;

    public UserNewsRecyclerViewNewAPIAdapter(List<Document> documents, OnItemClickListener<Document> clickListener) {
        super();
        mDocuments = documents != null ? documents : new ArrayList<Document>();
        mClickListener = clickListener;
    }

    @Override
    public UserNewsItemNewAPIHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_user_news, parent, false);
        return new UserNewsItemNewAPIHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(UserNewsItemNewAPIHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (mDocuments != null) return mDocuments.size();
        return 0;
    }

    public void addData(List<Document> documents) {
        mDocuments.addAll(documents);
        this.notifyDataSetChanged();
    }

    public Document getItem(int position) {
        if (mDocuments == null || mDocuments.isEmpty() || mDocuments.size() < position) return null;
        return mDocuments.get(position);
    }
}
