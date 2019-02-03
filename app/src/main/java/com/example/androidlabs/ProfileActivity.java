package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton imageButton;
    public static final String ACTIVITY_NAME = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        EditText emailField = findViewById(R.id.profileEdit2);
        SharedPreferences sp = getSharedPreferences("Lab3Prefs", Context.MODE_PRIVATE);

        String emailString = sp.getString("userEmail", "Email");

        emailField.setText(emailString);

        imageButton = findViewById(R.id.profilePictureButton);
        imageButton.setOnClickListener( e -> {

            dispatchTakePictureIntent();
        });

        Log.e(ACTIVITY_NAME, "In function:" + "onCreate");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }

        Log.e(ACTIVITY_NAME, "In function:" + "onActivityResult");
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RequestCode.REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.e(ACTIVITY_NAME, "In function:" + "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();

        Log.e(ACTIVITY_NAME, "In function:" + "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();

        Log.e(ACTIVITY_NAME, "In function:" + "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.e(ACTIVITY_NAME, "In function:" + "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.e(ACTIVITY_NAME, "In function:" + "onDestroy");
    }

}
