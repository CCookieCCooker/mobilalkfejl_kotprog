package com.example.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilalkfejl_kotprog.helpers.StaticHelpers;
import com.example.mobilalkfejl_kotprog.models.Player;
import com.example.mobilalkfejl_kotprog.models.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePlayerActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private String teamId;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        teamId = getIntent().getStringExtra("TEAM_ID");
    }

    public void createPlayer(View view) {
        EditText nameInput = findViewById(R.id.createPlayerName);
        String name = nameInput.getText().toString();
        EditText roleInput = findViewById(R.id.createPlayerRole);
        String role = roleInput.getText().toString();
        EditText dateInput = findViewById(R.id.createPlayerDate);
        String date = dateInput.getText().toString();


        if (name.isEmpty() || role.isEmpty() || date.isEmpty()) {
            StaticHelpers.showTopSnackbar(view, "All fields are required!");
            return;
        }

        DocumentReference newDoc = firestore.collection("users").document(user.getUid())
                .collection("players").document();

        newDoc.set(new Player(newDoc.getId(), name, role, date))
                .addOnSuccessListener(res -> {
                    StaticHelpers.showTopSnackbar(view, "Player created successfully");
                    finish();
                })
                .addOnFailureListener(ex -> {
                    StaticHelpers.showTopSnackbar(view, ex);
                });
    }
}