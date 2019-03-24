package com.example.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ListView chatWindow;
    private ArrayList<Message> messages;
    private ChatAdapter adapter;
    private DBOpenHelper dbOpener;
    private SQLiteDatabase db;
    public static final String ITEM = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_BOOLEAN = "TYPE";
    public static final int MESSAGE_DETAIL_ACTIVITY = 300;
    public static final String FRAGMENT = "MessageDetailFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState){

        /* Standard method calls */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room_layout);

        /* Initialize objects */
        messages = new ArrayList<>();

        chatWindow = findViewById(R.id.chatListView);
        boolean isTablet = findViewById(R.id.frame_layout) != null;

        SwipeRefreshLayout refresher = findViewById(R.id.chatRefresher);

        Button sendButton = findViewById(R.id.chatSendButton);
        Button receiveButton = findViewById(R.id.chatReceiveButton);

        /* Retrieve database and load ListView messages */
        dbOpener = new DBOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        String[] columns = {DBOpenHelper.COL_ID, DBOpenHelper.COL_MESSAGE, DBOpenHelper.COL_BOOLEAN};
        Cursor results = db.query(false, DBOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results);
        results.moveToFirst();

        int idIndex = results.getColumnIndex(DBOpenHelper.COL_ID);
        int messageIndex = results.getColumnIndex(DBOpenHelper.COL_MESSAGE);
        int booleanIndex = results.getColumnIndex(DBOpenHelper.COL_BOOLEAN);

        do {

            if(!results.isAfterLast()) {
                long id = results.getLong(idIndex);
                String message = results.getString(messageIndex);
                boolean wasSent = true;

                if (results.getInt(booleanIndex) == 0)
                    wasSent = false;

                messages.add(new Message(message, wasSent, id));
            }
        } while(results.moveToNext());


        adapter = new ChatAdapter(messages, this);
        chatWindow.setAdapter(adapter);

        chatWindow.setOnItemClickListener((list, view, position, id) -> {

            Bundle data = new Bundle();
            data.putString(ITEM, messages.get(position).getMessage());
            data.putInt(ITEM_POSITION, position);
            data.putLong(ITEM_ID, id);
            data.putBoolean(ITEM_BOOLEAN, messages.get(position).wasSent());

            if(isTablet){

                FragmentManager manager = getSupportFragmentManager();

                Fragment tempFragment = manager.findFragmentByTag(FRAGMENT);
                manager.popBackStack();

                if(tempFragment != null){

                    manager.beginTransaction().remove(tempFragment).commit();
                }

                /*
                if(manager.getBackStackEntryCount() >= 1){

                    manager.popBackStack();

                    List<Fragment> fragments = manager.getFragments();
                    FragmentTransaction trans = manager.beginTransaction();

                    for(Fragment existingFragment: fragments) {
                        trans.remove(existingFragment);
                    }

                    trans.commit();
                }
*/
                MessageFragment fragment = new MessageFragment();
                fragment.setArguments(data);
                fragment.setTablet(true);

                manager.beginTransaction()
                        .add(R.id.frame_layout, fragment)
                        .addToBackStack(FRAGMENT)
                        .commit();


            } else {

                Intent messageDetail = new Intent(ChatActivity.this, MessageDetailActivity.class);
                messageDetail.putExtras(data);
                startActivityForResult(messageDetail, MESSAGE_DETAIL_ACTIVITY);
            }

        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == MESSAGE_DETAIL_ACTIVITY){

            if(resultCode == Activity.RESULT_OK){

               long id = data.getLongExtra(ITEM_ID, 0);
               int position = data.getIntExtra(ITEM_POSITION, 0);
               deleteMessage((int)id, position);
            }

        }
    }

    public void deleteMessage(int id, int position){

        Log.i("Deleted message", "Message ID = "+ messages.get(position).getId());
        messages.remove(position);
        dbOpener.delete(db, id);
        adapter.notifyDataSetChanged();
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
        int idIndex = cursor.getColumnIndex(DBOpenHelper.COL_ID);
        int messageIndex = cursor.getColumnIndex(DBOpenHelper.COL_MESSAGE);
        int wasSentIndex = cursor.getColumnIndex(DBOpenHelper.COL_BOOLEAN);


        Log.e("CursorDebugStart", "\t*************************** Start Cursor Debug ***************************");
        Log.e("DB_Version", "\t\t|DB Version number: "+ DBOpenHelper.VERSION_NUMBER);
        Log.e("Column_Number", "\t|Column count: "+ cursor.getColumnCount());

         for(int i = 0; i < numColumns; i++){
           Log.e("Column"+ (i+1), "\t\t\t|Column "+ (i+1) +" "+ columns[i]);
        }

        Log.e("Result_Count", "\t\t|Result count: "+ cursor.getCount());

       int i = 1;
       Message message = null;

       while(cursor.moveToNext()){

           if(cursor.getInt(wasSentIndex) == 0)
            message = new Message(cursor.getString(messageIndex), false, cursor.getLong(0));
           else if(cursor.getInt(wasSentIndex) == 1)
               message = new Message(cursor.getString(messageIndex), true, cursor.getLong(0));

           Log.e("Result "+i++, "\t\t\t|Result "+ (i+1)+ ": Id = "+ message.getId() + " | wasSent = " + String.valueOf(message.wasSent()) + " | Message = " + message.getMessage());
       }

        Log.e("CursorDebugEnd", "\t*************************** End Cursor Debug ***************************");
    }

}
