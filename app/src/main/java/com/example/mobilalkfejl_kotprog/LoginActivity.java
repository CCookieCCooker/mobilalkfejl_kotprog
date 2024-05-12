package com.example.mobilalkfejl_kotprog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobilalkfejl_kotprog.helpers.StaticHelpers;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void login(View view) {
        EditText emailInput = findViewById(R.id.loginEmailInput);
        String email = emailInput.getText().toString();
        EditText passwordInput = findViewById(R.id.loginPasswordInput);
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            StaticHelpers.showTopSnackbar(view, "Filling the E-mail and Password fields is required");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(res -> {
                    StaticHelpers.showTopSnackbar(view, "Login successful");

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(ex -> {
                    StaticHelpers.showTopSnackbar(view, ex);
                });
    }

    public void redirectToRegistration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
        finish();
    }
}
