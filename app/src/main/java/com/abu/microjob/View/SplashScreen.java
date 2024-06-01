package com.abu.microjob.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.abu.microjob.R;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    int prog_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.splash_progress_bar);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mDoProgWork();
                goToMain();
            }
        });
        thread.start();

    }

    private void goToMain() {
        Intent intent = new Intent(SplashScreen.this, MyAuth.class);
        startActivity(intent);
        finish();
    }

    private void mDoProgWork() {
        for (prog_value = 1; prog_value<=100; prog_value++){

            try {
                Thread.sleep(50);
                progressBar.setProgress(prog_value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}