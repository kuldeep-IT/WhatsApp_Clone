package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginUserName, edtLoginPassWord;
    private Button btnLoginButton, btnSingUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginUserName = findViewById(R.id.edtLoginUserName);
        edtLoginPassWord = findViewById(R.id.edtLoginPassWord);

        btnLoginButton = findViewById(R.id.loginButton);
        btnSingUpButton = findViewById(R.id.btnSingupButton);

        btnLoginButton.setOnClickListener(LoginActivity.this);
        btnSingUpButton.setOnClickListener(LoginActivity.this);

        edtLoginPassWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    btnLoginButton.setOnClickListener(LoginActivity.this);
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginButton:

                ParseUser.logInInBackground(edtLoginUserName.getText().toString(),
                        edtLoginPassWord.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if (user != null && e == null)
                                {
                                    FancyToast.makeText(LoginActivity.this,"Welcome to WhatsApp Clone " + ParseUser.getCurrentUser().getUsername(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

                                    moveToWhatsAppUsers();


                                }
                                else
                                {
                                    FancyToast.makeText(LoginActivity.this,"Error: " + e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                }

                            }
                        });

                break;

            case R.id.btnSingupButton:

                Intent i2 = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i2);

                break;


        }


    }

    private void moveToWhatsAppUsers()
    {
        Intent i1= new Intent(LoginActivity.this,WhatsAppUser.class);
        startActivity(i1);
        finish();
    }
}