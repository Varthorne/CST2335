package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MessageDetailActivity extends AppCompatActivity {

    private boolean isTablet;
    private Bundle data;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_detail_activity_layout);

        Bundle data = getIntent().getExtras();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(data);
        fragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, fragment)
                .addToBackStack("Fragment")
                .commit();

    }


}
