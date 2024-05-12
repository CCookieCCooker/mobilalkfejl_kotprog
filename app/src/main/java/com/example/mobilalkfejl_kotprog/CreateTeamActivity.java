package com.example.mobilalkfejl_kotprog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

public class CreateTeamActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_team);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createTeam(View view) {
        EditText teamNameInput = findViewById(R.id.updateTeamNameInput);
        String teamName = teamNameInput.getText().toString();
        EditText teamCityInput = findViewById(R.id.updateTeamCityInput);
        String teamCity = teamCityInput.getText().toString();
        EditText teamPrincipalInput = findViewById(R.id.updateTeamPrincipalInput);
        String teamPrincipal = teamPrincipalInput.getText().toString();

        if (teamName.isEmpty() || teamCity.isEmpty() || teamPrincipal.isEmpty()) {
            StaticHelpers.showTopSnackbar(view, "All fields are required");
            return;
        }

        DocumentReference newDoc = firestore.collection("users").document(user.getUid())
                .collection("teams").document();

        newDoc.set(new Team(newDoc.getId(), teamName, teamCity, teamPrincipal))
                .addOnSuccessListener(res -> {
                    StaticHelpers.showTopSnackbar(view, "Team created successfully");
                    finish();
                })
                .addOnFailureListener(ex -> {
                    StaticHelpers.showTopSnackbar(view, ex);
                });
    }
}