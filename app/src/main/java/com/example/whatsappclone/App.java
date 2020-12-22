package com.example.whatsappclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UVFvzNJjZnqKhpK1SJuiZ4hqLpX8DtIljv3nPNrs")
                // if defined
                .clientKey("oqJt4srfEVEN0akMsVjdwP0CqtI7ZLMExeOR7dIw")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
