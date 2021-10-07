package com.example.tictactoe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ConfigurePlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private int CAMERA_PERMISSION_CODE = 1;
    private int CAMERA_REQUEST_CODE = 1;
    private AppCompatImageView playerOneImage, playerTwoImage;
    private Bitmap imgPlayerOne, imgPlayerTwo;
    private boolean setPlayerImageOne = true;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_player);

        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        playerOneImage = (AppCompatImageView) findViewById(R.id.playerOneImage);
        playerTwoImage = (AppCompatImageView) findViewById(R.id.playerTwoImage);

        playerOneImage.setOnClickListener(this);
        playerTwoImage.setOnClickListener(this);

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent main = new Intent(ConfigurePlayerActivity.this, MainActivity.class);
                main.putExtra("imgPlayerOne", imgPlayerOne);
                main.putExtra("imgPlayerTwo", imgPlayerTwo);
                startActivity(main);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.playerOneImage)
            setPlayerImageOne = true;
        else
            setPlayerImageOne = false;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE);
            }else{
                Toast.makeText(this, "Denied permission for camera! Allow it on settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                Log.d(data.toString(), data.toString());
                Bitmap image = (Bitmap) data.getExtras().get("data");
                if(setPlayerImageOne) {
                    imgPlayerOne = image;
                    playerOneImage.setImageBitmap(image);
                }
                else{
                    imgPlayerTwo = image;
                    playerTwoImage.setImageBitmap(image);
                }

            }
        }
    }
}