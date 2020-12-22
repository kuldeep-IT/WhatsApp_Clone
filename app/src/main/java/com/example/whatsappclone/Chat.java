package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;
    private ListView chatListView;

    private Button btnChatSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        chatListView = findViewById(R.id.chatsListView);
        btnChatSend = findViewById(R.id.btnChatSend);

        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(adapter);

        setTitle(selectedUser);

        try {
            ParseQuery<ParseObject> firstUserChatQuery=ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery=ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries=new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery=ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    if (objects.size() > 0 && e == null) {

                        for (ParseObject chatObject : objects) {
                            String waMessage=chatObject.get("waChat") + "";

                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage=ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }

                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage=selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnChatSend.setOnClickListener(Chat.this);


    }

    @Override
    public void onClick(View v) {

        EditText edtChat = findViewById(R.id.edtChat);

        ParseObject object = new ParseObject("Chat");
        object.put("waSender", ParseUser.getCurrentUser().getUsername());
        object.put("waTargetRecipient",selectedUser);
        object.put("waChat",edtChat.getText().toString());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                {
                    chatsList.add(ParseUser.getCurrentUser().getUsername()+": " + edtChat.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtChat.setText("");
                }

            }
        });

    }
}