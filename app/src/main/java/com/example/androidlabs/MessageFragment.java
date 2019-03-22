package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private boolean isTablet;
    private Bundle data;
    private long id;
    private int position;
    private boolean wasSent;

    public void setTablet(boolean isTablet){

        this.isTablet = isTablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        data = getArguments();
        id = data.getLong(ChatActivity.ITEM_ID);
        position = data.getInt(ChatActivity.ITEM_POSITION);
        wasSent = data.getBoolean(ChatActivity.ITEM_BOOLEAN);

        View frameView = inflater.inflate(R.layout.message_detail, container, false);

        TextView message = frameView.findViewById(R.id.textview_message);
        message.setText(data.getString(ChatActivity.ITEM));

        TextView messageID = frameView.findViewById(R.id.message_id);
        messageID.setText(String.valueOf(id));

        TextView messageType = frameView.findViewById(R.id.message_type);

        if(wasSent)
            messageType.setText(R.string.Lab8_message_type_Sent);
        else
            messageType.setText(R.string.Lab8_message_type_Received);

        Button deleteButton = frameView.findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> {

            if(isTablet){

                ChatActivity parent = (ChatActivity)getActivity();
                parent.deleteMessage((int)id, position);

                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            } else {

                Activity parent = getActivity();

                Intent backToChat = new Intent();
                backToChat.putExtra(ChatActivity.ITEM_POSITION, data.getInt(ChatActivity.ITEM_POSITION));
                backToChat.putExtra(ChatActivity.ITEM_ID, data.getLong(ChatActivity.ITEM_ID));
                parent.setResult(Activity.RESULT_OK, backToChat);

                parent.finish();

            }
        });

        return frameView;
    }
}
