package com.example.androidlabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<Message> messages;
    private Context context;

    protected ChatAdapter(ArrayList array, Context context){
        this.messages = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {

        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

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
