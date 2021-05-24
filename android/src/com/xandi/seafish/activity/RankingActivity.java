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
import com.xandi.seafish.util.Constants;
import com.xandi.seafish.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingActivity extends Activity {

    private LinearLayout background;
    private RecyclerViewPositionAdapter recyclerViewPositionAdapter;
    private RecyclerView rankingView;
    private String userUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        userUid = getIntent().getExtras().getString("firebaseId");
        background = findViewById(R.id.background);
        rankingView = findViewById(R.id.ranking_view);

        background.setOnClickListener(v -> finish());

        Util.mDatabaseRankingRef.orderByChild(Constants.DATABASE_REF_SCORE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Position> ranking = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Position position = snap.getValue(Position.class);
                    if (position != null && position.getScore() > 0) {
                        if (ranking.contains(position)) {
                            int index = ranking.indexOf(position);
                            Position position1 = ranking.get(index);
                            if (position.getScore() > position1.getScore()) {
                                ranking.set(index, position);
                            }
                        } else {
                            ranking.add(position);
                        }
                    }
                }
                ranking.sort(Collections.reverseOrder());
                recyclerViewPositionAdapter = new RecyclerViewPositionAdapter(ranking, RankingActivity.this, userUid);
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