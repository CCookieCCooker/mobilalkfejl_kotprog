package com.example.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilalkfejl_kotprog.helpers.StaticHelpers;
import com.example.mobilalkfejl_kotprog.models.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.stream.StreamSupport;

public class EditTeamActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser user;

    EditText teamNameInput;
    EditText teamCityInput;
    EditText teamPrincipalInput;

    String teamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_team);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        teamNameInput = findViewById(R.id.updateTeamNameInput);
        teamCityInput = findViewById(R.id.updateTeamCityInput);
        teamPrincipalInput = findViewById(R.id.updateTeamPrincipalInput);

        teamId = getIntent().getStringExtra("TEAM_ID");

        firestore.collection("users")
                .document(user.getUid())
                .collection("teams")
                .document(teamId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Team team = snapshot.toObject(Team.class);
                    assert team != null;
                    teamNameInput.setText(team.getName());
                    teamCityInput.setText(team.getCity());
                    teamPrincipalInput.setText(team.getPrincipal());
                })
                .addOnFailureListener(ex -> {
                    finish();
                });
    }

    public void editTeam(View view) {
        String teamName = teamNameInput.getText().toString();
        String teamCity = teamCityInput.getText().toString();
        String teamPrincipal = teamPrincipalInput.getText().toString();

        if (teamName.isEmpty() || teamCity.isEmpty() || teamPrincipal.isEmpty()) {
            StaticHelpers.showTopSnackbar(view, "All fields are required");
            return;
        }

        DocumentReference team = firestore.collection("users").document(user.getUid())
                .collection("teams").document(teamId);

        team.set(new Team(team.getId(), teamName, teamCity, teamPrincipal))
                .addOnSuccessListener(res -> {
                    StaticHelpers.showTopSnackbar(view, "Team updated successfully");
                    finish();
                })
                .addOnFailureListener(ex -> {
                    StaticHelpers.showTopSnackbar(view, ex);
                });
    }

    public void deleteTeam(View view) {
        DocumentReference team = firestore.collection("users").document(user.getUid())
                .collection("teams").document(teamId);

        team.delete()
            .addOnSuccessListener(res -> {
                StaticHelpers.showTopSnackbar(view, "Team deleted successfully");
                finish();
            })
            .addOnFailureListener(ex -> {
                StaticHelpers.showTopSnackbar(view, ex);
            });
    }

    public void viewPlayers(View view) {
        Intent intent = new Intent(view.getContext(), PlayersListActivity.class);
        intent.putExtra("TEAM_ID", teamId);
        startActivity(intent);
    }
}