package com.xandi.seafish.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xandi.seafish.R;
import com.xandi.seafish.model.Position;

public class ShowRankingDetailsDialog extends Dialog {

    private Context context;
    private Position position;
    private String username;
    private int rankPosition;

    private TextView positionTV;
    private TextView usernameTV;
    private TextView score;
    private ImageView fish;
    private ImageView deathTackle;
    private ImageView warm;
    private ImageView specialWarm;
    private ImageView bubble;
    private ImageView shark;
    private ImageView hook;
    private TextView warmQuantity;
    private TextView specialWarmQuantity;
    private TextView bubbleQuantity;
    private TextView sharkQuantity;
    private TextView hookQuantity;

    public ShowRankingDetailsDialog(@NonNull Context context, Position position, String username, int rankPosition) {
        super(context);
        this.context = context;
        this.position = position;
        this.username = username;
        this.rankPosition = rankPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ranking_details_dialog);

        setLayoutAttributes();

        positionTV.setText(rankPosition + 1 + "º");
        usernameTV.setText(username);
        warmQuantity.setText(String.valueOf(position.getCaughtWarms()));
        specialWarmQuantity.setText(String.valueOf(position.getCaughtSpecialWarms()));
        bubbleQuantity.setText(String.valueOf(position.getCaughtBubbles()));
        sharkQuantity.setText(String.valueOf(position.getTurnedShark()));
        hookQuantity.setText(String.valueOf(position.getCaughtByHook()));

        if (position.getUsedFish().contains("peixe1.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe1));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao1));
        } else if (position.getUsedFish().contains("peixe2.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe2));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao2));
        } else if (position.getUsedFish().contains("peixe3.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe3));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao3));
        } else if (position.getUsedFish().contains("peixe4.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe4));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao4));
        } else if (position.getUsedFish().contains("peixe5.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe5));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao5));
        } else if (position.getUsedFish().contains("peixe6.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe6));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao6));
        } else if (position.getUsedFish().contains("peixe7.png")) {
            fish.setImageDrawable(context.getResources().getDrawable(R.drawable.peixe7));
            shark.setImageDrawable(context.getResources().getDrawable(R.drawable.tubarao7));
        }
        if (position.getDeathTackle().contains("canudo.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.canudo));
        } else if (position.getDeathTackle().contains("baiacu.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.baiacu));
        } else if (position.getDeathTackle().contains("garrafa.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.garrafa));
        } else if (position.getDeathTackle().contains("piranha.spng")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.piranhas));
        } else if (position.getDeathTackle().contains("pneu.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.pneu));
        } else if (position.getDeathTackle().contains("tubaraoinimigo2.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.tubaraoinimigo2));
        } else if (position.getDeathTackle().contains("plastico.png")) {
            deathTackle.setImageDrawable(context.getResources().getDrawable(R.drawable.plastico));
        }
        hook.setImageDrawable(context.getResources().getDrawable(R.drawable.anzol));
        warm.setImageDrawable(context.getResources().getDrawable(R.drawable.minhoca));
        specialWarm.setImageDrawable(context.getResources().getDrawable(R.drawable.minhocabonus));
        bubble.setImageDrawable(context.getResources().getDrawable(R.drawable.bolha));
        score.setText(position.getScore() + "m");
    }

    private void setLayoutAttributes() {
        score = findViewById(R.id.score);
        positionTV = findViewById(R.id.position);
        usernameTV = findViewById(R.id.name);
        fish = findViewById(R.id.fish);
        deathTackle = findViewById(R.id.death_tackle);
        warm = findViewById(R.id.warm);
        warmQuantity = findViewById(R.id.warm_quantity);
        bubble = findViewById(R.id.bubble);
        bubbleQuantity = findViewById(R.id.bubble_quantity);
        specialWarm = findViewById(R.id.special_warm);
        specialWarmQuantity = findViewById(R.id.special_warm_quantity);
        hook = findViewById(R.id.hook);
        hookQuantity = findViewById(R.id.hook_quantity);
        shark = findViewById(R.id.shark);
        sharkQuantity = findViewById(R.id.shark_quantity);
    }
}