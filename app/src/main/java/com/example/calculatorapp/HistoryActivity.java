package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    TextView historyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyText = findViewById(R.id.historyText);

        String history = getIntent().getStringExtra("history");

        if (history != null) {
            historyText.setText(history);
        }
    }
}
