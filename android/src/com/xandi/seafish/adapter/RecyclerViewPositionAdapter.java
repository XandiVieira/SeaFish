package com.xandi.seafish.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xandi.seafish.R;
import com.xandi.seafish.model.Position;

import java.util.List;

public class RecyclerViewPositionAdapter extends RecyclerView.Adapter<RecyclerViewPositionAdapter.ViewHolder> {

    private List<Position> elements;
    private Context context;

    public RecyclerViewPositionAdapter(List<Position> elements, Context context, Activity activity) {
        this.elements = elements;
        this.context = context;
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

        holder.position.setText((pos + 1) + "º");
        holder.name.setText(position.getName());
        holder.score.setText(String.valueOf(position.getScore()));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView position;
        private TextView name;
        private TextView score;

        ViewHolder(View rowView) {
            super(rowView);
            position = rowView.findViewById(R.id.position);
            name = rowView.findViewById(R.id.name);
            score = rowView.findViewById(R.id.score);
        }
    }
}