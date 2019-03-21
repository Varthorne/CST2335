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
        setContentView(R.layout.chat_room_message_detail);

        data = getIntent().getExtras();

        TextView messageContent = findViewById(R.id.textview_message);
        messageContent.setText(data.getString(ChatActivity.ITEM));

        TextView messageID = findViewById(R.id.message_id);
        messageID.setText(String.valueOf(data.getLong(ChatActivity.ITEM_ID)));

        TextView messageType = findViewById(R.id.message_type);
        boolean wasSent = data.getBoolean(ChatActivity.ITEM_BOOLEAN);

        if(wasSent)
            messageType.setText("Sent");
        else
            messageType.setText("Received");


        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> {

            Intent backToChat = new Intent(MessageDetailActivity.this, ChatActivity.class);
            backToChat.putExtra(ChatActivity.ITEM_POSITION, data.getInt(ChatActivity.ITEM_POSITION));
            backToChat.putExtra(ChatActivity.ITEM_ID, data.getLong(ChatActivity.ITEM_ID));
            setResult(Activity.RESULT_OK, backToChat);

            finish();
        });


    }


}
