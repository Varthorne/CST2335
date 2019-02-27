package com.example.androidlabs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ListView chatWindow;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        /* Standard method calls */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_layout);

        /* Initialize objects */
        messages = new ArrayList<>();
        chatWindow = findViewById(R.id.chatListView);
        //SwipeRefreshLayout refresher = findViewById(R.id.chatRefresher);
        ChatAdapter adapter = new ChatAdapter(messages);
        Button sendButton = findViewById(R.id.chatSendButton);
        Button receiveButton = findViewById(R.id.chatReceiveButton);

        chatWindow.setAdapter(adapter);

        /* Add Listeners */
        sendButton.setOnClickListener(e -> {

            EditText chatEditText = findViewById(R.id.chatEditText);

            if(!(chatEditText.getText().toString().equals(""))) {
                messages.add(new Message(chatEditText.getText().toString(), true, 0));
                adapter.notifyDataSetChanged();
                chatEditText.setText("");
            }
        });

        receiveButton.setOnClickListener(e -> {



            EditText chatEditText = findViewById(R.id.chatEditText);

            if(!(chatEditText.getText().toString().equals(""))) {
                messages.add(new Message(chatEditText.getText().toString(), false, 0));
                adapter.notifyDataSetChanged();
                chatEditText.setText("");
            }
        });
        /*
        refresher.setOnRefreshListener(() -> {

            adapter.notifyDataSetChanged();
            refresher.setRefreshing(false);
        });
        */
    }


    protected class ChatAdapter extends BaseAdapter {

        private ArrayList<Message> messages;

        protected ChatAdapter(ArrayList array){
            this.messages = array;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {

            return messages.get(position);
        }

        @Override //Not currently in use
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            String message = messages.get(position).getMessage();
            boolean wasSent = messages.get(position).wasSent();
            View newView;

            if(wasSent) {
                newView = inflater.inflate(R.layout.send_layout, parent, false);
            } else {
                newView = inflater.inflate(R.layout.receive_layout, parent, false);
            }

            RelativeLayout layout = newView.findViewById(R.id.layout);

            TextView text = (TextView) layout.getChildAt(1);

            text.setText(message);

            return layout;
        }
    }

}
