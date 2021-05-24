package com.xandi.seafish.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.xandi.seafish.R;
import com.xandi.seafish.dialog.ShowRankingDetailsDialog;
import com.xandi.seafish.model.Position;
import com.xandi.seafish.model.User;
import com.xandi.seafish.util.Util;

import java.util.List;

public class RecyclerViewPositionAdapter extends RecyclerView.Adapter<RecyclerViewPositionAdapter.ViewHolder> {

    private final List<Position> elements;
    private final Context context;
    private final String userUid;

    public RecyclerViewPositionAdapter(List<Position> elements, Context context, String userUid) {
        this.elements = elements;
        this.context = context;
        this.userUid = userUid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_item, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewPositionAdapter.ViewHolder holder, int pos) {
        Position position = elements.get(pos);
        final String[] username = new String[1];

        if (position.getUserUid().equals(userUid)) {
            holder.name.setTextColor(context.getResources().getColor(R.color.accent));
        }

        int textSize = 30;

        if (pos > 0 && pos < 10) {
            textSize = textSize - pos;
        } else if (pos >= 10) {
            textSize = 20;
        }

        holder.position.setTextSize(textSize);
        holder.name.setTextSize(textSize);
        holder.score.setTextSize(textSize);

        if (pos <= 2) {
            holder.score.setTextColor(context.getResources().getColor(R.color.accent_dark));
        } else {
            holder.score.setTextColor(context.getResources().getColor(R.color.primary_dark));
        }

        Util.mDatabaseUserRef.child(position.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    username[0] = user.getName();
                    Glide.with(context).load(user.getPhotoPath()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).apply(RequestOptions.circleCropTransform()).into(holder.photo);
                    holder.position.setText((pos + 1) + "º");
                    holder.name.setText(user.getName());
                    holder.score.setText(position.getScore() + "m");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.background.setOnClickListener(v -> {
            ShowRankingDetailsDialog showRankingDetailsDialog = new ShowRankingDetailsDialog(context, position, username[0], pos);
            showRankingDetailsDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout background;
        private final ImageView photo;
        private final TextView position;
        private final TextView name;
        private final TextView score;

        ViewHolder(View rowView) {
            super(rowView);
            background = rowView.findViewById(R.id.background);
            photo = rowView.findViewById(R.id.photo);
            position = rowView.findViewById(R.id.position);
            name = rowView.findViewById(R.id.name);
            score = rowView.findViewById(R.id.score);
        }
    }
}