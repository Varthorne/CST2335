package com.example.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.androidlabs.R.layout.activity_main_grid;
import static com.example.androidlabs.R.layout.activity_main_linear;
import static com.example.androidlabs.R.layout.activity_main_relative;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main_linear);
    }
}
