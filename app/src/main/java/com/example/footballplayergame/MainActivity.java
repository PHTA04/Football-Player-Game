package com.example.footballplayergame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footballplayergame.Activities.SetsActivity;
import com.example.footballplayergame.Activities.SignInActivity;
import com.example.footballplayergame.Activities.UserManual;
import com.example.footballplayergame.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    private String difficultyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLevel = "easy";
                startSetsActivity("Easy Level");
            }
        });

        binding.normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLevel = "normal";
                startSetsActivity("Normal Level");
            }
        });

        binding.hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLevel = "hard";
                startSetsActivity("Hard Level");
            }
        });

        binding.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficultyLevel = "all";
                startSetsActivity("All Levels");
            }
        });

        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnUserManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserManual.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this,
                        GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Mở SignInActivity sau khi đã đăng xuất
                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public void startSetsActivity(String category) {
        Intent intent = new Intent(MainActivity.this, SetsActivity.class);
        intent.putExtra("nameCategory", category);
        intent.putExtra("difficultyLevel", difficultyLevel); // Pass difficulty level to SetsActivity
        startActivity(intent);
    }
}