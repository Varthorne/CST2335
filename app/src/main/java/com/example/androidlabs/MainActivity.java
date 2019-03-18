package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    EditText emailField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emailField = findViewById(R.id.EditTextEmail);
        sp = getSharedPreferences("Lab3Prefs", Context.MODE_PRIVATE);
        String emailString = sp.getString("userEmail", "Email");

        emailField.setText(emailString);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener( c -> {
            Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);

            startActivityForResult(profilePage, RequestCode.LOGIN);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainToolbar, menu);
*/
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = sp.edit();

        String emailTyped = emailField.getText().toString();
        editor.putString("userEmail", emailTyped);

        editor.commit();
    }
}
