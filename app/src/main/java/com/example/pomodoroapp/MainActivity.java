package com.example.pomodoroapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Button;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeRemainingMillis = 25 * 60 * 1000;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerStart;
    private String mode = "work";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.ring);
        mediaPlayerStart = MediaPlayer.create(this, R.raw.ring);

        Button buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStartPause.setOnClickListener(view -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
                updateTimerText();
            }

            public void onFinish() {
                isTimerRunning = false;
                updateTimerText();
                mediaPlayer.start();
                if (mode == "work"){
                    setTimer(3);
                    mode = "short";
                } else {
                    setTimer(25);
                    mode = "work";
                }
                pauseTimer();
                changeBackground();
            }
        }.start();

        mediaPlayerStart.start();
        isTimerRunning = true;
        Button buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStartPause.setText("Pause");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        Button buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStartPause.setText("Start");
        mediaPlayerStart.start();
    }

    private void updateTimerText() {
        long minutes = timeRemainingMillis / 1000 / 60;
        long seconds = (timeRemainingMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        TextView textViewTimer = findViewById(R.id.textViewTimer);
        textViewTimer.setText(timeLeftFormatted);
    }

    public void setTimer(int minutes) {
        timeRemainingMillis = minutes * 60 * 1000;
        updateTimerText();
        if (isTimerRunning) {
            pauseTimer();
            startTimer();
        }
    }
    private void changeBackground() {
        ConstraintLayout mainLayout = findViewById(R.id.main);
        int colorResId;
        switch (mode) {
            case "work":
                colorResId = R.color.white;
                break;
            case "short":
                colorResId = R.color.colorShortBreak;
                break;
            case "long":
                colorResId = R.color.colorLongBreak;
                break;
            default:
                colorResId = R.color.white;
                break;
        }
        mainLayout.setBackgroundColor(ContextCompat.getColor(this, colorResId));
    }

    public void setTimer25Minutes(View view) {
        setTimer(25);
        mode = "work";
        changeBackground();
        mediaPlayerStart.start();
    }

    public void setTimer3Minutes(View view) {
        setTimer(3);
        mode = "short";
        changeBackground();
        mediaPlayerStart.start();
    }

    public void setTimer15Minutes(View view) {
        setTimer(15);
        mode = "long";
        changeBackground();
        mediaPlayerStart.start();
    }
}