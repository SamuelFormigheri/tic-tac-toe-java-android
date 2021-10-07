package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    private Button btnExit, btnPlay, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnExit = (Button) findViewById(R.id.btnExit);

        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(DashboardActivity.this, ConfigurePlayerActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(DashboardActivity.this, AboutActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                exitApp(view);
            }
        });


    }

    public void exitApp(View view) {
        finish();
        System.exit(0);
    }

}