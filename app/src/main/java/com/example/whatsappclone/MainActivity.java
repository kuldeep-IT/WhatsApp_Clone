package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSingUpEmail, edtSingUpUserName, edtSingUpPassword;
    private Button btnSingUp, btnSingUpLogin;
    private ImageView rootOfLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSingUpEmail= findViewById(R.id.edtSingUpEmail);
        edtSingUpUserName = findViewById(R.id.edtSingUpName);
        edtSingUpPassword = findViewById(R.id.edtSingUpPassWord);

        btnSingUp = findViewById(R.id.btnSingUp);
        btnSingUpLogin = findViewById(R.id.btnSingUpLogin);

        rootOfLayout = findViewById(R.id.root);

        btnSingUpLogin.setOnClickListener(MainActivity.this);
        btnSingUp.setOnClickListener(MainActivity.this);

        edtSingUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSingUp);
                }

                return false;
            }
        });

        if (ParseUser.getCurrentUser() != null)
        {
            moveToWhatsAppUsers();
        }

    }

    public void root(View view) {

        try {
            InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSingUp:

                if (TextUtils.isEmpty(edtSingUpEmail.getText().toString())
                        || TextUtils.isEmpty(edtSingUpUserName.getText().toString() )
                        || TextUtils.isEmpty(edtSingUpPassword.getText().toString()))
                {
                    FancyToast.makeText(MainActivity.this, "please fill the data ", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
//                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(edtSingUpUserName.getText().toString());
                    parseUser.setEmail(edtSingUpEmail.getText().toString());
                    parseUser.setPassword(edtSingUpPassword.getText().toString());

                    ProgressDialog pd=new ProgressDialog(MainActivity.this);
                    pd.setMessage("Please wait... " + edtSingUpUserName.getText().toString());
                    pd.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                            {
                                FancyToast.makeText(MainActivity.this, "Voila ! registered as " + parseUser.get("username"), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//                                Toast.makeText(MainActivity.this, "Voila ! registered as " + parseUser.get("username"), Toast.LENGTH_SHORT).show();
                                moveToWhatsAppUsers();

                            }
                            else
                            {
                                FancyToast.makeText(MainActivity.this, e.getMessage()+"", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
//                                Toast.makeText(MainActivity.this,e.getMessage() +"", Toast.LENGTH_SHORT).show();

                            }
                            pd.dismiss();
                        }
                    });
                }

                break;

            case R.id.btnSingUpLogin:

                Intent i1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i1);

                break;
        }

    }

    private void moveToWhatsAppUsers()
    {
        Intent i1= new Intent(MainActivity.this,WhatsAppUser.class);
        startActivity(i1);
        finish();
    }
}