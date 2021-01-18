package com.xandi.seafish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.xandi.seafish.R;
import com.xandi.seafish.adapter.RecyclerViewPositionAdapter;
import com.xandi.seafish.model.Position;
import com.xandi.seafish.model.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking extends Activity {

    private LinearLayout background;
    private RecyclerViewPositionAdapter recyclerViewPositionAdapter;
    private RecyclerView rankingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        background = findViewById(R.id.background);
        rankingView = findViewById(R.id.ranking_view);

        background.setOnClickListener(v -> finish());

        Util.mDatabaseRankingRef.orderByChild(Constants.DATABASE_REF_SCORE).limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Position> ranking = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ranking.add(snap.getValue(Position.class));
                }
                Collections.reverse(ranking);
                recyclerViewPositionAdapter = new RecyclerViewPositionAdapter(ranking, getApplicationContext(), Util.getUserUid());
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rankingView.setLayoutManager(layoutManager);
                rankingView.setAdapter(recyclerViewPositionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}