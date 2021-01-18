package com.xandi.seafish.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.xandi.seafish.model.Position;
import com.xandi.seafish.model.User;
import com.xandi.seafish.model.Util;

import java.util.List;

public class RecyclerViewPositionAdapter extends RecyclerView.Adapter<RecyclerViewPositionAdapter.ViewHolder> {

    private List<Position> elements;
    private Context context;
    private String userUid;

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

        if (position.getUserUid().equals(userUid)) {
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green));
        }

        int textSize = 30;

        if (pos == 0) {
            holder.position.setTextSize(textSize);
            holder.name.setTextSize(textSize);
            holder.score.setTextSize(textSize);
        } else if (pos > 0 && pos < 10) {
            textSize = textSize - (pos * 2);
            holder.position.setTextSize(textSize);
            holder.name.setTextSize(textSize);
            holder.score.setTextSize(textSize);
        }

        Util.mDatabaseUserRef.child(position.getUserUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    Glide.with(context).load(user.getPhotoPath()).apply(RequestOptions.circleCropTransform()).addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("GlideError:", e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(holder.photo);
                    holder.position.setText((pos + 1) + "º");
                    holder.name.setText(user.getName());
                    holder.score.setText(String.valueOf(position.getScore().intValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout background;
        private ImageView photo;
        private TextView position;
        private TextView name;
        private TextView score;

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