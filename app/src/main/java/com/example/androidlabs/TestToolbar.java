package com.example.androidlabs;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    String toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        toastMessage = getString(R.string.Lab6_toastMessage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lab6toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch(item.getItemId()){

            /* Displays a toast */
            case R.id.menuDisplay:
                Toast.makeText(this, R.string.Lab6_overflow, Toast.LENGTH_SHORT).show();
                break;

            /* Displays a toast with the value of toastMessage */
            case R.id.menuInfo:
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                break;

            /* Sets the value of toastMessage */
            case R.id.menuBubble:
                buildDialog();
                break;

            /* Display a snackbar to return to the previous activity */
            case R.id.menuExit:
                LinearLayout layout = findViewById(R.id.lab6ToolbarLayout);
                Snackbar.make(layout,"Go back?", Snackbar.LENGTH_SHORT).setAction("Go back", e -> finish() ).show();
                break;

        }


        return true;
    }

    private void buildDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        /* Inflating the dialog box layout we created */
        View dialogLayout = inflater.inflate(R.layout.lab6dialogbox, null);
        LinearLayout layout = dialogLayout.findViewById(R.id.Lab6_dialogLayout);

        builder.setView(layout);

        builder.setPositiveButton(R.string.Lab6_dialog_positive, (dialog, id) -> {
            EditText textbox = (EditText) layout.getChildAt(2);
            toastMessage = textbox.getText().toString();
        });

        builder.setNegativeButton(R.string.Lab6_dialog_negative, (dialog, id) -> {
            /* Nothing happens when the user clicks cancel */
        });

        builder.create().show();
    }
}
