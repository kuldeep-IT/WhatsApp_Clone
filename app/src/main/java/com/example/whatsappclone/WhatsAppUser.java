package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> waUser;
    private ListView listView;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_user);

        listView = findViewById(R.id.listView);
        waUser = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,waUser);
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                try {


                    if (objects.size() > 0 && e == null) {
                        for (ParseUser user : objects) {
                            waUser.add(user.getUsername());
                        }
                        listView.setAdapter(adapter);
                    }
                    else {
                        FancyToast.makeText(WhatsAppUser.this, e.getMessage() + "", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                swipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {

                                try {

                                    ParseQuery<ParseUser> parseQuery1 = ParseUser.getQuery();
                                    parseQuery1.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

                                    parseQuery1.whereNotContainedIn("username",waUser);

                                    parseQuery1.findInBackground(new FindCallback<ParseUser>() {
                                        @Override
                                        public void done(List<ParseUser> objects, ParseException e) {

                                            if (objects.size() > 0 && e == null)
                                            {
                                                for (ParseUser user : objects)
                                                {
                                                    waUser.add(user.getUsername());
                                                }
                                                adapter.notifyDataSetChanged();

                                                if (swipeRefreshLayout.isRefreshing())
                                                {
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }
                                            }
                                            else
                                            {
                                                if (swipeRefreshLayout.isRefreshing())
                                                {
                                                    swipeRefreshLayout.setRefreshing(false);
                                                }
                                            }

                                        }
                                    });


                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }


                            }
                        }
                );

            }
        });


        listView.setOnItemClickListener(WhatsAppUser.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logOut:

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent i1 = new Intent(WhatsAppUser.this,MainActivity.class);
                        startActivity(i1);
                        finish();

                    }
                });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i1 = new Intent(WhatsAppUser.this,Chat.class);
        i1.putExtra("selectedUser",waUser.get(position));
        startActivity(i1);

    }
}