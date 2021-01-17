package com.xandi.seafish.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xandi.seafish.R;
import com.xandi.seafish.adapter.RecyclerViewPositionAdapter;
import com.xandi.seafish.model.Position;
import com.xandi.seafish.model.User;
import com.xandi.seafish.model.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking extends Activity {

    private Activity activity;
    private User user;
    private DatabaseReference mRankingDatabaseRef;

    private LinearLayout background;
    private RecyclerViewPositionAdapter recyclerViewPositionAdapter;
    private RecyclerView rankingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        startFirebaseInstances();

        activity = this;

        background = findViewById(R.id.background);
        rankingView = findViewById(R.id.ranking_view);

        background.setOnClickListener(v -> finish());

        mRankingDatabaseRef.orderByChild("score").limitToLast(100).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Position> ranking = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ranking.add(snap.getValue(Position.class));
                }
                Collections.reverse(ranking);
                recyclerViewPositionAdapter = new RecyclerViewPositionAdapter(ranking, getApplicationContext(), activity);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rankingView.setLayoutManager(layoutManager);
                rankingView.setAdapter(recyclerViewPositionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void startFirebaseInstances() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = mFirebaseDatabase.getReference();
        mRankingDatabaseRef = mDatabaseRef.child(Constants.DATABASE_REF_RANKING);
        Util.setmDatabaseRankingRef(mRankingDatabaseRef);
    }
}