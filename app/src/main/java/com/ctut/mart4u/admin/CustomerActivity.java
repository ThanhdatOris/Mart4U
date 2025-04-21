package com.ctut.mart4u.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ctut.mart4u.MainActivity;
import com.ctut.mart4u.R;

public class CustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Start MainActivity
        Intent intent = new Intent(CustomerActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Close CustomerActivity if you don't want to return to it
    }
}