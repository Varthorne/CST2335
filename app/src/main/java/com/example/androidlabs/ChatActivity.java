package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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


        SwipeRefreshLayout refresher = findViewById(R.id.chatRefresher);

        Button sendButton = findViewById(R.id.chatSendButton);
        Button receiveButton = findViewById(R.id.chatReceiveButton);


        /* Retrieve database and load ListView messages */
        DBOpenHelper dbOpener = new DBOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String[] columns = {DBOpenHelper.COL_ID, DBOpenHelper.COL_MESSAGE, DBOpenHelper.COL_BOOLEAN};
        Cursor results = db.query(false, DBOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results );
        results.moveToFirst();

        int idIndex = results.getColumnIndex(DBOpenHelper.COL_ID);
        int messageIndex = results.getColumnIndex(DBOpenHelper.COL_MESSAGE);
        int booleanIndex = results.getColumnIndex(DBOpenHelper.COL_BOOLEAN);

        do {

            long id = results.getLong(idIndex);
            String message = results.getString(messageIndex);
            boolean wasSent = true;

            if(results.getInt(booleanIndex) == 0)
                wasSent = false;

            messages.add(new Message(message, wasSent, id));
        } while(results.moveToNext());


        ChatAdapter adapter = new ChatAdapter(messages, this);
        chatWindow.setAdapter(adapter);

        /* Add Listeners */
        sendButton.setOnClickListener(e -> {

            EditText chatEditText = findViewById(R.id.chatEditText);

            if(!(chatEditText.getText().toString().equals(""))) {

                Message message = new Message(chatEditText.getText().toString(), true);

                createMessage(message, db);

                adapter.notifyDataSetChanged();
                chatEditText.setText("");
            }
        });

        receiveButton.setOnClickListener(e -> {



            EditText chatEditText = findViewById(R.id.chatEditText);

            if(!(chatEditText.getText().toString().equals(""))) {

                Message message = new Message(chatEditText.getText().toString(), false);

                createMessage(message, db);

                adapter.notifyDataSetChanged();
                chatEditText.setText("");
            }
        });

        refresher.setOnRefreshListener(() -> {

            adapter.notifyDataSetChanged();
            refresher.setRefreshing(false);
        });

    }

    private void createMessage(Message message, SQLiteDatabase db){

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COL_MESSAGE, message.getMessage());

        if(message.wasSent() == true)
            values.put(DBOpenHelper.COL_BOOLEAN, 1);
        else
            values.put(DBOpenHelper.COL_BOOLEAN, 0);

        long newId = db.insert(DBOpenHelper.TABLE_NAME, null, values);
        message.setId(newId);

        messages.add(message);
    }

    private void printCursor(Cursor cursor){

        String[] columns = cursor.getColumnNames();
        int numColumns = columns.length;

        Log.e("CursorDebugStart", "\t*************************** Start Cursor Debug ***************************");
        Log.e("DB_Version", "\t\t|DB Version number: "+ DBOpenHelper.VERSION_NUMBER);
        Log.e("Column_Number", "\t|Column count: "+ cursor.getColumnCount());

         for(int i = 0; i < numColumns; i++){
           Log.e("Column"+ (i+1), "\t\t\t|Column "+ (i+1) +columns[i]);
        }

        Log.e("Result_Count", "\t\t|Result count: "+ cursor.getCount());

       int i = 1;
       Message message = null;

       while(cursor.moveToNext()){

           if(cursor.getInt(1) == 0)
            message = new Message(cursor.getString(1), false, cursor.getLong(0));
           else if(cursor.getInt(1) == 1)
               message = new Message(cursor.getString(1), true, cursor.getLong(0));

           Log.e("Result "+i++, "\t\t\t|Result "+ (i+1)+ ": Id = "+ message.getId() + " | wasSent = " + message.wasSent() + " | Message = " + message.getMessage());
       }

        Log.e("CursorDebugEnd", "\t*************************** End Cursor Debug ***************************");
    }

}
