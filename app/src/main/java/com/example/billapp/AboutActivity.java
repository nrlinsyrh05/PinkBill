package com.example.billapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tvGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvGithub =
                findViewById(R.id.tvGithub);

        tvGithub.setOnClickListener(v -> {

            Intent browserIntent =
                    new Intent(
                            Intent.ACTION_VIEW,

                            Uri.parse(
                                    "https://github.com/nrlinsyrh05"));

            startActivity(browserIntent);
        });
    }
}