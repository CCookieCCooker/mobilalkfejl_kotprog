package com.example.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilalkfejl_kotprog.databinding.FragmentTeamsListBinding;
import com.example.mobilalkfejl_kotprog.helpers.StaticHelpers;
import com.example.mobilalkfejl_kotprog.models.Player;
import com.example.mobilalkfejl_kotprog.models.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.stream.StreamSupport;

public class PlayersListActivity extends AppCompatActivity {

    private String teamId;

    private LinearLayout listLayout;

    private FirebaseFirestore firestore;

    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_players_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        teamId = getIntent().getStringExtra("TEAM_ID");

        listLayout = findViewById(R.id.playersList);

        firestore = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();

        TextView teamName = findViewById(R.id.playersTeamNameText);

        firestore.collection("users")
                .document(user.getUid())
                .collection("teams")
                .document(teamId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Team team = snapshot.toObject(Team.class);
                    assert team != null;
                    teamName.setText(String.format("%s %s", team.getCity(), team.getName()));
                })
                .addOnFailureListener(ex -> {
                    finish();
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        listLayout.removeAllViews();
        firestore
                .collection("users")
                .document(user.getUid())
                .collection("players")
                .orderBy("name")
                .get()
                .addOnSuccessListener(snapshot -> {
                    StreamSupport.stream(snapshot.spliterator(), false)
                            .map(doc -> doc.toObject(Player.class))
                            .forEach(player -> {
                                View teamView = getLayoutInflater().inflate(R.layout.player_list_item, null);

                                TextView playerName = teamView.findViewById(R.id.playerListItemName);
                                TextView playerRole = teamView.findViewById(R.id.playerListItemRole);
                                TextView playerDate = teamView.findViewById(R.id.playerListItemDate);

                                playerName.setText(player.getName());
                                playerRole.setText(player.getRole());
                                playerDate.setText(player.getBirthDate());

                                listLayout.addView(teamView);
                            });
                });
    }

    public void createPlayer(View view) {
        Intent intent = new Intent(view.getContext(), CreatePlayerActivity.class);
        intent.putExtra("TEAM_ID", teamId);
        startActivity(intent);
    }
}