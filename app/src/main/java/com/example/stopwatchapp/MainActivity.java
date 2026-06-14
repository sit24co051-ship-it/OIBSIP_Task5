package com.example.stopwatchapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvTimer, tvLaps;
    Button btnStart, btnPause, btnReset, btnLap;
    Button btnBlue, btnPurple, btnBlack, btnWhite;
    Switch switchTheme;
    LinearLayout mainLayout;

    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean running = false;
    private int lapCount = 1;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        tvLaps = findViewById(R.id.tvLaps);

        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);

        btnBlue = findViewById(R.id.btnBlue);
        btnPurple = findViewById(R.id.btnPurple);
        btnBlack = findViewById(R.id.btnBlack);
        btnWhite = findViewById(R.id.btnWhite);

        switchTheme = findViewById(R.id.switchTheme);
        mainLayout = findViewById(R.id.mainLayout);

        prefs = getSharedPreferences("StopwatchPrefs", MODE_PRIVATE);

        loadSettings();

        runTimer();

        btnStart.setOnClickListener(v -> running = true);

        btnPause.setOnClickListener(v -> running = false);

        btnReset.setOnClickListener(v -> {
            running = false;
            seconds = 0;
            lapCount = 1;
            tvTimer.setText("00:00:00");
            tvLaps.setText("");
        });

        btnLap.setOnClickListener(v -> {

            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;

            String lapTime = String.format(
                    Locale.getDefault(),
                    "Lap %d - %02d:%02d:%02d\n",
                    lapCount,
                    hours,
                    minutes,
                    secs
            );

            tvLaps.append(lapTime);
            lapCount++;
        });

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mainLayout.setBackgroundColor(Color.DKGRAY);
                tvTimer.setTextColor(Color.WHITE);
            } else {
                mainLayout.setBackgroundColor(Color.WHITE);
                tvTimer.setTextColor(Color.BLACK);
            }

            prefs.edit().putBoolean("darkMode", isChecked).apply();
        });

        btnBlue.setOnClickListener(v -> saveColor(Color.BLUE));

        btnPurple.setOnClickListener(v ->
                saveColor(Color.parseColor("#800080")));

        btnBlack.setOnClickListener(v -> saveColor(Color.BLACK));

        btnWhite.setOnClickListener(v -> saveColor(Color.WHITE));
    }

    private void saveColor(int color) {
        mainLayout.setBackgroundColor(color);
        prefs.edit().putInt("bgColor", color).apply();
    }

    private void loadSettings() {

        boolean darkMode =
                prefs.getBoolean("darkMode", false);

        switchTheme.setChecked(darkMode);

        int bgColor =
                prefs.getInt("bgColor", Color.WHITE);

        mainLayout.setBackgroundColor(bgColor);
    }

    private void runTimer() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(
                        Locale.getDefault(),
                        "%02d:%02d:%02d",
                        hours,
                        minutes,
                        secs
                );

                tvTimer.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}